package entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import control.reflection.NoSuchGetterException;
import control.reflection.ReflectionMiscellaneous;

@Entity(tableName = "starredDrinks")
public class Drink implements Parcelable {
    @PrimaryKey @NonNull
    private String idDrink;
    private String strTags;
    private String strCreativeCommonsConfirmed;
    private String strAlcoholic;
    private String strDrink;
    private String strCategory;
    private String strIBA;
    private String strGlass;
    private String strInstructions;
    private String strDrinkThumb;
    private String strIngredient1;
    private String strIngredient2;
    private String strIngredient3;
    private String strIngredient4;
    private String strIngredient5;
    private String strIngredient6;
    private String strIngredient7;
    private String strIngredient8;
    private String strIngredient9;
    private String strIngredient10;
    private String strIngredient11;
    private String strIngredient12;
    private String strIngredient13;
    private String strIngredient14;
    private String strIngredient15;
    private String strMeasure1;
    private String strMeasure2;
    private String strMeasure3;
    private String strMeasure4;
    private String strMeasure5;
    private String strMeasure6;
    private String strMeasure7;
    private String strMeasure8;
    private String strMeasure9;
    private String strMeasure10;
    private String strMeasure11;
    private String strMeasure12;
    private String strMeasure13;
    private String strMeasure14;
    private String strMeasure15;

    public Drink(){

    }

    protected Drink(Parcel in) {
        idDrink = in.readString();
        strTags = in.readString();
        strCreativeCommonsConfirmed = in.readString();
        strAlcoholic = in.readString();
        strDrink = in.readString();
        strCategory = in.readString();
        strIBA = in.readString();
        strGlass = in.readString();
        strInstructions = in.readString();
        strDrinkThumb = in.readString();
        strIngredient1 = in.readString();
        strIngredient2 = in.readString();
        strIngredient3 = in.readString();
        strIngredient4 = in.readString();
        strIngredient5 = in.readString();
        strIngredient6 = in.readString();
        strIngredient7 = in.readString();
        strIngredient8 = in.readString();
        strIngredient9 = in.readString();
        strIngredient10 = in.readString();
        strIngredient11 = in.readString();
        strIngredient12 = in.readString();
        strIngredient13 = in.readString();
        strIngredient14 = in.readString();
        strIngredient15 = in.readString();
        strMeasure1 = in.readString();
        strMeasure2 = in.readString();
        strMeasure3 = in.readString();
        strMeasure4 = in.readString();
        strMeasure5 = in.readString();
        strMeasure6 = in.readString();
        strMeasure7 = in.readString();
        strMeasure8 = in.readString();
        strMeasure9 = in.readString();
        strMeasure10 = in.readString();
        strMeasure11 = in.readString();
        strMeasure12 = in.readString();
        strMeasure13 = in.readString();
        strMeasure14 = in.readString();
        strMeasure15 = in.readString();
    }

    public static final Creator<Drink> CREATOR = new Creator<Drink>() {
        @Override
        public Drink createFromParcel(Parcel in) {
            return new Drink(in);
        }

        @Override
        public Drink[] newArray(int size) {
            return new Drink[size];
        }
    };

    public List<Perk> getAllPerks(){
        //Retrieve all perks of this Drink registered in enum Perk with non-null value using reflection
        List<Perk> perks = new ArrayList<>();
        for (PerkType p : PerkType.values()){
            try {
                String perkVal = (String) ReflectionMiscellaneous
                        .getGetter(p.toString(), this)
                        .invoke(this, (Object[]) null);
                if (isSafe(perkVal)){
                    Perk perk = new Perk();
                    perk.setValue(perkVal);
                    perk.setPerkType(p);
                    perks.add(perk);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchGetterException e) {
                Log.e(this.getClass().getName(), e.toString());
            }
        }
        return perks;
    }

    private boolean isSafe(String toCheck){
        return (toCheck != null && toCheck.compareTo("") != 0 && toCheck.compareTo("Other/Unknown") != 0);
    }

    public List<Ingredient> getAllIngredients(){
        // return a list of all ingredients which value is not equal to null with respective measure using reflection
        Log.i(this.getClass().getName(), String.format("getting all ingredients of cocktail with name=%s", this.strDrink));
        List<Ingredient> allIngredients = new ArrayList<>();
        List<Field> allDrinkFields = Arrays.asList(this.getClass().getDeclaredFields());
        for(int i = 0; i < allDrinkFields.size(); i ++ ){
            try {
                if (allDrinkFields.get(i).getName().contains("strIngredient")) {
                    String ingredientName = (String) ReflectionMiscellaneous.getGetter(allDrinkFields.get(i).getName(), this)
                            .invoke(this, (Object[]) null);
                    if (isSafe(ingredientName)) {
                        Ingredient ingredient = new Ingredient();
                        ingredient.setName(ingredientName);
                        String measure = (String) ReflectionMiscellaneous.getGetter(
                                (new StringBuffer())
                                    .append("strMeasure")
                                    .append(allDrinkFields
                                            .get(i)
                                            .getName()
                                            .replace("strIngredient", ""))
                                    .toString(),
                                this)
                                .invoke(this, (Object[]) null)
                        ;
                        if (isSafe(measure)){
                            ingredient.setMeasure(measure);
                        }
                        Log.i(this.getClass().getName(), String.format("adding to ingredient list ingredient with name=%s and quantity=%s", ingredientName, measure));
                        allIngredients.add(ingredient);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchGetterException e) {
                Log.e(this.getClass().getName(), e.toString());
            }
        }
        return allIngredients;
    }

    @NonNull
    public String getIdDrink() {
        return idDrink;
    }

    public void setIdDrink(@NonNull String idDrink) {
        this.idDrink = idDrink;
    }

    public String getStrTags() {
        return strTags;
    }

    public void setStrTags(String strTags) {
        this.strTags = strTags;
    }

    public String getStrCreativeCommonsConfirmed() {
        return strCreativeCommonsConfirmed;
    }

    public void setStrCreativeCommonsConfirmed(String strCreativeCommonsConfirmed) {
        this.strCreativeCommonsConfirmed = strCreativeCommonsConfirmed;
    }

    public String getStrAlcoholic() {
        return strAlcoholic;
    }

    public void setStrAlcoholic(String strAlcoholic) {
        this.strAlcoholic = strAlcoholic;
    }

    public String getStrDrink() {
        return strDrink;
    }

    public void setStrDrink(String strDrink) {
        this.strDrink = strDrink;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getStrIBA() {
        return strIBA;
    }

    public void setStrIBA(String strIBA) {
        this.strIBA = strIBA;
    }

    public String getStrGlass() {
        return strGlass;
    }

    public void setStrGlass(String strGlass) {
        this.strGlass = strGlass;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
    }

    public String getStrDrinkThumb() {
        return strDrinkThumb;
    }

    public void setStrDrinkThumb(String strDrinkThumb) {
        this.strDrinkThumb = strDrinkThumb;
    }

    public String getStrIngredient1() {
        return strIngredient1;
    }

    public void setStrIngredient1(String strIngredient1) {
        this.strIngredient1 = strIngredient1;
    }

    public String getStrIngredient2() {
        return strIngredient2;
    }

    public void setStrIngredient2(String strIngredient2) {
        this.strIngredient2 = strIngredient2;
    }

    public String getStrIngredient3() {
        return strIngredient3;
    }

    public void setStrIngredient3(String strIngredient3) {
        this.strIngredient3 = strIngredient3;
    }

    public String getStrIngredient4() {
        return strIngredient4;
    }

    public void setStrIngredient4(String strIngredient4) {
        this.strIngredient4 = strIngredient4;
    }

    public String getStrIngredient5() {
        return strIngredient5;
    }

    public void setStrIngredient5(String strIngredient5) {
        this.strIngredient5 = strIngredient5;
    }

    public String getStrIngredient6() {
        return strIngredient6;
    }

    public void setStrIngredient6(String strIngredient6) {
        this.strIngredient6 = strIngredient6;
    }

    public String getStrIngredient7() {
        return strIngredient7;
    }

    public void setStrIngredient7(String strIngredient7) {
        this.strIngredient7 = strIngredient7;
    }

    public String getStrIngredient8() {
        return strIngredient8;
    }

    public void setStrIngredient8(String strIngredient8) {
        this.strIngredient8 = strIngredient8;
    }

    public String getStrIngredient9() {
        return strIngredient9;
    }

    public void setStrIngredient9(String strIngredient9) {
        this.strIngredient9 = strIngredient9;
    }

    public String getStrIngredient10() {
        return strIngredient10;
    }

    public void setStrIngredient10(String strIngredient10) {
        this.strIngredient10 = strIngredient10;
    }

    public String getStrIngredient11() {
        return strIngredient11;
    }

    public void setStrIngredient11(String strIngredient11) {
        this.strIngredient11 = strIngredient11;
    }

    public String getStrIngredient12() {
        return strIngredient12;
    }

    public void setStrIngredient12(String strIngredient12) {
        this.strIngredient12 = strIngredient12;
    }

    public String getStrIngredient13() {
        return strIngredient13;
    }

    public void setStrIngredient13(String strIngredient13) {
        this.strIngredient13 = strIngredient13;
    }

    public String getStrIngredient14() {
        return strIngredient14;
    }

    public void setStrIngredient14(String strIngredient14) {
        this.strIngredient14 = strIngredient14;
    }

    public String getStrIngredient15() {
        return strIngredient15;
    }

    public void setStrIngredient15(String strIngredient15) {
        this.strIngredient15 = strIngredient15;
    }

    public String getStrMeasure1() {
        return strMeasure1;
    }

    public void setStrMeasure1(String strMeasure1) {
        this.strMeasure1 = strMeasure1;
    }

    public String getStrMeasure2() {
        return strMeasure2;
    }

    public void setStrMeasure2(String strMeasure2) {
        this.strMeasure2 = strMeasure2;
    }

    public String getStrMeasure3() {
        return strMeasure3;
    }

    public void setStrMeasure3(String strMeasure3) {
        this.strMeasure3 = strMeasure3;
    }

    public String getStrMeasure4() {
        return strMeasure4;
    }

    public void setStrMeasure4(String strMeasure4) {
        this.strMeasure4 = strMeasure4;
    }

    public String getStrMeasure5() {
        return strMeasure5;
    }

    public void setStrMeasure5(String strMeasure5) {
        this.strMeasure5 = strMeasure5;
    }

    public String getStrMeasure6() {
        return strMeasure6;
    }

    public void setStrMeasure6(String strMeasure6) {
        this.strMeasure6 = strMeasure6;
    }

    public String getStrMeasure7() {
        return strMeasure7;
    }

    public void setStrMeasure7(String strMeasure7) {
        this.strMeasure7 = strMeasure7;
    }

    public String getStrMeasure8() {
        return strMeasure8;
    }

    public void setStrMeasure8(String strMeasure8) {
        this.strMeasure8 = strMeasure8;
    }

    public String getStrMeasure9() {
        return strMeasure9;
    }

    public void setStrMeasure9(String strMeasure9) {
        this.strMeasure9 = strMeasure9;
    }

    public String getStrMeasure10() {
        return strMeasure10;
    }

    public void setStrMeasure10(String strMeasure10) {
        this.strMeasure10 = strMeasure10;
    }

    public String getStrMeasure11() {
        return strMeasure11;
    }

    public void setStrMeasure11(String strMeasure11) {
        this.strMeasure11 = strMeasure11;
    }

    public String getStrMeasure12() {
        return strMeasure12;
    }

    public void setStrMeasure12(String strMeasure12) {
        this.strMeasure12 = strMeasure12;
    }

    public String getStrMeasure13() {
        return strMeasure13;
    }

    public void setStrMeasure13(String strMeasure13) {
        this.strMeasure13 = strMeasure13;
    }

    public String getStrMeasure14() {
        return strMeasure14;
    }

    public void setStrMeasure14(String strMeasure14) {
        this.strMeasure14 = strMeasure14;
    }

    public String getStrMeasure15() {
        return strMeasure15;
    }

    public void setStrMeasure15(String strMeasure15) {
        this.strMeasure15 = strMeasure15;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idDrink);
        dest.writeString(strTags);
        dest.writeString(strCreativeCommonsConfirmed);
        dest.writeString(strAlcoholic);
        dest.writeString(strDrink);
        dest.writeString(strCategory);
        dest.writeString(strIBA);
        dest.writeString(strGlass);
        dest.writeString(strInstructions);
        dest.writeString(strDrinkThumb);
        dest.writeString(strIngredient1);
        dest.writeString(strIngredient2);
        dest.writeString(strIngredient3);
        dest.writeString(strIngredient4);
        dest.writeString(strIngredient5);
        dest.writeString(strIngredient6);
        dest.writeString(strIngredient7);
        dest.writeString(strIngredient8);
        dest.writeString(strIngredient9);
        dest.writeString(strIngredient10);
        dest.writeString(strIngredient11);
        dest.writeString(strIngredient12);
        dest.writeString(strIngredient13);
        dest.writeString(strIngredient14);
        dest.writeString(strIngredient15);
        dest.writeString(strMeasure1);
        dest.writeString(strMeasure2);
        dest.writeString(strMeasure3);
        dest.writeString(strMeasure4);
        dest.writeString(strMeasure5);
        dest.writeString(strMeasure6);
        dest.writeString(strMeasure7);
        dest.writeString(strMeasure8);
        dest.writeString(strMeasure9);
        dest.writeString(strMeasure10);
        dest.writeString(strMeasure11);
        dest.writeString(strMeasure12);
        dest.writeString(strMeasure13);
        dest.writeString(strMeasure14);
        dest.writeString(strMeasure15);
    }
}
