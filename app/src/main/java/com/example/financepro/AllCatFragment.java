package com.example.financepro;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;

public class AllCatFragment extends Fragment {
    private CategoryDatabase categoryDatabase;
    private ArrayList<Category> captionInc = new ArrayList<>();
    private ArrayList<Category> captionExp = new ArrayList<>();
    private CategoryAdapter adapterInc;
    private CategoryAdapter adapterExp;
    private Category category;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_cat, container, false);
        categoryDatabase = new CategoryDatabase(getActivity());

        captionInc = categoryDatabase.showList(1);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.incomeCat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterInc = new CategoryAdapter(getActivity(), captionInc);
        recyclerView.setAdapter(adapterInc);


        captionExp = categoryDatabase.showList(0);
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.expenseCat);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        if(captionExp.size() >0 ){
            adapterExp = new CategoryAdapter(getActivity(), captionExp);
            recycler.setAdapter(adapterExp);
        }else {
            recycler.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Add Category",Toast.LENGTH_SHORT).show();
        }
        Button addbtn = (Button) view.findViewById(R.id.add_cat);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });

        return view;
    }
      private void addCategory(){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.input_category, null);
        RadioGroup radioGroup = (RadioGroup) subView.findViewById(R.id.groupradio_cat);
        radioGroup.check(R.id.radia_id2_cat);
        EditText editText = (EditText) subView.findViewById(R.id.catInput);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Category");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                category = new Category();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if(selectedId == R.id.radia_id1_cat){
                    category.setType(1);   //Income type = 1 and Expense type = 0
                }
                else if(selectedId==R.id.radia_id2_cat){
                    category.setType(0);
                }
                category.setName(editText.getText().toString());
                if(category.getName().isEmpty()){
                    Toast.makeText(getActivity(), "Please Enter a Category Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(categoryDatabase.insertQ(category)==1){
                    Toast.makeText(getActivity(), "This Category Already Exists", Toast.LENGTH_SHORT).show();
                }else {
                    if(category.getType()==1){
                        captionInc.add(category);
                        adapterInc.notifyDataSetChanged();;
                    }else{
                        captionExp.add(category);
                        adapterExp.notifyDataSetChanged();
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
}