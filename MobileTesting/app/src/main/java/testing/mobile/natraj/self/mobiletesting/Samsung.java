package testing.mobile.natraj.self.mobiletesting;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by natraj.gadumala on 7/1/2015.
 */
public class Samsung extends Activity implements View.OnClickListener {
    // Remove the below line after defining your own ad unit ID.


    private InterstitialAd mInterstitialAd;

    Button service,battery,gsm,dump;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.samsung);

        service =(Button)findViewById(R.id.service);
        battery=(Button)findViewById(R.id.battery);
        gsm=(Button)findViewById(R.id.gsm);
        dump=(Button)findViewById(R.id.dump);

        service.setOnClickListener(this);
        battery.setOnClickListener(this);
        gsm.setOnClickListener(this);
        dump.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.service){
            String s="*#0*#";
            String ussdCode = "*" +Uri.encode ("#")+"0"+"*"+Uri.encode ("#");
            Intent intent = new Intent(Intent.ACTION_DIAL);
         //   intent.setClassName("com.android.settings", "com.android.settings.TestingSettings");
            intent.setData(Uri.parse("tel:"+ussdCode));
            startActivity(intent);
        }else if(v.getId()==R.id.battery){

        }else if(v.getId()==R.id.gsm){

        }else if(v.getId()==R.id.dump){

        }
    }
}