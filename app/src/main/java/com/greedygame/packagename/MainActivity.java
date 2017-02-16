package com.greedygame.packagename;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context context;
    private ArrayAdapter<String> adapter;
    ArrayList<String> innerArray = new ArrayList<String>();
    ArrayList<String> namesArray = new ArrayList<String>();
    ArrayList<String> newArray = new ArrayList<String>();
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new MyAdapter(this,innerArray);
        listView.setAdapter(adapter);

        AsyncTaskRunner task = new AsyncTaskRunner();
        task.execute();
        context = this;
        // Construct the data source
// Create the adapter to convert the array to views

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String packageName = innerArray.get(position);
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });
    }

    public class MyAdapter extends ArrayAdapter<String> {
        public MyAdapter(Context context, ArrayList<String> objects) {
            super(context, 0, objects);
        }

        @Override
        public int getCount() {
            return innerArray.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_items, null);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            String packageName = innerArray.get(position);
            PackageManager packageManager= getApplicationContext().getPackageManager();
            try {
                String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
                textView.setText(appName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Drawable icon = null;
            try {
                icon = getPackageManager().getApplicationIcon(packageName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            imageView.setImageDrawable(icon);
            return convertView;

        }

    }
    public class AsyncTaskRunner extends AsyncTask<String, String, String> {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);

        @Override
        protected String doInBackground(String... params) {
            PackageManager pm = getPackageManager();
            List<ApplicationInfo> apps = pm.getInstalledApplications(0);
            for(int i=0;i<apps.size();i++){
                newArray.add(apps.get(i).packageName);
                String applicationName = (String) (apps.get(i) != null ? pm.getApplicationLabel(apps.get(i)) : "(unknown)");
                //namesArray.add(apps.get(i).packageName);
                namesArray.add(applicationName);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            innerArray.addAll(newArray);
            adapter.notifyDataSetChanged();
            pd.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("loading");
            pd.show();
        }

    }

}