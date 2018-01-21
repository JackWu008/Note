package com.jackwu.note.models;

import android.database.Cursor;

import com.jackwu.note.constants.DbConstants;

import net.lzzy.sqllib.DataUtils;
import net.lzzy.sqllib.ISqlitable;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Note implements ISqlitable {
    private UUID id;
    private String title;
    private String content;
    private Date time;

    public Note() {
        id = UUID.randomUUID();
        time = new Date();
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        setTitle();
    }

    public String getTitle() {
        return title;
    }

    private void setTitle() {
        if (content.length() > 34)
            title = content.substring(0, 34).concat(".......");
        else
            title = content;
    }

    @Override
    public String getTableName() {
        return DbConstants.NOTE_TABLE_NAME;
    }

    @Override
    public String getPKName() {
        return DbConstants.NOTE_ID;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public HashMap<String, Object> getInsertCols() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DbConstants.NOTE_ID, id.toString());
        map.put(DbConstants.NOTE_TIME, time.getTime());
        map.put(DbConstants.NOTE_CONTENT, content);
        map.put(DbConstants.NOTE_TITLE, title);
        return map;
    }

    @Override
    public HashMap<String, Object> getUpdateCols() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DbConstants.NOTE_TIME, time.getTime());
        map.put(DbConstants.NOTE_CONTENT, content);
        map.put(DbConstants.NOTE_TITLE, title);
        return map;
    }

    @Override
    public HashMap<String, String> createTableCols() {
        HashMap<String, String> map = new HashMap<>();
        map.put(DbConstants.NOTE_ID, DataUtils.TYPE_STRING);
        map.put(DbConstants.NOTE_CONTENT, DataUtils.TYPE_STRING);
        map.put(DbConstants.NOTE_TIME, DataUtils.TYPE_DOUBLE);
        map.put(DbConstants.NOTE_TITLE, DataUtils.TYPE_STRING);
        return map;
    }

    @Override
    public boolean needUpdate() {
        return false;
    }

    @Override
    public void fromCursor(Cursor cursor) {
        id = UUID.fromString(cursor.getString(cursor.getColumnIndex(DbConstants.NOTE_ID)));
        content = cursor.getString(cursor.getColumnIndex(DbConstants.NOTE_CONTENT));
        time = new Date(cursor.getLong(cursor.getColumnIndex(DbConstants.NOTE_TIME)));
        title = cursor.getString(cursor.getColumnIndex(DbConstants.NOTE_TITLE));
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
