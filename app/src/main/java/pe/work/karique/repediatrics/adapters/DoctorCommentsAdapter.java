package pe.work.karique.repediatrics.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.DoctorComment;
import pe.work.karique.repediatrics.models.TimeTableByDoctor;
import pe.work.karique.repediatrics.util.FuncionesFecha;


public class DoctorCommentsAdapter extends RecyclerView.Adapter<DoctorCommentsAdapter.UserViewHolder> {
    private List<DoctorComment> doctorComments;

    public DoctorCommentsAdapter(List<DoctorComment> doctorComments) {
        this.doctorComments = doctorComments;
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_doctor_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final DoctorComment doctorComment = doctorComments.get(position);

        holder.commentTextView.setText(doctorComment.getText());
        Date dateOfComment = FuncionesFecha.getDateFromString(doctorComment.getDateTimeOfComment());
        holder.userAndDateTextView.setText(String.format("%s - %s", doctorComment.getUser().getFullName(), FuncionesFecha.formatDateToTextForComment(dateOfComment)));

        holder.commentConstraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onDoctorCommentLongClickedListener != null){
                    onDoctorCommentLongClickedListener.OnDoctorCommentLongClicked(doctorComment);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorComments.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView commentTextView;
        TextView userAndDateTextView;
        ConstraintLayout commentConstraintLayout;

        public UserViewHolder(View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            userAndDateTextView = itemView.findViewById(R.id.userAndDateTextView);
            commentConstraintLayout = itemView.findViewById(R.id.commentConstraintLayout);
        }
    }

    public interface OnDoctorCommentClickedListener {
        void OnDoctorCommentClicked(DoctorComment doctorComment);
    }
    private OnDoctorCommentClickedListener onDoctorCommentClickedListener;
    public void setOnDoctorCommentClicked(OnDoctorCommentClickedListener onDoctorCommentClickedListener) {
        this.onDoctorCommentClickedListener = onDoctorCommentClickedListener;
    }

    public interface OnDoctorCommentLongClickedListener {
        void OnDoctorCommentLongClicked(DoctorComment doctorComment);
    }
    private OnDoctorCommentLongClickedListener onDoctorCommentLongClickedListener;
    public void setOnDoctorCommentLongClicked(OnDoctorCommentLongClickedListener onDoctorCommentLongClickedListener) {
        this.onDoctorCommentLongClickedListener = onDoctorCommentLongClickedListener;
    }
}
