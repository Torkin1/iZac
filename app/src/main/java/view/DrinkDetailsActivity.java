package view;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import java.util.List;

import control.staradrink.ResultCode;
import control.staradrink.StarredDrinksRepo;
import dao.thecocktaildb.DrinkDAO;
import dao.thecocktaildb.DrinkThumbnailSize;
import dao.thecocktaildb.IngredientDAO;
import dao.thecocktaildb.IngredientThumbnailSize;
import entity.Drink;
import entity.Ingredient;
import entity.Perk;
import torkin.homework.cocktail.R;

public class DrinkDetailsActivity extends AppCompatActivity {

    private class Holder{

        private class OnStarClickHandler implements View.OnClickListener{

            private class OnDrinkStarred extends Handler{
                @Override
                public void handleMessage(Message msg){
                    // drink is starred, changing ibStar on click behaviour to unstarring drink
                    if (msg.what == ResultCode.OK.getCode()){
                        // selected drink has been starred, updates UI
                        Toast.makeText(
                                DrinkDetailsActivity.this,
                                getString(R.string.Toast_OnDrinkStarred),
                                Toast.LENGTH_SHORT)
                                .show();
                        ibStar.setOnClickListener(new OnUnstarClickHandler());
                        ibStar.setImageDrawable(getDrawable(R.drawable.icons8_star_filled_100_1_));
                    }
                }
            }

            @Override
            public void onClick(View v) {
                StarredDrinksRepo.getInstance().starDrinks(
                        DrinkDetailsActivity.this,
                        new OnDrinkStarred(),
                        selectedDrink
                );
            }
        }
        private class OnUnstarClickHandler implements View.OnClickListener{

            private class OnDrinkUnstarred extends Handler{
                @Override
                public void handleMessage(Message msg){
                    // drink is unstarred, changing ibStar on click behaviour to starring drink
                    if (msg.what == ResultCode.OK.getCode()){
                        // selected drink has been unstarred, updates UI
                        Toast.makeText(
                                DrinkDetailsActivity.this,
                                getString(R.string.Toast_OnDrinkUnstarred),
                                Toast.LENGTH_SHORT)
                                .show();
                        ibStar.setOnClickListener(new OnStarClickHandler());
                        ibStar.setImageDrawable(getDrawable(R.drawable.icons8_star_filled_100));
                    }
                }
            }

            @Override
            public void onClick(View v) {
                StarredDrinksRepo.getInstance().unstarDrinks(
                        DrinkDetailsActivity.this,
                        new OnDrinkUnstarred(),
                        selectedDrink
                );
            }
        }
        private class OnIsStarredResponse extends Handler{
            @Override
            public void handleMessage(Message msg){
                if (msg.what == ResultCode.STARRED.getCode()){
                   // Drink is already starred, button click unstars drink
                   ibStar.setImageDrawable(getDrawable(R.drawable.icons8_star_filled_100_1_));
                   ibStar.setOnClickListener(new OnUnstarClickHandler());
                }
                else if (msg.what == ResultCode.NOT_STARRED.getCode()){
                    // Drink is not starred, button click stars drink
                    ibStar.setOnClickListener(new OnStarClickHandler());
                }
                else{
                    // uh-oh, this should not happen
                    Log.e(this.getClass().getName(), String.format("msg.what is %d", msg.what));
                }
            }
        }

        private final TextView tvCocktailName;
        private final ImageView ivCocktailPreview;
        private final TableLayout tIngredients;
        private final TableLayout tPerks;
        private final TextView tvRecipe;
        private final ImageButton ibStar;

        public Holder(){

            ibStar = DrinkDetailsActivity.this.findViewById(R.id.ibStar);
            // Checks if selectedDrink is starred and updates ibStar accordingly
            StarredDrinksRepo.getInstance().isStarred(
                    DrinkDetailsActivity.this,
                    new OnIsStarredResponse(),
                    selectedDrink);
            // Thumbnail must be queried again, but this is not a problem because it will probably be in local cache provided by Volley library
            DrinkDAO.getInstance(DrinkDetailsActivity.this)
                    .queryDrinkThumbnail(
                            selectedDrink.getStrDrinkThumb(),
                            DrinkThumbnailSize.DRINK_NORMAL,
                            null,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    ivCocktailPreview.setImageBitmap(response);
                                }
                            });
            // binds layout to views
            tvCocktailName = DrinkDetailsActivity.this.findViewById(R.id.tvCocktailName);
            ivCocktailPreview = DrinkDetailsActivity.this.findViewById(R.id.ivCocktailPreview);
            tIngredients = DrinkDetailsActivity.this.findViewById(R.id.tIngredients);
            tPerks = DrinkDetailsActivity.this.findViewById(R.id.tPerks);
            tvRecipe = DrinkDetailsActivity.this.findViewById(R.id.tvRecipe);
            // populates textViews recipe and Drink name
            tvCocktailName.setText(selectedDrink.getStrDrink());
            tvRecipe.setText(selectedDrink.getStrInstructions());
            // Populating table of ingredients
            List<Ingredient> allIngredients = selectedDrink.getAllIngredients();
            Ingredient glass = new Ingredient();
            glass.setName(selectedDrink.getStrGlass());
            allIngredients.add(glass);
            for (Ingredient i : allIngredients){
                TableRow row = (TableRow) DrinkDetailsActivity.this.getLayoutInflater().inflate(R.layout.table_ingredients_item, null);
                final ImageView ivIngredientThumb = row.findViewById(R.id.ivIngredientThumb);
                TextView tvIngredientName = row.findViewById(R.id.tvIngredientName);
                TextView tvIngredientQuantity = row.findViewById(R.id.tvIngredientQuantity);
                if (allIngredients.indexOf(i) != (allIngredients.size() - 1)) {
                    IngredientDAO
                            .getInstance(DrinkDetailsActivity.this)
                            .queryIngredientThumbnail(i.getName(), IngredientThumbnailSize.INGREDIENT_NORMAL, null, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    if (response != null) {
                                        ivIngredientThumb.setImageBitmap(response);
                                    }
                                }
                            });
                }
                else{
                    ivIngredientThumb.setImageDrawable(getDrawable(R.drawable.icons8_cocktail_64));
                }
                tvIngredientName.setText(i.getName());
                tvIngredientQuantity.setText(i.getMeasure());
                if (allIngredients.indexOf(i) % 2 == 0){
                    tvIngredientName.setBackgroundColor(getResources().getColor(R.color.colorAccent2, null));
                    tvIngredientQuantity.setBackgroundColor(getResources().getColor(R.color.colorAccent2, null));
                    ivIngredientThumb.setBackgroundColor(getResources().getColor(R.color.colorAccent2, null));
                }
                tIngredients.addView(row);
            }
            // populating table of perks
            List<Perk> perks = selectedDrink.getAllPerks();
            for (Perk p : perks) {
                TableRow row = (TableRow) DrinkDetailsActivity
                        .this
                        .getLayoutInflater()
                        .inflate(R.layout.table_perks_item, null);
                ImageView ivPerkThumb = row.findViewById(R.id.ivPerkThumb);
                TextView tvPerkVal = row.findViewById(R.id.tvPerkVal);
                ivPerkThumb.setImageDrawable(getDrawable(p.getPerkType().getThumbId()));
                tvPerkVal.setText(p.getValue());
                if (perks.indexOf(p) % 2 == 0) {
                    ivPerkThumb.setBackgroundColor(getResources().getColor(R.color.colorAccent2, null));
                    tvPerkVal.setBackgroundColor(getResources().getColor(R.color.colorAccent2, null));
                }
                tPerks.addView(row);
            }
        }

    }
    private Drink selectedDrink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cocktail_details);
        this.selectedDrink = getIntent().getParcelableExtra(getString(R.string.DRINK_SELECTED_KEY));
        Log.i(this.getClass().getName(), String.format("idDrink of selected Drink is: %s", selectedDrink.getIdDrink()));
        Holder holder = new Holder();
    }

    public Drink getSelectedDrink() {
        return selectedDrink;
    }
}
