package esn.com.urboost;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;

/**
 * Created by natraj.gadumala on 5/20/2015.
 */
public class Tempo extends Activity {
  //   private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private TextView batteryTxt;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryTxt.setText(String.valueOf(level) + "%");
        }
    };

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.tempo);
        batteryTxt = (TextView) this.findViewById(R.id.b);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
}