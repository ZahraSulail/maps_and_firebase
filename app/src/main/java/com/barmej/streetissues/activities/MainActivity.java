package com.barmej.streetissues.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.barmej.streetissues.R;
import com.barmej.streetissues.fragments.IssuesListFragment;
import com.barmej.streetissues.fragments.MapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    /*
     Define required variables
     */
    private FloatingActionButton mAddFloatingActionButton;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MenuItem listMenuItem;
    private  MenuItem mapMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        /*
         Set tool bar
         */
        mToolbar = findViewById(R.id.toolBar_home);
        setSupportActionBar(mToolbar);

        /*
          find viewPager by id
         */
        mViewPager = findViewById(R.id.view_pager_home);

        /*
          find TabLayout by id
         */
        mTabLayout = findViewById(R.id.tab_layout_home);

        /*\
         ViewPagerAdapter object
         */
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter( getSupportFragmentManager());

        /*
          Add fragmnets to the viewPager adapter
         */
        viewPagerAdapter.addFragment( new IssuesListFragment());
        viewPagerAdapter.addFragment( new MapFragment());

        /*
          Set tabLayout with viewPager
         */
        mTabLayout.setupWithViewPager(mViewPager);

        /*
          Set adapter to the view pager
         */
        mViewPager.setAdapter(viewPagerAdapter);

        /*
          find floatingactionButton by id
         */
        mAddFloatingActionButton = findViewById(R.id.button_add_new_issue);

        /*
          AddFloatingActionButton setOnClickListener to start AddNewIssueActivity by intent
         */
        mAddFloatingActionButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, AddNewIssueActivity.class) );
            }
        } );
    }

    /*/
      ViewPager calss
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super( fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT );
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

      public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return getString(R.string.issues_list);
                case 1:
                    return getString(R.string.issues_on_map);
                default:
                    return null;
            }
        }

        public void addFragment(Fragment fragment){
            fragments.add(fragment);
        }
    }
}
