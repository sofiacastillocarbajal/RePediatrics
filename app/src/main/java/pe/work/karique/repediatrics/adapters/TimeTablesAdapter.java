package pe.work.karique.repediatrics.adapters;

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
import pe.work.karique.repediatrics.models.TimeTableByDoctor;
import pe.work.karique.repediatrics.util.FuncionesFecha;


public class TimeTablesAdapter extends RecyclerView.Adapter<TimeTablesAdapter.UserViewHolder> {
    private List<TimeTableByDoctor> timeTableByDoctors;

    public TimeTablesAdapter(List<TimeTableByDoctor> timeTableByDoctors) {
        this.timeTableByDoctors = timeTableByDoctors;
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_timetable, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final TimeTableByDoctor timeTableByDoctor = timeTableByDoctors.get(position);

        Date dateStart = FuncionesFecha.getDateWithHourFromString(timeTableByDoctor.getStartDateTime());
        Date dateEnd = FuncionesFecha.getDateWithHourFromString(timeTableByDoctor.getEndDateTime());

        String dateFormatted = FuncionesFecha.formatDate(dateStart);
        holder.dateTextView.setText(dateFormatted);

        String timeStart = FuncionesFecha.formatDateToHour(dateStart);
        String timeEnd = FuncionesFecha.formatDateToHour(dateEnd);
        holder.timeTextView.setText(String.format("%s - %s", timeStart, timeEnd));

        holder.timeTableCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTimeTableByDoctorClickedListener != null)
                    onTimeTableByDoctorClickedListener.OnTimeTableByDoctorClicked(timeTableByDoctor);
            }
        });
        holder.timeTableCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onTimeTableByDoctorLongClickedListener != null){
                    onTimeTableByDoctorLongClickedListener.OnTimeTableByDoctorLongClicked(timeTableByDoctor);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeTableByDoctors.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        CardView timeTableCardView;
        TextView timeTextView;
        TextView dateTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTableCardView = itemView.findViewById(R.id.timeTableCardView);
        }
    }

    public interface OnTimeTableByDoctorClickedListener {
        void OnTimeTableByDoctorClicked(TimeTableByDoctor medicalAppointment);
    }
    private OnTimeTableByDoctorClickedListener onTimeTableByDoctorClickedListener;
    public void setOnTimeTableByDoctorClicked(OnTimeTableByDoctorClickedListener onTimeTableByDoctorClickedListener) {
        this.onTimeTableByDoctorClickedListener = onTimeTableByDoctorClickedListener;
    }

    public interface OnTimeTableByDoctorLongClickedListener {
        void OnTimeTableByDoctorLongClicked(TimeTableByDoctor timeTableByDoctor);
    }
    private OnTimeTableByDoctorLongClickedListener onTimeTableByDoctorLongClickedListener;
    public void setOnTimeTableByDoctorLongClicked(OnTimeTableByDoctorLongClickedListener onTimeTableByDoctorLongClickedListener) {
        this.onTimeTableByDoctorLongClickedListener = onTimeTableByDoctorLongClickedListener;
    }
}
