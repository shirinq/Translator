package com.example.translator.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.translator.Datebase.WordBaseHelper;
import com.example.translator.Datebase.WordCursorWrapper;
import com.example.translator.Datebase.WordSchema;
import com.example.translator.Model.Word;

import java.util.ArrayList;
import java.util.List;

public class WordRepository {

    private static WordRepository instance;
    private List<Word> mWordList;
    private SQLiteDatabase mDatabase;

    private WordRepository(){}

    public static WordRepository getInstance(Context context){
        if(instance == null)
            instance = new WordRepository(context);
        return instance;
    }
    private WordRepository (Context context){
        mDatabase =  new WordBaseHelper(context).getWritableDatabase();
    }

    /**
     * queryWords
     * @param columns
     * @param where
     * @param whereArgs
     * @return
     */
    private CursorWrapper queryWords(String[] columns , String where , String[] whereArgs){
        String OrderBy = WordSchema.WordsTable.Cols.TITLE;
        Cursor cursor = mDatabase.query(WordSchema.WordsTable.NAME,
                columns, where, whereArgs, null, null,OrderBy);

        return new WordCursorWrapper(cursor);
    }

    /**
     * ContentValues
     * @param word
     * @return
     */
    private ContentValues Values(Word word){

        ContentValues values = new ContentValues();
        values.put(WordSchema.WordsTable.Cols.ID,word.getId().toString());
        values.put(WordSchema.WordsTable.Cols.TITLE,word.getTitle());
        values.put(WordSchema.WordsTable.Cols.TRANSLATION,word.getTranslation());

        return values;
    }

    /**
     * READ
     * @return
     */
    public List<Word> getWordList(){
        mWordList = new ArrayList<>();

        WordCursorWrapper cursor = (WordCursorWrapper) queryWords(null,null,null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                mWordList.add(cursor.getWord());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return mWordList;
    }

    /**
     * INSERT
     */
    public void insertWord(Word word){
        ContentValues values = Values(word);
        mDatabase.insert(WordSchema.WordsTable.NAME,null,values);
    }

    /**
     * DELETE
     */
    public void deleteWord(Word word){
        String where = WordSchema.WordsTable.Cols.ID + " = ?";
        String[] whereArgs = new String[]{word.getId().toString()};
        mDatabase.delete(WordSchema.WordsTable.NAME,where,whereArgs);
    }

    /**
     * UPDATE
     */
    public void updateWord(Word word){
        ContentValues values = Values(word);
        String where = WordSchema.WordsTable.Cols.ID + " = ?";
        String[] whereArgs = new String[]{word.getId().toString()};
        mDatabase.update(WordSchema.WordsTable.NAME,values,where,whereArgs);
    }
}
