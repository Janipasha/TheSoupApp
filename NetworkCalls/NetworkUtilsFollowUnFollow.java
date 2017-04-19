package in.thesoup.thesoup.NetworkCalls;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.thesoup.thesoup.SoupContract;

/**
 * Created by Jani on 17-04-2017.
 */

public class NetworkUtilsFollowUnFollow {

    private Context mcontext;
    private String StoryId;
    private Map<String ,String> params;
    private final String BOUNDARY = "whatshitisthis";

    public NetworkUtilsFollowUnFollow(Context context, String Storyid,Map<String,String> params) {
        this.mcontext = context;
        this.StoryId = Storyid;
        this.params = params;

    }

    private String createPostBody(Map<String, String> params) {
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

    public int followRequest() {

        int result= 0;

        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.FOLLOWURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("followjsonresponse", response.toString());

                        //check story Id exist
                        //TODO: if story ID exists reuslt =1 else result =0


                    }


                    //mEarthquakedatajsonclass = red;

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Auto-generated method stub

                    }
                })  {
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
            //nee to implement params for post request.
        };

        return result;


    }


    public int unFollowRequest(String storyId){

        int result =1;

        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.UNFOLLOWURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("followjsonresponse", response.toString());

                        //TODO return reuslt




                    }


                    //mEarthquakedatajsonclass = red;

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Auto-generated method stub

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<String, String>();
                headers.put("TOKEN_KEY","TokenValue");
                headers.put("USER_ID","userId");
                return headers;
            }
            //nee to implement params for post request.
        };


     return result;

    }



}
