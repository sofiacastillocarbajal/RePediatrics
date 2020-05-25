package pe.work.karique.repediatrics.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimeTableByDoctor {
    private String id;
    private String startDateTime;
    private String endDateTime;
    private String specialistId;
    private String hospitalId;
    private String active;

    public static List<TimeTableByDoctor> from(JSONArray jsonArray){
        List<TimeTableByDoctor> timeTableByDoctors = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                timeTableByDoctors.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return timeTableByDoctors;
    }

    public static TimeTableByDoctor from(JSONObject jsonObject){
        TimeTableByDoctor timeTableByDoctor = new TimeTableByDoctor();
        try {
            timeTableByDoctor.setId(jsonObject.getString("id"));
            timeTableByDoctor.setStartDateTime(jsonObject.getString("startDateTime"));
            timeTableByDoctor.setEndDateTime(jsonObject.getString("endDateTime"));
            timeTableByDoctor.setSpecialistId(jsonObject.getString("specialistId"));
            timeTableByDoctor.setHospitalId(jsonObject.getString("hospitalId"));
            timeTableByDoctor.setActive(jsonObject.getString("active"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return timeTableByDoctor;
    }

    public static TimeTableByDoctor from(Bundle bundle){
        TimeTableByDoctor timeTableByDoctor = new TimeTableByDoctor();
        timeTableByDoctor.setId(bundle.getString("id"));
        timeTableByDoctor.setStartDateTime(bundle.getString("startDateTime"));
        timeTableByDoctor.setEndDateTime(bundle.getString("endDateTime"));
        timeTableByDoctor.setSpecialistId(bundle.getString("specialistId"));
        timeTableByDoctor.setHospitalId(bundle.getString("hospitalId"));
        timeTableByDoctor.setActive(bundle.getString("active"));

        return timeTableByDoctor;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("startDateTime", startDateTime);
        bundle.putString("endDateTime", endDateTime);
        bundle.putString("specialistId", specialistId);
        bundle.putString("hospitalId", hospitalId);
        bundle.putString("active", active);

        return bundle;
    }

    public TimeTableByDoctor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(String specialistId) {
        this.specialistId = specialistId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
