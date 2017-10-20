package comcesar1287.github.salaoon;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, CategoryRegisterActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.logo_move);

        logo = (ImageView) findViewById(R.id.logo);
        logo.startAnimation(animation);
    }
}
