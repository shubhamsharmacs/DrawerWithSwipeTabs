package com.arcasolutions.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.weedfinder.R;
import com.arcasolutions.graphics.Polygon;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class DrawView extends View {

    public interface OnDrawListener extends Serializable {
        void onDrawDone(List<Point> points, Bitmap bitmap);
    }

    private static final float TOUCH_TOLERANCE = 4;
    private float mX;
    private float mY;

    private int mFillColor;
    private int mStrokeColor;

    private OnDrawListener mListener;
    private boolean hasPolygon = false;

    private final List<Point> mPoints = Lists.newArrayList();

    public DrawView(Context context) {
        this(context, null, 0);
    }

    public DrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        Resources res = getResources();
        mFillColor = res.getColor(R.color.map_draw_fill);
        mStrokeColor = res.getColor(R.color.map_draw_stroke);
        setBackgroundColor(Color.TRANSPARENT);
        setClickable(true);
        setDrawingCacheEnabled(true);
    }

    public void setOnDrawListener(OnDrawListener listener) {
        mListener = listener;
    }

    @Override
    public void draw(Canvas canvas) {
        int size = mPoints.size();
        if (size > 1) {
            Polygon mPolygon = new Polygon(size);
            for (Point p : mPoints) mPolygon.addPoint(p.x, p.y);
            mPolygon.close();

            // filling the polygon
            Paint mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mFillColor);
            canvas.drawPath(mPolygon, mPaint);

            // drawing stroke
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mStrokeColor);
            mPaint.setStrokeWidth(4);
            canvas.drawPath(mPolygon, mPaint);

            if (hasPolygon && mListener != null) {
                mListener.onDrawDone(Lists.newArrayList(mPoints), getDrawingCache(true));
                mPoints.clear();
            }

        } else {
            super.draw(canvas);
        }
    }

    public void clear() {
        hasPolygon = false;
        mPoints.clear();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;

            case MotionEvent.ACTION_UP:
                touchUp();
                break;
        }
        return true;
    }

    private void touchStart(float x, float y) {
        mPoints.clear();
        mPoints.add(new Point((int) x, (int) y));
        hasPolygon = false;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mX = x;
            mY = y;
            mPoints.add(new Point((int) mX, (int) mY));
        }
        invalidate();
    }

    private void touchUp() {
        if (mPoints.size() > 3) {
            mPoints.add(mPoints.get(0));
        }
        hasPolygon = true;
        invalidate();
    }

}
