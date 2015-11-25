package esn.com.urboost;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

/**
 * Created by natraj.gadumala on 5/19/2015.
 */
public class Charge extends Activity implements View.OnClickListener {

    private static final String TAG = "paymentExample";

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AdH6uzQgKatIIttNF6HRcmoFOHAJSobHKLVW4R4JCVNxV_nlWpYc6iqUHm2u0mZfEDfmEIjIZLI12ZUB";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Temp Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));


    ProgressBar vProgressBar;

    Button start,finis;
    TextView chargingpercentage,currentfar;
    static int ip=0,lp;
    int level;
    double a;
    boolean forfinish=false, forstart=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.charge);
        vProgressBar = (ProgressBar)findViewById(R.id.vprogressbar);

        start=(Button)findViewById(R.id.start);
        finis=(Button)findViewById(R.id.finish);
        chargingpercentage=(TextView)findViewById(R.id.chargingpercentage);
        currentfar=(TextView)findViewById(R.id.currentfar);

        start.setOnClickListener(this);
        finis.setOnClickListener(this);

        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
              level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            vProgressBar.setProgress(level);
            chargingpercentage.setText("Charging "+level+" %");
            if(ip==0){
                currentfar.setText("Current Fare "+0+" $USD");
            }else{
                double fare=level-ip;
                fare=fare*0.1;
                if(fare>-1) {
                    currentfar.setText("Current Fare " + fare + " $USD");
                }
            }

        }
    };
    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.start){
      //      new asyncTaskUpdateProgress().execute();


            if(forstart){
                ip=level;
                Toast.makeText(getApplicationContext(),"Charging Started Successfully",Toast.LENGTH_LONG).show();
                forfinish=true;
                forstart=false;
            }else{
                 paymentmessage("Not Allowed!! Already Running");
            }

        }
        if(v.getId()==R.id.finish) {
//            Intent homeintent=new Intent(getApplicationContext(), Home.class);
//            homeintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(homeintent);
              if(forfinish){
                  forfinish=false;
                  forstart=true;
            start.setClickable(true);

            lp = level;
            final Dialog d = new Dialog(Charge.this);
            d.setTitle("Confirm Payment Details");
            d.setContentView(R.layout.dialog);
            d.show();
            TextView intialpercentage = (TextView) d.findViewById(R.id.intialpercentage);
            TextView gainedpercentage = (TextView) d.findViewById(R.id.gainedpercentage);
            TextView amounttopay = (TextView) d.findViewById(R.id.amounttopay);
            Button pay = (Button) d.findViewById(R.id.pay);
            intialpercentage.setText("Initial Percentage: " + ip + " %");
            int g = level - ip;
            gainedpercentage.setText("Gain Percentage: " + g + " %");
            a = g * 0.1;
            amounttopay.setText("Final Fare: " + castRoundTo2(a) + " $USD");


            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();

                    Intent intent = new Intent(Charge.this, PayPalService.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                    startService(intent);
                    onBuyPressed(a);
                    ip = 0;
                    lp = 0;
                }
            });
              } else {
                  paymentmessage("Not Allowed!! Without Start");

              }
         }
    }

    public static double castRoundTo2(double d) {
        return (long) (d * 100 + 0.5) / 100.0;
    }
    public void onBuyPressed(double a) {
        /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE,""+a);

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        Intent intent = new Intent(Charge.this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }
    private PayPalPayment getThingToBuy(String paymentIntent,String fare) {
        return new PayPalPayment(new BigDecimal(fare), "USD", "Charging amount",
                paymentIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
//                        Toast.makeText(
//                                getApplicationContext(),
//                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
//                                .show();

                        paymentmessage("PaymentConfirmation info received from PayPal");

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
                paymentmessage("The user canceled.");

            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment, must be greater than zero or PayPalConfiguration was submitted. Please see the docs.");
                paymentmessage("An invalid Payment, must be greater than zero or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(
                                getApplicationContext(),
                                "Future Payment code received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(
                                getApplicationContext(),
                                "Profile Sharing code received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

        /**
         * TODO: Send the authorization response to your server, where it can
         * exchange the authorization code for OAuth access and refresh tokens.
         *
         * Your server must then store these tokens, so that your server code
         * can execute payments for this user in the future.
         *
         * A more complete example that includes the required app-server to
         * PayPal-server integration is available from
         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
         */

    }

    public void paymentmessage(String s){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(Charge.this);
        builder1.setMessage(""+s);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Got It!!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}

