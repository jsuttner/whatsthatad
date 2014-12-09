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

public class DrawingView extends View{
	
	private Context context;	
	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//
	private static LinearLayout.LayoutParams params;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;
	// logo bitmap
	private Bitmap logoBm;
	private float logoX;
	private float logoY;
	
	public DrawingView(Context context, AttributeSet attrs, Bitmap bm){
	    super(context, attrs);
	    canvasBitmap = bm;
	    this.context = context;
	    setupDrawing();
	}
	
	/**
	 * Initialises the brush drawing
	 */
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
         // Set the dimensions of this view to the bitmap's dimensions
         int width = canvasBitmap.getWidth();
         int height = canvasBitmap.getHeight();
         setMeasuredDimension(width, height);
    }
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		// only draw logo when its set
		if (logoBm != null)
			canvas.drawBitmap(logoBm, logoX, logoY, null);
		canvas.drawPath(drawPath, drawPaint);
	}
	
	/**
	 * Initialises the bitmap, if already set it just updates its positions
	 * @param bm - Bm to be initialised
	 * @param left - position left (x)
	 * @param top - position top (y)
	 */
	public void initLogoBm(Bitmap bm, float left, float top) {
		if(logoBm.equals(bm)) {
			logoBm = bm;
			logoX = left;
			logoY = left;
			invalidate();
		} else {
			translateLogoDims(left, top);
		}
	}
	
	/**
	 * Updates the bitmap
	 * @param bm - New bitmap to be shown on canvas
	 */
	public void updateBm(Bitmap bm) {
		logoBm = bm;
	}
	
	/**
	 * Lets user draw on view
	 * @param touchX - X Position to be drawn
	 * @param touchY - Y Position to be drawn
	 * @param event - Touchevent (MotionEvent)
	 * @return
	 */
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

	/**
	 * Updates Bitmaps dimensions
	 * @param x - X Position of Bm
	 * @param y - Y Position of Bm
	 */
	public void translateLogoDims(float x, float y) {
		logoX = x;
		logoY = y;
		invalidate();
	}

}
