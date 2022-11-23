package com.example.food2go.Domain;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryDomain implements Parcelable {
    private  String title;
    private String pic;

    public CategoryDomain(String title, String pic) {
        this.title = title;
        this.pic = pic;
    }

    protected CategoryDomain(Parcel in) {
        title = in.readString();
        pic = in.readString();
    }

    public static final Creator<CategoryDomain> CREATOR = new Creator<CategoryDomain>() {
        @Override
        public CategoryDomain createFromParcel(Parcel in) {
            return new CategoryDomain(in);
        }

        @Override
        public CategoryDomain[] newArray(int size) {
            return new CategoryDomain[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(pic);
    }
}
