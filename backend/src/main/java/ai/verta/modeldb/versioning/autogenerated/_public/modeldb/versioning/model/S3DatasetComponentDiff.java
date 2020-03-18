// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.versioning.*;
import ai.verta.modeldb.versioning.blob.diff.*;
import ai.verta.modeldb.versioning.blob.diff.Function3;
import ai.verta.modeldb.versioning.blob.visitors.Visitor;
import com.pholser.junit.quickcheck.generator.*;
import com.pholser.junit.quickcheck.random.*;
import java.util.*;
import java.util.function.Function;

public class S3DatasetComponentDiff implements ProtoType {
  public PathDatasetComponentDiff Path;

  public S3DatasetComponentDiff() {
    this.Path = null;
  }

  public Boolean isEmpty() {
    if (this.Path != null && !this.Path.equals(null)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\"class\": \"S3DatasetComponentDiff\", \"fields\": {");
    boolean first = true;
    if (this.Path != null && !this.Path.equals(null)) {
      if (!first) sb.append(", ");
      sb.append("\"Path\": " + Path);
      first = false;
    }
    sb.append("}}");
    return sb.toString();
  }

  // TODO: actually hash
  public String getSHA() {
    StringBuilder sb = new StringBuilder();
    sb.append("S3DatasetComponentDiff");
    if (this.Path != null && !this.Path.equals(null)) {
      sb.append("::Path::").append(Path);
    }

    return sb.toString();
  }

  // TODO: not consider order on lists
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (!(o instanceof S3DatasetComponentDiff)) return false;
    S3DatasetComponentDiff other = (S3DatasetComponentDiff) o;

    {
      Function3<PathDatasetComponentDiff, PathDatasetComponentDiff, Boolean> f =
          (x, y) -> x.equals(y);
      if (this.Path != null || other.Path != null) {
        if (this.Path == null && other.Path != null) return false;
        if (this.Path != null && other.Path == null) return false;
        if (!f.apply(this.Path, other.Path)) return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.Path);
  }

  public S3DatasetComponentDiff setPath(PathDatasetComponentDiff value) {
    this.Path = Utils.removeEmpty(value);
    return this;
  }

  public static S3DatasetComponentDiff fromProto(
      ai.verta.modeldb.versioning.S3DatasetComponentDiff blob) {
    if (blob == null) {
      return null;
    }

    S3DatasetComponentDiff obj = new S3DatasetComponentDiff();
    {
      Function<ai.verta.modeldb.versioning.S3DatasetComponentDiff, PathDatasetComponentDiff> f =
          x -> PathDatasetComponentDiff.fromProto(blob.getPath());
      obj.Path = Utils.removeEmpty(f.apply(blob));
    }
    return obj;
  }

  public ai.verta.modeldb.versioning.S3DatasetComponentDiff.Builder toProto() {
    ai.verta.modeldb.versioning.S3DatasetComponentDiff.Builder builder =
        ai.verta.modeldb.versioning.S3DatasetComponentDiff.newBuilder();
    {
      if (this.Path != null && !this.Path.equals(null)) {
        Function<ai.verta.modeldb.versioning.S3DatasetComponentDiff.Builder, Void> f =
            x -> {
              builder.setPath(this.Path.toProto());
              return null;
            };
        f.apply(builder);
      }
    }
    return builder;
  }

  public void preVisitShallow(Visitor visitor) throws ModelDBException {
    visitor.preVisitS3DatasetComponentDiff(this);
  }

  public void preVisitDeep(Visitor visitor) throws ModelDBException {
    this.preVisitShallow(visitor);
    visitor.preVisitDeepPathDatasetComponentDiff(this.Path);
  }

  public S3DatasetComponentDiff postVisitShallow(Visitor visitor) throws ModelDBException {
    return visitor.postVisitS3DatasetComponentDiff(this);
  }

  public S3DatasetComponentDiff postVisitDeep(Visitor visitor) throws ModelDBException {
    this.setPath(visitor.postVisitDeepPathDatasetComponentDiff(this.Path));
    return this.postVisitShallow(visitor);
  }
}
