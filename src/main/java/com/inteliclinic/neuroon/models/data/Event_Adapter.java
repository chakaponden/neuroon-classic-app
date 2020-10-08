package com.inteliclinic.neuroon.models.data;

import android.content.ContentValues;
import android.database.Cursor;
import com.inteliclinic.neuroon.models.data.Event;
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

public final class Event_Adapter extends ModelAdapter<Event> {
    private final DateConverter global_typeConverterDateConverter;

    public Event_Adapter(DatabaseHolder holder, DatabaseDefinition databaseDefinition) {
        super(databaseDefinition);
        this.global_typeConverterDateConverter = (DateConverter) holder.getTypeConverterForClass(Date.class);
    }

    public final Class<Event> getModelClass() {
        return Event.class;
    }

    public final String getTableName() {
        return "`Event`";
    }

    public final void updateAutoIncrement(Event model, Number id) {
        model.mId = id.longValue();
    }

    public final Number getAutoIncrementingId(Event model) {
        return Long.valueOf(model.mId);
    }

    public final String getAutoIncrementingColumnName() {
        return "ID";
    }

    public final IProperty[] getAllColumnProperties() {
        return Event_Table.getAllColumnProperties();
    }

    public final void bindToInsertValues(ContentValues values, Event model) {
        Long refmStartDate;
        Long refmEndDate;
        String refmType = null;
        if (model.mStartDate != null) {
            refmStartDate = this.global_typeConverterDateConverter.getDBValue(model.mStartDate);
        } else {
            refmStartDate = null;
        }
        if (refmStartDate != null) {
            values.put(Event_Table.START_DATE.getCursorKey(), refmStartDate);
        } else {
            values.putNull(Event_Table.START_DATE.getCursorKey());
        }
        if (model.mEndDate != null) {
            refmEndDate = this.global_typeConverterDateConverter.getDBValue(model.mEndDate);
        } else {
            refmEndDate = null;
        }
        if (refmEndDate != null) {
            values.put(Event_Table.END_DATE.getCursorKey(), refmEndDate);
        } else {
            values.putNull(Event_Table.END_DATE.getCursorKey());
        }
        if (model.mType != null) {
            refmType = model.mType.name();
        }
        if (refmType != null) {
            values.put(Event_Table.TYPE.getCursorKey(), refmType);
        } else {
            values.putNull(Event_Table.TYPE.getCursorKey());
        }
        values.put(Event_Table.COMPLETED.getCursorKey(), Integer.valueOf(model.mCompleted ? 1 : 0));
    }

    public final void bindToContentValues(ContentValues values, Event model) {
        values.put(Event_Table.ID.getCursorKey(), Long.valueOf(model.mId));
        bindToInsertValues(values, model);
    }

    public final void bindToInsertStatement(DatabaseStatement statement, Event model, int start) {
        Long refmStartDate;
        Long refmEndDate;
        String refmType = null;
        if (model.mStartDate != null) {
            refmStartDate = this.global_typeConverterDateConverter.getDBValue(model.mStartDate);
        } else {
            refmStartDate = null;
        }
        if (refmStartDate != null) {
            statement.bindLong(start + 1, refmStartDate.longValue());
        } else {
            statement.bindNull(start + 1);
        }
        if (model.mEndDate != null) {
            refmEndDate = this.global_typeConverterDateConverter.getDBValue(model.mEndDate);
        } else {
            refmEndDate = null;
        }
        if (refmEndDate != null) {
            statement.bindLong(start + 2, refmEndDate.longValue());
        } else {
            statement.bindNull(start + 2);
        }
        if (model.mType != null) {
            refmType = model.mType.name();
        }
        if (refmType != null) {
            statement.bindString(start + 3, refmType);
        } else {
            statement.bindNull(start + 3);
        }
        statement.bindLong(start + 4, model.mCompleted ? 1 : 0);
    }

    public final void bindToStatement(DatabaseStatement statement, Event model) {
        statement.bindLong(1, model.mId);
        bindToInsertStatement(statement, model, 1);
    }

    public final String getInsertStatementQuery() {
        return "INSERT INTO `Event`(`START_DATE`,`END_DATE`,`TYPE`,`COMPLETED`) VALUES (?,?,?,?)";
    }

    public final String getCompiledStatementQuery() {
        return "INSERT INTO `Event`(`ID`,`START_DATE`,`END_DATE`,`TYPE`,`COMPLETED`) VALUES (?,?,?,?,?)";
    }

    public final String getCreationQuery() {
        return "CREATE TABLE IF NOT EXISTS `Event`(`ID` INTEGER PRIMARY KEY AUTOINCREMENT,`START_DATE` INTEGER,`END_DATE` INTEGER,`TYPE` null,`COMPLETED` INTEGER);";
    }

    public final void loadFromCursor(Cursor cursor, Event model) {
        boolean z = true;
        int indexID = cursor.getColumnIndex("ID");
        if (indexID == -1 || cursor.isNull(indexID)) {
            model.mId = 0;
        } else {
            model.mId = cursor.getLong(indexID);
        }
        int indexSTART_DATE = cursor.getColumnIndex("START_DATE");
        if (indexSTART_DATE == -1 || cursor.isNull(indexSTART_DATE)) {
            model.mStartDate = null;
        } else {
            model.mStartDate = this.global_typeConverterDateConverter.getModelValue(Long.valueOf(cursor.getLong(indexSTART_DATE)));
        }
        int indexEND_DATE = cursor.getColumnIndex("END_DATE");
        if (indexEND_DATE == -1 || cursor.isNull(indexEND_DATE)) {
            model.mEndDate = null;
        } else {
            model.mEndDate = this.global_typeConverterDateConverter.getModelValue(Long.valueOf(cursor.getLong(indexEND_DATE)));
        }
        int indexTYPE = cursor.getColumnIndex("TYPE");
        if (indexTYPE == -1 || cursor.isNull(indexTYPE)) {
            model.mType = null;
        } else {
            model.mType = Event.EventType.valueOf(cursor.getString(indexTYPE));
        }
        int indexCOMPLETED = cursor.getColumnIndex("COMPLETED");
        if (indexCOMPLETED == -1 || cursor.isNull(indexCOMPLETED)) {
            model.mCompleted = false;
            return;
        }
        if (cursor.getInt(indexCOMPLETED) != 1) {
            z = false;
        }
        model.mCompleted = z;
    }

    public final boolean exists(Event model, DatabaseWrapper wrapper) {
        if (model.mId > 0) {
            if (new Select(Method.count(new IProperty[0])).from(Event.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0) {
                return true;
            }
        }
        return false;
    }

    public final ConditionGroup getPrimaryConditionClause(Event model) {
        ConditionGroup clause = ConditionGroup.clause();
        clause.and(Event_Table.ID.eq(model.mId));
        return clause;
    }

    public final Event newInstance() {
        return new Event();
    }

    public final BaseProperty getProperty(String name) {
        return Event_Table.getProperty(name);
    }
}
