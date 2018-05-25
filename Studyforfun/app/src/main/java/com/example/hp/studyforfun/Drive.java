package com.example.hp.studyforfun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class Drive extends AppCompatActivity {

    GridView grid;
    LinearLayout new_l;
    EditText folder_name;
    EditText search1;
    ListView search_view;
    LinearLayout search_l;
    SharedPreferences s;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        s = getSharedPreferences("Info",MODE_PRIVATE);
        email = s.getString("email","");

        grid = (GridView)findViewById(R.id.grid);
        new_l = (LinearLayout)findViewById(R.id.new_l);
        folder_name = (EditText)findViewById(R.id.folder_name);
        new_l.setVisibility(View.GONE);
        DriveTask t = new DriveTask();
        t.execute(email,"");
        search1 = (EditText)findViewById(R.id.search1);
        search_view = (ListView) findViewById(R.id.search_view);
        search_l = (LinearLayout)findViewById(R.id.search_l);
        search1();

        search1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               SearchTask2 t = new SearchTask2();
                t.execute(email,charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    public void search(View v){
        search_l.setVisibility(View.VISIBLE);
        search1.setVisibility(View.VISIBLE);
        search_view.setVisibility(View.VISIBLE);

    }

public void search1(){
        search_l.setVisibility(View.GONE);
        search1.setVisibility(View.GONE);
        search_view.setVisibility(View.GONE);

    }

    public void new_f(View v){
        new_l.setVisibility(View.VISIBLE);
    }
    public void folder(View v){
        String name = folder_name.getText().toString();
        DriveTask t = new DriveTask();
        t.execute(email,name);
        new_l.setVisibility(View.GONE);


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
        Intent i = new Intent(this, QuizActivity.class);
        startActivity(i);

    }


    class DriveTask extends AsyncTask<String, Void, List<DriveModel>> {


        @Override
        protected List<DriveModel> doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/folder.php";
            try {

                String email = strings[0];
                String fo_name = strings[1];

                URL url = new URL(page_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+"&"+
                        URLEncoder.encode("f_name", "UTF-8") + "=" + URLEncoder.encode(fo_name, "UTF-8");
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

                List<DriveModel> modellist = new ArrayList<>();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");
            for(int i=0; i<a1.length(); i++) {
                 DriveModel model = new DriveModel();

                JSONObject o2 = a1.getJSONObject(i);
                String id = o2.getString("id");
                String f_name = o2.getString("folder");
                String time = o2.getString("time");
                model.setF_name(f_name);
                model.setId(id);
                model.setTime(time);



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
        protected void onPostExecute(List<DriveModel> driveModels) {
            super.onPostExecute(driveModels);
            GridAdapter ad = new GridAdapter(getApplicationContext(),R.layout.folder,driveModels);

          grid.setAdapter(ad);
        }
    }



    class GridAdapter extends BaseAdapter {


        private List<DriveModel> list;
        private int res;
        private LayoutInflater inflater;


        public GridAdapter(Context context,int res,List<DriveModel> object) {

            this.res = res;
            list = object;
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null){
                view = inflater.inflate(R.layout.folder,null);
            }

            ImageView f_image = (ImageView)view.findViewById(R.id.f_image);
            TextView f_text = (TextView)view.findViewById(R.id.f_name);
            f_text.setText(list.get(i).getF_name());

            grid.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            search1();
                            Intent i1 = new Intent(Drive.this,Files.class);
                            i1.putExtra("folder",list.get(i).getF_name());
                            startActivity(i1);
                        }
                    }
            );


            return view;
        }
    }
    class SearchTask2 extends AsyncTask<String, String, List<SearchModel2>> {

        @Override
        protected List<SearchModel2> doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/drive1.php";
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

                List<SearchModel2> modellist1 = new ArrayList<>();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");
                for (int i = 0; i < a1.length(); i++) {
                    SearchModel2 model1 = new SearchModel2();
                    JSONObject o2 = a1.getJSONObject(i);
                    String id = o2.getString("id");
                    String fo_name = o2.getString("fo_name");
                    model1.setId(id);
                    model1.setFo_name(fo_name);

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
        protected void onPostExecute(List<SearchModel2> searchModel2s) {
            super.onPostExecute(searchModel2s);
            CustomAdapter13 ad = new CustomAdapter13(getApplicationContext(),R.layout.search2,searchModel2s);
            search_view.setAdapter(ad);
        }
    }





    class CustomAdapter13 extends ArrayAdapter {
        public List<SearchModel2> modellist;
        private int resource;
        private LayoutInflater inflater;


        public CustomAdapter13(Context context, int resource, List<SearchModel2> objects) {
            super(context, resource, objects);
            modellist = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.search2, null);
            }

            TextView name = (TextView)convertView.findViewById(R.id.name);
            name.setText(modellist.get(position).getFo_name());



            search_view.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(Drive.this,Files.class);
                            intent.putExtra("folder",modellist.get(i).getFo_name());
                            startActivity(intent);
                            search_view.setVisibility(View.GONE);
                            search1.setVisibility(View.GONE);


                        }
                    }
            );
            return convertView;
        }



    }











}
