package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.Condition;
import java.util.Collection;

public interface ITypeConditional<ValueType> extends Query, IConditional {
    Condition.Between between(ValueType valuetype);

    Condition concatenate(ValueType valuetype);

    Condition eq(ValueType valuetype);

    Condition greaterThan(ValueType valuetype);

    Condition greaterThanOrEq(ValueType valuetype);

    Condition.In in(ValueType valuetype, ValueType... valuetypeArr);

    Condition.In in(Collection<ValueType> collection);

    Condition is(ValueType valuetype);

    Condition isNot(ValueType valuetype);

    Condition lessThan(ValueType valuetype);

    Condition lessThanOrEq(ValueType valuetype);

    Condition notEq(ValueType valuetype);

    Condition.In notIn(ValueType valuetype, ValueType... valuetypeArr);

    Condition.In notIn(Collection<ValueType> collection);
}
