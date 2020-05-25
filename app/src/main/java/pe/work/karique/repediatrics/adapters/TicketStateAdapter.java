package pe.work.karique.repediatrics.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.TicketState;
import pe.work.karique.repediatrics.util.FuncionesFecha;


public class TicketStateAdapter extends RecyclerView.Adapter<TicketStateAdapter.UserViewHolder> {
    private List<TicketState> ticketStates;

    public TicketStateAdapter(List<TicketState> ticketStates) {
        this.ticketStates = ticketStates;
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_ticket_state, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final TicketState ticketState = ticketStates.get(position);

        holder.stateNameTextView.setText(ticketState.getState());
        holder.doctorNameTextView.setText("Por: " + ticketState.getUserUpdate().getDrName());
        Date date = FuncionesFecha.getDateWithHourFromString(ticketState.getDateOfUpdate());
        String sd = FuncionesFecha.formatDateForAPIWithHour(date);
        holder.dateTextView.setText(sd);
    }

    @Override
    public int getItemCount() {
        return ticketStates.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        CardView stateCardView;
        TextView stateNameTextView;
        TextView doctorNameTextView;
        TextView dateTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            stateCardView = itemView.findViewById(R.id.stateCardView);
            stateNameTextView = itemView.findViewById(R.id.stateNameTextView);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
