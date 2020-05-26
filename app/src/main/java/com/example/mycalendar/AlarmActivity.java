package com.example.mycalendar;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

//알람설정하는 창
public class AlarmActivity extends AppCompatActivity {

    //알람 시간
    private Calendar calendar;
    private TimePicker timePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        this.calendar=Calendar.getInstance();
        //현재 날짜 표시
        displayDate();

        this.timePicker=findViewById(R.id.timePicker);

        findViewById(R.id.btnCalendar).setOnClickListener(mClickListener);
        findViewById(R.id.btnAlarm).setOnClickListener(mClickListener);

    }


    //날짜표시
    private void displayDate(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        ((TextView) findViewById(R.id.txtDate)).setText(format.format(this.calendar.getTime()));

    }


    //DatePickerDialog 호출
    private  void showDatePicker(){
        DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                //알람날짜 설정
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                //날짜 표시
                displayDate();
            }
        } ,this.calendar.get(Calendar.YEAR), this.calendar.get(Calendar.MONTH),this.calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    //알람등록
    private  void  setAlarm(){
        this.calendar.set(Calendar.HOUR_OF_DAY, this.timePicker.getHour());
        this.calendar.set(Calendar.MINUTE, this.timePicker.getMinute());
        this.calendar.set(Calendar.SECOND,0);



        //현재일보다 이전이면 등록 실패
        if(this.calendar.before(Calendar.getInstance())){
            Toast.makeText(this,"알람시간이 현재시간보다 이전일 수 없습니다.",Toast.LENGTH_LONG).show();
            return;
        }

        //Receiver 설정
        Intent intent =new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        //알람설정
        AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, this.calendar.getTimeInMillis(),pendingIntent);



        //Toast보여주기
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss",Locale.getDefault());
        Toast.makeText(this,"Alarm: "+format.format(calendar.getTime()),Toast.LENGTH_LONG).show();


    }

    View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btnCalendar:
                    showDatePicker();
                    break;

                case R.id.btnAlarm:
                    setAlarm();
                    break;

            }
        }
    };

}
