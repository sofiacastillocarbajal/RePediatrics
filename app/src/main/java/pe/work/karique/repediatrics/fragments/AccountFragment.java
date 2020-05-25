package pe.work.karique.repediatrics.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.libizo.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

import pe.work.karique.repediatrics.R;
import pe.work.karique.repediatrics.activities.DoctorDetailsActivity;
import pe.work.karique.repediatrics.activities.LoginActivity;
import pe.work.karique.repediatrics.network.RepediatricsApi;
import pe.work.karique.repediatrics.session.SessionManager;
import pe.work.karique.repediatrics.util.FuncionesFecha;

public class AccountFragment extends Fragment {

    CardView logOutCardView;

    CustomEditText usernameCustomEditText;
    CustomEditText passwordCustomEditText;
    CustomEditText nameCustomEditText;
    CustomEditText lastNameCustomEditText;
    CustomEditText emailCustomEditText;

    private SessionManager sessionManager;

    public AccountFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = SessionManager.getInstance(getContext());
        logOutCardView = view.findViewById(R.id.logOutCardView);
        setlogOutColor();
        logOutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogOutDialog();
            }
        });

        usernameCustomEditText = view.findViewById(R.id.usernameCustomEditText);
        passwordCustomEditText = view.findViewById(R.id.passwordCustomEditText);
        passwordCustomEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInsertNewPasswordDialog();
            }
        });
        nameCustomEditText = view.findViewById(R.id.nameCustomEditText);
        lastNameCustomEditText = view.findViewById(R.id.lastNameCustomEditText);
        emailCustomEditText = view.findViewById(R.id.emailCustomEditText);

        usernameCustomEditText.setText(sessionManager.getusername());
        nameCustomEditText.setText(sessionManager.getname());
        lastNameCustomEditText.setText(sessionManager.getlastname());
        emailCustomEditText.setText(sessionManager.getemail());
    }
    private void showLogOutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Seguro que desea cerrar sesión?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void logOut(){
        sessionManager.deleteUserSession();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
    private void setlogOutColor() {
        Context context = getContext();
        if (context != null) {
            if (sessionManager.isParent()){
                logOutCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            }
            else if (sessionManager.isDoctor()){
                logOutCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
            }
            else if (sessionManager.isSpecialist()){
                logOutCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
            }
        }
    }
    private void showInsertNewPasswordDialog(){
        final EditText taskEditText = new EditText(getContext());
        taskEditText.setFocusableInTouchMode(true);
        taskEditText.requestFocus();
        taskEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Ingrese su nueva contraseña")
                .setMessage("¿Cuál es su nueva contraseña?")
                .setView(taskEditText)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPassword = String.valueOf(taskEditText.getText());
                        validatePassword(newPassword);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
    private void validatePassword(String newPassword){
        if (passwordCustomEditText.getText().toString().length() <= 7 ||
                !checkPassword(passwordCustomEditText.getText().toString())) {
            Toast.makeText(getContext(), "Ingrese una contraseña valida (Mas de 7 caracteres, Una mayuscula, una minuscula y un numero)", Toast.LENGTH_LONG).show();
            return;
        }

        resetPassword(newPassword);
    }
    private void resetPassword(String newPassword){
        //progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "Procesando...", Toast.LENGTH_SHORT).show();

        JSONObject commentJsonObject = new JSONObject();
        try {
            commentJsonObject.put("userId", sessionManager.getid());
            commentJsonObject.put("newPassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.put(RepediatricsApi.RESET_PASSWORD_URL)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(commentJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "Contraseña actualizada correctamente", Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.GONE);
                        //doctorCommentsFragment.getComments();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getContext(), "Error al actualizar Contraseña", Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.GONE);
                    }
                });
    }
    private boolean checkPassword(String password) {
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isDigit(ch))
                numberFlag = true;
            else if (Character.isUpperCase(ch))
                capitalFlag = true;
            else if (Character.isLowerCase(ch))
                lowerCaseFlag = true;
        }
        return numberFlag && capitalFlag && lowerCaseFlag;
    }
}
