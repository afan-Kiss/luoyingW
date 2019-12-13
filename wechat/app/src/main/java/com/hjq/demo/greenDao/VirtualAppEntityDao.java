package com.hjq.demo.greenDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.hjq.demo.daerxiansheng.sql.VirtualAppEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "VIRTUAL_APP_ENTITY".
*/
public class VirtualAppEntityDao extends AbstractDao<VirtualAppEntity, Long> {

    public static final String TABLENAME = "VIRTUAL_APP_ENTITY";

    /**
     * Properties of entity VirtualAppEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property App_img = new Property(1, String.class, "app_img", false, "APP_IMG");
        public final static Property AppName = new Property(2, String.class, "appName", false, "APP_NAME");
        public final static Property AppLink = new Property(3, String.class, "appLink", false, "APP_LINK");
        public final static Property App_number = new Property(4, String.class, "app_number", false, "APP_NUMBER");
        public final static Property App_founder = new Property(5, String.class, "app_founder", false, "APP_FOUNDER");
        public final static Property UserCard = new Property(6, String.class, "userCard", false, "USER_CARD");
    }


    public VirtualAppEntityDao(DaoConfig config) {
        super(config);
    }
    
    public VirtualAppEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"VIRTUAL_APP_ENTITY\" (" + //
                "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"APP_IMG\" TEXT," + // 1: app_img
                "\"APP_NAME\" TEXT," + // 2: appName
                "\"APP_LINK\" TEXT," + // 3: appLink
                "\"APP_NUMBER\" TEXT," + // 4: app_number
                "\"APP_FOUNDER\" TEXT," + // 5: app_founder
                "\"USER_CARD\" TEXT);"); // 6: userCard
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"VIRTUAL_APP_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, VirtualAppEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String app_img = entity.getApp_img();
        if (app_img != null) {
            stmt.bindString(2, app_img);
        }
 
        String appName = entity.getAppName();
        if (appName != null) {
            stmt.bindString(3, appName);
        }
 
        String appLink = entity.getAppLink();
        if (appLink != null) {
            stmt.bindString(4, appLink);
        }
 
        String app_number = entity.getApp_number();
        if (app_number != null) {
            stmt.bindString(5, app_number);
        }
 
        String app_founder = entity.getApp_founder();
        if (app_founder != null) {
            stmt.bindString(6, app_founder);
        }
 
        String userCard = entity.getUserCard();
        if (userCard != null) {
            stmt.bindString(7, userCard);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, VirtualAppEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String app_img = entity.getApp_img();
        if (app_img != null) {
            stmt.bindString(2, app_img);
        }
 
        String appName = entity.getAppName();
        if (appName != null) {
            stmt.bindString(3, appName);
        }
 
        String appLink = entity.getAppLink();
        if (appLink != null) {
            stmt.bindString(4, appLink);
        }
 
        String app_number = entity.getApp_number();
        if (app_number != null) {
            stmt.bindString(5, app_number);
        }
 
        String app_founder = entity.getApp_founder();
        if (app_founder != null) {
            stmt.bindString(6, app_founder);
        }
 
        String userCard = entity.getUserCard();
        if (userCard != null) {
            stmt.bindString(7, userCard);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public VirtualAppEntity readEntity(Cursor cursor, int offset) {
        VirtualAppEntity entity = new VirtualAppEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // app_img
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // appName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // appLink
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // app_number
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // app_founder
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // userCard
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, VirtualAppEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setApp_img(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAppName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAppLink(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setApp_number(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setApp_founder(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUserCard(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(VirtualAppEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(VirtualAppEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(VirtualAppEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}