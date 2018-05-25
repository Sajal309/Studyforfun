package com.example.hp.studyforfun;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private ListView l3;
   SharedPreferences s;
   String user_email;
   String u_name;
   ListView search_view;
   EditText search1;
   ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_message);

        l3 = (ListView) findViewById(R.id.l3);
        Task t = new Task();
        s = getSharedPreferences("Info",MODE_PRIVATE);
         user_email = s.getString("email","");
       u_name = s.getString("name","");
       search_view = (ListView)findViewById(R.id.search_view);
       search1 = (EditText)findViewById(R.id.search1);
        search_view.setVisibility(View.GONE);
       search1.setVisibility(View.GONE);
     bar = (ProgressBar)findViewById(R.id.bar);
        t.execute(u_name,user_email,"","");
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
                SearchTask1 t1 = new SearchTask1();
                t1.execute(user_email,charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }



    public void all(View v){
        Task t = new Task();
        t.execute(u_name,user_email,"","");
    }
    public void active(View v){
        Task t = new Task();
        t.execute(u_name,user_email,"1","");
    }
    public void suggest(View v){
        Task t = new Task();
        t.execute(u_name,user_email,"","1");

    }

    public void search(View v){
        search1.setVisibility(View.VISIBLE);
        search_view.setVisibility(View.VISIBLE);

    }



    public void home1(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("name",u_name);
        intent.putExtra("email",user_email);
        startActivity(intent);

    }
    public void chat1(View view) {
        Intent i = new Intent(this,MessageActivity.class);
        startActivity(i);

    }
    public void tutorial1(View view) {
        Intent i = new Intent(this,Tutorial.class);
        startActivity(i);

    }
    public void quiz1(View view) {
        Intent i = new Intent(this,QuizOption.class);
        startActivity(i);

    }
    public void upload1(View view) {
        Intent i = new Intent(this, Upload.class);
        startActivity(i);

    }



    public void home(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    class Task extends AsyncTask<String, String, List<MessageModel>> {

        @Override
        protected List<MessageModel> doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String msg_url = "http://studyforfun.000webhostapp.com/message.php";
            try {
                String user_name = strings[0];
                String email = strings[1];
                String active = strings[2];
                String more = strings[3];
                URL url = new URL(msg_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                        + "&"
                        + URLEncoder.encode("active", "UTF-8") + "=" + URLEncoder.encode(active, "UTF-8")
                        + "&"
                        + URLEncoder.encode("more", "UTF-8") + "=" + URLEncoder.encode(more, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = x.getInputStream();
                br = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuffer buffer = new StringBuffer();
                String line="";
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                br.close();
                inputStream.close();
                x.disconnect();
                String res = buffer.toString();

                List<MessageModel> modellist = new ArrayList<>();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");
                for (int i = 0; i < a1.length(); i++) {
                    MessageModel model = new MessageModel();
                    JSONObject o2 = a1.getJSONObject(i);
                    String name = o2.getString("name");
                    String profile = o2.getString("profile");

                    String user_email = o2.getString("email");
                    String on_off = o2.getString("on_off");

                    model.setProfile("http://studyforfun.000webhostapp.com/" + profile);
                    model.setName(name);
                    model.setOn_off(on_off);

                    model.setEmail(user_email);

                    modellist.add(model);
                }

                return modellist;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
        protected void onPostExecute(final List<MessageModel> messageModels) {
            super.onPostExecute(messageModels);

            CustomAdapter2 ad = new CustomAdapter2(getApplicationContext(), R.layout.chat_list, messageModels);
            l3.setAdapter(ad);

            l3.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent1 = new Intent(MessageActivity.this,chat.class);
                            intent1.putExtra("email",messageModels.get(i).getEmail());
                            intent1.putExtra("name",messageModels.get(i).getName());
                            intent1.putExtra("profile",messageModels.get(i).getProfile());

                            startActivity(intent1);
                        }
                        }
            );


        }
    }


    class CustomAdapter2 extends ArrayAdapter {
        public List<MessageModel> modellist;
        private int resource;
        private LayoutInflater inflater;


        public CustomAdapter2(Context context, int resource, List<MessageModel> objects) {
            super(context, resource, objects);
            modellist = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {



            if (convertView == null) {
                convertView = inflater.inflate(R.layout.chat_list, null);
            }



            TextView t4 = (TextView) convertView.findViewById(R.id.t4);
            TextView t5 = (TextView) convertView.findViewById(R.id.t5);
            TextView t6 = (TextView) convertView.findViewById(R.id.t6);
            ImageView i6 = (ImageView)convertView.findViewById(R.id.i6);
            ImageView on = (ImageView)convertView.findViewById(R.id.on);
            ImageView off= (ImageView)convertView.findViewById(R.id.off);
            LinearLayout off_l = (LinearLayout)convertView.findViewById(R.id.off_l);
            on.setVisibility(View.GONE);
            off.setVisibility(View.GONE);
            off_l.setVisibility(View.GONE);
            if(modellist.get(position).getOn_off().equals("1")){
                on.setVisibility(View.VISIBLE);

            }
            if(modellist.get(position).getOn_off().equals("0")){
                off_l.setVisibility(View.VISIBLE);
                off.setVisibility(View.VISIBLE);

            }


            t4.setText(modellist.get(position).getName());

            ImageLoader.getInstance().displayImage(modellist.get(position).getProfile(), i6);
           bar.setVisibility(View.GONE);
            return convertView;
        }


    }

    class SearchTask1 extends AsyncTask<String, String, List<SearchModel4>> {

        @Override
        protected List<SearchModel4> doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/message1.php";
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

                List<SearchModel4> modellist1 = new ArrayList<>();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");
                for (int i = 0; i < a1.length(); i++) {
                    SearchModel4 model1 = new SearchModel4();
                    JSONObject o2 = a1.getJSONObject(i);
                    String id = o2.getString("id");
                    String email1 = o2.getString("email");
                    String name1 = o2.getString("name");
                    String profile = o2.getString("profile");
                    model1.setEmail(email1);
                    model1.setName(name1);
                    model1.setProfile("http://studyforfun.000webhostapp.com/"+profile);
                    model1.setId(id);
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
        protected void onPostExecute(final List<SearchModel4> tutorialModels) {
            super.onPostExecute(tutorialModels);

            CustomAdapter10 ad = new CustomAdapter10(getApplicationContext(),R.layout.search1,tutorialModels);
            search_view.setAdapter(ad);

        }
    }




    class CustomAdapter10 extends ArrayAdapter {
        public List<SearchModel4> modellist;
        private int resource;
        private LayoutInflater inflater;


        public CustomAdapter10(Context context, int resource, List<SearchModel4> objects) {
            super(context, resource, objects);
            modellist = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public View getView(final int i1, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.search1, null);
            }

            TextView name = (TextView)convertView.findViewById(R.id.name);
            name.setText(modellist.get(i1).getName());

            search_view.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            search1.setVisibility(View.GONE);
                            search_view.setVisibility(View.GONE);

                            Intent intent = new Intent(MessageActivity.this,chat.class);
                            intent.putExtra("email",modellist.get(i).getEmail());
                            intent.putExtra("name",modellist.get(i).getName());
                            intent.putExtra("profile",modellist.get(i).getProfile());
                            startActivity(intent);


                        }
                    }
            );


            return convertView;
        }



    }



}
