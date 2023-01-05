package control.drinkquerier;

import android.os.Parcelable;

import com.android.volley.Response;

public interface DrinkQuerier extends Parcelable{
    public void queryNextSet() throws EndReachedException;
}
