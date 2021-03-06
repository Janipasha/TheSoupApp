package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilsFollowUnFollow;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilsLogin;
import in.thesoup.thesoup.PreferencesFbAuth.PrefUtil;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.SoupContract;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Jani on 11-04-2017.
 */

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackmanager;
    private LoginButton loginButton;
    PrefUtil prefUtil;
    private String StoryId,activityId;
    private HashMap<String, String> params;
    private Intent intent1;
    private Tracker mTracker;
    private AnalyticsApplication application;
    private SharedPreferences pref;
    private String StoryTitle = "";
    private Bundle extras;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.loginpage);

        application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarlogin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Semibolditalic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        extras = getIntent().getExtras();

        if(extras==null){
            application.sendEvent(mTracker, SoupContract.PAGE_VIEW, SoupContract.LOGINPAGE_VIEWED, SoupContract.LOGIN_PAGE);

        }
        else if(extras.containsKey("story_id")){
            StoryId = extras.getString("story_id", "");

            Log.d("StoryID analytics ",StoryId);

            application.sendEventCollection(mTracker,SoupContract.PAGE_VIEW,SoupContract.LOGINPAGE_VIEWED,SoupContract.LOGIN_PAGE,StoryId,StoryTitle);

        }

        prefUtil = new PrefUtil(LoginActivity.this);

        callbackmanager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        loginButton.registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {


                        String getScopes = loginResult.getAccessToken().getPermissions().toString();
                        Log.d("Scopes", getScopes);


                        System.out.println("Success");
                        Log.d("Acess Token", loginResult.getAccessToken().getToken().toString());

                        String accessToken = loginResult.getAccessToken().getToken();
                        prefUtil.saveAccessTokenPermissions(accessToken, getScopes);

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject jsonobject, GraphResponse response) {

                                        Bundle facebookData = getFacebookData(jsonobject);


                                        params = new HashMap<>();
                                        params.put("fb_token", prefUtil.getToken());
                                        params.put("fb_id", prefUtil.getId());
                                        params.put("grantedScopes", prefUtil.getPermissions());
                                        params.put("first_name", prefUtil.getFirstname());
                                        params.put("last_name", prefUtil.getLastname());
                                        params.put("email_id", prefUtil.getEmail());
                                        params.put("age_min", prefUtil.getAgeMin());
                                        params.put("gender", prefUtil.getGender());
                                        params.put("age_max", prefUtil.getAgeMax());
                                        //params.put("dob",);//send dob as null for future
                                        params.put("image_url", prefUtil.getPictureUrl());


                                        // Log.d("prefUtilemail",prefUtil.getEmail());

                                        for (String name : params.keySet()) {

                                            String key = name;


                                            String value = params.get(key);
                                            Log.d("param values", key + " " + value);


                                        }



                                        NetworkUtilsLogin loginRequest = new NetworkUtilsLogin(LoginActivity.this, params);


                                        intent1 = getIntent();


                                        if(extras == null) {

                                            loginRequest.loginvolleyRequestfromMain();

                                        } else if(extras.containsKey("story_id")&&extras.containsKey("activity")){

                                            //Bundle extras = getIntent().getExtras();
                                            StoryId = extras.getString("story_id", "");

                                            activityId = extras.getString("activity","");

                                            Log.d("activityId",activityId);

                                            loginRequest.loginvolleyRequest() ;
                                        }

                                        



                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");

                                        }
                                    }

                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,email,gender,age_range");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }


                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }

                });


    }

    public void Followpasson() {

        params.put("story_id",StoryId);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        params.put("auth_token",pref.getString("auth_token",null));

       NetworkUtilsFollowUnFollow followrequest = new NetworkUtilsFollowUnFollow(this,params);
        followrequest.followRequest(1);//passed for the sake of it , no use
        //TODO: 1) if user already follows this there will be an error which should be handled


    }

    @Override
    protected void onStart() {
        super.onStart();

        application.sendScreenName(mTracker, SoupContract.LOGINPAGE_VIEWED);




    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void startActivityMD(String followstatus) {

        if (intent1.hasExtra("activity")) {
            if (activityId.equals("0")) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            } else if (activityId.equals("1")) {
                Intent intent = new Intent(LoginActivity.this, DetailsActivity.class);
                intent.putExtra("story_id", StoryId);
                intent.putExtra("followstatus", followstatus);
                finish();
                startActivity(intent);
            }
        }else {
                Intent intent = new Intent(LoginActivity.this, feedActivity.class);
                intent.putExtra("feedIdfromLogin", "1");
                finish();
                startActivity(intent);
            }


    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");
            URL profile_pic;
            try {
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));

            Log.d("Bundle", bundle.toString());

            String age_min = object.getJSONObject("age_range").getString("min");
            Log.d("age min1 ", age_min);

            JSONObject agerange = object.getJSONObject("age_range");
            String age_max = null;
            if (agerange.has("max")) {
                age_max = object.getJSONObject("age_range").getString("max");
            }

            //Log.d("age max",age_max);


            prefUtil.saveFacebookUserInfo(object.getString("first_name"),
                    object.getString("last_name"),
                    object.getString("email"),
                    object.getString("gender"),
                    profile_pic.toString(),
                    object.getString("id"),
                    age_min, age_max);


            //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            //Log.d("Shared Preference ",pref.getString("email","") + " "+ pref.getString("first_name","")+" "+pref.getString("age_min"," "));

        } catch (Exception e) {
            Log.d("BUNDLE Exception : ", e.toString());
        }

        return bundle;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}

