package com.greedygame.packagename;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static com.greedygame.packagename.R.id.textView;

public class Main2Activity extends AppCompatActivity {
    private View.OnClickListener buttonListener;
    TextView packageName;
    TextView AppName;

    ImageView AppIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        packageName = (TextView) findViewById(textView);
        AppName = (TextView) findViewById(R.id.textView1);
        AppIcon = (ImageView) findViewById(R.id.imageView);

        Bundle bundle = getIntent().getExtras();

        final String stuff = bundle.getString("stuff");

        packageName.setText(stuff);
        PackageManager packageManager= getApplicationContext().getPackageManager();

        try {
            String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(stuff, PackageManager.GET_META_DATA));
            AppName.setText(appName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Drawable icon = getPackageManager().getApplicationIcon(stuff);
            AppIcon.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Button button = (Button) findViewById(R.id.button);
        buttonListener =  new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(stuff);
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        };
        button.setOnClickListener(buttonListener);


        //button.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View view) {
              //  Intent launchIntent = getPackageManager().getLaunchIntentForPackage(stuff);
                //if (launchIntent != null) {
                   // startActivity(launchIntent);//null pointer check in case package name was not found
             //   }
            //}
        //});


    }


}




