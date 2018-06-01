package com.example.ljs.account;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ljs on 2017/5/23.
 */

public class IncomeFragment extends Fragment implements View.OnClickListener {


    MySqliteHelper helper5;
    LinearLayout income_layout01;
    LinearLayout income_layout02;
    TextView income_kind;
    TextView income_pay;
    TextView income_date;
    EditText income_money;
    EditText income_remarks;
    Button income_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.income_fragement,null,true);
    }
    @Override
    public void onStart() {
        super.onStart();
        income_layout01=(LinearLayout)getView().findViewById(R.id.income_linearlayout01);
        income_layout02=(LinearLayout)getView().findViewById(R.id.income_linearlayout02);
        income_kind=(TextView)getView().findViewById(R.id.income_kind);
        income_pay=(TextView)getView().findViewById(R.id.income_pay);
        income_date=(TextView)getView().findViewById(R.id.income_date);
        income_btn=(Button)getView().findViewById(R.id.income_btn);
        income_money=(EditText)getView().findViewById(R.id.income_money);
        income_remarks=(EditText)getView().findViewById(R.id.income_remarks);
        //上下文注册
        this.registerForContextMenu(income_layout01);
        this.registerForContextMenu(income_layout02);
        //监听接口注册
        income_layout01.setOnClickListener(this);
        income_layout02.setOnClickListener(this);
        income_btn.setOnClickListener(this);

        //设置日期
        MakeANoteActivity activity=(MakeANoteActivity)getActivity();
        String data_text=activity.get_data();
        income_date.setText(data_text);

        //设置填写income_money的时候只显示数字键盘
        income_money.setInputType(EditorInfo.TYPE_CLASS_PHONE);




    }

    //实现监听接口中的点击方法
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.income_linearlayout01:
                v.showContextMenu();
                break;
            case R.id.income_linearlayout02:
                v.showContextMenu();
                break;
            case R.id.income_btn:
                double money=Double.parseDouble(income_money.getText().toString());
                String kind=income_kind.getText().toString();
                String pay=income_pay.getText().toString();
                String remarks;

                if(money==0.0) {
                    Toast.makeText(getActivity(), "金额不能为0", Toast.LENGTH_LONG).show();
                } else {
                    if(income_remarks.getText().toString()==""&&income_remarks.getText().toString()==null) {
                        remarks="";
                    } else {
                        remarks=income_remarks.getText().toString();
                        String date=income_date.getText().toString();
                        int id = getActivity().getIntent().getIntExtra("id",1);
                        if(insert(id,money,kind,pay,remarks,date)) {
                            Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_LONG).show();
                            getActivity().finish();
                        };
                    }
                }
            default:
                break;

        }
    }

    //向数据中插入数据
    public boolean insert(int fid,double money, String kind, String pay, String remarks,String date) {
        helper5=new MySqliteHelper(getContext(),"budget.db", null, 2);
        SQLiteDatabase db=helper5.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("fid", fid);
        values.put("money", money);
        values.put("kind", kind);
        values.put("pay",pay);
        values.put("remarks",remarks);
        values.put("date",date);
        values.put("budget","收入");
        db.insert("budget", null, values);
        db.close();
        return true;
    }

    //重写注册方法，把长按显示菜单取消
    @Override
    public void registerForContextMenu(View view) {
        super.registerForContextMenu(view);
        view.setLongClickable(false);
    }

    //设置上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        if(v==income_layout01) {
            SubMenu subMenu1=menu.addSubMenu("职业收入");
            subMenu1.add(2,11, Menu.NONE,"工资收入");
            subMenu1.add(2,12,Menu.NONE,"利息收入");
            subMenu1.add(2,13,Menu.NONE,"奖金收入");
            SubMenu subMenu2=menu.addSubMenu("其他收入");
            subMenu2.add(2,14,Menu.NONE,"礼金收入");
            subMenu2.add(2,15,Menu.NONE,"中奖收入");
            subMenu2.add(2,16,Menu.NONE,"经营所得");
        }
        if(v==income_layout02) {
            SubMenu subMenu1=menu.addSubMenu("现金");
            subMenu1.add(3,17,Menu.NONE,"现金");
            SubMenu subMenu2=menu.addSubMenu("信用卡");
            subMenu2.add(3,18,Menu.NONE,"信用卡");
            SubMenu subMenu3=menu.addSubMenu("虚拟账户");
            subMenu3.add(3,19,Menu.NONE,"饭卡");
            subMenu3.add(3,20,Menu.NONE,"支付宝");
            subMenu3.add(3,21,Menu.NONE,"公交卡");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 11:
                income_kind.setText("职业收入>工资收入");
                break;
            case 12:
                income_kind.setText("职业收入>利息收入");
                break;
            case 13:
                income_kind.setText("职业收入>奖金收入");
                break;
            case 14:
                income_kind.setText("其他收入>礼金收入");
                break;
            case 15:
                income_kind.setText("其他收入>中奖收入");
                break;
            case 16:
                income_kind.setText("其他收入>经营所得");
                break;
            case 17:
                income_pay.setText("现金");
                break;
            case 18:
                income_pay.setText("信用卡");
                break;
            case 19:
                income_pay.setText("饭卡");
                break;
            case 20:
                income_pay.setText("支付宝");
                break;
            case 21:
                income_pay.setText("公交卡");
                break;
            default:
                return  super.onContextItemSelected(item);
        }
        return true;
    }
}