package com.example.ljs.account;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Created by ljs on 2017/5/23.
 */

public class AlarmActivity extends Activity {
    MediaPlayer alarmMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载指定音乐，并为之创建MediaPlayer对象
        alarmMusic=MediaPlayer.create(this,R.raw.alarm);
        alarmMusic.setLooping(true);
        //播放音乐
        alarmMusic.start();
        //创建一个对话框
        new AlertDialog.Builder(AlarmActivity.this)
                .setTitle("闹钟")
                .setMessage("闹钟响了，go！go！go！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //停止音乐
                        alarmMusic.stop();
                        //结束该Activity
                        AlarmActivity.this.finish();
                    }
                });
    }
}
