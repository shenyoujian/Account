package com.example.ljs.account;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private  MySqliteHelper helper;
    private  AutoCompleteTextView username_edit;
    private  EditText pwd_edit;
    private  Button login_btn;
    private  TextView txtRegisterLink;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_btn=(Button)findViewById(R.id.Login_btn);
        helper=new MySqliteHelper(this,"budget.db",null,2);
        txtRegisterLink=(TextView)findViewById(R.id.zhucexinxi);



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginclicked();

            }
        });

        txtRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }




    //点击登陆按钮
    public void loginclicked() {
        username_edit=(AutoCompleteTextView) findViewById(R.id.login_username);
        pwd_edit=(EditText)findViewById(R.id.login_pwd);
        String username=username_edit.getText().toString();
        String pwd=pwd_edit.getText().toString();
        if(username.equals("")||pwd.equals("")) {
            Toast.makeText(this,"账号和密码不能为空",Toast.LENGTH_SHORT).show();
        } else if(login(username,pwd)) {
            Toast.makeText(MainActivity.this,"登陆成功",Toast.LENGTH_LONG).show();
            int id=getid(username);

            Intent intent=new Intent(MainActivity.this,WaterActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
        } else  {
            Toast.makeText(MainActivity.this,"登陆失败",Toast.LENGTH_LONG).show();
        }
    }


    //验证登录
    public boolean login(String username,String pwd) {
        SQLiteDatabase db=helper.getWritableDatabase();
        String sql="select * from users where username=? and pwd=?";
        Cursor cursor=db.rawQuery(sql,new String[] {username,pwd});
        if(cursor.moveToFirst()) {
            cursor.close();
            return  true;
        }
        return  false;
    }
    //获取账户
    public  int getid(String username) {
        SQLiteDatabase db=helper.getWritableDatabase();
        String sql="select id from users where username=?";
        Cursor cursor=db.rawQuery(sql,new String[] {username});
        if(cursor.moveToFirst()) {
            int id=cursor.getInt(0);
            return id;
        }
        return 0;
    }
}
