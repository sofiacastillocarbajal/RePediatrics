package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.fragments.MedicalAppointmentsListWidgetFragment;
import pe.work.karique.repediatrics.fragments.ParentPatientsFragment;
import pe.work.karique.repediatrics.fragments.TicketsListWidgetFragment;
import pe.work.karique.repediatrics.models.MedicalAppointment;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AdminParentSummaryActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 16;

    private AppCompatImageButton backImageButton;
    private ProgressBar progressBar;
    private TextView repediatricsTitleTextView;

    private TabLayout _homeTabLayout;
    private ViewPager _homeViewPager;
    private ViewPagerAdapter adapter;

    private ParentPatientsFragment parentPatientsFragment;
    private TicketsListWidgetFragment ticketsFromParentFragment;
    private MedicalAppointmentsListWidgetFragment medicalAppointmentsListWidgetFragment;

    private User parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_parent_summary);
        hide();

        parent = User.from(getIntent().getBundleExtra("parent"));
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressBar = findViewById(R.id.progressBar);
        repediatricsTitleTextView = findViewById(R.id.repediatricsTitleTextView);
        repediatricsTitleTextView.setText(parent.getFullName());

        initializeTab();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void initializeTab(){
        String ticketsByParentUrl = RepediatricsApi.TICKETS_BY_PARENT(parent.getId());
        String medicalAppointmentsByParentUrl = RepediatricsApi.MEDICAL_APPOINTMENTS_BY_PARENT(parent.getId());

        _homeTabLayout = findViewById(R.id.home_tabs);
        _homeViewPager = findViewById(R.id.home_viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        parentPatientsFragment = new ParentPatientsFragment();
        parentPatientsFragment.setUserId(parent.getId());
        parentPatientsFragment.setOnProgressBarChanged(new ParentPatientsFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        parentPatientsFragment.setOnUserClicked(new ParentPatientsFragment.OnUserClickedListener() {
            @Override
            public void OnUserClicked(User user) {
                showPatientDetails(user);
            }
        });

        ticketsFromParentFragment = new TicketsListWidgetFragment();
        ticketsFromParentFragment.setUrl(ticketsByParentUrl);
        ticketsFromParentFragment.setOnProgressBarChanged(new TicketsListWidgetFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        ticketsFromParentFragment.setOnTicketClicked(new TicketsListWidgetFragment.OnTicketClickedListener() {
            @Override
            public void onTicketClicked(Ticket ticket) {
                showTicketDetails(ticket);
            }
        });

        medicalAppointmentsListWidgetFragment = new MedicalAppointmentsListWidgetFragment();
        medicalAppointmentsListWidgetFragment.setUrl(medicalAppointmentsByParentUrl);
        medicalAppointmentsListWidgetFragment.setOnProgressBarChanged(new MedicalAppointmentsListWidgetFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        medicalAppointmentsListWidgetFragment.setOnMedicalAppointmentClicked(new MedicalAppointmentsListWidgetFragment.OnMedicalAppointmentClickedListener() {
            @Override
            public void onMedicalAppointmentClicked(MedicalAppointment medicalAppointment) {
                showMedicalAppointmentsDetails(medicalAppointment);
            }
        });

        adapter.addFragment(parentPatientsFragment, "Hijos");
        adapter.addFragment(ticketsFromParentFragment, "Tickets");
        adapter.addFragment(medicalAppointmentsListWidgetFragment, "Citas");

        _homeViewPager.setAdapter(adapter);
        _homeTabLayout.setupWithViewPager(_homeViewPager);
        _homeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {}
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    private void showPatientDetails(User user){
        Intent intent = new Intent(this, PatientDetailsActivity.class);
        intent.putExtras(user.toBundle());
        startActivityForResult(intent, PatientDetailsActivity.REQUEST_CODE);
    }
    private void showTicketDetails(Ticket ticket){
        Intent intent = new Intent(this, TicketDetailsActivity.class);
        intent.putExtras(ticket.toBundle());
        startActivityForResult(intent, TicketDetailsActivity.REQUEST_CODE);
    }
    private void showMedicalAppointmentsDetails(MedicalAppointment medicalAppointment){
        Intent intent = new Intent(this, MedicalAppointmentDetailsActivity.class);
        intent.putExtras(medicalAppointment.toBundle());
        startActivityForResult(intent, MedicalAppointmentDetailsActivity.REQUEST_CODE);
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
}
























































