package control.drinkquerier;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Response;

import dao.thecocktaildb.DrinkDAO;

public class AllDrinksQuerier implements DrinkQuerier {
    // Uses CocktailDBDAO to query all drinks one letter at a time, using a generator
    private char currentLetter ;
    private char startLetter;
    private char endLetter;
    private Context context;
    private Response.Listener listener;
    public AllDrinksQuerier(Context context, Response.Listener listener, char startLetter, char endLetter){
        this.startLetter = startLetter;
        this.currentLetter = startLetter;
        this.context = context;
        this.endLetter = endLetter;
        this.listener = listener;
    }

    protected AllDrinksQuerier(Parcel in) {
        currentLetter = (char) in.readInt();
        startLetter = (char) in.readInt();
        endLetter = (char) in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt((int) currentLetter);
        dest.writeInt((int) startLetter);
        dest.writeInt((int) endLetter);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AllDrinksQuerier> CREATOR = new Creator<AllDrinksQuerier>() {
        @Override
        public AllDrinksQuerier createFromParcel(Parcel in) {
            return new AllDrinksQuerier(in);
        }

        @Override
        public AllDrinksQuerier[] newArray(int size) {
            return new AllDrinksQuerier[size];
        }
    };

    public char getCurrentLetter() {
        return currentLetter;
    }

    public char getEndLetter() {
        return endLetter;
    }

    @Override
    public void queryNextSet() throws EndReachedException {
        if (currentLetter <= endLetter) {
            DrinkDAO
                    .getInstance(context)
                    .queryDrinksByFirstLetter(
                            String.valueOf(currentLetter),
                            null,
                            this.listener
                    );
            currentLetter ++;
        }else{
            throw new EndLetterReachedException();
        }
    }
    public void reset(){
        this.currentLetter = startLetter;
    }
}
