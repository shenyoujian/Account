package com.example.ljs.account;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * Created by ljs on 2017/5/28.
 */

public class UpdatePersonalInformation extends Activity {
    private MySqliteHelper helper;
    private EditText oldpwd_edit;
    private EditText newpwd_edit;
    private Button update_btn;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateinformation);
        helper=new MySqliteHelper(this,"budget.db",null,2);
        update_btn=(Button)findViewById(R.id.udpate_btn);


        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpwd_edit = (EditText) findViewById(R.id.update_pwd);
                newpwd_edit = (EditText) findViewById(R.id.udpate_mail);
                String oldpwd = oldpwd_edit.getText().toString();
                String newpwd = newpwd_edit.getText().toString();
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                if(oldpwd.isEmpty()||newpwd.isEmpty()) {
                    Toast.makeText(UpdatePersonalInformation.this, "不能为空", Toast.LENGTH_SHORT).show();
                } else {

                    if(oldpwd.equals(getPwd())){
                        values.put("pwd",newpwd);
                        db.update("users",values,"id=?",new String[]{get_data()});
                        Toast.makeText(UpdatePersonalInformation.this, "修改成功", Toast.LENGTH_SHORT).show();
                        UpdatePersonalInformation.this.finish();
                    }else {
                        Toast.makeText(UpdatePersonalInformation.this, "旧密码错误,请重新填写", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        back=(ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePersonalInformation.this.finish();
            }
        });

    }

    public String getPwd() {
        MySqliteHelper helper4=new MySqliteHelper(this,"budget.db",null,2);
        SQLiteDatabase mdatabase=helper4.getReadableDatabase();
        Cursor cursor=mdatabase.rawQuery("select * from users where id = ?",new String[]{get_data()});
        cursor.moveToFirst();
        String pwd=cursor.getString(cursor.getColumnIndex("pwd"));
        cursor.close();
        mdatabase.close();
        helper4.close();
        return pwd;
    }

    //获取id
    public String get_data() {
        Intent intent=getIntent();
       int id=intent.getIntExtra("id",0);
        String idstring=String.valueOf(id);
        return idstring;
    }
}
