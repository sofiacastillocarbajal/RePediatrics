package pe.work.karique.repediatrics.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ticket {
    public static String STATE_EN_ESPERA = "En espera";
    public static String STATE_APROBADO = "Aprobado";
    public static String STATE_CANCELADO = "Cancelado";

    private String id;
    private String pediatricianId;
    private String parentId;
    private String patientId;
    private String hospitalId;
    private String questionaryId;
    private String commentFromPediatrician;
    private String commentToPatient;
    private String finalDiagnosis;
    private String state;
    private String active;
    private User pediatrician;
    private User parent;
    private User patient;
    private Hospital hospital;
    private Questionary questionary;

    public static List<Ticket> from(JSONArray jsonArray){
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                tickets.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tickets;
    }

    public static Ticket from(JSONObject jsonObject){
        Ticket ticket = new Ticket();
        try {
            ticket.setId(jsonObject.getString("id"));
            ticket.setPediatricianId(jsonObject.getString("pediatricianId"));
            ticket.setParentId(jsonObject.getString("parentId"));
            ticket.setPatientId(jsonObject.getString("patientId"));
            ticket.setHospitalId(jsonObject.getString("hospitalId"));
            ticket.setQuestionaryId(jsonObject.getString("questionaryId"));
            ticket.setCommentFromPediatrician(jsonObject.getString("commentFromPediatrician"));
            ticket.setCommentToPatient(jsonObject.getString("commentToPatient"));
            ticket.setFinalDiagnosis(jsonObject.getString("finalDiagnosis"));
            ticket.setState(jsonObject.getString("state"));
            ticket.setActive(jsonObject.getString("active"));

            User pediatrician = User.from(jsonObject.getJSONObject("user"));
            User parent = User.from(jsonObject.getJSONObject("user1"));
            User patient = User.from(jsonObject.getJSONObject("user2"));
            Hospital hospital = Hospital.from(jsonObject.getJSONObject("hospital"));
            Questionary questionary = Questionary.from(jsonObject.getJSONObject("questionary"));

            ticket.setPediatrician(pediatrician);
            ticket.setParent(parent);
            ticket.setPatient(patient);
            ticket.setHospital(hospital);
            ticket.setQuestionary(questionary);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ticket;
    }

    public static Ticket from(Bundle bundle){
        Ticket ticket = new Ticket();
        ticket.setId(bundle.getString("id"));
        ticket.setPediatricianId(bundle.getString("pediatricianId"));
        ticket.setParentId(bundle.getString("parentId"));
        ticket.setPatientId(bundle.getString("patientId"));
        ticket.setHospitalId(bundle.getString("hospitalId"));
        ticket.setQuestionaryId(bundle.getString("questionaryId"));
        ticket.setCommentFromPediatrician(bundle.getString("commentFromPediatrician"));
        ticket.setCommentToPatient(bundle.getString("commentToPatient"));
        ticket.setFinalDiagnosis(bundle.getString("finalDiagnosis"));
        ticket.setState(bundle.getString("state"));
        ticket.setActive(bundle.getString("active"));

        Bundle bundlePediatrician = bundle.getBundle("pediatrician");
        if (bundlePediatrician != null) {
            User pediatrician = User.from(bundlePediatrician);
            ticket.setPediatrician(pediatrician);
        }

        Bundle bundleParent = bundle.getBundle("parent");
        if (bundleParent != null) {
            User parent = User.from(bundleParent);
            ticket.setParent(parent);
        }

        Bundle bundlePatient = bundle.getBundle("patient");
        if (bundlePatient != null) {
            User specialist = User.from(bundlePatient);
            ticket.setPatient(specialist);
        }

        Bundle bundleHospital = bundle.getBundle("hospital");
        if (bundleHospital != null) {
            Hospital hospital = Hospital.from(bundleHospital);
            ticket.setHospital(hospital);
        }

        Bundle bundleQuestionary = bundle.getBundle("questionary");
        if (bundleQuestionary != null) {
            Questionary questionary = Questionary.from(bundleQuestionary);
            ticket.setQuestionary(questionary);
        }

        return ticket;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("pediatricianId", pediatricianId);
        bundle.putString("parentId", parentId);
        bundle.putString("patientId", patientId);
        bundle.putString("hospitalId", hospitalId);
        bundle.putString("questionaryId", questionaryId);
        bundle.putString("commentFromPediatrician", commentFromPediatrician);
        bundle.putString("commentToPatient", commentToPatient);
        bundle.putString("finalDiagnosis", finalDiagnosis);
        bundle.putString("state", state);
        bundle.putString("active", active);

        bundle.putBundle("pediatrician", pediatrician.toBundle());
        bundle.putBundle("parent", parent.toBundle());
        bundle.putBundle("patient", patient.toBundle());
        bundle.putBundle("hospital", hospital.toBundle());
        bundle.putBundle("questionary", questionary.toBundle());

        return bundle;
    }

    public Ticket() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPediatricianId() {
        return pediatricianId;
    }

    public void setPediatricianId(String pediatricianId) {
        this.pediatricianId = pediatricianId;
    }

    public String getCommentFromPediatrician() {
        return commentFromPediatrician.isEmpty() ? "Ninguno" : commentFromPediatrician;
    }

    public void setCommentFromPediatrician(String commentFromPediatrician) {
        this.commentFromPediatrician = commentFromPediatrician;
    }

    public String getCommentToPatient() {
        return commentToPatient;
    }

    public void setCommentToPatient(String commentToPatient) {
        this.commentToPatient = commentToPatient;
    }

    public String getFinalDiagnosis() {
        return finalDiagnosis;
    }

    public void setFinalDiagnosis(String finalDiagnosis) {
        this.finalDiagnosis = finalDiagnosis;
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

    public User getPediatrician() {
        return pediatrician;
    }

    public void setPediatrician(User pediatrician) {
        this.pediatrician = pediatrician;
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

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getQuestionaryId() {
        return questionaryId;
    }

    public void setQuestionaryId(String questionaryId) {
        this.questionaryId = questionaryId;
    }

    public User getParent() {
        return parent;
    }

    public void setParent(User parent) {
        this.parent = parent;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Questionary getQuestionary() {
        return questionary;
    }

    public void setQuestionary(Questionary questionary) {
        this.questionary = questionary;
    }
}
