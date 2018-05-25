package com.example.hp.studyforfun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.NetworkChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class chat extends AppCompatActivity {
    private TextView t8;
    private ImageView i7;
    private ListView l4;
    private String name;
    private String profile;
     private ImageButton send;
     private EditText msg;
   CustomAdapter3 ad;
     List<FirebaseModel> list2;

     DatabaseReference ref1;
     DatabaseReference ref2;
     String email;
     SharedPreferences s;
     String email2;
     String name2;



    private Button b9;
    private Bitmap map;
    private ImageView i9;
    private final int req_id = 1;
    private final int req_id2 = 2;
    String file_name;
    String time;
    ImageView i12;

    LinearLayout add;
    LinearLayout del1;
    TextView del;
    ImageButton remove;
    ConstraintLayout main;
    LinearLayout pop_up;
    TextView edit_msg;
    TextView del_both;
    EditText edited_msg;
    ImageButton resend;
   String time1;
   LinearLayout edit_l1;
 LinearLayout edit_l;
 ProgressBar bar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
         email = getIntent().getStringExtra("email");
         email  = email.replaceAll("@gmail.com","");

        l4 = (ListView) findViewById(R.id.l4);
        t8 = (TextView) findViewById(R.id.t8);
        i7 = (ImageView) findViewById(R.id.i7);
        String name = getIntent().getStringExtra("name");
        String profile = getIntent().getStringExtra("profile");
        t8.setText(name);
        Picasso.with(getApplicationContext()).load(profile).into(i7);


        s = getSharedPreferences("Info",Context.MODE_PRIVATE);
        email2 = s.getString("email","");
        name2 = s.getString("name","");
        email2  = email2.replaceAll("@gmail.com","");




        FirebaseModel model = new FirebaseModel();
        send = (ImageButton) findViewById(R.id.send);
        msg = (EditText) findViewById(R.id.msg);
        list = new ArrayList<FirebaseModel>();
        list2 = new ArrayList<>();

        ref1 = FirebaseDatabase.getInstance().getReference(email2);
        ref2 = FirebaseDatabase.getInstance().getReference(email);

        list.clear();
        ad = new CustomAdapter3(chat.this, list);
        ad.notifyDataSetChanged();

        l4.setAdapter(ad);
        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        send_msg();
                    }
                }
        );



        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");

        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

      time = date.format(currentLocalTime);









        b9 = (Button)findViewById(R.id.b9);
        b9.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upload();



                    }
                }
        );

        i9 = (ImageView)findViewById(R.id.i9);
        i12 = (ImageView)findViewById(R.id.i12);

        add = (LinearLayout)findViewById(R.id.add);
        del = (TextView)findViewById(R.id.del);
        del1 = (LinearLayout)findViewById(R.id.del1);
        remove = (ImageButton)findViewById(R.id.remove);
        pop_up = (LinearLayout)findViewById(R.id.pop_up);
         edited_msg = (EditText)findViewById(R.id.edited_msg);
        resend = (ImageButton)findViewById(R.id.resend);
        edit_msg = (TextView)findViewById(R.id.edit_msg);
        del_both = (TextView)findViewById(R.id.del_both);
        edit_l1 = (LinearLayout)findViewById(R.id.edit_l1);
        edit_l = (LinearLayout)findViewById(R.id.edit_l);
        bar2 = (ProgressBar)findViewById(R.id.bar2);



       pop_up.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);
        edited_msg.setVisibility(View.GONE);
        resend.setVisibility(View.GONE);



     i9.setVisibility(View.GONE);
     b9.setVisibility(View.GONE);
     i12.setVisibility(View.GONE);
     edit_l1.setVisibility(View.GONE);
   edit_l.setVisibility(View.GONE);




        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)

                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                .defaultDisplayImageOptions(defaultOptions)

                .build();
        ImageLoader.getInstance().init(config);
    }


    public void cancel(View v){
        pop_up.setVisibility(View.GONE);
    }


    public void remove(View v){

        i12.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);
    }


   public void image(View v){
      select();
   }

    private void select() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,req_id);

    }

    public void select1(View v) {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select a Video "), req_id2);

    }


    private void upload(){

        StringRequest req = new StringRequest(Request.Method.POST, "http://studyforfun.000webhostapp.com/upload3.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {






                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }


                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("image",ImagetoString(map));
                params.put("name",file_name);
                return params;
            }
        };
        MySingleton.getInstance(chat.this).addToRequestQueue(req);


        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentLocalTime = cal.getTime();
        DateFormat date1 = new SimpleDateFormat("HH:mm:ss");

        date1.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        time1 = date1.format(currentLocalTime);


        final int rand  = new Random().nextInt(100) + 3;

        Date currentTime = Calendar.getInstance().getTime();
        file_name = String.valueOf(currentTime);
        file_name = file_name+rand;

        String id1 = ref1.push().getKey();
        String id2 = ref2.push().getKey();
        FirebaseModel model = new FirebaseModel(id1,email2,email,null,time,file_name,time1);
        ref1.child(id1).setValue(model);
        FirebaseModel2 model2 = new FirebaseModel2(id1,email2,email,null,time,file_name,time1);
        ref2.child(id1).setValue(model2);


        i9.setVisibility(View.GONE);
        b9.setVisibility(View.GONE);





    }
    private String ImagetoString(Bitmap map) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.JPEG,30,os);
        byte[] image = os.toByteArray();
        return Base64.encodeToString(image,Base64.DEFAULT);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == req_id && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                b9.setVisibility(View.VISIBLE);
                i9.setVisibility(View.VISIBLE);

                map = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                i9.setImageBitmap(map);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }




    }
      public void send_msg(){
    String message = msg.getText().toString();
    if(!TextUtils.isEmpty(message)){

        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentLocalTime = cal.getTime();
        DateFormat date1 = new SimpleDateFormat("HH:mm:ss");

        date1.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

        time1 = date1.format(currentLocalTime);

        String id1 = ref1.push().getKey();
        String id2 = ref2.push().getKey();
        FirebaseModel model = new FirebaseModel(id1,email2,email,message,time,null,time1);
        ref1.child(id1).setValue(model);
        FirebaseModel2 model2 = new FirebaseModel2(id1,email2,email,message,time,null,time1);
        ref2.child(id1).setValue(model2);
        msg.setText("");


        String name1 = getIntent().getStringExtra("name");
        String email1 = getIntent().getStringExtra("email");
        notify n = new notify(this);
        n.execute(name1,email1,name2,email2,message);

    }



    }

    List<FirebaseModel> list;
    public void back(View view) {
        Intent intent2 = new Intent(chat.this,MessageActivity.class);

        startActivity(intent2);

    }
    @Override
    public void onStart() {
        super.onStart();

         ref1.addValueEventListener(
                 new ValueEventListener() {

                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {

                         list.clear();
                         for (DataSnapshot snap : dataSnapshot.getChildren()) {
                             FirebaseModel msg = snap.getValue(FirebaseModel.class);
                             list.add(msg);

                         }
                         l4.deferNotifyDataSetChanged();
                         ad.notifyDataSetChanged();


                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }


                 }


         );


      l4.setFriction(ViewConfiguration.getScrollFriction() * 5);
      Chatlist l = new Chatlist();
      l.execute();


    }





    class CustomAdapter3 extends ArrayAdapter<FirebaseModel> {
        Context context;
        List<FirebaseModel> list;

        public CustomAdapter3(Context context, @NonNull List<FirebaseModel> objects) {
            super(context, R.layout.messages, objects);
            this.context = context;
            this.list = objects;

        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {


            view = getLayoutInflater().inflate(R.layout.messages,null);

            final FirebaseModel msg = list.get(i);


            String send_by = msg.getSend_by();
            String user = msg.getUser();
            String file = msg.getFile();

            TextView t11 = (TextView) view.findViewById(R.id.t11);
            TextView t14 = (TextView) view.findViewById(R.id.t14);
            TextView t12 = (TextView) view.findViewById(R.id.t12);
            TextView t15 = (TextView) view.findViewById(R.id.t15);
            ImageView i10 = (ImageView)view.findViewById(R.id.i10);
             ImageView i11 = (ImageView)view.findViewById(R.id.i11);
             LinearLayout list1 = (LinearLayout)view.findViewById(R.id.list1);
            LinearLayout list2 = (LinearLayout)view.findViewById(R.id.list2);


            t11.setVisibility(View.GONE);
            t14.setVisibility(View.GONE);
            t12.setVisibility(View.GONE);
            t15.setVisibility(View.GONE);
            i10.setVisibility(View.GONE);
            i11.setVisibility(View.GONE);
            list1.setVisibility(View.GONE);
            list2.setVisibility(View.GONE);






                if(send_by.equals(email2) && user.equals(email)) {
                    list2.setVisibility(View.VISIBLE);

                    if(file != null) {
                         l4.setClickable(true);
                        i10.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage("http://studyforfun.000webhostapp.com/" + msg.getFile() + ".jpg", i10);
                        l4.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        if(list.get(i).getMsg() != null){
                                            i12.setVisibility(View.GONE);
                                        }
                                        else if(list.get(i).getMsg() == null){
                                            i12.setVisibility(View.VISIBLE);
                                        }

                                        remove.setVisibility(View.VISIBLE);

                                        ImageLoader.getInstance().displayImage("http://studyforfun.000webhostapp.com/" + list.get(i).getFile() + ".jpg", i12);

                                    }
                                }
                        );

                    }

                }


                if(send_by.equals(email) && user.equals(email2)) {
                    list1.setVisibility(View.VISIBLE);

                    if(file != null) {
                        l4.setClickable(true);

                        i11.setVisibility(View.VISIBLE);
                        try {
                            ImageLoader.getInstance().displayImage("http://studyforfun.000webhostapp.com/" + msg.getFile() + ".jpg", i11);
                        }
                        catch(IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                        catch (OutOfMemoryError e){
                            e.printStackTrace();
                        }
                        l4.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        if(list.get(i).getMsg() != null){
                                            i12.setVisibility(View.GONE);
                                        }
                                        else if(list.get(i).getMsg() == null){
                                            i12.setVisibility(View.VISIBLE);
                                        }

                                        remove.setVisibility(View.VISIBLE);
                                      try {
                                          ImageLoader.getInstance().displayImage("http://studyforfun.000webhostapp.com/" + list.get(i).getFile() + ".jpg", i12);
                                      }
                                      catch(OutOfMemoryError e){
                                          e.printStackTrace();
                                      }
                                      catch (IndexOutOfBoundsException e){
                                          e.printStackTrace();
                                      }

                                    }
                                }
                        );

                    }

                }





            if(send_by.equals(email2) && user.equals(email)) {

                if(msg.getMsg() != null) {


                    t11.setVisibility(View.VISIBLE);
                    t12.setVisibility(View.VISIBLE);
                    t11.setText(msg.getMsg());
                    t12.setText(msg.getTime());

                }

            }
            if(send_by.equals(email) && user.equals(email2)) {
                if(msg.getMsg() != null) {


                    t14.setVisibility(View.VISIBLE);
                    t15.setVisibility(View.VISIBLE);
                    t14.setText(msg.getMsg());
                    t15.setText(msg.getTime());

                }
            }







            l4.setLongClickable(true);
            l4.setOnItemLongClickListener(
                    new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i1, long l) {
                            pop_up.setVisibility(View.VISIBLE);


                         if(list.get(i1).getMsg() != null){
                             add.setVisibility(View.GONE);
                         }
                         else if(list.get(i1).getMsg() == null){
                             add.setVisibility(View.VISIBLE);
                         }
                         if(list.get(i1).getFile() != null){
                             edit_l.setVisibility(View.GONE);
                         }
                         else if(list.get(i1).getFile() == null){
                             edit_l.setVisibility(View.VISIBLE);
                         }


                          del.setOnClickListener(
                                  new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {

                                          try {

                                              FirebaseModel msg1 = list.get(i1);
                                              String id = msg1.getId();
                                              Log.d("id", "id "+ id);

                                              ref1.child(id).removeValue();


                                          }
                                          catch (NullPointerException e){


                                          }

                                          pop_up.setVisibility(View.GONE);



                                      }
                                  }
                          );


                          edit_msg.setOnClickListener(

                                  new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {

                                          edit_l1.setVisibility(View.VISIBLE);
                                          pop_up.setVisibility(View.GONE);


                                          edited_msg.setVisibility(View.VISIBLE);
                                       resend.setVisibility(View.VISIBLE);

                                       resend.setOnClickListener(
                                               new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                      String id = list.get(i1).getId();


                                                       String message = edited_msg.getText().toString();
                                                       if(!TextUtils.isEmpty(message))
                                                       {


                                                           FirebaseModel model = new FirebaseModel(id,email2,email,message,time,null,time1);

                                                           Map<String, Object> Updates = new HashMap<>();
                                                           Updates.put(id,model);
                                                           ref2.updateChildren(Updates);
                                                           ref1.updateChildren(Updates);

                                                           edited_msg.setVisibility(View.GONE);
                                                           resend.setVisibility(View.GONE);
                                                           edit_l1.setVisibility(View.GONE);


                                                       }
                                                   }
                                               }
                                       );

                                      }
                                  }
                          );


                          del_both.setOnClickListener(
                                  new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          pop_up.setVisibility(View.GONE);

                                          try {

                                              FirebaseModel msg1 = list.get(i1);
                                              String id = msg1.getId();

                                              ref1.child(id).removeValue();
                                              ref2.child(id).removeValue();


                                          }
                                          catch (NullPointerException e){


                                          }
                                      }
                                  }
                          );


                            return true;
                        }
                    }
            );


            bar2.setVisibility(View.GONE);

            return view;

        }
    }
    class Chatlist extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/chatlist.php";
            try {

                String email1 = s.getString("email","");
                String email2 = getIntent().getStringExtra("email");
                URL url = new URL(page_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email1", "UTF-8") + "=" + URLEncoder.encode(email1, "UTF-8")
                        +"&"+URLEncoder.encode("email2", "UTF-8") + "=" + URLEncoder.encode(email2, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = x.getInputStream();
                br = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                br.close();
                inputStream.close();
                x.disconnect();
                String res = buffer.toString();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (x != null) {
                    x.disconnect();
                }
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

}



