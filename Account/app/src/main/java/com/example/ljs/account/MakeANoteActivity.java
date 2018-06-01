package com.example.ljs.account;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v4.app.FragmentManager;

import java.util.ArrayList;


/**
 * 主界面显示，包括图片、电影和音乐三个选项
 * @author weizhi
 * @version 1.0
 */

public class MakeANoteActivity extends FragmentActivity {
    //四个Textview
    private TextView t1,t2,t3,t4;
    //实现Tab滑动效果
    private ViewPager mViewPager;
    //动画图片
    private ImageView cursor;

    //动画图片偏移量
    private int offset=0;
    private int position_one;
    private int position_two;
    private int position_three;

    //动画图片宽度
    private int bmpW;

    //当前页卡编号
    private int currIndex=0;

    //存放Fragement
    private ArrayList<Fragment>  fragmentArrayList;

    //管理Fragment
    private FragmentManager fragmentManager;

    public Context context;

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_a_note_layout);
        context=this;

        //初始化TextView
        InitTextView();

        //初始化ImageView
        InitImageView();

        //初始化Fragment
        InitFragment();

        //初始化ViewPager
        InitViewPager();

        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public String get_data() {
        Intent intent=getIntent();
        String date=intent.getStringExtra("date");
        return date;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onResume() {
        //设置为竖屏
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }





    /**
     * 初始化头标
     */
    private  void InitTextView() {

        //支出头标
        t1=(TextView)findViewById(R.id.water_expenses);
        //收入头标
        t2=(TextView)findViewById(R.id.water_income);
        //转账头标
        t3=(TextView)findViewById(R.id.water_transfer);
        //余额头标
        t4=(TextView)findViewById(R.id.water_balance);


        //添加点击事件
        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
        t3.setOnClickListener(new MyOnClickListener(2));
        t4.setOnClickListener(new MyOnClickListener(3));
    }

    /**
     * 初始化页卡内容区
     */
    private void InitViewPager() {

        mViewPager=(ViewPager)findViewById(R.id.vpager);
        mViewPager.setAdapter(new MFragmentPagerAdapter(fragmentManager, fragmentArrayList));

        //让ViewPager缓存2个页面
        mViewPager.setOffscreenPageLimit(3);

        //设置默认打开第一页
        mViewPager.setCurrentItem(0);


        //将顶部文字恢复默认值
        resetTextViewTextColor();
        t1.setTextColor(Color.parseColor("#ffffff"));

        //设置viewpager页面滑动监听事件
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化动画
     */
    private  void InitImageView() {
        cursor=(ImageView)findViewById(R.id.cursor);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        //获取分辨率宽度
        int screenW=dm.widthPixels;

        bmpW=(screenW/4);

        //设置动画图片宽度
        setBmpW(cursor,bmpW);
        offset=0;

        //动画图片偏移量赋值
        position_one=(int)(screenW/4.0);
        position_two=position_one*2;
        position_three=position_one*3;


    }
    /**
     * 初始化Fragment，并添加到ArrayList中
     */
    private void InitFragment() {
        fragmentArrayList=new ArrayList<>();
        fragmentArrayList.add(new ExpensesFragment());
        fragmentArrayList.add(new IncomeFragment());
        fragmentArrayList.add(new TransferFragment());
        fragmentArrayList.add(new BalanceFragment());

        fragmentManager = getSupportFragmentManager();
    }

    /**
     * 头标点击监听
     * @author weizhi
     * @version 1.0
     */
    public class MyOnClickListener implements View.OnClickListener {
        private  int index=0;
        public MyOnClickListener(int i) {
            index=i;
        }
        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 页卡切换监听
     * @author weizhi
     * @version 1.0
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageSelected(int position) {
            Animation animation=null;
            switch (position) {

                //当前为页卡1
                case 0:
                    //从页卡1跳转转到页卡2
                    if(currIndex==1) {
                        animation=new TranslateAnimation(position_one, 0, 0, 0);
                        resetTextViewTextColor();
                        t1.setTextColor(Color.parseColor("#ffffff"));
                    } else if (currIndex==2) { //从页卡1跳转转到页卡3
                        animation = new TranslateAnimation(position_two, 0, 0, 0);
                        resetTextViewTextColor();
                        t1.setTextColor(Color.parseColor("#ffffff"));

                    } else if(currIndex==3) { //从页卡1跳转转到页卡4
                        animation = new TranslateAnimation(position_three, 0, 0, 0);
                        resetTextViewTextColor();
                        t1.setTextColor(Color.parseColor("#ffffff"));
            }
            break;

                //当前为页卡2
                case 1:
                    //从页卡2跳转转到页卡1
                    if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_one, 0, 0);
                    resetTextViewTextColor();
                    t2.setTextColor(Color.parseColor("#ffffff"));
                } else if (currIndex == 2) { //从页卡1跳转转到页卡3
                    animation = new TranslateAnimation(position_two, position_one, 0, 0);
                    resetTextViewTextColor();
                        t2.setTextColor(Color.parseColor("#ffffff"));
                } else if (currIndex==3) {  //从页卡1跳转转到页卡4
                        animation = new TranslateAnimation(position_three, position_one, 0, 0);
                        resetTextViewTextColor();
                        t2.setTextColor(Color.parseColor("#ffffff"));
                    }
                break;
                //当前为页卡3
                case 2:
                    //从页卡3跳转转到页卡1
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_two, 0, 0);
                        resetTextViewTextColor();
                        t3.setTextColor(Color.parseColor("#ffffff"));
                    } else if (currIndex == 1) {//从页卡3跳转转到页卡2
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        resetTextViewTextColor();
                        t3.setTextColor(Color.parseColor("#ffffff"));
                    } else if(currIndex==3) { //从页卡3跳转转到页卡4
                        animation = new TranslateAnimation(position_three, position_two, 0, 0);
                        resetTextViewTextColor();
                        t3.setTextColor(Color.parseColor("#ffffff"));
                    }
                    break;
                //当前为页卡4
                case 3:
                    //从页卡4跳转转到页卡1
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_three, 0, 0);
                        resetTextViewTextColor();
                        t4.setTextColor(Color.parseColor("#ffffff"));
                    } else if (currIndex == 1) {//从页卡4跳转转到页卡2
                        animation = new TranslateAnimation(position_one, position_three, 0, 0);
                        resetTextViewTextColor();
                        t4.setTextColor(Color.parseColor("#ffffff"));
                    } else if(currIndex==2) { //从页卡4跳转转到页卡3
                        animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        resetTextViewTextColor();
                        t4.setTextColor(Color.parseColor("#ffffff"));
                    }
                    break;
        }
        currIndex=position;

            animation.setFillAfter(true); //true:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
    }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    /**
     * 设置动画图片宽度
     * @param mWidth
     */
    private void setBmpW(ImageView imageView,int mWidth){
        ViewGroup.LayoutParams para;
        para = imageView.getLayoutParams();
        para.width = mWidth;
        imageView.setLayoutParams(para);
    }
    /**
     * 将顶部文字恢复默认值
     */
    private void resetTextViewTextColor(){

        t1.setTextColor(Color.parseColor("#fcc9c5"));
        t2.setTextColor(Color.parseColor("#fcc9c5"));
        t3.setTextColor(Color.parseColor("#fcc9c5"));
        t4.setTextColor(Color.parseColor("#fcc9c5"));
    }
}
