package view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;

import control.drinkquerier.WhatDrinks;
import dao.thecocktaildb.DrinkDAO;
import entity.DrinkPack;
import torkin.homework.cocktail.R;

public class MainActivity extends AppCompatActivity {

    private class Holder{
        private Button bRandom = findViewById(R.id.bRandom);
        private Button bStarred = findViewById(R.id.bStarred);
        private Button bBrowse = findViewById(R.id.bBrowse);

        public Holder(){
            this.bBrowse.setOnClickListener(new bBrowseHandler());
            this.bStarred.setOnClickListener(new bStarredHandler());
            this.bRandom.setOnClickListener(new bRandomHandler());
        }

        private class bNotImplementedHandler implements View.OnClickListener{
            @Override
            public void onClick(View v) {
                //TODO: starts BrowseStarred activity
                (new NotImplementedDialog()).show(MainActivity.this.getSupportFragmentManager(), this.getClass().getName());
            }
        }
        private class bStarredHandler implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                Intent iBrowseCocktails = new Intent(MainActivity.this, BrowseDrinksActivity.class);
                iBrowseCocktails.putExtra(getString(R.string.browseDrinks_whatDrinks), WhatDrinks.STARRED);
                startActivity(iBrowseCocktails);
            }
        }
        private class bBrowseHandler implements View.OnClickListener{
            @Override
            public void onClick(View v) {
                // starts BrowseCocktails activity
                Intent iBrowseCocktails = new Intent(MainActivity.this, BrowseDrinksActivity.class);
                iBrowseCocktails.putExtra(getString(R.string.browseDrinks_whatDrinks), WhatDrinks.ALL);
                startActivity(iBrowseCocktails);
            }
        }
        private class bRandomHandler implements View.OnClickListener{
            private class OnRandomCocktailReceivedListener implements Response.Listener<DrinkPack>{

                @Override
                public void onResponse(DrinkPack response) {
                    // Starts DrinkDetails activity with a random selectedDrink
                    if (response != null && response.getDrinks() != null){
                        Intent DrinkDetailsIntent = new Intent(MainActivity.this, DrinkDetailsActivity.class);
                        DrinkDetailsIntent.putExtra(getString(R.string.DRINK_SELECTED_KEY), response.getDrinks()[0]);
                        startActivity(DrinkDetailsIntent);
                    }
                }
            }
            @Override
            public void onClick(View v) {
                // queries random drink
                DrinkDAO
                        .getInstance(MainActivity.this)
                        .queryRandomDrink(
                                null,
                                new OnRandomCocktailReceivedListener()
                        );
                // it could take some time, better inform user about what we are doing
                Toast toast = Toast.makeText(MainActivity.this, R.string.Toast_wait_server, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Holder holder = new Holder();
    }
}
