package com.keke.a10056.myzhuangpandemo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
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


public class BigSoundBoardDialog extends Dialog implements View.OnClickListener {
    private final Activity ac;

    //转盘绑定
    private WheelSurfView.Builder build;

    //每个扇形旋转的时间
    private int mVarTime = 75;

    //最低圈数 默认值3 也就是说每次旋转都会最少转3圈
    private int mMinTime = 6;


    //大转盘布局
    private RelativeLayout rl_dzp, rl_jl;

    //添加每一个item集合
    private List<View> views;
    //每一个扇形的角度
    private float mAngle;


    //颜色的集合
    private List<Integer> colors;

    //控件大转盘背景背景
    private WheelSurfView wheelSurfView2;


    //启动中间按钮
    private ImageView img_qidong;


    //上面显示的图片
    private ImageView img_face;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:     //删除
                    for (int i = 0; i < views.size(); i++) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(views.get(i), "rotation", 0f, (float) (i * (360.0 / views.size())));
                        animator.setDuration(1);
                        animator.start();
                    }
                    putData();

                    break;
                case 2:   //添加

                    //添加颜色
                    colors.add(Color.parseColor("#4394C5"));

                    //添加扇形item布局
                    View inflate = LayoutInflater.from(getContext()).inflate(R.layout.board_item, rl_dzp, false);
                    View view = inflate.findViewById(R.id.ll_1);
                    views.add(view);
                    ((RelativeLayout) inflate).removeView(view);
                    rl_dzp.addView(view);


                    //实现控件转移角度  形成一个大转盘
                    for (int i = 0; i < views.size(); i++) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(views.get(i), "rotation", 0f, (float) (i * (360.0 / views.size())));
                        animator.setDuration(1);
                        animator.start();
                    }
                    //为每一个控件赋值  并计算角度
                    putData();
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    public BigSoundBoardDialog(Activity context) {
        super(context, R.style.dialog);
        ac = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_sound_board_dia);
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
    private void initView() {
//        初始化大装盘
        rl_dzp = ((RelativeLayout) findViewById(R.id.rl_dzp));
        rl_jl = ((RelativeLayout) findViewById(R.id.rl_jl));
        wheelSurfView2 = findViewById(R.id.wheelSurfView2);
        img_qidong = findViewById(R.id.img_qidong);
        img_face = findViewById(R.id.img_face);


        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.img_qidong).setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.tv_add).setOnClickListener(this);

    }


    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        colors = new ArrayList<>();
        colors.add(Color.parseColor("#F6829F"));
        colors.add(Color.parseColor("#E83030"));
        colors.add(Color.parseColor("#464AE1"));

        //实现item布局
        views = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.board_item, rl_dzp, false);
            View view = inflate.findViewById(R.id.ll_1);
            views.add(view);
            ((RelativeLayout) inflate).removeView(view);
            rl_dzp.addView(view);
        }

        //转移角度   实现大转盘
        for (int i = 0; i < views.size(); i++) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(views.get(i), "rotation", 0f, (float) (i * (360.0 / views.size())));
            animator.setDuration(1);
            animator.start();
        }
        putData();
    }


    /**
     * 添加数据
     **/
    public void putData() {
        //每一个扇形的角度
        mAngle = (float) (360.0 / colors.size());
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            TextView tv_1_num = (TextView) view.findViewById(R.id.tv_dzp_num);
            ImageView iv_1_ic = (ImageView) view.findViewById(R.id.iv_1_ic);
            tv_1_num.setText(10 + i + "");
            iv_1_ic.setBackgroundResource(R.drawable._2_weixin);
        }
        ZhuangJiaoDu();
        initChuShiHua();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:    //按钮启动转盘
                if (colors.size() == 1) {
                    Toast.makeText(ac, "目前只有一人无法进行游戏！！！！！", Toast.LENGTH_LONG).show();
                    return;
                }
                putLuckData(2);
                break;
            case R.id.iv_close:   //dissmiss取消按钮
                cancel();
                break;
            case R.id.img_qidong:   //中间转盘按钮
                if (colors.size() == 1) {
                    Toast.makeText(ac, "目前只有一人无法进行游戏！！！！！", Toast.LENGTH_LONG).show();
                    return;
                }
                putLuckData(2);
                break;
            case R.id.tv_add:
                initAdd();
                break;
        }
    }


    /**
     * 添加数据
     **/
    private void initAdd() {
        if (colors.size() == 8) {
            Toast.makeText(ac, "已经到达游戏上线人数！！！！！", Toast.LENGTH_LONG).show();
            return;
        }

        handler.sendEmptyMessage(2);

    }


    /**
     * 删除数据
     **/
    private void initClear(final int num) {

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (colors.size()>1){
                    colors.remove((colors.size() - num + 1) %
                            colors.size());
                }

                if (views.size()>1){
                    rl_dzp.removeView(views.get((colors.size() - num + 1) %
                            colors.size()));

                    views.remove((colors.size() - num + 1) %
                            colors.size());
                }



                handler.sendEmptyMessage(1);
            }
        }, 2000);
    }

    /**
     * 初始化转盘
     **/
    private void initChuShiHua() {
        build = new WheelSurfView.Builder()
                .setmColors(colors)
                .setmTypeNum(colors.size())
                .build();
        wheelSurfView2.setConfig(build);
        wheelSurfView2.startZhangJiaoDu();
    }


    /**
     * 启动转盘   设置选择哪个
     **/
    public void putLuckData(int type) {
        wheelSurfView2.startRotate(type);
        play(type);
    }


    /**
     * 动画效果转盘
     **/
    //目前的角度
    private float currAngle = 0;
    //记录上次的位置
    private int lastPosition;

    private void play(final int num) {
        //最低圈数是mMinTimes圈
        int newAngle = (int) (360 * mMinTime + (num - 1) * mAngle + currAngle - (lastPosition == 0 ? 0 : ((lastPosition - 1) * mAngle)));
        //计算目前的角度划过的扇形份数
        int nums = (int) ((newAngle - currAngle) / mAngle);

        ObjectAnimator animator = ObjectAnimator.ofFloat(rl_dzp, "rotation", currAngle, newAngle);

//        currAngle = newAngle;
////        lastPosition = num;
        animator.setDuration(nums * mVarTime);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                img_face.setBackgroundResource(R.drawable._2_weixin);
                initClear(num);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }


    /**
     * 动画效果转盘
     **/
    //目前的角度
    private float currAngles = 0;
    //记录上次的位置
    private int lastPositions;

    private void ZhuangJiaoDu() {
        //最低圈数是mMinTimes圈
        int newAngle = (int) (360 * mMinTime + (1 - 1) * mAngle + currAngles - (lastPositions == 0 ? 0 : ((lastPositions - 1) * mAngle)));
        //计算目前的角度划过的扇形份数
        int nums = (int) ((newAngle - currAngles) / mAngle);

        ObjectAnimator animator = ObjectAnimator.ofFloat(rl_dzp, "rotation", currAngles, newAngle);
        animator.setDuration(1);

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }
}
