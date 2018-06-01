package com.example.ljs.account;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Modify_Expenses_Activity extends AppCompatActivity{

    MySqliteHelper helper4;
    private SQLiteDatabase mdatabase;

    EditText expenses_money;
    TextView expenses_kind;
    TextView expenses_pay;
    EditText expenses_remarks;
    TextView expenses_date;
    Button expenses_btn;
    Button delete_btn;

    HashMap<String,Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_modify__expenses_);


        expenses_money=(EditText)findViewById(R.id.expenses_money);
        expenses_kind=(TextView)findViewById(R.id.expenses_kind);
        expenses_pay=(TextView)findViewById(R.id.expenses_pay);
        expenses_remarks=(EditText)findViewById(R.id.expenses_remarks);
        expenses_date=(TextView)findViewById(R.id.expenses_date);
        expenses_btn=(Button)findViewById(R.id.expenses_btn);
        delete_btn=(Button)findViewById(R.id.delete_btn);

        Intent intent =getIntent();
        Bundle data = intent.getExtras();
        SerializableMap serializableMap=(SerializableMap)data.get("map");
        map=serializableMap.getMap();

        expenses_money.setText(map.get("money").toString());
        expenses_kind.setText(map.get("kind").toString());
        expenses_pay.setText(map.get("pay").toString());
        expenses_remarks.setText(map.get("remarks").toString());
        expenses_date.setText(map.get("date").toString());

        expenses_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Modify_Expenses_Activity.this)
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


                                        helper4=new MySqliteHelper(Modify_Expenses_Activity.this,"budget.db",null,2);
                                        mdatabase = helper4.getWritableDatabase();
                                        ContentValues values = new ContentValues();

                                        String expenses_money_text=expenses_money.getText().toString();
                                        String expenses_kind_text=expenses_kind.getText().toString();
                                        String expenses_pay_text=expenses_pay.getText().toString();
                                        String expenses_remarks_text=expenses_remarks.getText().toString();
                                        String expenses_date_text=expenses_date.getText().toString();

                                        if(expenses_money_text.isEmpty() || expenses_kind_text.isEmpty()||expenses_pay_text.isEmpty()){
                                            Toast.makeText(Modify_Expenses_Activity.this, "不能为空", Toast.LENGTH_SHORT).show();
                                        }else
                                            values.put("money",expenses_money_text);
                                        values.put("kind",expenses_kind_text);
                                        values.put("pay",expenses_pay_text);
                                        values.put("remarks",expenses_remarks_text);
                                        values.put("date",expenses_date_text);
                                        mdatabase.update("budget",values,"sid=?",new String[]{String.valueOf(id)});
                                        Toast.makeText(Modify_Expenses_Activity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        Intent intent1=new Intent(Modify_Expenses_Activity.this,Bill_Activity.class);
                                        intent1.putExtra("id",getIntent().getIntExtra("fid",1));
                                        startActivity(intent1);
                                        mdatabase.close();
                                        helper4.close();
                                        Modify_Expenses_Activity.this.finish();
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
                new AlertDialog.Builder(Modify_Expenses_Activity.this)
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
                                        helper4=new MySqliteHelper(Modify_Expenses_Activity.this,"budget.db",null,2);
                                        mdatabase=helper4.getReadableDatabase();
                                        try {
                                            mdatabase.delete("budget","sid=?",whereArgs);
                                        }catch (Exception e) {
                                            Toast.makeText(Modify_Expenses_Activity.this,"删除出错",Toast.LENGTH_SHORT).show();
                                        }finally {
                                            Intent intent1=new Intent(Modify_Expenses_Activity.this,Bill_Activity.class);
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
            Intent intent1=new Intent(Modify_Expenses_Activity.this,Bill_Activity.class);
            intent1.putExtra("id",getIntent().getIntExtra("fid",1));
            startActivity(intent1);
            finish();
        }
        return false;
    }

}
