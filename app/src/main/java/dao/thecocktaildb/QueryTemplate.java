package dao.thecocktaildb;

public enum QueryTemplate {
    DRINK_BY_NAME( "https://www.thecocktaildb.com/api/json/v1/1/search.php?s="),
    DRINK_BY_FIRST_LETTER("https://www.thecocktaildb.com/api/json/v1/1/search.php?f="),
    INGREDIENT_BY_FIRST_LETTER("https://www.thecocktaildb.com/api/json/v1/1/search.php?i="),
    DRINK_BY_ID("https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i="),
    INGREDIENT_BY_ID("https://www.thecocktaildb.com/api/json/v1/1/lookup.php?iid="),
    DRINK_RANDOM("https://www.thecocktaildb.com/api/json/v1/1/random.php"),
    DRINK_BY_INGREDIENT("https://www.thecocktaildb.com/api/json/v1/1/filter.php?i="),
    DRINK_BY_ALCOHOLIC("https://www.thecocktaildb.com/api/json/v1/1/filter.php?a="),
    DRINK_BY_CATEGORY("https://www.thecocktaildb.com/api/json/v1/1/filter.php?c="),
    DRINK_BY_GLASS("https://www.thecocktaildb.com/api/json/v1/1/filter.php?g="),
    INGREDIENT_THUMB("https://www.thecocktaildb.com/images/ingredients/"),
    ;
    private final String template;

    private QueryTemplate(String template){
        this.template = template;
    }

    @Override
    public String toString(){
        return this.template;
    }
}
