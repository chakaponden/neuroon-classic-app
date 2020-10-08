package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.annotation.Collate;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Condition extends BaseCondition implements ITypeConditional {

    public static class Operation {
        public static final String AND = "AND";
        public static final String BETWEEN = "BETWEEN";
        public static final String CONCATENATE = "||";
        public static final String DIVISION = "/";
        public static final String EMPTY_PARAM = "?";
        public static final String EQUALS = "=";
        public static final String GLOB = "GLOB";
        public static final String GREATER_THAN = ">";
        public static final String GREATER_THAN_OR_EQUALS = ">=";
        public static final String IN = "IN";
        public static final String IS_NOT_NULL = "IS NOT NULL";
        public static final String IS_NULL = "IS NULL";
        public static final String LESS_THAN = "<";
        public static final String LESS_THAN_OR_EQUALS = "<=";
        public static final String LIKE = "LIKE";
        public static final String MINUS = "-";
        public static final String MOD = "%";
        public static final String MULTIPLY = "*";
        public static final String NOT_EQUALS = "!=";
        public static final String NOT_IN = "NOT IN";
        public static final String OR = "OR";
        public static final String PLUS = "+";
    }

    public static String convertValueToString(Object value) {
        return BaseCondition.convertValueToString(value, false);
    }

    public static Condition column(NameAlias column) {
        return new Condition(column);
    }

    Condition(NameAlias nameAlias) {
        super(nameAlias);
    }

    public void appendConditionToQuery(QueryBuilder queryBuilder) {
        queryBuilder.append(columnName()).append(operation());
        if (this.isValueSet) {
            queryBuilder.append(BaseCondition.convertValueToString(value(), true));
        }
        if (postArgument() != null) {
            queryBuilder.appendSpace().append(postArgument());
        }
    }

    public Condition is(Object value) {
        this.operation = Operation.EQUALS;
        return value(value);
    }

    public Condition eq(Object value) {
        return is(value);
    }

    public Condition isNot(Object value) {
        this.operation = Operation.NOT_EQUALS;
        return value(value);
    }

    public Condition notEq(Object value) {
        return isNot(value);
    }

    public Condition like(String value) {
        this.operation = String.format(" %1s ", new Object[]{Operation.LIKE});
        return value(value);
    }

    public Condition glob(String value) {
        this.operation = String.format(" %1s ", new Object[]{Operation.GLOB});
        return value(value);
    }

    public Condition value(Object value) {
        this.value = value;
        this.isValueSet = true;
        return this;
    }

    public Condition greaterThan(Object value) {
        this.operation = Operation.GREATER_THAN;
        return value(value);
    }

    public Condition greaterThanOrEq(Object value) {
        this.operation = Operation.GREATER_THAN_OR_EQUALS;
        return value(value);
    }

    public Condition lessThan(Object value) {
        this.operation = Operation.LESS_THAN;
        return value(value);
    }

    public Condition lessThanOrEq(Object value) {
        this.operation = Operation.LESS_THAN_OR_EQUALS;
        return value(value);
    }

    public Condition operation(String operation) {
        this.operation = operation;
        return this;
    }

    public Condition collate(String collation) {
        this.postArg = "COLLATE " + collation;
        return this;
    }

    public Condition collate(Collate collation) {
        if (collation.equals(Collate.NONE)) {
            this.postArg = null;
        } else {
            collate(collation.name());
        }
        return this;
    }

    public Condition postfix(String postfix) {
        this.postArg = postfix;
        return this;
    }

    public Condition isNull() {
        this.operation = String.format(" %1s ", new Object[]{Operation.IS_NULL});
        return this;
    }

    public Condition isNotNull() {
        this.operation = String.format(" %1s ", new Object[]{Operation.IS_NOT_NULL});
        return this;
    }

    public Condition separator(String separator) {
        this.separator = separator;
        return this;
    }

    public Condition is(IConditional conditional) {
        return is((Object) conditional);
    }

    public Condition eq(IConditional conditional) {
        return eq((Object) conditional);
    }

    public Condition isNot(IConditional conditional) {
        return isNot((Object) conditional);
    }

    public Condition notEq(IConditional conditional) {
        return notEq((Object) conditional);
    }

    public Condition like(IConditional conditional) {
        return like(conditional.getQuery());
    }

    public Condition glob(IConditional conditional) {
        return glob(conditional.getQuery());
    }

    public Condition greaterThan(IConditional conditional) {
        return greaterThan((Object) conditional);
    }

    public Condition greaterThanOrEq(IConditional conditional) {
        return greaterThanOrEq((Object) conditional);
    }

    public Condition lessThan(IConditional conditional) {
        return lessThan((Object) conditional);
    }

    public Condition lessThanOrEq(IConditional conditional) {
        return lessThanOrEq((Object) conditional);
    }

    public Between between(IConditional conditional) {
        return between((Object) conditional);
    }

    public In in(IConditional firstConditional, IConditional... conditionals) {
        return in((Object) firstConditional, (Object[]) conditionals);
    }

    public In notIn(IConditional firstConditional, IConditional... conditionals) {
        return notIn((Object) firstConditional, (Object[]) conditionals);
    }

    public In notIn(BaseModelQueriable firstBaseModelQueriable, BaseModelQueriable[] baseModelQueriables) {
        return notIn((Object) firstBaseModelQueriable, (Object[]) baseModelQueriables);
    }

    public Condition is(BaseModelQueriable baseModelQueriable) {
        return is((Object) baseModelQueriable);
    }

    public Condition eq(BaseModelQueriable baseModelQueriable) {
        return eq((Object) baseModelQueriable);
    }

    public Condition isNot(BaseModelQueriable baseModelQueriable) {
        return isNot((Object) baseModelQueriable);
    }

    public Condition notEq(BaseModelQueriable baseModelQueriable) {
        return notEq((Object) baseModelQueriable);
    }

    public Condition like(BaseModelQueriable baseModelQueriable) {
        return like(baseModelQueriable.getQuery());
    }

    public Condition glob(BaseModelQueriable baseModelQueriable) {
        return glob(baseModelQueriable.getQuery());
    }

    public Condition greaterThan(BaseModelQueriable baseModelQueriable) {
        return greaterThan((Object) baseModelQueriable);
    }

    public Condition greaterThanOrEq(BaseModelQueriable baseModelQueriable) {
        return greaterThanOrEq((Object) baseModelQueriable);
    }

    public Condition lessThan(BaseModelQueriable baseModelQueriable) {
        return lessThan((Object) baseModelQueriable);
    }

    public Condition lessThanOrEq(BaseModelQueriable baseModelQueriable) {
        return lessThanOrEq((Object) baseModelQueriable);
    }

    public Between between(BaseModelQueriable baseModelQueriable) {
        return between((Object) baseModelQueriable);
    }

    public In in(BaseModelQueriable firstBaseModelQueriable, BaseModelQueriable... baseModelQueriables) {
        return in((Object) firstBaseModelQueriable, (Object[]) baseModelQueriables);
    }

    public String getQuery() {
        QueryBuilder queryBuilder = new QueryBuilder();
        appendConditionToQuery(queryBuilder);
        return queryBuilder.getQuery();
    }

    public Condition concatenate(Object value) {
        TypeConverter typeConverter;
        this.operation = new QueryBuilder(Operation.EQUALS).append(columnName()).toString();
        if (!(value == null || (typeConverter = FlowManager.getTypeConverterForClass(value.getClass())) == null)) {
            value = typeConverter.getDBValue(value);
        }
        if ((value instanceof String) || (value instanceof ITypeConditional)) {
            this.operation = String.format("%1s %1s ", new Object[]{this.operation, Operation.CONCATENATE});
        } else if (value instanceof Number) {
            this.operation = String.format("%1s %1s ", new Object[]{this.operation, Operation.PLUS});
        } else {
            Object[] objArr = new Object[1];
            objArr[0] = value != null ? value.getClass() : "null";
            throw new IllegalArgumentException(String.format("Cannot concatenate the %1s", objArr));
        }
        this.value = value;
        this.isValueSet = true;
        return this;
    }

    public Condition concatenate(IConditional conditional) {
        return concatenate((Object) conditional);
    }

    public Between between(Object value) {
        return new Between(value);
    }

    public In in(Object firstArgument, Object... arguments) {
        return new In(firstArgument, true, arguments);
    }

    public In notIn(Object firstArgument, Object... arguments) {
        return new In(firstArgument, false, arguments);
    }

    public In in(Collection values) {
        return new In(values, true);
    }

    public In notIn(Collection values) {
        return new In(values, false);
    }

    public static class Between extends BaseCondition {
        private Object secondValue;

        private Between(Condition condition, Object value) {
            super(condition.nameAlias);
            this.operation = String.format(" %1s ", new Object[]{Operation.BETWEEN});
            this.value = value;
            this.isValueSet = true;
            this.postArg = condition.postArgument();
        }

        public Between and(Object secondValue2) {
            this.secondValue = secondValue2;
            return this;
        }

        public Object secondValue() {
            return this.secondValue;
        }

        public void appendConditionToQuery(QueryBuilder queryBuilder) {
            queryBuilder.append(columnName()).append(operation()).append(BaseCondition.convertValueToString(value(), true)).appendSpaceSeparated(Operation.AND).append(BaseCondition.convertValueToString(secondValue(), true)).appendSpace().appendOptional(postArgument());
        }
    }

    public static class In extends BaseCondition {
        private List<Object> inArguments;

        private In(Condition condition, Object firstArgument, boolean isIn, Object... arguments) {
            super(condition.columnAlias());
            this.inArguments = new ArrayList();
            this.inArguments.add(firstArgument);
            Collections.addAll(this.inArguments, arguments);
            Object[] objArr = new Object[1];
            objArr[0] = isIn ? Operation.IN : Operation.NOT_IN;
            this.operation = String.format(" %1s ", objArr);
        }

        private In(Condition condition, Collection<Object> args, boolean isIn) {
            super(condition.columnAlias());
            this.inArguments = new ArrayList();
            this.inArguments.addAll(args);
            Object[] objArr = new Object[1];
            objArr[0] = isIn ? Operation.IN : Operation.NOT_IN;
            this.operation = String.format(" %1s ", objArr);
        }

        public In and(Object argument) {
            this.inArguments.add(argument);
            return this;
        }

        public void appendConditionToQuery(QueryBuilder queryBuilder) {
            queryBuilder.append(columnName()).append(operation()).append("(").append(ConditionGroup.joinArguments((CharSequence) ",", (Iterable) this.inArguments)).append(")");
        }
    }
}
