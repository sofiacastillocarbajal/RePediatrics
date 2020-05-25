package pe.work.karique.repediatrics.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.MedicalAppointment;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.util.FuncionesFecha;


public class MedicalAppointmentsAdapter extends RecyclerView.Adapter<MedicalAppointmentsAdapter.UserViewHolder> {
    private List<MedicalAppointment> medicalAppointments;

    public MedicalAppointmentsAdapter(List<MedicalAppointment> medicalAppointments) {
        this.medicalAppointments = medicalAppointments;
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_medical_appointment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final MedicalAppointment medicalAppointment = medicalAppointments.get(position);

        holder.titleNameTextView.setText(String.format("Cita médica #%s", medicalAppointment.getId()));
        holder.placeTextView.setText(medicalAppointment.getHospital().getName());

        Date date = FuncionesFecha.getDateWithHourFromString(medicalAppointment.getTimeTableByDoctor().getStartDateTime());
        String dateF = FuncionesFecha.formatDate(date);
        String timeF = FuncionesFecha.formatDateToHour(date);
        holder.dateTextView.setText(String.format("%s - %s", dateF, timeF));

        holder.stateTextView.setText(medicalAppointment.getState());
        if (medicalAppointment.getState().equals(MedicalAppointment.STATE_EN_ESPERA)){
            holder.stateTextView.setTextColor(Color.parseColor("#FFC107"));
        }
        else if (medicalAppointment.getState().equals(MedicalAppointment.STATE_APROBADO)){
            holder.stateTextView.setTextColor(Color.parseColor("#3ED362"));
        }
        else if (medicalAppointment.getState().equals(MedicalAppointment.STATE_CANCELADO)){
            holder.stateTextView.setTextColor(Color.parseColor("#F76E8E"));
        }

        holder.medicalAppointmentsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMedicalAppointmentClickedListener != null)
                    onMedicalAppointmentClickedListener.OnMedicalAppointmentClicked(medicalAppointment);
            }
        });
        holder.seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMedicalAppointmentClickedListener != null)
                    onMedicalAppointmentClickedListener.OnMedicalAppointmentClicked(medicalAppointment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicalAppointments.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView placeTextView;
        TextView dateTextView;
        TextView stateTextView;
        CardView medicalAppointmentsCardView;
        Button seeMoreButton;
        TextView titleNameTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            placeTextView = itemView.findViewById(R.id.placeTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            stateTextView = itemView.findViewById(R.id.stateTextView);
            medicalAppointmentsCardView = itemView.findViewById(R.id.medicalAppointmentsCardView);
            seeMoreButton = itemView.findViewById(R.id.seeMoreButton);
            titleNameTextView = itemView.findViewById(R.id.titleNameTextView);
        }
    }

    public interface OnMedicalAppointmentClickedListener {
        void OnMedicalAppointmentClicked(MedicalAppointment medicalAppointment);
    }
    private OnMedicalAppointmentClickedListener onMedicalAppointmentClickedListener;
    public void setOnMedicalAppointmentClicked(OnMedicalAppointmentClickedListener onMedicalAppointmentClickedListener) {
        this.onMedicalAppointmentClickedListener = onMedicalAppointmentClickedListener;
    }
}
