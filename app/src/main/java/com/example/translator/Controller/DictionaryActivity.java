package com.example.translator.Controller;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
    public void onBackPressed() {
        invalidateOptionsMenu();
        //super.onBackPressed();
    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putBoolean(searchActivity.key, true);
        startSearch(null, false, appData, false);
        return true;
    }
}
