package com.example.translator.Controller;


import androidx.appcompat.app.AppCompatActivity;
import android.app.SearchManager;
import android.widget.SearchView;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.translator.R;

public class DictionaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_container, DictionaryFragment.newInstance())
            .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
}
