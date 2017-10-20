package comcesar1287.github.salaoon.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import comcesar1287.github.salaoon.R;
import comcesar1287.github.salaoon.controller.domain.User;
import comcesar1287.github.salaoon.controller.firebase.FirebaseHelper;

import comcesar1287.github.salaoon.controller.util.Utility;
import es.dmoral.toasty.Toasty;

public class SignWithActivity extends AppCompatActivity implements FacebookCallback<LoginResult>, View.OnClickListener{

    private CallbackManager callbackManager;

    private FirebaseAuth mAuth;

    private ProgressDialog dialog;

    private DatabaseReference mDatabase;

    private String Uid, name , email, profile_pic, database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        database = getIntent().getStringExtra(Utility.KEY_CONTENT_EXTRA_DATABASE);

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
        dialog = ProgressDialog.show(SignWithActivity.this,"",
                SignWithActivity.this.getResources().getString(R.string.processing_login), true, false);

        handleFacebookAccessToken(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
        Toasty.error(SignWithActivity.this, getResources().getString(R.string.error_facebook_login_canceled), Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onError(FacebookException error) {
        Toasty.error(SignWithActivity.this, getResources().getString(R.string.error_facebook_login_unknown_error), Toast.LENGTH_SHORT, true).show();
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

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnFailureListener(SignWithActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        if(e instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(SignWithActivity.this, R.string.error_failed_signin_email_exists,
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(SignWithActivity.this, R.string.error_unknown_error,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnSuccessListener(SignWithActivity.this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();

                        FirebaseUser user = mAuth.getCurrentUser();

                        finishLogin(user);
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            checkIfRegistrationIsComplete();
                        }
                    }
                });
    }

    private void checkIfRegistrationIsComplete() {
        DatabaseReference ckeckRegistration = mDatabase.child(database);
        ckeckRegistration.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(!snapshot.child(mAuth.getCurrentUser().getUid()).exists()) {
                    if (database.equals(FirebaseHelper.FIREBASE_DATABASE_CLIENTS)) {
                        Intent intent = new Intent(SignWithActivity.this, RegisterClientActivity.class);
                        intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                        startActivity(intent);
                    } else if (database.equals(FirebaseHelper.FIREBASE_DATABASE_PROFESSINALS)) {
                        Intent intent = new Intent(SignWithActivity.this, RegisterProfessionalActivity.class);
                        intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                        startActivity(intent);
                    }
                    finish();
                }else{
                    startActivity(new Intent(SignWithActivity.this, MainActivity.class));
                    finish();
                }

                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }

    public void finishLogin(FirebaseUser user){

        Uid = user.getUid();
        name = user.getDisplayName();
        email = ((user.getEmail()==null) ? "none" : user.getEmail());
        profile_pic = ((user.getPhotoUrl())==null ? "none" : user.getPhotoUrl().toString());

        mDatabase.child(FirebaseHelper.FIREBASE_DATABASE_USERS).child(Uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            FirebaseHelper.writeNewUser(mDatabase, Uid, name, email, profile_pic);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(SignWithActivity.this,
                                getResources().getString(R.string.error_signin),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
