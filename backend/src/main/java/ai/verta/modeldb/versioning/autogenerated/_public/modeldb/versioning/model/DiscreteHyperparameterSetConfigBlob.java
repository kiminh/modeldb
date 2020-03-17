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

public class DiscreteHyperparameterSetConfigBlob implements ProtoType {
    public List<HyperparameterValuesConfigBlob> Values;

    public DiscreteHyperparameterSetConfigBlob() {
        this.Values = null;
    }

    public Boolean isEmpty() {
        if (this.Values != null) {
            return false;
        }
        return true;
    }

    // TODO: not consider order on lists
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof DiscreteHyperparameterSetConfigBlob)) return false;
        DiscreteHyperparameterSetConfigBlob other = (DiscreteHyperparameterSetConfigBlob) o;

        {
            Function3<List<HyperparameterValuesConfigBlob>,List<HyperparameterValuesConfigBlob>,Boolean> f = (x2, y2) -> IntStream.range(0, Math.min(x2.size(), y2.size())).mapToObj(i -> { Function3<HyperparameterValuesConfigBlob,HyperparameterValuesConfigBlob,Boolean> f2 = (x, y) -> x.equals(y); return f2.apply(x2.get(i), y2.get(i));}).filter(x -> x != null).collect(Collectors.toList()).isEmpty();
            if (this.Values != null || other.Values != null) {
                if (this.Values == null && other.Values != null)
                    return false;
                if (this.Values != null && other.Values == null)
                    return false;
                if (!f.apply(this.Values, other.Values))
                    return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(
        this.Values
        );
      }

    public DiscreteHyperparameterSetConfigBlob setValues(List<HyperparameterValuesConfigBlob> value) {
        this.Values = value;
        return this;
    }

    static public DiscreteHyperparameterSetConfigBlob fromProto(ai.verta.modeldb.versioning.DiscreteHyperparameterSetConfigBlob blob) {
        if (blob == null) {
            return null;
        }

        DiscreteHyperparameterSetConfigBlob obj = new DiscreteHyperparameterSetConfigBlob();
        {
            Function<ai.verta.modeldb.versioning.DiscreteHyperparameterSetConfigBlob,List<HyperparameterValuesConfigBlob>> f = x -> blob.getValuesList().stream().map(HyperparameterValuesConfigBlob::fromProto).collect(Collectors.toList());
            obj.Values = Utils.removeEmpty(f.apply(blob));
        }
        return obj;
    }

    public ai.verta.modeldb.versioning.DiscreteHyperparameterSetConfigBlob.Builder toProto() {
        ai.verta.modeldb.versioning.DiscreteHyperparameterSetConfigBlob.Builder builder = ai.verta.modeldb.versioning.DiscreteHyperparameterSetConfigBlob.newBuilder();
        {
            if (this.Values != null) {
                Function<ai.verta.modeldb.versioning.DiscreteHyperparameterSetConfigBlob.Builder,Void> f = x -> { builder.addAllValues(this.Values.stream().map(y -> y.toProto().build()).collect(Collectors.toList())); return null; };
                f.apply(builder);
            }
        }
        return builder;
    }

    public void preVisitShallow(Visitor visitor) throws ModelDBException {
        visitor.preVisitDiscreteHyperparameterSetConfigBlob(this);
    }

    public void preVisitDeep(Visitor visitor) throws ModelDBException {
        this.preVisitShallow(visitor);
        visitor.preVisitDeepListOfHyperparameterValuesConfigBlob(this.Values);
    }

    public DiscreteHyperparameterSetConfigBlob postVisitShallow(Visitor visitor) throws ModelDBException {
        return visitor.postVisitDiscreteHyperparameterSetConfigBlob(this);
    }

    public DiscreteHyperparameterSetConfigBlob postVisitDeep(Visitor visitor) throws ModelDBException {
        this.Values = visitor.postVisitDeepListOfHyperparameterValuesConfigBlob(this.Values);
        return this.postVisitShallow(visitor);
    }
}
