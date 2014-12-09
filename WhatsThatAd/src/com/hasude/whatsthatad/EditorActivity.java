package com.hasude.whatsthatad;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class EditorActivity extends Activity {

	// for uploaded images
	static final int REQUEST_IMAGE_CAPTURE = 1;
	// for photos
	static final int PHOTO_FROM_GALLERY = 2;
	private Uri photoUri;

	// view to draw on
	private DrawingView draw;

	// Bitmaps to pass to the next activity
	private Bitmap cencored;
	private Bitmap uncencored;

	// to ensure correct scolling
	private float mx, my;
	private float curX, curY;
	private int difX, difY;
	private int mode = 0;

	// stuff for scaling the logo
	private float mScaleFactor = 1.0f;
	private ScaleGestureDetector mScaleDetector;

	// touch is only allowed when image is loaded
	private boolean isLoaded = false;

	private ScrollView vScroll;
	private HorizontalScrollView hScroll;
	private LinearLayout btnList;
	private Point size;

	// list of all the buttons which need active background
	private ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();

	// Bitmap for the logo
	Bitmap bm;
	Bitmap scaledBm;
	private float bmX = 200;
	private float bmY = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		btnList = (LinearLayout) findViewById(R.id.button_list);

		// get display size to know when not to add sth to dif anymore
		Display display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);

		// get logo
		bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_grey);
		
		// init gesture detector
		mScaleDetector = new ScaleGestureDetector(getApplicationContext(),
				new ScaleListener());

		// add all the right buttons to the list
		buttons.add((ImageButton) findViewById(R.id.scrollBtn));
		buttons.add((ImageButton) findViewById(R.id.brushBtn));
		buttons.add((ImageButton) findViewById(R.id.logoBtn));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.editor_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * Creates a temporary file and returns it
	 */
	private File createTemporaryFile(String part, String ext) throws Exception {
		File tempDir = Environment.getExternalStorageDirectory();
		tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
		if (!tempDir.exists()) {
			tempDir.mkdir();
		}
		return File.createTempFile(part, ext, tempDir);
	}

	/**
	 * Grabs an image from the specified uri
	 * @param uri - Uri to the image to be grapped
	 * @return - Image as Bitmap
	 */
	private Bitmap grabImage(Uri uri) {
		this.getContentResolver().notifyChange(uri, null);
		ContentResolver cr = this.getContentResolver();
		try {
			return android.provider.MediaStore.Images.Media.getBitmap(cr, uri);
		} catch (Exception e) {
			Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
			Log.d("PHOTO", "Failed to load", e);
			return null;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		// After taking a photo
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bitmap bm = grabImage(photoUri);
			uncencored = bm;
			loadDrawView(bm);
		// Image picked from gallery
		} else if (requestCode == PHOTO_FROM_GALLERY
				&& resultCode == Activity.RESULT_OK) {
			Uri selectedImage = intent.getData();
			Bitmap bm = grabImage(selectedImage);
			uncencored = bm;
			loadDrawView(bm);
		}
	}

	/**
	 * Loads Bitmap and creates the Drawing view accordingly
	 * @param bm - Bitmap which is the background of the canvas
	 */
	private void loadDrawView(Bitmap bm) {

		// get the parent element
		vScroll = (VScroll) findViewById(R.id.vScroll);

		// remove all views inside parent
		vScroll.removeAllViews();

		// create the draw view
		difX = 0;
		difY = 0;
		draw = new DrawingView(this, null, bm);

		// create horizontal scroll view
		hScroll = new HScroll(this);

		// add them correctly
		hScroll.addView(draw);
		vScroll.addView(hScroll);

		// set isLoaded to true, to avoid errors
		isLoaded = true;
	}

	/**
	 * Resizes a bitmap and returns it
	 * @param bm - Bitmap to be resized
	 * @param newHeight
	 * @param newWidth
	 * @return - Resized bitmap
	 */
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		// get right dimensions
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// matrix for manipulation
		Matrix matrix = new Matrix();
		// resize bitmap
		matrix.postScale(scaleWidth, scaleHeight);

		// create new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// only accept touch input, when image is loaded
		if (isLoaded) {
			// if not in draw mode, let user scroll
			if (mode == 0) {
				switch (event.getAction()) {
				// when finger goes down get the position of it
				case MotionEvent.ACTION_DOWN:
					mx = event.getX();
//					Log.d("ED", "MX is: " + mx);
					my = event.getY();
//					Log.d("ED", "MY is: " + my);
					break;
				// when the finger moves, get the position and scroll the view 
				// by the difference of old points and new ones
				case MotionEvent.ACTION_MOVE:
					curX = event.getX();
//					Log.d("ED", "CurX is: " + curX);
//					Log.d("ED", "DifX here is: " + (mx - curX));
					curY = event.getY();
//					Log.d("ED", "CurY is: " + curY);
//					Log.d("ED", "DifY here is: " + (my - curY));
					vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
					hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
//					Log.d("ED", "Width: " + draw.getWidth());

					// if transition isnt to big for screen stop scrolling / remember the value
					if (difX + (int) (mx - curX) > 0
							&& difX + (int) (mx - curX) < draw.getWidth()
									- size.x)
						difX += (int) (mx - curX);
					if (difY + (int) (mx - curX) > 0
							&& difY + (int) (mx - curX) < draw.getHeight()
									- size.y)
						difY += (int) (my - curY);

					mx = curX;
					my = curY;
					break;
				// same as move
				case MotionEvent.ACTION_UP:
					curX = event.getX();
					curY = event.getY();
					vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
					hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
					break;
				}
			// else let him draw
			} else if (mode == 1) {
//				Log.d("ED", "X: " + event.getX() + " Y: " + event.getY());
//				Log.d("ED", "DifX: " + difX);
//				Log.d("ED", "DifY: " + difY);
				draw.touchDraw(event.getX() + difX,
						event.getY() - (btnList.getHeight() + 20) + difY, event);
			// else let user place logo
			} else if (mode == 2) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					scaledBm = getResizedBitmap(bm, (int) bmX, (int) bmY);
					draw.initLogoBm(scaledBm,
							event.getX() + difX - scaledBm.getWidth() / 2,
							event.getY() - (btnList.getHeight() + 20) + difY
									- scaledBm.getHeight() / 2);
					break;
				// let Gesturedetector also watch the event
				case MotionEvent.ACTION_MOVE:
					mScaleDetector.onTouchEvent(event);
					scaledBm = getResizedBitmap(bm, (int) bmX, (int) bmY);
					draw.updateBm(scaledBm);
					// Log.d("ED", "BM WIDTH: " + scaledBm.getWidth());
					draw.initLogoBm(scaledBm,
							event.getX() + difX - scaledBm.getWidth() / 2,
							event.getY() - (btnList.getHeight() + 20) + difY
									- scaledBm.getHeight() / 2);
					break;

				}
			} else {
				// TODO: error
			}
			return true;
		} else
			// TODO: error
			return false;
	}

	/**
	 * Gets fired when clicked on the scroll button
	 * @param v
	 */
	public void scrollBtnListener(View v) {
		mode = 0;
		for (ImageButton b : buttons) {
			b.setBackgroundColor(getResources().getColor(R.color.transparent));
		}
		buttons.get(mode).setBackgroundColor(
				getResources().getColor(R.color.wta_blue));
	}

	/**
	 * Gets fired when clicked on the brush button
	 * @param v
	 */
	public void brushBtnListener(View v) {
		mode = 1;
		for (ImageButton b : buttons) {
			b.setBackgroundColor(getResources().getColor(R.color.transparent));
		}
		buttons.get(mode).setBackgroundColor(
				getResources().getColor(R.color.wta_blue));
	}

	/**
	 * Gets fired when clicked on the logo button
	 * @param v
	 */
	public void logoBtnListener(View v) {
		mode = 2;
		for (ImageButton b : buttons) {
			b.setBackgroundColor(getResources().getColor(R.color.transparent));
		}
		buttons.get(mode).setBackgroundColor(
				getResources().getColor(R.color.wta_blue));
	}

	/**
	 * Gets fired when clicked on the upload photo button
	 * @param v
	 */
	public void uploadBtnListener(View v) {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		startActivityForResult(i, PHOTO_FROM_GALLERY);

		for (ImageButton b : buttons)
			b.setBackgroundColor(getResources().getColor(R.color.transparent));
		buttons.get(mode).setBackgroundColor(
				getResources().getColor(R.color.wta_blue));
	}

	/**
	 * Gets fired when clicked on the camera button
	 * @param v
	 */
	public void photoBtnListener(View v) {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File photo;
		try {
			photo = createTemporaryFile("addPhoto", ".jpg");
			photo.delete();
		} catch (Exception e) {
			Log.v("PHOTO", "Can't create file to take picture!");
			Toast.makeText(this,
					"Please check SD card! Image shot is impossible!",
					Toast.LENGTH_SHORT).show();
			return;
		}
		photoUri = Uri.fromFile(photo);
		i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		// start camera intent
		startActivityForResult(i, REQUEST_IMAGE_CAPTURE);

		for (ImageButton b : buttons)
			b.setBackgroundColor(getResources().getColor(R.color.transparent));
		buttons.get(mode).setBackgroundColor(
				getResources().getColor(R.color.wta_blue));
		
	}

	public void nextBtnListener(View v) {
		// TODO: save canvas in bitmap

		// start activity to set details
		Intent i = new Intent(getApplicationContext(),
				QuestionUploadActivity.class);
		// i.putExtra("cencored", cencored);
		// i.putExtra("uncencored", uncencored);
		startActivity(i);
		finish();
	}

	/**
	 * Detects scale gestures
	 */
	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor(); // scale change since
														// previous event
			// restict it, so that the scale is not too fast
			if (mScaleFactor > 1.05f)
				mScaleFactor = 1.05f;
			else if (mScaleFactor < 0.95f)
				mScaleFactor = 0.95f;

			// dont let it get to small
			// TODO: dont let it get too big => out of memory
			if (bmX * mScaleFactor > 100)
				bmX *= mScaleFactor;
			if (bmY * mScaleFactor > 100)
				bmY *= mScaleFactor;
			return true; // indicate event was handled
		}
	}

}
