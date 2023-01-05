package dao.thecocktaildb;

public enum DrinkThumbnailSize {
    DRINK_NORMAL(""),
    DRINK_PREVIEW("/preview"),
    ;
    private String size;
    private DrinkThumbnailSize(String size){
        this.size = size;
    }
    @Override
    public String toString(){
        return this.size;
    }
}
