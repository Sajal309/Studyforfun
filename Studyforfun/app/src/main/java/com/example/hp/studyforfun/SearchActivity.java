package com.example.hp.studyforfun;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ConnectivityManager con = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED
                || con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {


            Intent i = new Intent(SearchActivity.this,MainActivity.class);
            startActivity(i);


        }

    }
}
