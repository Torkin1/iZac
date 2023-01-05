package view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import control.staradrink.ResultCode;
import control.staradrink.StarredDrinksRepo;
import control.TheCocktailDBRepo;
import control.drinkquerier.DrinkQuerier;
import control.drinkquerier.EndReachedException;
import control.drinkquerier.WhatDrinks;
import dao.thecocktaildb.DrinkDAO;
import dao.thecocktaildb.DrinkThumbnailSize;
import entity.Drink;
import entity.DrinkPack;
import torkin.homework.cocktail.R;

public class BrowseDrinksActivity extends AppCompatActivity {
    // Browses a set of drinks provided by a DrinkQuerier
    private class Holder{

        private class OnSearchBarClick implements View.OnClickListener{
            @Override
            public void onClick(View v) {
                // TODO: filters items of recyclerview by ingredient or drink name
                (new NotImplementedDialog()).show(BrowseDrinksActivity.this.getSupportFragmentManager(), this.getClass().getName());
            }
        }

        private SearchView svSearch = findViewById(R.id.svSearch);
        private RecyclerView rvDrinks = findViewById(R.id.rvDrinks);
        private ProgressBar pbLoading = findViewById(R.id.pbLoading);
        Holder(){
            // Binding views and listeners
            svSearch.setOnSearchClickListener(new OnSearchBarClick());
            this.rvDrinks.setLayoutManager(new LinearLayoutManager(BrowseDrinksActivity.this));
            this.rvDrinks.setAdapter(BrowseDrinksActivity.this.getDrinksAdapter());
            pbLoading.setIndeterminate(true);
        }

    }

    private DrinkQuerier drinkQuerier;
    private List<Drink> allDrinks;
    private DrinksAdapter drinksAdapter;
    private Holder holder;
    private int lastDrinkItemClicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_cocktails);
        this.drinksAdapter = new DrinksAdapter();
        this.holder = new Holder();
        if(savedInstanceState != null){
            this.allDrinks = (List<Drink>) savedInstanceState.get(getString(R.string.browseDrinks_allDrinks));
            this.drinkQuerier = (DrinkQuerier) savedInstanceState.get(getString(R.string.browseDrinks_whatDrinks));
            this.holder.pbLoading.setVisibility(View.INVISIBLE);
        }
        else{
            // this is the first time this activity is launched, so it needs to know for witch case it was called.
            this.allDrinks = new ArrayList<>();
            switch ((WhatDrinks) this.getIntent().getSerializableExtra(getString(R.string.browseDrinks_whatDrinks))){
                case ALL:
                    this.drinkQuerier = TheCocktailDBRepo
                            .getInstance()
                            .getAllDrinksQuerier(
                                    this,
                                    new OnDrinksReceived(this.holder, this.drinksAdapter));
                    break;
                case STARRED:
                    this.drinkQuerier = StarredDrinksRepo
                            .getInstance()
                            .getStarredDrinksQuerier(
                                    this,
                                    new OnStarredDrinksLoaded(this.holder, this.drinksAdapter));
                    break;
            }
            // loads first set of drinks
            try {
                this.drinkQuerier.queryNextSet();
            } catch (EndReachedException e) {
                // no-op, this exception should never be thrown at this point
                Log.w(this.getClass().getName(), e.toString());
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.KEY_ALL_DRINKS), (ArrayList<? extends Parcelable>) this.allDrinks);
        outState.putParcelable(getString(R.string.browseDrinks_whatDrinks), this.drinkQuerier);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        this.drinksAdapter.notifyItemChanged(this.lastDrinkItemClicked);
    }

    public int getLastDrinkItemClicked() {
        return lastDrinkItemClicked;
    }

    public void setLastDrinkItemClicked(int lastDrinkItemClicked) {
        this.lastDrinkItemClicked = lastDrinkItemClicked;
    }

    public DrinkQuerier getDrinkQuerier() {
        return drinkQuerier;
    }

    public List<Drink> getAllDrinks() {
        return allDrinks;
    }

    public DrinksAdapter getDrinksAdapter() {
        return drinksAdapter;
    }

    public Holder getHolder() {
        return holder;
    }

    private class OnDrinksReceived implements Response.Listener<DrinkPack>{

        // Updates data set of adapter with drinks received. First drinks received are also the first to be added to data set.

        private Holder holder;
        private DrinksAdapter drinksAdapter;

        OnDrinksReceived(Holder holder, DrinksAdapter drinksAdapter) {
            this.holder = holder;
            this.drinksAdapter = drinksAdapter;
        }

        @Override
        public void onResponse(DrinkPack response) {
            // If drink set queried is empty (say, no drink starts with a given letter) queries next set
            if (response.getDrinks() == null){
                try {
                    BrowseDrinksActivity.this.drinkQuerier.queryNextSet();
                } catch (EndReachedException e) {
                    // no-op
                }
            }
            else {
                // Updates DrinksAdapter data set
                allDrinks.addAll(Arrays.asList(response.getDrinks()));
                Log.i(this.getClass().getName(), String.format("AllDrinks counts %s items", allDrinks.size()));
                if (holder.pbLoading.getVisibility() == View.VISIBLE) {
                    holder.pbLoading.setVisibility(View.GONE);
                }
                drinksAdapter.notifyDataSetChanged();
            }
        }
    }
    private class OnStarredDrinksLoaded implements Observer<List<Drink>>{

        private Holder holder;
        private DrinksAdapter drinksAdapter;

        public OnStarredDrinksLoaded(Holder holder, DrinksAdapter drinksAdapter){
            this.holder = holder;
            this.drinksAdapter = drinksAdapter;
        }

        @Override
        public void onChanged(List<Drink> drinks) {
            // updates all drinks in response to changes made to all starred drinks in db
            if (drinks != null){
                allDrinks = drinks;
                if (holder.pbLoading.getVisibility() == View.VISIBLE) {
                    holder.pbLoading.setVisibility(View.GONE);
                }
                drinksAdapter.notifyDataSetChanged();
            }
        }
    }
    private class DrinksAdapter extends RecyclerView.Adapter<DrinksItem>{

        private class OnIsStarredResponse extends Handler{

            private DrinksItem drinksItem;

            public OnIsStarredResponse(@NonNull DrinksItem drinksItem){
                this.drinksItem = drinksItem;
            }

            @Override
            public void handleMessage(Message msg){
                if (msg.what == ResultCode.STARRED.getCode()){
                    this.drinksItem.ivStarred.setVisibility(View.VISIBLE);
                }
                else if (msg.what == ResultCode.NOT_STARRED.getCode()){
                    this.drinksItem.ivStarred.setVisibility(View.INVISIBLE);
                }
            }
        }

        @NonNull @Override
        public DrinksItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CardView cvListDrinkItem = (CardView) BrowseDrinksActivity.this.getLayoutInflater().inflate(R.layout.list_cocktails_item, parent, false);
            return new DrinksItem(cvListDrinkItem);
        }

        @Override
        public void onBindViewHolder(@NonNull final DrinksItem viewHolder, final int position) {
            // If last item is being displayed, queries drinks with starting letter next in the alphabet to the one of currently displaying drinks
            Log.i(this.getClass().getName(), String.format("getItemsCount returned %d", this.getItemCount()));
            if (position == allDrinks.size() - 1){
                try {
                    BrowseDrinksActivity.this.drinkQuerier.queryNextSet();
                } catch (EndReachedException e) {
                    // no-op (all drinks queried)
                }
            }
            // Populating drink item with data
            viewHolder.tvCocktailName.setText(allDrinks.get(position).getStrDrink() == null ? getString(R.string.not_available) : allDrinks.get(position).getStrDrink());
            Log.i(this.getClass().getName(), allDrinks.get(position).getStrDrink() + allDrinks.get(position).getAllIngredients().toString() + allDrinks.get(position).getAllIngredients().size());
            viewHolder.tvNumOfIngredients.setText(
                    String.format(getString(R.string.browsedrinks_drinkitem_subtitle), allDrinks.get(position).getAllIngredients().size()));
            if (allDrinks.get(position).getStrAlcoholic().compareTo("Alcoholic") != 0){
                viewHolder.ivAlcoholic.setVisibility(View.INVISIBLE);
            }
            else{
                viewHolder.ivAlcoholic.setVisibility(View.VISIBLE);
            }
            // check if cocktail id is present in starred cocktails list and set ivStarred visibility accordingly
            StarredDrinksRepo.getInstance().isStarred(
                    BrowseDrinksActivity.this,
                    new OnIsStarredResponse(viewHolder),
                    allDrinks.get(position)
            );
            // Quering TheCocktailDB for Thumbnail Image
            DrinkDAO.getInstance(BrowseDrinksActivity.this).queryDrinkThumbnail(
                    allDrinks.get(position).getStrDrinkThumb(),
                    DrinkThumbnailSize.DRINK_PREVIEW,
                    null,
                    // Listener must be declared here, otherwise it would be hard to pass holder param to the listener
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            Bitmap thumb = response.copy(Bitmap.Config.ARGB_8888, true);
                            viewHolder.ivCocktail.setImageBitmap(thumb);
                        }
                    }
            );

        }

        @Override
        public int getItemCount() {
            if (allDrinks == null){
                return 0;
            }
            return allDrinks.size();
        }
    }

    private class DrinksItem extends RecyclerView.ViewHolder{

        private class OnDrinksItemClick implements View.OnClickListener{
            @Override
            public void onClick(View v) {
                // When a DrinkItem is clicked it starts an activity displaying details of drink
                BrowseDrinksActivity.this.setLastDrinkItemClicked(getAdapterPosition()) ;
                Intent drinkDetailsIntent = new Intent(BrowseDrinksActivity.this, DrinkDetailsActivity.class);
                drinkDetailsIntent
                        .putExtra(
                        getString(R.string.DRINK_SELECTED_KEY),
                        BrowseDrinksActivity
                                .this
                                .allDrinks
                                .get(DrinksItem.this.getAdapterPosition())
                );
                BrowseDrinksActivity.this.startActivity(drinkDetailsIntent);
            }
        }

        // Holds data to be presented in rvCocktails

        final ImageView ivCocktail;
        final TextView tvCocktailName;
        final TextView tvNumOfIngredients;
        final ImageView ivAlcoholic;
        final ImageView ivStarred;

        DrinksItem(@NonNull View itemView) {
            super(itemView);
            this.ivCocktail = itemView.findViewById(R.id.ivCocktailPreview);
            tvCocktailName = itemView.findViewById(R.id.tvCocktailName);
            tvNumOfIngredients = itemView.findViewById(R.id.tvNumOfIngredients);
            ivAlcoholic = itemView.findViewById(R.id.ivAlcoholic);
            ivStarred = itemView.findViewById(R.id.ivStarred);
            itemView.setOnClickListener(new OnDrinksItemClick());
        }
    }
}
