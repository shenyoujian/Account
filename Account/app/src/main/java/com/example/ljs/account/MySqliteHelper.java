package com.example.ljs.account;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MySqliteHelper extends SQLiteOpenHelper {
    //自定义访问sqlite
    public static final String CREATE_USERS="create table users("
            + "id integer primary key autoincrement,"
            + "username Text,"
            + "pwd Text,"
            + "mail Text)";
    public static final String CREATE_BUDGET="create table budget("
            + "sid integer primary key autoincrement,"
            + "fid integer,"
            + "money real,"
            + "kind Text,"
            + "pay Text,"
            + "remarks Text,"
            + "date Text,"
            + "budget Text,"
            + "foreign key(fid) references users(id))";
    private Context mcontext;
    public MySqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version)   {
          super(context,name,factory,version);
          mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
                 db.execSQL(CREATE_USERS);
                 db.execSQL(CREATE_BUDGET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");    //删除表users
        db.execSQL("drop table if exists budget");//删除表shouru
        onCreate(db);  //然后再创建数据库，在activity的onCreate方法中，设置数据库version为2，实现了数据库的升级

    }

}
