package com.dspark.lotteryclassic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentSub1 extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static int pflayout = com.dspark.lotteryclassic.R.layout.fragment_main;


    private static final int SEND_THREAD_INFOMATION = 0;
    private static final int SEND_THREAD_STOP_MESSAGE = 1;

    public static  int maxWinners = 0;
    public static  int remainWinners = 0;
    public static  Timer mainTimer = new Timer();

    public static TextView Viewer;
    public static ListView lv;
    public static ArrayList<String> datalist = new ArrayList<String>();
    public static ArrayAdapter<String> adapter;

    public static ArrayList<String> userlist = new ArrayList<String>();
    public static SimpleLotteryTask slt;

    public static String lastLog = "";
    public static int handlernum = 0;
    //private SendMessageHandler handler = new SendMessageHandler();
    //private TextView Viewer;

    static MainActivity ma;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentSub1 newInstance(int sectionNumber, int _pflayout, MainActivity _ma) {
        pflayout = _pflayout;
        ma = _ma;
        FragmentSub1 fragment = new FragmentSub1();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSub1() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(pflayout, container, false);

        ma.actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399FF")));

        rootView.findViewById(R.id.btnStart).setOnClickListener(btnStart_Work);
        rootView.findViewById(R.id.btnStop).setOnClickListener(btnStop_Work);

        Viewer = (TextView)rootView.findViewById(R.id.txtSimpleLotteryViewer);
        lv = (ListView)rootView.findViewById(R.id.listSimpleLottery);
        adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, datalist);
        lv.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }


    //Custom Fragment Controls
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //savedInstanceState.putInt("text", "savedText");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try{
            mainTimer.cancel();
        }catch(Exception e){

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //activity = null;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1 < userlist.size()){
                try{
                    handlernum = msg.arg1;
                    Viewer.setText(userlist.get(msg.arg1));
                }catch(Exception e){
                    try{
                        Viewer.setText(userlist.get(0));
                    }catch(Exception e2){

                    }
                }
            }
        }
    };

    Handler alerthandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    View.OnClickListener btnStart_Work = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //Toast.makeText(getActivity(), "굿", Toast.LENGTH_LONG).show();
            try{
                mainTimer.cancel();
            }catch(Exception e){

            }

            EditText tmaxn = (EditText)getActivity().findViewById(R.id.txtSimpleLotteryMaxNum);
            EditText twinn = (EditText)getActivity().findViewById(R.id.txtSimpleLotteryWinners);
            int max = Integer.parseInt(tmaxn.getText().toString());
            int winners = Integer.parseInt(twinn.getText().toString());
			
			/*
			AlertDialog ad = new AlertDialog.Builder(this).create();  
			ad.setCancelable(false); // This blocks the 'BACK' button  
			ad.setMessage("Hello World");  
			
			ad.setButton("OK", new DialogInterface.OnClickListener() {  
			    @Override  
			    public void onClick(DialogInterface dialog, int which) {  
			        dialog.dismiss();                      
			    }  
			});
			ad.show(); 
			*/

            if(max < winners){
                Message msg = alerthandler.obtainMessage();
                AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
                ad.setCancelable(false);
                ad.setMessage("당첨자수는 총인원수보다 많을 수 없습니다. 수정후 다시 시도하세요.");
                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "확인", msg);

                ad.show();
                return;
            }

            if(winners == 0){
                Message msg = alerthandler.obtainMessage();
                AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
                ad.setCancelable(false);
                ad.setMessage("당첨자수는 0이 될 수 없습니다. 수정후 다시 시도하세요.");
                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "확인", msg);
                ad.show();
                return;
            }

            datalist.clear();
            //LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lv.getLayoutParams();
            if(winners > 1){
                //lp.height = 100;
                //lv.setLayoutParams(lp);
                lv.setVisibility(0);
                adapter.notifyDataSetChanged();
            }
            else{
                lv.setVisibility(8);
            }

            userlist.clear();
            for(int i = 0; i < max; i++){
                userlist.add(String.valueOf(i+1));
            }

            adapter.notifyDataSetChanged();

            maxWinners = winners;
            remainWinners = winners;
            mainTimer = new Timer();
            lastLog = "";

            slt = new SimpleLotteryTask(max);
            mainTimer.schedule(slt, 0, 100);

            TextView infol = (TextView)getActivity().findViewById(R.id.txtSimpleLotteryInfol);
            infol.setText("마음의 준비가 되면 당첨자확인을 누르세요");
            infol.setBackgroundColor(Color.parseColor("#FFFF99"));

            InputMethodManager imm= (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(null, 0);

        }
    };

    View.OnClickListener btnStop_Work = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //Toast.makeText(getActivity(), "굿", Toast.LENGTH_LONG).show();
            TextView infol = (TextView)getActivity().findViewById(R.id.txtSimpleLotteryInfol);

            remainWinners--;
            if(remainWinners >= 0){
                if(maxWinners > 1){
                    int selWinner = handlernum;
                    String nowWinner = userlist.get(selWinner);

                    userlist.remove(selWinner);
                    try{
                        slt.setMaxNum(userlist.size());
                    }catch(Exception e){

                    }

                    int seqWinner = maxWinners - remainWinners;
                    datalist.add(String.valueOf(seqWinner) + "번째 당첨번호 (" + nowWinner + ")");
                    if (datalist.isEmpty() == false)
                        lv.setSelection(datalist.size());

                    adapter.notifyDataSetChanged();

                    infol.setText(String.valueOf(seqWinner) + "번째 당첨자 축하드립니다!");
                    infol.setBackgroundColor(Color.parseColor("#99FF99"));
                }
            }
            if(remainWinners == 0){
                try{
                    mainTimer.cancel();
                }catch(Exception e){

                }

                infol.setText("추첨완료! 축하드립니다!");
                infol.setBackgroundColor(Color.parseColor("#99FF99"));

                TextView tagv = (TextView)getActivity().findViewById(R.id.txtSimpleLotteryTag);
                String tag = tagv.getText().toString();

                // 현재 시간을 msec으로 구한다.
                long now = System.currentTimeMillis();
                // 현재 시간을 저장 한다.
                Date date = new Date(now);
                // 시간 포맷으로 만든다.
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String strNow = sdfNow.format(date);

                lastLog += "당첨 기록\n";
                if(tag.isEmpty() == false){
                    lastLog += "추첨 제목: " + tag + "\n";
                }else{
                    lastLog += "추첨 제목: " + strNow + "\n";
                }

                lastLog += "추첨 시각: " + strNow + "\n\n";
                lastLog += "당첨 결과: \n";



                Message msg = alerthandler.obtainMessage();
                AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
                ad.setCancelable(false);
                ad.setTitle("당첨 결과");

                if(maxWinners > 1){
                    Viewer.setText("-");

                    for(int i = 0; i < datalist.size(); i++){
                        lastLog += datalist.get(i) + "\n";
                    }

                    ad.setMessage(lastLog + "\n당첨번호는 추첨기록에 저장되었습니다.");
                }
                else{

                    lastLog += "당첨번호 (" + Viewer.getText().toString() + ")\n";
                    datalist.add("당첨번호 (" + Viewer.getText().toString() + ")");

                    ad.setMessage(lastLog + "\n당첨번호는 추첨기록에 저장되었습니다.");
                }

                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "확인", msg);
                ad.show();

                String[] datalistClone = datalist.toArray(new String[datalist.size()]);
                Intent intent = new Intent(getActivity(), ActivityListView.class);
                intent.putExtra("list", datalistClone);
                startActivity(intent);

                //FragmentMain.setData(lastLog);

                sdfNow = new SimpleDateFormat("yyyy/MM/dd");
                strNow = sdfNow.format(date);

                SharedPreferences pref = getActivity().getSharedPreferences("MINILOTTERY", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor ed = pref.edit();
                ed.putInt("totalLottery", (pref.getInt("totalLottery", 0) + 1));
                ed.putString("lastLottery", strNow);
                String beforeLog = pref.getString("Log", "");
                ed.putString("Log", "<data>" + lastLog + "</data>" + beforeLog);
                ed.commit();
            }

        }
    };

    private class SimpleLotteryTask extends TimerTask{
        //TextView Viewer;
        Random random = new Random();
        int MaxNum = 0;
        public SimpleLotteryTask(int _MaxNum){
            //Viewer = _Viewer;
            MaxNum = _MaxNum;
        }

        public void setMaxNum(int _MaxNum){
            MaxNum = _MaxNum;
        }

        public void run() {
            //counter++;
            //Viewer.setText(String.valueOf(counter));

            // 메시지 얻어오기
            Message msg = handler.obtainMessage();

            // 메시지 ID 설정
            msg.what = SEND_THREAD_INFOMATION;

            // 메시지 정보 설정 (int 형식)
            msg.arg1 = random.nextInt(userlist.size());

            handler.sendMessage(msg);
        }
    }
	 
	 /*
	 private class SendMessageHandler extends Handler {
         
        @Override
        public void handleMessage(Message msg) {
        	findViewById()''
        }
	 }
	 */
}