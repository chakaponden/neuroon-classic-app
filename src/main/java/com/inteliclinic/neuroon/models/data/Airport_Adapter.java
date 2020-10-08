package com.inteliclinic.neuroon.models.data;

import android.content.ContentValues;
import android.database.Cursor;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.DatabaseHolder;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public final class Airport_Adapter extends ModelAdapter<Airport> {
    public Airport_Adapter(DatabaseHolder holder, DatabaseDefinition databaseDefinition) {
        super(databaseDefinition);
    }

    public final Class<Airport> getModelClass() {
        return Airport.class;
    }

    public final String getTableName() {
        return "`Airport`";
    }

    public final void updateAutoIncrement(Airport model, Number id) {
        model.id = id.longValue();
    }

    public final Number getAutoIncrementingId(Airport model) {
        return Long.valueOf(model.id);
    }

    public final String getAutoIncrementingColumnName() {
        return "ID";
    }

    public final IProperty[] getAllColumnProperties() {
        return Airport_Table.getAllColumnProperties();
    }

    public final void bindToInsertValues(ContentValues values, Airport model) {
        if (model.name != null) {
            values.put(Airport_Table.NAME.getCursorKey(), model.name);
        } else {
            values.putNull(Airport_Table.NAME.getCursorKey());
        }
        if (model.city != null) {
            values.put(Airport_Table.CITY.getCursorKey(), model.city);
        } else {
            values.putNull(Airport_Table.CITY.getCursorKey());
        }
        if (model.cityCode != null) {
            values.put(Airport_Table.CITY_CODE.getCursorKey(), model.cityCode);
        } else {
            values.putNull(Airport_Table.CITY_CODE.getCursorKey());
        }
        if (model.country != null) {
            values.put(Airport_Table.COUNTRY.getCursorKey(), model.country);
        } else {
            values.putNull(Airport_Table.COUNTRY.getCursorKey());
        }
        if (model.iata != null) {
            values.put(Airport_Table.IATA.getCursorKey(), model.iata);
        } else {
            values.putNull(Airport_Table.IATA.getCursorKey());
        }
        if (model.icao != null) {
            values.put(Airport_Table.ICAO.getCursorKey(), model.icao);
        } else {
            values.putNull(Airport_Table.ICAO.getCursorKey());
        }
        values.put(Airport_Table.LATITUDE.getCursorKey(), Float.valueOf(model.latitude));
        values.put(Airport_Table.LONGITUDE.getCursorKey(), Float.valueOf(model.longitude));
        values.put(Airport_Table.UTC_OFFSET_HOURS.getCursorKey(), Float.valueOf(model.utcOffsetHours));
    }

    public final void bindToContentValues(ContentValues values, Airport model) {
        values.put(Airport_Table.ID.getCursorKey(), Long.valueOf(model.id));
        bindToInsertValues(values, model);
    }

    public final void bindToInsertStatement(DatabaseStatement statement, Airport model, int start) {
        if (model.name != null) {
            statement.bindString(start + 1, model.name);
        } else {
            statement.bindNull(start + 1);
        }
        if (model.city != null) {
            statement.bindString(start + 2, model.city);
        } else {
            statement.bindNull(start + 2);
        }
        if (model.cityCode != null) {
            statement.bindString(start + 3, model.cityCode);
        } else {
            statement.bindNull(start + 3);
        }
        if (model.country != null) {
            statement.bindString(start + 4, model.country);
        } else {
            statement.bindNull(start + 4);
        }
        if (model.iata != null) {
            statement.bindString(start + 5, model.iata);
        } else {
            statement.bindNull(start + 5);
        }
        if (model.icao != null) {
            statement.bindString(start + 6, model.icao);
        } else {
            statement.bindNull(start + 6);
        }
        statement.bindDouble(start + 7, (double) model.latitude);
        statement.bindDouble(start + 8, (double) model.longitude);
        statement.bindDouble(start + 9, (double) model.utcOffsetHours);
    }

    public final void bindToStatement(DatabaseStatement statement, Airport model) {
        statement.bindLong(1, model.id);
        bindToInsertStatement(statement, model, 1);
    }

    public final String getInsertStatementQuery() {
        return "INSERT INTO `Airport`(`NAME`,`CITY`,`CITY_CODE`,`COUNTRY`,`IATA`,`ICAO`,`LATITUDE`,`LONGITUDE`,`UTC_OFFSET_HOURS`) VALUES (?,?,?,?,?,?,?,?,?)";
    }

    public final String getCompiledStatementQuery() {
        return "INSERT INTO `Airport`(`ID`,`NAME`,`CITY`,`CITY_CODE`,`COUNTRY`,`IATA`,`ICAO`,`LATITUDE`,`LONGITUDE`,`UTC_OFFSET_HOURS`) VALUES (?,?,?,?,?,?,?,?,?,?)";
    }

    public final String getCreationQuery() {
        return "CREATE TABLE IF NOT EXISTS `Airport`(`ID` INTEGER PRIMARY KEY AUTOINCREMENT,`NAME` TEXT,`CITY` TEXT,`CITY_CODE` TEXT,`COUNTRY` TEXT,`IATA` TEXT,`ICAO` TEXT,`LATITUDE` REAL,`LONGITUDE` REAL,`UTC_OFFSET_HOURS` REAL);";
    }

    public final void loadFromCursor(Cursor cursor, Airport model) {
        int indexID = cursor.getColumnIndex("ID");
        if (indexID == -1 || cursor.isNull(indexID)) {
            model.id = 0;
        } else {
            model.id = cursor.getLong(indexID);
        }
        int indexNAME = cursor.getColumnIndex("NAME");
        if (indexNAME == -1 || cursor.isNull(indexNAME)) {
            model.name = null;
        } else {
            model.name = cursor.getString(indexNAME);
        }
        int indexCITY = cursor.getColumnIndex("CITY");
        if (indexCITY == -1 || cursor.isNull(indexCITY)) {
            model.city = null;
        } else {
            model.city = cursor.getString(indexCITY);
        }
        int indexCITY_CODE = cursor.getColumnIndex("CITY_CODE");
        if (indexCITY_CODE == -1 || cursor.isNull(indexCITY_CODE)) {
            model.cityCode = null;
        } else {
            model.cityCode = cursor.getString(indexCITY_CODE);
        }
        int indexCOUNTRY = cursor.getColumnIndex("COUNTRY");
        if (indexCOUNTRY == -1 || cursor.isNull(indexCOUNTRY)) {
            model.country = null;
        } else {
            model.country = cursor.getString(indexCOUNTRY);
        }
        int indexIATA = cursor.getColumnIndex("IATA");
        if (indexIATA == -1 || cursor.isNull(indexIATA)) {
            model.iata = null;
        } else {
            model.iata = cursor.getString(indexIATA);
        }
        int indexICAO = cursor.getColumnIndex("ICAO");
        if (indexICAO == -1 || cursor.isNull(indexICAO)) {
            model.icao = null;
        } else {
            model.icao = cursor.getString(indexICAO);
        }
        int indexLATITUDE = cursor.getColumnIndex("LATITUDE");
        if (indexLATITUDE == -1 || cursor.isNull(indexLATITUDE)) {
            model.latitude = 0.0f;
        } else {
            model.latitude = cursor.getFloat(indexLATITUDE);
        }
        int indexLONGITUDE = cursor.getColumnIndex("LONGITUDE");
        if (indexLONGITUDE == -1 || cursor.isNull(indexLONGITUDE)) {
            model.longitude = 0.0f;
        } else {
            model.longitude = cursor.getFloat(indexLONGITUDE);
        }
        int indexUTC_OFFSET_HOURS = cursor.getColumnIndex("UTC_OFFSET_HOURS");
        if (indexUTC_OFFSET_HOURS == -1 || cursor.isNull(indexUTC_OFFSET_HOURS)) {
            model.utcOffsetHours = 0.0f;
        } else {
            model.utcOffsetHours = cursor.getFloat(indexUTC_OFFSET_HOURS);
        }
    }

    public final boolean exists(Airport model, DatabaseWrapper wrapper) {
        if (model.id > 0) {
            if (new Select(Method.count(new IProperty[0])).from(Airport.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0) {
                return true;
            }
        }
        return false;
    }

    public final ConditionGroup getPrimaryConditionClause(Airport model) {
        ConditionGroup clause = ConditionGroup.clause();
        clause.and(Airport_Table.ID.eq(model.id));
        return clause;
    }

    public final Airport newInstance() {
        return new Airport();
    }

    public final BaseProperty getProperty(String name) {
        return Airport_Table.getProperty(name);
    }
}
