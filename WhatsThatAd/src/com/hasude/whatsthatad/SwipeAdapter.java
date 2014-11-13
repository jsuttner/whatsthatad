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
	
    private void initFragments(int fragCount, SingleMenuActivity s) {
		for(int i = 0; i < fragCount; i++)
			fragmentList.add(new SingleLevelFragment(i, s));
	}

	@Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

	@Override
	public int getCount() {
		return fragmentList.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return "Level " + (position+1);
	}
}
