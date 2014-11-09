package com.hasude.whatsthatad;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class SingleMenuActivity extends FragmentActivity{
	
	ViewPager viewPager;
	SwipeAdapter swipeAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_menu);
		
		viewPager = (ViewPager) findViewById(R.id.singlePager);
		swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), 3);
		if(viewPager == null){
			System.out.println("Swipeadapter ist null");
		}
		viewPager.setAdapter(swipeAdapter);
	}
}