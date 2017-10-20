package comcesar1287.github.salaoon.view;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import comcesar1287.github.salaoon.R;

public class RegisterProfessionalActivity extends AppCompatActivity {

    Button hourFirst, hourFinal;
    int horaInicial, horaFinal, minutoInicial, minutoFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_professional);

        hourFirst = (Button) findViewById(R.id.register_button_hour_first);
        hourFinal = (Button) findViewById(R.id.register_button_hour_final);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void btn_hour_first(View view) {
        final Calendar c = Calendar.getInstance();
        horaInicial=c.get(Calendar.HOUR_OF_DAY);
        minutoInicial=c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hourFirst.setText(hourOfDay + ":" + minute);
            }
            }, horaInicial, minutoInicial, false);

        timePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void btn_hour_final(View view) {
        final Calendar c = Calendar.getInstance();
        horaFinal=c.get(Calendar.HOUR_OF_DAY);
        minutoFinal=c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hourFinal.setText(hourOfDay + ":" + minute);
            }
        }, horaInicial, minutoInicial, false);

        timePickerDialog.show();
    }
}
