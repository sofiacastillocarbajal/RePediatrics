package pe.work.karique.repediatrics.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.fragments.DoctorCommentsFragment;
import pe.work.karique.repediatrics.fragments.DoctorDetailsFragment;
import pe.work.karique.repediatrics.fragments.MedicalAppointmentsListWidgetFragment;
import pe.work.karique.repediatrics.fragments.ParentPatientsFragment;
import pe.work.karique.repediatrics.fragments.TicketsListWidgetFragment;
import pe.work.karique.repediatrics.models.DoctorComment;
import pe.work.karique.repediatrics.models.MedicalAppointment;
import pe.work.karique.repediatrics.models.Questionary;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;
import pe.work.karique.repediatrics.util.FuncionesFecha;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorDetailsActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 17;

    private AppCompatImageButton backImageButton;
    private ProgressBar progressBar;
    private TextView repediatricsTitleTextView;

    private TabLayout _homeTabLayout;
    private ViewPager _homeViewPager;
    private ViewPagerAdapter adapter;

    private DoctorDetailsFragment doctorDetailsFragment;
    private TicketsListWidgetFragment ticketsListWidgetFragment;
    private MedicalAppointmentsListWidgetFragment medicalAppointmentsListWidgetFragment;
    private DoctorCommentsFragment doctorCommentsFragment;

    private User doctor;

    private AppCompatImageView doctorPictureAppCompatImageView;
    private TextView fullNameValueTextView;
    private TextView specialityTitleTextView;
    private TextView doctorRateTextView;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        hide();

        sessionManager = SessionManager.getInstance(this);
        doctor = User.from(getIntent().getExtras());
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressBar = findViewById(R.id.progressBar);
        repediatricsTitleTextView = findViewById(R.id.repediatricsTitleTextView);
        repediatricsTitleTextView.setText(doctor.getFullName());

        doctorPictureAppCompatImageView = findViewById(R.id.doctorPictureAppCompatImageView);
        fullNameValueTextView = findViewById(R.id.fullNameValueTextView);
        specialityTitleTextView = findViewById(R.id.specialityTitleTextView);
        doctorRateTextView = findViewById(R.id.doctorRateTextView);

        setData();
        initializeTab();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void setData(){
        doctorPictureAppCompatImageView.setImageResource(doctor.getDoctorPictureId());
        fullNameValueTextView.setText(doctor.getDrName());
        specialityTitleTextView.setText(doctor.getSpecialty());
        doctorRateTextView.setText(String.valueOf(doctor.getRate()));
    }
    private void initializeTab(){
        String ticketsByDoctorUrl = doctor.isPediatrician() ? RepediatricsApi.TICKETS_BY_DOCTOR(doctor.getId()) : RepediatricsApi.TICKETS_BY_SPECIALISTS(doctor.getId());
        String medicalAppointmentsByDoctorUrl = RepediatricsApi.MEDICAL_APPOINTMENTS_BY_DOCTORS(doctor.getId());

        _homeTabLayout = findViewById(R.id.home_tabs);
        _homeViewPager = findViewById(R.id.home_viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        doctorDetailsFragment = new DoctorDetailsFragment();
        doctorDetailsFragment.setDoctor(doctor);

        if (sessionManager.isAdmin()) {
            ticketsListWidgetFragment = new TicketsListWidgetFragment();
            ticketsListWidgetFragment.setUrl(ticketsByDoctorUrl);
            ticketsListWidgetFragment.setOnProgressBarChanged(new TicketsListWidgetFragment.OnProgressBarChanged() {
                @Override
                public void OnProgressBarVisible() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void OnProgressBarHide() {
                    progressBar.setVisibility(View.GONE);
                }
            });
            ticketsListWidgetFragment.setOnTicketClicked(new TicketsListWidgetFragment.OnTicketClickedListener() {
                @Override
                public void onTicketClicked(Ticket ticket) {
                    showTicketDetails(ticket);
                }
            });
        }
        if (sessionManager.isAdmin()) {
            medicalAppointmentsListWidgetFragment = new MedicalAppointmentsListWidgetFragment();
            medicalAppointmentsListWidgetFragment.setUrl(medicalAppointmentsByDoctorUrl);
            medicalAppointmentsListWidgetFragment.setOnProgressBarChanged(new MedicalAppointmentsListWidgetFragment.OnProgressBarChanged() {
                @Override
                public void OnProgressBarVisible() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void OnProgressBarHide() {
                    progressBar.setVisibility(View.GONE);
                }
            });
            medicalAppointmentsListWidgetFragment.setOnMedicalAppointmentClicked(new MedicalAppointmentsListWidgetFragment.OnMedicalAppointmentClickedListener() {
                @Override
                public void onMedicalAppointmentClicked(MedicalAppointment medicalAppointment) {
                    showMedicalAppointmentsDetails(medicalAppointment);
                }
            });
        }

        doctorCommentsFragment = new DoctorCommentsFragment();
        doctorCommentsFragment.setDoctor(doctor);
        doctorCommentsFragment.setOnProgressBarChanged(new DoctorCommentsFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        doctorCommentsFragment.setOnDoctorCommentClicked(new DoctorCommentsFragment.OnDoctorCommentClickedListener() {
            @Override
            public void OnDoctorCommentClicked(DoctorComment doctorComment) {

            }
        });
        doctorCommentsFragment.setOnAddCommentClicked(new DoctorCommentsFragment.OnAddCommentClickedListener() {
            @Override
            public void OnAddCommentClicked() {
                showInsertCommentDialog();
            }
        });
        doctorCommentsFragment.setOnDoctorCommentLongClicked(new DoctorCommentsFragment.OnDoctorCommentLongClickedListener() {
            @Override
            public void OnDoctorCommentLongClicked(DoctorComment doctorComment) {
                showAlertDeleteComment(doctorComment);
            }
        });

        adapter.addFragment(doctorDetailsFragment, "Detalles");
        if (sessionManager.isAdmin()) {
            adapter.addFragment(ticketsListWidgetFragment, "Tickets");
        }
        if (sessionManager.isAdmin()) {
            adapter.addFragment(medicalAppointmentsListWidgetFragment, "Citas");
        }
        adapter.addFragment(doctorCommentsFragment, "Comentarios");

        _homeViewPager.setAdapter(adapter);
        _homeTabLayout.setupWithViewPager(_homeViewPager);
        _homeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {}
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    private void showTicketDetails(Ticket ticket){
        Intent intent = new Intent(this, TicketDetailsActivity.class);
        intent.putExtras(ticket.toBundle());
        startActivityForResult(intent, TicketDetailsActivity.REQUEST_CODE);
    }
    private void showMedicalAppointmentsDetails(MedicalAppointment medicalAppointment){
        Intent intent = new Intent(this, MedicalAppointmentDetailsActivity.class);
        intent.putExtras(medicalAppointment.toBundle());
        startActivityForResult(intent, MedicalAppointmentDetailsActivity.REQUEST_CODE);
    }
    private void showInsertCommentDialog(){
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Agregue un comentario")
                .setMessage("¿Cuál es el comentario que quiere agregar?")
                .setView(taskEditText)
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = String.valueOf(taskEditText.getText());
                        addComment(comment);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
    private void addComment(String comment){
        progressBar.setVisibility(View.VISIBLE);

        JSONObject commentJsonObject = new JSONObject();
        try {
            commentJsonObject.put("text", comment);
            commentJsonObject.put("parentId", sessionManager.getid());
            commentJsonObject.put("doctorId", doctor.getId());
            commentJsonObject.put("dateTimeOfComment", FuncionesFecha.formatDateForAPI(FuncionesFecha.getCurrentDate()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(RepediatricsApi.COMMENTSBYDOCTOR_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(commentJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(DoctorDetailsActivity.this, "Comentario agregado correctamente", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        doctorCommentsFragment.getComments();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(DoctorDetailsActivity.this, "Error al agregar comentario", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
    private void showAlertDeleteComment(DoctorComment doctorComment){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro que desea eliminar este comentario?")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteComment(doctorComment);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void deleteComment(DoctorComment doctorComment){
        progressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.delete(RepediatricsApi.COMMENTSBYDOCTOR_URL + "/" + doctorComment.getId())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(
                                DoctorDetailsActivity.this,
                                "Comentario eliminado correctamente",
                                Toast.LENGTH_SHORT
                        ).show();
                        doctorCommentsFragment.getComments();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(
                                DoctorDetailsActivity.this,
                                "Eliminar comentario" + " - " + "Error de conexión",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
