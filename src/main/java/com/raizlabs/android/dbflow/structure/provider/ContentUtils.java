package com.raizlabs.android.dbflow.structure.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.ArrayList;
import java.util.List;

public class ContentUtils {
    public static final String BASE_CONTENT_URI = "content://";

    public static Uri buildUriWithAuthority(String authority, String... paths) {
        return buildUri(BASE_CONTENT_URI, authority, paths);
    }

    public static Uri buildUri(String baseContentUri, String authority, String... paths) {
        Uri.Builder builder = Uri.parse(baseContentUri + authority).buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    public static <TableClass extends Model> Uri insert(Uri insertUri, TableClass model) {
        return insert(FlowManager.getContext().getContentResolver(), insertUri, model);
    }

    public static <TableClass extends Model> Uri insert(ContentResolver contentResolver, Uri insertUri, TableClass model) {
        ModelAdapter<TableClass> adapter = FlowManager.getModelAdapter(model.getClass());
        ContentValues contentValues = new ContentValues();
        adapter.bindToInsertValues(contentValues, model);
        Uri uri = contentResolver.insert(insertUri, contentValues);
        adapter.updateAutoIncrement(model, Long.valueOf(uri.getPathSegments().get(uri.getPathSegments().size() - 1)));
        return uri;
    }

    public static <TableClass extends Model> int bulkInsert(ContentResolver contentResolver, Uri bulkInsertUri, Class<TableClass> table, List<TableClass> models) {
        ContentValues[] contentValues = new ContentValues[(models == null ? 0 : models.size())];
        ModelAdapter<TableClass> adapter = FlowManager.getModelAdapter(table);
        if (models != null) {
            for (int i = 0; i < contentValues.length; i++) {
                contentValues[i] = new ContentValues();
                adapter.bindToInsertValues(contentValues[i], (Model) models.get(i));
            }
        }
        return contentResolver.bulkInsert(bulkInsertUri, contentValues);
    }

    public static <TableClass extends Model> int bulkInsert(Uri bulkInsertUri, Class<TableClass> table, List<TableClass> models) {
        return bulkInsert(FlowManager.getContext().getContentResolver(), bulkInsertUri, table, models);
    }

    public static <TableClass extends Model> int update(Uri updateUri, TableClass model) {
        return update(FlowManager.getContext().getContentResolver(), updateUri, model);
    }

    public static <TableClass extends Model> int update(ContentResolver contentResolver, Uri updateUri, TableClass model) {
        ModelAdapter<TableClass> adapter = FlowManager.getModelAdapter(model.getClass());
        ContentValues contentValues = new ContentValues();
        adapter.bindToContentValues(contentValues, model);
        int count = contentResolver.update(updateUri, contentValues, adapter.getPrimaryConditionClause(model).getQuery(), (String[]) null);
        if (count == 0) {
            FlowLog.log(FlowLog.Level.W, "Updated failed of: " + model.getClass());
        }
        return count;
    }

    public static <TableClass extends Model> int delete(Uri deleteUri, TableClass model) {
        return delete(FlowManager.getContext().getContentResolver(), deleteUri, model);
    }

    public static <TableClass extends Model> int delete(ContentResolver contentResolver, Uri deleteUri, TableClass model) {
        ModelAdapter<TableClass> adapter = FlowManager.getModelAdapter(model.getClass());
        int count = contentResolver.delete(deleteUri, adapter.getPrimaryConditionClause(model).getQuery(), (String[]) null);
        if (count > 0) {
            adapter.updateAutoIncrement(model, 0);
        } else {
            FlowLog.log(FlowLog.Level.W, "A delete on " + model.getClass() + " within the ContentResolver appeared to fail.");
        }
        return count;
    }

    public static Cursor query(ContentResolver contentResolver, Uri queryUri, ConditionGroup whereConditions, String orderBy, String... columns) {
        return contentResolver.query(queryUri, columns, whereConditions.getQuery(), (String[]) null, orderBy);
    }

    public static <TableClass extends Model> List<TableClass> queryList(Uri queryUri, Class<TableClass> table, ConditionGroup whereConditions, String orderBy, String... columns) {
        return queryList(FlowManager.getContext().getContentResolver(), queryUri, table, whereConditions, orderBy, columns);
    }

    public static <TableClass extends Model> List<TableClass> queryList(ContentResolver contentResolver, Uri queryUri, Class<TableClass> table, ConditionGroup whereConditions, String orderBy, String... columns) {
        Cursor cursor = contentResolver.query(queryUri, columns, whereConditions.getQuery(), (String[]) null, orderBy);
        if (cursor != null) {
            return (List) FlowManager.getModelAdapter(table).getListModelLoader().load(cursor);
        }
        return new ArrayList();
    }

    public static <TableClass extends Model> TableClass querySingle(Uri queryUri, Class<TableClass> table, ConditionGroup whereConditions, String orderBy, String... columns) {
        return querySingle(FlowManager.getContext().getContentResolver(), queryUri, table, whereConditions, orderBy, columns);
    }

    public static <TableClass extends Model> TableClass querySingle(ContentResolver contentResolver, Uri queryUri, Class<TableClass> table, ConditionGroup whereConditions, String orderBy, String... columns) {
        List<TableClass> list = queryList(contentResolver, queryUri, table, whereConditions, orderBy, columns);
        if (list.size() > 0) {
            return (Model) list.get(0);
        }
        return null;
    }
}
