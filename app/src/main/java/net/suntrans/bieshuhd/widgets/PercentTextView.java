package net.suntrans.bieshuhd.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import net.suntrans.bieshuhd.R;


/**
 * Created by Looney on 2018/2/6.
 * Des:
 */

public class PercentTextView extends AppCompatTextView {
    private static final String TAG = PercentTextView.class.getSimpleName();
    private static int baseScreenHeight = 1920;
    private static int txHeight = 144;
    private float mTextSizePercent = 1f;

    public PercentTextView(Context context) {
        super(context);
        setTextSize(getTextSize());
    }

    public PercentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        setTextSize(getTextSize());
    }

    public PercentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        setTextSize(getTextSize());
        this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                removeOnLayoutChangeListener(this);
                mTextSizePercent = txHeight / getMeasuredHeight();
                setTextSize(getTextSize());
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }




    @Override
    public void setTextSize(int unit, float size) {
        size = (int)(size * mTextSizePercent);
        super.setTextSize(unit, size);
    }

    @Override
    public void setPaintFlags(int flags) {
        super.setPaintFlags(flags);
    }

    @Override
    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
    }

    public float getTextSizePercent() {
        return mTextSizePercent;
    }

    public void setTextSizePercent(int unit, float textSizePercent) {
        mTextSizePercent = textSizePercent;
        setTextSize(unit, getTextSize());
    }

    public void setTextSizePercent(float textSizePercent) {
        mTextSizePercent = textSizePercent;
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize());
    }

    /**
     * 得到自定义的属性值
     *
     * @param context
     * @param attrs
     */
    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PercentTextView);
        txHeight = ta.getInt(R.styleable.PercentTextView_baseScreenHeight, baseScreenHeight);
        ta.recycle();
    }



    /**
     * 获取当前设备屏幕的宽高
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

}
