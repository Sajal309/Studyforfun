package com.example.hp.studyforfun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;

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
import java.util.HashMap;
import java.util.List;

public class Tutorial extends AppCompatActivity {
   SharedPreferences s;
   String u_name;
   String user_email;
   ListView l5;
   ListView search_view;
   EditText search1;
   ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_tutorial);
        s = getSharedPreferences("Info",MODE_PRIVATE);
        u_name = s.getString("name","");
        user_email = s.getString("email","");
        l5 = (ListView)findViewById(R.id.l5);
        search_view =(ListView)findViewById(R.id.search_view);
        search_view.setVisibility(View.GONE);
       search1 =(EditText) findViewById(R.id.search1);
       bar = (ProgressBar)findViewById(R.id.bar);
        search1.setVisibility(View.GONE);
        Task3 t3 = new Task3();
        t3.execute(user_email,"");

         search1.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                  }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 SearchTask t1 = new SearchTask();
                 t1.execute(user_email,charSequence.toString());
             }

             @Override
             public void afterTextChanged(Editable editable) {
                }
         });

    }

    public void search(View v){
        search1.setVisibility(View.VISIBLE);
        search_view.setVisibility(View.VISIBLE);
    }

    public void panel(View v){
        Intent intent = new Intent(this,Panel.class);
        intent.putExtra("name",u_name);
        intent.putExtra("email",user_email);
        startActivity(intent);
    }

    public void upload(View v){
        Intent intent = new Intent(this,TutorialUpload.class);
        intent.putExtra("name",u_name);
        intent.putExtra("email",user_email);
        startActivity(intent);
    }
    public void drive(View v){
        Intent intent = new Intent(this,Drive.class);
        intent.putExtra("email",user_email);
        startActivity(intent);
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

     class Task3 extends AsyncTask<String, String, List<TutorialModel>> {

        @Override
        protected List<TutorialModel> doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/tutorial.php";
            try {

                String email = strings[0];
                String id1 = strings[1];
                URL url = new URL(page_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+
                        "&"+
                        URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id1, "UTF-8");
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

                List<TutorialModel> modellist = new ArrayList<>();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");
                for (int i = 0; i < a1.length(); i++) {
                    TutorialModel model = new TutorialModel();
                    JSONObject o2 = a1.getJSONObject(i);
                    String descr = o2.getString("descr");
                    String name = o2.getString("name");
                    String file = o2.getString("file");
                    String profile = o2.getString("profile");
                    String votes = o2.getString("up");
                    String id = o2.getString("id");
                    String downvotes = o2.getString("down");
                    String subs = o2.getString("subs");
                    String up_stat = o2.getString("up_stat");
                    String down_stat = o2.getString("down_stat");
                    String views = o2.getString("views");
                    String sub_stat = o2.getString("sub_stat");
                    String email1 = o2.getString("email");
                    String time1 = o2.getString("time1");
                    String time2 = o2.getString("time2");

                    model.setVotes(votes);
                    model.setDescr(descr);
                    model.setProfile("http://studyforfun.000webhostapp.com/" + profile);
                    model.setName(name);
                    model.setFile("http://studyforfun.000webhostapp.com/" + file);
                    model.setId(id);
                    model.setDownvotes(downvotes);
                    model.setSubs(subs);
                    model.setUp_stat(up_stat);
                    model.setDown_stat(down_stat);
                    model.setViews(views);
                    model.setSub_stat(sub_stat);
                    model.setEmail(email1);
                    model.setTime1(time1);
                    model.setTime2(time2);
                    modellist.add(model);
                }

                return modellist;


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
        protected void onPostExecute(final List<TutorialModel> tutorialModels) {
            super.onPostExecute(tutorialModels);

            CustomAdapter8 ad = new CustomAdapter8(Tutorial.this,R.layout.tutorial,tutorialModels);
            l5.setAdapter(ad);
            int index = l5.getFirstVisiblePosition();
            View v = l5.getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - l5.getPaddingTop());
            l5.setSelectionFromTop(index, top);


        }
    }




    class CustomAdapter8 extends ArrayAdapter {
        public List<TutorialModel> modellist;
        private int resource;
        private LayoutInflater inflater;


        public CustomAdapter8(Context context, int resource, List<TutorialModel> objects) {
            super(context, resource, objects);
            modellist = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.tutorial, null);
            }




            TextView descr = (TextView)convertView.findViewById(R.id.descr);
            TextView name = (TextView)convertView.findViewById(R.id.name);
            TextView time = (TextView)convertView.findViewById(R.id.time);
            TextView votes = (TextView)convertView.findViewById(R.id.votes);
            ImageView file = (ImageView)convertView.findViewById(R.id.file);
            ImageView profile = (ImageView)convertView.findViewById(R.id.profile);
            VideoView v1 = (VideoView)convertView.findViewById(R.id.v2);
            RelativeLayout video = (RelativeLayout)convertView.findViewById(R.id.video1);
            video.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            file.setVisibility(View.GONE);

             descr.setText(modellist.get(position).getDescr());
             name.setText(modellist.get(position).getName());
             time.setText(modellist.get(position).getTime1());
             votes.setText(modellist.get(position).getVotes()+" votes");

             String file1  = modellist.get(position).getFile();
             String ext = file1.substring(file1.lastIndexOf(".")+1);
             if(ext.equals("jpg") || ext.equals("png") || ext.equals("jpeg")){
                 try {
                     file.setVisibility(View.VISIBLE);
                     ImageLoader.getInstance().displayImage(modellist.get(position).getFile(), file);
                 }
                 catch (OutOfMemoryError e){
                     e.printStackTrace();
                 }

             }

             else if(ext.equals("mp4") || ext.equals("3gp") || ext.equals("wav")){
                 file.setVisibility(View.VISIBLE);
                 String path = modellist.get(position).getFile();

                 try {

                     Bitmap bitmap = null;
                     MediaMetadataRetriever mediaMetadataRetriever = null;

                         mediaMetadataRetriever = new MediaMetadataRetriever();
                         if (Build. VERSION.SDK_INT >= 14)
                             mediaMetadataRetriever.setDataSource(path, new HashMap<String, String>());
                         else
                             mediaMetadataRetriever.setDataSource(path);
                         bitmap = mediaMetadataRetriever.getFrameAtTime(6);
                         file.setImageBitmap(bitmap);

                 }
                 catch (OutOfMemoryError e){
                     e.printStackTrace();
                 } catch (Throwable throwable) {
                     throwable.printStackTrace();
                 }

             }

             else if(ext.equals("pdf") || ext.equals("doc") || ext.equals("word")){
                 try {
                     file.setVisibility(View.VISIBLE);
                     file.setImageResource(R.drawable.doc);
                 }
                 catch (OutOfMemoryError e){
                     e.printStackTrace();
                 }



             }


             try {
                 ImageLoader.getInstance().displayImage(modellist.get(position).getProfile(), profile);
             }
             catch (OutOfMemoryError e){
                 e.printStackTrace();
             }

            l5.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            search1.setVisibility(View.GONE);
                            search_view.setVisibility(View.GONE);
                            Intent i2 = new Intent(Tutorial.this,MainTutorial.class);
                            i2.putExtra("id",modellist.get(i).getId());
                            i2.putExtra("name",modellist.get(i).getName());
                            i2.putExtra("email",modellist.get(i).getEmail());
                            i2.putExtra("profile",modellist.get(i).getProfile());
                            i2.putExtra("file",modellist.get(i).getFile());
                            i2.putExtra("up",modellist.get(i).getVotes());
                            i2.putExtra("down",modellist.get(i).getDownvotes());
                            i2.putExtra("descr",modellist.get(i).getDescr());
                            i2.putExtra("subs",modellist.get(i).getSubs());
                             i2.putExtra("up_stat",modellist.get(i).getUp_stat());
                            i2.putExtra("down_stat",modellist.get(i).getDown_stat());
                            i2.putExtra("views", modellist.get(i).getViews());
                            i2.putExtra("sub_stat",modellist.get(i).getSub_stat());
                            i2.putExtra("time",modellist.get(i).getTime1());

                            startActivity(i2);

                        }
                    }
            );

             bar.setVisibility(View.GONE);

            return convertView;
        }



    }


    class SearchTask extends AsyncTask<String, String, List<SearchModel>> {

        @Override
        protected List<SearchModel> doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/tutorial4.php";
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

                List<SearchModel> modellist1 = new ArrayList<>();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");
                for (int i = 0; i < a1.length(); i++) {
                    SearchModel model1 = new SearchModel();
                    JSONObject o2 = a1.getJSONObject(i);
                    String id = o2.getString("id");
                    String descr = o2.getString("descr");
                    model1.setDescr(descr);
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
        protected void onPostExecute(final List<SearchModel> tutorialModels) {
            super.onPostExecute(tutorialModels);

            CustomAdapter9 ad = new CustomAdapter9(Tutorial.this,R.layout.search,tutorialModels);
            search_view.setAdapter(ad);

        }
    }




    class CustomAdapter9 extends ArrayAdapter {
        public List<SearchModel> modellist;
        private int resource;
        private LayoutInflater inflater;


        public CustomAdapter9(Context context, int resource, List<SearchModel> objects) {
            super(context, resource, objects);
            modellist = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.search, null);
            }

            TextView text = (TextView)convertView.findViewById(R.id.descr);
            text.setText(modellist.get(position).getDescr());
            search_view.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            search_view.setVisibility(View.GONE);
                            search1.setVisibility(View.GONE);
                            Task3 t = new Task3();
                            t.execute(user_email,modellist.get(i).getId());

                        }
                    }
            );
            return convertView;
        }



    }



}
