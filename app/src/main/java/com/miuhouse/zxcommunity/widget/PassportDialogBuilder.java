package com.miuhouse.zxcommunity.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.FinalActivity;
import com.miuhouse.zxcommunity.activity.user.UserInfoActivity;
import com.miuhouse.zxcommunity.bean.UserBean;
import com.nineoldandroids.view.ViewHelper;

/**
 * 自定义对话框
 * Created by kings on 4/15/2016.
 */
public class PassportDialogBuilder extends Dialog implements DialogInterface {

    private final String defTextColor = "#FFFFFFFF";

    private final String defDividerColor = "#11000000";

    private final String defMsgColor = "#FFFFFFFF";

    private final String defDialogColor = "#FFE74C3C";

    private static Context tmpContext;

    private LinearLayout mLinearLayoutView;

    private CardView mCardView;

    private View mDialogView;

//
    private ImageView roundedCancel;
    //
    private Button btnExperience;

    private int mDuration = -1;

    private static int mOrientation = 1;

    private boolean isCancelable = true;

    private static PassportDialogBuilder instance;

    public PassportDialogBuilder(Context context) {
        super(context);
        init(context);
    }

    public PassportDialogBuilder(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected PassportDialogBuilder(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    public static PassportDialogBuilder getInstance(Context context) {

        if (instance == null || !tmpContext.equals(context)) {
            synchronized (PassportDialogBuilder.class) {
                if (instance == null || !tmpContext.equals(context)) {
                    instance = new PassportDialogBuilder(context, R.style.dialog_untran);
                }
            }
        }
        tmpContext = context;
        return instance;

    }

    private void init(Context context) {
        mDialogView = View.inflate(context, R.layout.item_popup_checking, null);
//        mLinearLayoutView = (LinearLayout) mDialogView.findViewById(R.id.parentPanel);
        mCardView = (CardView) mDialogView.findViewById(R.id.main);
        btnExperience = (Button) mDialogView.findViewById(R.id.btn_experience);
        roundedCancel = (ImageView) mDialogView.findViewById(R.id.rounded_cancel);
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                start();
            }
        });
        setContentView(mDialogView);

    }


    public PassportDialogBuilder setButton1Click(View.OnClickListener click) {
        roundedCancel.setOnClickListener(click);
        return this;
    }

    //
    public PassportDialogBuilder setButton2Click(View.OnClickListener click) {
        btnExperience.setOnClickListener(click);
        return this;
    }

    private void start() {
        FadeIn animator = new FadeIn();
        if (mDuration != -1) {
            animator.setDuration(Math.abs(mDuration));
        }
        animator.start(mCardView);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    /**
     * 判断用户是否通过审核，没有显示dialog
     *
     * @param ownerStatus
     */
    public static void isShowDialog(int ownerStatus, final FinalActivity mContext) {
        if (ownerStatus != UserBean.CHECKED) {
            final PassportDialogBuilder dialog = PassportDialogBuilder.getInstance(mContext);
            dialog.setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    mContext.finish();
                }
            }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(UserInfoActivity.class);
                }
            });
            //点击空白不关闭dialog
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    class FadeIn {
        private static final int DURATION = 1 * 700;

        protected long mDuration = DURATION;

        private AnimatorSet mAnimatorSet;

        {
            mAnimatorSet = new AnimatorSet();
        }

        public void start(View view) {
            reset(view);
            setupAnimation(view);
            mAnimatorSet.start();
        }

        public void reset(View view) {
            ViewHelper.setPivotX(view, view.getMeasuredWidth() / 2.0f);
            ViewHelper.setPivotY(view, view.getMeasuredHeight() / 2.0f);
        }


        public AnimatorSet getAnimatorSet() {
            return mAnimatorSet;
        }

        public void setDuration(long duration) {
            this.mDuration = duration;
        }

        protected void setupAnimation(View view) {
            getAnimatorSet().playTogether(
                    ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration)

            );
        }
    }
}
