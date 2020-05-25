package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.fragments.TicketsFragment;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class TicketsActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private ImageButton backImageButton;
    private ProgressBar progressBar;

    private TabLayout _homeTabLayout;
    private ViewPager _homeViewPager;
    private ViewPagerAdapter adapter;

    public TicketsFragment aprobadosFragment;
    public TicketsFragment enEsperaFragment;
    public TicketsFragment canceladosFragment;

    private List<Ticket> tickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        hide();

        tickets = new ArrayList<>();
        sessionManager = SessionManager.getInstance(this);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressBar = findViewById(R.id.progressBar);
        initializeTab();
        getTickets();
    }

    private void initializeTab(){
        _homeTabLayout = (TabLayout) findViewById(R.id.home_tabs);
        _homeViewPager = (ViewPager) findViewById(R.id.home_viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        aprobadosFragment = new TicketsFragment();
        aprobadosFragment.setOnTicketClicked(new TicketsFragment.OnTicketClickedListener() {
            @Override
            public void onTicketClicked(Ticket ticket) {
                showTicketDetails(ticket);
            }
        });
        enEsperaFragment= new TicketsFragment();
        enEsperaFragment.setOnTicketClicked(new TicketsFragment.OnTicketClickedListener() {
            @Override
            public void onTicketClicked(Ticket ticket) {
                showTicketDetails(ticket);
            }
        });
        canceladosFragment = new TicketsFragment();
        canceladosFragment.setOnTicketClicked(new TicketsFragment.OnTicketClickedListener() {
            @Override
            public void onTicketClicked(Ticket ticket) {
                showTicketDetails(ticket);
            }
        });

        adapter.addFragment(aprobadosFragment, "Aprobados");
        adapter.addFragment(enEsperaFragment, "En espera");
        adapter.addFragment(canceladosFragment, "Cancelados");

        _homeViewPager.setOffscreenPageLimit(3);
        _homeViewPager.setAdapter(adapter);
        _homeTabLayout.setupWithViewPager(_homeViewPager);
        _homeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {}

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void showTicketDetails(Ticket ticket){
        Intent intent = new Intent(this, TicketDetailsActivity.class);
        intent.putExtras(ticket.toBundle());
        startActivityForResult(intent, TicketDetailsActivity.REQUEST_CODE);
    }
    private void getTickets(){
        progressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(RepediatricsApi.TICKETS_BY_PARENT(sessionManager.getid()))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);

                        tickets.clear();
                        tickets.addAll(Ticket.from(response));
                        setTicketsInFragments(tickets);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
    private void setTicketsInFragments(List<Ticket> tickets){
        //declaro
        List<Ticket> aprobados = new ArrayList<>();
        List<Ticket> enEspera = new ArrayList<>();
        List<Ticket> cancelados = new ArrayList<>();

        //filtro
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            if (ticket.getState().equals(Ticket.STATE_APROBADO)){
                aprobados.add(ticket);
            }
            else if (ticket.getState().equals(Ticket.STATE_EN_ESPERA)){
                enEspera.add(ticket);
            }
            else if (ticket.getState().equals(Ticket.STATE_CANCELADO)){
                cancelados.add(ticket);
            }
        }

        //seteo
        aprobadosFragment.setTickets(aprobados);
        enEsperaFragment.setTickets(enEspera);
        canceladosFragment.setTickets(cancelados);
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
