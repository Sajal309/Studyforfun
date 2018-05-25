package com.example.hp.studyforfun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

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

public class React extends AppCompatActivity {
    EditText reaction;
    ListView react_view;
    String i_id;
   SharedPreferences s;
   String user_email;
   String u_name;
   LinearLayout wait;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_react);
        reaction = (EditText)findViewById(R.id.reaction);
        react_view = (ListView)findViewById(R.id.react_view);
        wait = (LinearLayout)findViewById(R.id.wait);
        i_id = getIntent().getStringExtra("id");
        s = getSharedPreferences("Info",MODE_PRIVATE);
        user_email = s.getString("email","");
        u_name = s.getString("name","");
        ReactTask t = new ReactTask();


        t.execute(user_email,i_id,"","");

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    public void send(View v){
        String reaction1 = reaction.getText().toString();
        ReactTask t = new ReactTask();
        t.execute(user_email,i_id,reaction1,"");
        reaction.setText("");
    }
    public  void back(View v){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("name",u_name);
        intent.putExtra("email",user_email);
        startActivity(intent);
    }


    class ReactTask extends AsyncTask<String, String, List<ReactClass>> {

        @Override
        protected List<ReactClass> doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/react.php";
            try {

                String email = strings[0];
                String id = strings[1];
                String comment = strings[2];
                String id2 = strings[3];

                URL url = new URL(page_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+
                        "&"+URLEncoder.encode("id","UTF-8") +"="+ URLEncoder.encode(id,"UTF-8")
                        +
                        "&"+URLEncoder.encode("comment","UTF-8") +"="+ URLEncoder.encode(comment,"UTF-8")
                        +
                        "&"+URLEncoder.encode("id2","UTF-8") +"="+ URLEncoder.encode(id2,"UTF-8");
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

                List<ReactClass> modellist1 = new ArrayList<>();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");
                for (int i = 0; i < a1.length(); i++) {
                    ReactClass model1 = new ReactClass();
                    JSONObject o2 = a1.getJSONObject(i);
                    String id1 = o2.getString("id");
                    String i_id = o2.getString("i_id");
                    String email1 = o2.getString("email");
                    String comment1 = o2.getString("comment");
                    String name1 = o2.getString("name");
                    String profile = o2.getString("profile");
                    String time1 = o2.getString("time");
                    model1.setComment(comment1);
                    model1.setEmail(email1);
                    model1.setI_id(i_id);
                    model1.setName(name1);
                    model1.setProfile("http://studyforfun.000webhostapp.com/"+profile);
                    model1.setTime(time1);
                    model1.setI_id(id1);


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
        protected void onPostExecute(List<ReactClass> reactClasses) {
            super.onPostExecute(reactClasses);
            CustomAdapter11 ad = new CustomAdapter11(getApplicationContext(),R.layout.reaction,reactClasses);
            react_view.setAdapter(ad);
        }
    }




    class CustomAdapter11 extends ArrayAdapter {
        public List<ReactClass> modellist;
        private int resource;
        private LayoutInflater inflater;


        public CustomAdapter11(Context context, int resource, List<ReactClass> objects) {
            super(context, resource, objects);
            modellist = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.reaction, null);
            }

            TextView name = (TextView)convertView.findViewById(R.id.name);
            TextView reactions = (TextView)convertView.findViewById(R.id.react);
            ImageView user = (ImageView)convertView.findViewById(R.id.user);
            TextView time = (TextView)convertView.findViewById(R.id.time);

            name.setText(modellist.get(position).getName());
            reactions.setText(modellist.get(position).getComment());
            ImageLoader.getInstance().displayImage(modellist.get(position).getProfile(), user);
            time.setText(modellist.get(position).getTime());

            react_view.setLongClickable(true);
            react_view.setOnItemLongClickListener(
                    new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            ReactTask t = new ReactTask();

                            t.execute(user_email,i_id,"",modellist.get(i).getI_id());


                            return true;
                        }
                    }
            );


            wait.setVisibility(View.GONE);

            return convertView;
        }



    }



}


