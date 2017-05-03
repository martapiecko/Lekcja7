package com.example.x.lekcja7;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class Tele1Activity extends AppCompatActivity {

    private ListView lv;
    private String[] spec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele);

        ImageView image = (ImageView)findViewById(R.id.imageView1);
        image.setImageResource(R.mipmap.samsungs7);

        lv = (ListView) findViewById(R.id.specifications);
        initResources();
        initSpecificationListView();

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context;
                context = getApplicationContext();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initResources(){
        Resources res = getResources();
        spec = res.getStringArray(R.array.specification1);
    }

    private void initSpecificationListView(){
        lv.setAdapter(new ArrayAdapter<String>(getBaseContext(), R.layout.custom_layout, spec));
    }
}

