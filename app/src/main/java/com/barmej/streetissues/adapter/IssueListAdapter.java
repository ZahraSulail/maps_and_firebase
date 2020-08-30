package com.barmej.streetissues.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.barmej.streetissues.R;
import com.barmej.streetissues.data.Issue;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IssueListAdapter extends RecyclerView.Adapter<IssueListAdapter.IssueViewHolder> {

    public interface OnIssueClickListener{
        void onIssueClick(Issue issue);
    }

    private OnIssueClickListener mOnIssueClickListener;

    private List<Issue> mIssuesList;

    public IssueListAdapter(List<Issue> IssuesList,OnIssueClickListener onIssueClickListener){
        this.mIssuesList = IssuesList;
        this.mOnIssueClickListener = onIssueClickListener;

    }
    @NonNull
    @Override
    public IssueListAdapter.IssueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.item_issue, parent, false);
        return new IssueViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull IssueListAdapter.IssueViewHolder holder, int position) {
       holder.bind( mIssuesList.get(position));
    }

    @Override
    public int getItemCount() {
        return mIssuesList.size();
    }

    public class IssueViewHolder extends RecyclerView.ViewHolder {

        TextView issueTitleTextView;
        ImageView issuePhotoImageView;
        Issue issue;
        public IssueViewHolder(@NonNull View itemView) {
            super( itemView );

            issueTitleTextView = itemView.findViewById( R.id.text_view_issue_title );
            issuePhotoImageView = itemView.findViewById(R.id.image_view__issue_photo);
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnIssueClickListener.onIssueClick(issue);
                }
            } );


        }
        public void bind(Issue issue){
            this.issue = issue;
            issueTitleTextView.setText( issue.getTitle());
            Glide.with( issuePhotoImageView).load( issue.getPhoto()).into(issuePhotoImageView);
            Log.i("", "issue link"+issue.getPhoto());

        }

    }
}
