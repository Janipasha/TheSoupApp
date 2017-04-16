package in.thesoup.thesoup.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.thesoup.thesoup.PreferencesFbAuth.PrefUtil;
import in.thesoup.thesoup.R;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Share;

/**
 * Created by Jani on 11-04-2017.
 */

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackmanager;
    private LoginButton loginButton;
    PrefUtil prefUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.loginpage);
        prefUtil = new PrefUtil(LoginActivity.this);

        callbackmanager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        loginButton.registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {


                        System.out.println("Success");
                        Log.d("Acess Token",loginResult.getAccessToken().getToken().toString());

                        GraphRequest request= GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject jsonobject, GraphResponse response) {

                                        Bundle facebookData = getFacebookData(jsonobject);

                                        try {
                                            String email = jsonobject.getString("email");
                                            Log.d("Email",email);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("graphjson", response.toString());
                                        Log.d("json", jsonobject.toString());

                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            /*try {

                                         //       String jsonresult = String.valueOf(json);
                                           //     System.out.println("JSON Result" + jsonresult);


                                                String str_email = json.getString("email");
                                               // String str_profileURL = "http://graph.facebook.com/"+userID+"/picture?type=large"
                                                String str_id = json.getString("id");
                                                 String str_firstname = json.getString("first_name");
                                                String str_lastname = json.getString("last_name");
                                                String str_picture = json.getString("picture");
                                                Log.d("str_picture",str_picture);
                                                PrefUtil User = new PrefUtil(LoginActivity.this);
                                                User.saveFacebookUserInfo(str_firstname,str_lastname,str_email,str_picture);
                                                SharedPreferences Pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

                                                String Value = Pref.getString("fb_first_name","");
                                                Log.d("Value",Value);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }*/
                                        }
                                    }

                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,email,gender");
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


            prefUtil.saveFacebookUserInfo(object.getString("first_name"),
                    object.getString("last_name"),object.getString("email"),
                    object.getString("gender"), profile_pic.toString());

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            Log.d("Shared Preference ",pref.getString("email","") + " "+ pref.getString("first_name",""));

        } catch (Exception e) {
            Log.d("BUNDLE Exception : ",e.toString());
        }

        return bundle;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }


}

 /* loginButton.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("User ID: ",loginResult.getAccessToken().getUserId());
                Log.d("Auth Token: " ,loginResult.getAccessToken().getToken());
                Log.d("loginResult:",loginResult.toString());






            }

            @Override
            public void onCancel() {


            }

            @Override
            public void onError(FacebookException error) {



            }
        });
    }*/
