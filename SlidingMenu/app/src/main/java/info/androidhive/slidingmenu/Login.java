package info.androidhive.slidingmenu;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by natraj.gadumala on 7/28/2015.
 */
public class Login extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

   // private TextView mTextDetails;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    boolean onconnected =false;


    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "MainActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;


    private boolean mIntentInProgress;

    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;

    private SignInButton btnSignIn;
  //  private Button btnSignOut, btnRevokeAccess;
   // private ImageView imgProfilePic;
  //  private TextView txtName, txtEmail;
//    private LinearLayout llProfileLayout;
   TextView sinup,forgotpassword;
    SharedPreferences otlogin;
    SharedPreferences.Editor otloginedit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        otlogin =getSharedPreferences("OT",0);
        otloginedit =otlogin.edit();


//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .build();
//        StrictMode.setThreadPolicy(policy);


        mCallbackManager = CallbackManager.Factory.create();
        setupTokenTracker();
        setupProfileTracker();

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();
//        setupTextDetails();
        setupLoginButton();

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
    //    setGooglePlusButtonText(btnSignIn,"Sing in");

//        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
//        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
//        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
//        txtName = (TextView) findViewById(R.id.txtName);
//        txtEmail = (TextView) findViewById(R.id.txtEmail);
    //    llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        sinup=(TextView)findViewById(R.id.sinup);
        forgotpassword=(TextView)findViewById(R.id.forgot);

        sinup.setPaintFlags(sinup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgotpassword.setPaintFlags(forgotpassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Button click listeners
        btnSignIn.setOnClickListener(this);
//        btnSignOut.setOnClickListener(this);
//        btnRevokeAccess.setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }


    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
//    protected void setGooglePlusButtonText(SignInButton signInButton,
//                                           String buttonText) {
//        for (int i = 0; i < signInButton.getChildCount(); i++) {
//            View v = signInButton.getChildAt(i);
//
//            if (v instanceof TextView) {
//                TextView tv = (TextView) v;
//                tv.setTextSize(15);
//                tv.setTypeface(null, Typeface.NORMAL);
//                tv.setText(buttonText);
//                return;
//            }
//        }
//    }
    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
       // Log.v("------", "---------" + constructWelcomeMessage(profile));
        constructWelcomeMessage(profile);
    }

    @Override
    public void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }
    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

        // Get user's information
        getProfileInformation();
        onconnected =true;
        // Update the UI after signin
         updateUI(true);

    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
         } else {

        }
    }

    /**
     * Fetching user's information name, email, profile pic
     * */
    public String getProfileInformation() {
        String personName = null;
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                otloginedit.putString("UserName", personName);
                otloginedit.putString("UserEmail", email);
                otloginedit.commit();

//                txtName.setText(personName);
//                txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                //    new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
                Log.v("----", "-----" + personPhotoUrl);
                URL newurl = new URL(personPhotoUrl);
                Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                //  imgProfilePic.setImageBitmap(mIcon_val);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personName;
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        //updateUI(false);
        onconnected=false;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

//    private void setupTextDetails() {
//        mTextDetails = (TextView) findViewById(R.id.text_details);
//    }

    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("VIVZ", "" + currentAccessToken);
            }
        };
    }

    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.d("VIVZ", "" + currentProfile);
          //  Log.v("------", "---------" + constructWelcomeMessage(currentProfile));
                constructWelcomeMessage(currentProfile);
            }
        };
    }

    private void setupLoginButton() {
        LoginButton mButtonLogin = (LoginButton)findViewById(R.id.login_button);

         mButtonLogin.setReadPermissions("email");
         mButtonLogin.registerCallback(mCallbackManager, mFacebookCallback);
    }

    private String constructWelcomeMessage(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (profile != null) {
            stringBuffer.append("Welcome " + profile.getName());
            otloginedit.putString("UserName",profile.getName() );
            otloginedit.commit();
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();

        }
        return stringBuffer.toString();
    }
    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("VIVZ", "onSuccess");
            AccessToken accessToken = loginResult.getAccessToken();
        Profile profile = Profile.getCurrentProfile();



            //    Log.v("------", "---------" + constructWelcomeMessage(profile));
            GraphRequest.newMeRequest(
                    loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            if (response.getError() != null) {
                                // handle error
                                Log.v("---------", "--------------" + response.getError());

                            } else {
                                String fbemail = me.optString("email");
                                String id = me.optString("id");
                                // send email and id to your web server
                             Log.v("---------", "-------------time base-" + fbemail);
                                otloginedit.putString("UserEmail", fbemail);
                                otloginedit.commit();

                            }

                        }
                    }).executeAsync();
            constructWelcomeMessage(profile);

        }


        @Override
        public void onCancel() {
            Log.d("VIVZ", "onCancel");
        }

        @Override
        public void onError(FacebookException e) {
            Log.d("VIVZ", "onError " + e);
        }
    };

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }
     /**
     * Sign-out from google
     * */
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
             updateUI(false);
            onconnected=false;
        }
    }

    /**
     * Revoking access from google
     * */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                            onconnected=false;
                        }

                    });
        }
    }

    @Override
    public void onClick(View v) {

        if (onconnected) {
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
         } else {
            signInWithGplus();

        }
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            //bmImage.setImageResource(R.drawable.ic_launcher);
         }
    }
}

