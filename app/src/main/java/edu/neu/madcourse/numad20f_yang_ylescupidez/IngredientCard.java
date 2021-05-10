package edu.neu.madcourse.numad20f_yang_ylescupidez;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class IngredientCard implements Parcelable {

    public IngredientCard() {
    }

    protected IngredientCard(Parcel in) {

    }

    public static final Creator<IngredientCard> CREATOR = new Creator<IngredientCard>() {
        @Override
        public IngredientCard createFromParcel(Parcel in) {
            return new IngredientCard(in);
        }

        @Override
        public IngredientCard[] newArray(int size) {
            return new IngredientCard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
