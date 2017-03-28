package com.marknkamau.justjava.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CoffeeDrink implements Parcelable {
    private final String drinkID;
    private final String drinkName;
    private final String drinkContents;
    private final String drinkDescription;
    private final String drinkPrice;
    private final String drinkImage;

    public CoffeeDrink(String drinkID, String drinkName, String drinkContents, String drinkDescription, String drinkPrice, String drinkImage) {
        this.drinkID = drinkID;
        this.drinkName = drinkName;
        this.drinkContents = drinkContents;
        this.drinkDescription = drinkDescription;
        this.drinkPrice = drinkPrice;
        this.drinkImage = drinkImage;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public String getDrinkContents() {
        return drinkContents;
    }

    public String getDrinkDescription() {
        return drinkDescription;
    }

    public String getDrinkPrice() {
        return drinkPrice;
    }

    public String getDrinkImage() {
        return drinkImage;
    }

    @Override
    public String toString() {
        return "CoffeeDrink{" +
                "drinkID='" + drinkID + '\'' +
                ", drinkName='" + drinkName + '\'' +
                ", drinkContents='" + drinkContents + '\'' +
                ", drinkDescription='" + drinkDescription + '\'' +
                ", drinkPrice='" + drinkPrice + '\'' +
                ", drinkImage='" + drinkImage + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.drinkID);
        dest.writeString(this.drinkName);
        dest.writeString(this.drinkContents);
        dest.writeString(this.drinkDescription);
        dest.writeString(this.drinkPrice);
        dest.writeString(this.drinkImage);
    }

    private CoffeeDrink(Parcel in) {
        this.drinkID = in.readString();
        this.drinkName = in.readString();
        this.drinkContents = in.readString();
        this.drinkDescription = in.readString();
        this.drinkPrice = in.readString();
        this.drinkImage = in.readString();
    }

    public static final Creator<CoffeeDrink> CREATOR = new Creator<CoffeeDrink>() {
        @Override
        public CoffeeDrink createFromParcel(Parcel source) {
            return new CoffeeDrink(source);
        }

        @Override
        public CoffeeDrink[] newArray(int size) {
            return new CoffeeDrink[size];
        }
    };
}
