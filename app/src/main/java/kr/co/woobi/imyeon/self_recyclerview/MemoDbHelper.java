package kr.co.woobi.imyeon.self_recyclerview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.PropertyResourceBundle;

public class MemoDbHelper extends SQLiteOpenHelper {
    private static  MemoDbHelper sInstance;

    private static final int DB_VESION =1;
    private  static final String DB_NAME="Memo.db";
    private static final String SQL_CREATE_ETRIES=
            String.format("Create table %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)",
                    MemoContract.memoEntry.TABLE_NAME,
                    MemoContract.memoEntry._ID,
                    MemoContract.memoEntry.COLUMN_NAME_TITLE,
                    MemoContract.memoEntry.COLUMN_NAME_CONTENTS);
    private static  final  String SQL_DELETE_ENTRIES=
            "DROP TABLE IF EXISTS " +  MemoContract.memoEntry.TABLE_NAME;

    public static MemoDbHelper getInstance(Context context){
        if(sInstance==null) {
            sInstance=new MemoDbHelper(context);
        }
        return sInstance;
    }

    private MemoDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VESION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ETRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ETRIES);
    }
}
