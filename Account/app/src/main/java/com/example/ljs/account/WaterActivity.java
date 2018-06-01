package com.example.ljs.account;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WaterActivity extends Activity implements View.OnClickListener {
    private TextView date_show;
    private static final int msgKey1 = 1;
    private Button make_a_note;
    private TextView today_expenses;
    private TextView today_income;
    private TextView week_expenses;
    private TextView week_income;
    private TextView month_expenses;
    private TextView month_income;

    private MySqliteHelper helper3;
    private LinearLayout bottom_account;
    private LinearLayout bottom_more;
    private LinearLayout bottom_water;
    private LinearLayout bottom_me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.water_layout);
        helper3=new MySqliteHelper(this,"budget.db",null,2);
        date_show = (TextView) findViewById(R.id.date_show);
        make_a_note=(Button)findViewById(R.id.make_a_note);
        today_expenses=(TextView)findViewById(R.id.today_expenses);
        today_income=(TextView)findViewById(R.id.today_income);
        week_expenses=(TextView)findViewById(R.id.week_expenses);
        week_income=(TextView)findViewById(R.id.week_income);
        month_expenses=(TextView)findViewById(R.id.month_expenses);
        month_income=(TextView)findViewById(R.id.month_income);
        bottom_account=(LinearLayout)findViewById(R.id.bottom_account);
        bottom_more=(LinearLayout)findViewById(R.id.bottom_more);
        bottom_water=(LinearLayout)findViewById(R.id.bottom_water);
        bottom_me=(LinearLayout)findViewById(R.id.bottom_me);



        //注册监听器
        make_a_note.setOnClickListener(this);
        bottom_account.setOnClickListener(this);
        bottom_more.setOnClickListener(this);
        bottom_water.setOnClickListener(this);
        bottom_me.setOnClickListener(this);
        //启动一个线程
        new TimeThread().start();


    }

    @Override
    protected void onStart() {
        super.onStart();
        //设置支出金钱
        today_expenses.setText(get_expenses()[0]);
        week_expenses.setText(get_expenses()[0]);
        month_expenses.setText(get_expenses()[0]);

        //设置收入金钱
        today_income.setText(get_income()[0]);
        week_income.setText(get_income()[0]);
        month_income.setText(get_income()[0]);
    }

    //实现监听接口的方法
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.make_a_note:
                String date=date_show.getText().toString();
                Intent intent=new Intent(WaterActivity.this,MakeANoteActivity.class);
                intent.putExtra("date",date);
                intent.putExtra("id",get_data());
                startActivity(intent);
                break;
            case R.id.bottom_account:
                Intent intent1=new Intent(WaterActivity.this,AccountActivity.class);
                /*
                   我就不设置月和年的界限了，因为不会，直接就传这边
                   今天的值过去
                 */
                intent1.putExtra("income",get_income()[2]);
                intent1.putExtra("expenses",get_expenses()[2]);
                intent1.putExtra("id",get_data());
                startActivity(intent1);
                break;
            case  R.id.bottom_more:
                Intent intent2=new Intent(WaterActivity.this,MoreActivity.class);
                intent2.putExtra("id",get_data());
                startActivity(intent2);
                break;
            case R.id.bottom_water:
                this.finish();
                break;
            case R.id.bottom_me:
                Intent intent3=new Intent(WaterActivity.this,MeActivity.class);
                intent3.putExtra("id",get_data());
                startActivity(intent3);
            default:
                break;
        }
    }


    //实时获取本地时间
    public class TimeThread extends  Thread{
        @Override
        public void run() {
            super.run();
            do{
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case msgKey1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                    date_show.setText(format.format(date));
                    break;
                default:
                    break;
            }
        }
    };

    //获取数据
    public int get_data() {
        Intent intent=getIntent();
        int data=intent.getIntExtra("id",0);
        return data;
    }

    //从数据库里获取支出的钱，然后相加
    public String[] get_expenses() {
        SQLiteDatabase db=helper3.getWritableDatabase();
        int id=get_data();
        String idstring=String.valueOf(id);
        String sql="select money,date from budget where fid=? and budget=?";

        double money_today=0.0;
        double money_week=0.0;
        double money_month=0.0;
        double money_year=0.0;
        Cursor cursor=db.rawQuery(sql,new String[] {idstring,"支出"});
        if(cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据打印
                if(DateUtil.isToday(cursor.getString(1)))
                  money_today=money_today+cursor.getDouble(0);

                if(DateUtil.isThisWeek(cursor.getString(1)))
                    money_week =money_week+cursor.getDouble(0);

                if(DateUtil.isThisMonth(cursor.getString(1).substring(0,8)))
                    money_month=money_month+cursor.getDouble(0);

                if(DateUtil.isThisYear(cursor.getString(1).substring(0,5)))
                    money_year=money_year+cursor.getDouble(0);

            } while (cursor.moveToNext());
        }

            cursor.close();
            String[] money={String.valueOf(money_today),String.valueOf(money_month)
            ,String.valueOf(money_year),String.valueOf(money_week)};

        return money;
    }


    //从数据库里获取收入的钱，然后相加
    public String[] get_income() {
        SQLiteDatabase db=helper3.getWritableDatabase();
        int id=get_data();
        String idstring=String.valueOf(id);
        String sql="select money,date from budget where fid=? and budget=?";

        double money_today=0.0;
        double money_week=0.0;
        double money_month=0.0;
        double money_year=0.0;
        Cursor cursor=db.rawQuery(sql,new String[] {idstring,"收入"});
        if(cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据打印
                if(DateUtil.isToday(cursor.getString(1)))
                money_today=money_today+cursor.getDouble(0);

                if(DateUtil.isThisWeek(cursor.getString(1)))
                    money_week =money_week+cursor.getDouble(0);

                if(DateUtil.isThisMonth(cursor.getString(1).substring(0,8)))
                    money_month=money_month+cursor.getDouble(0);

                if(DateUtil.isThisYear(cursor.getString(1).substring(0,5)))
                    money_year=money_year+cursor.getDouble(0);

            } while (cursor.moveToNext());
        }
        cursor.close();
        String[] money={String.valueOf(money_today),String.valueOf(money_week),
                String.valueOf(money_month)
        ,String.valueOf(money_year)};

        return money;

    }
}