package pe.work.karique.repediatrics.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.adapters.TicketsAdapter;
import pe.work.karique.repediatrics.models.Ticket;

public class TicketsFragment extends Fragment {

    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    private RecyclerView ticketsRecyclerView;
    private TicketsAdapter ticketsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Ticket> tickets;

    public TicketsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tickets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageConstraintLayout = view.findViewById(R.id.messageConstraintLayout);
        messageTextView = view.findViewById(R.id.messageTextView);

        tickets = new ArrayList<>();
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

        if (tickets.isEmpty()){
            showNoDataMessage();
        }
        else hideNoDataMessage();
    }
    private void showNoDataMessage(){
        if (messageTextView != null)
            messageTextView.setText(getResources().getString(R.string.no_data));
        if (messageConstraintLayout != null)
            messageConstraintLayout.setVisibility(View.VISIBLE);
    }
    private void hideNoDataMessage(){
        if (messageTextView != null)
            messageTextView.setText(getResources().getString(R.string.no_data));
        if (messageConstraintLayout != null)
            messageConstraintLayout.setVisibility(View.GONE);
    }
    public void setTickets(List<Ticket> tickets) {
        if (this.tickets == null)
            this.tickets = new ArrayList<>();

        this.tickets.clear();
        this.tickets.addAll(tickets);
        if (ticketsAdapter != null){
            ticketsAdapter.notifyDataSetChanged();
        }

        if (this.tickets.isEmpty()) showNoDataMessage();
        else hideNoDataMessage();
    }

    public interface OnTicketClickedListener {
        void onTicketClicked(Ticket ticket);
    }
    private OnTicketClickedListener onTicketClickedListener;
    public void setOnTicketClicked(OnTicketClickedListener onTicketClickedListener) {
        this.onTicketClickedListener = onTicketClickedListener;
    }
}















