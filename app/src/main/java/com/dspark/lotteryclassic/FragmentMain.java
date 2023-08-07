package com.dspark.lotteryclassic;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentMain extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static int pflayout = com.dspark.lotteryclassic.R.layout.fragment_main;
    public static FileControls fc = new FileControls();
    public static ArrayList<String> datalist = new ArrayList<String>();
    public static ArrayList<String> detailData = new ArrayList<String>();
    public static ArrayAdapter<String> adapter;
    public static ListView log;
    public static TextView nolog;
    public static LinearLayout lnolog;
    static MainActivity ma;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentMain newInstance(int sectionNumber, int _pflayout, MainActivity _ma) {
        pflayout = _pflayout;
        ma = _ma;
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMain() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(pflayout, container, false);

        ma.actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CC66FF")));

        rootView.findViewById(R.id.btnFirstStartSimple).setOnClickListener(btnFirstStartSimple);
        rootView.findViewById(R.id.btnFirstStartDetail).setOnClickListener(btnFirstStartDetail);

        nolog = (TextView)rootView.findViewById(R.id.txtNoLog);
        lnolog = (LinearLayout)rootView.findViewById(R.id.layoutNoLog);
        log = (ListView)rootView.findViewById(R.id.listLog);
        adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, datalist);
        log.setAdapter(adapter);
        log.setOnItemClickListener(listClick);
        
        /*
        FileControls fc = new FileControls();
        File dir = fc.makeDirectory(fc.StoragePath + "/MiniLottery/");
        fc.makeFile(dir, fc.StoragePath + "/MiniLottery/MiniLotteryLog.txt");
        
        String saveData = fc.readFile(new File(fc.StoragePath + "/MiniLottery/MiniLotteryLog.txt"));
        if(saveData.indexOf("<data>") != -1){
        	String[] temp = saveData.split("<data>");
        	datalist.clear();
        	detailData.clear();
        	for(int i = 1; i < temp.length; i++){
        		String[] temp2 = temp[i].split("</data>");
        		detailData.add(temp2[0]);
        		temp2 = temp2[0].split("추첨 제목: ");
        		temp2 = temp2[1].split("\n");
        		datalist.add(temp2[0]);
        	}
        	
        	lnolog.setVisibility(8);
        	adapter.notifyDataSetChanged();
        }
        else{
        	lnolog.setVisibility(0);
        }
        */
        setData();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    View.OnClickListener btnFirstStartSimple = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            ma.onNavigationDrawerItemSelected(1);
        }
    };

    View.OnClickListener btnFirstStartDetail = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            ma.onNavigationDrawerItemSelected(2);
        }
    };

    Handler alerthandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    OnItemClickListener listClick = new OnItemClickListener(){
        public void onItemClick(AdapterView<?> parentView, View clickedView, int position, long id)
        {
            Message msg = alerthandler.obtainMessage();
            AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
            ad.setCancelable(false);
            ad.setTitle(((TextView)clickedView).getText().toString());
            ad.setMessage(detailData.get(position));
            ad.setButton(AlertDialog.BUTTON_NEUTRAL, "확인", msg);
            ad.show();
        }
    };


    public void setData(){
        SharedPreferences pref = getActivity().getSharedPreferences("MINILOTTERY", getActivity().MODE_PRIVATE);

        String _data = pref.getString("Log", "");
        if(_data.indexOf("<data>") != -1){
            try{
                lnolog.setVisibility(8);
            }catch(Exception e){

            }

            detailData.clear();
            datalist.clear();

            String[] rdd = _data.split("<data>");
            for(int i = 1; i < rdd.length; i++){
                String[] tt = rdd[i].split("</data>");
                detailData.add(tt[0]);
                String[] temp = tt[0].split("추첨 제목: ");
                temp = temp[1].split("\n");
                datalist.add(temp[0]);

                if(detailData.size() > 100){
                    SharedPreferences.Editor ed = pref.edit();
                    String rebuilddata = "";

                    for(int j = 2; j < rdd.length; j++){
                        rebuilddata += "<data>" + rdd[j];
                    }
                    ed.putString("Log", rebuilddata);
                }

                try{
                    if (datalist.isEmpty() == false)
                        log.setSelection(datalist.size());

                    adapter.notifyDataSetChanged();
                }catch(Exception e){

                }
            }
        }
        else{
            try{
                lnolog.setVisibility(0);
            }catch(Exception e){

            }
        }
    }
}