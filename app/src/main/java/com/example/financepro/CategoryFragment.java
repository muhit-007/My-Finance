package com.example.financepro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class CategoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        return view;
    }
    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new AllCatFragment();
            } else if (position == 1) {
                return new BudgetCatFragment();
            }
            return null;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getResources().getText(R.string.all_tab);
            } else if (position == 1) {
                return getResources().getText(R.string.budget_tab);
            }
            return null;
        }
    }
}