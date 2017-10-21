package comcesar1287.github.salaoon.view;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import comcesar1287.github.salaoon.R;

public class RegisterProfessionalActivity extends AppCompatActivity {

    Button hourFirst, hourFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_professional);

        hourFirst = findViewById(R.id.register_button_hour_first);
        hourFinal = findViewById(R.id.register_button_hour_final);
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
                hourFirst.setText(formatted);
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
                hourFinal.setText(formatted);
            }
        }, calendar.getTime().getHours(), calendar.getTime().getMinutes(), true);

        timePickerDialog.show();
    }
}
