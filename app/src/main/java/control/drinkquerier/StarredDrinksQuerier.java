package control.drinkquerier;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import dao.localdb.StarredDrinksDB;
import entity.Drink;

public class StarredDrinksQuerier implements DrinkQuerier {
    // Queries Local db for starred Drinks asynchronously
    private Context context;
    private boolean queried = false;
    private dao.localdb.DrinkDAO dao;
    private LiveData<List<Drink>> starredDrinks;
    private Observer<List<Drink>> observer;
    public StarredDrinksQuerier(Context context, Observer<List<Drink>> observer){
        // An Observer is needed to handle updates to starredDrinks asynchronously
        this.context = context;
        this.dao = StarredDrinksDB.getInstance(context).getDrinkDAO();
        this.observer = observer;
    }

    @Override
    public void queryNextSet() throws EndReachedException {
        //  queries local db for starred drinks
        if (queried){
            throw new EndReachedException();
        }
        else{
            this.starredDrinks = this.dao.getAllStarredDrinks();
            this.queried = true;
            this.starredDrinks.observe((LifecycleOwner) context, this.observer);
        }
    }

    protected StarredDrinksQuerier(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StarredDrinksQuerier> CREATOR = new Creator<StarredDrinksQuerier>() {
        @Override
        public StarredDrinksQuerier createFromParcel(Parcel in) {
            return new StarredDrinksQuerier(in);
        }

        @Override
        public StarredDrinksQuerier[] newArray(int size) {
            return new StarredDrinksQuerier[size];
        }
    };


}
