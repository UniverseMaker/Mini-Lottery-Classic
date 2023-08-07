package com.dspark.lotteryclassic;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ActivityListView extends Activity {
    ArrayList<String> datalist = new ArrayList<String>();
    //ArrayList<String> datalist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        Bundle b = getIntent().getExtras();
        String[] list = b.getStringArray("list");
        datalist.clear();
        for(int i = 0; i < list.length; i++){
            datalist.add(list[i]);
        }

        ListView lv = (ListView)findViewById(R.id.activity_listview_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, datalist);
        lv.setAdapter(adapter);

        Button mbtnBack = (Button)findViewById(R.id.activity_listview_btnBack);
        mbtnBack.setOnClickListener(btnBack_Work);
    }

    View.OnClickListener btnBack_Work = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            finish();

        }
    };
}
