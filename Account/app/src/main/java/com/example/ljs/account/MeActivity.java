package com.example.ljs.account;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;




/**
 * Created by ljs on 2017/5/28.
 */

public class MeActivity extends Activity implements View.OnClickListener {
    private ImageView login;
    private TextView login_username;
    private MySqliteHelper helper5;
    private FrameLayout changemanage;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_layout);
        helper5=new MySqliteHelper(this,"budget.db",null,2);
        login=(ImageView)findViewById(R.id.login_image);
        login_username=(TextView)findViewById(R.id.login_show_username);
        changemanage=(FrameLayout)findViewById(R.id.changemanage);
        back=(ImageButton)findViewById(R.id.back);


        String idstring=String.valueOf(get_data());
        SQLiteDatabase db=helper5.getWritableDatabase();
        String sql="select username from users where id=?";
        Cursor cursor=db.rawQuery(sql,new String[] {idstring});
        if(cursor.moveToFirst()) {
            login_username.setText(cursor.getString(cursor.getColumnIndex("username")));
        }



        //注册
        login.setOnClickListener(this);
        changemanage.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    //获取id
    public int get_data() {
        Intent intent=getIntent();
        int id=intent.getIntExtra("id",1);
        return id;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changemanage:
                Intent intent=new Intent(MeActivity.this,ChangeManage.class);
                intent.putExtra("id",get_data());
                startActivity(intent);
                break;
            case R.id.back:
                this.finish();
                break;
            case R.id.login_image:
                Intent intent1=new Intent(MeActivity.this,UpdatePersonalInformation.class);
                intent1.putExtra("id",get_data());
                startActivity(intent1);
            default:
                break;
        }

    }
}
