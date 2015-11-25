package registration.vehicle.natraj.self.vehicleregistration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Method;


public class MainActivity extends Activity {
    WebView mWebView;
    private LinearLayout container;
     ImageView fb, ln ,gp;
    private EditText findBox;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         fb=(ImageView)findViewById(R.id.fb);
        ln=(ImageView)findViewById(R.id.ln);
        gp=(ImageView)findViewById(R.id.gp);


        mWebView = (WebView) findViewById(R.id.web);
        mWebView.loadUrl("https://aptransport.in/APCFSTONLINE/Reports/VehicleRegistrationSearch.aspx");
        mWebView.scrollTo(0, 100);



        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i=new Intent(getApplicationContext(), TraceView.class);
//                startActivity(i);

                      AlertDialog a=new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Please Copy Owner Name")
                        .setMessage("Please Copy Owner Name from table then Click Next to Serach")
                        .setPositiveButton("Find", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mWebView.findAll("Owner Name:");
                                try {
                                    Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                                    m.invoke(mWebView, true);
                                } catch (Exception ignored) {
                                }
                            }
                        })
                        .setNegativeButton("Next", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent i=new Intent(getApplicationContext(),TraceView.class);
                                i.putExtra("key","fb");
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);

                             }
                        })
                         .show();

            }
        });

        ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog a=new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Please Copy Owner Name")
                        .setMessage("Click Ok to Find Owner Name then Click Next to Serach")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int a= mWebView.findAll("Owner Name:");
                                try {
                                    Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                                    m.invoke(mWebView, true);
                                } catch (Exception ignored) {
                                }
                                Toast.makeText(getApplicationContext(), "find " + a, Toast.LENGTH_LONG).show();

                            }
                        })
                        .setNegativeButton("Next", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent i=new Intent(getApplicationContext(),TraceView.class);
                                i.putExtra("key","ln");
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            }
                        })
                        .show();
            }
        });

        gp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog a=new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Please Copy Owner Name")
                        .setMessage("Click Ok to Find Owner Name then Click Next to Serach")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               mWebView.findAll("Owner Name:");
                                try {
                                    Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                                    m.invoke(mWebView, true);
                                } catch (Exception ignored) {
                                }
                            }
                        })
                        .setNegativeButton("Next", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent i=new Intent(getApplicationContext(),TraceView.class);
                                i.putExtra("key","gp");
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);

                            }
                        })
                        .show();
            }
        });
    }

}
