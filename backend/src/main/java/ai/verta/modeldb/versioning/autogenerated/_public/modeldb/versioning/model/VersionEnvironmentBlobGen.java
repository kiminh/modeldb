// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model;

import ai.verta.modeldb.versioning.*;
import ai.verta.modeldb.versioning.blob.diff.*;
import com.pholser.junit.quickcheck.generator.*;
import com.pholser.junit.quickcheck.generator.java.util.*;
import com.pholser.junit.quickcheck.random.*;
import java.util.*;

public class VersionEnvironmentBlobGen extends Generator<VersionEnvironmentBlob> {
  public VersionEnvironmentBlobGen() {
    super(VersionEnvironmentBlob.class);
  }

  @Override
  public VersionEnvironmentBlob generate(SourceOfRandomness r, GenerationStatus status) {
    // if (r.nextBoolean())
    //     return null;

    VersionEnvironmentBlob obj = new VersionEnvironmentBlob();
    if (r.nextBoolean()) {
      obj.setMajor(Utils.removeEmpty(gen().type(Integer.class).generate(r, status)));
    }
    if (r.nextBoolean()) {
      obj.setMinor(Utils.removeEmpty(gen().type(Integer.class).generate(r, status)));
    }
    if (r.nextBoolean()) {
      obj.setPatch(Utils.removeEmpty(gen().type(Integer.class).generate(r, status)));
    }
    if (r.nextBoolean()) {
      obj.setSuffix(Utils.removeEmpty(new StringGenerator().generate(r, status)));
    }
    return obj;
  }
}
