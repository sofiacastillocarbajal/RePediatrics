package pe.work.karique.repediatrics.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    private String id;
    private String questionText;
    private String position;
    private String weigth;
    private String questionType;

    public static List<Question> from(JSONArray jsonArray){
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                questions.add(from(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return questions;
    }

    public static Question from(JSONObject jsonObject){
        Question question = new Question();
        try {
            question.setId(jsonObject.getString("id"));
            question.setQuestionText(jsonObject.getString("questionText"));
            question.setPosition(jsonObject.getString("position"));
            question.setWeigth(jsonObject.getString("weigth"));
            question.setQuestionType(jsonObject.getString("questionType"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return question;
    }

    public static Question from(Bundle bundle){
        Question question = new Question();
        question.setId(bundle.getString("id"));
        question.setQuestionText(bundle.getString("questionText"));
        question.setPosition(bundle.getString("position"));
        question.setWeigth(bundle.getString("weigth"));
        question.setQuestionType(bundle.getString("questionType"));

        return question;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("questionText", questionText);
        bundle.putString("position", position);
        bundle.putString("weigth", weigth);
        bundle.putString("questionType", questionType);

        return bundle;
    }

    public Question() {
    }

    public Question(String id, String questionText, String position, String weigth, String questionType) {
        this.id = id;
        this.questionText = questionText;
        this.position = position;
        this.weigth = weigth;
        this.questionType = questionType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWeigth() {
        return weigth;
    }

    public void setWeigth(String weigth) {
        this.weigth = weigth;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
}
