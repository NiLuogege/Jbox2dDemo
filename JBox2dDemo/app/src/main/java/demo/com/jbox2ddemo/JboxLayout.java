package demo.com.jbox2ddemo;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by ${LuoChen} on 2017/7/27 15:30.
 * email:luochen0519@foxmail.com
 */

public class JboxLayout extends FrameLayout {

    private JboxImpl jboxImpl;

    public JboxLayout(@NonNull Context context) {
        this(context, null);
    }

    public JboxLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JboxLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);//强制Viewgroup进行绘制
        initView();
    }

    private void initView() {
        //创建一个JboxImpl
        float density = getResources().getDisplayMetrics().density;
        jboxImpl = new JboxImpl(density);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        jboxImpl.setSize(w, h);//将View的大小传入

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        jboxImpl.createWorld();
        //子viwe创建tag 设置body
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view != null) {

                if (!jboxImpl.isBodyView(view) || changed) {
                    jboxImpl.creatBody(view);//去创建小球对应的刚体
                }
//                    Object tag = view.getTag(R.id.jbox_body_view);
//                    if(tag==null){//说明使我们添加的小球
//                        jboxImpl.creatBody(view);//去创建小球对应的刚体
//                    }
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        jboxImpl.startWorld();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view != null) {
                if (jboxImpl.isBodyView(view)) {
                    float viewX = jboxImpl.getViewX(view);
                    float viewY = jboxImpl.getViewY(view);
                    view.setX(viewX);
                    view.setY(viewY);
                }
            }
        }

        invalidate();
    }

    public void onSensorChanged(float x, float y) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (jboxImpl.isBodyView(view)) {
                jboxImpl.applyLinearImpulse(x,y,view);
            }
//            jboxImpl.applyLinearImpulse(x, y, view);
        }
    }
}
