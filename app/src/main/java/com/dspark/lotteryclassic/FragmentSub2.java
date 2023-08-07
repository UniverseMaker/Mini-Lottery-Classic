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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentSub2 extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static int pflayout = com.dspark.lotteryclassic.R.layout.fragment_main;

    static MainActivity ma;

    private static final int SEND_THREAD_INFOMATION = 0;
    private static final int SEND_THREAD_STOP_MESSAGE = 1;

    private Timer mainTimer = new Timer();
    public static ArrayList<String> datalist = new ArrayList<String>();
    public static ArrayList<String> datalistClone = new ArrayList<String>();

    public static ArrayAdapter<String> adapter;
    public static ListView log;

    DetailLotteryTask dlt;
    TextView Viewer;

    String lastLog = "";
    ArrayList<String> Winners = new ArrayList<String>();

    int maxWinners = 0;
    int remainWinners = 0;
    int currentValue = 0;
    public static int handlernum = 0;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentSub2 newInstance(int sectionNumber, int _pflayout, MainActivity _ma) {
        pflayout = _pflayout;
        ma = _ma;
        FragmentSub2 fragment = new FragmentSub2();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSub2() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(pflayout, container, false);

        ma.actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399FF")));

        log = (ListView)rootView.findViewById(R.id.listDetailLotteryUserList);
        adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, datalist);
        log.setAdapter(adapter);

        Viewer = (TextView)rootView.findViewById(R.id.txtDetailLotteryViewer);

        rootView.findViewById(R.id.btnDetailLotteryAdd).setOnClickListener(btnAdd_Work);
        rootView.findViewById(R.id.btnDetailLotteryClear).setOnClickListener(btnClear_Work);
        rootView.findViewById(R.id.btnDetailLotteryStart).setOnClickListener(btnStart_Work);
        rootView.findViewById(R.id.btnDetailLotteryStop).setOnClickListener(btnStop_Work);

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
            currentValue = msg.arg1;
            if(currentValue < datalistClone.size()){
                try{
                    Viewer.setText(datalistClone.get(msg.arg1));
                }catch(Exception e){
                    try{
                        Viewer.setText(datalistClone.get(0));
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

    View.OnClickListener btnAdd_Work = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView tv = (TextView)getActivity().findViewById(R.id.txtDetailLotteryUserName);
            if(tv.getText().toString().isEmpty() == false){
                datalist.add(tv.getText().toString());
                tv.setText("");
                adapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "총 " + String.valueOf(datalist.size()) + "명 등록됨", Toast.LENGTH_SHORT).show();
            }
            else{
                Message msg = alerthandler.obtainMessage();
                AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
                ad.setCancelable(false);
                ad.setTitle("안내");
                ad.setMessage("안내! 참가자의 이름을 입력하고 추가버튼을 눌러주세요!");
                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "확인", msg);
                ad.show();
            }
        }
    };

    View.OnClickListener btnStart_Work = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                mainTimer.cancel();
            }catch(Exception e){

            }

            if(datalist.size() <= 0){
                Message msg = alerthandler.obtainMessage();
                AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
                ad.setCancelable(false);
                ad.setMessage("한명 이상의 참여자를 등록해주세요");
                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "확인", msg);

                ad.show();
                return;
            }

            EditText twinn = (EditText)getActivity().findViewById(R.id.txtDetailLotteryWinners);
            int winners = Integer.parseInt(twinn.getText().toString());

            if(datalist.size() < winners){
                Message msg = alerthandler.obtainMessage();
                AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
                ad.setCancelable(false);
                ad.setMessage("당첨자수는 총인원수보다 많을 수 없습니다. 수정후 다시 시도하세요.");
                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "확인", msg);

                ad.show();
                return;
            }

            datalistClone.clear();
            for(int i = 0; i < datalist.size(); i++)
                datalistClone.add(datalist.get(i));

            mainTimer = new Timer();
            lastLog = "";

            maxWinners = winners;
            remainWinners = winners;
            Winners.clear();

            Viewer.setVisibility(0);

            dlt = new DetailLotteryTask(datalistClone.size());
            mainTimer.schedule(dlt, 0, 100);

            TextView infol = (TextView)getActivity().findViewById(R.id.txtDetailLotteryInfol);
            infol.setText("마음의 준비가 되면 당첨자확인을 누르세요");
            infol.setBackgroundColor(Color.parseColor("#FFFF99"));
        }
    };

    View.OnClickListener btnStop_Work = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            remainWinners--;

            TextView infol = (TextView)getActivity().findViewById(R.id.txtDetailLotteryInfol);

            if(remainWinners >= 0){
                int selWinner = currentValue;
                String nowWinner = datalistClone.get(selWinner);

                datalistClone.remove(selWinner);
                try{
                    dlt.setMaxNum(datalistClone.size());
                }catch(Exception e){

                }

                int seqWinner = maxWinners - remainWinners;
                Winners.add(nowWinner + " (" + seqWinner + "위)");

                infol.setText(String.valueOf(seqWinner) + "번째 당첨자 " + nowWinner + "\n" + String.valueOf(remainWinners) + "명 남았습니다");
                infol.setBackgroundColor(Color.parseColor("#99FF99"));
            }

            if(remainWinners == 0){
                try{
                    mainTimer.cancel();
                }catch(Exception e){

                }

                Viewer.setVisibility(8);

                infol.setText("추첨완료! 축하합니다!");
                infol.setBackgroundColor(Color.parseColor("#99FF99"));

                TextView tagv = (TextView)getActivity().findViewById(R.id.txtDetailLotteryTag);
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

                for(int i = 0; i < Winners.size(); i++){
                    lastLog += Winners.get(i) + "\n";
                }

                Message msg = alerthandler.obtainMessage();
                AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
                ad.setCancelable(false);
                ad.setTitle("당첨 결과");
                ad.setMessage(lastLog + "\n당첨번호는 추첨기록에 저장되었습니다.");
                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "확인", msg);
                ad.show();

                String[] winnersClone = Winners.toArray(new String[Winners.size()]);
                Intent intent = new Intent(getActivity(), ActivityListView.class);
                intent.putExtra("list", winnersClone);
                startActivity(intent);

                //FragmentMain.setData(lastLog);

                sdfNow = new SimpleDateFormat("yyyy/MM/dd");
                strNow = sdfNow.format(date);

                SharedPreferences pref = getActivity().getSharedPreferences("MINILOTTERY", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor ed = pref.edit();
                ed.putInt("totalLottery", (pref.getInt("totalLottery", 0)+1));
                ed.putString("lastLottery", strNow);
                ed.putString("Log", "<data>" + lastLog + "</data>" + pref.getString("Log", ""));
                ed.commit();
            }
        }
    };

    View.OnClickListener btnClear_Work = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            datalist.clear();
            ((TextView)getActivity().findViewById(R.id.txtDetailLotteryWinners)).setText("1");
            ((TextView)getActivity().findViewById(R.id.txtDetailLotteryUserName)).setText("");
            adapter.notifyDataSetChanged();
        }
    };

    private class DetailLotteryTask extends TimerTask{
        //TextView Viewer;
        Random random = new Random();
        int MaxNum = 0;
        public DetailLotteryTask(int _MaxNum){
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
            msg.arg1 = random.nextInt(MaxNum);

            handler.sendMessage(msg);
        }
    }
}
