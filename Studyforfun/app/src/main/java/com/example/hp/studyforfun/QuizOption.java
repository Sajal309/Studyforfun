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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class QuizOption extends AppCompatActivity {

    ListView quiz_view;
     SharedPreferences s;
    String user_email;
    String u_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_option);
        quiz_view = (ListView)findViewById(R.id.quiz_view);
        s = getSharedPreferences("Info",MODE_PRIVATE);
        user_email = s.getString("email","");
        u_name = s.getString("name","");
        QuizTask2 t = new QuizTask2();
        t.execute(user_email);

    }


    class QuizTask2 extends AsyncTask<String, String, List<QuizModel1>> {

        @Override
        protected List<QuizModel1> doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/quiz1.php";
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
                String res = buffer.toString();

                List<QuizModel1> modellist1 = new ArrayList<>();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");
                for (int i = 0; i < a1.length(); i++) {
                    QuizModel1 model1 = new QuizModel1();
                    JSONObject o2 = a1.getJSONObject(i);
                    String name = o2.getString("name");
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
        protected void onPostExecute(List<QuizModel1> quizModel1s) {
            super.onPostExecute(quizModel1s);
            CustomAdapter12 ad = new CustomAdapter12(getApplicationContext(),R.layout.quiz2,quizModel1s);
            quiz_view.setAdapter(ad);
        }
    }




    class CustomAdapter12 extends ArrayAdapter {
        public List<QuizModel1> modellist;
        private int resource;
        private LayoutInflater inflater;


        public CustomAdapter12(Context context, int resource, List<QuizModel1> objects) {
            super(context, resource, objects);
            modellist = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.quiz2, null);
            }

            TextView name = (TextView)convertView.findViewById(R.id.name);

            name.setText(modellist.get(position).getName());
            quiz_view.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent i1  = new Intent(QuizOption.this,QuizActivity.class);
                            i1.putExtra("table",modellist.get(i).getName());
                            startActivity(i1);
                        }
                    }
            );



            return convertView;
        }



    }



}


