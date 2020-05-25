package pe.work.karique.repediatrics.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorComment {
    private String id;
    private String text;
    private String parentId;
    private String doctorId;
    private String dateTimeOfComment;
    private User user;

    public static List<DoctorComment> from(JSONArray jsonArray){
        List<DoctorComment> doctorComments = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                doctorComments.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return doctorComments;
    }

    public static DoctorComment from(JSONObject jsonObject){
        DoctorComment doctorComment = new DoctorComment();
        try {
            doctorComment.setId(jsonObject.getString("id"));
            doctorComment.setText(jsonObject.getString("text"));
            doctorComment.setParentId(jsonObject.getString("parentId"));
            doctorComment.setDoctorId(jsonObject.getString("doctorId"));
            doctorComment.setDateTimeOfComment(jsonObject.getString("dateTimeOfComment"));

            User user = User.from(jsonObject.getJSONObject("user"));
            doctorComment.setUser(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return doctorComment;
    }

    public DoctorComment() {
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDateTimeOfComment() {
        return dateTimeOfComment;
    }

    public void setDateTimeOfComment(String dateTimeOfComment) {
        this.dateTimeOfComment = dateTimeOfComment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
