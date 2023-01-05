package entity;

public class Ingredient {
    private String name = "";
    private String measure = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = (name == null )? this.name : name ;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {

        this.measure = (measure == null)? this.measure : measure;
    }
}
