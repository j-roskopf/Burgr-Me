package io.burgrme.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.burgrme.Adapters.MainAdapter;
import io.burgrme.BuildConfig;
import io.burgrme.Dagger.Components.AppComponent;
import io.burgrme.Dagger.Components.DaggerAppComponent;
import io.burgrme.Dagger.Modules.AppModule;
import io.burgrme.Logging.Logger;
import io.burgrme.Model.FoodItem;
import io.burgrme.Model.responses.YelpTokenResponse;
import io.burgrme.R;
import io.burgrme.Retrofit.YelpService;
import io.burgrme.Retrofit.YelpServiceFactory;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    /**
     * UI
     */

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    /**
     * DAGGER INJECTED
     */

    @Inject
    Logger logger;

    @Inject
    FoodItem[] foodItems;

    /**
     * NON UI
     */

    Context context;
    SharedPreferences mPrefs;
    MainAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppComponent component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        component.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initVars();

        setupDisplay();


    }

    /**
     * Initialize variables
     */
    private void initVars(){
        context = this;
        mPrefs = getSharedPreferences("Main", MODE_PRIVATE);
        YelpServiceFactory.getService().getToken(BuildConfig.CLIENT_ID,BuildConfig.CLIENT_SECRET,new Callback<YelpTokenResponse>() {
            @Override
            public void success(YelpTokenResponse yelpTokenResponse, Response response) {
                mPrefs.edit().putString("access_token",yelpTokenResponse.access_token).apply();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("D","Yelp fail to get access token error = " + error.toString());
            }
        });
    }

    /**
     * Sets display of contents of Enum in Constants.java in recycler view
     */
    private void setupDisplay(){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);

        ArrayList<FoodItem> toPass = new ArrayList<>();

        for(int i = 0; i < foodItems.length; i++){
            toPass.add(foodItems[i]);
        }

        mainAdapter = new MainAdapter(toPass, context);
        recyclerView.setAdapter(mainAdapter);

    }
}
