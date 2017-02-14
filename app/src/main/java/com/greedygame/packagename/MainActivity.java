package com.greedygame.packagename;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    Context context;
    ArrayList<String> innerArray = new ArrayList<String>();
    ArrayList<String> namesArray = new ArrayList<String>();
    ArrayList<String> newArray = new ArrayList<String>();
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AsyncTaskRunner task = new AsyncTaskRunner();
        task.execute();
        context = this;
        listView = (ListView) findViewById(R.id.listview);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, innerArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String packageName = newArray.get(position);
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });
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
            innerArray.addAll(namesArray);
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