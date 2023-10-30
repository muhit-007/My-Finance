package com.example.financepro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.SharedPreferences;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import android.widget.ImageButton;


public class TransactionFragment extends Fragment implements MyDialog.DisplayOptionListener{
    public static ArrayList<HistoryRecord> records = new ArrayList<>();
    private CategoryDatabase categoryDatabase;
    public static TransactionAdapter adapter;
    private LinearLayout body;
    private DisplayOption displayOption;

    public static Calendar cal = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);


        displayOption = new DisplayOption(getActivity(), view);
        displayOption.displayRecord(cal);

        ImageButton button = (ImageButton) view.findViewById(R.id.showDisplayOption);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog d = new MyDialog(TransactionFragment.this);
                d.show(getChildFragmentManager(), "MyDialog");
            }
        });

        ImageButton buttonNext = (ImageButton) view.findViewById(R.id.next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cal.add(Calendar.DATE, 1);
                displayOption.displayRecord(cal);
            }
        });

        ImageButton buttonPrev = (ImageButton) view.findViewById(R.id.prev);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayOption.initState(cal);
                cal.add(Calendar.DATE, -1);
                displayOption.displayRecord(cal);
            }
        });

        return view;
    }
    @Override
    public void onDisplayOptionChanged(){
        cal = Calendar.getInstance();
        displayOption.displayRecord(cal);
    }
    @Override
    public void onResume(){
        super.onResume();
        displayOption.displayRecord(cal);
    }


}