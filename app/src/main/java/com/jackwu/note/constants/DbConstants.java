package com.jackwu.note.constants;

import com.jackwu.note.R;
import com.jackwu.note.utils.AppUtils;

import net.lzzy.sqllib.ClassUtils;
import net.lzzy.sqllib.DbPackager;

import java.util.List;

/**
 * 静态常量键
 */

public final class DbConstants {
    private DbConstants() {
    }

    /**
     * 数据库名称
     */
    private static final String DB_NAME = "notes";
    /**
     * 数据库版本
     */
    private static final int DB_VERSION = 1;
    /**
     * table note
     */
    public static final String NOTE_TABLE_NAME = "notes";
    public static final String NOTE_ID = "id";
    public static final String NOTE_TITLE = "title";
    public static final String NOTE_TIME = "time";
    public static final String NOTE_CONTENT = "content";
    public static DbPackager dbPackage;

    static {
        List<String> tables = ClassUtils.getModels(AppUtils.getContext(), R.raw.tables);
        dbPackage = DbPackager.getInstance(DB_NAME, DB_VERSION, tables);
    }
}
