package com.example.translator.Controller;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.translator.Model.Word;
import com.example.translator.R;
import com.example.translator.Repository.WordRepository;

import java.util.ArrayList;
import java.util.List;

public class searchActivity extends AppCompatActivity {

    public static String key = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query){

        List<Word> temp = new ArrayList<>();
        for (Word word : WordRepository.getInstance(this).getWordList()) {
            if (word.getTitle().equals(query) || word.getTranslation().equals(query))
                temp.add(word);
        }
    }
}
