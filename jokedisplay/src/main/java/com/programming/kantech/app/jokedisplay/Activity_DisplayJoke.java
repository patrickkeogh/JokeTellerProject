package com.programming.kantech.app.jokedisplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Activity_DisplayJoke extends AppCompatActivity {

    public static String JOKE_KEY = "Joke key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joke);

        Intent intent = getIntent();
        String joke = intent.getStringExtra(JOKE_KEY);
        TextView tv_joke = (TextView) findViewById(R.id.tv_joke);
        if (joke != null && joke.length() != 0) {
            tv_joke.setText(joke);
        }
    }
}
