package esn.com.parceble_example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int a[]={1,2,3,4,5,9,8,7,6};

//        Intent i=new Intent(getApplication(),Second.class);
//        i.putExtra("data",new Parse("a","b",1,2));
//        startActivity(i);

        for(int i=0;i<a.length;i++){
for(int j=1;j<a.length;j++){
    Log.v("loglog",a[i]+"------yyyyy---------"+a[j]);

    if(i<j){
        int temp=a[i];
        a[i]=a[j];
        a[j]=temp;
    }
}

        }

    }


}
