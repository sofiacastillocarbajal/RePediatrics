package pe.work.karique.repediatrics.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.Ticket;


public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.UserViewHolder> {
    private List<Ticket> tickets;

    public TicketsAdapter(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_ticket, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final Ticket ticket = tickets.get(position);

        holder.titleNameTextView.setText(String.format("Ticket #%s", ticket.getId()));
        holder.dateTextView.setText(ticket.getHospital().getName());
        holder.stateTextView.setText(ticket.getState());
        if (ticket.getState().equals(Ticket.STATE_EN_ESPERA)){
            holder.stateTextView.setTextColor(Color.parseColor("#FFC107"));
        }
        else if (ticket.getState().equals(Ticket.STATE_APROBADO)){
            holder.stateTextView.setTextColor(Color.parseColor("#3ED362"));
        }
        else if (ticket.getState().equals(Ticket.STATE_CANCELADO)){
            holder.stateTextView.setTextColor(Color.parseColor("#F76E8E"));
        }
        holder.patientTextView.setText(ticket.getPatient().getFullName());
        holder.ticketCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTicketClickedListener != null)
                    onTicketClickedListener.onTicketClicked(ticket);
            }
        });
        holder.seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTicketClickedListener != null)
                    onTicketClickedListener.onTicketClicked(ticket);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView titleNameTextView;
        TextView dateTextView;
        TextView stateTextView;
        TextView patientTextView;
        CardView ticketCardView;
        Button seeMoreButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            titleNameTextView = itemView.findViewById(R.id.titleNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            stateTextView = itemView.findViewById(R.id.stateTextView);
            patientTextView = itemView.findViewById(R.id.patientTextView);
            ticketCardView = itemView.findViewById(R.id.ticketCardView);
            seeMoreButton = itemView.findViewById(R.id.seeMoreButton);
        }
    }

    public interface OnTicketClickedListener {
        void onTicketClicked(Ticket ticket);
    }
    private OnTicketClickedListener onTicketClickedListener;
    public void setOnTicketClicked(OnTicketClickedListener onTicketClickedListener) {
        this.onTicketClickedListener = onTicketClickedListener;
    }
}
