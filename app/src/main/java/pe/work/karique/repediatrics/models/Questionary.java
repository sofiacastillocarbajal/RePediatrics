package pe.work.karique.repediatrics.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Questionary implements Serializable {
    private String id;
    private String percentage;
    private String isAnswered;
    private String parentId;
    private String patientId;
    private List<QuestionResponse> questionResponses;

    public static List<Questionary> from(JSONArray jsonArray){
        List<Questionary> questionaries = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                questionaries.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return questionaries;
    }

    public static Questionary from(JSONObject jsonObject){
        Questionary questionary = new Questionary();
        try {
            questionary.setId(jsonObject.getString("id"));
            questionary.setPercentage(jsonObject.getString("percentage"));
            questionary.setIsAnswered(jsonObject.getString("isAnswered"));
            questionary.setParentId(jsonObject.getString("parentId"));
            questionary.setPatientId(jsonObject.getString("patientId"));

            JSONArray questionResponsesJsonArray = jsonObject.getJSONArray("questionResponses");
            List<QuestionResponse> questionResponses = QuestionResponse.from(questionResponsesJsonArray);

            questionary.setQuestionResponses(questionResponses);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return questionary;
    }

    public static Questionary from(Bundle bundle){
        Questionary questionary = new Questionary();
        questionary.setId(bundle.getString("id"));
        questionary.setPercentage(bundle.getString("percentage"));
        questionary.setIsAnswered(bundle.getString("isAnswered"));
        questionary.setParentId(bundle.getString("parentId"));
        questionary.setPatientId(bundle.getString("patientId"));

        List<QuestionResponse> questionResponses = (List<QuestionResponse>)bundle.getSerializable("questionResponses");

        questionary.setQuestionResponses(questionResponses);
        return questionary;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("percentage", percentage);
        bundle.putString("isAnswered", isAnswered);
        bundle.putString("parentId", parentId);
        bundle.putString("patientId", patientId);

        bundle.putSerializable("questionResponses", (Serializable) questionResponses);
        return bundle;
    }

    public JSONObject toPostJsonObject(){
        JSONObject postJsonObject = new JSONObject();

        JSONObject questionaryJsonObject = new JSONObject();
        try {
            questionaryJsonObject.put("isAnswered", isAnswered);
            questionaryJsonObject.put("parentId", parentId);
            questionaryJsonObject.put("patientId", patientId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray questionariesJsonArray = new JSONArray();
        for (int i = 0; i < questionResponses.size(); i++) {
            QuestionResponse questionResponse = questionResponses.get(i);

            JSONObject questionResponseJsonObject = new JSONObject();
            try {
                questionResponseJsonObject.put("questionId", questionResponse.getQuestionId());
                questionResponseJsonObject.put("response", questionResponse.getResponse());
                questionResponseJsonObject.put("questionaryId", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            questionariesJsonArray.put(questionResponseJsonObject);
        }

        try {
            postJsonObject.put("questionary", questionaryJsonObject);
            postJsonObject.put("responses", questionariesJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postJsonObject;
    }

    public Questionary() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPercentage() {
        return percentage;
    }

    public String getPercentageResult() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(String isAnswered) {
        this.isAnswered = isAnswered;
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

    public List<QuestionResponse> getQuestionResponses() {
        return questionResponses;
    }

    public void setQuestionResponses(List<QuestionResponse> questionResponses) {
        this.questionResponses = questionResponses;
    }
}
