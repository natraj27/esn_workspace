package info.androidhive.slidingmenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
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

public class HomeFragment extends Fragment {

    private static final String TAG = "paymentExample";

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AfiRmgoNZMGyenSDiYe_n1j1V_uRXOwKKwaX5ysa1JYPDYkUD_TyyUJx6xksYABTNH7KhlLPrRufl79r";

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


    private Context mContext = null;
    private BitmapCache mMemoryCache = null;

    String tilte[]={"Causes","Architecture","Summer Holiday","Swachh Bharat","Nepal Earthquake","Architecture","Summer Holiday","Swachh Bharat","Nepal Earthquake"};

    int images []={R.drawable.architecture,R.drawable.penguins,R.drawable.summer,R.drawable.swacch,R.drawable.earth,R.drawable.architecture,R.drawable.summer,R.drawable.swacch,R.drawable.earth};


    private static final int INITIAL_DELAY_MILLIS = 300;

    private GoogleCardsAdapter mGoogleCardsAdapter;
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_googlecards, container, false);



        ListView listView = (ListView)rootView.findViewById(R.id.activity_googlecards_listview);

        mGoogleCardsAdapter = new GoogleCardsAdapter(getActivity());
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter=new SwingBottomInAnimationAdapter(mGoogleCardsAdapter) ;
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setAdapter(swingBottomInAnimationAdapter);

        for (int i = 0; i < 8; i++) {
            mGoogleCardsAdapter.add(i);
        }
        return rootView;
    }

//    @Override
//    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
//        for (int position : reverseSortedPositions) {
//            mGoogleCardsAdapter.remove(position);
//        }
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.menu_home, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
public class GoogleCardsAdapter extends ArrayAdapter<Integer> {


    GoogleCardsAdapter(final Context context) {
        mContext = context;
        mMemoryCache = new BitmapCache();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_googlecards_card, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.activity_googlecards_card_textview);
            view.setTag(viewHolder);

            viewHolder.imageView = (ImageView) view.findViewById(R.id.activity_googlecards_card_imageview);
            viewHolder.donate = (Button) view.findViewById(R.id.donate);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // setImageView(viewHolder, position);
        Bitmap bitmap = getBitmapFromMemCache(images[position]);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), images[position]);
            addBitmapToMemoryCache(images[position], bitmap);
        }
        if(position==0){
            viewHolder.donate.setVisibility(View.GONE);
            viewHolder.imageView.setVisibility(View.GONE);

        }else{
            viewHolder.donate.setVisibility(View.VISIBLE);
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.imageView.setImageBitmap(bitmap);
        }
        viewHolder.textView.setText(tilte[position]);

        viewHolder.donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PayPalService.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                getActivity().startService(intent);
                onBuyPressed(1.0);
            }
        });
        return view;
    }

//    private void setImageView(final ViewHolder viewHolder, final int position) {
//        int imageResId;
//        switch (getItem(position) % 5) {
//            case 0:
//                imageResId = images[position];
//                break;
//            case 1:
//                imageResId =images[position];
//                break;
//            case 2:
//                imageResId =images[position];
//                break;
//            case 3:
//                imageResId =images[position];
//                break;
//            default:
//                imageResId = images[position];
//        }
//
//        Bitmap bitmap = getBitmapFromMemCache(imageResId);
//        if (bitmap == null) {
//            bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
//            addBitmapToMemoryCache(imageResId, bitmap);
//        }
//        viewHolder.imageView.setImageBitmap(bitmap);
//    }

    private void addBitmapToMemoryCache(final int key, final Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(final int key) {
        return mMemoryCache.get(key);
    }

    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private  class ViewHolder {
        TextView textView;
        ImageView imageView;
        Button donate;
    }
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

        Intent intent = new Intent(getActivity(), PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        getActivity().startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }
    private PayPalPayment getThingToBuy(String paymentIntent,String fare) {
        return new PayPalPayment(new BigDecimal(fare), "USD", "Charging amount",
                paymentIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                                getActivity(),
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
                                getActivity(),
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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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

