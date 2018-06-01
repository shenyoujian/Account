package com.example.ljs.account;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by ljs on 2017/5/21.
 */

public class AccountActivity extends Activity implements View.OnClickListener {

    private ImageView back;
    private TextView money_sum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_layout);
        back=(ImageView)findViewById(R.id.back);
        money_sum=(TextView)findViewById(R.id.money_sum);
        back.setOnClickListener(this);



        //设置净资产
        money_sum.setText(get_money_sum());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            default:
                break;

        }

    }
    public String get_money_sum() {
        Intent intent=getIntent();
        double income=Double.parseDouble(intent.getStringExtra("income"));
        double expenses=Double.parseDouble(intent.getStringExtra("expenses"));

        return String.valueOf(income-expenses);

    }


        }




