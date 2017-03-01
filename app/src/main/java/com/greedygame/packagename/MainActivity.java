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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context context;
    private MyAdapter adapter;
    ArrayList<String> innerArray = new ArrayList<String>();
    ArrayList<String> namesArray = new ArrayList<String>();
    ArrayList<String> newArray = new ArrayList<String>();
    ListView listView;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.txtseearch);
        adapter = new MyAdapter(this, innerArray);
        listView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                adapter.getFilter().filter(cs.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        AsyncTaskRunner task = new AsyncTaskRunner();
        task.execute();
        context = this;
        // Construct the data source
// Create the adapter to convert the array to views

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                String packageName = adapter.getTempArray().get(position);

                Bundle bundle = new Bundle();

                bundle.putString("stuff", packageName);

                myIntent.putExtras(bundle);

                startActivityForResult(myIntent, 0);

            }
        });
    }

    public class MyAdapter extends BaseAdapter implements Filterable {
        Context mContext;
        ArrayList<String> tempArray = new ArrayList();


        public MyAdapter(Context context, ArrayList<String> objects) {
            mContext = context;
            tempArray = objects;
        }

        public ArrayList<String> getTempArray() {
            return tempArray;
        }

        @Override
        public int getCount() {
            return tempArray.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_items, null);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            String packageName = tempArray.get(position);
            PackageManager packageManager = getApplicationContext().getPackageManager();
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

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence cs) {
                    FilterResults results = new FilterResults();
                    List<String> FilteredArrList = new ArrayList<String>();
                    for (int i = 0; i < namesArray.size(); i++) {
                        String data = namesArray.get(i);
                        if (data.toLowerCase().contains(cs.toString().toLowerCase())) {
                            FilteredArrList.add(innerArray.get(i));
                        }
                    }

                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;


                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    tempArray = (ArrayList<String>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
            return filter;
        }

    }

    public class AsyncTaskRunner extends AsyncTask<String, String, String> {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);

        @Override
        protected String doInBackground(String... params) {
            PackageManager pm = getPackageManager();
            List<ApplicationInfo> apps = pm.getInstalledApplications(0);
            for (int i = 0; i < apps.size(); i++) {
                newArray.add(apps.get(i).packageName);
                String applicationName = (String) (apps.get(i) != null ? pm.getApplicationLabel(apps.get(i)) : "(unknown)");
                //namesArray.add(apps.get(i).packageName);
                namesArray.add(applicationName);
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