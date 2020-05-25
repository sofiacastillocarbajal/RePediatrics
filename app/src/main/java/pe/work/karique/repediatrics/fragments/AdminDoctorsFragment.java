package pe.work.karique.repediatrics.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.User;


public class AdminDoctorsFragment extends Fragment {
    private TabLayout _homeTabLayout;
    private ViewPager _homeViewPager;
    private ViewPagerAdapter adapter;

    private AdminDoctorsPediatricianFragment adminDoctorsPediatricianFragment;
    private AdminDoctorsOncologistFragment adminDoctorsOncologistFragment;

    private int CURRENT_FRAGMENT_SELECTED = 0;
    public static final int PEDIATRICIAN_FRAGMENT_SELECTED = 0;
    public static final int ONCOLOGIST_FRAGMENT_SELECTED = 1;

    public AdminDoctorsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_doctors, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeTab(view);
    }

    private void initializeTab(View view){
        _homeTabLayout = view.findViewById(R.id.home_tabs);
        _homeViewPager = view.findViewById(R.id.home_viewpager);
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adminDoctorsPediatricianFragment = new AdminDoctorsPediatricianFragment();
        adminDoctorsPediatricianFragment.setOnProgressBarChanged(new AdminDoctorsPediatricianFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                onProgressBarChanged.OnProgressBarVisible();
            }

            @Override
            public void OnProgressBarHide() {
                onProgressBarChanged.OnProgressBarHide();
            }
        });
        adminDoctorsPediatricianFragment.setOnUserClicked(new AdminDoctorsPediatricianFragment.OnUserClickedListener() {
            @Override
            public void OnUserClicked(User user) {
                onUserClickedListener.OnUserClicked(user);
            }
        });

        adminDoctorsOncologistFragment = new AdminDoctorsOncologistFragment();
        adminDoctorsOncologistFragment.setOnProgressBarChanged(new AdminDoctorsOncologistFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                onProgressBarChanged.OnProgressBarVisible();
            }

            @Override
            public void OnProgressBarHide() {
                onProgressBarChanged.OnProgressBarHide();
            }
        });
        adminDoctorsOncologistFragment.setOnUserClicked(new AdminDoctorsOncologistFragment.OnUserClickedListener() {
            @Override
            public void OnUserClicked(User user) {
                onUserClickedListener.OnUserClicked(user);
            }
        });

        adapter.addFragment(adminDoctorsPediatricianFragment, "Pediatras");
        adapter.addFragment(adminDoctorsOncologistFragment, "Especialistas");

        _homeViewPager.setAdapter(adapter);
        _homeTabLayout.setupWithViewPager(_homeViewPager);
        _homeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CURRENT_FRAGMENT_SELECTED = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public int getCurrentFragmentSelected() {
        return CURRENT_FRAGMENT_SELECTED;
    }

    public interface OnUserClickedListener {
        void OnUserClicked(User user);
    }
    private OnUserClickedListener onUserClickedListener;
    public void setOnUserClicked(OnUserClickedListener onUserClickedListener) {
        this.onUserClickedListener = onUserClickedListener;
    }

    public interface OnProgressBarChanged {
        void OnProgressBarVisible();
        void OnProgressBarHide();
    }
    private OnProgressBarChanged onProgressBarChanged;
    public void setOnProgressBarChanged(OnProgressBarChanged onProgressBarChanged) {
        this.onProgressBarChanged = onProgressBarChanged;
    }
}
