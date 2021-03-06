package com.barmej.streetissues.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class Issue implements Parcelable {

    private String title;
    private String description;
    private String photo;
    private GeoPoint location;
    private Timestamp dateTime;


    public Issue() {
    }

    public Issue(String title, String description, String photo, GeoPoint location) {
        this.title = title;
        this.description = description;
        this.photo = photo;
        this.location = location;
    }

    protected Issue(Parcel in) {
        title = in.readString();
        description = in.readString();
        photo = in.readString();
        location = new GeoPoint( in.readDouble(), in.readDouble() );
        dateTime = in.readParcelable( Timestamp.class.getClassLoader() );
    }

    public static final Creator<Issue> CREATOR = new Creator<Issue>() {
        @Override
        public Issue createFromParcel(Parcel in) {
            return new Issue( in );
        }

        @Override
        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( title );
        dest.writeString( description );
        dest.writeString( photo );
        dest.writeDouble( location.getLatitude() );
        dest.writeDouble( location.getLongitude() );
        dest.writeParcelable( dateTime, flags );
    }


}
