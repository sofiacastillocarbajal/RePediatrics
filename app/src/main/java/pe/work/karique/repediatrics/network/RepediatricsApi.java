package pe.work.karique.repediatrics.network;

public class RepediatricsApi {
    public static String BASE_URL = "https://repediatrics.azurewebsites.net/";  //azure

    public static String LOGIN_URL = BASE_URL + "api/login";
    public static String USERS_URL = BASE_URL + "api/users";
    public static String DOCTORS_URL = BASE_URL + "api/doctors";
    public static String SPECIALISTS_URL = BASE_URL + "api/specialists";
    public static String PARENTS_URL = BASE_URL + "api/parents";
    public static String QUESTIONS_URL = BASE_URL + "api/questions";
    public static String HOSPITALS_URL = BASE_URL + "api/hospitals";
    public static String QUESTIONARIES_RESPONSES_URL = BASE_URL + "api/Questionaries/responses";
    public static String PARENTS_PATIENT_URL = BASE_URL + "api/parents/patients";
    public static String TICKETS_URL = BASE_URL + "api/tickets";
    public static String LAST_TICKET_NUMBER_URL = TICKETS_URL + "/lastTicketNumber";
    public static String UPDATE_TICKET_STATE_URL = TICKETS_URL + "/updateState";
    public static String MEDICAL_APPOINTMENTS_URL = BASE_URL + "api/MedicalAppointments";
    public static String TIMETABLEBYDOCTOR_URL = BASE_URL + "api/TimeTableByDoctors";
    public static String COMMENTSBYDOCTOR_URL = BASE_URL + "api/CommentByDoctors";
    public static String SEND_RESET_PASSWORD_EMAIL_URL = BASE_URL + "api/users/sendResetPasswordEmail";
    public static String TOKEN_FCM_URL = BASE_URL + "api/users/tokenFCM";
    public static String RESET_PASSWORD_URL = BASE_URL + "api/users/resetPassword";

    public static String MEDICAL_APPOINTMENTS_BY_PARENT(String parentId){
        return PARENTS_URL + "/" + parentId + "/" + "MedicalAppointment";
    }

    public static String MEDICAL_APPOINTMENTS_BY_DOCTORS(String parentId){
        return DOCTORS_URL + "/" + parentId + "/" + "MedicalAppointment";
    }

    public static String TICKETS_BY_PARENT(String parentId){
        return PARENTS_URL + "/" + parentId + "/" + "tickets";
    }

    public static String TICKETS_BY_PARENT_FOR_CHOOSE(String parentId){
        return PARENTS_URL + "/" + parentId + "/" + "TicketsForChoose";
    }

    public static String TICKETS_BY_DOCTOR(String doctorId){
        return DOCTORS_URL + "/" + doctorId + "/" + "tickets";
    }

    public static String TICKETS_BY_SPECIALISTS(String specialistId){
        return SPECIALISTS_URL + "/" + specialistId + "/" + "tickets";
    }

    public static String PATIENTS_BY_PARENT(String parentId){
        return PARENTS_URL + "/" + parentId + "/" + "Patients";
    }

    public static String PATIENT_BY_USERNAME(String username){
        return USERS_URL + "/" + username + "/" + "PatientExists";
    }

    public static String SPECIALISTS_BY_HOSPITAL(String hospitalId){
        return HOSPITALS_URL + "/" + hospitalId + "/" + "specialist";
    }

    public static String TIMETABLE_BY_DOCTOR_BY_HOSPITAL_BY_DATE(String doctorId, String hospitalId, String date){
        return DOCTORS_URL + "/" + doctorId + "/" + "timetable" + "/" + "byHospital" + "/" + hospitalId + "/" + "byDate" + "/" + date;
    }

    public static String TIMETABLE_BY_DOCTOR_BY_HOSPITAL_BY_NEXTAVAILABLEDATE(String doctorId, String hospitalId, String date){
        return DOCTORS_URL + "/" + doctorId + "/" + "timetable" + "/" + "byHospital" + "/" + hospitalId + "/" + "nextAvailableDate" + "/" + date;
    }

    public static String HOSPITALS_BY_DOCTOR(String doctorId){
        return DOCTORS_URL + "/" + doctorId + "/" + "hospitals";
    }

    public static String COMMENTS_BY_DOCTOR(String doctorId){
        return DOCTORS_URL + "/" + doctorId + "/" + "comments";
    }

    public static String MEDICAL_APPOINTMENTS_BY_RANGEDATE(String startDate, String endDate){
        return MEDICAL_APPOINTMENTS_URL + "/" + "startDate" + "/" + startDate + "/" + "endDate" + "/" + endDate;
    }

    public static String NOTIFICATIONS_BY_USERS(String userId){
        return USERS_URL + "/" + userId + "/" + "notifications";
    }

    public static String STATES_BY_TICKET(String ticketId){
        return TICKETS_URL + "/" + ticketId + "/" + "states";
    }

    public static String STATES_BY_MEDICAL_APPOINTMENT(String medicalAppointmentId){
        return MEDICAL_APPOINTMENTS_URL + "/" + medicalAppointmentId + "/" + "states";
    }

    public static String TICKET_IS_USED(String ticketId){
        return TICKETS_URL + "/" + ticketId + "/" + "isused";
    }
}
