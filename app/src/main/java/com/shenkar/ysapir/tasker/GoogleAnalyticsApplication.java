package com.shenkar.ysapir.tasker;


import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.Parse;

import tasker.R;

public class GoogleAnalyticsApplication extends Application {

    private static GoogleAnalytics analytics;
    private static Tracker mTracker;


    public static GoogleAnalytics analytics() {
        return analytics;
    }
    public static Tracker tracker() {
        return mTracker;
    }


    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // Setting mTracker to Analytics Tracker declared in our xml Folder
            mTracker = analytics.newTracker(R.xml.analytics_tracker);
        }
        return mTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this);
        analytics = GoogleAnalytics.getInstance(this);

        // TODO: Replace the tracker-id with your app one from https://www.google.com/analytics/web/
        mTracker = analytics.newTracker("UA-76517816-1");

        // Provide unhandled exceptions reports. Do that first after creating the tracker
        mTracker.enableExceptionReporting(true);

        // Enable Remarketing, Demographics & Interests reports
        // https://developers.google.com/analytics/devguides/collection/android/display-features
        mTracker.enableAdvertisingIdCollection(true);

        // Enable automatic activity tracking for your app
        mTracker.enableAutoActivityTracking(true);
    }

}