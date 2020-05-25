package pe.work.karique.repediatrics.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Hospital {
    private String id;
    private String name;
    private String lat;
    private String lng;
    private String rate;
    private String timeTable;
    private String department;
    private String district;
    private String address;

    public static List<Hospital> from(JSONArray jsonArray){
        List<Hospital> hospitals = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                hospitals.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return hospitals;
    }

    public static Hospital from(JSONObject jsonObject){
        Hospital hospital = new Hospital();
        try {
            hospital.setId(jsonObject.getString("id"));
            hospital.setName(jsonObject.getString("name"));
            hospital.setLat(jsonObject.getString("lat"));
            hospital.setLng(jsonObject.getString("lng"));
            hospital.setRate(jsonObject.getString("rate"));
            hospital.setTimeTable(jsonObject.getString("timeTable"));
            hospital.setDepartment(jsonObject.getString("department"));
            hospital.setDistrict(jsonObject.getString("district"));
            hospital.setAddress(jsonObject.getString("address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return hospital;
    }

    public static Hospital from(Bundle bundle){
        Hospital hospital = new Hospital();
        hospital.setId(bundle.getString("id"));
        hospital.setName(bundle.getString("name"));
        hospital.setLat(bundle.getString("lat"));
        hospital.setLng(bundle.getString("lng"));
        hospital.setRate(bundle.getString("rate"));
        hospital.setTimeTable(bundle.getString("timeTable"));
        hospital.setDepartment(bundle.getString("department"));
        hospital.setDistrict(bundle.getString("district"));
        hospital.setAddress(bundle.getString("address"));

        return hospital;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putString("lat", lat);
        bundle.putString("lng", lng);
        bundle.putString("rate", rate);
        bundle.putString("timeTable", timeTable);
        bundle.putString("department", department);
        bundle.putString("district", district);
        bundle.putString("address", address);

        return bundle;
    }

    public Hospital() {
    }

    public Hospital(String id, String name, String lat, String lng, String rate, String timeTable, String department, String district, String address) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.rate = rate;
        this.timeTable = timeTable;
        this.department = department;
        this.district = district;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(String timeTable) {
        this.timeTable = timeTable;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
