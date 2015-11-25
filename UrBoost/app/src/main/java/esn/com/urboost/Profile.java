package esn.com.urboost;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by natraj.gadumala on 5/19/2015.
 */
public class Profile extends Activity {

    EditText name,address,email,phone,cardnumber,expiredate,cvv;

    Button update;
    SharedPreferences sp ;
    SharedPreferences.Editor se;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile);

        sp=getSharedPreferences("profile",0);
        se=sp.edit();

        name=(EditText)findViewById(R.id.name);
        address=(EditText)findViewById(R.id.address);
        email=(EditText)findViewById(R.id.email);
        phone=(EditText)findViewById(R.id.phone);


        cardnumber=(EditText)findViewById(R.id.cardnumber);
        expiredate=(EditText)findViewById(R.id.expiredate);
        cvv=(EditText)findViewById(R.id.cvv);

        if(sp.getInt("v",0)==1){
            name.setText(sp.getString("name","name"));
            address.setText(sp.getString("address","address"));
            email.setText(sp.getString("email","email"));
            phone.setText(sp.getString("phone","phone"));
            cardnumber.setText(sp.getString("cardnumber","cardnumber"));
            expiredate.setText(sp.getString("expiredate","expiredate"));
            cvv.setText(sp.getString("cvv","cvv"));
        }


        update=(Button)findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().trim().length() > 0){

                    if(address.getText().toString().trim().length() > 0){

                        if(email.getText().toString().trim().length() > 0){

                            if(phone.getText().toString().trim().length() > 9){

                                if(expiredate.getText().toString().trim().length() > 0){

                                    if(cvv.getText().toString().trim().length() > 2){
                                        if(cardnumber.getText().toString().trim().length() > 2){
                                            se.putString("name",name.getText().toString().trim());
                                            se.putString("address",address.getText().toString().trim());
                                            se.putString("email",email.getText().toString().trim());
                                            se.putString("phone",phone.getText().toString().trim());
                                            se.putString("cardnumber",cardnumber.getText().toString().trim());
                                            se.putString("expiredate",expiredate.getText().toString().trim());
                                            se.putString("cvv",cvv.getText().toString().trim());
                                            se.putInt("v",1);
                                            se.commit();
                                            showerror("Profile Updated");

                                        }else{
                                            showerror(" Please Check Invalid Card");
                                        }
                                    }else{
                                        showerror(" Please Check Invalid CVV");
                                    }
                                }else{
                                    showerror(" Please Check Expire Date");
                                }
                            }else{
                                showerror(" Please Check Phone Number");
                            }
                        }else{
                            showerror(" Please Check Email");
                        }
                    }else{
                        showerror(" Please Check Address");
                    }
                }else{
                    showerror("Please Check User Name");
                }
            }
        });


    }

    public void showerror(String s){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(Profile.this);
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
