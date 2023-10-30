package com.example.financepro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class BudgetCatFragment extends Fragment {
    private CategoryDatabase categoryDatabase;
    protected static ArrayList<Category> categories = new ArrayList<>();
    protected static BudgetedCatAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget_cat, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.budget_recycler_view);
        return view;
    }
    public void onResume(){
        super.onResume();
        categoryDatabase = new CategoryDatabase(getActivity());
        categories = categoryDatabase.showBudgetCat();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BudgetedCatAdapter(getActivity(), categories);
        recyclerView.setAdapter(adapter);
    }

}