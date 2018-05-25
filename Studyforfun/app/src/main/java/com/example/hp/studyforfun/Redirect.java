package com.example.hp.studyforfun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Redirect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);
        Intent intent1 = getIntent();
        String email1 = intent1.getStringExtra("email");
        String name1 = intent1.getStringExtra("name");
        String profile = intent1.getStringExtra("profile");

            Intent i = new Intent(this,chat.class);
            i.putExtra("email",email1);
            i.putExtra("name",name1);
            i.putExtra("profile",profile);
            startActivity(i);




    }
}
