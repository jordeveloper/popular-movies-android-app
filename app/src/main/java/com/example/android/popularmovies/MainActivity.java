package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.APIUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.test);
        String testUrl = APIUtils.buildQueryUrl(APIUtils.POPULAR_ENDPOINT, getString(R.string.API_KEY)).toString();
        tv.setText(testUrl);
    }
}
