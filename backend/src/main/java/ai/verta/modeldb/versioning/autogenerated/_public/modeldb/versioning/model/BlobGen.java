// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model;

import ai.verta.modeldb.versioning.*;
import ai.verta.modeldb.versioning.blob.diff.*;
import com.pholser.junit.quickcheck.generator.*;
import com.pholser.junit.quickcheck.generator.java.util.*;
import com.pholser.junit.quickcheck.random.*;
import java.util.*;

public class BlobGen extends Generator<Blob> {
  public BlobGen() {
    super(Blob.class);
  }

  @Override
  public Blob generate(SourceOfRandomness r, GenerationStatus status) {
    // if (r.nextBoolean())
    //     return null;

    Blob obj = new Blob();
    if (r.nextBoolean()) {
      obj.setCode(Utils.removeEmpty(gen().type(CodeBlob.class).generate(r, status)));
    }
    if (r.nextBoolean()) {
      obj.setConfig(Utils.removeEmpty(gen().type(ConfigBlob.class).generate(r, status)));
    }
    if (r.nextBoolean()) {
      obj.setDataset(Utils.removeEmpty(gen().type(DatasetBlob.class).generate(r, status)));
    }
    if (r.nextBoolean()) {
      obj.setEnvironment(Utils.removeEmpty(gen().type(EnvironmentBlob.class).generate(r, status)));
    }
    return obj;
  }
}
