package in.thesoup.thesoup.NetworkCalls;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.thesoup.thesoup.Activities.LoginActivity;
import in.thesoup.thesoup.PreferencesFbAuth.PrefUtil;
import in.thesoup.thesoup.SoupContract;

/**
 * Created by Jani on 18-04-2017.
 */

public class NetworkUtilsLogin {

    private Context mcontext;
    private HashMap<String,String> params;
    private final String BOUNDARY = "whagtstheaek";


    public NetworkUtilsLogin(Context context,HashMap<String,String> params){
        this.mcontext =  context;
        this.params = params;

    }

    private String createPostBody(HashMap<String, String> params) {
        StringBuilder sbPost = new StringBuilder();
        for (String key : params.keySet()) {
            if (params.get(key) != null) {
                sbPost.append("\r\n" + "--" + BOUNDARY + "\r\n");
                sbPost.append("Content-Disposition: form-data; name=\"" + key + "\"" + "\r\n\r\n");
                sbPost.append(params.get(key));
            }
        }

        return sbPost.toString();
    }

    public void loginvolleyRequest(){



        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.LOGINURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loginjsonresponse", response.toString());

                        String outh_token= "" ;




                        try {
                            outh_token = response.getJSONObject("data").getString("token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        LoginActivity Object = (LoginActivity) mcontext;
                        Object.Demo(outh_token);

                      SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mcontext);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("auth_token",outh_token);
                        editor.apply();


                        Log.d("prefvalue 1", pref.getString("auth_token",null)+"-----");









                    }



                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headerParam = new HashMap<>();
                headerParam.put("Content-Type","multipart/form-data;boundary="+BOUNDARY+";");
                return headerParam;
            }
            @Override
            public byte[] getBody() {
                String postBody = createPostBody(params);

                return postBody.getBytes();
            }


        };

        singleton.addToRequestQueue(jsObjRequest);



    }
}
