// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.modeldb.versioning.autogenerated.{{package}}.model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ai.verta.modeldb.ModelDBException;
import ai.verta.modeldb.versioning.*;
import ai.verta.modeldb.versioning.blob.diff.Function3;
import ai.verta.modeldb.versioning.blob.diff.*;
import ai.verta.modeldb.versioning.blob.visitors.Visitor;
import com.pholser.junit.quickcheck.generator.*;
import com.pholser.junit.quickcheck.random.*;

public class {{class_name}} implements ProtoType {
    {{#properties}}
    {{^required}}
    public {{#type}}{{> type}}{{/type}} {{name}};
    {{/required}}
    {{/properties}}

    public {{class_name}}() {
        {{#properties}}
        {{^required}}
        this.{{name}} = {{#type}}{{> default_value}}{{/type}};
        {{/required}}
        {{/properties}}
    }

    public Boolean isEmpty() {
        {{#properties}}
        {{^required}}
        {{#type}}
        if (this.{{name}} != null && !this.{{name}}.equals({{> default_value}}) {{#is_list}} && !this.{{name}}.isEmpty(){{/is_list}}) {
            return false;
        }
        {{/type}}
        {{/required}}
        {{/properties}}
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"class\": \"{{class_name}}\", \"fields\": {");
        boolean first = true;
        {{#properties}}
        {{^required}}
        {{#type}}
        if (this.{{name}} != null && !this.{{name}}.equals({{> default_value}}) {{#is_list}} && !this.{{name}}.isEmpty(){{/is_list}}) {
            if (!first) sb.append(", ");
            sb.append("\"{{name}}\": " + {{#type}}{{#string}}"\"" + {{/string}}{{/type}}{{name}}{{#type}}{{#string}} + "\""{{/string}}{{/type}});
            first = false;
        }
        {{/type}}
        {{/required}}
        {{/properties}}
        sb.append("}}");
        return sb.toString();
    }

    // TODO: actually hash
    public String getSHA() {
        StringBuilder sb = new StringBuilder();
        sb.append("{{class_name}}");
        {{#properties}}
        {{^required}}
        {{#type}}
        if (this.{{name}} != null && !this.{{name}}.equals({{> default_value}}) {{#is_list}} && !this.{{name}}.isEmpty(){{/is_list}}) {
            sb.append("::{{name}}::").append({{name}});
        }
        {{/type}}
        {{/required}}
        {{/properties}}

        return sb.toString();
    }

    // TODO: not consider order on lists
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof {{class_name}})) return false;
        {{class_name}} other = ({{class_name}}) o;

        {{#properties}}
        {
            {{#type}}
            Function3<{{> type}},{{> type}},Boolean> f = {{> equals}};
            {{/type}}
            if (this.{{name}} != null || other.{{name}} != null) {
                if (this.{{name}} == null && other.{{name}} != null)
                    return false;
                if (this.{{name}} != null && other.{{name}} == null)
                    return false;
                if (!f.apply(this.{{name}}, other.{{name}}))
                    return false;
            }
        }
        {{/properties}}
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        {{#properties}}
        this.{{name}}{{^last}},{{/last}}
        {{/properties}}
        );
      }

    {{#properties}}
    {{^required}}
    public {{class_name}} set{{name}}({{#type}}{{> type}}{{/type}} value) {
        this.{{name}} = Utils.removeEmpty(value);
        return this;
    }
    {{/required}}
    {{/properties}}

    static public {{class_name}} fromProto(ai.verta.modeldb.versioning.{{class_name}} blob) {
        if (blob == null) {
            return null;
        }

        {{class_name}} obj = new {{class_name}}();
        {{#properties}}
        {{^required}}
        {
            Function<ai.verta.modeldb.versioning.{{class_name}},{{#type}}{{> type}}{{/type}}> f = x -> {{#type}}{{#is_list}}blob.get{{name}}List(){{#list_type}}{{#custom}}.stream().map({{name}}::fromProto).collect(Collectors.toList()){{/custom}}{{/list_type}}{{/is_list}}{{^is_list}}{{#custom}}{{name}}.fromProto{{/custom}}(blob.get{{name}}()){{/is_list}}{{/type}};
            obj.{{name}} = Utils.removeEmpty(f.apply(blob));
        }
        {{/required}}
        {{/properties}}
        return obj;
    }

    public ai.verta.modeldb.versioning.{{class_name}}.Builder toProto() {
        ai.verta.modeldb.versioning.{{class_name}}.Builder builder = ai.verta.modeldb.versioning.{{class_name}}.newBuilder();
        {{#properties}}
        {{^required}}
        {{#type}}
        {
            if (this.{{name}} != null && !this.{{name}}.equals({{> default_value}}) {{#is_list}} && !this.{{name}}.isEmpty(){{/is_list}}) {
                Function<ai.verta.modeldb.versioning.{{class_name}}.Builder,Void> f = x -> { {{#type}}{{#is_list}}builder.addAll{{name}}(this.{{name}}{{#list_type}}{{#is_custom}}.stream().map(y -> y.toProto().build()).collect(Collectors.toList()){{/is_custom}}{{/list_type}}){{/is_list}}{{^is_list}}builder.set{{name}}(this.{{name}}{{#is_custom}}.toProto(){{/is_custom}}){{/is_list}}{{/type}}; return null; };
                f.apply(builder);
            }
        }
        {{/type}}
        {{/required}}
        {{/properties}}
        return builder;
    }

    public void preVisitShallow(Visitor visitor) throws ModelDBException {
        visitor.preVisit{{class_name}}(this);
    }

    public void preVisitDeep(Visitor visitor) throws ModelDBException {
        this.preVisitShallow(visitor);
        {{#properties}}
        {{^required}}
        visitor.preVisitDeep{{#type}}{{> visittype}}{{/type}}(this.{{name}});
        {{/required}}
        {{/properties}}
    }

    public {{class_name}} postVisitShallow(Visitor visitor) throws ModelDBException {
        return visitor.postVisit{{class_name}}(this);
    }

    public {{class_name}} postVisitDeep(Visitor visitor) throws ModelDBException {
        {{#properties}}
        {{^required}}
        this.set{{name}}(visitor.postVisitDeep{{#type}}{{> visittype}}{{/type}}(this.{{name}}));
        {{/required}}
        {{/properties}}
        return this.postVisitShallow(visitor);
    }
}