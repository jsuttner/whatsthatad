package com.hasude.whatsthatad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class DrawingView extends View{
	
	private Context context;
	
	//drawing path
	private Path drawPath;
	
	private Path lastPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//
	private static LinearLayout.LayoutParams params;
	//initial color
	private int paintColor = 0xFF660000;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;
	// logo bitmap
	private Bitmap logoBm;
	private float logoX;
	private float logoY;
	
	private float brushSize, lastBrushSize;
	
	private boolean erase=false;
	
	public DrawingView(Context context, AttributeSet attrs, Bitmap bm){
	    super(context, attrs);
	    canvasBitmap = bm;
	    this.context = context;
	    setupDrawing();
	}
	
	private void setupDrawing(){
		drawPath = new Path();
		drawPaint = new Paint();
		
		drawPaint.setColor(Color.BLACK);
		drawPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.INNER));
		
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(40);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = canvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
		drawCanvas = new Canvas(canvasBitmap);
		
	}
	
	 @Override
     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         // Compute the height required to render the view
         // Assume Width will always be MATCH_PARENT.
         int width = canvasBitmap.getWidth();
         int height = canvasBitmap.getHeight(); // Since 3000 is bottom of last Rect to be drawn added and 50 for padding.
         setMeasuredDimension(width, height);
     }
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		if (logoBm != null)
			canvas.drawBitmap(logoBm, logoX, logoY, null);
		canvas.drawPath(drawPath, drawPaint);
	}
	
	public void initLogoBm(Bitmap bm, float left, float top) {
		if(logoBm == null) {
			logoBm = bm;
			logoX = left;
			logoY = left;
			invalidate();
		} else {
			translateLogoDims(left, top);
		}
	}
	
	public void updateBm(Bitmap bm) {
		logoBm = bm;
	}
	
	public boolean touchDraw(float touchX, float touchY, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		    drawPath.moveTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_MOVE:
		    drawPath.lineTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_UP:
		    drawCanvas.drawPath(drawPath, drawPaint);
		    drawPath.reset();
		    break;
		default:
		    return false;
		}
		
		invalidate();
		return true;
	}

	public void translateLogoDims(float x, float y) {
		logoX = x;
		logoY = y;
		invalidate();
	}

}
