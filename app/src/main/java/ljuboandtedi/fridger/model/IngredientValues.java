package ljuboandtedi.fridger.model;

/**
 * Created by NoLight on 10.10.2016 г..
 */

public class IngredientValues {
    private String type;
    private double value;

    public IngredientValues(String type, double value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }
    public double getValue() {
        return value;
    }
}
