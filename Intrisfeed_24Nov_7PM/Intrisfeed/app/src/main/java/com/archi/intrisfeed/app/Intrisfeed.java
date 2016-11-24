package com.archi.intrisfeed.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by archi_info on 9/21/2016.
 */

//@ReportsCrashes( // will not be used
//        mailTo = "archirayan56@gmail.com", // my email here
//        mode = ReportingInteractionMode.TOAST,
//        resToastText = R.string.crash_toast_text)
public class Intrisfeed extends Application{

        @Override
        protected void attachBaseContext(Context base) {
                super.attachBaseContext(base);
                MultiDex.install(this);
        }

        @Override
        public void onCreate() {
                super.onCreate();
//                ACRA.init(this);
//                ActivityLifecycle.init(this);
//                initCredentials(Constant.APP_ID, Constant.AUTH_KEY, Constant.AUTH_SECRET, Constant.ACCOUNT_KEY);
        }

}
