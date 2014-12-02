package com.hasude.whatsthatad;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

public class EditorActivity extends Activity{
	
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private Uri mImageUri;
	
	private DrawingView drawView;
	
	private SurfaceView surfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		
//		LinearLayout editorLayout = (LinearLayout)findViewById(R.id.editorLayout);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, 300, 0);
//		editorLayout.setLayoutParams(params);
		
		drawView = (DrawingView)findViewById(R.id.drawing);
		

		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.editor_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	private void dispatchTakePictureIntent() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    File photo;
	    try {
	        // place where to store camera taken picture
	        photo = this.createTemporaryFile("picture", ".jpg");
	        photo.delete();
	    } catch(Exception e){
	        Log.v("PHOTO", "Can't create file to take picture!");
	        Toast.makeText(this, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
	        return;
	    }
	    mImageUri = Uri.fromFile(photo);
	    i.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
	    //start camera intent
	    startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
	}

	private File createTemporaryFile(String part, String ext) throws Exception {
	    File tempDir= Environment.getExternalStorageDirectory();
	    tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
	    if(!tempDir.exists()) {
	        tempDir.mkdir();
	    }
	    return File.createTempFile(part, ext, tempDir);
	}
	
	public void grabImage(ImageView imageView) {
	    this.getContentResolver().notifyChange(mImageUri, null);
	    ContentResolver cr = this.getContentResolver();
	    Bitmap bitmap;
	    try {
	        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
	        imageView.setImageBitmap(bitmap);
	    }
	    catch (Exception e) {
	        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
	        Log.d("PHOTO", "Failed to load", e);
	    }
	}


	//called after camera intent finished
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
	    if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK) {
	       //this.grabImage(imageView);
	    }
	    super.onActivityResult(requestCode, resultCode, intent);
	}

}
