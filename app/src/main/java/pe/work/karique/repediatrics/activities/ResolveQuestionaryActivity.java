package pe.work.karique.repediatrics.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.models.Question;
import pe.work.karique.repediatrics.models.QuestionResponse;
import pe.work.karique.repediatrics.models.Questionary;
import pe.work.karique.repediatrics.models.Ticket;
import pe.work.karique.repediatrics.models.User;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ResolveQuestionaryActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 7;

    private SessionManager sessionManager;
    private ImageButton backImageButton;
    private TextView titleTextView;
    private ProgressBar progressBar;

    private TextView questionDescriptionTextView;
    private CardView yesCardView;
    private CardView noCardView;
    private TextView questionNumberTextView;
    private CardView questionNumberCardView;

    private List<Question> questions;
    private List<QuestionResponse> questionResponses;
    private int currentQuestion = 0;

    private ConstraintLayout resultsConstraintLayout;
    private TextView percentageValueTextView;
    private TextView resultDescriptionValueTextView;
    private CardView generateTicketCardView;
    private Button obervacionesButton;

    private String percentage = "";
    private String textResult = "bajo";
    private String commentToPatient = "";

    private User patient;
    private String parentId;
    private Questionary questionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolve_questionary);
        hide();

        patient = User.from(getIntent().getBundleExtra("patient"));
        parentId = getIntent().getStringExtra("parentId");

        sessionManager = SessionManager.getInstance(this);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleTextView = findViewById(R.id.titleTextView);
        progressBar = findViewById(R.id.progressBar);

        questionDescriptionTextView = findViewById(R.id.questionDescriptionTextView);
        yesCardView = findViewById(R.id.yesCardView);
        yesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseQuestion("true");
            }
        });
        noCardView = findViewById(R.id.noCardView);
        noCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseQuestion("false");
            }
        });
        questionNumberTextView = findViewById(R.id.questionNumberTextView);
        questionNumberCardView = findViewById(R.id.questionNumberCardView);

        resultsConstraintLayout = findViewById(R.id.resultsConstraintLayout);
        percentageValueTextView = findViewById(R.id.percentageValueTextView);
        resultDescriptionValueTextView = findViewById(R.id.resultDescriptionValueTextView);
        generateTicketCardView = findViewById(R.id.generateTicketCardView);
        generateTicketCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddTicketActivity();
            }
        });
        obervacionesButton = findViewById(R.id.obervacionesButton);
        obervacionesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddTicketObservationActivity();
            }
        });

        questions = new ArrayList<>();
        questionResponses = new ArrayList<>();
        getQuestions();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void startAddTicketActivity(){
        Intent intent = new Intent(this, AddTicketActivity.class);
        intent.putExtra("patient", patient.toBundle());
        intent.putExtra("parentId", parentId);
        intent.putExtra("commentToPatient", commentToPatient);
        intent.putExtra("percentage", percentage);
        intent.putExtra("textResult", textResult);
        intent.putExtra("questionary", questionary.toBundle());
        startActivityForResult(intent, AddTicketActivity.REQUEST_CODE);
    }
    private void startAddTicketObservationActivity(){
        Intent intent = new Intent(this, AddTicketObservationActivity.class);
        intent.putExtra("patient", patient.toBundle());
        intent.putExtra("patientName", patient.getFullName());
        intent.putExtra("questionaryResult", percentage + "% (" + textResult + ")");
        startActivityForResult(intent, AddTicketObservationActivity.REQUEST_CODE);
    }
    public void getQuestions(){
        progressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.get(RepediatricsApi.QUESTIONS_URL)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);

                        questions.clear();
                        questions.addAll(Question.from(response));
                        setQuestionsData();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ResolveQuestionaryActivity.this, "Error al obtener las preguntas", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void setQuestionsData(){
        setControlsVisible();
        questionNumberTextView.setText(String.format("%d/%d", currentQuestion + 1, questions.size()));
        if (questions.size() > currentQuestion) {
            questionDescriptionTextView.setText(questions.get(currentQuestion).getQuestionText());
        }
    }
    private void responseQuestion(String response){
        setControlsGone();
        QuestionResponse questionResponse = new QuestionResponse();
        questionResponse.setQuestionId(questions.get(currentQuestion).getId());
        questionResponse.setResponse(response);
        questionResponse.setQuestionaryId("1");
        questionResponse.setQuestion(questions.get(currentQuestion));

        questionResponses.add(questionResponse);
        currentQuestion++;

        //si termina de contestar todas las preguntas
        if (currentQuestion >= questions.size()){
            setResults();
        }
        else {
            setQuestionsData();
        }
    }
    private void setControlsVisible(){
        setControlsVisibility(View.VISIBLE);
    }
    private void setControlsGone(){
        setControlsVisibility(View.GONE);
    }
    private void setControlsVisibility(int visibility){
        questionDescriptionTextView.setVisibility(visibility);
        yesCardView.setVisibility(visibility);
        noCardView.setVisibility(visibility);
    }
    private void setResults(){
        setControlsGone();
        questionNumberCardView.setVisibility(View.GONE);
        resultsConstraintLayout.setVisibility(View.VISIBLE);

        //porcentajes
        int weight = 0;

        for (int i = 0; i < questionResponses.size(); i++) {
            QuestionResponse questionResponse = questionResponses.get(i);
            if (questionResponse.getResponseBool()) {
                weight += Integer.valueOf(questionResponse.getQuestion().getWeigth());
            }
        }
        percentage = weight < 10 ? "30" : weight < 20 ? "50" : "70";
        textResult = weight < 10 ? "baja" : weight < 20 ? "media" : "alta";

        //texto
        percentageValueTextView.setText("Porcentage: " + percentage + "%");
        resultDescriptionValueTextView.setText("En base al cuestionario se ha obtenido que el paciente tiene un " + textResult + " porcentaje de tener cáncer infantil.");

        questionary = new Questionary();
        questionary.setIsAnswered("true");
        questionary.setParentId(parentId);
        questionary.setPatientId(patient.getId());
        questionary.setQuestionResponses(questionResponses);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Observacion a paciente agregado correctamente
        if (requestCode == AddTicketObservationActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null)
                    commentToPatient = data.getStringExtra("commentToPatient");
            }
        }
        //Ticket agregado correctamente
        if (requestCode == AddTicketActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK);
                finish();
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        }
    }
}
