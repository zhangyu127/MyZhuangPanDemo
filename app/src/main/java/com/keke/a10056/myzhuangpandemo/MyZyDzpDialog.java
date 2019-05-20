package com.keke.a10056.myzhuangpandemo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.keke.a10056.myzhuangpandemo.view.WheelSurfView;

import java.util.ArrayList;
import java.util.List;


public class MyZyDzpDialog extends Dialog implements View.OnClickListener {
    private final Activity ac;

    private DzpView dzpView;


    public MyZyDzpDialog(Activity context) {
        super(context, R.style.dialog);
        ac = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_zy_dzp_dia);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();

    }


    /**
     * 初始化界面控件
     */
    @SuppressLint("WrongViewCast")
    private void initView() {
//        初始化大装盘

        dzpView = (DzpView) findViewById(R.id.dzp);
        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.tv_add).setOnClickListener(this);
        findViewById(R.id.tv_clear).setOnClickListener(this);

    }


    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:    //按钮启动转盘

                break;
            case R.id.iv_close:   //dissmiss取消按钮
                dismiss();
                break;
            case R.id.img_qidong:   //中间转盘按钮

                break;
            case R.id.tv_add:      //添加数据

                break;
            case R.id.tv_clear: //删除数据

                break;
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }
}
