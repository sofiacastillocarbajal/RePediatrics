package pe.work.karique.repediatrics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.User;


public class ParentsAdapter extends RecyclerView.Adapter<ParentsAdapter.UserViewHolder> {
    private List<User> users;

    public ParentsAdapter(List<User> Users) {
        this.users = Users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_parent, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User user = users.get(position);

        holder.usernameTextView.setText(user.getUsername());
        holder.fullNameTextView.setText(user.getFullName());
        holder.parentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onUserClickedListener != null)
                    onUserClickedListener.OnUserClicked(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView fullNameTextView;
        CardView parentCardView;

        public UserViewHolder(View itemView) {
            super(itemView);

            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            parentCardView = itemView.findViewById(R.id.parentCardView);
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
