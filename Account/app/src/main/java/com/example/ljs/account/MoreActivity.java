package com.example.ljs.account;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


/**
 * Created by ljs on 2017/5/25.
 */

public class MoreActivity extends Activity implements View.OnClickListener {
    private FrameLayout bills;
    private FrameLayout alarm;
    private FrameLayout chart;
    private AlarmManager aManager;
    private ImageButton back;
    int id ;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_layout);
        bills=(FrameLayout) findViewById(R.id.frameLayoutRecorder);
        alarm=(FrameLayout) findViewById(R.id.frameLayoutPlan);
        chart=(FrameLayout) findViewById(R.id.frameLayoutChart);
        back=(ImageButton)findViewById(R.id.back);

        id = getIntent().getIntExtra("id",1);

        bills.setOnClickListener(this);
        back.setOnClickListener(this);
        chart.setOnClickListener(this);
        set_alarm();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frameLayoutRecorder:
                Intent intent=new Intent(MoreActivity.this,Bill_Activity.class);
                intent.putExtra("id",id);
                startActivity(intent);
                break;
            case R.id.frameLayoutChart:
                Intent intent1=new Intent(MoreActivity.this,ChartActivity.class);
                intent1.putExtra("id",id);
                startActivity(intent1);
                break;
            case R.id.back:
                this.finish();
                break;
            default:
                break;

        }

    }
    public void set_alarm() {
        aManager=(AlarmManager)getSystemService(Service.ALARM_SERVICE);
        //为设置闹铃按钮绑定监听器
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime=Calendar.getInstance();
                //创建一个TimePickerDiaglog实例，并把它显现出来
                new TimePickerDialog(MoreActivity.this,0,
                        //绑定监听器
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //指定启动AccountActiity组件
                                Intent intent=new Intent(MoreActivity.this,AlarmActivity.class);
                                //创建PendingIntent对象
                                PendingIntent pi=PendingIntent.getActivity(
                                        MoreActivity.this,0,intent,0);
                                Calendar c=Calendar.getInstance();
                                c.setTimeInMillis(System.currentTimeMillis());
                                //根据用户选择时间来设置Calendar对象
                                c.set(Calendar.HOUR,hourOfDay);
                                c.set(Calendar.MINUTE,minute);
                                //设置ALarmManager将在Calendar对应的时间启动指定组件
                                aManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pi);
                                //显示闹铃设置成功的提示信息
                                Toast.makeText(MoreActivity.this,"闹铃设置成功啦",Toast.LENGTH_LONG).show();

                            }},currentTime.get(Calendar.HOUR_OF_DAY)
                        ,currentTime.get(Calendar.MINUTE),false).show();
            }});
    }
}






