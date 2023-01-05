package dao.thecocktaildb;

public enum IngredientThumbnailSize {
    INGREDIENT_NORMAL(".png"),
    INGREDIENT_SMALL("-Small.png"),
    INGREDIENT_MEDIUM("-Medium.png"),
    ;
    private String size;
    private IngredientThumbnailSize(String size){
        this.size = size;
    }
    @Override
    public String toString(){
        return this.size;
    }
}
