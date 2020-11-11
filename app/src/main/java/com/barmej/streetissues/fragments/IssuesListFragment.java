package com.barmej.streetissues.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barmej.streetissues.R;
import com.barmej.streetissues.activities.IssueDetailsActivity;
import com.barmej.streetissues.adapter.IssueListAdapter;
import com.barmej.streetissues.data.Issue;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class IssuesListFragment extends Fragment implements IssueListAdapter.OnIssueClickListener {
    /*
      RecyclerView variable to display a lsit of issues
     */
    private RecyclerView mRecyclerView;

    /*
     IssueListAdapter object
     */
    private IssueListAdapter mIssuesListAdapter;

    /*
     Issue ArrayList
     */
    private ArrayList<Issue> mIssues;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_issues_list, container, false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        /*
         Find recycler view by id
         */
        mRecyclerView = view.findViewById( R.id.recycler_view_issue );

        /*
          Set layout manager of the recycler view to specify how to show issues list
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        mRecyclerView.setLayoutManager( layoutManager );
        mRecyclerView.addItemDecoration( new DividerItemDecoration( getContext(), DividerItemDecoration.VERTICAL ) );




        /*
         ArrayList object
         */
        mIssues = new ArrayList<>();

        /*
         IssueListAdapter object
         */
        mIssuesListAdapter = new IssueListAdapter( mIssues, IssuesListFragment.this );

        /*
          Set adapter to the recycler view
         */
        mRecyclerView.setAdapter( mIssuesListAdapter );

        /*
         Get data from the collection by firestore
         */
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection( "issues" ).orderBy( "dateTime" ).addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mIssues.clear();
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Issue issue = document.toObject( Issue.class );
                        mIssues.add( issue );
                        System.out.println( issue.getTitle() );
                    }

                    //Notify adpter aboutnchange
                    mIssuesListAdapter.notifyDataSetChanged();
                }
            }
        } );
    }

    /*
      An intent to pass Issue object to teh IssueDetalesActivity
     */
    @Override
    public void onIssueClick(Issue issue) {
        Intent intent = new Intent( getContext(), IssueDetailsActivity.class );
        intent.putExtra( IssueDetailsActivity.ISSUE_DATA, issue );
        startActivity( intent );
    }
}
