package pe.work.karique.repediatrics.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.work.karique.repediatrics.R;

public class User {
    public static String SPECIALITY_PEDIATRICIAN = "Pediatria";
    public static String SPECIALITY_ONCOLOGIST = "Oncologia";

    private String id;
    private String name;
    private String lastName;
    private String userTypeId;
    private String gender;
    private String dni;
    private String nationality;
    private String birthDate;
    private String birthPlace;
    private String insuranceType;
    private String email;
    private String pictureUrl;
    private String specialty;
    private String active;
    private String username;
    private float  rate;
    private UserType userType;

    public String getDrName(){
        String honorifico = gender.equals("Masculino") ? "Dr. " : "Dra. ";
        return honorifico + name + " " + lastName;
    }

    public static List<User> from(JSONArray jsonArray){
        List<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                users.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return users;
    }

    public static User from(JSONObject jsonObject){
        User user = new User();
        try {
            user.setId(jsonObject.getString("id"));
            user.setName(jsonObject.getString("name"));
            user.setLastName(jsonObject.getString("lastName"));
            user.setUserTypeId(jsonObject.getString("userTypeId"));
            user.setGender(jsonObject.getString("gender"));
            user.setDni(jsonObject.getString("dni"));
            user.setNationality(jsonObject.getString("nationality"));
            user.setBirthDate(jsonObject.getString("birthDate"));
            user.setBirthPlace(jsonObject.getString("birthPlace"));
            user.setInsuranceType(jsonObject.getString("insuranceType"));
            user.setEmail(jsonObject.getString("email"));
            user.setPictureUrl(jsonObject.getString("pictureUrl"));
            user.setSpecialty(jsonObject.getString("speciality"));
            user.setActive(jsonObject.getString("active"));
            user.setUsername(jsonObject.getString("username"));
            user.setRate((float)jsonObject.getDouble("rate"));

            UserType userType = UserType.from(jsonObject.getJSONObject("userType"));
            user.setUserType(userType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static User from(Bundle bundle){
        User user = new User();
        user.setId(bundle.getString("id"));
        user.setName(bundle.getString("name"));
        user.setLastName(bundle.getString("lastName"));
        user.setUserTypeId(bundle.getString("userTypeId"));
        user.setGender(bundle.getString("gender"));
        user.setDni(bundle.getString("dni"));
        user.setNationality(bundle.getString("nationality"));
        user.setBirthDate(bundle.getString("birthDate"));
        user.setBirthPlace(bundle.getString("birthPlace"));
        user.setInsuranceType(bundle.getString("insuranceType"));
        user.setEmail(bundle.getString("email"));
        user.setPictureUrl(bundle.getString("pictureUrl"));
        user.setSpecialty(bundle.getString("specialty"));
        user.setActive(bundle.getString("active"));
        user.setUsername(bundle.getString("username"));
        user.setRate(bundle.getFloat("rate"));

        Bundle bundleUserType = bundle.getBundle("userType");
        if (bundleUserType != null) {
            UserType userType = UserType.from(bundleUserType);
            user.setUserType(userType);
        }

        return user;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("name", name);
        bundle.putString("lastName", lastName);
        bundle.putString("userTypeId", userTypeId);
        bundle.putString("gender", gender);
        bundle.putString("dni", dni);
        bundle.putString("nationality", nationality);
        bundle.putString("birthDate", birthDate);
        bundle.putString("birthPlace", birthPlace);
        bundle.putString("insuranceType", insuranceType);
        bundle.putString("email", email);
        bundle.putString("pictureUrl", pictureUrl);
        bundle.putString("specialty", specialty);
        bundle.putString("active", active);
        bundle.putString("username", username);
        bundle.putFloat("rate", rate);

        bundle.putBundle("userType", userType.toBundle());

        return bundle;
    }

    public User() {
    }

    public User(String id, String name, String lastName, String userTypeId, String gender, String dni, String nationality, String birthDate, String birthPlace, String insuranceType, String email, float rate, String pictureUrl, String specialty, String active, String username) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.userTypeId = userTypeId;
        this.gender = gender;
        this.dni = dni;
        this.nationality = nationality;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.insuranceType = insuranceType;
        this.email = email;
        this.rate = rate;
        this.pictureUrl = pictureUrl;
        this.specialty = specialty;
        this.active = active;
        this.username = username;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(String userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getGender() {
        return gender;
    }

    public boolean isMale() {
        return gender.equals("Masculino");
    }

    public boolean isFemale() {
        return gender.equals("Femenino");
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    public int getDoctorPictureId() {
        return isMale() ? R.drawable.doctor_male : R.drawable.doctor_female;
    }

    public boolean isPediatrician(){
        return specialty.equals(SPECIALITY_PEDIATRICIAN);
    }

    public boolean isOncologist(){
        return specialty.equals(SPECIALITY_ONCOLOGIST);
    }
}
