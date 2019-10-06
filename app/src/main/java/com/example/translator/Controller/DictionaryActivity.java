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
}
