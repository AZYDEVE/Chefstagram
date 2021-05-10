package edu.neu.madcourse.numad20f_yang_ylescupidez.model;

import java.io.Serializable;

public class RecipeStep implements Serializable {
    private String instructions;

    public RecipeStep(String instructions) {
        this.instructions = instructions;
    }

    public RecipeStep() {
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
