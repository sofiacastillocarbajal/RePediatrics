package pe.work.karique.repediatrics.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.fragments.AccountFragment;
import pe.work.karique.repediatrics.fragments.AdminDoctorsFragment;
import pe.work.karique.repediatrics.fragments.AdminMedicalAppointmentsFragment;
import pe.work.karique.repediatrics.fragments.AdminParentsFragment;
import pe.work.karique.repediatrics.fragments.AdminTicketsFragment;
import pe.work.karique.repediatrics.fragments.MedicalAppointmentsListWidgetFragment;
import pe.work.karique.repediatrics.fragments.ParentHomeFragment;
import pe.work.karique.repediatrics.fragments.TicketsListWidgetFragment;
import pe.work.karique.repediatrics.models.MedicalAppointment;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;
import pe.work.karique.repediatrics.util.FuncionesFecha;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.libizo.CustomEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

public class AdminMainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static int TIME_MODE_FECHA_INICIO = 1;
    private static int TIME_MODE_FECHA_FIN = 2;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return navigateAccordingTo(item.getItemId());
        }
    };

    private BottomNavigationView navigation;
    private ProgressBar progressBar;

    private AdminDoctorsFragment adminDoctorsFragment;
    private MedicalAppointmentsListWidgetFragment adminMedicalAppointmentsFragment;
    private AdminParentsFragment adminParentsFragment;
    private TicketsListWidgetFragment adminTicketsFragment;
    private AccountFragment accountFragment;

    private SessionManager sessionManager;

    private FloatingActionButton speedDialFloatingActionButton;
    private CardView filterControlsCardView;
    private CardView closeFilterCardView;
    private CardView searchFilterCardView;
    private CardView downloadCardView;
    private CustomEditText startDateCustomEditText;
    private CustomEditText endDateCustomEditText;

    private int TIME_MODE = TIME_MODE_FECHA_INICIO;
    private String startTime = "";
    private String endTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        hide();

        sessionManager = SessionManager.getInstance(this);
        progressBar = findViewById(R.id.progressBar);

        filterControlsCardView = findViewById(R.id.filterControlsCardView);
        closeFilterCardView = findViewById(R.id.closeFilterCardView);
        closeFilterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterControlsCardView.setVisibility(View.GONE);
                speedDialFloatingActionButton.show();
                getAllMedicalAppointments();
            }
        });
        searchFilterCardView = findViewById(R.id.searchFilterCardView);
        searchFilterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMedicalAppointmentsFiltered();
            }
        });
        downloadCardView = findViewById(R.id.downloadCardView);
        downloadCardView.setVisibility(View.GONE);
        downloadCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWriteExcel();
            }
        });
        startDateCustomEditText = findViewById(R.id.startDateCustomEditText);
        startDateCustomEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TIME_MODE = TIME_MODE_FECHA_INICIO;
                showDatePickerDialog();
            }
        });
        endDateCustomEditText = findViewById(R.id.endDateCustomEditText);
        endDateCustomEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TIME_MODE = TIME_MODE_FECHA_FIN;
                showDatePickerDialog();
            }
        });

        speedDialFloatingActionButton = findViewById(R.id.speedDialFloatingActionButton);
        speedDialFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterControlsCardView.setVisibility(View.VISIBLE);
                speedDialFloatingActionButton.hide();
            }
        });
        speedDialFloatingActionButton.hide();

        filterControlsCardView.setOnClickListener(null);

        navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigateAccordingTo(R.id.navigation_admin_doctors);
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private boolean navigateAccordingTo(int id){
        try
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content,getFragmentFor(id))
                    .commit();
            return true;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }
    private Fragment getFragmentFor(int id) {
        if (id == R.id.navigation_admin_doctors) {
            filterControlsCardView.setVisibility(View.GONE);
            speedDialFloatingActionButton.hide();
            return getAdminDoctorsFragment();
        }
        else if (id == R.id.navigation_admin_parents) {
            filterControlsCardView.setVisibility(View.GONE);
            speedDialFloatingActionButton.hide();
            return getAdminParentsFragment();
        }
        else if (id == R.id.navigation_admin_ticket) {
            filterControlsCardView.setVisibility(View.GONE);
            speedDialFloatingActionButton.hide();
            return getAdminTicketsFragment();
        }
        else if (id == R.id.navigation_admin_medical_appointments) {
            speedDialFloatingActionButton.show();
            return getAdminMedicalAppointmentsFragment();
        }
        else if (id == R.id.navigation_admin_account) {
            filterControlsCardView.setVisibility(View.GONE);
            speedDialFloatingActionButton.hide();
            return getAccountFragment();
        }
        return null;
    }
    private void startAdminParentSummary(User parent){
        Intent intent = new Intent(this, AdminParentSummaryActivity.class);
        intent.putExtra("parent", parent.toBundle());
        startActivityForResult(intent, AdminParentSummaryActivity.REQUEST_CODE);
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
    private void showDoctorDetails(User doctor){
        Intent intent = new Intent(this, DoctorDetailsActivity.class);
        intent.putExtras(doctor.toBundle());
        startActivityForResult(intent, DoctorDetailsActivity.REQUEST_CODE);
    }
    private void showDatePickerDialog(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AdminMainActivity.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        String titleSelection = TIME_MODE == TIME_MODE_FECHA_INICIO ? "Seleccione una fecha inicial" : "Seleccione una fecha final";
        dpd.setTitle(titleSelection);
        dpd.setAccentColor(getResources().getColor(R.color.colorAccent));
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }
    private void getMedicalAppointmentsFiltered(){
        if (startTime.equals("") || endTime.equals("")){
            Toast.makeText(this, "Ingrese rango de fechas", Toast.LENGTH_LONG).show();
        }
        else {
            String url = RepediatricsApi.MEDICAL_APPOINTMENTS_BY_RANGEDATE(startTime, endTime);
            adminMedicalAppointmentsFragment.setUrl(url);
            adminMedicalAppointmentsFragment.getMedicalAppointments();
            downloadCardView.setVisibility(View.VISIBLE);
        }
    }
    private void getAllMedicalAppointments(){
        adminMedicalAppointmentsFragment.setUrl(RepediatricsApi.MEDICAL_APPOINTMENTS_URL);
        adminMedicalAppointmentsFragment.getMedicalAppointments();
    }
    private void startWriteExcel(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            writeExcel();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }
    private void writeExcel() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "Citas medicas de " + startTime + " a " + endTime);

        String[] headers = new String[]{
                "Especialista: Username",
                "Especialista: Nombre",
                "Padre: Username",
                "Padre: Nombre",
                "Paciente: Username",
                "Paciente: Nombre",
                "Inicio",
                "Fin",
                "Hospital",
                "Porcentaje de cuestionario",
                "Número de Ticket",
                "Estado"
        };

        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; ++i) {
            String header = headers[i];
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(header);
        }

        for (int i = 0; i < adminMedicalAppointmentsFragment.medicalAppointments.size(); ++i) {
            HSSFRow dataRow = sheet.createRow(i + 1);

            MedicalAppointment medicalAppointment = adminMedicalAppointmentsFragment.medicalAppointments.get(i);
            String specialistUsername = medicalAppointment.getSpecialist().getUsername();
            String specialistFullName = medicalAppointment.getSpecialist().getFullName();
            String parentUserName = medicalAppointment.getParent().getUsername();
            String parentFullName = medicalAppointment.getParent().getFullName();
            String patientUserName = medicalAppointment.getPatient().getUsername();
            String patientFullName = medicalAppointment.getPatient().getFullName();
            String inicio = medicalAppointment.getTimeTableByDoctor().getStartDateTime();
            String fin = medicalAppointment.getTimeTableByDoctor().getEndDateTime();
            String hospital = medicalAppointment.getHospital().getName();
            String questionaryPercenatge = medicalAppointment.getQuestionary().getPercentage();
            String ticketNumber = medicalAppointment.getTicket().getId();
            String state = medicalAppointment.getState();

            dataRow.createCell(0).setCellValue(specialistUsername);
            dataRow.createCell(1).setCellValue(specialistFullName);
            dataRow.createCell(2).setCellValue(parentUserName);
            dataRow.createCell(3).setCellValue(parentFullName);
            dataRow.createCell(4).setCellValue(patientUserName);
            dataRow.createCell(5).setCellValue(patientFullName);
            dataRow.createCell(6).setCellValue(inicio);
            dataRow.createCell(7).setCellValue(fin);
            dataRow.createCell(8).setCellValue(hospital);
            dataRow.createCell(9).setCellValue(questionaryPercenatge);
            dataRow.createCell(10).setCellValue(ticketNumber);
            dataRow.createCell(11).setCellValue(state);
        }

        HSSFRow dataRow = sheet.createRow(1 + adminMedicalAppointmentsFragment.medicalAppointments.size());
        dataRow.createCell(1);

        FileOutputStream file = null;
        boolean paso1 = false;
        boolean paso2 = false;
        boolean paso3 = false;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String fecha = FuncionesFecha.formatDateForFileWithHour(FuncionesFecha.getCurrentDate());
            file = new FileOutputStream(new File(dir, "Resumen_RePediatrics_" + fecha + ".xls"));
            paso1 = true;

            try {
                workbook.write(file);
                paso2 = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                file.close();
                paso3 = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (paso1 && paso2 && paso3){
            Toast.makeText(this, "Resumen creado en \\Documentos", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Error al crear el resumen", Toast.LENGTH_LONG).show();
        }
    }

    private AdminDoctorsFragment getAdminDoctorsFragment(){
        adminDoctorsFragment = new AdminDoctorsFragment();
        adminDoctorsFragment.setOnProgressBarChanged(new AdminDoctorsFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        adminDoctorsFragment.setOnUserClicked(new AdminDoctorsFragment.OnUserClickedListener() {
            @Override
            public void OnUserClicked(User doctor) {
                showDoctorDetails(doctor);
            }
        });
        return adminDoctorsFragment;
    }
    private MedicalAppointmentsListWidgetFragment getAdminMedicalAppointmentsFragment(){
        adminMedicalAppointmentsFragment = new MedicalAppointmentsListWidgetFragment();
        adminMedicalAppointmentsFragment.setUrl(RepediatricsApi.MEDICAL_APPOINTMENTS_URL);
        adminMedicalAppointmentsFragment.setOnProgressBarChanged(new MedicalAppointmentsListWidgetFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        adminMedicalAppointmentsFragment.setOnMedicalAppointmentClicked(new MedicalAppointmentsListWidgetFragment.OnMedicalAppointmentClickedListener() {
            @Override
            public void onMedicalAppointmentClicked(MedicalAppointment medicalAppointment) {
                showMedicalAppointmentsDetails(medicalAppointment);
            }
        });

        return adminMedicalAppointmentsFragment;
    }
    private AdminParentsFragment getAdminParentsFragment(){
        adminParentsFragment = new AdminParentsFragment();
        adminParentsFragment.setOnProgressBarChanged(new AdminParentsFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        adminParentsFragment.setOnUserClicked(new AdminParentsFragment.OnUserClickedListener() {
            @Override
            public void OnUserClicked(User parent) {
                startAdminParentSummary(parent);
            }
        });
        return adminParentsFragment;
    }
    private TicketsListWidgetFragment getAdminTicketsFragment(){
        adminTicketsFragment = new TicketsListWidgetFragment();
        adminTicketsFragment.setUrl(RepediatricsApi.TICKETS_URL);
        adminTicketsFragment.setOnProgressBarChanged(new TicketsListWidgetFragment.OnProgressBarChanged() {
            @Override
            public void OnProgressBarVisible() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnProgressBarHide() {
                progressBar.setVisibility(View.GONE);
            }
        });
        adminTicketsFragment.setOnTicketClicked(new TicketsListWidgetFragment.OnTicketClickedListener() {
            @Override
            public void onTicketClicked(Ticket ticket) {
                showTicketDetails(ticket);
            }
        });

        return adminTicketsFragment;
    }
    private AccountFragment getAccountFragment(){
        accountFragment = new AccountFragment();
        return accountFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Patient agregado correctamente
        //if (requestCode == AddPatientActivity.REQUEST_CODE) {
        //    if (resultCode == Activity.RESULT_OK) {
        //        if (parentPatientsFragment != null)
        //            parentPatientsFragment.getPatientsByParent();
        //    }
        //}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0){
            writeExcel();
        }
        else {
            Toast.makeText(this, "Debe darle permisos a la aplicacion de escritura de archivos", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (TIME_MODE == TIME_MODE_FECHA_INICIO){
            //2018-05-8
            startTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            startDateCustomEditText.setText(startTime);
        }
        else if (TIME_MODE == TIME_MODE_FECHA_FIN){
            //2022-05-8
            endTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            endDateCustomEditText.setText(endTime);
        }
    }
}


































