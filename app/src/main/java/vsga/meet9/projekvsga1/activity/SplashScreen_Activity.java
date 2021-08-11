package vsga.meet9.projekvsga1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import vsga.meet9.projekvsga1.R;
import vsga.meet9.projekvsga1.sign.SignInActivity;


public class SplashScreen_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen_Activity.this, SignInActivity.class);
                startActivity(i);
                finish();
            }
        }, 5000);
    }
}