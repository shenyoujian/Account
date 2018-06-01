package com.example.ljs.account;


import android.content.ContentValues;
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
 * Created by ljs on 2017/5/15.
 */

public class ExpensesFragment extends Fragment implements View.OnClickListener {
    MySqliteHelper helper2;
    LinearLayout expenses_layout01;
    LinearLayout expenses_layout02;
    TextView expenses_kind;
    TextView expenses_pay;
    TextView expenses_date;
    EditText expenses_money;
    EditText expenses_remarks;
    Button expenses_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expenses_fragment, null, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        expenses_layout01=(LinearLayout)getView().findViewById(R.id.expenses_linearlayout01);
        expenses_layout02=(LinearLayout)getView().findViewById(R.id.expenses_linearlayout02);
        expenses_kind=(TextView)getView().findViewById(R.id.expenses_kind);
        expenses_pay=(TextView)getView().findViewById(R.id.expenses_pay);
        expenses_date=(TextView)getView().findViewById(R.id.expenses_date);
        expenses_btn=(Button) getView().findViewById(R.id.expenses_btn);
        expenses_money=(EditText)getView().findViewById(R.id.expenses_money);
        expenses_remarks=(EditText)getView().findViewById(R.id.expenses_remarks);
        //上下文注册
        this.registerForContextMenu(expenses_layout01);
        this.registerForContextMenu(expenses_layout02);
        //监听接口注册
        expenses_layout01.setOnClickListener(this);
        expenses_layout02.setOnClickListener(this);
        expenses_btn.setOnClickListener(this);

        //设置日期
        MakeANoteActivity activity=(MakeANoteActivity)getActivity();
        String data_text=activity.get_data();
        expenses_date.setText(data_text);

        //设置填写income_money的时候只显示数字键盘
        expenses_money.setInputType(EditorInfo.TYPE_CLASS_PHONE);


    }

    //实现监听接口中的点击方法
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expenses_linearlayout01:
                v.showContextMenu();
                break;
            case R.id.expenses_linearlayout02:
                v.showContextMenu();
                break;
            case R.id.expenses_btn:
                double money=Double.parseDouble(expenses_money.getText().toString());
                String kind=expenses_kind.getText().toString();
                String pay=expenses_pay.getText().toString();
                String remarks;

                if(money==0.0) {
                    Toast.makeText(getActivity(), "金额不能为0", Toast.LENGTH_LONG).show();
                } else {
                    if(expenses_remarks.getText().toString()==""&&expenses_remarks.getText().toString()==null) {
                        remarks="";
                    } else {
                        remarks=expenses_remarks.getText().toString();
                        String date=expenses_date.getText().toString();
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
        helper2=new MySqliteHelper(getActivity(),"budget.db", null, 2);
        SQLiteDatabase db=helper2.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("fid", fid);
        values.put("money", money);
        values.put("kind", kind);
        values.put("pay",pay);
        values.put("remarks",remarks);
        values.put("date",date);
        values.put("budget","支出");
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        if(v==expenses_layout01) {
            SubMenu subMenu1=menu.addSubMenu("食品酒水");
            subMenu1.add(0,0,Menu.NONE,"早午晚餐");
            subMenu1.add(0,1,Menu.NONE,"烟酒茶");
            subMenu1.add(0,2,Menu.NONE,"水果零食");
            SubMenu subMenu2=menu.addSubMenu("衣服饰品");
            subMenu2.add(0,3,Menu.NONE,"衣服裤子");
            subMenu2.add(0,4,Menu.NONE,"鞋帽包包");
            subMenu2.add(0,5,Menu.NONE,"化妆饰品");
        }
        if(v==expenses_layout02) {
            SubMenu subMenu1=menu.addSubMenu("现金");
            subMenu1.add(1,6,Menu.NONE,"现金");
            SubMenu subMenu2=menu.addSubMenu("信用卡");
            subMenu2.add(1,7,Menu.NONE,"信用卡");
            SubMenu subMenu3=menu.addSubMenu("虚拟账户");
            subMenu3.add(1,8,Menu.NONE,"饭卡");
            subMenu3.add(1,9,Menu.NONE,"支付宝");
            subMenu3.add(1,10,Menu.NONE,"公交卡");
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                expenses_kind.setText("食品酒水>早午晚餐");
                break;
            case 1:
                expenses_kind.setText("食品酒水>烟酒茶");
                break;
            case 2:
                expenses_kind.setText("食品酒水>水果零食");
                break;
            case 3:
               expenses_kind.setText("衣服饰品>衣服裤子");
                break;
            case 4:
                expenses_kind.setText("衣服饰品>鞋帽包包");
                break;
            case 5:
                expenses_kind.setText("衣服饰品>化妆饰品");
                break;
            case 6:
                expenses_pay.setText("现金");
                break;
            case 7:
                expenses_pay.setText("信用卡");
                break;
            case 8:
                expenses_pay.setText("虚拟账号>饭卡");
                break;
            case 9:
                expenses_pay.setText("虚拟账号>支付宝");
                break;
            case 10:
                expenses_pay.setText("虚拟账号>公交卡");
                break;
            default:
                return  super.onContextItemSelected(item);

        }
        return true;
    }
    }

