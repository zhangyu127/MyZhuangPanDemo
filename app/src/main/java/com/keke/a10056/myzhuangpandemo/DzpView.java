package com.keke.a10056.myzhuangpandemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.keke.a10056.myzhuangpandemo.view.WheelSurfView;

public class DzpView extends RelativeLayout {

    private WheelSurfView wheelSurfView;
    private RelativeLayout rl_dzp, rl_jl, rl_baoguo;
    private ImageView img_qidong, img_face;

    public DzpView(Context context) {
        super(context);
    }

    public DzpView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DzpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.dzp_zy_layout, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.wheelSurfView);

        //初始化相关自定义属性
        initStyle(typedArray);
    }

    //开始抽奖的图标
    private Integer mGoImgRes;

    private void initStyle(TypedArray typedArray) {
        mGoImgRes = typedArray.getResourceId(R.styleable.wheelSurfView_goImg, 0);
        rl_baoguo = (RelativeLayout) findViewById(R.id.rl_baoguo);
        rl_dzp = (RelativeLayout) findViewById(R.id.rl_dzp);
        rl_jl = (RelativeLayout) findViewById(R.id.rl_jl);
        wheelSurfView = (WheelSurfView) findViewById(R.id.wheelSurfView2);
        img_qidong = (ImageView) findViewById(R.id.img_qidong);
        img_face = (ImageView) findViewById(R.id.img_face);

        typedArray.recycle();
    }


}
