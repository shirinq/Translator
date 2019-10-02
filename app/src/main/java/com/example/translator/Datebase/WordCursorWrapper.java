package com.example.translator.Datebase;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.translator.Model.Word;

import java.util.UUID;

public class WordCursorWrapper extends CursorWrapper {

    public WordCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Word getWord(){

        String stringId = getString(getColumnIndex(WordSchema.WordsTable.Cols.ID));
        String Title = getString(getColumnIndex(WordSchema.WordsTable.Cols.TITLE));
        String Translation = getString(getColumnIndex(WordSchema.WordsTable.Cols.TRANSLATION));

        UUID Id = UUID.fromString(stringId);

        Word word = new Word(Id);
        word.setTitle(Title);
        word.setTranslation(Translation);

        return word;
    }
}
