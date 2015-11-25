package esn.com.urboost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;


public class Home extends Activity implements View.OnClickListener {

    Button charge,profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);


        charge=(Button)findViewById(R.id.charge);
        profile=(Button)findViewById(R.id.profile);

        charge.setOnClickListener(this);
        profile.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.charge){

            Intent chargeintent=new Intent(getApplicationContext(),Charge.class);
            startActivity(chargeintent);
        }else if(v.getId()==R.id.profile){
            Intent profileintent=new Intent(getApplicationContext(),Profile.class);
            startActivity(profileintent);
        }
    }
}
