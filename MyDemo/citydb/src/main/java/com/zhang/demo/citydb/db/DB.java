package com.zhang.demo.citydb.db;

/**
 * Created by Administrator on 2016/6/23.
 */
public class DB {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "city.db";

    public static class City{
        public static final String TABLE_NAME = "t_p_area";
        public static final String COLUME_ID = "id";
        public static final String COLUME_AREA_NO = "areano";
        public static final String COLUME_AREA_NAME = "areaname";
        public static final String COLUME_PARENT_NO = "parentno";
        public static final String COLUME_AREA_CODE = "areacode";
        public static final String COLUME_AREA_LEVEL = "arealevel";
        public static final String COLUME_AREA_LEVEL_NAME = "arealevelname";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME
                + "("
                + COLUME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUME_AREA_NO + " INTEGER NOT NULL, "
                + COLUME_AREA_NAME + " TEXT NOT NULL, "
                + COLUME_PARENT_NO + " INTEGER NOT NULL, "
                + COLUME_AREA_CODE + " TEXT, "
                + COLUME_AREA_LEVEL + " INTEGER NOT NULL, "
                + COLUME_AREA_LEVEL_NAME + " TEXT "
                + ")";
    }
}
