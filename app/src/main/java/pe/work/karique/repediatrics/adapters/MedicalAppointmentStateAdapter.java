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
import pe.work.karique.repediatrics.models.MedicalAppointmentState;
import pe.work.karique.repediatrics.models.TicketState;
import pe.work.karique.repediatrics.util.FuncionesFecha;


public class MedicalAppointmentStateAdapter extends RecyclerView.Adapter<MedicalAppointmentStateAdapter.UserViewHolder> {
    private List<MedicalAppointmentState> medicalAppointmentStates;

    public MedicalAppointmentStateAdapter(List<MedicalAppointmentState> medicalAppointmentStates) {
        this.medicalAppointmentStates = medicalAppointmentStates;
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_medical_appointment_state, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final MedicalAppointmentState medicalAppointmentState = medicalAppointmentStates.get(position);

        holder.stateNameTextView.setText(medicalAppointmentState.getState());
        holder.doctorNameTextView.setText("Por: " + medicalAppointmentState.getUserUpdate().getDrName());
        Date date = FuncionesFecha.getDateWithHourFromString(medicalAppointmentState.getDateOfUpdate());
        String sd = FuncionesFecha.formatDateForAPIWithHour(date);
        holder.dateTextView.setText(sd);
    }

    @Override
    public int getItemCount() {
        return medicalAppointmentStates.size();
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
