package pe.work.karique.repediatrics.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MedicalAppointmentState {
    private String id;
    private String medicalAppointmentId;
    private String state;
    private String dateOfUpdate;
    private String userUpdateId;
    private User userUpdate;

    public static List<MedicalAppointmentState> from(JSONArray jsonArray){
        List<MedicalAppointmentState> medicalAppointmentStates = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                medicalAppointmentStates.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return medicalAppointmentStates;
    }

    public static MedicalAppointmentState from(JSONObject jsonObject){
        MedicalAppointmentState medicalAppointmentState = new MedicalAppointmentState();
        try {
            medicalAppointmentState.setId(jsonObject.getString("id"));
            medicalAppointmentState.setMedicalAppointmentId(jsonObject.getString("medicalAppointmentId"));
            medicalAppointmentState.setState(jsonObject.getString("state"));
            medicalAppointmentState.setDateOfUpdate(jsonObject.getString("dateOfUpdate"));
            medicalAppointmentState.setUserUpdateId(jsonObject.getString("userUpdateId"));

            User userUpdate = User.from(jsonObject.getJSONObject("user"));
            medicalAppointmentState.setUserUpdate(userUpdate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return medicalAppointmentState;
    }

    public MedicalAppointmentState() {
    }

    public MedicalAppointmentState(String id, String medicalAppointmentId, String state, String dateOfUpdate, String userUpdateId, User userUpdate) {
        this.id = id;
        this.medicalAppointmentId = medicalAppointmentId;
        this.state = state;
        this.dateOfUpdate = dateOfUpdate;
        this.userUpdateId = userUpdateId;
        this.userUpdate = userUpdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicalAppointmentId() {
        return medicalAppointmentId;
    }

    public void setMedicalAppointmentId(String medicalAppointmentId) {
        this.medicalAppointmentId = medicalAppointmentId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDateOfUpdate() {
        return dateOfUpdate;
    }

    public void setDateOfUpdate(String dateOfUpdate) {
        this.dateOfUpdate = dateOfUpdate;
    }

    public String getUserUpdateId() {
        return userUpdateId;
    }

    public void setUserUpdateId(String userUpdateId) {
        this.userUpdateId = userUpdateId;
    }

    public User getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(User userUpdate) {
        this.userUpdate = userUpdate;
    }
}
