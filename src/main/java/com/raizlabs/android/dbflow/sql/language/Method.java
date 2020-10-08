package com.raizlabs.android.dbflow.sql.language;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.ArrayList;
import java.util.List;

public class Method extends Property {
    private final IProperty methodProperty;
    private List<String> operationsList;
    private final List<IProperty> propertyList;

    public static Method avg(IProperty... properties) {
        return new Method("AVG", properties);
    }

    public static Method count(IProperty... properties) {
        return new Method("COUNT", properties);
    }

    public static Method group_concat(IProperty... properties) {
        return new Method("GROUP_CONCAT", properties);
    }

    public static Method max(IProperty... properties) {
        return new Method("MAX", properties);
    }

    public static Method min(IProperty... properties) {
        return new Method("MIN", properties);
    }

    public static Method sum(IProperty... properties) {
        return new Method("SUM", properties);
    }

    public static Method total(IProperty... properties) {
        return new Method("TOTAL", properties);
    }

    public static Cast cast(@NonNull IProperty property) {
        return new Cast(property);
    }

    public Method(IProperty... properties) {
        this((String) null, properties);
    }

    public Method(String methodName, IProperty... properties) {
        super((Class<? extends Model>) null, (String) null);
        this.propertyList = new ArrayList();
        this.operationsList = new ArrayList();
        this.methodProperty = new Property((Class<? extends Model>) null, NameAlias.rawBuilder(methodName).build());
        if (properties.length == 0) {
            this.propertyList.add(Property.ALL_PROPERTY);
            return;
        }
        for (IProperty property : properties) {
            addProperty(property);
        }
    }

    public Method plus(IProperty property) {
        return append(property, Condition.Operation.PLUS);
    }

    public Method minus(IProperty property) {
        return append(property, Condition.Operation.MINUS);
    }

    public Method addProperty(@NonNull IProperty property) {
        if (this.propertyList.size() == 1 && this.propertyList.get(0) == Property.ALL_PROPERTY) {
            this.propertyList.remove(0);
        }
        return append(property, ",");
    }

    public Method append(IProperty property, String operation) {
        this.propertyList.add(property);
        this.operationsList.add(operation);
        return this;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public List<IProperty> getPropertyList() {
        return this.propertyList;
    }

    public NameAlias getNameAlias() {
        if (this.nameAlias == null) {
            String query = this.methodProperty.getQuery();
            if (query == null) {
                query = "";
            }
            String query2 = query + "(";
            List<IProperty> propertyList2 = getPropertyList();
            for (int i = 0; i < propertyList2.size(); i++) {
                IProperty property = propertyList2.get(i);
                if (i > 0) {
                    query2 = query2 + " " + this.operationsList.get(i) + " ";
                }
                query2 = query2 + property.toString();
            }
            this.nameAlias = NameAlias.rawBuilder(query2 + ")").build();
        }
        return this.nameAlias;
    }

    public static class Cast {
        private final IProperty property;

        private Cast(@NonNull IProperty property2) {
            this.property = property2;
        }

        public IProperty as(SQLiteType sqLiteType) {
            return new Method("CAST", new Property(this.property.getTable(), this.property.getNameAlias().newBuilder().shouldAddIdentifierToAliasName(false).as(sqLiteType.name()).build()));
        }
    }
}
