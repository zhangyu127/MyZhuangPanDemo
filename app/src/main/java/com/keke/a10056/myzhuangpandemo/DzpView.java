package com.keke.a10056.myzhuangpandemo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
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


/**
 * 2019/5/21（喜庆的日子）
 * 张宇
 * 自定义DzpView，通过自定义底部的扇形（借鉴）和上面的RelativeLayout控件addview添加的view通过动画进行
 * 计算角度旋转变成上面的扇形，实现抽奖大转盘，并且使用多种转盘，可以进行增删来回切换，使用灵活
 * 用系统自定义的view，实现上面扇形上面的图片进行灵活的添加，并且还要计算角度，bitmap进行回收的话会报canvas回收错误，
 * 如果不回收，bitmap会过大，报内存溢出！！！！
 **/
public class DzpView extends RelativeLayout {

    //上下文对象
    private Context context;


    //实现动画渐隐渐出效果
    private AlphaAnimation alphaAnimation;
    private TransitionDrawable transitionDrawable;

    //底部转盘
    private WheelSurfView wheelSurfView;
    //上层转盘布局rl_dzp
    private RelativeLayout rl_dzp, rl_jl;
    //启动按键go   动画停止显示的图片
    private ImageView img_qidong, img_face;

    //数据名字列表
    private List<String> nameDzp;

    //颜色的集合
    private List<Integer> colors;

    //添加每一个item集合
    private List<View> views;
    //每一个扇形的角度
    private float mAngle;

    //每个扇形旋转的时间
    private int mVarTime = 75;

    //最低圈数 默认值3 也就是说每次旋转都会最少转3圈
    private int mMinTime = 6;

    //底部转盘绑定
    private WheelSurfView.Builder build;

    //handler实现删除和添加
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:     //删除

                    //一开始先设置透明，这样图片不会显示，等点击按钮时再显示
                    img_face.setAlpha(0.0f);
                    alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setFillAfter(true);


                    for (int i = 0; i < views.size(); i++) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(views.get(i), "rotation", 0f, (float) (i * (360.0 / views.size())));
                        animator.setDuration(1);
                        animator.start();
                    }

                    putData();

                    break;
                case 2:   //添加

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

                case 3:     //删除

                    //一开始先设置透明，这样图片不会显示，等点击按钮时再显示
                    img_face.setAlpha(0.0f);
                    alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setFillAfter(true);


                    for (int i = 0; i < views.size(); i++) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(views.get(i), "rotation", 0f, (float) (i * (360.0 / views.size())));
                        animator.setDuration(1);
                        animator.start();
                    }

                    putData();

                    break;
                default:
                    break;
            }
            return false;
        }
    });

    public DzpView(Context context) {
        super(context);
        this.context = context;
    }

    public DzpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //导入布局
        LayoutInflater.from(context).inflate(R.layout.dzp_zy_layout, this);
        rl_dzp = (RelativeLayout) findViewById(R.id.rl_dzp);
        rl_jl = (RelativeLayout) findViewById(R.id.rl_jl);
        wheelSurfView = (WheelSurfView) findViewById(R.id.wheelSurfView2);
        img_qidong = (ImageView) findViewById(R.id.img_qidong);
        img_face = (ImageView) findViewById(R.id.img_face);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.dzp_view);

        //初始化相关自定义属性
        initStyle(typedArray);
    }

    public DzpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    //开始抽奖的图标
    private Integer mGoImgRes;


    /**
     * 初始化转盘
     **/
    private void initStyle(TypedArray typedArray) {
        try {
            //获得属性值
            mGoImgRes = typedArray.getResourceId(R.styleable.dzp_view_dzp_image, 0);
        } finally {
            //回收这个对象
            typedArray.recycle();
        }

        if (mGoImgRes != 0) {
            img_qidong.setImageResource(mGoImgRes);
        }

        //监听方法
        img_qidong.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameDzp.size()!=0){
                    putLuckData(1);
                }
            }
        });

        initData();//初始化数据
    }

    /**
     * 初始化数据
     **/
    public void initData() {
        //一开始先设置透明，这样图片不会显示，等点击按钮时再显示
        img_face.setAlpha(0.0f);
        alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(2000);    //深浅动画持续时间
        alphaAnimation.setFillAfter(true);   //动画结束时保持结束的画面

        colors = new ArrayList<>();
        nameDzp = new ArrayList<>();


        //实现item布局
        views = new ArrayList<>();
        for (int i = 0; i < nameDzp.size(); i++) {
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
        putData();   //更新数据
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
            tv_1_num.setText(nameDzp.get(i) + "");
            iv_1_ic.setBackgroundResource(R.drawable._2_weixin);
        }
        ZhuangJiaoDu();   //转移角度对其中间
        initChuShiHua();   //初始化转盘
    }


    /**
     * 初始化转盘
     **/
    private void initChuShiHua() {
        build = new WheelSurfView.Builder()
                .setmColors(colors)
                .setmTypeNum(nameDzp.size())
                .build();
        wheelSurfView.setConfig(build);
        wheelSurfView.startZhangJiaoDu();
    }


    /**
     * 添加数据
     **/
    public void initAdd(String name, Integer colrs) {
        if (colors.size() == 10) {
            Toast.makeText(context, "已经到达游戏上线人数！！！！！", Toast.LENGTH_LONG).show();
            return;
        }
        colors.add(colrs);
        nameDzp.add(name.toString());

        //发送handler
        handler.sendEmptyMessage(2);

    }


    /**
     * 动画结束删除数据
     **/
    public void initClearEnd(final int num) {
        handler.postDelayed(new Runnable() {
            public void run() {
                //添加数据大于一  才能删除
                if (nameDzp.size() > 1) {
                    colors.remove((nameDzp.size() - num + 1) %
                            nameDzp.size());
                    nameDzp.remove((nameDzp.size() - num + 1) %
                            nameDzp.size());
                }

                if (views.size() > 1) {
                    rl_dzp.removeView(views.get((nameDzp.size() - num + 1) %
                            nameDzp.size()));

                    views.remove((nameDzp.size() - num + 1) %
                            nameDzp.size());
                }
                handler.sendEmptyMessage(1);
            }

        }, 4000);
    }


    /**
     * 没有开始删除数据
     **/
    public void initClearStart(int num) {
        //添加数据大于一  才能删除
        if (nameDzp.size() > 0) {
            if (nameDzp.size() == 1) {

                colors.remove(0);
                nameDzp.remove(0);
                rl_dzp.removeView(views.get(0));
                views.remove(0);

                handler.sendEmptyMessage(3);

            } else {
                if (nameDzp.size() > 1) {
                    colors.remove((nameDzp.size() - num + 1) %
                            nameDzp.size());
                    nameDzp.remove((nameDzp.size() - num + 1) %
                            nameDzp.size());
                }
                if (views.size() > 1) {
                    rl_dzp.removeView(views.get((nameDzp.size() - num + 1) %
                            nameDzp.size()));

                    views.remove((nameDzp.size() - num + 1) %
                            nameDzp.size());
                }
                handler.sendEmptyMessage(1);
            }
        }
    }


    /**
     * 启动转盘   设置选择哪个
     **/
    public void putLuckData(int type) {
        wheelSurfView.startRotate(type);
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

        animator.setDuration(nums * mVarTime);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                img_face.setBackgroundResource(R.drawable._2_weixin);

                //选中人的动画渐隐渐现动画
                img_face.setAlpha(1.0f);
                img_face.setAnimation(alphaAnimation);
                alphaAnimation.start();

                initClearEnd(num);
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


    //启动按钮更改图像
    public void setImageFace(int resId) {
        img_qidong.setImageResource(resId);
    }


    //启动按钮可以点击
    public void setEnaTrue(Boolean isFor) {
        img_qidong.setEnabled(isFor);
    }


    //启动按钮不可以点击
    public void setEnaFalse(Boolean isFor) {
        img_qidong.setEnabled(isFor);
    }

    //销毁handler
    public void dzpDismiss() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

}
