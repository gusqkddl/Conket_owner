package com.example.conket_owner;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		//�׺���̼� �� �������� �׼ǹ� ����
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		//������ ���� Fragment�� �����ϴ� PagerAdapter����
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// swipe����� ViewPager����
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

		// swipe �̺�Ʈ ó��
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {

						actionBar.setSelectedNavigationItem(position);
					}
				});

		// �׼ǹٿ� �� �߰�
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {

			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	// �� ���ý� �̺�Ʈ ó��
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = ProductlistFragment.newInstance();
				// Fragment�� ���� �ѱ��
				Bundle args = new Bundle();
				args.putString("Productlist", "ProductlistFragment");
				fragment.setArguments(args);
				break;
			case 1:
				fragment = CouponFragment.newInstance();
				break;
			case 2:
				fragment = ReviewFragment.newInstance();
				break;
			case 3:
				fragment = OptionFragment.newInstance();
				break;
			}// end switch
			return fragment;
		}// end getItem

		@Override
		public int getCount() {
			// ��ü ������ ȭ�� ��������
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "Product";
			case 1:
				return "Coupon";
			case 2:
				return "Review";
			case 3:
				return "Settings";
			}
			return null;
		}
	}

}// end class
