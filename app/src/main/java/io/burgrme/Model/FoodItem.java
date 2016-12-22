package io.burgrme.Model;

import javax.inject.Inject;

/**
 * Created by Joe on 12/21/2016.
 */

public class FoodItem {

    @Inject
    public FoodItem(String drawable, String name){
        this.drawable = drawable;
        this.name = name;
    }

    private String drawable;
    private String name;

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
