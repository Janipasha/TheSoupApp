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
import java.util.List;
import java.util.Map;

import in.thesoup.thesoup.SoupContract;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.Adapters.StoryFeedAdapter;
import in.thesoup.thesoup.gsonConversion;

/**
 * Created by Jani on 06-04-2017.
 */

public class Networkutils {
    private Context mcontext;
    private List<StoryData> mStoryData;

    public Networkutils(Context context, List<StoryData> storyData) {
        this.mcontext = context;
        this.mStoryData = storyData;


    }


    public void getFeed(final StoryFeedAdapter feedAdapter) {

        MySingleton singleton = MySingleton.getInstance(mcontext);

        //RequestQueue queue = singleton.getRequestQueue();


   JsonObjectRequest jsObjRequest = new JsonObjectRequest
            (Request.Method.GET, SoupContract.URL, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("akunamatata", response.toString());



                   gsonConversion mpopulateUI = new gsonConversion();

                 mpopulateUI.fillUI(response,mStoryData,feedAdapter);


                }


                //mEarthquakedatajsonclass = red;

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // Auto-generated method stub

                }
            });

    singleton.addToRequestQueue(jsObjRequest);




}

    public void followrequest(String storyId){

        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, SoupContract.FOLLOWURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("followjsonresponse", response.toString());




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




    }

    public void loginvolleyRequest(){

        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, SoupContract.FOLLOWURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("followjsonresponse", response.toString());




                    }



                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<String, String>();
                headers.put("TOKEN_KEY","TokenValue");
                headers.put("USER_ID","userId");
                return headers;
            }
            //TODO: nee to implement params for post request.
        };



    }


}