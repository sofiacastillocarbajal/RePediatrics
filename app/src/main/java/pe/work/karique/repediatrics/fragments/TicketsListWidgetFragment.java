package pe.work.karique.repediatrics.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.adapters.TicketsAdapter;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.network.RepediatricsApi;


public class TicketsListWidgetFragment extends Fragment {
    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    private RecyclerView ticketsRecyclerView;
    private TicketsAdapter ticketsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Ticket> tickets;
    private List<Ticket> ticketsTodos;

    private CardView todosCardView;
    private CardView enEsperaCardView;
    private CardView aprobadosCardView;
    private CardView canceladosCardView;

    private String url;

    public TicketsListWidgetFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tickets_list_widget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageConstraintLayout = view.findViewById(R.id.messageConstraintLayout);
        messageTextView = view.findViewById(R.id.messageTextView);

        tickets = new ArrayList<>();
        ticketsTodos = new ArrayList<>();

        ticketsRecyclerView = view.findViewById(R.id.ticketsRecyclerView);
        ticketsAdapter = new TicketsAdapter(tickets);
        ticketsAdapter.setOnTicketClicked(new TicketsAdapter.OnTicketClickedListener() {
            @Override
            public void onTicketClicked(Ticket ticket) {
                if (onTicketClickedListener != null){
                    onTicketClickedListener.onTicketClicked(ticket);
                }
            }
        });
        layoutManager = new LinearLayoutManager(view.getContext());
        ticketsRecyclerView.setAdapter(ticketsAdapter);
        ticketsRecyclerView.setLayoutManager(layoutManager);

        todosCardView = view.findViewById(R.id.todosCardView);
        todosCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTodosTickets();
            }
        });
        enEsperaCardView = view.findViewById(R.id.enEsperaCardView);
        enEsperaCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEnEsperaTickets();
            }
        });
        aprobadosCardView = view.findViewById(R.id.aprobadosCardView);
        aprobadosCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAprobadosTickets();
            }
        });
        canceladosCardView = view.findViewById(R.id.canceladosCardView);
        canceladosCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCanceladosTickets();
            }
        });

        getTickets();
    }

    private void getTickets(){
        showNoDataMessage("Cargando...");
        ticketsRecyclerView.setVisibility(View.GONE);
        onProgressBarChanged.OnProgressBarVisible();

        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ticketsRecyclerView.setVisibility(View.VISIBLE);
                        onProgressBarChanged.OnProgressBarHide();
                        messageConstraintLayout.setVisibility(View.GONE);

                        tickets.clear();
                        tickets.addAll(Ticket.from(response));
                        ticketsTodos.addAll(Ticket.from(response));
                        ticketsAdapter.notifyDataSetChanged();

                        if (tickets.isEmpty()){
                            showNoDataMessage("Sin datos");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ticketsRecyclerView.setVisibility(View.VISIBLE);
                        onProgressBarChanged.OnProgressBarHide();
                        showNoDataMessage("Error de carga");
                    }
                });
    }
    private void showNoDataMessage(String message){
        messageTextView.setText(message);
        messageConstraintLayout.setVisibility(View.VISIBLE);
    }
    public void setUrl(String url) {
        this.url = url;
    }
    private void showTodosTickets(){
        tickets.clear();
        tickets.addAll(ticketsTodos);
        ticketsAdapter.notifyDataSetChanged();

        if (tickets.isEmpty()){
            showNoDataMessage("Sin datos");
        }
        else messageConstraintLayout.setVisibility(View.GONE);
    }
    private void showEnEsperaTickets(){
        tickets.clear();
        tickets.addAll(getTicketsFiltrados(Ticket.STATE_EN_ESPERA));
        ticketsAdapter.notifyDataSetChanged();

        if (tickets.isEmpty()){
            showNoDataMessage("Sin datos");
        }
        else messageConstraintLayout.setVisibility(View.GONE);
    }
    private void showAprobadosTickets(){
        tickets.clear();
        tickets.addAll(getTicketsFiltrados(Ticket.STATE_APROBADO));
        ticketsAdapter.notifyDataSetChanged();

        if (tickets.isEmpty()){
            showNoDataMessage("Sin datos");
        }
        else messageConstraintLayout.setVisibility(View.GONE);
    }
    private void showCanceladosTickets(){
        tickets.clear();
        tickets.addAll(getTicketsFiltrados(Ticket.STATE_CANCELADO));
        ticketsAdapter.notifyDataSetChanged();

        if (tickets.isEmpty()){
            showNoDataMessage("Sin datos");
        }
        else messageConstraintLayout.setVisibility(View.GONE);
    }
    private List<Ticket> getTicketsFiltrados(String state){
        List<Ticket> ticketsFiltrados = new ArrayList<>();
        for (int i = 0; i < ticketsTodos.size(); i++) {
            Ticket ticket = ticketsTodos.get(i);
            if (ticket.getState().equals(state)){
                ticketsFiltrados.add(ticket);
            }
        }
        return ticketsFiltrados;
    }

    public interface OnTicketClickedListener {
        void onTicketClicked(Ticket ticket);
    }
    private OnTicketClickedListener onTicketClickedListener;
    public void setOnTicketClicked(OnTicketClickedListener onTicketClickedListener) {
        this.onTicketClickedListener = onTicketClickedListener;
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
