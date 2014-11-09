package com.hasude.whatsthatad;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.hasude.whatsthatad.fragments.SingleLevelFragment;

public class SwipeAdapter extends FragmentPagerAdapter{
	
	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	public SwipeAdapter(FragmentManager fm, int fragCount, ViewPager p) {
		super(fm);
		initFragments(fragCount, p);
	}	
	
    private void initFragments(int fragCount, ViewPager p) {
		for(int i = 0; i < fragCount; i++)
			fragmentList.add(new SingleLevelFragment(p));
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
