// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.modeldb.versioning.autogenerated._public.modeldb.versioning.model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.versioning.*;
import ai.verta.modeldb.versioning.blob.diff.Function3;
import ai.verta.modeldb.versioning.blob.diff.*;
import ai.verta.modeldb.versioning.blob.visitors.Visitor;

public class EnvironmentBlob implements ProtoType {
    public PythonEnvironmentBlob Python;
    public DockerEnvironmentBlob Docker;
    public List<EnvironmentVariablesBlob> EnvironmentVariables;
    public List<String> CommandLine;

    public EnvironmentBlob() {
        this.Python = null;
        this.Docker = null;
        this.EnvironmentVariables = null;
        this.CommandLine = null;
    }

    public Boolean isEmpty() {
        if (this.Python != null) {
            return false;
        }
        if (this.Docker != null) {
            return false;
        }
        if (this.EnvironmentVariables != null) {
            return false;
        }
        if (this.CommandLine != null) {
            return false;
        }
        return true;
    }

    // TODO: not consider order on lists
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof EnvironmentBlob)) return false;
        EnvironmentBlob other = (EnvironmentBlob) o;

        {
            Function3<PythonEnvironmentBlob,PythonEnvironmentBlob,Boolean> f = (x, y) -> x.equals(y);
            if (this.Python != null || other.Python != null) {
                if (this.Python == null && other.Python != null)
                    return false;
                if (this.Python != null && other.Python == null)
                    return false;
                if (!f.apply(this.Python, other.Python))
                    return false;
            }
        }
        {
            Function3<DockerEnvironmentBlob,DockerEnvironmentBlob,Boolean> f = (x, y) -> x.equals(y);
            if (this.Docker != null || other.Docker != null) {
                if (this.Docker == null && other.Docker != null)
                    return false;
                if (this.Docker != null && other.Docker == null)
                    return false;
                if (!f.apply(this.Docker, other.Docker))
                    return false;
            }
        }
        {
            Function3<List<EnvironmentVariablesBlob>,List<EnvironmentVariablesBlob>,Boolean> f = (x2, y2) -> IntStream.range(0, Math.min(x2.size(), y2.size())).mapToObj(i -> { Function3<EnvironmentVariablesBlob,EnvironmentVariablesBlob,Boolean> f2 = (x, y) -> x.equals(y); return f2.apply(x2.get(i), y2.get(i));}).filter(x -> x != null).collect(Collectors.toList()).isEmpty();
            if (this.EnvironmentVariables != null || other.EnvironmentVariables != null) {
                if (this.EnvironmentVariables == null && other.EnvironmentVariables != null)
                    return false;
                if (this.EnvironmentVariables != null && other.EnvironmentVariables == null)
                    return false;
                if (!f.apply(this.EnvironmentVariables, other.EnvironmentVariables))
                    return false;
            }
        }
        {
            Function3<List<String>,List<String>,Boolean> f = (x2, y2) -> IntStream.range(0, Math.min(x2.size(), y2.size())).mapToObj(i -> { Function3<String,String,Boolean> f2 = (x, y) -> x.equals(y); return f2.apply(x2.get(i), y2.get(i));}).filter(x -> x != null).collect(Collectors.toList()).isEmpty();
            if (this.CommandLine != null || other.CommandLine != null) {
                if (this.CommandLine == null && other.CommandLine != null)
                    return false;
                if (this.CommandLine != null && other.CommandLine == null)
                    return false;
                if (!f.apply(this.CommandLine, other.CommandLine))
                    return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(
        this.Python,
        this.Docker,
        this.EnvironmentVariables,
        this.CommandLine
        );
      }

    public EnvironmentBlob setPython(PythonEnvironmentBlob value) {
        this.Python = value;
        return this;
    }
    public EnvironmentBlob setDocker(DockerEnvironmentBlob value) {
        this.Docker = value;
        return this;
    }
    public EnvironmentBlob setEnvironmentVariables(List<EnvironmentVariablesBlob> value) {
        this.EnvironmentVariables = value;
        return this;
    }
    public EnvironmentBlob setCommandLine(List<String> value) {
        this.CommandLine = value;
        return this;
    }

    static public EnvironmentBlob fromProto(ai.verta.modeldb.versioning.EnvironmentBlob blob) {
        if (blob == null) {
            return null;
        }

        EnvironmentBlob obj = new EnvironmentBlob();
        {
            Function<ai.verta.modeldb.versioning.EnvironmentBlob,PythonEnvironmentBlob> f = x -> PythonEnvironmentBlob.fromProto(blob.getPython());
            obj.Python = Utils.removeEmpty(f.apply(blob));
        }
        {
            Function<ai.verta.modeldb.versioning.EnvironmentBlob,DockerEnvironmentBlob> f = x -> DockerEnvironmentBlob.fromProto(blob.getDocker());
            obj.Docker = Utils.removeEmpty(f.apply(blob));
        }
        {
            Function<ai.verta.modeldb.versioning.EnvironmentBlob,List<EnvironmentVariablesBlob>> f = x -> blob.getEnvironmentVariablesList().stream().map(EnvironmentVariablesBlob::fromProto).collect(Collectors.toList());
            obj.EnvironmentVariables = Utils.removeEmpty(f.apply(blob));
        }
        {
            Function<ai.verta.modeldb.versioning.EnvironmentBlob,List<String>> f = x -> blob.getCommandLineList();
            obj.CommandLine = Utils.removeEmpty(f.apply(blob));
        }
        return obj;
    }

    public ai.verta.modeldb.versioning.EnvironmentBlob.Builder toProto() {
        ai.verta.modeldb.versioning.EnvironmentBlob.Builder builder = ai.verta.modeldb.versioning.EnvironmentBlob.newBuilder();
        {
            if (this.Python != null) {
                Function<ai.verta.modeldb.versioning.EnvironmentBlob.Builder,Void> f = x -> { builder.setPython(this.Python.toProto()); return null; };
                f.apply(builder);
            }
        }
        {
            if (this.Docker != null) {
                Function<ai.verta.modeldb.versioning.EnvironmentBlob.Builder,Void> f = x -> { builder.setDocker(this.Docker.toProto()); return null; };
                f.apply(builder);
            }
        }
        {
            if (this.EnvironmentVariables != null) {
                Function<ai.verta.modeldb.versioning.EnvironmentBlob.Builder,Void> f = x -> { builder.addAllEnvironmentVariables(this.EnvironmentVariables.stream().map(y -> y.toProto().build()).collect(Collectors.toList())); return null; };
                f.apply(builder);
            }
        }
        {
            if (this.CommandLine != null) {
                Function<ai.verta.modeldb.versioning.EnvironmentBlob.Builder,Void> f = x -> { builder.addAllCommandLine(this.CommandLine); return null; };
                f.apply(builder);
            }
        }
        return builder;
    }

    public void preVisitShallow(Visitor visitor) throws ModelDBException {
        visitor.preVisitEnvironmentBlob(this);
    }

    public void preVisitDeep(Visitor visitor) throws ModelDBException {
        this.preVisitShallow(visitor);
        visitor.preVisitDeepPythonEnvironmentBlob(this.Python);
        visitor.preVisitDeepDockerEnvironmentBlob(this.Docker);
        visitor.preVisitDeepListOfEnvironmentVariablesBlob(this.EnvironmentVariables);
        visitor.preVisitDeepListOfString(this.CommandLine);
    }

    public EnvironmentBlob postVisitShallow(Visitor visitor) throws ModelDBException {
        return visitor.postVisitEnvironmentBlob(this);
    }

    public EnvironmentBlob postVisitDeep(Visitor visitor) throws ModelDBException {
        this.Python = visitor.postVisitDeepPythonEnvironmentBlob(this.Python);
        this.Docker = visitor.postVisitDeepDockerEnvironmentBlob(this.Docker);
        this.EnvironmentVariables = visitor.postVisitDeepListOfEnvironmentVariablesBlob(this.EnvironmentVariables);
        this.CommandLine = visitor.postVisitDeepListOfString(this.CommandLine);
        return this.postVisitShallow(visitor);
    }
}
