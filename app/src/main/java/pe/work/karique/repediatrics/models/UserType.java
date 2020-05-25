package pe.work.karique.repediatrics.models;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

public class UserType {
    public static String TYPE_DOCTOR = "1";
    public static String TYPE_PARENT = "2";
    public static String TYPE_PATIENT = "4";
    public static String TYPE_ADMIN = "5";

    private String id;
    private String description;
    private String active;

    public static UserType from(Bundle bundle){
        UserType userType = new UserType();
        userType.setId(bundle.getString("id"));
        userType.setDescription(bundle.getString("description"));
        userType.setActive(bundle.getString("active"));

        return userType;
    }

    public static UserType from(JSONObject jsonObject){
        UserType userType = new UserType();
        try {
            userType.setId(jsonObject.getString("id"));
            userType.setDescription(jsonObject.getString("description"));
            userType.setActive(jsonObject.getString("active"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userType;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("description", description);
        bundle.putString("active", active);

        return bundle;
    }

    public UserType() {
    }

    public UserType(String id, String description, String active) {
        this.id = id;
        this.description = description;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
