package com.raizlabs.android.dbflow.sql.migration;

import android.database.Cursor;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.ArrayList;
import java.util.List;

public class AlterTableMigration<TModel extends Model> extends BaseMigration {
    private List<QueryBuilder> columnDefinitions;
    private List<String> columnNames;
    private String oldTableName;
    private QueryBuilder query;
    private QueryBuilder renameQuery;
    private final Class<TModel> table;

    public AlterTableMigration(Class<TModel> table2) {
        this.table = table2;
    }

    public final void migrate(DatabaseWrapper database) {
        Cursor cursorToCheckColumnFor;
        String sql = getAlterTableQueryBuilder().getQuery();
        String tableName = FlowManager.getTableName(this.table);
        if (this.renameQuery != null) {
            database.execSQL(new QueryBuilder(sql).appendQuotedIfNeeded(this.oldTableName).append(this.renameQuery.getQuery()).append(tableName).toString());
        }
        if (this.columnDefinitions != null && (cursorToCheckColumnFor = SQLite.select(new IProperty[0]).from(this.table).limit(0).query(database)) != null) {
            try {
                String sql2 = new QueryBuilder(sql).append(tableName).toString();
                for (int i = 0; i < this.columnDefinitions.size(); i++) {
                    QueryBuilder columnDefinition = this.columnDefinitions.get(i);
                    if (cursorToCheckColumnFor.getColumnIndex(QueryBuilder.stripQuotes(this.columnNames.get(i))) == -1) {
                        database.execSQL(sql2 + " ADD COLUMN " + columnDefinition.getQuery());
                    }
                }
            } finally {
                cursorToCheckColumnFor.close();
            }
        }
    }

    @CallSuper
    public void onPostMigrate() {
        this.query = null;
        this.renameQuery = null;
        this.columnDefinitions = null;
        this.columnNames = null;
    }

    public AlterTableMigration<TModel> renameFrom(@NonNull String oldName) {
        this.oldTableName = oldName;
        this.renameQuery = new QueryBuilder().append(" RENAME").appendSpaceSeparated("TO");
        return this;
    }

    public AlterTableMigration<TModel> addColumn(@NonNull SQLiteType sqLiteType, @NonNull String columnName) {
        if (this.columnDefinitions == null) {
            this.columnDefinitions = new ArrayList();
            this.columnNames = new ArrayList();
        }
        this.columnDefinitions.add(new QueryBuilder().append(QueryBuilder.quoteIfNeeded(columnName)).appendSpace().appendSQLiteType(sqLiteType));
        this.columnNames.add(columnName);
        return this;
    }

    public AlterTableMigration<TModel> addForeignKeyColumn(SQLiteType sqLiteType, String columnName, String referenceClause) {
        if (this.columnDefinitions == null) {
            this.columnDefinitions = new ArrayList();
            this.columnNames = new ArrayList();
        }
        this.columnDefinitions.add(new QueryBuilder().append(QueryBuilder.quoteIfNeeded(columnName)).appendSpace().appendSQLiteType(sqLiteType).appendSpace().append("REFERENCES ").append(referenceClause));
        this.columnNames.add(columnName);
        return this;
    }

    public String getRenameQuery() {
        return new QueryBuilder(getAlterTableQueryBuilder().getQuery()).appendQuotedIfNeeded(this.oldTableName).append(this.renameQuery).append(FlowManager.getTableName(this.table)).getQuery();
    }

    public List<String> getColumnDefinitions() {
        String sql = new QueryBuilder(getAlterTableQueryBuilder()).append(FlowManager.getTableName(this.table)).toString();
        List<String> columnDefinitions2 = new ArrayList<>();
        if (this.columnDefinitions != null) {
            for (QueryBuilder columnDefinition : this.columnDefinitions) {
                columnDefinitions2.add(new QueryBuilder(sql).appendSpaceSeparated("ADD COLUMN").append(columnDefinition.getQuery()).getQuery());
            }
        }
        return columnDefinitions2;
    }

    public QueryBuilder getAlterTableQueryBuilder() {
        if (this.query == null) {
            this.query = new QueryBuilder().append("ALTER").appendSpaceSeparated("TABLE");
        }
        return this.query;
    }
}
