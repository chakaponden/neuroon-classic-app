package com.inteliclinic.neuroon.models.data;

import android.content.ContentValues;
import android.database.Cursor;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.DatabaseHolder;
import com.raizlabs.android.dbflow.converter.DateConverter;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.Date;

public final class Tip_Adapter extends ModelAdapter<Tip> {
    private final DateConverter global_typeConverterDateConverter;

    public Tip_Adapter(DatabaseHolder holder, DatabaseDefinition databaseDefinition) {
        super(databaseDefinition);
        this.global_typeConverterDateConverter = (DateConverter) holder.getTypeConverterForClass(Date.class);
    }

    public final Class<Tip> getModelClass() {
        return Tip.class;
    }

    public final String getTableName() {
        return "`TIP`";
    }

    public final void updateAutoIncrement(Tip model, Number id) {
        model.id = id.longValue();
    }

    public final Number getAutoIncrementingId(Tip model) {
        return Long.valueOf(model.id);
    }

    public final String getAutoIncrementingColumnName() {
        return "ID";
    }

    public final IProperty[] getAllColumnProperties() {
        return Tip_Table.getAllColumnProperties();
    }

    public final void bindToInsertValues(ContentValues values, Tip model) {
        if (model.title != null) {
            values.put(Tip_Table.TITLE.getCursorKey(), model.title);
        } else {
            values.putNull(Tip_Table.TITLE.getCursorKey());
        }
        if (model.content != null) {
            values.put(Tip_Table.CONTENT.getCursorKey(), model.content);
        } else {
            values.putNull(Tip_Table.CONTENT.getCursorKey());
        }
        if (model.link != null) {
            values.put(Tip_Table.LINK.getCursorKey(), model.link);
        } else {
            values.putNull(Tip_Table.LINK.getCursorKey());
        }
        if (model.tags != null) {
            values.put(Tip_Table.TAGS.getCursorKey(), model.tags);
        } else {
            values.putNull(Tip_Table.TAGS.getCursorKey());
        }
        if (model.params != null) {
            values.put(Tip_Table.PARAMETERS.getCursorKey(), model.params);
        } else {
            values.putNull(Tip_Table.PARAMETERS.getCursorKey());
        }
        Long reflastUseDate = model.lastUseDate != null ? this.global_typeConverterDateConverter.getDBValue(model.lastUseDate) : null;
        if (reflastUseDate != null) {
            values.put(Tip_Table.LAST_USE_DATE.getCursorKey(), reflastUseDate);
        } else {
            values.putNull(Tip_Table.LAST_USE_DATE.getCursorKey());
        }
        values.put(Tip_Table.FREQUENCY.getCursorKey(), Integer.valueOf(model.frequency));
        values.put(Tip_Table.VERSION.getCursorKey(), Integer.valueOf(model.version));
        values.put(Tip_Table.SERVER_ID.getCursorKey(), Integer.valueOf(model.serverId));
    }

    public final void bindToContentValues(ContentValues values, Tip model) {
        values.put(Tip_Table.ID.getCursorKey(), Long.valueOf(model.id));
        bindToInsertValues(values, model);
    }

    public final void bindToInsertStatement(DatabaseStatement statement, Tip model, int start) {
        if (model.title != null) {
            statement.bindString(start + 1, model.title);
        } else {
            statement.bindNull(start + 1);
        }
        if (model.content != null) {
            statement.bindString(start + 2, model.content);
        } else {
            statement.bindNull(start + 2);
        }
        if (model.link != null) {
            statement.bindString(start + 3, model.link);
        } else {
            statement.bindNull(start + 3);
        }
        if (model.tags != null) {
            statement.bindString(start + 4, model.tags);
        } else {
            statement.bindNull(start + 4);
        }
        if (model.params != null) {
            statement.bindString(start + 5, model.params);
        } else {
            statement.bindNull(start + 5);
        }
        Long reflastUseDate = model.lastUseDate != null ? this.global_typeConverterDateConverter.getDBValue(model.lastUseDate) : null;
        if (reflastUseDate != null) {
            statement.bindLong(start + 6, reflastUseDate.longValue());
        } else {
            statement.bindNull(start + 6);
        }
        statement.bindLong(start + 7, (long) model.frequency);
        statement.bindLong(start + 8, (long) model.version);
        statement.bindLong(start + 9, (long) model.serverId);
    }

    public final void bindToStatement(DatabaseStatement statement, Tip model) {
        statement.bindLong(1, model.id);
        bindToInsertStatement(statement, model, 1);
    }

    public final String getInsertStatementQuery() {
        return "INSERT INTO `TIP`(`TITLE`,`CONTENT`,`LINK`,`TAGS`,`PARAMETERS`,`LAST_USE_DATE`,`FREQUENCY`,`VERSION`,`SERVER_ID`) VALUES (?,?,?,?,?,?,?,?,?)";
    }

    public final String getCompiledStatementQuery() {
        return "INSERT INTO `TIP`(`ID`,`TITLE`,`CONTENT`,`LINK`,`TAGS`,`PARAMETERS`,`LAST_USE_DATE`,`FREQUENCY`,`VERSION`,`SERVER_ID`) VALUES (?,?,?,?,?,?,?,?,?,?)";
    }

    public final String getCreationQuery() {
        return "CREATE TABLE IF NOT EXISTS `TIP`(`ID` INTEGER PRIMARY KEY AUTOINCREMENT,`TITLE` TEXT,`CONTENT` TEXT,`LINK` TEXT,`TAGS` TEXT,`PARAMETERS` TEXT,`LAST_USE_DATE` INTEGER,`FREQUENCY` INTEGER,`VERSION` INTEGER,`SERVER_ID` INTEGER UNIQUE ON CONFLICT FAIL);";
    }

    public final void loadFromCursor(Cursor cursor, Tip model) {
        int indexID = cursor.getColumnIndex("ID");
        if (indexID == -1 || cursor.isNull(indexID)) {
            model.id = 0;
        } else {
            model.id = cursor.getLong(indexID);
        }
        int indexTITLE = cursor.getColumnIndex("TITLE");
        if (indexTITLE == -1 || cursor.isNull(indexTITLE)) {
            model.title = null;
        } else {
            model.title = cursor.getString(indexTITLE);
        }
        int indexCONTENT = cursor.getColumnIndex("CONTENT");
        if (indexCONTENT == -1 || cursor.isNull(indexCONTENT)) {
            model.content = null;
        } else {
            model.content = cursor.getString(indexCONTENT);
        }
        int indexLINK = cursor.getColumnIndex("LINK");
        if (indexLINK == -1 || cursor.isNull(indexLINK)) {
            model.link = null;
        } else {
            model.link = cursor.getString(indexLINK);
        }
        int indexTAGS = cursor.getColumnIndex("TAGS");
        if (indexTAGS == -1 || cursor.isNull(indexTAGS)) {
            model.tags = null;
        } else {
            model.tags = cursor.getString(indexTAGS);
        }
        int indexPARAMETERS = cursor.getColumnIndex("PARAMETERS");
        if (indexPARAMETERS == -1 || cursor.isNull(indexPARAMETERS)) {
            model.params = null;
        } else {
            model.params = cursor.getString(indexPARAMETERS);
        }
        int indexLAST_USE_DATE = cursor.getColumnIndex("LAST_USE_DATE");
        if (indexLAST_USE_DATE == -1 || cursor.isNull(indexLAST_USE_DATE)) {
            model.lastUseDate = null;
        } else {
            model.lastUseDate = this.global_typeConverterDateConverter.getModelValue(Long.valueOf(cursor.getLong(indexLAST_USE_DATE)));
        }
        int indexFREQUENCY = cursor.getColumnIndex("FREQUENCY");
        if (indexFREQUENCY == -1 || cursor.isNull(indexFREQUENCY)) {
            model.frequency = 0;
        } else {
            model.frequency = cursor.getInt(indexFREQUENCY);
        }
        int indexVERSION = cursor.getColumnIndex("VERSION");
        if (indexVERSION == -1 || cursor.isNull(indexVERSION)) {
            model.version = 0;
        } else {
            model.version = cursor.getInt(indexVERSION);
        }
        int indexSERVER_ID = cursor.getColumnIndex("SERVER_ID");
        if (indexSERVER_ID == -1 || cursor.isNull(indexSERVER_ID)) {
            model.serverId = 0;
        } else {
            model.serverId = cursor.getInt(indexSERVER_ID);
        }
    }

    public final boolean exists(Tip model, DatabaseWrapper wrapper) {
        if (model.id > 0) {
            if (new Select(Method.count(new IProperty[0])).from(Tip.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0) {
                return true;
            }
        }
        return false;
    }

    public final ConditionGroup getPrimaryConditionClause(Tip model) {
        ConditionGroup clause = ConditionGroup.clause();
        clause.and(Tip_Table.ID.eq(model.id));
        return clause;
    }

    public final Tip newInstance() {
        return new Tip();
    }

    public final BaseProperty getProperty(String name) {
        return Tip_Table.getProperty(name);
    }
}
