package pe.work.karique.repediatrics.session;

import android.content.Context;
import android.content.SharedPreferences;

import pe.work.karique.repediatrics.models.User;

public class SessionManager {
    private static final String PREFERENCE_NAME = "pe.work.karique.repediatrics";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String USER_LOGIN = "userLogin";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String LASTNAME = "lastName";
    private static final String USERNAME = "username";
    private static final String USERTYPEID = "userTypeId";
    private static final String EMAIL = "email";
    private static final String SPECIALITY = "speciality";

    private static SessionManager sessionManager;
    public static SessionManager getInstance(Context context){
        if (sessionManager == null){
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }

    private SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    private void setUserIsLogged(boolean userLogin) { editor.putBoolean(USER_LOGIN, userLogin).commit(); }
    public boolean getUserIsLogged() {
        return preferences.getBoolean(USER_LOGIN, false);
    }

    private void setid(String id) { editor.putString(ID, id).commit(); }
    public String getid() { return preferences.getString(ID, ""); }

    private void setname(String name) { editor.putString(NAME, name).commit(); }
    public String getname() { return preferences.getString(NAME, ""); }

    private void setusername(String username) { editor.putString(USERNAME, username).commit(); }
    public String getusername() { return preferences.getString(USERNAME, ""); }

    private void setlastname(String lastName) { editor.putString(LASTNAME, lastName).commit(); }
    public String getlastname() { return preferences.getString(LASTNAME, ""); }

    private void setusertypeid(String usertypeid) { editor.putString(USERTYPEID, usertypeid).commit(); }
    public String getusertypeid() { return preferences.getString(USERTYPEID, ""); }

    private void setemail(String email) { editor.putString(EMAIL, email).commit(); }
    public String getemail() { return preferences.getString(EMAIL, ""); }

    private void setspeciality(String speciality) { editor.putString(SPECIALITY, speciality).commit(); }
    public String getspeciality() { return preferences.getString(SPECIALITY, ""); }

    public String getDrName(){ return "Dr. " + preferences.getString(NAME, "") + " " + preferences.getString(LASTNAME, ""); }

    public void saveUserSession(User userLogin){
        setUserIsLogged(true);
        setid(userLogin.getId());
        setname(userLogin.getName());
        setusername(userLogin.getUsername());
        setlastname(userLogin.getLastName());
        setusertypeid(userLogin.getUserTypeId());
        setemail(userLogin.getEmail());
        setspeciality(userLogin.getSpecialty());
    }

    public void deleteUserSession(){
        setUserIsLogged(false);
        setid("");
        setname("");
        setusername("");
        setlastname("");
        setusertypeid("");
        setemail("");
        setspeciality("");
    }

    public boolean isDoctor(){
        String userTypeId = getusertypeid();
        String speciality = getspeciality();
        return userTypeId.equals("1") && speciality.equals("Pediatria");
    }
    public boolean isSpecialist(){
        String userTypeId = getusertypeid();
        String speciality = getspeciality();
        return userTypeId.equals("1") && speciality.equals("Oncologia");
    }
    public boolean isParent(){
        String userTypeId = getusertypeid();
        return userTypeId.equals("2");
    }
    public boolean isAdmin(){
        String userTypeId = getusertypeid();
        return userTypeId.equals("5");
    }
}
