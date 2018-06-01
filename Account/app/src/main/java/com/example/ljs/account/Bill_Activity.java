package com.example.ljs.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by ljs on 2017/5/21.
 */

public class Bill_Activity extends Activity implements View.OnClickListener{

    //列举数据的ListView
    private ListView mlistbills;
    //适配器
    private SimpleAdapter mlistbillsAdapter;
    //数据库
    private MySqliteHelper helper4;
    private ImageView back;
    private SQLiteDatabase mdatabase;

    //用户ID
    private int fid;

    //存储数据的数组列表
    ArrayList<HashMap<String ,Object>> listData=new ArrayList<HashMap<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bills);


        mlistbills=(ListView)findViewById(R.id.list_bills);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);

        GetData();
        mlistbillsAdapter=new SimpleAdapter(this,
                listData,
                R.layout.billitem,
                new String[] {"date","money","kind"},
                new int[] {R.id.texttimeshow,R.id.textmoneyshow,R.id.textkindshow}
        );
        //赋予数据
        mlistbills.setAdapter(mlistbillsAdapter);

        //长按响应
        mlistbills.setOnCreateContextMenuListener(listviewLongPress);


        //点击事件
        mlistbills.setOnItemClickListener(listviewClick);
        mlistbills.setOnItemLongClickListener(listviewLongClick);

        mlistbills.setOnTouchListener(onTouchListener);

    }
    View.OnTouchListener onTouchListener=new View.OnTouchListener() {
        float x,y,ux,uy;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x=event.getX();
                    y=event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    ux=event.getX();
                    uy=event.getY();
                    int p2=((ListView)v).pointToPosition((int)ux,(int)uy);
                     return false;
            }
            return false;
        }
    };

    AdapterView.OnItemLongClickListener listviewLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            return false;
        }
    };

    AdapterView.OnItemClickListener listviewClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

            Intent intent;
            HashMap<String,Object> map = listData.get(i);
            SerializableMap serializableMap=new SerializableMap();
            serializableMap.setMap(map);
            Bundle data=new Bundle();
            data.putSerializable("map",serializableMap);
            data.putInt("fid",fid);

            String kind=map.get("budget").toString();
            if(kind.equals("支出")){
                intent=new Intent(Bill_Activity.this,Modify_Expenses_Activity.class);
                intent.putExtras(data);
                startActivity(intent);
                finish();
            }else if(kind.equals("收入")){
                intent=new Intent(Bill_Activity.this,Modify_Income_Activity.class);
                intent.putExtras(data);
                startActivity(intent);
                finish();
            }else{
                //查询数据库有误
                Toast.makeText(Bill_Activity.this,"查询有误",Toast.LENGTH_SHORT).show();
            }

        }
    };




    //从数据库获取适配器数据
    public void GetData() {
        helper4=new MySqliteHelper(this,"budget.db",null,2);
        mdatabase=helper4.getReadableDatabase();

        fid = getIntent().getIntExtra("id",1);

        Cursor cursor=mdatabase.rawQuery("select * from budget where fid = ? order by sid desc",new String[]{String.valueOf(fid)});
        cursor.moveToFirst();
        int columnSize=cursor.getColumnCount();
        int number=0;
        while (number<cursor.getCount()) {
            HashMap<String,Object> map=new HashMap<String,Object>();
            String budget=cursor.getString(cursor.getColumnIndex("budget"));
            map.put("budget",budget);
            map.put("sid",cursor.getString(cursor.getColumnIndex("sid")));
            map.put("money",cursor.getDouble(cursor.getColumnIndex("money")));
            map.put("date",cursor.getString(cursor.getColumnIndex("date")));
            map.put("kind",cursor.getString(cursor.getColumnIndex("kind")));
            map.put("pay",cursor.getString(cursor.getColumnIndex("pay")));
            map.put("remarks",cursor.getString(cursor.getColumnIndex("remarks")));
            cursor.moveToNext();
            listData.add(map);
            number++;
            System.out.println(listData);

        }
        cursor.close();
        mdatabase.close();
        helper4.close();
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


    //长按事件响应
    View.OnCreateContextMenuListener listviewLongPress=new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            final AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) menuInfo;
            new AlertDialog.Builder(Bill_Activity.this)
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
                                    //获取位置索引
                                    int mlistpos=info.position;
                                    //获取对应hashmap数据内容
                                    HashMap<String,Object>map =listData.get(mlistpos);
                                    //获取id
                                    int id=Integer.valueOf((map.get("sid").toString()));


                                    // 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
                                    String[] whereArgs=new String[] {String.valueOf(id)};
                                    //获取当前数据库
                                    helper4=new MySqliteHelper(Bill_Activity.this,"budget.db",null,2);
                                    mdatabase=helper4.getReadableDatabase();
                                    try {
                                        mdatabase.delete("budget","sid=?",whereArgs);
                                        listData.remove(mlistpos);
                                        mlistbillsAdapter.notifyDataSetChanged();
                                    }catch (Exception e) {
                                        Toast.makeText(Bill_Activity.this,"删除出错",Toast.LENGTH_SHORT).show();
                                    }finally {
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
    };

}

