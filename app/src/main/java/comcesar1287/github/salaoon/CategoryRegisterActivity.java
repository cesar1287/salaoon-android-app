package comcesar1287.github.salaoon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CategoryRegisterActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_register);

        Button btCategoryClient = (Button) findViewById(R.id.button_category_client);
        btCategoryClient.setOnClickListener(this);
        Button btCategoryProfessional = (Button) findViewById(R.id.button_category_professional);
        btCategoryProfessional.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        Intent intent = new Intent(this, SignWithActivity.class);

        switch (id){
            case R.id.button_category_professional:
                startActivity(intent);
                finish();
                break;
            case R.id.button_category_client:
                startActivity(intent);
                finish();
                break;
        }
    }
}
