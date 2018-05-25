package com.example.hp.studyforfun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MainActivity extends Activity {
    SharedPreferences s;
    private ListView l1;
    private LinearLayout chat_l;
    String email;
    EditText search1;
    ListView search_view;
    LinearLayout search_l;
    int pos;
    int index,top;
    private int last;
    private boolean isup;
    AppBarLayout toolbar;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseMessaging.getInstance().subscribeToTopic("test");
        final String token = FirebaseInstanceId.getInstance().getToken();
        s = getSharedPreferences("Info", Context.MODE_PRIVATE);
        email = s.getString("email", "");

        BackgroundToken tokenTask = new BackgroundToken(this);
        tokenTask.execute(email, token);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);


        setContentView(R.layout.activity_main);



        l1 = (ListView) findViewById(R.id.l1);
        search1 = (EditText)findViewById(R.id.search1);
        search_view = (ListView) findViewById(R.id.search_view);
        search_l = (LinearLayout)findViewById(R.id.search_l);
        search_l.setVisibility(View.GONE);
        search1.setVisibility(View.GONE);
        search_view.setVisibility(View.GONE);
        toolbar = (AppBarLayout)findViewById(R.id.toolbar);
        bar = (ProgressBar)findViewById(R.id.bar);




        final Task t = new Task();
        t.execute(email);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)

                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                .defaultDisplayImageOptions(defaultOptions)

                .build();
        ImageLoader.getInstance().init(config);


        search1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SearchTask1 t = new SearchTask1();
                t.execute(email,charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


  l1.setOnScrollListener(
          new AbsListView.OnScrollListener() {
              @Override
              public void onScrollStateChanged(AbsListView absListView, int i) {


              }

              @Override
              public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                  final ListView lw = l1;
                  if(absListView.getId() == lw.getId()){
                      final int current  = lw.getFirstVisiblePosition();
                      if(current > last){
                          isup = false;
                          toolbar.setVisibility(View.GONE);

                      }
                      else if(current < last){
                          isup = true;
                          toolbar.setVisibility(View.VISIBLE);
                      }
                      last = current;
                  }
              }
          }
  );


    }





    public void search(View v){
        search_l.setVisibility(View.VISIBLE);
        search1.setVisibility(View.VISIBLE);
        search_view.setVisibility(View.VISIBLE);

    }


    public void home1(View view) {
        Intent i = new Intent(this, Profile.class);
        startActivity(i);

    }

    public void chat1(View view) {
        Intent i = new Intent(this, MessageActivity.class);
        startActivity(i);

    }

    public void tutorial1(View view) {
        Intent i = new Intent(this, Tutorial.class);
        startActivity(i);

    }

    public void quiz1(View view) {
        Intent i = new Intent(this, QuizOption.class);
        startActivity(i);

    }
    public void upload1(View view) {
        Intent i = new Intent(this, Upload.class);
        startActivity(i);

    }


    @Override
    protected void onStop() {
        Task2 t2 = new Task2();
        t2.execute(email);
        super.onStop();
    }

    @Override
    protected void onPause() {
        Task2 t2 = new Task2();
        t2.execute(email);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Task2 t2 = new Task2();
        t2.execute(email);
        super.onDestroy();
    }







    class Task extends AsyncTask<String, String, List<Model>> {

        @Override
        protected List<Model> doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/android_userp.php";
            try {

                String email = strings[0];
                URL url = new URL(page_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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
                String res = buffer.toString(               );

                List<Model> modellist = new ArrayList<>();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");
                for (int i = 0; i < a1.length(); i++) {
                    Model model = new Model();
                    JSONObject o2 = a1.getJSONObject(i);
                    String descr = o2.getString("descr");
                    String name = o2.getString("name");
                    String file = o2.getString("file");
                    String profile = o2.getString("i");
                    String like = o2.getString("like");
                    String id = o2.getString("id");
                    String like_stat = o2.getString("like_stat");
                    String email1 = o2.getString("email");

                    model.setLike(like);
                    model.setEmail(email1);
                    model.setDescr(descr);
                    model.setProfile(profile);
                    model.setName(name);
                    model.setFile("http://studyforfun.000webhostapp.com/" + file);
                    model.setId(id);
                    model.setLike_stat(like_stat);

                    modellist.add(model);
                }

                return modellist;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
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
        protected void onPostExecute(List<Model> result) {
            super.onPostExecute(result);

            CustomAdapter ad = new CustomAdapter(getApplicationContext(), R.layout.modellist, result);
            l1.setAdapter(ad);
            l1.setSelectionFromTop(index, top);





        }
    }

    class Task2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/offline.php";
            try {

                String email = strings[0];
                URL url = new URL(page_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    class CustomAdapter extends ArrayAdapter {
        public List<Model> modellist;
        private int resource;
        private LayoutInflater inflater;


        public CustomAdapter(Context context, int resource, List<Model> objects) {
            super(context, resource, objects);
            modellist = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final int pos1  = l1.getSelectedItemPosition() - l1.getFirstVisiblePosition();
            Log.e("pos1", String.valueOf(pos1));




            index = l1.getFirstVisiblePosition();
            View v = l1.getChildAt(0);
            top = (v == null) ? 0 : (v.getTop() - l1.getPaddingTop());

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.modellist, null);
            }
            TextView t1 = (TextView) convertView.findViewById(R.id.t1);
            ImageView react = (ImageView) convertView.findViewById(R.id.react);
            TextView t2 = (TextView) convertView.findViewById(R.id.t2);
            ImageView i5 = (ImageView) convertView.findViewById(R.id.i5);
            LinearLayout a1 = (LinearLayout) convertView.findViewById(R.id.a1)  ;
            TextView t3 = (TextView) convertView.findViewById(R.id.t3);
            ImageView i3 = (ImageView)convertView.findViewById(R.id.i3);

            String img = modellist.get(position).getFile();
            String ext = img.substring(img.lastIndexOf(".") + 1);
            Log.d(TAG, "Extension: " + ext);
            RelativeLayout video = (RelativeLayout)convertView.findViewById(R.id.video);
            VideoView v1 = (VideoView)convertView.findViewById(R.id.v1);
            video.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            ImageView i1 = (ImageView) convertView.findViewById(R.id.i1);
           final ImageView like1 = (ImageView) convertView.findViewById(R.id.like1);

            final ImageView like2 = (ImageView) convertView.findViewById(R.id.like2);


            like1.setVisibility(View.GONE);
            like2.setVisibility(View.GONE);

            final String[] like_stat = {modellist.get(position).getLike_stat()};
            if (like_stat[0].equals("0")) {
                like1.setVisibility(View.VISIBLE);

            } else if (like_stat[0].equals("1")) {

                like2.setVisibility(View.VISIBLE);
            }

            i5.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(MainActivity.this, Profile.class);
                            i.putExtra("email",modellist.get(position).getEmail());
                            startActivity(i);
                        }
                    }
            );


            like1.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            like_stat[0] = "1";
                            like1.setVisibility(View.GONE);
                            like2.setVisibility(View.VISIBLE);
                            String id = String.valueOf(modellist.get(position).getId());
                            Like1 l = new Like1();
                            l.execute(email,id,id,"");
                            Task t = new Task();
                            t.execute(email);


                        }
                    }
            );
            like2.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            like_stat[0] = "0";
                            like2.setVisibility(View.GONE);
                            like1.setVisibility(View.VISIBLE);
                            String id = String.valueOf(modellist.get(position).getId());
                            Like1 l = new Like1();
                            l.execute(email,id,"",id);
                            Task t = new Task();
                            t.execute(email);



                        }
                    }
            );







            if (ext.equals("jpg") || ext.equals("png") || ext.equals("jpeg")) {
                i1.setVisibility(View.VISIBLE);

                try {
                    ImageLoader.getInstance().displayImage(modellist.get(position).getFile(), i1);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }


            } else if (ext.equals("mp4") || ext.equals("3gp")) {
                video.setVisibility(View.VISIBLE);
                v1.setVisibility(View.VISIBLE);

                v1.setVideoPath(modellist.get(position).getFile());
                v1.start();
                i1.setVisibility(View.GONE);


            } else {

                i1.setVisibility(View.GONE);

            }

            l1.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            search_l.setVisibility(View.GONE);
                            search1.setVisibility(View.GONE);
                            search_view.setVisibility(View.GONE);
                        }
                    }
            );

            t1.setText(modellist.get(position).getName());
            t2.setText(modellist.get(position).getDescr());
            t3.setText(modellist.get(position).getLike());
            Picasso.with(getApplicationContext()).load("http://studyforfun.000webhostapp.com/" + modellist.get(position).getProfile()).into(i5);

           react.setOnClickListener(
                   new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent i = new Intent(MainActivity.this,React.class);
                           i.putExtra("id",modellist.get(position).getId());
                           startActivity(i);
                       }
                   }
           );


           i3.setOnClickListener(
                   new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent i = new Intent(MainActivity.this,Edit.class);
                           i.putExtra("id",modellist.get(position).getId());
                           startActivity(i);
                       }
                   }
           );




           bar.setVisibility(View.GONE);
            return convertView;
        }


    }

    class Like1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/like3.php";
            try {

                String email = strings[0];
                String id = strings[1];
                String id1 = strings[2];
                String id2 = strings[3];
                URL url = new URL(page_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")
                        + "&" +
                        URLEncoder.encode("id1", "UTF-8") + "=" + URLEncoder.encode(id1, "UTF-8")
                        + "&" +
                        URLEncoder.encode("id2", "UTF-8") + "=" + URLEncoder.encode(id2, "UTF-8");
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


                return res;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
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
    }




    class SearchTask1 extends AsyncTask<String, String, List<SearchModel1>> {

        @Override
        protected List<SearchModel1> doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/users1.php";
            try {

                String email = strings[0];
                String query = strings[1];
                URL url = new URL(page_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+
                        "&"+URLEncoder.encode("query","UTF-8") +"="+ URLEncoder.encode(query,"UTF-8");
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

                List<SearchModel1> modellist1 = new ArrayList<>();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");
                for (int i = 0; i < a1.length(); i++) {
                    SearchModel1 model1 = new SearchModel1();
                    JSONObject o2 = a1.getJSONObject(i);
                    String id = o2.getString("id");
                    String name = o2.getString("name");
                    String email1 = o2.getString("email");
                    String profile = o2.getString("profile");
                    model1.setId(id);
                    model1.setEmail(email1);
                    model1.setProfile(profile);
                    model1.setName(name);


                    modellist1.add(model1);
                }

                return modellist1;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e){
                e.printStackTrace();

            }
            catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }

            finally {
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
        protected void onPostExecute(List<SearchModel1> searchModel1s) {
            super.onPostExecute(searchModel1s);
            CustomAdapter12 ad = new CustomAdapter12(getApplicationContext(),R.layout.search1,searchModel1s);
            search_view.setAdapter(ad);

        }
    }




class CustomAdapter12 extends ArrayAdapter {
    public List<SearchModel1> modellist;
    private int resource;
    private LayoutInflater inflater;


    public CustomAdapter12(Context context, int resource, List<SearchModel1> objects) {
        super(context, resource, objects);
        modellist = objects;
        this.resource = resource;
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

    }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.search1, null);
            }

            TextView name = (TextView)convertView.findViewById(R.id.name);
            name.setText(modellist.get(position).getName());
            ImageView profile = (ImageView)convertView.findViewById(R.id.profile);
            ImageLoader.getInstance().displayImage(modellist.get(position).getProfile(), profile);



            search_view.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            search_l.setVisibility(View.GONE);
                            search_view.setVisibility(View.GONE);
                            search1.setVisibility(View.GONE);


                            Intent i2 = new Intent(MainActivity.this, Profile.class);
                            i2.putExtra("email",modellist.get(i).getEmail());
                            startActivity(i2);


                        }
                    }
            );
            return convertView;
        }



    }









}