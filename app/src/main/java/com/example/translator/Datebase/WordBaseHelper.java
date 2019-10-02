package com.example.translator.Datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class WordBaseHelper extends SQLiteOpenHelper {
    private static int VERSION = 1;
    private static String DATABASE_NAME = "WordBase";

    public WordBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE " + WordSchema.WordsTable.NAME + " ( "
        + WordSchema.WordsTable.Cols.ID + " ,"
        + WordSchema.WordsTable.Cols.TITLE + " ,"
        + WordSchema.WordsTable.Cols.TRANSLATION
        + " )"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
