package comcesar1287.github.salaoon.view;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import comcesar1287.github.salaoon.R;
import comcesar1287.github.salaoon.controller.firebase.FirebaseHelper;
import comcesar1287.github.salaoon.controller.util.Utility;
import es.dmoral.toasty.Toasty;

public class RegisterClientActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputLayout etName, etCPF, etCEP, etAddress;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        etName = findViewById(R.id.register_name);
        etName.getEditText().setText(mAuth.getCurrentUser().getDisplayName());
        etCPF = findViewById(R.id.register_cpf);
        etCEP = findViewById(R.id.register_cep);
        etAddress = findViewById(R.id.register_address);

        Button btSave = findViewById(R.id.register_save);
        btSave.setOnClickListener(this);

        setupFieldMasks();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.register_save:
                attemptLogin();
                break;
        }
    }

    private void setupFieldMasks() {
        TextWatcher cpfMask = Utility.insertMask(getResources().getString(R.string.cpf_mask), etCPF.getEditText());
        etCPF.getEditText().addTextChangedListener(cpfMask);

        TextWatcher cepMask = Utility.insertMask(getResources().getString(R.string.cep_mask), etCEP.getEditText());
        etCEP.getEditText().addTextChangedListener(cepMask);
    }

    private void attemptLogin() {
        String uid, name, cpf, cep, address;

        uid = mAuth.getCurrentUser().getUid();
        name = etName.getEditText().getText().toString();
        cpf = etCPF.getEditText().getText().toString();
        cep = etCEP.getEditText().getText().toString();
        address = etAddress.getEditText().getText().toString();

        boolean allFieldsFilled = true;
        boolean allFilledCorrectly = true;

        if(name.equals("")){
            allFieldsFilled = false;
            etName.setError("Campo obrigatório");
        }else{
            etName.setErrorEnabled(false);
        }

        if(cpf.equals("")){
            allFieldsFilled = false;
            etCPF.setError("Campo obrigatório");
        }else{
            etCPF.setErrorEnabled(false);
        }

        if(cep.equals("")){
            allFieldsFilled = false;
            etCEP.setError("Campo obrigatório");
        }else{
            etCEP.setErrorEnabled(false);
        }

        if(address.equals("")){
            allFieldsFilled = false;
            etAddress.setError("Campo obrigatório");
        }else{
            etAddress.setErrorEnabled(false);
        }

        if(allFieldsFilled){
            cpf = cpf.replaceAll("[.]", "").replaceAll("[-]","");

            if(!Utility.isValidCPF(cpf)){
                allFilledCorrectly = false;
                etCPF.setError("CPF inválido");
            }else{
                etCPF.setErrorEnabled(false);
            }
        }

        if(allFieldsFilled && allFilledCorrectly) {
            FirebaseHelper.writeNewClient(mDatabase, uid, name, cpf, cep, address);
            Toasty.success(this, "Cadastrado com sucesso", Toast.LENGTH_SHORT, true).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
