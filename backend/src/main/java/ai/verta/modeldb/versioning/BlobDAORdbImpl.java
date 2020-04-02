package ai.verta.modeldb.versioning;

import static java.util.stream.Collectors.toMap;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.authservice.AuthService;
import ai.verta.modeldb.entities.versioning.CommitEntity;
import ai.verta.modeldb.entities.versioning.InternalFolderElementEntity;
import ai.verta.modeldb.entities.versioning.RepositoryEntity;
import ai.verta.modeldb.utils.ModelDBHibernateUtil;
import ai.verta.modeldb.versioning.DiffStatusEnum.DiffStatus;
import ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model.AutogenBlob;
import ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model.AutogenBlobDiff;
import ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model.AutogenDiffStatusEnumDiffStatus;
import ai.verta.modeldb.versioning.blob.container.BlobContainer;
import ai.verta.modeldb.versioning.blob.diff.DiffComputer;
import ai.verta.modeldb.versioning.blob.diff.DiffMerger;
import ai.verta.modeldb.versioning.blob.diff.TypeChecker;
import ai.verta.modeldb.versioning.blob.factory.BlobFactory;
import ai.verta.uac.UserInfo;
import com.google.protobuf.ProtocolStringList;
import io.grpc.Status;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class BlobDAORdbImpl implements BlobDAO {

  private static final Logger LOGGER = LogManager.getLogger(BlobDAORdbImpl.class);

  public static final String TREE = "TREE";
  private final AuthService authService;

  public BlobDAORdbImpl(AuthService authService) {
    this.authService = authService;
  }

  /**
   * Goes through each BlobExpanded creating TREE/BLOB node top down and computing SHA bottom up
   * there is a rootSHA which holds one TREE node of each BlobExpanded
   *
   * @throws ModelDBException
   */
  @Override
  public String setBlobs(Session session, List<BlobContainer> blobContainers, FileHasher fileHasher)
      throws NoSuchAlgorithmException, ModelDBException {
    TreeElem rootTree = new TreeElem();
    Set<String> blobHashes = new HashSet<>();
    for (BlobContainer blobContainer : blobContainers) {
      // should save each blob during one session to avoid recurring entities ids
      blobContainer.process(session, rootTree, fileHasher, blobHashes);
    }
    Set<String> hashes = new HashSet<>();
    final InternalFolderElement internalFolderElement =
        rootTree.saveFolders(session, fileHasher, hashes);
    return internalFolderElement.getElementSha();
  }

  private ai.verta.modeldb.versioning.Blob getBlob(
      Session session, InternalFolderElementEntity folderElementEntity) throws ModelDBException {
    return BlobFactory.create(folderElementEntity).getBlob(session);
  }

  private Folder getFolder(Session session, String commitSha, String folderSha) {
    Optional result =
        session
            .createQuery(
                "From "
                    + InternalFolderElementEntity.class.getSimpleName()
                    + " where folder_hash = '"
                    + folderSha
                    + "'")
            .list().stream()
            .map(
                d -> {
                  InternalFolderElementEntity entity = (InternalFolderElementEntity) d;
                  Folder.Builder folder = Folder.newBuilder();
                  FolderElement.Builder folderElement =
                      FolderElement.newBuilder()
                          .setElementName(entity.getElement_name())
                          .setCreatedByCommit(commitSha);

                  if (entity.getElement_type().equals(TREE)) {
                    folder.addSubFolders(folderElement);
                  } else {
                    folder.addBlobs(folderElement);
                  }
                  return folder.build();
                })
            .reduce((a, b) -> ((Folder) a).toBuilder().mergeFrom((Folder) b).build());

    if (result.isPresent()) {
      return (Folder) result.get();
    } else {
      return null;
    }
  }

  // TODO : check if there is a way to optimize on the calls to data base.
  // We should fetch data  in a single query.
  @Override
  public GetCommitComponentRequest.Response getCommitComponent(
      RepositoryFunction repositoryFunction, String commitHash, ProtocolStringList locationList)
      throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();
      RepositoryEntity repository = repositoryFunction.apply(session);
      CommitEntity commit = session.get(CommitEntity.class, commitHash);

      if (commit == null) {
        throw new ModelDBException("No such commit", Status.Code.NOT_FOUND);
      }

      if (!VersioningUtils.commitRepositoryMappingExists(session, commitHash, repository.getId())) {
        throw new ModelDBException("No such commit found in the repository", Status.Code.NOT_FOUND);
      }

      String folderHash = commit.getRootSha();
      if (locationList.isEmpty()) { // getting root
        Folder folder = getFolder(session, commit.getCommit_hash(), folderHash);
        session.getTransaction().commit();
        if (folder == null) { // root is empty
          return GetCommitComponentRequest.Response.newBuilder().build();
        }
        return GetCommitComponentRequest.Response.newBuilder().setFolder(folder).build();
      }
      for (int index = 0; index < locationList.size(); index++) {
        String folderLocation = locationList.get(index);
        String folderQueryHQL =
            "From "
                + InternalFolderElementEntity.class.getSimpleName()
                + " parentIfe WHERE parentIfe.element_name = :location AND parentIfe.folder_hash = :folderHash";
        Query<InternalFolderElementEntity> fetchTreeQuery = session.createQuery(folderQueryHQL);
        fetchTreeQuery.setParameter("location", folderLocation);
        fetchTreeQuery.setParameter("folderHash", folderHash);
        InternalFolderElementEntity elementEntity = fetchTreeQuery.uniqueResult();

        if (elementEntity == null) {
          LOGGER.warn(
              "No such folder found : {}. Failed at index {} looking for {}",
              folderLocation,
              index,
              folderLocation);
          throw new ModelDBException(
              "No such folder found : " + folderLocation, Status.Code.NOT_FOUND);
        }
        if (elementEntity.getElement_type().equals(TREE)) {
          folderHash = elementEntity.getElement_sha();
          if (index == locationList.size() - 1) {
            Folder folder = getFolder(session, commit.getCommit_hash(), folderHash);
            session.getTransaction().commit();
            if (folder == null) { // folder is empty
              return GetCommitComponentRequest.Response.newBuilder().build();
            }
            return GetCommitComponentRequest.Response.newBuilder().setFolder(folder).build();
          }
        } else {
          if (index == locationList.size() - 1) {
            ai.verta.modeldb.versioning.Blob blob = getBlob(session, elementEntity);
            session.getTransaction().commit();
            return GetCommitComponentRequest.Response.newBuilder().setBlob(blob).build();
          } else {
            throw new ModelDBException(
                "No such folder found : " + locationList.get(index + 1), Status.Code.NOT_FOUND);
          }
        }
      }
    } catch (Throwable throwable) {
      if (throwable instanceof ModelDBException) {
        throw (ModelDBException) throwable;
      }
      LOGGER.warn(throwable);
      throw new ModelDBException("Unknown error", Status.Code.INTERNAL);
    }
    throw new ModelDBException(
        "Unexpected logic issue found when fetching blobs", Status.Code.UNKNOWN);
  }

  /**
   * get the Folder Element pointed to by the parentFolderHash and elementName
   *
   * @param session
   * @param parentFolderHash : folder hash of the parent
   * @param elementName : element name of the element to be fetched
   * @return {@link List<InternalFolderElementEntity>}
   */
  private List<InternalFolderElementEntity> getFolderElement(
      Session session, String parentFolderHash, String elementName) {
    StringBuilder folderQueryHQLBuilder =
        new StringBuilder("From ")
            .append(InternalFolderElementEntity.class.getSimpleName())
            .append(" parentIfe WHERE parentIfe.folder_hash = :folderHash ");

    if (elementName != null && !elementName.isEmpty()) {
      folderQueryHQLBuilder.append("AND parentIfe.element_name = :elementName");
    }

    Query<InternalFolderElementEntity> fetchTreeQuery =
        session.createQuery(folderQueryHQLBuilder.toString());
    fetchTreeQuery.setParameter("folderHash", parentFolderHash);
    if (elementName != null && !elementName.isEmpty()) {
      fetchTreeQuery.setParameter("elementName", elementName);
    }
    return fetchTreeQuery.list();
  }

  boolean childContains(Set<?> list, Set<?> sublist) {
    return Collections.indexOfSubList(new LinkedList<>(list), new LinkedList<>(sublist)) != -1;
  }

  private Map<String, Map.Entry<BlobExpanded, String>> getChildFolderBlobMap(
      Session session,
      List<String> requestedLocation,
      Set<String> parentLocation,
      String parentFolderHash)
      throws ModelDBException {
    String folderQueryHQL =
        "From "
            + InternalFolderElementEntity.class.getSimpleName()
            + " parentIfe WHERE parentIfe.folder_hash = :folderHash";
    Query<InternalFolderElementEntity> fetchTreeQuery = session.createQuery(folderQueryHQL);
    fetchTreeQuery.setParameter("folderHash", parentFolderHash);
    List<InternalFolderElementEntity> childElementFolders = fetchTreeQuery.list();

    Map<String, Map.Entry<BlobExpanded, String>> childBlobExpandedMap = new LinkedHashMap<>();
    for (InternalFolderElementEntity childElementFolder : childElementFolders) {
      if (childElementFolder.getElement_type().equals(TREE)) {
        Set<String> childLocation = new LinkedHashSet<>(parentLocation);
        childLocation.add(childElementFolder.getElement_name());
        if (childContains(new LinkedHashSet<>(requestedLocation), childLocation)
            || childLocation.containsAll(requestedLocation)) {
          childBlobExpandedMap.putAll(
              getChildFolderBlobMap(
                  session, requestedLocation, childLocation, childElementFolder.getElement_sha()));
        }
      } else {
        if (parentLocation.containsAll(requestedLocation)) {
          ai.verta.modeldb.versioning.Blob blob = getBlob(session, childElementFolder);
          BlobExpanded blobExpanded =
              BlobExpanded.newBuilder()
                  .addAllLocation(parentLocation)
                  .addLocation(childElementFolder.getElement_name())
                  .setBlob(blob)
                  .build();
          childBlobExpandedMap.put(
              getStringFromLocationList(blobExpanded.getLocationList()),
              new AbstractMap.SimpleEntry<>(blobExpanded, childElementFolder.getElement_sha()));
        }
      }
    }
    return childBlobExpandedMap;
  }

  /**
   * Given a folderHash and a location list, collects all the blobs along the location list and
   * returns them with their location as set
   *
   * @param session
   * @param folderHash : the base folder to start the search for location list
   * @param locationList : list of trees and psossibly terminating with blob
   * @return
   * @throws ModelDBException
   */
  @Override
  public Map<String, BlobExpanded> getCommitBlobMap(
      Session session, String folderHash, List<String> locationList) throws ModelDBException {
    return convertToLocationBlobMap(getCommitBlobMapWithHash(session, folderHash, locationList));
  }

  private Map<String, BlobExpanded> convertToLocationBlobMap(
      Map<String, Map.Entry<BlobExpanded, String>> commitBlobMapWithHash) {
    return commitBlobMapWithHash.entrySet().stream()
        .collect(
            Collectors.toMap(
                Entry::getKey, stringEntryEntry -> stringEntryEntry.getValue().getKey()));
  }

  Map<String, Map.Entry<BlobExpanded, String>> getCommitBlobMapWithHash(
      Session session, String folderHash, List<String> locationList) throws ModelDBException {

    String parentLocation = locationList.size() == 0 ? null : locationList.get(0);
    List<InternalFolderElementEntity> parentFolderElementList =
        getFolderElement(session, folderHash, parentLocation);
    if (parentFolderElementList == null || parentFolderElementList.isEmpty()) {
      if (parentLocation
          != null) { // = null mainly is supporting the call on init commit which is an empty commit
        throw new ModelDBException(
            "No such folder found : " + parentLocation, Status.Code.NOT_FOUND);
      }
    }

    Map<String, Map.Entry<BlobExpanded, String>> finalLocationBlobMap = new LinkedHashMap<>();
    for (InternalFolderElementEntity parentFolderElement : parentFolderElementList) {
      if (!parentFolderElement.getElement_type().equals(TREE)) {
        ai.verta.modeldb.versioning.Blob blob = getBlob(session, parentFolderElement);
        BlobExpanded blobExpanded =
            BlobExpanded.newBuilder()
                .addLocation(parentFolderElement.getElement_name())
                .setBlob(blob)
                .build();
        finalLocationBlobMap.put(
            getStringFromLocationList(blobExpanded.getLocationList()),
            new SimpleEntry<>(blobExpanded, parentFolderElement.getElement_sha()));
      } else {
        // if this is tree, search further
        Set<String> location = new LinkedHashSet<>();
        Map<String, Map.Entry<BlobExpanded, String>> locationBlobList =
            getChildFolderBlobMap(session, locationList, location, folderHash);
        finalLocationBlobMap.putAll(locationBlobList);
      }
    }

    Comparator<Map.Entry<String, Map.Entry<BlobExpanded, String>>> locationComparator =
        Comparator.comparing(
            (Map.Entry<String, Map.Entry<BlobExpanded, String>> o) ->
                o.getKey().replaceAll("#", ""));

    finalLocationBlobMap =
        finalLocationBlobMap.entrySet().stream()
            .sorted(locationComparator)
            .collect(
                toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

    return finalLocationBlobMap;
  }

  @Override
  public ListCommitBlobsRequest.Response getCommitBlobsList(
      RepositoryFunction repositoryFunction, String commitHash, List<String> locationList)
      throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();

      CommitEntity commit = session.get(CommitEntity.class, commitHash);
      if (commit == null) {
        throw new ModelDBException("No such commit", Status.Code.NOT_FOUND);
      }

      RepositoryEntity repository = repositoryFunction.apply(session);
      if (!VersioningUtils.commitRepositoryMappingExists(session, commitHash, repository.getId())) {
        throw new ModelDBException("No such commit found in the repository", Status.Code.NOT_FOUND);
      }
      Map<String, BlobExpanded> locationBlobMap =
          getCommitBlobMap(session, commit.getRootSha(), locationList);
      return ListCommitBlobsRequest.Response.newBuilder()
          .addAllBlobs(locationBlobMap.values())
          .build();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      if (throwable instanceof ModelDBException) {
        throw (ModelDBException) throwable;
      }
      throw new ModelDBException("Unknown error", Status.Code.INTERNAL);
    }
  }

  @Override
  public ComputeRepositoryDiffRequest.Response computeRepositoryDiff(
      RepositoryFunction repositoryFunction, ComputeRepositoryDiffRequest request)
      throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      session.beginTransaction();
      RepositoryEntity repositoryEntity = repositoryFunction.apply(session);

      CommitEntity internalCommitA = session.get(CommitEntity.class, request.getCommitA());
      if (internalCommitA == null) {
        throw new ModelDBException(
            "No such commit found : " + request.getCommitA(), Status.Code.NOT_FOUND);
      }

      CommitEntity internalCommitB = session.get(CommitEntity.class, request.getCommitB());
      if (internalCommitB == null) {
        throw new ModelDBException(
            "No such commit found : " + request.getCommitB(), Status.Code.NOT_FOUND);
      }

      if (!VersioningUtils.commitRepositoryMappingExists(
          session, internalCommitA.getCommit_hash(), repositoryEntity.getId())) {
        throw new ModelDBException(
            "No such commit found in the repository : " + internalCommitA.getCommit_hash(),
            Status.Code.NOT_FOUND);
      }

      if (!VersioningUtils.commitRepositoryMappingExists(
          session, internalCommitB.getCommit_hash(), repositoryEntity.getId())) {
        throw new ModelDBException(
            "No such commit found in the repository : " + internalCommitB.getCommit_hash(),
            Status.Code.NOT_FOUND);
      }

      if (request.getReplaceAWithCommonAncestor()) {
        internalCommitA =
            getCommonParent(
                session, internalCommitA.getCommit_hash(), internalCommitB.getCommit_hash());
      }
      // get list of blob expanded in both commit and group them in a map based on location
      Map<String, Map.Entry<BlobExpanded, String>> locationBlobsMapCommitA =
          getCommitBlobMapWithHash(session, internalCommitA.getRootSha(), new ArrayList<>());

      Map<String, Map.Entry<BlobExpanded, String>> locationBlobsMapCommitB =
          getCommitBlobMapWithHash(session, internalCommitB.getRootSha(), new ArrayList<>());

      session.getTransaction().commit();
      return computeDiffFromCommitMaps(locationBlobsMapCommitA, locationBlobsMapCommitB);
    }
  }

  private ComputeRepositoryDiffRequest.Response computeDiffFromCommitMaps(
      Map<String, Map.Entry<BlobExpanded, String>> locationBlobsMapCommitA,
      Map<String, Map.Entry<BlobExpanded, String>> locationBlobsMapCommitB) {
    // Added new blob location in the CommitB, locations in
    Set<String> addedLocations = new LinkedHashSet<>(locationBlobsMapCommitB.keySet());
    addedLocations.removeAll(locationBlobsMapCommitA.keySet());
    LOGGER.debug("Added location for Diff : {}", addedLocations);

    // deleted new blob location from the CommitA
    Set<String> deletedLocations = new LinkedHashSet<>(locationBlobsMapCommitA.keySet());
    deletedLocations.removeAll(locationBlobsMapCommitB.keySet());
    LOGGER.debug("Deleted location for Diff : {}", deletedLocations);

    // get B sha -> blobs
    Map<String, Set<BlobExpanded>> blobsB = getCollectToMap(locationBlobsMapCommitB);
    // get A sha -> blobs
    Map<String, Set<BlobExpanded>> blobsA = getCollectToMap(locationBlobsMapCommitA);
    // delete blobs same with A
    for (Map.Entry<String, Set<BlobExpanded>> entry : blobsA.entrySet()) {
      Set<BlobExpanded> ent = blobsB.get(entry.getKey());
      if (ent != null) {
        ent.removeAll(entry.getValue());
      }
    }
    // get modified location -> blob
    Map<String, BlobExpanded> locationBlobsModified =
        getLocationWiseBlobExpandedMapFromCollection(
            blobsB.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));
    // remove added from modified
    locationBlobsModified.keySet().removeAll(addedLocations);
    Set<String> modifiedLocations = locationBlobsModified.keySet();
    LOGGER.debug("Modified location for Diff : {}", modifiedLocations);

    List<ai.verta.modeldb.versioning.BlobDiff> addedBlobDiffList =
        getAddedBlobDiff(addedLocations, convertToLocationBlobMap(locationBlobsMapCommitB));
    List<ai.verta.modeldb.versioning.BlobDiff> deletedBlobDiffList =
        getDeletedBlobDiff(deletedLocations, convertToLocationBlobMap(locationBlobsMapCommitA));
    List<ai.verta.modeldb.versioning.BlobDiff> modifiedBlobDiffList =
        getModifiedBlobDiff(
            modifiedLocations,
            convertToLocationBlobMap(locationBlobsMapCommitA),
            convertToLocationBlobMap(locationBlobsMapCommitB));

    return ComputeRepositoryDiffRequest.Response.newBuilder()
        .addAllDiffs(addedBlobDiffList)
        .addAllDiffs(deletedBlobDiffList)
        .addAllDiffs(modifiedBlobDiffList)
        .build();
  }

  @Override
  public MergeRepositoryCommitsRequest.Response mergeCommit(
      RepositoryFunction repositoryFunction, MergeRepositoryCommitsRequest request)
      throws ModelDBException, NoSuchAlgorithmException {
	  Map<String, Map.Entry<BlobExpanded, String>> locationBlobsMapCommitA =
		        new HashMap<String, Map.Entry<BlobExpanded, String>>();
    Map<String, Map.Entry<BlobExpanded, String>> locationBlobsMapCommitB =
        new HashMap<String, Map.Entry<BlobExpanded, String>>();
    Map<String, Map.Entry<BlobExpanded, String>> locationBlobsMapParentCommit =
        new HashMap<String, Map.Entry<BlobExpanded, String>>();
    Map<String, BlobExpanded> locationBlobsMapCommitASimple = new HashMap<String, BlobExpanded>();
    CommitEntity internalCommitA;
    CommitEntity internalCommitB;
    CommitEntity parentCommit;
    Commit parentCommitProto;
    try (Session readSession = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      RepositoryEntity repositoryEntity = repositoryFunction.apply(readSession);

      internalCommitA = readSession.get(CommitEntity.class, request.getCommitShaA());
      if (internalCommitA == null) {
        throw new ModelDBException(
            "No such commit found : " + request.getCommitShaA(), Status.Code.NOT_FOUND);
      }

      internalCommitB = readSession.get(CommitEntity.class, request.getCommitShaB());
      if (internalCommitB == null) {
        throw new ModelDBException(
            "No such commit found : " + request.getCommitShaB(), Status.Code.NOT_FOUND);
      }

      if (!VersioningUtils.commitRepositoryMappingExists(
          readSession, internalCommitA.getCommit_hash(), repositoryEntity.getId())) {
        throw new ModelDBException(
            "No such commit found in the repository : " + internalCommitA.getCommit_hash(),
            Status.Code.NOT_FOUND);
      }

      if (!VersioningUtils.commitRepositoryMappingExists(
          readSession, internalCommitB.getCommit_hash(), repositoryEntity.getId())) {
        throw new ModelDBException(
            "No such commit found in the repository : " + internalCommitB.getCommit_hash(),
            Status.Code.NOT_FOUND);
      }

      parentCommit =
          getCommonParent(readSession, request.getCommitShaA(), request.getCommitShaB());
parentCommitProto = parentCommit.toCommitProto();
       locationBlobsMapCommitA =
          getCommitBlobMapWithHash(readSession, internalCommitA.getRootSha(), new ArrayList<>());

      for (Entry<String, Entry<BlobExpanded, String>> locationBlobsEntry :
          locationBlobsMapCommitA.entrySet()) {
        locationBlobsMapCommitASimple.put(
            locationBlobsEntry.getKey(), locationBlobsEntry.getValue().getKey());
      }

      locationBlobsMapCommitB =
          getCommitBlobMapWithHash(readSession, internalCommitB.getRootSha(), new ArrayList<>());

      locationBlobsMapParentCommit =
          getCommitBlobMapWithHash(readSession, parentCommit.getRootSha(), new ArrayList<>());
    }
    try (Session writeSession = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      writeSession.beginTransaction();
      List<ai.verta.modeldb.versioning.BlobDiff> diffB =
          computeDiffFromCommitMaps(locationBlobsMapParentCommit, locationBlobsMapCommitB)
              .getDiffsList();

      HashMap<String, List<String>> conflictLocationMap = new HashMap<String, List<String>>();

      List<BlobContainer> blobContainerList =
          getBlobContainers(diffB, locationBlobsMapCommitASimple, conflictLocationMap);

      if(conflictLocationMap.isEmpty()) {
      final String rootSha = setBlobs(writeSession, blobContainerList, new FileHasher());
      long timeCreated = new Date().getTime();
      List<String> parentSHAs = Arrays.asList(request.getCommitShaA(), request.getCommitShaB());
      List<CommitEntity> parentCommits = Arrays.asList(internalCommitA, internalCommitB);
      String mergeMessage = request.getContent().getMessage();
      if (mergeMessage.isEmpty()) {
        mergeMessage =
            "Merge "
                + request.getCommitShaA().substring(0, 7)
                + " into "
                + request.getCommitShaB().substring(0, 7);
      }
      UserInfo currentLoginUserInfo = authService.getCurrentLoginUserInfo();
      String author = authService.getVertaIdFromUserInfo(currentLoginUserInfo);
      final String commitSha =
          VersioningUtils.generateCommitSHA(parentSHAs, mergeMessage, timeCreated, author, rootSha);

      Commit internalCommit =
          Commit.newBuilder()
              .setDateCreated(timeCreated)
              .setAuthor(author)
              .setMessage(mergeMessage)
              .setCommitSha(commitSha)
              .build();
      CommitEntity commitEntity =
          new CommitEntity(
              repositoryFunction.apply(writeSession), parentCommits, internalCommit, rootSha);
      writeSession.saveOrUpdate(commitEntity);
      writeSession.getTransaction().commit();
      return MergeRepositoryCommitsRequest.Response.newBuilder()
          .setCommit(commitEntity.toCommitProto())
          .build();
      }
      else {

          List<ai.verta.modeldb.versioning.BlobDiff> diffA =
              computeDiffFromCommitMaps(locationBlobsMapParentCommit, locationBlobsMapCommitA)
                  .getDiffsList();
          List<BlobDiff> blobDiffList = getConflictDiff(diffA,diffB,conflictLocationMap);
          writeSession.getTransaction().commit();
    	  LOGGER.debug("conflict found", conflictLocationMap);
    	  return MergeRepositoryCommitsRequest.Response.newBuilder()
    			  .setCommonBase(parentCommitProto)
    			  .addAllConflicts(blobDiffList)
    			  .build();
      }
    }
  }

  private List<BlobDiff> getConflictDiff(List<BlobDiff> diffListA,
		List<BlobDiff> diffListB, HashMap<String, List<String>> conflictLocationMap) {
	List<BlobDiff> blobDiffList = new LinkedList<BlobDiff>();//TODO sort?
	HashMap<String, List<BlobDiff>> diffMapA = getLocationMapDiff(diffListA,conflictLocationMap.keySet());
	HashMap<String, List<BlobDiff>> diffMapB = getLocationMapDiff(diffListB,conflictLocationMap.keySet());
			
	for(Entry<String,List<String>> entry : conflictLocationMap.entrySet()) {
		LOGGER.debug(entry.getKey());
		LOGGER.debug(entry.getValue());
		List<BlobDiff> locSpecificBlobDiffA = diffMapA.getOrDefault(entry.getKey(), Collections.emptyList());
		List<BlobDiff> locSpecificBlobDiffB = diffMapB.getOrDefault(entry.getKey(), Collections.emptyList());
		BlobDiff.Builder diffBuilder = BlobDiff.newBuilder().setStatus(DiffStatus.CONFLICTED);
		if(!locSpecificBlobDiffA.isEmpty()) {
			diffBuilder.addAllLocation(locSpecificBlobDiffA.get(0).getLocationList());
		}  else {
			diffBuilder.addAllLocation(locSpecificBlobDiffB.get(0).getLocationList());	
		}
		blobDiffList.add(diffBuilder.build());
	}
	return blobDiffList;
}

private HashMap<String, List<BlobDiff>> getLocationMapDiff(List<BlobDiff> diffList, Set<String> interestSet) {
	HashMap<String, List<BlobDiff>> diffMap = new HashMap<String, List<BlobDiff>>();
	for(BlobDiff diff : diffList) {
		String locKey = getStringFromLocationList(diff.getLocationList());
		if(interestSet.contains(locKey)) {
			if(!diffMap.containsKey(locKey)) {
				diffMap.put(locKey, new LinkedList<BlobDiff>());
			}
			diffMap.get(locKey).add(diff);
		}
	}
	return diffMap;
}

private CommitEntity getCommonParent(Session session, String commitA, String commitB)
      throws ModelDBException {
    List<CommitEntity> parentCommitA = VersioningUtils.getParentCommits(session, commitA);
    List<CommitEntity> parentCommitB = VersioningUtils.getParentCommits(session, commitB);

    CommitEntity commonParent = null;
    int itrA = 0;
    int itrB = 0;
    // TODO : this algorithm does not require all the commits to be available before starting.
    while (itrA < parentCommitA.size() && itrB < parentCommitB.size()) {
      CommitEntity candidateA = parentCommitA.get(itrA);
      CommitEntity candidateB = parentCommitB.get(itrB);
      if (candidateA.getCommit_hash().equals(candidateB.getCommit_hash())) {
        return candidateA;
      } else if (candidateA.getDate_created() > candidateB.getDate_created()) {
        itrA++;
      } else {
        itrB++;
      }
    }
    // Should never happen, since we have the initial commit
    throw new ModelDBException("Could not find base commit for merge", Status.Code.INTERNAL);
  }

  private Map<String, Set<BlobExpanded>> getCollectToMap(
      Map<String, Entry<BlobExpanded, String>> locationBlobsMapCommit) {
    return locationBlobsMapCommit.values().stream()
        .collect(
            Collectors.toMap(
                Entry::getValue,
                entry -> new LinkedHashSet<>(Collections.singletonList(entry.getKey())),
                (m1, m2) -> {
                  LinkedHashSet<BlobExpanded> newHash = new LinkedHashSet<>(m1);
                  newHash.addAll(m2);
                  return newHash;
                },
                LinkedHashMap::new));
  }

  List<ai.verta.modeldb.versioning.BlobDiff> getAddedBlobDiff(
      Set<String> addedLocations, Map<String, BlobExpanded> locationBlobsMapCommitB) {
    return addedLocations.stream()
        .map(
            location -> {
              BlobExpanded blobExpanded = locationBlobsMapCommitB.get(location);
              AutogenBlobDiff diff =
                  DiffComputer.computeBlobDiff(null, fromBlobProto(blobExpanded));
              diff.setStatus(AutogenDiffStatusEnumDiffStatus.fromProto(DiffStatus.ADDED));
              diff.setLocation(blobExpanded.getLocationList());
              return diff.toProto().build();
            })
        .collect(Collectors.toList());
  }

  private AutogenBlob fromBlobProto(BlobExpanded blobExpanded) {
    return AutogenBlob.fromProto(blobExpanded.getBlob());
  }

  List<ai.verta.modeldb.versioning.BlobDiff> getDeletedBlobDiff(
      Set<String> deletedLocations, Map<String, BlobExpanded> locationBlobsMapCommitA) {
    return deletedLocations.stream()
        .map(
            location -> {
              BlobExpanded blobExpanded = locationBlobsMapCommitA.get(location);

              AutogenBlobDiff diff =
                  DiffComputer.computeBlobDiff(fromBlobProto(blobExpanded), null);
              diff.setStatus(AutogenDiffStatusEnumDiffStatus.fromProto(DiffStatus.DELETED));
              diff.setLocation(blobExpanded.getLocationList());
              return diff.toProto().build();
            })
        .collect(Collectors.toList());
  }

  List<ai.verta.modeldb.versioning.BlobDiff> getModifiedBlobDiff(
      Set<String> modifiedLocations,
      Map<String, BlobExpanded> locationBlobsMapCommitA,
      Map<String, BlobExpanded> locationBlobsMapCommitB) {
    return modifiedLocations.stream()
        .flatMap(
            location -> {
              BlobExpanded blobExpandedCommitA = locationBlobsMapCommitA.get(location);
              BlobExpanded blobExpandedCommitB = locationBlobsMapCommitB.get(location);
              final AutogenBlob a = fromBlobProto(blobExpandedCommitA);
              final AutogenBlob b = fromBlobProto(blobExpandedCommitB);
              if (TypeChecker.sameType(a, b)) {
                AutogenBlobDiff blobDiff = DiffComputer.computeBlobDiff(a, b);
                // diff can be null because the old hash computation could detect two same entities
                // evaluate to different sha because it evaluated the list it contains in random
                // order
                if (blobDiff != null) {
                  return Stream.of(
                      blobDiff
                          .setLocation(blobExpandedCommitA.getLocationList())
                          .setStatus(AutogenDiffStatusEnumDiffStatus.fromProto(DiffStatus.MODIFIED))
                          .toProto()
                          .build());
                }
                return null;
              } else {
                return Stream.of(
                    DiffComputer.computeBlobDiff(a, null)
                        .setLocation(blobExpandedCommitA.getLocationList())
                        .setStatus(AutogenDiffStatusEnumDiffStatus.fromProto(DiffStatus.DELETED))
                        .toProto()
                        .build(),
                    DiffComputer.computeBlobDiff(null, b)
                        .setLocation(blobExpandedCommitB.getLocationList())
                        .setStatus(AutogenDiffStatusEnumDiffStatus.fromProto(DiffStatus.ADDED))
                        .toProto()
                        .build());
              }
            })
        .collect(Collectors.toList());
  }

  private Map<String, BlobExpanded> getLocationWiseBlobExpandedMapFromCollection(
      Collection<BlobExpanded> blobExpandeds) {
    return blobExpandeds.stream()
        .collect(
            Collectors.toMap(
                // TODO: Here used the `#` for joining the locations but if folder locations contain
                // TODO: - the `#` then this functionality will break.
                blobExpanded -> getStringFromLocationList(blobExpanded.getLocationList()),
                blobExpanded -> blobExpanded));
  }

  private String getStringFromLocationList(List<String> locationList) {
    return String.join("#", locationList);
  }

  @Override
  public List<BlobContainer> convertBlobDiffsToBlobs(
      CreateCommitRequest request,
      RepositoryFunction repositoryFunction,
      CommitFunction commitFunction)
      throws ModelDBException {
    try (Session session = ModelDBHibernateUtil.getSessionFactory().openSession()) {
      RepositoryEntity repositoryEntity = repositoryFunction.apply(session);
      CommitEntity commitEntity = commitFunction.apply(session, session1 -> repositoryEntity);
      Map<String, BlobExpanded> locationBlobsMap =
          getCommitBlobMap(session, commitEntity.getRootSha(), new ArrayList<>());
      return getBlobContainers(request.getDiffsList(), locationBlobsMap, new HashMap<String, List<String>>());
    }
  }

  private List<BlobContainer> getBlobContainers(
      List<ai.verta.modeldb.versioning.BlobDiff> blobDiffList,
      Map<String, BlobExpanded> locationBlobsMap,
      HashMap<String, List<String>> conflictLocationMap)
      throws ModelDBException {
    Map<String, BlobExpanded> locationBlobsMapNew = new LinkedHashMap<>();
    for (ai.verta.modeldb.versioning.BlobDiff blobDiff : blobDiffList) {
      final ProtocolStringList locationList = blobDiff.getLocationList();
      if (locationList == null || locationList.isEmpty()) {
        throw new ModelDBException(
            "Location in BlobDiff should not be empty", Status.Code.INVALID_ARGUMENT);
      }
      BlobExpanded blobExpanded = locationBlobsMap.get(getStringFromLocationList(locationList));
      
      HashSet<String> conflictKeys = new HashSet<>();
      AutogenBlob blob =
          DiffMerger.mergeBlob(
              blobExpanded == null ? null : AutogenBlob.fromProto(blobExpanded.getBlob()),
              AutogenBlobDiff.fromProto(blobDiff),
              conflictKeys);
      if(!conflictKeys.isEmpty()) {
    	  if(!conflictLocationMap.containsKey(getStringFromLocationList(locationList))) {
    		  conflictLocationMap.put(getStringFromLocationList(locationList), new LinkedList<String>());
    	  }
    	  conflictLocationMap.get(getStringFromLocationList(locationList)).addAll(conflictKeys);
    	  continue;
      }
      locationBlobsMapNew.put(
          getStringFromLocationList(locationList),
          blob == null
              ? null
              : BlobExpanded.newBuilder()
                  .addAllLocation(locationList)
                  .setBlob(blob.toProto())
                  .build());
    }
    
    locationBlobsMap.putAll(locationBlobsMapNew);
    List<BlobContainer> blobContainerList = new LinkedList<>();
    for (Map.Entry<String, BlobExpanded> blobExpandedEntry : locationBlobsMap.entrySet()) {
      if (blobExpandedEntry.getValue() != null) {
        blobContainerList.add(BlobContainer.create(blobExpandedEntry.getValue()));
      }
    }
    return blobContainerList;
  }
}
