package in.thesoup.thesoup.Application;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import in.thesoup.thesoup.R;

import static android.os.Build.ID;

/**
 * Created by Jani on 29-04-2017.
 */

public class AnalyticsApplication extends Application {
    private Tracker mTracker;

    static AnalyticsApplication MyAppInstance;

    public AnalyticsApplication(){
        MyAppInstance = this;
    }

    public static AnalyticsApplication getInstance(){
        return MyAppInstance;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }



    public void sendEventUser(Tracker tracker,String pageview_click,String eventname,String fromwhere,String fb_id,String name ){

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(pageview_click)
                .setAction(eventname)
                .setLabel(fromwhere)
                .setCustomDimension(1,fb_id)
                .setCustomDimension(2,name)
                .build());
    }

    public void sendEvent(Tracker tracker,String pageview_click,String eventname,String fromwhere ){

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(pageview_click)
                .setAction(eventname)
                .setLabel(fromwhere)
                .build());
    }

    public void sendEventCollection(Tracker tracker,String pageview_click,String eventname
            ,String fromwhere,String CollectionName,String CollectionID ){

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(pageview_click)
                .setAction(eventname)
                .setLabel(fromwhere)
                .setCustomDimension(3,CollectionID)
                .setCustomDimension(4,CollectionName)
                .build());
    }

    public void sendEventCollectionUser(Tracker tracker,String pageview_click,String eventname
            ,String fromwhere,String CollectionName,String CollectionID,String fb_id,String name ){

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(pageview_click)
                .setAction(eventname)
                .setLabel(fromwhere)
                .setCustomDimension(1,fb_id)
                .setCustomDimension(2,name)
                .setCustomDimension(3,CollectionID)
                .setCustomDimension(4,CollectionName)
                .build());
    }







    public void sendScreenName(Tracker tracker,String action){
        tracker.setScreenName(action);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
