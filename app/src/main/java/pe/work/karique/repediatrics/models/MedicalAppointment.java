package pe.work.karique.repediatrics.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MedicalAppointment {
    public static String STATE_EN_ESPERA = "En espera";
    public static String STATE_APROBADO = "Aprobado";
    public static String STATE_CANCELADO = "Cancelado";

    private String id;
    private String specialistId;
    private String parentId;
    private String patientId;
    private String timeTableByDoctorId;
    private String hospitalId;
    private String questionaryId;
    private String ticketId;
    private String state;
    private String active;
    private User specialist;
    private User parent;
    private User patient;
    private TimeTableByDoctor timeTableByDoctor;
    private Hospital hospital;
    private Questionary questionary;
    private Ticket ticket;

    public static List<MedicalAppointment> from(JSONArray jsonArray){
        List<MedicalAppointment> medicalAppointments = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                medicalAppointments.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return medicalAppointments;
    }

    public static MedicalAppointment from(JSONObject jsonObject){
        MedicalAppointment medicalAppointment = new MedicalAppointment();
        try {
            medicalAppointment.setId(jsonObject.getString("id"));
            medicalAppointment.setSpecialistId(jsonObject.getString("specialistId"));
            medicalAppointment.setParentId(jsonObject.getString("parentId"));
            medicalAppointment.setPatientId(jsonObject.getString("patientId"));
            medicalAppointment.setTimeTableByDoctorId(jsonObject.getString("timeTableByDoctorId"));
            medicalAppointment.setHospitalId(jsonObject.getString("hospitalId"));
            medicalAppointment.setQuestionaryId(jsonObject.getString("questionaryId"));
            medicalAppointment.setTicketId(jsonObject.getString("ticketId"));
            medicalAppointment.setState(jsonObject.getString("state"));
            medicalAppointment.setActive(jsonObject.getString("active"));

            User specialist = User.from(jsonObject.getJSONObject("user"));
            User patient = User.from(jsonObject.getJSONObject("user2"));
            TimeTableByDoctor timeTableByDoctor = TimeTableByDoctor.from(jsonObject.getJSONObject("timeTableByDoctor"));
            Hospital hospital = Hospital.from(jsonObject.getJSONObject("hospital"));
            Questionary questionary = Questionary.from(jsonObject.getJSONObject("questionary"));
            Ticket ticket = Ticket.from(jsonObject.getJSONObject("ticket"));
            User parent = User.from(jsonObject.getJSONObject("user1"));

            medicalAppointment.setSpecialist(specialist);
            medicalAppointment.setTimeTableByDoctor(timeTableByDoctor);
            medicalAppointment.setHospital(hospital);
            medicalAppointment.setPatient(patient);
            medicalAppointment.setQuestionary(questionary);
            medicalAppointment.setTicket(ticket);
            medicalAppointment.setParent(parent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return medicalAppointment;
    }

    public static MedicalAppointment from(Bundle bundle){
        MedicalAppointment medicalAppointment = new MedicalAppointment();
        medicalAppointment.setId(bundle.getString("id"));
        medicalAppointment.setSpecialistId(bundle.getString("specialistId"));
        medicalAppointment.setParentId(bundle.getString("parentId"));
        medicalAppointment.setPatientId(bundle.getString("patientId"));
        medicalAppointment.setTimeTableByDoctorId(bundle.getString("timeTableByDoctorId"));
        medicalAppointment.setHospitalId(bundle.getString("hospitalId"));
        medicalAppointment.setQuestionaryId(bundle.getString("questionaryId"));
        medicalAppointment.setTicketId(bundle.getString("ticketId"));
        medicalAppointment.setState(bundle.getString("state"));
        medicalAppointment.setActive(bundle.getString("active"));

        Bundle bundleDoctor = bundle.getBundle("specialist");
        if (bundleDoctor != null) {
            User doctor = User.from(bundleDoctor);
            medicalAppointment.setSpecialist(doctor);
        }

        Bundle bundlePatient = bundle.getBundle("patient");
        if (bundlePatient != null) {
            User patient = User.from(bundlePatient);
            medicalAppointment.setPatient(patient);
        }

        Bundle bundleTimeTableByDoctor = bundle.getBundle("timeTableByDoctor");
        if (bundleTimeTableByDoctor != null){
            TimeTableByDoctor timeTableByDoctor = TimeTableByDoctor.from(bundleTimeTableByDoctor);
            medicalAppointment.setTimeTableByDoctor(timeTableByDoctor);
        }

        Bundle bundleHospital = bundle.getBundle("hospital");
        if (bundleHospital != null) {
            Hospital hospital = Hospital.from(bundleHospital);
            medicalAppointment.setHospital(hospital);
        }

        Bundle bundleQuestionary = bundle.getBundle("questionary");
        if (bundleQuestionary != null) {
            Questionary questionary = Questionary.from(bundleQuestionary);
            medicalAppointment.setQuestionary(questionary);
        }

        Bundle bundleTicket = bundle.getBundle("ticket");
        if (bundleTicket != null) {
            Ticket ticket = Ticket.from(bundleTicket);
            medicalAppointment.setTicket(ticket);
        }

        Bundle bundleParent = bundle.getBundle("parent");
        if (bundleParent != null) {
            User parent = User.from(bundleParent);
            medicalAppointment.setParent(parent);
        }

        return medicalAppointment;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("specialistId", specialistId);
        bundle.putString("parentId", parentId);
        bundle.putString("patientId", patientId);
        bundle.putString("timeTableByDoctorId", timeTableByDoctorId);
        bundle.putString("hospitalId", hospitalId);
        bundle.putString("questionaryId", questionaryId);
        bundle.putString("ticketId", ticketId);
        bundle.putString("state", state);
        bundle.putString("active", active);

        bundle.putBundle("hospital", hospital.toBundle());
        bundle.putBundle("timeTableByDoctor", timeTableByDoctor.toBundle());
        bundle.putBundle("specialist", specialist.toBundle());
        bundle.putBundle("patient", patient.toBundle());
        bundle.putBundle("questionary", questionary.toBundle());
        bundle.putBundle("ticket", ticket.toBundle());
        bundle.putBundle("parent", parent.toBundle());

        return bundle;
    }

    public MedicalAppointment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(String specialistId) {
        this.specialistId = specialistId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getTimeTableByDoctorId() {
        return timeTableByDoctorId;
    }

    public void setTimeTableByDoctorId(String timeTableByDoctorId) {
        this.timeTableByDoctorId = timeTableByDoctorId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public User getSpecialist() {
        return specialist;
    }

    public void setSpecialist(User specialist) {
        this.specialist = specialist;
    }

    public TimeTableByDoctor getTimeTableByDoctor() {
        return timeTableByDoctor;
    }

    public void setTimeTableByDoctor(TimeTableByDoctor timeTableByDoctor) {
        this.timeTableByDoctor = timeTableByDoctor;
    }

    public String getQuestionaryId() {
        return questionaryId;
    }

    public void setQuestionaryId(String questionaryId) {
        this.questionaryId = questionaryId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public Questionary getQuestionary() {
        return questionary;
    }

    public void setQuestionary(Questionary questionary) {
        this.questionary = questionary;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public User getParent() {
        return parent;
    }

    public void setParent(User parent) {
        this.parent = parent;
    }
}
