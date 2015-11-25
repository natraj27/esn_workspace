package esn.com.parceble_example;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by natraj.gadumala on 6/16/2015.
 */
public class Second extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse b=getIntent().getParcelableExtra("data");

        if(b!=null){
           String s1=b.s1;
        }
        Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
    }
}
