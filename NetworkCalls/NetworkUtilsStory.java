package in.thesoup.thesoup.NetworkCalls;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.List;

import in.thesoup.thesoup.Adapters.SingleStoryAdapter;
import in.thesoup.thesoup.Adapters.StoryFeedAdapter;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substories;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substoryjsondata;
import in.thesoup.thesoup.SoupContract;
import in.thesoup.thesoup.gsonConversion;

/**
 * Created by Jani on 13-04-2017.
 */

public class NetworkUtilsStory {

    private Context mcontext;
    private List<Substories> mSubstories;
    private String Storytitle, followstatus;


    public NetworkUtilsStory(Context context, List<Substories> substories,String Storytitle, String followstatus){
        this.mcontext = context;
        this.mSubstories= substories;
        this.Storytitle = Storytitle;
        this.followstatus =followstatus;

    }


    public void getSingleStory(final SingleStoryAdapter feedstoryAdapter) {

        MySingleton singleton = MySingleton.getInstance(mcontext);

        //RequestQueue queue = singleton.getRequestQueue();


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, SoupContract.STORYURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("akunamatata", response.toString());



                        gsonConversion mpopulateUIStory = new gsonConversion();

                        Log.i("gson",mpopulateUIStory.toString());

                        mpopulateUIStory.fillStoryUI(response,mSubstories,feedstoryAdapter,Storytitle,followstatus);




                    }



                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Auto-generated method stub

                    }
                });/*{
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String,String> headers = new HashMap<String, String>();
            headers.put("TOKEN_KEY","TokenValue");
            headers.put("USER_ID","userId");
            return headers;
        }
        //nee to implement params for post request.
    };*/

        singleton.addToRequestQueue(jsObjRequest);

        //MySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }


}
