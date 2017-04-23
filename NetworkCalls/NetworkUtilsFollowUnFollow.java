package in.thesoup.thesoup.NetworkCalls;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.thesoup.thesoup.Activities.DetailsActivity;
import in.thesoup.thesoup.Activities.LoginActivity;
import in.thesoup.thesoup.Activities.MainActivity;
import in.thesoup.thesoup.Activities.feedActivity;
import in.thesoup.thesoup.SoupContract;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Jani on 17-04-2017.
 */

public class NetworkUtilsFollowUnFollow {

    private Context mcontext;
    private Map<String, String> params;
    private final String BOUNDARY = "whatshitisthis";

    public NetworkUtilsFollowUnFollow(Context context, Map<String, String> params) {
        this.mcontext = context;
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

    public void followRequest(final int position) {


        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.FOLLOWURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("followjsonresponse", response.toString());

                        //check story Id exist
                        //TODO: if story ID exists reuslt =1 else result =0

                        String Story_id = "";

                        if (mcontext instanceof MainActivity) {

                            MainActivity activity = (MainActivity) mcontext;

                            try {
                                Story_id = response.getJSONObject("data").getString("story_id");
                                activity.demo1(position, "1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                activity.demo1(position, "0");
                            }

                        }

                        if (mcontext instanceof DetailsActivity) {

                            DetailsActivity activity = (DetailsActivity) mcontext;

                            try {
                                Story_id = response.getJSONObject("data").getString("story_id");
                                activity.DetailsActivitydemo("1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                activity.DetailsActivitydemo("0");
                            }

                        }

                        if(mcontext instanceof LoginActivity){
                            LoginActivity activity = (LoginActivity)mcontext;

                            try {
                                Story_id = response.getJSONObject("data").getString("story_id");
                                activity.startActivityMD("1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                activity.startActivityMD("0");
                            }

                        }

                        if (mcontext instanceof feedActivity) {

                            feedActivity activity = (feedActivity) mcontext;

                            try {
                                Story_id = response.getJSONObject("data").getString("story_id");
                                activity.demo1(position, "1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                activity.demo1(position, "0");
                            }

                        }


                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headerParam = new HashMap<>();
                headerParam.put("Content-Type", "multipart/form-data;boundary=" + BOUNDARY + ";");
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


    public void unFollowRequest(final int position) {

        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.UNFOLLOWURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("unfollowjsonresponse", response.toString());

                        //TODO return reuslt

                        String Story_id = "";

                        if (mcontext instanceof MainActivity) {

                            MainActivity activity = (MainActivity) mcontext;

                            try {
                                Story_id = response.getJSONObject("data").getString("story_id");
                                activity.demo1(position, "0");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                activity.demo1(position, "1");
                            }
                        } else if (mcontext instanceof DetailsActivity) {
                            DetailsActivity activity = (DetailsActivity) mcontext;

                            try {
                                Story_id = response.getJSONObject("data").getString("story_id");
                                activity.DetailsActivitydemo("0");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                activity.DetailsActivitydemo("1");
                            }


                        } else if (mcontext instanceof feedActivity) {

                            feedActivity activity = (feedActivity) mcontext;

                            try {
                                Story_id = response.getJSONObject("data").getString("story_id");
                                activity.demo1(position, "0");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                activity.demo1(position, "1");

                            }

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Auto-generated method stub

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headerParam = new HashMap<>();
                headerParam.put("Content-Type", "multipart/form-data;boundary=" + BOUNDARY + ";");
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
