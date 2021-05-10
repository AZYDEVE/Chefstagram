package edu.neu.madcourse.numad20f_yang_ylescupidez.model;

import java.io.Serializable;

public class Ingredient implements Serializable {

    private String ingredient;
    private String size;
    private String unit;

    public Ingredient() {
    }

    public Ingredient(String ingredient, String size, String unit) {
        this.ingredient = ingredient;
        this.size = size;
        this.unit = unit;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
