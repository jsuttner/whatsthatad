package com.hasude.whatsthatad;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hasude.whatsthatad.fragments.SingleLevelFragment;

public class SwipeAdapter extends FragmentPagerAdapter{
	
	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	public SwipeAdapter(FragmentManager fm, int fragCount) {
		super(fm);
		initFragments(fragCount);
	}	
	
    private void initFragments(int fragCount) {
		for(int i = 0; i < fragCount; i++)
			fragmentList.add(new SingleLevelFragment());
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
		// TODO Auto-generated method stub
		return "Level " + (position+1);
	}
}
