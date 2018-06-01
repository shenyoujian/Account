package com.example.ljs.account;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeManage extends Activity {

    TextView change_username;
    TextView change_mail;
    Button change_btn;
    String username;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_manage);

        change_username=(TextView)findViewById(R.id.change_username);
        change_mail=(TextView)findViewById(R.id.change_mail);

        getData();

        change_username.setText(username);
        change_mail.setText(mail);

        change_btn=(Button)findViewById(R.id.change_btn);

        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    changeData();
            }
        });
    }

    public void getData() {
        MySqliteHelper helper=new MySqliteHelper(this,"budget.db",null,2);
        SQLiteDatabase mdatabase=helper.getReadableDatabase();

        Cursor cursor=mdatabase.rawQuery("select * from users where id = ?",new String[]{get_id()});
        cursor.moveToFirst();
        username=cursor.getString(cursor.getColumnIndex("username"));
        mail=cursor.getString(cursor.getColumnIndex("mail"));

        cursor.close();
        mdatabase.close();
        helper.close();
    }

    public void changeData(){
        MySqliteHelper helper=new MySqliteHelper(this,"budget.db",null,2);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String change_username_text =  change_username.getText().toString();
        String change_mail_text  = change_mail.getText().toString();

        if(change_username_text.isEmpty() || change_mail_text.isEmpty()){
            Toast.makeText(ChangeManage.this, "不能为空", Toast.LENGTH_SHORT).show();
        }else if(!Validator.isEmail(change_mail_text)) {
            Toast.makeText(ChangeManage.this, "邮箱格式错误", Toast.LENGTH_SHORT).show();
        }else {
                values.put("username",change_username_text);
                values.put("mail",change_mail_text);
                db.update("users",values,"id=?",new String[]{get_id()});
                Toast.makeText(ChangeManage.this, "修改成功", Toast.LENGTH_SHORT).show();
                ChangeManage.this.finish();
        }


        db.close();
        helper.close();
    }

    public String get_id(){
        return String.valueOf(getIntent().getIntExtra("id",1));
    }
}
