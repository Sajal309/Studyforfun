package com.example.hp.studyforfun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Files extends AppCompatActivity {
  GridView grid;
  WebView web2;
  ImageView cancel1;
  String folder;
  LinearLayout del_l;
  ImageView del;
  TextView del1;
  EditText search1;
  ListView search_view;
  LinearLayout search_l;
  SharedPreferences s;
  String email;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {

                            results = new Uri[]{Uri.parse(mCM)};

                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};


                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {
            if (requestCode == FCR) {
                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        s = getSharedPreferences("Info",MODE_PRIVATE);
        email = s.getString("email","");

        grid = (GridView)findViewById(R.id.grid);
        folder = getIntent().getStringExtra("folder");
        web2 = (WebView)findViewById(R.id.web2);
        cancel1 = (ImageView)findViewById(R.id.cancel1);
        cancel1.setVisibility(View.GONE);
        WebSettings settings = web2.getSettings();
        settings.setJavaScriptEnabled(true);
        web2.loadUrl("http://studyforfun.000webhostapp.com/files.php?name="+folder+"&email="+email);
        search1 = (EditText)findViewById(R.id.search1);
        search_view = (ListView) findViewById(R.id.search_view);
        search_l = (LinearLayout)findViewById(R.id.search_l);
        search1();


        FileTask t = new FileTask();
        t.execute(email,folder,"");
        web2.setWebChromeClient(new WebChromeClient() {
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(Files.this.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch (IOException ex) {
                        Log.e(TAG, "Image file creation failed", ex);
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);
                return true;
            }
        });

        web2.setVisibility(View.GONE);




        del_l = (LinearLayout)findViewById(R.id.del_l);
        del = (ImageView)findViewById(R.id.del);
        del1 = (TextView)findViewById(R.id.del1);


        del_l.setVisibility(View.GONE);

        search1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               SearchTask3 t = new SearchTask3();
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


    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }


    @Override
    public void onBackPressed() {

        if (web2.canGoBack()) {
            web2.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public void cancel1(View v){
         FileTask t = new FileTask();
        t.execute(email,folder,"");
        web2.setVisibility(View.GONE);
        cancel1.setVisibility(View.GONE);

    }

    public void upload1(View v){
        web2.setVisibility(View.VISIBLE);
        cancel1.setVisibility(View.VISIBLE);
    }


    class FileTask extends AsyncTask<String, Void, List<FileModel>> {


        @Override
        protected List<FileModel> doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/file.php";
            try {

                String email = strings[0];
                String folder = strings[1];
                String del = strings[2];

                URL url = new URL(page_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+"&"+
                        URLEncoder.encode("folder", "UTF-8") + "=" + URLEncoder.encode(folder, "UTF-8")
                        +"&"+
                        URLEncoder.encode("del", "UTF-8") + "=" + URLEncoder.encode(del, "UTF-8");
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

                List<FileModel> modellist = new ArrayList<>();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");
                for(int i=0; i<a1.length(); i++) {
                    FileModel model = new FileModel();

                    JSONObject o2 = a1.getJSONObject(i);
                    String id = o2.getString("id");
                    String name = o2.getString("name");
                    String time = o2.getString("time");
                    model.setName(name);
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
        protected void onPostExecute(List<FileModel> fileModels) {
            super.onPostExecute(fileModels);
            GridAdapter1 ad = new GridAdapter1(getApplicationContext(),R.layout.files,fileModels);
            grid.setAdapter(ad);
        }
    }


    class GridAdapter1 extends BaseAdapter {


        private List<FileModel> list;
        private int res;
        private LayoutInflater inflater;


        public GridAdapter1(Context context, int res, List<FileModel> object) {

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
                view = inflater.inflate(R.layout.files,null);
            }

            ImageView f_image = (ImageView)view.findViewById(R.id.f_image);
            TextView f_text = (TextView)view.findViewById(R.id.f_name);


            String image = list.get(i).getName();
            String ext = image.substring(image.lastIndexOf(".")+1);
            if(ext.equals("jpg") || ext.equals("png") || ext.equals("jpeg")) {
                com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage("http://studyforfun.000webhostapp.com/" + image, f_image);
            }
            else if(ext.equals("mp4") || ext.equals("3gp")){
                f_image.setImageResource(R.drawable.video);

            }
            else if(ext.equals("mp3") || ext.equals("ogg")){
                f_image.setImageResource(R.drawable.audio);

            }
            else if(ext.equals("pdf")){
                f_image.setImageResource(R.drawable.pdf);

            } else if (ext.equals("gif")) {
                f_image.setImageResource(R.drawable.gif);
            }



             grid.setOnItemClickListener(
                     new AdapterView.OnItemClickListener() {
                         @Override
                         public void onItemClick(AdapterView<?> adapterView, View view, int i2, long l) {
                             Intent i1 = new Intent(Intent.ACTION_VIEW,Uri.parse("http://studyforfun.000webhostapp.com/"+list.get(i2).getName()));
                             startActivity(i1);
                             search1();
                         }
                     }
             );

            grid.setLongClickable(true);
           grid.setOnItemLongClickListener(
                   new AdapterView.OnItemLongClickListener() {
                       @Override
                       public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                           del_l.setVisibility(View.VISIBLE);
                           del1.setOnClickListener(
                                   new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           FileTask t = new FileTask();
                                           t.execute("a123",folder,list.get(i).getId());
                                           del_l.setVisibility(View.GONE);
                                       }
                                   }
                           );

                           return true;
                       }
                   }
           );

            return view;
        }
    }
    class SearchTask3 extends AsyncTask<String, String, List<SearchModel3>> {

        @Override
        protected List<SearchModel3> doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/file1.php";
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

                List<SearchModel3> modellist1 = new ArrayList<>();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");
                for (int i = 0; i < a1.length(); i++) {
                    SearchModel3 model1 = new SearchModel3();
                    JSONObject o2 = a1.getJSONObject(i);
                    String id = o2.getString("id");
                    String name = o2.getString("name");
                    String file = o2.getString("file");
                    model1.setId(id);
                    model1.setName(name);
                    model1.setFile(file);

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
        protected void onPostExecute(List<SearchModel3> searchModel3s) {
            super.onPostExecute(searchModel3s);
            CustomAdapter14 ad = new CustomAdapter14(getApplicationContext(),R.layout.search3,searchModel3s);
            search_view.setAdapter(ad);
        }
    }





    class CustomAdapter14 extends ArrayAdapter {
        public List<SearchModel3> modellist;
        private int resource;
        private LayoutInflater inflater;


        public CustomAdapter14(Context context, int resource, List<SearchModel3> objects) {
            super(context, resource, objects);
            modellist = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.search3, null);
            }

            TextView name = (TextView)convertView.findViewById(R.id.name);
            name.setText(modellist.get(position).getName());



            search_view.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Intent i1 = new Intent(Intent.ACTION_VIEW,Uri.parse("http://studyforfun.000webhostapp.com/"+modellist.get(i).getFile()));
                            startActivity(i1);  search_view.setVisibility(View.GONE);
                            search1();

                        }
                    }
            );
            return convertView;
        }



    }


}
