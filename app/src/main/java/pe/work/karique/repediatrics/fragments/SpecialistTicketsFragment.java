package pe.work.karique.repediatrics.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import pe.work.karique.repediatrics.session.SessionManager;

public class SpecialistTicketsFragment extends Fragment {

    private SessionManager sessionManager;
    private ConstraintLayout messageConstraintLayout;
    private TextView messageTextView;

    private RecyclerView ticketsRecyclerView;
    private TicketsAdapter ticketsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Ticket> tickets;

    public SpecialistTicketsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_specialist_tickets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = SessionManager.getInstance(getActivity());
        ticketsRecyclerView = view.findViewById(R.id.ticketsRecyclerView);
        messageConstraintLayout = view.findViewById(R.id.messageConstraintLayout);
        messageTextView = view.findViewById(R.id.messageTextView);

        tickets = new ArrayList<>();
        ticketsAdapter = new TicketsAdapter(tickets);
        ticketsAdapter.setOnTicketClicked(new TicketsAdapter.OnTicketClickedListener() {
            @Override
            public void onTicketClicked(Ticket ticket) {
                if (onTicketClickedListener != null){
                    onTicketClickedListener.OnTicketClicked(ticket);
                }
            }
        });
        layoutManager = new LinearLayoutManager(getActivity());
        ticketsRecyclerView.setAdapter(ticketsAdapter);
        ticketsRecyclerView.setLayoutManager(layoutManager);
        getTickets();
    }

    public void getTickets(){
        onProgressBarChanged.OnProgressBarVisible();
        ticketsRecyclerView.setVisibility(View.GONE);

        AndroidNetworking.get(RepediatricsApi.TICKETS_BY_SPECIALISTS(sessionManager.getid()))
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onProgressBarChanged.OnProgressBarHide();
                        ticketsRecyclerView.setVisibility(View.VISIBLE);
                        messageConstraintLayout.setVisibility(View.GONE);

                        tickets.clear();
                        tickets.addAll(Ticket.from(response));
                        ticketsAdapter.notifyDataSetChanged();

                        if (tickets.isEmpty()){
                            showNoDataMessage("Sin datos");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ticketsRecyclerView.setVisibility(View.VISIBLE);
                        onProgressBarChanged.OnProgressBarHide();
                        showNoDataMessage("Error de conexión");
                    }
                });
    }
    private void showNoDataMessage(String mensaje){
        messageTextView.setText(mensaje);
        messageConstraintLayout.setVisibility(View.VISIBLE);
    }

    public interface OnProgressBarChanged {
        void OnProgressBarVisible();
        void OnProgressBarHide();
    }
    private OnProgressBarChanged onProgressBarChanged;
    public void setOnProgressBarChanged(OnProgressBarChanged onProgressBarChanged) {
        this.onProgressBarChanged = onProgressBarChanged;
    }

    public interface OnTicketClickedListener {
        void OnTicketClicked(Ticket ticket);
    }
    private OnTicketClickedListener onTicketClickedListener;
    public void setOnTicketClickedListener(OnTicketClickedListener onTicketClickedListener) {
        this.onTicketClickedListener = onTicketClickedListener;
    }
}
