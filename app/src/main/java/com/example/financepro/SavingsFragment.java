package com.example.financepro;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class SavingsFragment extends Fragment {
    private CategoryDatabase categoryDatabase;
    private ArrayList<AccountRecord> records = new ArrayList<>();
    private AccountAdapter adapter;
    private AccountRecord newRecord;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liability, container, false);
        categoryDatabase = new CategoryDatabase(getActivity());

        records = categoryDatabase.showAc(1);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.liability_record);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AccountAdapter(getActivity(), records);
        recyclerView.setAdapter(adapter);

        Button addbtn = (Button) view.findViewById(R.id.add_cat);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        return view;

    }
    private void add(){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.input_account_record, null);
        EditText editText = (EditText) subView.findViewById(R.id.set_label);
        EditText editAmount = (EditText) subView.findViewById(R.id.amountET);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Create Debt");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newRecord = new AccountRecord();

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
                newRecord.setDate(sd.format(cal.getTime()));
                newRecord.setLabel(editText.getText().toString());
                newRecord.setAmount(Integer.parseInt(editAmount.getText().toString()));
                if(newRecord.getLabel().isEmpty()){
                    Toast.makeText(getActivity(), "Please Enter a Label", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newRecord.getAmount() <= 0){
                    Toast.makeText(getActivity(), "Please set amount", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    newRecord.setType(1);
                    newRecord.setStatus(true);
                    categoryDatabase.insertAc(newRecord);
                    records.add(newRecord);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("Drop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
}