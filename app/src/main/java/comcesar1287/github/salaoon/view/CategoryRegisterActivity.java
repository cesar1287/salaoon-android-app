package comcesar1287.github.salaoon.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import comcesar1287.github.salaoon.R;
import comcesar1287.github.salaoon.controller.firebase.FirebaseHelper;
import comcesar1287.github.salaoon.controller.util.Utility;

public class CategoryRegisterActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_register);

        Button btCategoryClient = findViewById(R.id.button_category_client);
        btCategoryClient.setOnClickListener(this);
        Button btCategoryProfessional = findViewById(R.id.button_category_professional);
        btCategoryProfessional.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String database;

        Intent intent = new Intent(this, SignWithActivity.class);

        switch (id){
            case R.id.button_category_professional:
                database = FirebaseHelper.FIREBASE_DATABASE_PROFESSINALS;
                intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                startActivity(intent);
                finish();
                break;
            case R.id.button_category_client:
                database = FirebaseHelper.FIREBASE_DATABASE_CLIENTS;
                intent.putExtra(Utility.KEY_CONTENT_EXTRA_DATABASE, database);
                startActivity(intent);
                finish();
                break;
        }
    }
}
