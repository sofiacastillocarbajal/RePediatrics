package pe.work.karique.repediatrics.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketState {
    private String id;
    private String ticketId;
    private String state;
    private String dateOfUpdate;
    private String userUpdateId;
    private User userUpdate;

    public static List<TicketState> from(JSONArray jsonArray){
        List<TicketState> ticketStates = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                ticketStates.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return ticketStates;
    }

    public static TicketState from(JSONObject jsonObject){
        TicketState ticketState = new TicketState();
        try {
            ticketState.setId(jsonObject.getString("id"));
            ticketState.setTicketId(jsonObject.getString("ticketId"));
            ticketState.setState(jsonObject.getString("state"));
            ticketState.setDateOfUpdate(jsonObject.getString("dateOfUpdate"));
            ticketState.setUserUpdateId(jsonObject.getString("userUpdateId"));

            User userUpdate = User.from(jsonObject.getJSONObject("user"));
            ticketState.setUserUpdate(userUpdate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ticketState;
    }

    public TicketState() {
    }

    public TicketState(String id, String ticketId, String state, String dateOfUpdate, String userUpdateId, User userUpdate) {
        this.id = id;
        this.ticketId = ticketId;
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

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
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
