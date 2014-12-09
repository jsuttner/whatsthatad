package com.hasude.whatsthatad.fragments;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.hasude.whatsthatad.R;
import com.hasude.whatsthatad.SingleMenuActivity;
import com.hasude.whatsthatad.SinglePlayerActivity;
import com.hasude.whatsthatad.gameobjects.QuestionSinglePlayer;

public class SingleLevelFragment extends Fragment {

	private int level;
	private SingleMenuActivity sma;
	private ProgressBar progress;
	private TextView progressText;
	private boolean unlocked;

	public SingleLevelFragment(int lvl, SingleMenuActivity s) {
		super();
		this.level = lvl;
		this.sma = s;
		this.unlocked = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View levelView = inflater.inflate(R.layout.activity_single_level,
				container, false);

		// Set Progressbar with Levelinformation
		initLevelInfo(levelView);

		// Set all Thumbnails, ClickListeners and Tags with DatabaseIDs
		initImageBoxes(levelView);

		return levelView;
	}

	// Update level progress bar and information
	private void initLevelInfo(View levelView) {
		// set Progressbar
		progress = (ProgressBar) levelView.findViewById(R.id.singleProgressbar);
		Drawable barStyle = getResources().getDrawable(
				R.drawable.single_progressbar);
		progress.setProgressDrawable(barStyle);
		progress.setProgress(0);
		// set unlock next level border
		progress.setSecondaryProgress((int) ((float) 7 / (float) 12 * (float) 100));

		// set ProgressInfo
		progressText = (TextView) levelView.findViewById(R.id.progressText);
		progressText.setText("0/12 "+ getResources().getString(R.string.next_level_info));

	}

	// Update Level info
	private void updateLevelInfo() {
		int progressCount = 0;
		for (int i = 0; i < 12; i++) {
			if (sma.questionList.get(level * 12 + i).getSolved())
				progressCount++;
		}
		progress.setProgress((int) ((float) progressCount / (float) 12 * (float) 100));

		progressText.setText(progressCount + "/12 " + getResources().getString(R.string.next_level_info));
	}

	// Initialize Imageboxes, set images in Boxes, tags and clicklistener
	private void initImageBoxes(View levelView) {

		checkLevelAvailablity();

		// Setup Imageboxes
		TableLayout levelTable = (TableLayout) levelView
				.findViewById(R.id.singleLevelTable);
		int count = levelTable.getChildCount();
		TableRow tableRow = null;
		for (int i = 0; i < count; i++) {
			tableRow = (TableRow) levelTable.getChildAt(i);
			// Iteration through Images
			int countRow = tableRow.getChildCount();
			ImageView image = null;
			for (int j = 0; j < countRow; j++) {

				// Set ClickListener for Image
				RelativeLayout rl = (RelativeLayout) tableRow.getChildAt(j);
				image = (ImageView) rl.getChildAt(0);

				// Set Identifier in Tag
				image.setTag("" + (j + (i * 3)));

				// If Level is unlocked, pictures and clicklisteners loading
				if (unlocked) {
					// Set Thumbnail
					Drawable d;
					try {
						InputStream inputStream = getActivity()
								.getContentResolver().openInputStream(
										sma.questionList.get(
												level * 12 + (j + (i * 3)))
												.getAdCensoredAsUri());
						d = Drawable.createFromStream(inputStream,
								sma.questionList
										.get(level * 12 + (j + (i * 3)))
										.getAdCensoredAsUri().toString());
					} catch (FileNotFoundException e) {
						d = getResources().getDrawable(R.drawable.face);
						System.out.println("Bild: "
								+ sma.questionList
										.get(level * 12 + (j + (i * 3)))
										.getAdCensoredAsUri().getPath());
					}
					Bitmap bm = convertToBitmap(d, 150, 150);

					// Make Checkimage visible if solved
					if (sma.questionList.get(level * 12 + (j + (i * 3)))
							.getSolved()) {
						ImageView check = (ImageView) rl.getChildAt(1);
						check.setVisibility(0);
					} else {
						bm = toGrayscale(bm);
					}
					image.setImageBitmap(bm);

					// Set Clicklistener
					image.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							int questionID = Integer.parseInt((String) v
									.getTag());

							List<QuestionSinglePlayer> questionList = sma.questionList;

							// start single player menu
							Intent i = new Intent(getActivity(),
									SinglePlayerActivity.class);
							i.putExtra("question",
									questionList.get(level * 12 + questionID));
							startActivity(i);
							getActivity().finish();

						}
					});
				} else {
					// Set Thumbnail
					Drawable d;
					d = getResources().getDrawable(R.drawable.lock);
					Bitmap bm = convertToBitmap(d, 200, 200);
					image.setImageBitmap(bm);
				}
			}
		}

		updateLevelInfo();
	}

	// Grayscale for unsolved Questions
	private Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	// Check if enough question solved in level before
	private void checkLevelAvailablity() {
		if (level == 0) {
			unlocked = true;
		} else {
			int solvedCount = 0;
			for (int i = 0; i < 12; i++) {
				if (sma.questionList.get((level - 1) * 12 + i).getSolved())
					solvedCount++;
			}
			if (solvedCount > 6)
				unlocked = true;
		}
	}

	// Convert drawable to Bitmap
	public Bitmap convertToBitmap(Drawable drawable, int widthPixels,
			int heightPixels) {
		Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(mutableBitmap);
		drawable.setBounds(0, 0, widthPixels, heightPixels);
		drawable.draw(canvas);

		return mutableBitmap;
	}
}
