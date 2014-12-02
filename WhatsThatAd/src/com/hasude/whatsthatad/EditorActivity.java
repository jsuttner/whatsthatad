package com.hasude.whatsthatad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.view.SurfaceView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class EditorActivity extends Activity {

	// for uploaded images
	static final int REQUEST_IMAGE_CAPTURE = 1;
	// for photos
	static final int PHOTO_FROM_GALLERY = 2;
	private Uri photoUri;

	// view to draw on
	private DrawingView draw;

	private Bitmap cencored;
	private Bitmap uncencored;
	private Uri canvasUri;

	// to ensure correct scolling
	private float mx, my;
	private float curX, curY;
	private int difX, difY;
	private int mode = 0;

	private float mScaleFactor = 1.0f;
	private ScaleGestureDetector mScaleDetector;

	// touch is only allowed when image is loaded
	private boolean isLoaded = false;

	private ScrollView vScroll;
	private HorizontalScrollView hScroll;
	private LinearLayout btnList;
	private Point size;

	private ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();

	Bitmap bm;
	Bitmap scaledBm;
	private float bmX = 200;
	private float bmY = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		btnList = (LinearLayout) findViewById(R.id.button_list);

		Display display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);

		bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo);
		mScaleDetector = new ScaleGestureDetector(getApplicationContext(),
				new ScaleListener());

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

	private File createTemporaryFile(String part, String ext) throws Exception {
		File tempDir = Environment.getExternalStorageDirectory();
		tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
		if (!tempDir.exists()) {
			tempDir.mkdir();
		}
		return File.createTempFile(part, ext, tempDir);
	}

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

	// called after camera intent finished
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bitmap bm = grabImage(photoUri);
			uncencored = bm;
			loadDrawView(bm);
			// Image picked from gallery
		} else if (requestCode == PHOTO_FROM_GALLERY
				&& resultCode == Activity.RESULT_OK) {
			Uri selectedImage = intent.getData();
			try {
				Bitmap bm = MediaStore.Images.Media.getBitmap(
						this.getContentResolver(), selectedImage);
				uncencored = bm;
				loadDrawView(bm);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

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

		// set is loaded to true, to avoid errors
		isLoaded = true;
	}

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
				case MotionEvent.ACTION_DOWN:
					mx = event.getX();
					Log.d("ED", "MX is: " + mx);
					my = event.getY();
					Log.d("ED", "MY is: " + my);
					break;
				case MotionEvent.ACTION_MOVE:
					curX = event.getX();
					Log.d("ED", "CurX is: " + curX);
					Log.d("ED", "DifX here is: " + (mx - curX));
					curY = event.getY();
					Log.d("ED", "CurY is: " + curY);
					Log.d("ED", "DifY here is: " + (my - curY));
					vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
					hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
					Log.d("ED", "Width: " + draw.getWidth());

					// if transition isnt to big for screen remember the value
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
				case MotionEvent.ACTION_UP:
					curX = event.getX();
					curY = event.getY();
					vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
					hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
					break;
				}
				// else let him draw
			} else if (mode == 1) {
				Log.d("ED", "X: " + event.getX() + " Y: " + event.getY());
				Log.d("ED", "DifX: " + difX);
				Log.d("ED", "DifY: " + difY);
				draw.touchDraw(event.getX() + difX,
						event.getY() - (btnList.getHeight() + 20) + difY, event);
			} else if (mode == 2) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					scaledBm = getResizedBitmap(bm, (int) bmX, (int) bmY);
					draw.initLogoBm(scaledBm,
							event.getX() + difX - scaledBm.getWidth() / 2,
							event.getY() - (btnList.getHeight() + 20) + difY
									- scaledBm.getHeight() / 2);
					break;
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
			return false;
	}

	public void scrollBtnListener(View v) {
		mode = 0;
		for (ImageButton b : buttons) {
			b.setBackgroundColor(getResources().getColor(R.color.transparent));
		}
		buttons.get(mode).setBackgroundColor(
				getResources().getColor(R.color.wta_blue));
	}

	public void brushBtnListener(View v) {
		mode = 1;
		for (ImageButton b : buttons) {
			b.setBackgroundColor(getResources().getColor(R.color.transparent));
		}
		buttons.get(mode).setBackgroundColor(
				getResources().getColor(R.color.wta_blue));
	}

	public void logoBtnListener(View v) {
		mode = 2;
		for (ImageButton b : buttons) {
			b.setBackgroundColor(getResources().getColor(R.color.transparent));
		}
		buttons.get(mode).setBackgroundColor(
				getResources().getColor(R.color.wta_blue));
	}

	public void uploadBtnListener(View v) {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		startActivityForResult(i, PHOTO_FROM_GALLERY);

		for (ImageButton b : buttons)
			b.setBackgroundColor(getResources().getColor(R.color.transparent));
	}

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

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor(); // scale change since
														// previous event
			if (mScaleFactor > 1.05f)
				mScaleFactor = 1.05f;
			else if (mScaleFactor < 0.95f)
				mScaleFactor = 0.95f;

			if (bmX * mScaleFactor > 100)
				bmX *= mScaleFactor;
			if (bmY * mScaleFactor > 100)
				bmY *= mScaleFactor;
			return true; // indicate event was handled
		}
	}

}
