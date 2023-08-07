package com.dspark.lotteryclassic;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentIndex extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static int pflayout = com.dspark.lotteryclassic.R.layout.fragment_index;

    static MainActivity ma;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentIndex newInstance(int sectionNumber, int _pflayout, MainActivity _ma) {
        pflayout = _pflayout;
        ma = _ma;
        FragmentIndex fragment = new FragmentIndex();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentIndex() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(pflayout, container, false);

        ma.actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3399FF")));

        SharedPreferences pref = getActivity().getSharedPreferences("MINILOTTERY", getActivity().MODE_PRIVATE);
        //pref.getString("totalLottery", "");
        ((TextView)rootView.findViewById(R.id.txtTotalLottery)).setText("총 추첨횟수\n" + String.valueOf(pref.getInt("totalLottery", 0)) + "회");
        ((TextView)rootView.findViewById(R.id.txtLastLottery)).setText("최근 추첨일\n" + pref.getString("lastLottery", "기록없음"));

        rootView.findViewById(R.id.imgFirstSimpleLottery).setOnClickListener(imgFirstSimpleLottery);
        rootView.findViewById(R.id.imgFirstDetailLottery).setOnClickListener(imgFirstDetailLottery);
        rootView.findViewById(R.id.txtFirstSimpleLottery).setOnClickListener(imgFirstSimpleLottery);
        rootView.findViewById(R.id.txtFirstDetailLottery).setOnClickListener(imgFirstDetailLottery);

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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //activity = null;
    }

    View.OnClickListener imgFirstSimpleLottery = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            ma.onNavigationDrawerItemSelected(2);
        }
    };

    View.OnClickListener imgFirstDetailLottery = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            ma.onNavigationDrawerItemSelected(3);
        }
    };
}