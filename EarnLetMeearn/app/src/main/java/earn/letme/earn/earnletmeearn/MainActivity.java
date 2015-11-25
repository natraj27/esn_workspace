package earn.letme.earn.earnletmeearn;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;


public class MainActivity extends Activity {

    WebView w;
    Button b1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        w=(WebView)findViewById(R.id.webView);
        b1=(Button)findViewById(R.id.b1);

        w.loadUrl("https://developer.android.com/index.html");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTextSelection();
            }
        });


    }



    public void startTextSelection() {
        try {
            WebView.class.getMethod("Android").invoke(this);
        } catch (Exception e) {
            try {
                WebView.class.getMethod("emulateShiftHeld").invoke(this);
            } catch (Exception e1) {
                KeyEvent shiftPressEvent = new KeyEvent(0, 0,
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SHIFT_LEFT, 0, 0);
                shiftPressEvent.dispatch(this);
          //      Toast.makeText(getContext(), R.string.select_text, Toast.LENGTH_LONG).show();
            }
        }
    }
 }