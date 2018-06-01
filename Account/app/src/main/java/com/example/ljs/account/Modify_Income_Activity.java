package com.example.ljs.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class Modify_Income_Activity extends Activity {

    MySqliteHelper helper4;
    private SQLiteDatabase mdatabase;

    EditText income_money;
    TextView income_kind;
    TextView income_pay;
    EditText income_remarks;
    TextView income_date;
    Button income_btn;
    Button delete_btn;

    HashMap<String,Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_modify__income_);

        income_money=(EditText)findViewById(R.id.income_money);
        income_kind=(TextView)findViewById(R.id.income_kind);
        income_pay=(TextView)findViewById(R.id.income_pay);
        income_remarks=(EditText)findViewById(R.id.income_remarks);
        income_date=(TextView)findViewById(R.id.income_date);
        income_btn=(Button)findViewById(R.id.income_btn);
        delete_btn=(Button)findViewById(R.id.delete_btn);

        final Intent intent =getIntent();
        Bundle data = intent.getExtras();
        SerializableMap serializableMap=(SerializableMap)data.get("map");
        map=serializableMap.getMap();

        income_money.setText(map.get("money").toString());
        income_kind.setText(map.get("kind").toString());
        income_pay.setText(map.get("pay").toString());
        income_remarks.setText(map.get("remarks").toString());
        income_date.setText(map.get("date").toString());

        income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Modify_Income_Activity.this)
                        //弹出窗口的最上头文字
                        .setTitle("修改当前数据")
                        //设置弹出窗口的图示
                        .setIcon(R.drawable.feiji)
                        //设置弹出窗口的信息
                        .setMessage("确定修改当前记录")
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //获取id
                                        int id=Integer.valueOf((map.get("sid").toString()));


                                        helper4=new MySqliteHelper(Modify_Income_Activity.this,"budget.db",null,2);
                                        mdatabase = helper4.getWritableDatabase();
                                        ContentValues values = new ContentValues();

                                        String income_money_text=income_money.getText().toString();
                                        String income_kind_text=income_kind.getText().toString();
                                        String income_pay_text=income_pay.getText().toString();
                                        String income_remarks_text=income_remarks.getText().toString();
                                        String income_date_text=income_date.getText().toString();

                                        if(income_money_text.isEmpty() || income_kind_text.isEmpty()||income_pay_text.isEmpty()){
                                            Toast.makeText(Modify_Income_Activity.this, "不能为空", Toast.LENGTH_SHORT).show();
                                        }else
                                            values.put("money",income_money_text);
                                            values.put("kind",income_kind_text);
                                            values.put("pay",income_pay_text);
                                            values.put("remarks",income_remarks_text);
                                            values.put("date",income_date_text);
                                            mdatabase.update("budget",values,"sid=?",new String[]{String.valueOf(id)});
                                            Toast.makeText(Modify_Income_Activity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                            Intent intent1=new Intent(Modify_Income_Activity.this,Bill_Activity.class);
                                            intent1.putExtra("id",getIntent().getIntExtra("fid",1));
                                            startActivity(intent1);
                                            mdatabase.close();
                                            helper4.close();
                                            Modify_Income_Activity.this.finish();
                                        }
                                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Modify_Income_Activity.this)
                        //弹出窗口的最上头文字
                        .setTitle("删除当前数据")
                        //设置弹出窗口的图示
                        .setIcon(R.drawable.feiji)
                        //设置弹出窗口的信息
                        .setMessage("确定删除当前记录")
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //获取id
                                        int id=Integer.valueOf((map.get("sid").toString()));


                                        // 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
                                        String[] whereArgs=new String[] {String.valueOf(id)};
                                        //获取当前数据库
                                        helper4=new MySqliteHelper(Modify_Income_Activity.this,"budget.db",null,2);
                                        mdatabase=helper4.getReadableDatabase();
                                        try {
                                            mdatabase.delete("budget","sid=?",whereArgs);
                                        }catch (Exception e) {
                                            Toast.makeText(Modify_Income_Activity.this,"删除出错",Toast.LENGTH_SHORT).show();
                                        }finally {
                                            Intent intent1=new Intent(Modify_Income_Activity.this,Bill_Activity.class);
                                            intent1.putExtra("id",getIntent().getIntExtra("fid",1));
                                            startActivity(intent1);
                                            finish();
                                            mdatabase.close();
                                            helper4.close();
                                        }

                                    }
                                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Intent intent1=new Intent(Modify_Income_Activity.this,Bill_Activity.class);
            intent1.putExtra("id",getIntent().getIntExtra("fid",1));
            startActivity(intent1);
            finish();
        }
        return false;
    }

}
