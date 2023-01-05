package entity;

import torkin.homework.cocktail.R;

public enum PerkType {
    IBA("strIBA", R.drawable.index),
    TAGS("strTags", R.drawable.icons8_price_tag_64),
    CATEGORY("strCategory", R.drawable.icons8_category_80),
    ALCOHOLIC("strAlcoholic", R.drawable.icons8_wine_glass_100),
    CREATIVE_COMMONS_CONFIRMED("strCreativeCommonsConfirmed", R.drawable.icons8_creative_commons_80),
    ;
    private String perk;
    private int thumbId;
    private PerkType(String perk, int thumbId){
        this.perk = perk;
        this.thumbId = thumbId;
    }
    @Override
    public String toString(){
        return this.perk;
    }
    public int getThumbId(){
        return this.thumbId;
    }
}
