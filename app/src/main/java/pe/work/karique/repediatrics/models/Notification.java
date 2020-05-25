package pe.work.karique.repediatrics.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Notification {
    private String id;
    private String text;
    private String userId;
    private String dateOfSending;

    public static List<Notification> from(JSONArray jsonArray){
        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                notifications.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return notifications;
    }

    public static Notification from(JSONObject jsonObject){
        Notification notification = new Notification();
        try {
            notification.setId(jsonObject.getString("id"));
            notification.setText(jsonObject.getString("text"));
            notification.setUserId(jsonObject.getString("userId"));
            notification.setDateOfSending(jsonObject.getString("dateOfSending"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notification;
    }

    public Notification() {
    }

    public Notification(String id, String text, String userId, String dateOfSending) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.dateOfSending = dateOfSending;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDateOfSending() {
        return dateOfSending;
    }

    public void setDateOfSending(String dateOfSending) {
        this.dateOfSending = dateOfSending;
    }
}
