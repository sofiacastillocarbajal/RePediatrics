package pe.work.karique.repediatrics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.User;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private ContextProvider cp;
    private List<User> users;

    public UsersAdapter(List<User> Users, ContextProvider cp) {
        this.users = Users;
        this.cp = cp;
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_doctor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User user = users.get(position);

        holder.doctorNameTextView.setText(user.getDrName());
        holder.doctorSpecialityTextView.setText(user.getSpecialty());
        holder.doctorRateTextView.setText(String.valueOf(user.getRate()));
        holder.doctorDetailsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onUserClickedListener != null)
                    onUserClickedListener.OnUserClicked(user);
            }
        });
        holder.doctorPictureAppCompatImageView.setImageResource(user.getDoctorPictureId());
        //Picasso.with(cp.getContext())
        //        .load(user.getPictureUrl())
        //        .placeholder(R.drawable.ic_person_white)
        //        .error(R.drawable.ic_person_white)
        //        .into(holder.doctorPictureAppCompatImageView);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView doctorPictureAppCompatImageView;
        TextView doctorNameTextView;
        TextView doctorSpecialityTextView;
        TextView doctorRateTextView;
        CardView doctorDetailsCardView;

        public UserViewHolder(View itemView) {
            super(itemView);

            doctorPictureAppCompatImageView = itemView.findViewById(R.id.doctorPictureAppCompatImageView);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            doctorSpecialityTextView = itemView.findViewById(R.id.doctorSpecialityTextView);
            doctorRateTextView = itemView.findViewById(R.id.doctorRateTextView);
            doctorDetailsCardView = itemView.findViewById(R.id.doctorDetailsCardView);
        }
    }

    public interface ContextProvider {
        Context getContext();
    }

    public interface OnUserClickedListener {
        void OnUserClicked(User user);
    }

    private OnUserClickedListener onUserClickedListener;

    public void setOnUserClicked(OnUserClickedListener onUserClickedListener) {
        this.onUserClickedListener = onUserClickedListener;
    }
}
