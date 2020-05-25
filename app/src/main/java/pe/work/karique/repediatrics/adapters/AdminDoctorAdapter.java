package pe.work.karique.repediatrics.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.util.FuncionesFecha;


public class AdminDoctorAdapter extends RecyclerView.Adapter<AdminDoctorAdapter.UserViewHolder> {
    private List<User> specialists;

    public AdminDoctorAdapter(List<User> specialists) {
        this.specialists = specialists;
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_admin_doctor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User user = specialists.get(position);

        holder.doctorPictureAppCompatImageView.setImageResource(user.getDoctorPictureId());
        holder.fullNameTextView.setText(user.getDrName());
        holder.specialityTextView.setText(user.getSpecialty());
        int age = FuncionesFecha.getYearsFromNow(FuncionesFecha.getDateFromString(user.getBirthDate()));
        holder.ageTextView.setText(String.format("%s años de edad", String.valueOf(age)));
        int percentage = (int)((user.getRate() * 100)/5);
        holder.rateTextView.setText(String.format("%d%%", percentage));
        holder.placeTextView.setText(user.getBirthPlace());

        holder.doctorCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onUserClickedListener != null){
                    onUserClickedListener.OnUserClicked(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return specialists.size();
    }
    public class UserViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView doctorPictureAppCompatImageView;
        TextView fullNameTextView;
        TextView specialityTextView;
        TextView ageTextView;
        TextView rateTextView;
        TextView commentsTextView;
        TextView placeTextView;
        ConstraintLayout confirmConstraintLayout;
        TextView seePerfilTextView;
        CardView doctorCardView;

        public UserViewHolder(View itemView) {
            super(itemView);

            doctorPictureAppCompatImageView = itemView.findViewById(R.id.doctorPictureAppCompatImageView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            specialityTextView = itemView.findViewById(R.id.specialityTextView);
            ageTextView = itemView.findViewById(R.id.ageTextView);
            rateTextView = itemView.findViewById(R.id.rateTextView);
            commentsTextView = itemView.findViewById(R.id.commentsTextView);
            placeTextView = itemView.findViewById(R.id.placeTextView);
            confirmConstraintLayout = itemView.findViewById(R.id.confirmConstraintLayout);
            seePerfilTextView = itemView.findViewById(R.id.seePerfilTextView);
            doctorCardView = itemView.findViewById(R.id.doctorCardView);
        }
    }

    public interface OnUserClickedListener {
        void OnUserClicked(User user);
    }
    private OnUserClickedListener onUserClickedListener;
    public void setOnUserClicked(OnUserClickedListener onUserClickedListener) {
        this.onUserClickedListener = onUserClickedListener;
    }
}
