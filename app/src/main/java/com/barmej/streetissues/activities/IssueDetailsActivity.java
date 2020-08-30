package com.barmej.streetissues.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.barmej.streetissues.R;
import com.barmej.streetissues.data.Issue;
import com.barmej.streetissues.fragments.IssuesListFragment;
import com.barmej.streetissues.fragments.MapFragment;
import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class IssueDetailsActivity extends AppCompatActivity {
    /*
     String key to get data fro intent
     */
    public static final String ISSUE_DATA = "issue_data";

    /*
     Define required variables
     */
    private ImageView mIssueImageView;
    private TextView mIssueTitleTextView;
    private TextView mIssueDescriptionTextView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_issue_details);
        /*
          Set tool bar
         */
        mToolbar = findViewById( R.id.toolBar_home);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
         Find views by id's
         */
        mIssueImageView = findViewById(R.id.image_view__issue_photo);
        mIssueTitleTextView = findViewById(R.id.text_view_issue_title);
        mIssueDescriptionTextView = findViewById(R.id.text_view_issue_description);

        /*
          getIntent to get dat from Issue object
         */
        if(getIntent() != null && getIntent().getExtras() != null){
            Issue issue = getIntent().getExtras().getParcelable(ISSUE_DATA);
            if(issue!= null){
                getSupportActionBar().setTitle(issue.getTitle());
                mIssueDescriptionTextView.setText(issue.getDescription());
                Glide.with( this).load(issue.getPhoto()).into(mIssueImageView);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_list){
            showIssueListFragment();
            return  true;
        }else{
            if(id == R.id.action_map){
                showMapFragment();
            }
        }


        return true;
    }

    private void showIssueListFragment(){
        Fragment isuueListFragment = new IssuesListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace( R.id.layout_issue_details, isuueListFragment )
                .addToBackStack( null)
                .commit();
    }

    private void showMapFragment(){
        Fragment mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace( R.id.layout_issue_details, mapFragment )
                .addToBackStack( null )
                .commit();
    }
}