package testing.mobile.natraj.self.mobiletesting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.InterstitialAd;


public class MainActivity extends Activity implements View.OnClickListener {
    // Remove the below line after defining your own ad unit ID.


    private InterstitialAd interstitial;

    Button samsung,sony,nexsus,moto,htc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        samsung =(Button)findViewById(R.id.samsung);
        nexsus=(Button)findViewById(R.id.nexsus);
        moto=(Button)findViewById(R.id.moto);
        htc=(Button)findViewById(R.id.htc);
        sony=(Button)findViewById(R.id.sony);

        samsung.setOnClickListener(this);
        nexsus.setOnClickListener(this);
        moto.setOnClickListener(this);
        htc.setOnClickListener(this);
        sony.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.samsung){
            Intent i=new Intent(getApplicationContext(),Samsung.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }else if(v.getId()==R.id.nexsus){
            Intent i=new Intent(getApplicationContext(),Samsung.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }else if(v.getId()==R.id.moto){
            Intent i=new Intent(getApplicationContext(),Samsung.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }else if(v.getId()==R.id.htc){
            Intent i=new Intent(getApplicationContext(),Samsung.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }else if(v.getId()==R.id.sony){
            Intent i=new Intent(getApplicationContext(),Samsung.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }
}






