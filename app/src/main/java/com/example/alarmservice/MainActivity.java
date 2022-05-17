package com.example.alarmservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView edtSeconds;
    Button btnStartTimer;
    Button btnSelectTime;
    Calendar time = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtSeconds = findViewById(R.id.edtSeconds);
        btnStartTimer = findViewById(R.id.btnStartTimer);
        btnSelectTime = findViewById(R.id.btnSelectTime);

        edtSeconds.setText(DateUtils.formatDateTime(this,
                time.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR));

        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(MainActivity.this, t,
                        time.get(Calendar.HOUR_OF_DAY),
                        time.get(Calendar.MINUTE), true)
                        .show();
            }
        });

        btnStartTimer.setOnClickListener(view -> {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(
                    time.getTimeInMillis(), getAlarmInfoIntent());

            alarmManager.setAlarmClock(alarmClockInfo, getAlarmAction());
            Toast.makeText(this,"Будильник установлен", Toast.LENGTH_SHORT).show();
        });
    }

    private PendingIntent getAlarmAction(){
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this,0,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getAlarmInfoIntent(){
        Intent alarmInfoIntent = new Intent(this, MainActivity.class);
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this,0,alarmInfoIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time.set(Calendar.HOUR_OF_DAY, hourOfDay);
            time.set(Calendar.MINUTE, minute);
            edtSeconds.setText(DateUtils.formatDateTime(getApplicationContext(),
                    time.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR));
        }
    };

}