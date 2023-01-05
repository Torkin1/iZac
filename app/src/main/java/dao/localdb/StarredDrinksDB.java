package dao.localdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import entity.Drink;

@Database(entities = Drink.class, version = 1)
public abstract class StarredDrinksDB extends RoomDatabase {
    // local db of starred drinks
    private static StarredDrinksDB ref = null;
    public static StarredDrinksDB getInstance(Context context){
        if (ref == null){
            ref = Room.databaseBuilder(
                    context.getApplicationContext(),
                    StarredDrinksDB.class,
                    StarredDrinksDB.class.getName()
            ).build();
        }
        return ref;

    }
    public abstract DrinkDAO getDrinkDAO();
}
