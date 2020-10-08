package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.StringUtils;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;

public class NameAlias implements Query {
    private final String aliasName;
    private final String keyword;
    private final String name;
    private final boolean shouldAddIdentifierToAliasName;
    private final boolean shouldAddIdentifierToQuery;
    private final boolean shouldStripAliasName;
    private final boolean shouldStripIdentifier;
    private final String tableName;

    public static NameAlias joinNames(String operation, String... names) {
        if (names.length == 0) {
            return null;
        }
        String newName = "";
        for (int i = 0; i < names.length; i++) {
            if (i > 0) {
                newName = newName + " " + operation + " ";
            }
            newName = newName + names[i];
        }
        return rawBuilder(newName).build();
    }

    public static Builder builder(String name2) {
        return new Builder(name2);
    }

    public static Builder rawBuilder(String name2) {
        return new Builder(name2).shouldStripIdentifier(false).shouldAddIdentifierToName(false);
    }

    private NameAlias(Builder builder) {
        if (builder.shouldStripIdentifier) {
            this.name = QueryBuilder.stripQuotes(builder.name);
        } else {
            this.name = builder.name;
        }
        this.keyword = builder.keyword;
        if (builder.shouldStripAliasName) {
            this.aliasName = QueryBuilder.stripQuotes(builder.aliasName);
        } else {
            this.aliasName = builder.aliasName;
        }
        if (StringUtils.isNotNullOrEmpty(builder.tableName)) {
            this.tableName = QueryBuilder.quoteIfNeeded(builder.tableName);
        } else {
            this.tableName = null;
        }
        this.shouldStripIdentifier = builder.shouldStripIdentifier;
        this.shouldStripAliasName = builder.shouldStripAliasName;
        this.shouldAddIdentifierToQuery = builder.shouldAddIdentifierToQuery;
        this.shouldAddIdentifierToAliasName = builder.shouldAddIdentifierToAliasName;
    }

    public String name() {
        return (!StringUtils.isNotNullOrEmpty(this.name) || !this.shouldAddIdentifierToQuery) ? this.name : QueryBuilder.quoteIfNeeded(this.name);
    }

    public String nameRaw() {
        return this.shouldStripIdentifier ? this.name : QueryBuilder.stripQuotes(this.name);
    }

    public String aliasName() {
        return (!StringUtils.isNotNullOrEmpty(this.aliasName) || !this.shouldAddIdentifierToAliasName) ? this.aliasName : QueryBuilder.quoteIfNeeded(this.aliasName);
    }

    public String aliasNameRaw() {
        return this.shouldStripAliasName ? this.aliasName : QueryBuilder.stripQuotes(this.aliasName);
    }

    public String tableName() {
        return this.tableName;
    }

    public String keyword() {
        return this.keyword;
    }

    public boolean shouldStripIdentifier() {
        return this.shouldStripIdentifier;
    }

    public boolean shouldStripAliasName() {
        return this.shouldStripAliasName;
    }

    public String fullName() {
        return (StringUtils.isNotNullOrEmpty(this.tableName) ? tableName() + "." : "") + name();
    }

    public String getQuery() {
        if (StringUtils.isNotNullOrEmpty(this.aliasName)) {
            return aliasName();
        }
        if (StringUtils.isNotNullOrEmpty(this.name)) {
            return fullName();
        }
        return "";
    }

    public String getNameAsKey() {
        if (StringUtils.isNotNullOrEmpty(this.aliasName)) {
            return aliasNameRaw();
        }
        return nameRaw();
    }

    public String toString() {
        return getFullQuery();
    }

    public String getFullQuery() {
        String query = fullName();
        if (StringUtils.isNotNullOrEmpty(this.aliasName)) {
            query = query + " AS " + aliasName();
        }
        if (StringUtils.isNotNullOrEmpty(this.keyword)) {
            return this.keyword + " " + query;
        }
        return query;
    }

    public Builder newBuilder() {
        return new Builder(this.name).keyword(this.keyword).as(this.aliasName).shouldStripAliasName(this.shouldStripAliasName).shouldStripIdentifier(this.shouldStripIdentifier).shouldAddIdentifierToName(this.shouldAddIdentifierToQuery).shouldAddIdentifierToAliasName(this.shouldAddIdentifierToAliasName).withTable(this.tableName);
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public String aliasName;
        /* access modifiers changed from: private */
        public String keyword;
        /* access modifiers changed from: private */
        public final String name;
        /* access modifiers changed from: private */
        public boolean shouldAddIdentifierToAliasName = true;
        /* access modifiers changed from: private */
        public boolean shouldAddIdentifierToQuery = true;
        /* access modifiers changed from: private */
        public boolean shouldStripAliasName = true;
        /* access modifiers changed from: private */
        public boolean shouldStripIdentifier = true;
        /* access modifiers changed from: private */
        public String tableName;

        public Builder(String name2) {
            this.name = name2;
        }

        public Builder distinct() {
            return keyword("DISTINCT");
        }

        public Builder keyword(String keyword2) {
            this.keyword = keyword2;
            return this;
        }

        public Builder as(String aliasName2) {
            this.aliasName = aliasName2;
            return this;
        }

        public Builder withTable(String tableName2) {
            this.tableName = tableName2;
            return this;
        }

        public Builder shouldStripIdentifier(boolean shouldStripIdentifier2) {
            this.shouldStripIdentifier = shouldStripIdentifier2;
            return this;
        }

        public Builder shouldStripAliasName(boolean shouldStripAliasName2) {
            this.shouldStripAliasName = shouldStripAliasName2;
            return this;
        }

        public Builder shouldAddIdentifierToName(boolean shouldAddIdentifierToName) {
            this.shouldAddIdentifierToQuery = shouldAddIdentifierToName;
            return this;
        }

        public Builder shouldAddIdentifierToAliasName(boolean shouldAddIdentifierToAliasName2) {
            this.shouldAddIdentifierToAliasName = shouldAddIdentifierToAliasName2;
            return this;
        }

        public NameAlias build() {
            return new NameAlias(this);
        }
    }
}
