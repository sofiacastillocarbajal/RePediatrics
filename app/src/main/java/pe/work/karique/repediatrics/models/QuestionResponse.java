package pe.work.karique.repediatrics.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionResponse implements Serializable {
    private String id;
    private String questionId;
    private String response;
    private String questionaryId;
    private Question question;

    public static List<QuestionResponse> from(JSONArray jsonArray){
        List<QuestionResponse> questionResponses = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                questionResponses.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return questionResponses;
    }

    public static QuestionResponse from(JSONObject jsonObject){
        QuestionResponse questionResponse = new QuestionResponse();
        try {
            questionResponse.setId(jsonObject.getString("id"));
            questionResponse.setQuestionId(jsonObject.getString("questionId"));
            questionResponse.setResponse(jsonObject.getString("response"));
            questionResponse.setQuestionaryId(jsonObject.getString("questionaryId"));

            Question question = Question.from(jsonObject.getJSONObject("question"));
            questionResponse.setQuestion(question);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return questionResponse;
    }

    public static QuestionResponse from(Bundle bundle){
        QuestionResponse questionResponse = new QuestionResponse();
        questionResponse.setId(bundle.getString("id"));
        questionResponse.setQuestionId(bundle.getString("questionId"));
        questionResponse.setResponse(bundle.getString("response"));
        questionResponse.setQuestionaryId(bundle.getString("questionaryId"));

        Bundle bundleQuestion = bundle.getBundle("question");
        if (bundleQuestion != null) {
            Question question = Question.from(bundleQuestion);
            questionResponse.setQuestion(question);
        }

        return questionResponse;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("questionId", questionId);
        bundle.putString("response", response);
        bundle.putString("questionaryId", questionaryId);

        bundle.putBundle("question", question.toBundle());
        return bundle;
    }

    public QuestionResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getResponse() {
        return response;
    }

    public boolean getResponseBool() {
        return response.equals("true");
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getQuestionaryId() {
        return questionaryId;
    }

    public void setQuestionaryId(String questionaryId) {
        this.questionaryId = questionaryId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
