package com.hasude.whatsthatad;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hasude.whatsthatad.fragments.SingleLevelFragment;

public class SwipeAdapter extends FragmentPagerAdapter{
	
	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	public SwipeAdapter(FragmentManager fm, int fragCount, SingleMenuActivity s) {
		super(fm);
		initFragments(fragCount, s);
	}	
	
	// Add SingleLevelfragment for each level
    private void initFragments(int fragCount, SingleMenuActivity s) {
		for(int i = 0; i < fragCount; i++)
			fragmentList.add(new SingleLevelFragment(i, s));
	}

    // Returns SingleLevelFragment for requested position
	@Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

	// Return Amount of Fragments in Swipeview
	@Override
	public int getCount() {
		return fragmentList.size();
	}
	
	// Return Swipeinfo at the bottom of Viewpager
	@Override
	public CharSequence getPageTitle(int position) {
		return "Level " + (position+1);
	}
}
