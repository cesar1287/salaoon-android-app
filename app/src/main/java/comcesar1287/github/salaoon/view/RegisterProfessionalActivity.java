package comcesar1287.github.salaoon.view;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import comcesar1287.github.salaoon.R;
import comcesar1287.github.salaoon.controller.firebase.FirebaseHelper;
import comcesar1287.github.salaoon.controller.util.Utility;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class RegisterProfessionalActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btHourOpening, btHourClosing;

    private CheckBox cbHairDresse, cbMake, cbManicure;

    private TextInputLayout etName, etCNPJ, etCPF, etCEP, etAddress;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_professional);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        CircleImageView ivPhoto = findViewById(R.id.register_professional_photo);
        Glide.with(this)
                .load(firebaseUser.getPhotoUrl())
                .into(ivPhoto);

        etName = findViewById(R.id.register_name);
        etName.getEditText().setText(mAuth.getCurrentUser().getDisplayName());
        //etCNPJ = findViewById(R.id.register_cnpj);
        etCPF = findViewById(R.id.register_cpf);
        etCEP = findViewById(R.id.register_cep);
        etAddress = findViewById(R.id.register_address);

        btHourOpening = findViewById(R.id.register_button_hour_first);
        btHourClosing = findViewById(R.id.register_button_hour_final);

        cbHairDresse = findViewById(R.id.check_register_hairdresse);
        cbMake = findViewById(R.id.check_register_make);
        cbManicure = findViewById(R.id.check_register_manicure);

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

    private void attemptLogin() {
        String uid, name, cnpj, cpf, cep, address, hourOpening, hourClosing, specialty;

        uid = mAuth.getCurrentUser().getUid();
        name = etName.getEditText().getText().toString();
        //cnpj = etCNPJ.getEditText().getText().toString();
        cpf = etCPF.getEditText().getText().toString();
        cep = etCEP.getEditText().getText().toString();
        address = etAddress.getEditText().getText().toString();

        hourOpening = btHourOpening.getText().toString();
        hourClosing = btHourClosing.getText().toString();

        boolean allFieldsFilled = true;
        boolean allFilledCorrectly = true;

        if (name.equals("")) {
            allFieldsFilled = false;
            etName.setError(getString(R.string.error_required_field));
        } else {
            etName.setErrorEnabled(false);
        }

        if (cpf.equals("")) {
            allFieldsFilled = false;
            etCPF.setError(getString(R.string.error_required_field));
        } else {
            etCPF.setErrorEnabled(false);
        }

        if (cep.equals("")) {
            allFieldsFilled = false;
            etCEP.setError(getString(R.string.error_required_field));
        } else {
            etCEP.setErrorEnabled(false);
        }

        if (address.equals("")) {
            allFieldsFilled = false;
            etAddress.setError(getString(R.string.error_required_field));
        } else {
            etAddress.setErrorEnabled(false);
        }

        if(hourOpening.equals(getResources().getString(R.string.register_professional_button_hour_first))){
            allFieldsFilled = false;
            Toasty.error(this, getString(R.string.error_hour_opening_field_required), Toast.LENGTH_LONG, true).show();
        }

        if(hourClosing.equals(getResources().getString(R.string.register_professional_button_hour_final))){
            allFieldsFilled = false;
            Toasty.error(this, getString(R.string.error_hour_closing_field_required), Toast.LENGTH_LONG, true).show();
        }

        if(!cbHairDresse.isChecked() && !cbMake.isChecked() && !cbManicure.isChecked()){
            allFieldsFilled = false;
            Toasty.error(this, getString(R.string.error_specialty_checkbox_required), Toast.LENGTH_LONG, true).show();
        }

        if (allFieldsFilled) {
            if (!Utility.isValidCPF(cpf)) {
                allFilledCorrectly = false;
                etCPF.setError(getString(R.string.error_invalid_cpf));
            } else {
                etCPF.setErrorEnabled(false);
            }

            String opening[] = hourOpening.split(":");
            String closing[] = hourClosing.split(":");

            int hoursOpen = Integer.parseInt(opening[0]);
            int minutesOpen = Integer.parseInt(opening[1]);

            int hoursClose = Integer.parseInt(closing[0]);
            int minutesClose = Integer.parseInt(closing[1]);

            if(hoursOpen>hoursClose){
                allFilledCorrectly = false;
                Toasty.error(this, getString(R.string.error_hour_opening_greater_then_closing), Toast.LENGTH_LONG, true).show();
            }else if(hoursOpen==hoursClose){
                if(minutesOpen>minutesClose){
                    allFilledCorrectly = false;
                    Toasty.error(this, getString(R.string.error_hour_opening_greater_then_closing), Toast.LENGTH_LONG, true).show();
                }
            }
        }

        if (allFieldsFilled && allFilledCorrectly) {
            StringBuilder stringBuilderSpecialty = new StringBuilder();
            if(cbHairDresse.isChecked()){
                stringBuilderSpecialty.append(getResources().getString(R.string.register_professional_hairdresse));
                stringBuilderSpecialty.append(";");
            }
            if(cbMake.isChecked()){
                stringBuilderSpecialty.append(getResources().getString(R.string.register_professional_check_make));
                stringBuilderSpecialty.append(";");
            }
            if(cbManicure.isChecked()){
                stringBuilderSpecialty.append(getResources().getString(R.string.register_professional_manicure));
                stringBuilderSpecialty.append(";");
            }

            specialty = stringBuilderSpecialty.toString().toLowerCase();

            FirebaseHelper.writeNewProfessional(mDatabase, uid, name, cpf, cep, address, hourOpening, hourClosing, specialty);
            Toasty.success(this, getString(R.string.successfully_registered), Toast.LENGTH_SHORT, true).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void setupFieldMasks() {
        TextWatcher cpfMask = Utility.insertMask(getResources().getString(R.string.cpf_mask), etCPF.getEditText());
        etCPF.getEditText().addTextChangedListener(cpfMask);

        /*TextWatcher cnpjMask = Utility.insertMask(getResources().getString(R.string.cnpj_mask), etCNPJ.getEditText());
        etCNPJ.getEditText().addTextChangedListener(cnpjMask);*/

        TextWatcher cepMask = Utility.insertMask(getResources().getString(R.string.cep_mask), etCEP.getEditText());
        etCEP.getEditText().addTextChangedListener(cepMask);
    }

    public void btn_hour_first(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, 8, 0);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Date date = new Date();
                date.setMinutes(minute);
                date.setHours(hourOfDay);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.US);
                String formatted = format.format(date);
                btHourOpening.setText(formatted);
            }
            }, calendar.getTime().getHours(), calendar.getTime().getMinutes(), true);

        timePickerDialog.show();
    }

    public void btn_hour_final(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, 18, 0);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Date date = new Date();
                date.setMinutes(minute);
                date.setHours(hourOfDay);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.US);
                String formatted = format.format(date);
                btHourClosing.setText(formatted);
            }
        }, calendar.getTime().getHours(), calendar.getTime().getMinutes(), true);

        timePickerDialog.show();
    }
}
