package control.staradrink;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import control.drinkquerier.DrinkQuerier;
import control.drinkquerier.StarredDrinksQuerier;
import dao.localdb.StarredDrinksDB;
import entity.Drink;
import entity.IdDrink;

public class StarredDrinksRepo {
    // Repository for starred drinks local db. It delegates all interactions with local db on a separate thread.

    private abstract class AsyncRepoOperationRunnable implements Runnable{
        // Base class for all async operations provided by this repo
        protected Context context;
        protected Handler handler;
        protected Drink[] drinks;
        public AsyncRepoOperationRunnable(Context context, Handler handler, Drink... drinks){
            this.context = context;
            this.drinks = drinks;
            this.handler = handler;
        }
    }

    private class StarDrinksRunnable extends AsyncRepoOperationRunnable{
        // Asks StarredDrinksDB to add a drink asynchronously
        public StarDrinksRunnable(Context context, Handler handler, Drink... drinks) {
            super(context, handler, drinks);
        }

        @Override
        public void run() {
            StarredDrinksDB
                    .getInstance(this.context)
                    .getDrinkDAO()
                    .addDrinks(this.drinks);
            if (this.handler != null){
                int what = ResultCode.OK.getCode();
                this.handler.sendMessage(Message.obtain(handler, what));
            }

        }
    }

    private class UnstarDrinksRunnable extends AsyncRepoOperationRunnable{
        // Asks StarredDrinksDB to remove a drink asynchronously
        public UnstarDrinksRunnable(Context context, Handler handler, Drink... drinks) {
            super(context, handler, drinks);
        }

        @Override
        public void run() {
            StarredDrinksDB
                    .getInstance(this.context)
                    .getDrinkDAO()
                    .deleteDrinks(this.drinks);
            if (this.handler != null){
                int what = ResultCode.OK.getCode();
                this.handler.sendMessage(Message.obtain(handler, what));
            }
        }
    }
    private class IsStarredRunnable extends AsyncRepoOperationRunnable{
        // If a drinks is starred result code is STARRED, NOT_STARRED if it is not, null otherwise
        public IsStarredRunnable(Context context, @NonNull Handler handler, Drink drink) {
            super(context, handler, drink);
        }

        @Override
        public void run() {
            ResultCode resultCode;
            IdDrink result = StarredDrinksDB
            .getInstance(this.context)
            .getDrinkDAO()
            .isDrinkInDbById(drinks[0].getIdDrink());
            if (result == null){
                resultCode = ResultCode.NOT_STARRED;
            }
            else {
                resultCode = ResultCode.STARRED;
            }
            int what = resultCode.getCode();
            this.handler.sendMessage(Message.obtain(handler, what));
        }
    }

    private static StarredDrinksRepo ref;
    public static StarredDrinksRepo getInstance(){
        if (ref == null){
            ref = new StarredDrinksRepo();
        }
        return ref;
    }
    private StarredDrinksRepo(){ }

    public DrinkQuerier getStarredDrinksQuerier(Context context, Observer<List<Drink>> observer){
        return new StarredDrinksQuerier(context, observer);
    }
    public void starDrinks(Context context, Handler handler, Drink... drinks){
        // Starts a thread to add a drink to starred drinks
        Thread starDrinksThread = new Thread(new StarDrinksRunnable(context, handler, drinks));
        starDrinksThread.start();
    }
    public void unstarDrinks(Context context, Handler handler, Drink... drinks){
        // Starts a thread to remove a drink from starred drinks
        Thread unstarDrinksThread = new Thread(new UnstarDrinksRunnable(context, handler, drinks));
        unstarDrinksThread.start();
    }

    public void isStarred(Context context, Handler handler, Drink drink){
        // Starts a thread to check if a drink is starred
        Thread isStarredThread = new Thread(new IsStarredRunnable(context, handler, drink));
        isStarredThread.start();
    }
}
