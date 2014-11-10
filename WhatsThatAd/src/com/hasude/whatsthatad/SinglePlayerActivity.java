package com.hasude.whatsthatad;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.hasude.whatsthatad.exceptions.CorrectAnswerException;
import com.hasude.whatsthatad.gameobjects.QuestionSinglePlayer;

public class SinglePlayerActivity extends Activity {

	QuestionSinglePlayer q;
	ImageView questionImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_player);

		// get all views
		EditText solutionEdit = (EditText) findViewById(R.id.SingleEditSolution);
		TextView questionTV = (TextView) findViewById(R.id.SingleQuestionTextView);
		questionImageView = (ImageView) findViewById(R.id.SingleQuestionImageView);

		// social media images
		ImageView facebook = (ImageView) findViewById(R.id.SingleBtnFacebook);
		ImageView google = (ImageView) findViewById(R.id.SingleBtnGoogle);
		ImageView twitter = (ImageView) findViewById(R.id.SingleBtnTwitter);

		// load question and image
		// TODO: for testing only
		String correctA = "Adidas";
		String censored = "android.resource://com.hasude.whatsthatad/drawable/adidas_censored";
		String uncensored = "android.resource://com.hasude.whatsthatad/drawable/adidas_uncensored";
		// Bitmap uncensored =
		// BitmapFactory.decodeResource(getApplicationContext().getResources(),
		// R.drawable.adidas_uncensored);
		try {
			q = new QuestionSinglePlayer(1, censored, uncensored, correctA,
					"Which company launched this commercial?");
		} catch (CorrectAnswerException e) {
			Log.d("WTA", "Correct answer was not a given answer possibility");
			e.printStackTrace();
		}

		questionTV.setText(q.getQuestion());
		questionImageView.setImageURI(q.getAdCensoredAsUri());
		;

		// handler that fires when user finished typing
		solutionEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH
						|| actionId == EditorInfo.IME_ACTION_DONE
						|| event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					if (!event.isShiftPressed()) {
						// the user is done typing.
						if (q.isAnswerCorrect(v.getText().toString())) {
							Toast.makeText(getApplicationContext(),
									"Yay, that was correct!",
									Toast.LENGTH_SHORT).show();
							endTask();
						} else {
							Toast.makeText(getApplicationContext(),
									"Oh no, that was not correct. Try again!",
									Toast.LENGTH_SHORT).show();
						}
						return true; // consume.
					}
				}
				return false; // pass on to other listeners.
			}
		});

		// onClick listeners social media
		facebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("image/*");
				shareIntent.putExtra(Intent.EXTRA_STREAM, q.getAdCensored()); // put
																				// your
																				// image
																				// URI

				PackageManager pm = v.getContext().getPackageManager();

				List<ResolveInfo> activityList = pm.queryIntentActivities(
						shareIntent, 0);
				for (final ResolveInfo app : activityList) {
					if ((app.activityInfo.name).contains("facebook")) {
						final ActivityInfo activity = app.activityInfo;
						final ComponentName name = new ComponentName(
								activity.applicationInfo.packageName,
								activity.name);

						shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
						shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						shareIntent.setComponent(name);

						v.getContext().startActivity(shareIntent);
						break;
					}
				}

			}
		});
		twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent
						.putExtra(
								android.content.Intent.EXTRA_TEXT,
								"I'm playing Whats that ad right now and I need you help. Which Compony ran this ad?");
				shareIntent.putExtra(Intent.EXTRA_STREAM, q.getAdCensored());

				PackageManager pm = v.getContext().getPackageManager();
				List<ResolveInfo> activityList = pm.queryIntentActivities(
						shareIntent, 0);
				for (final ResolveInfo app : activityList) {
					if ((app.activityInfo.name).contains("twitter")) {
						final ActivityInfo activity = app.activityInfo;
						final ComponentName name = new ComponentName(
								activity.applicationInfo.packageName,
								activity.name);

						shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
						shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						shareIntent.setComponent(name);

						v.getContext().startActivity(shareIntent);
						break;
					}
				}
			}
		});
		google.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent shareIntent = ShareCompat.IntentBuilder
						.from(SinglePlayerActivity.this)
						.setType("text/plain")
						.setText(
								"I'm playing Whats that ad right now and I need you help. Which Compony ran this ad?")
						.setType("image/jpeg")
						.setStream(q.getAdCensoredAsUri()).getIntent()
						.setPackage("com.google.android.apps.plus");
				startActivity(shareIntent);
			}
		});

	} // onCreate

	// TODO: Nicht gebraucht?
	// private Uri storeImage(Bitmap bm) {
	// FileOutputStream fileOutputStream = null;
	// File path =
	// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	// File file = new File(path, "test.jpg");
	// try {
	// fileOutputStream = new FileOutputStream(file);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }
	// BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
	// bm.compress(CompressFormat.JPEG, 10, bos);
	// try {
	// bos.flush();
	// bos.close();
	// fileOutputStream.flush();
	// fileOutputStream.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return Uri.parse("file://" + file.getAbsolutePath());
	// }

	private void endTask() {
		questionImageView.setImageURI(q.getAdUncensoredAsUri());
		LinearLayout page = (LinearLayout) findViewById(R.id.SingleLayout);
		page.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// TODO: hier k�nnte sp�ter die jeweilige Frage geladen werden (wenn die
	// nicht direkt �bergeben wird)
	private QuestionSinglePlayer loadQuestion() {
		return null;
	}

}
