package pe.work.karique.repediatrics.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.TextStyle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FuncionesFecha {

    public static String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    public static boolean isDateMonday(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
    }

    public static String formatDate(Date date){
        if (date == null) return "No definido";

        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(date);
    }

    public static String formatDateToHour(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        return String.format("%02d", hours)+":"+ String.format("%02d", minutes);
    }

    public static String formatLDateToText(LocalDate localDate){
        return localDate.getDayOfMonth() + " de " + localDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("es","ES"));
    }

    public static String formatDateToText(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("es","ES"));
        return cal.get(Calendar.DAY_OF_MONTH) + " de " + month;
    }

    public static String formatDateToTextForComment(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("es","ES"));
        return cal.get(Calendar.DAY_OF_MONTH) + " de " + month + " de " + cal.get(Calendar.YEAR);
    }

    public static String formatDateLDApi(LocalDate localDate){
        return localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();
    }

    public static String formatDateForAPI(Date date){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(date);
    }

    public static String formatDateForAPIWithHour(Date date){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(date);
    }

    public static String formatDateForFileWithHour(Date date){
        String pattern = "yyyy_MM_dd_HH_mm_ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(date);
    }

    public static Date getCurrentDate(){
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        return localCalendar.getTime();
    }

    public static boolean equalDates(Date date1, Date date2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static Date addDayToDate(Date currentDate){
        return operateDate(currentDate, 1);
    }

    public static Date subtractDayToDate(Date currentDate){
        return operateDate(currentDate, -1);
    }

    public static Date operateDate(Date currentDate, int dias){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DATE, dias);
        return cal.getTime();
    }

    public static int getCurrentHour(){
        Calendar rightNow = Calendar.getInstance();
        return rightNow.get(Calendar.HOUR_OF_DAY);
    }

    public static int getYearsFromNow(Date birthDate){
        int yearNow = getYearFromDate(getCurrentDate());
        int yearBirth = getYearFromDate(birthDate);
        return yearNow - yearBirth;
    }

    public static int getYearFromDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
    public static int getMonthFromDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }
    public static int getDayFromDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    public static int getDayOfYearFromDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    public static Date getDateFromString(String dateStr){
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LocalDate getLocalDateFromString(String dateStr){
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            int year = getYearFromDate(date);
            int month = getMonthFromDate(date) + 1;
            int day = getDayFromDate(date);
            LocalDate dt = LocalDate.of(year, month, day);
            return dt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateWithHourFromString(String dateStr){
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toOrdinal(int number) {
        String Unidad[] = {"", "Primer", "Segundo", "Tercero",
                "Cuarto", "Quinto", "Sexto", "Septimo", "Octavo", "Noveno"};
        String Decena[] = {"", "Decimo", "Vigesimo", "Trigesimo",
                "Cuadragesimo", "Quincuagesimo", "Sexagesimo", "Septuagesimo",
                "Octogesimo", "Nonagesimo"};
        String Centena[] = {"", "Centesimo", "Ducentesimo", "Tricentesimo",
                "Cuadringentesimo", "Quingentesimo", "Sexcentesimo",
                "Septingentesimo", "Octingentesimo", "Noningentesimo"};

        int u = number % 10;
        int d = (number / 10) % 10;
        int c = number / 100;

        return number >= 100 ? Centena[c] + " " + Decena[d] + " " + Unidad[u] : number >= 10 ? Decena[d] + " " + Unidad[u] : Unidad[number];
    }

    public static void hideKeyboardFromActivity(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyboardFromContext(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
