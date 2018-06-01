package com.example.ljs.account;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class ChartActivity extends Activity {

    private PieChartView pieChart;
    private PieChartData pieCharData;

    Set<Double> shouru = new HashSet<>();
    Set<Double> zhichu = new HashSet<>();

    List<SliceValue> values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        pieChart = (PieChartView) findViewById(R.id.pie_chart);
        pieChart.setOnValueTouchListener(selectListener);//设置点击事件监听

        setPieChartData();
        initPieChart();

    }

    public void getData() {
        MySqliteHelper helper4=new MySqliteHelper(this,"budget.db",null,2);
        SQLiteDatabase mdatabase=helper4.getReadableDatabase();

        int id = getIntent().getIntExtra("id",1);


        Cursor cursor=mdatabase.rawQuery("select * from budget where fid = ?",new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        int number=0;
        while (number<cursor.getCount()) {
            String budget=cursor.getString(cursor.getColumnIndex("budget"));
            double money=cursor.getDouble(cursor.getColumnIndex("money"));
            Log.i("budget",budget);
            Log.i("money",String.valueOf(money));
            if(budget.equals("收入")) {
                shouru.add(money);
            } else {
                zhichu.add(money);
            }
            cursor.moveToNext();
            number++;

        }
        cursor.close();
        mdatabase.close();
        helper4.close();
    }

    /**
     * 获取数据
     */
    private void setPieChartData(){

        getData();

        for (double sr : shouru) {
            SliceValue sliceValue = new SliceValue( (float) sr, Color.GREEN);
            values.add(sliceValue);
        }

        for (double zc : zhichu) {
            SliceValue sliceValue = new SliceValue( (float) zc, Color.RED);
            values.add(sliceValue);
        }
    }


    /**
     * 初始化
     */
    private void initPieChart() {
        pieCharData = new PieChartData();
        pieCharData.setHasLabels(true);//显示表情
        pieCharData.setHasLabelsOnlyForSelected(false);//不用点击显示占的百分比
        pieCharData.setHasLabelsOutside(false);//占的百分比是否显示在饼图外面
        pieCharData.setHasCenterCircle(true);//是否是环形显示
        pieCharData.setValues(values);//填充数据
        pieCharData.setCenterCircleColor(Color.WHITE);//设置环形中间的颜色
        pieCharData.setCenterCircleScale(0.5f);//设置环形的大小级别
        pieCharData.setCenterText1("收支统计");//环形中间的文字1
        pieCharData.setCenterText1Color(Color.BLACK);//文字颜色
        pieCharData.setCenterText1FontSize(14);//文字大小

        pieCharData.setCenterText2("收入(绿)-支出(红)");
        pieCharData.setCenterText2Color(Color.BLACK);
        pieCharData.setCenterText2FontSize(18);
        /**这里也可以自定义你的字体   Roboto-Italic.ttf这个就是你的字体库*/
//      Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Italic.ttf");
//      data.setCenterText1Typeface(tf);

        pieChart.setPieChartData(pieCharData);
        pieChart.setValueSelectionEnabled(true);//选择饼图某一块变大
        pieChart.setAlpha(0.9f);//设置透明度
        pieChart.setCircleFillRatio(1f);//设置饼图大小

    }




    /**
     * 监听事件
     */
    private PieChartOnValueSelectListener selectListener = new PieChartOnValueSelectListener() {

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onValueSelected(int arg0, SliceValue value) {
            // TODO Auto-generated method stub
            Toast.makeText(ChartActivity.this, "Selected: " + value.getValue(), Toast.LENGTH_SHORT).show();
        }
    };
}
