package com.dspark.lotteryclassic;

import java.io.File;
import java.util.ArrayList;

import com.dspark.lotteryclassic.MainActivity.PlaceholderFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ActivitySettings extends Fragment{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static int pflayout = com.dspark.lotteryclassic.R.layout.activity_settings;
    private static MainActivity ma;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ActivitySettings newInstance(int sectionNumber, int _pflayout, MainActivity _ma) {
        pflayout = _pflayout;
        ma = _ma;
        ActivitySettings fragment = new ActivitySettings();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ActivitySettings() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(pflayout, container, false);

        ma.actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399FF")));

        rootView.findViewById(R.id.activity_settings_btnResetLog).setOnClickListener(btnResetLog_Work);
        rootView.findViewById(R.id.activity_settings_btnBack).setOnClickListener(btnBack_Work);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    //ArrayList<String> datalist = new ArrayList<String>();
	
	/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        
        Button resetlog = (Button)findViewById(R.id.activity_settings_btnResetLog);
        resetlog.setOnClickListener(btnResetLog_Work);
        
        Button back = (Button)findViewById(R.id.activity_settings_btnBack);
        back.setOnClickListener(btnBack_Work);
    }
	 */

    Handler alerthandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    View.OnClickListener btnResetLog_Work = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            setResetLog();

            Message msg = alerthandler.obtainMessage();
            AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
            ad.setCancelable(false);
            ad.setTitle("초기화 완료");
            ad.setMessage("초기화가 완료되었습니다");
            ad.setButton(AlertDialog.BUTTON_NEUTRAL, "확인", msg);
            ad.show();
        }
    };

    View.OnClickListener btnBack_Work = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //finish();

            Message msg = alerthandler.obtainMessage();
            AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
            ad.setCancelable(false);
            ad.setTitle("안내");
            ad.setMessage("안내! 돌아가시려면 좌측 상단 메뉴를 눌러 이동하세요");
            ad.setButton(AlertDialog.BUTTON_NEUTRAL, "확인", msg);
            ad.show();
        }
    };

    public void setResetLog(){

        //datalist.clear();
        //detailData.clear();
        //FileControls fc = new FileControls();
        //fc.writeFile(new File(fc.StoragePath + "/MiniLottery/MiniLotteryLog.txt"), new String("").getBytes());
        //fc.deleteFile(new File(fc.StoragePath + "/MiniLottery/MiniLotteryLog.txt"));
        SharedPreferences pref = getActivity().getSharedPreferences("MINILOTTERY", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.clear();
        ed.commit();
    }
}
