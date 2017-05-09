package com.miuhouse.zxcommunity.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/** 自动两端对齐的TextView，对齐根据TextView的宽度，宽度提前在width设定好
 *  只对中文适用！！！！！！！！
 * Created by khb on 2016/4/26.
 */
public class JustifyTextView extends TextView {

    private float textSize;
    private float textLineHeight;
    private int top;
    private int y;
    private int lines;
    private int bottom;
    private int right;
    private int left;
    private int lineDrawWords;
    private char[] textCharArray;
    private float singleWordWidth;

    private float lineSpacingExtra;

    private boolean first = true;

    public JustifyTextView(Context context) {
        this(context, null);
    }

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                initTextInfo();
                return true;
            }
        });
    }

    public JustifyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initTextInfo() {
//        int width = getWidth();
//        textSize = getTextSize();
//        textLineHeight = getLineHeight();
//        left = 0;
//        right = getRight();
//        y = getTop();
//        // 要画的宽度
//        int drawTotalWidth = right - left;
//        String text = getText().toString();
//        if (!TextUtils.isEmpty(text) && first) {
//            textCharArray = text.toCharArray();
//            TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//            mTextPaint.density = getResources().getDisplayMetrics().density;
//            mTextPaint.setTextSize(textSize);
//            // 一个单词的的宽度
//            singleWordWidth = mTextPaint.measureText("一") + lineSpacingExtra;
//            // 一行可以放多少个字符
//            lineDrawWords = (int) (drawTotalWidth / singleWordWidth);
//            int length = textCharArray.length;
//            int textActualWidth = drawTotalWidth / length;
//            lines = length / lineDrawWords;
//            if ((length % lineDrawWords) > 0) {
//                lines = lines + 1;
//            }
//            first = false;
//            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
//            int totalHeight = (int) (lines*textLineHeight + getPaddingBottom()+getPaddingTop()+layoutParams.bottomMargin+layoutParams.topMargin);
//            setHeight(totalHeight);
//        }
    }

    int maxLine;

    public void setMaxLines(int max){
        this.maxLine = max;
        this.maxLine = 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        height = getHeight();
        textSize = getTextSize();
        textLineHeight = getLineHeight();
        left = 0;
        right = getRight();
        y = 0;
        // 一个单词的的宽度
        TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = getResources().getDisplayMetrics().density;
        mTextPaint.setColor(getCurrentTextColor());
        mTextPaint.setTextSize(textSize);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        singleWordWidth = mTextPaint.measureText("一") + lineSpacingExtra;
        // 要 画的宽度
        int drawTotalWidth = right - left;
        int padding = getPaddingRight() + getPaddingLeft();
        int verticalPaddingOffset = getTopPaddingOffset() + getBottomPaddingOffset();
        int verticalPadding = getPaddingTop() + getPaddingBottom();

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        int margin = layoutParams.rightMargin + layoutParams.leftMargin;

        String text = getText().toString();
        char[] textCharArray = text.toCharArray();
        float horizontalSpacing = ((width - singleWordWidth) / (textCharArray.length - 1)) - singleWordWidth;
//        float baseline = getBaseline() - ((-fontMetrics.top - (fontMetrics.bottom - fontMetrics.top)/2)
//                                            - (-fontMetrics.ascent - (fontMetrics.descent - fontMetrics.ascent)/2));
//        baseline = height / 2 - fontMetrics.descent + (fontMetrics.bottom - fontMetrics.top) / 2;
        float baseline = (fontMetrics.bottom - fontMetrics.top) - fontMetrics.descent;
        bottom = getBottom();
        int drawTotalLine = lines;

        if(maxLine!=0&&drawTotalLine>maxLine){
            drawTotalLine = maxLine;
        }
        for (int i = 0;i < textCharArray.length; i++){
//            if (i == 0){
                canvas.drawText(String.valueOf(textCharArray[i]), left, baseline, mTextPaint);
//            }else {
//            }
            left += horizontalSpacing + singleWordWidth;
//            setGravity(Gravity.CENTER_VERTICAL);
        }


//        for (int i = 0; i < drawTotalLine; i++) {
//            try {
//                int length = this.textCharArray.length;
//                int mLeft = left;
//                // 第i+1行开始的字符index
//                int startIndex = (i * 1) * lineDrawWords;
//                // 第i+1行结束的字符index
//                int endTextIndex = startIndex + lineDrawWords;
//                if (endTextIndex > length) {
//                    endTextIndex = length;
//                    y += textLineHeight;
//                } else {
//                    y += textLineHeight;
//                }
//                for (; startIndex < endTextIndex; startIndex++) {
//                    char c = this.textCharArray[startIndex];
////         if (c == ' ') {
////           c = '\u3000';
////         } else if (c < '\177') {
////           c = (char) (c + 65248);
////         }
//                    canvas.drawText(String.valueOf(c), mLeft, y, getPaint());
//                    mLeft += singleWordWidth;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        super.onDraw(canvas);
    }

    public void setLineSpacingExtra(int lineSpacingExtra){
        this.lineSpacingExtra = lineSpacingExtra;
    }

    /**
     * 判断是否为中文
     * @return
     */
    public static boolean containChinese(String string){
        boolean flag = false;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if ((c >= 0x4e00) && (c <= 0x9FA5)) {
                flag = true;
            }
        }
        return flag;
    }


    public static String ToDBC(String input) {
        // 导致TextView异常换行的原因：安卓默认数字、字母不能为第一行以后每行的开头字符，因为数字、字母为半角字符
        // 所以我们只需要将半角字符转换为全角字符即可
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }
}
