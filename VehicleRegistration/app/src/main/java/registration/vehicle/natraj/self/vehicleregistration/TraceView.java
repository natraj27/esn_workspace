package registration.vehicle.natraj.self.vehicleregistration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by natraj.gadumala on 7/22/2015.
 */
public class TraceView extends Activity {

    WebView mWebView;

     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

         Intent i=getIntent();
         String s=i.getStringExtra("key");

         if(s.equals("fb")){
 //            mWebView = (WebView) findViewById(R.id.web);
//             mWebView.loadUrl("https://aptransport.in/APCFSTONLINE/Reports/VehicleRegistrationSearch.aspx");
//             mWebView.scrollTo(0, 100);
         }else if(s.equals("ln")){


         }else if(s.equals("gp")){


         }



     }
}
