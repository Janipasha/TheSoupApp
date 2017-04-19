package in.thesoup.thesoup.PreferencesFbAuth;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Jani on 15-04-2017.
 */

public class PrefUtil {

    private Activity activity;

    // Constructor
    public PrefUtil(Activity activity) {
        this.activity = activity;
    }

    public void saveAccessTokenPermissions(String token,String permissions) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fb_token", token);
        editor.putString("grantedScope", permissions);
        editor.apply(); // This line is IMPORTANT !!!
    }


    public String getToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("fb_token", null);
    }

    public String getPermissions() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("grantedScope", null);
    }

    public void clearToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply(); // This line is IMPORTANT !!!
    }

    public void saveFacebookUserInfo(String first_name,
                                     String last_name,
                                     String email,
                                     String gender,
                                     String profileURL,
                                     String id,
                                     String age_min,
                                     String age_max) {

        Log.d("pref_use", age_min);
        Log.d("pref_max",age_max);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("first_name", first_name);
        editor.putString("last_name", last_name);
        editor.putString("email_id", email);
        editor.putString("gender", gender);
        editor.putString("image_url", profileURL);
        editor.putString("fb_id", id);
        editor.putString("age_min", age_min);
        editor.putString("age_max", age_max);

        editor.apply(); // This line is IMPORTANT !!!
        Log.d("MyApp", "Shared Name : " + first_name + "\nLast Name : " +
                last_name + "\nEmail : " + email/*"\nGender : "+gender**/ + "\nProfile Pic : " +
                "" + profileURL + "\n age_min:" + age_min);

    }

    public void getFacebookUserInfo(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Log.d("MyApp", "Name : "+prefs.getString("fb_name",null)+"\nEmail : "+prefs.getString("fb_email",null));
    }

    public String getEmail(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("email_id",null);
    }

    public String getFirstname(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("first_name",null);
    }


    public String getLastname(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("last_name",null);
    }


    public String getGender(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("gender",null);
    }


    public String getId(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("fb_id",null);
    }

    public String getPictureUrl(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("image_url",null);}

    public String getAgeMin(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("age_min",null);
    }

    public String getAgeMax(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("age_max",null);
    }





}
