package com.example.hp.studyforfun;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;
/**
 * Created by hp on 22-12-2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    SharedPreferences s;
    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);

        registerToken(token);
    }

    private void registerToken(String token) {
        s = getSharedPreferences("Info", Context.MODE_PRIVATE);
        String name = s.getString("name"," ");
        String email = s.getString("email"," ");

        OkHttpClient client = new OkHttpClient();
         FormBody.Builder f_body = new FormBody.Builder()
                .add("token",token);


         RequestBody body = f_body.build();





        Request req = new Request.Builder()
                .url("http://studyforfun.000webhostapp.com/token.php")
                .post(body)
                .build();
        try {
            client.newCall(req).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
