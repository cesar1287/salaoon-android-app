package comcesar1287.github.salaoon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class SignWithActivity extends AppCompatActivity implements FacebookCallback<LoginResult>, View.OnClickListener{

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_with);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, this);

        RelativeLayout btFacebook = findViewById(R.id.login_button_facebook);
        btFacebook.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, CategoryRegisterActivity.class));
        finish();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.login_button_facebook:
                LoginManager.getInstance().logInWithReadPermissions(this,
                        Arrays.asList("public_profile","user_friends","email"));
                break;
        }
    }
}
