package vn.manroid.opencv.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import java.util.List;

public class DrawingView extends View {

    Paint drawingPaint;
    List<Rect> lstRectArea;

    public DrawingView(Context context) {
        super(context);
        drawingPaint = new Paint();
        drawingPaint.setColor(Color.GREEN);
        drawingPaint.setStyle(Paint.Style.STROKE);
        drawingPaint.setStrokeWidth(5);
    }

    public void setListRect(List<Rect> lstRect) {
        this.lstRectArea = lstRect;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (lstRectArea != null && lstRectArea.size() > 0) {
            for (Rect rect : lstRectArea) {
                canvas.drawRect(rect.left * 1.0F, rect.top * 1.0F, rect.right * 1.0F, rect.bottom * 1.0F,
                        drawingPaint);
            }
        }
    }

}
