package com.example.hp.studyforfun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.ImageLoader;
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

import static android.content.ContentValues.TAG;

public class QuizActivity extends AppCompatActivity {

     String table;
     String  num = "1";
    SharedPreferences s;
    String u_name;
    String u_email;
    ListView l6;
    TextView next,previous;

    final int[] num1 = {1};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        s = getSharedPreferences("Info", Context.MODE_PRIVATE);
        table = getIntent().getStringExtra("table");
        u_name = s.getString("name", "");
        u_email = s.getString("email", "");
        l6 = (ListView) findViewById(R.id.l6);
        next = (TextView)findViewById(R.id.next);
        previous = (TextView)findViewById(R.id.previous);


        QuizTask t = new QuizTask();
        t.execute(u_email,"1","",table);
       previous.setVisibility(View.VISIBLE);


        next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        num1[0] = num1[0] +1;
                        previous.setVisibility(View.VISIBLE);
                        QuizTask t = new QuizTask();
                        t.execute(u_email,String.valueOf(num1[0]),"",table);

                    }
                }
        );
        previous.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        num1[0] = num1[0] -1;
                        if(num1[0] == 1){
                             previous.setVisibility(View.GONE);
                        }
                        if(num1[0] >= 1) {
                            QuizTask t = new QuizTask();
                            t.execute(u_email, String.valueOf(num1[0]),"",table);
                        }
                    }
                }
        );


    }

    public void result(View v){
        Intent intent = new Intent(this,Result.class);
        intent.putExtra("table",table);
        intent.putExtra("email",u_email);
        startActivity(intent);
    }

    public void quit(View v){
             Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("name",u_name);
            intent.putExtra("email",u_email);
            startActivity(intent);

        }




    class QuizTask extends AsyncTask<String, Void, List<QuizModel>> {


        @Override
        protected List<QuizModel> doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/quiz.php";
            try {

                String email = strings[0];
                String n = strings[1];
                String ans1 = strings[2];
                String table = strings[3];
                URL url = new URL(page_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+"&"+
                        URLEncoder.encode("n", "UTF-8") + "=" + URLEncoder.encode(n, "UTF-8")
                        +"&"+
                        URLEncoder.encode("ans", "UTF-8") + "=" + URLEncoder.encode(ans1, "UTF-8")
                        +"&"+
                        URLEncoder.encode("table", "UTF-8") + "=" + URLEncoder.encode(table, "UTF-8") ;
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

                List<QuizModel> modellist = new ArrayList<>();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");

                 QuizModel model = new QuizModel();
                 for(int i = 0; i<a1.length(); i++) {
                     JSONObject o2 = a1.getJSONObject(i);
                     String num = o2.getString("num");
                     String que = o2.getString("que");
                     String a = o2.getString("a");
                     String b = o2.getString("b");
                     String c = o2.getString("c");
                     String d = o2.getString("d");
                     String id = o2.getString("id");
                     String file = o2.getString("file");
                     String ans = o2.getString("ans");
                     String stat = o2.getString("stat");
                     model.setStat(stat);
                     model.setNum(num);
                     model.setQue(que);
                     model.setA(a);
                     model.setB(b);
                     model.setC(c);
                     model.setD(d);
                     model.setId(id);
                     model.setAns(ans);
                     model.setFile("http://studyforfun.000webhostapp.com/" + file);


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
        protected void onPostExecute(List<QuizModel> quizModels) {
            super.onPostExecute(quizModels);
            CustomAdapter9 ad = new CustomAdapter9(getApplicationContext(), R.layout.quiz, quizModels);
            l6.setAdapter(ad);
        }
    }

    class CustomAdapter9 extends ArrayAdapter {
        public List<QuizModel> modellist;
        private int resource;
        private LayoutInflater inflater;


        public CustomAdapter9(Context context, int resource, List<QuizModel> objects) {
            super(context, resource, objects);
            modellist = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.quiz, null);
            }




            LinearLayout ans_l = (LinearLayout)convertView.findViewById(R.id.ans_l);
            LinearLayout answered = (LinearLayout)convertView.findViewById(R.id.answered);
            TextView que = (TextView)convertView.findViewById(R.id.que);
            final TextView num = (TextView)convertView.findViewById(R.id.num);
            final Button a = (Button) convertView.findViewById(R.id.a);
            final Button b = (Button) convertView.findViewById(R.id.b);
           final Button c = (Button)convertView.findViewById(R.id.c);
           final Button d = (Button)convertView.findViewById(R.id.d);
           ImageView file = (ImageView)convertView.findViewById(R.id.file);

           file.setVisibility(View.VISIBLE);

         if(modellist.get(position).getStat().equals("1")){

          ans_l.setVisibility(View.GONE);
          answered.setVisibility(View.VISIBLE);
         }
         else if(modellist.get(position).getStat().equals("0")){
            ans_l.setVisibility(View.VISIBLE);
            answered.setVisibility(View.GONE);
         }

          que.setText(modellist.get(position).getQue());
          num.setText(modellist.get(position).getNum());
          a.setText(modellist.get(position).getA());
          b.setText(modellist.get(position).getB());
          c.setText(modellist.get(position).getC());
          d.setText(modellist.get(position).getD());
            if(modellist.get(position).getFile().equals("http://studyforfun.000webhostapp.com/")){
                file.setVisibility(View.GONE);
            }
            else{
                file.setVisibility(View.VISIBLE);
            }
            ImageLoader.getInstance().displayImage(modellist.get(position).getFile(), file);







          final String ans1 = modellist.get(position).getAns();
            a.setOnClickListener(
                  new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          if(ans1 != null && ans1.equals("a")){
                              a.setBackgroundColor(Color.parseColor("#41c841"));



                          }
                          else{
                              a.setBackgroundColor(Color.parseColor("#c84141"));
                              if("b".equals(ans1)){
                                  b.setBackgroundColor(Color.parseColor("#41c841"));

                              } if("c".equals(ans1)){
                                  c.setBackgroundColor(Color.parseColor("#41c841"));

                              } if("d".equals(ans1)){
                                  d.setBackgroundColor(Color.parseColor("#41c841"));

                              }


                          }

                          QuizTask1 t = new QuizTask1();
                          t.execute(u_email, String.valueOf(num1[0]),"a",table);
                      }
                  }
          );
b.setOnClickListener(
                  new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          if(ans1 != null && ans1.equals("b")){
                               b.setBackgroundColor(Color.parseColor("#41c841"));



                          }
                          else{
                              b.setBackgroundColor(Color.parseColor("#c84141"));
                              if("a".equals(ans1)){
                                  a.setBackgroundColor(Color.parseColor("#41c841"));

                              } if("c".equals(ans1)){
                                  c.setBackgroundColor(Color.parseColor("#41c841"));

                              } if("d".equals(ans1)){
                                  d.setBackgroundColor(Color.parseColor("#41c841"));

                              }


                          }
                          QuizTask1 t = new QuizTask1();
                          t.execute(u_email,String.valueOf(num1[0]),"b",table);

                      }
                  }
          );
c.setOnClickListener(
                  new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          if(ans1 != null && ans1.equals("c")){
                              c.setBackgroundColor(Color.parseColor("#41c841"));



                          }
                          else{
                              c.setBackgroundColor(Color.parseColor("#c84141"));
                              if("b".equals(ans1)){
                                  b.setBackgroundColor(Color.parseColor("#41c841"));

                              } if("a".equals(ans1)){
                                  a.setBackgroundColor(Color.parseColor("#41c841"));

                              } if("d".equals(ans1)){
                                  d.setBackgroundColor(Color.parseColor("#41c841"));

                              }



                          }
                          QuizTask1 t = new QuizTask1();
                          t.execute(u_email,String.valueOf(num1[0]),"c",table);
                      }
                  }
          );

d.setOnClickListener(
                  new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          if(ans1 != null && ans1.equals("d")){
                              d.setBackgroundColor(Color.parseColor("#41c841"));



                          }
                          else{
                              d.setBackgroundColor(Color.parseColor("#c84141"));
                              if("b".equals(ans1)){
                                  b.setBackgroundColor(Color.parseColor("#41c841"));

                              } if("c".equals(ans1)){
                                  c.setBackgroundColor(Color.parseColor("#41c841"));

                              } if("a".equals(ans1)){
                                  a.setBackgroundColor(Color.parseColor("#41c841"));

                              }


                          }
                          QuizTask1 t = new QuizTask1();
                          t.execute(u_email,String.valueOf(num1[0]),"d",table);

                      }
                  }
          );


            return convertView;
        }


    }


    class QuizTask1 extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/quiz.php";
            try {

                String email = strings[0];
                String n = strings[1];
                String ans1 = strings[2];
                String table = strings[3];
                
                URL url = new URL(page_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+"&"+
                        URLEncoder.encode("n", "UTF-8") + "=" + URLEncoder.encode(n, "UTF-8")
                        +"&"+
                        URLEncoder.encode("ans", "UTF-8") + "=" + URLEncoder.encode(ans1, "UTF-8")
                        +"&"+
                        URLEncoder.encode("table", "UTF-8") + "=" + URLEncoder.encode(table, "UTF-8") ;
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


                return null;


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


