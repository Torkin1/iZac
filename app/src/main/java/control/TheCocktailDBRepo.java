package control;

import android.content.Context;

import com.android.volley.Response;

import control.drinkquerier.AllDrinksQuerier;
import control.drinkquerier.DrinkQuerier;

public class TheCocktailDBRepo {
    // Repository for interacting with TheCocktailDB remote database
    private static TheCocktailDBRepo ref;
    public static TheCocktailDBRepo getInstance(){
        if (ref == null){
            ref = new TheCocktailDBRepo();
        }
        return ref;
    }
    private TheCocktailDBRepo(){}

    public DrinkQuerier getAllDrinksQuerier(Context context, Response.Listener listener){
        return new AllDrinksQuerier(context, listener, 'a', 'z');
    }
}
