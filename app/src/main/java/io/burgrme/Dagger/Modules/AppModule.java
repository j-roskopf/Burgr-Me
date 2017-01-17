package io.burgrme.Dagger.Modules;

import android.content.Context;
import android.location.LocationManager;

import dagger.Module;
import dagger.Provides;
import io.burgrme.Logging.Logger;
import io.burgrme.Model.FoodItem;

/**
 * Created by Joe on 12/21/2016.
 */

@Module
public class AppModule {
    private Context context;

    public AppModule(Context context){
        this.context = context;
    }

    @Provides
    public LocationManager provideLocationManager(){
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Provides
    public Logger provideLogger(){
        return new Logger();
    }

    @Provides
    public FoodItem[] provideFoodItems(){
        return new FoodItem[] {
                new FoodItem("pizza", "Pizza", "Italian"),
                new FoodItem("cheeseburger", "Cheeseburger", "American"),
                new FoodItem("burrito", "Burrito", "Mexican"),
                new FoodItem("bahn_mi", "Bahn Mi", "Vietnamese"),
                new FoodItem("coffee", "Coffee" , "Drinks / Snacks"),
                new FoodItem("bakery", "Bakery", "Drinks / Snacks"),
                new FoodItem("sandwich", "Sandwich", "American")
        };
    }

}
