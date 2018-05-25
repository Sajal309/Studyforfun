package com.example.hp.studyforfun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class Edit extends AppCompatActivity {


    WebView web6;
    String id;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        bar = (ProgressBar)findViewById(R.id.bar);
        web6 = (WebView)findViewById(R.id.web6);
        WebSettings settings = web6.getSettings();
        settings.setJavaScriptEnabled(true);
        web6.setWebChromeClient(new WebChromeClient());

        id = getIntent().getStringExtra("id");
        web6.loadUrl("http://studyforfun.000webhostapp.com/edit.php?id="+id);
        bar.setVisibility(View.GONE);
    }
}
