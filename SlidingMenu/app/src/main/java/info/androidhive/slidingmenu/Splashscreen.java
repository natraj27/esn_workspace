package info.androidhive.slidingmenu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by natraj.gadumala on 8/12/2015.
 */
public class Splashscreen extends Activity {

    // Splash screen timer

  private static int SPLASH_TIME_OUT = 3000;

    ConnectionDetector cd ;

    Boolean isInternetPresent;
    RelativeLayout connectionview;

    SharedPreferences otlogin;
    SharedPreferences.Editor otloginedit;

    boolean loginstatus;
    TextView tryagain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        otlogin =getSharedPreferences("OT", 0);

        loginstatus=otlogin.getBoolean("Login", false);

        connectionview=(RelativeLayout)findViewById(R.id.connectionview);
        tryagain=(TextView)findViewById(R.id.tryagain);
        tryagain.setPaintFlags(tryagain.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isInternetPresent = cd.isConnectingToInternet();
                if(isInternetAccessible()) {


                    if(loginstatus){
                        Intent i = new Intent(Splashscreen.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(Splashscreen.this, Login.class);
                        startActivity(i);
                        finish();
                    }

                }else{

                }
            }
        });

        cd = new ConnectionDetector(getApplicationContext());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                isInternetPresent = cd.isConnectingToInternet();
                if(isInternetAccessible()) {



                    if(loginstatus){
                        Intent i = new Intent(Splashscreen.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(Splashscreen.this, Login.class);
                        startActivity(i);
                        finish();
                    }

                }else{
                }
            }
        }, SPLASH_TIME_OUT);
    }


    public boolean isInternetAccessible() {
        if (isInternetPresent) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("--------", "Couldn't check internet connection", e);
                connectionview.setVisibility(View.VISIBLE);
             }
        } else {
            Log.d("--------", "Internet not available!");
            connectionview.setVisibility(View.VISIBLE);

        }
        return false;
    }
}