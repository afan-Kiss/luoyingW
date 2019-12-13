package com.hjq.demo.greenDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.hjq.demo.daerxiansheng.sql.TokenEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TOKEN_ENTITY".
*/
public class TokenEntityDao extends AbstractDao<TokenEntity, Long> {

    public static final String TABLENAME = "TOKEN_ENTITY";

    /**
     * Properties of entity TokenEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Key = new Property(2, String.class, "key", false, "KEY");
        public final static Property Code = new Property(3, String.class, "code", false, "CODE");
        public final static Property Card = new Property(4, String.class, "card", false, "CARD");
        public final static Property LeftTime = new Property(5, long.class, "leftTime", false, "LEFT_TIME");
        public final static Property IsSelect = new Property(6, boolean.class, "isSelect", false, "IS_SELECT");
    }


    public TokenEntityDao(DaoConfig config) {
        super(config);
    }
    
    public TokenEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TOKEN_ENTITY\" (" + //
                "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"KEY\" TEXT," + // 2: key
                "\"CODE\" TEXT," + // 3: code
                "\"CARD\" TEXT," + // 4: card
                "\"LEFT_TIME\" INTEGER NOT NULL ," + // 5: leftTime
                "\"IS_SELECT\" INTEGER NOT NULL );"); // 6: isSelect
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TOKEN_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TokenEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(3, key);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(4, code);
        }
 
        String card = entity.getCard();
        if (card != null) {
            stmt.bindString(5, card);
        }
        stmt.bindLong(6, entity.getLeftTime());
        stmt.bindLong(7, entity.getIsSelect() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TokenEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(3, key);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(4, code);
        }
 
        String card = entity.getCard();
        if (card != null) {
            stmt.bindString(5, card);
        }
        stmt.bindLong(6, entity.getLeftTime());
        stmt.bindLong(7, entity.getIsSelect() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TokenEntity readEntity(Cursor cursor, int offset) {
        TokenEntity entity = new TokenEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // key
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // code
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // card
            cursor.getLong(offset + 5), // leftTime
            cursor.getShort(offset + 6) != 0 // isSelect
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TokenEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setKey(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCode(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCard(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLeftTime(cursor.getLong(offset + 5));
        entity.setIsSelect(cursor.getShort(offset + 6) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TokenEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TokenEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TokenEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
