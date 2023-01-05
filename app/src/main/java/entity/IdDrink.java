package entity;

import androidx.room.ColumnInfo;

public class IdDrink {
    @ColumnInfo(name = "idDrink")
    private String idDrink;

    public String getIdDrink() {
        return idDrink;
    }

    public void setIdDrink(String idDrink) {
        this.idDrink = idDrink;
    }
}
