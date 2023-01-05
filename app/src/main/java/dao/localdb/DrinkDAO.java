package dao.localdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entity.Drink;
import entity.IdDrink;

@Dao
public interface DrinkDAO {
    @Insert
    public void addDrinks(Drink... drinks);
    @Delete
    public int deleteDrinks(Drink... drinks);
    @Update
    public int updateDrinks(Drink... drinks);
    // All drinks are returned as LiveData so the query can run on a thread different than UI Thread without the need of the developer (me!) doing it
    @Query("SELECT * FROM starredDrinks")
    LiveData<List<Drink>> getAllStarredDrinks();
    @Query("SELECT idDrink FROM starredDrinks WHERE idDrink = :idDrink")
    IdDrink isDrinkInDbById(String idDrink);
}
