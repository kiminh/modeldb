// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model;

import ai.verta.modeldb.versioning.*;
import ai.verta.modeldb.versioning.blob.diff.*;
import com.pholser.junit.quickcheck.generator.*;
import com.pholser.junit.quickcheck.generator.java.util.*;
import com.pholser.junit.quickcheck.random.*;
import java.util.*;

public class S3DatasetDiffGen extends Generator<S3DatasetDiff> {
  public S3DatasetDiffGen() {
    super(S3DatasetDiff.class);
  }

  @Override
  public S3DatasetDiff generate(SourceOfRandomness r, GenerationStatus status) {
    // if (r.nextBoolean())
    //     return null;

    S3DatasetDiff obj = new S3DatasetDiff();
    if (r.nextBoolean()) {
      int size = r.nextInt(0, 10);
      List<S3DatasetComponentDiff> ret = new ArrayList(size);
      for (int i = 0; i < size; i++) {
        ret.add(gen().type(S3DatasetComponentDiff.class).generate(r, status));
      }
      obj.setComponents(Utils.removeEmpty(ret));
    }
    return obj;
  }
}
