package com.example.conket_owner;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
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
    User connected_user;
    String shop_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        connected_user = (User)intent.getParcelableExtra("connected_user");
        shop_id = intent.getStringExtra("shop_id");

        //?€λΉκ²?΄? ?­ ???Όλ‘? ?‘?λ°? ?€? 
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //? ?? ?­? Fragmentλ₯? λ¦¬ν΄?? PagerAdapter??±
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());

        // swipeκΈ°λ₯? ViewPager?€? 
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // swipe ?΄λ²€νΈ μ²λ¦¬
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {

                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        // ?‘?λ°μ ?­ μΆκ?
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }
    }

    // ?­ ? ?? ?΄λ²€νΈ μ²λ¦¬
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
                    // Fragmentλ‘? ? λ³? ?κΈ°κΈ°
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
            // ? μ²? λ³΄μ¬μ€? ?λ©? ??΄μ§??
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "product".toString();
                case 1:
                    return "coupon".toString();
                case 2:
                    return "review".toString();
                case 3:
                    return "settings".toString();
            }
            return null;
        }
    }

}// end class
