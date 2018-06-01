package com.example.ljs.account;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private MySqliteHelper helper;
    EditText username_edit;
    EditText pwd_edit;
    EditText pwd_again_edit;
    EditText mail_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        helper = new MySqliteHelper(this, "budget.db", null, 2);
        Button reg_btn = (Button) findViewById(R.id.reg_btn);
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    public void login() {
        username_edit = (EditText) findViewById(R.id.reg_username);
        pwd_edit = (EditText) findViewById(R.id.reg_pwd);
        pwd_again_edit = (EditText) findViewById(R.id.reg_pwd_again);
        mail_edit = (EditText) findViewById(R.id.reg_mail);
        String username = username_edit.getText().toString();
        String pwd = pwd_edit.getText().toString();
        String mail = mail_edit.getText().toString();
        String pwd_again = pwd_again_edit.getText().toString();
        if (empty(username, pwd, pwd_again)) {
            Toast.makeText(this, "账号密码确认密码都不能为空！", Toast.LENGTH_SHORT).show();
        } else if (confirm(pwd, pwd_again)) {
            Toast.makeText(this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
        } else if(!Validator.isEmail(mail)){
            Toast.makeText(this, "邮箱格式错误！", Toast.LENGTH_SHORT).show();
        } else if (check(username)) {
            Toast.makeText(this, "用户已经存在！", Toast.LENGTH_SHORT).show();
        } else {
            if (insert(username, pwd, mail)) {
                Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }
    }


    //向数据库中插入数据
    public boolean insert(String username, String pwd, String mail) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("pwd", pwd);
        values.put("mail", mail);
        db.insert("users", null, values);
        db.close();
        return true;

    }

    //验证用户名是否存在
    public boolean check(String username) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String query = "select * from users where username=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    //验证密码和用户名是否为空
    public boolean empty(String username, String pwd, String pwd_again) {
        if (username.equals("")|| pwd.equals("") || pwd_again.equals("")) {
            return true;
        }
        return false;
    }

    //验证密码和确认密码是否相同
    public boolean confirm(String pwd, String pwd_again) {
        if (!pwd.equals(pwd_again)) {
            return true;
        }
        return false;
    }

}