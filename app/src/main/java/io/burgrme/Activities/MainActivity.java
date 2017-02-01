package io.burgrme.Activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.ftinc.scoop.Scoop;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.burgrme.Adapters.MainAdapter;
import io.burgrme.BuildConfig;
import io.burgrme.Constants;
import io.burgrme.Dagger.Components.AppComponent;
import io.burgrme.Dagger.Components.DaggerAppComponent;
import io.burgrme.Dagger.Modules.AppModule;
import io.burgrme.Logging.Logger;
import io.burgrme.Model.FoodItem;
import io.burgrme.Model.responses.YelpTokenResponse;
import io.burgrme.R;
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

    @BindView(R.id.parent_view)
    RelativeLayout relativeLayout;

    @BindView(R.id.floatingSearchView)
    FloatingSearchView floatingSearchView;

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
        Scoop.getInstance().choose(Scoop.getInstance().getFlavors().get(1));
        Scoop.getInstance().apply(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initVars();

        setupDisplay();

        setupSearchBar();

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

    /**
     * Adds logic for search bar at the top
     */
    private void setupSearchBar(){
        floatingSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if(item.getItemId() == R.id.action_voice_rec){
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"What are you hungry for?");
                    try {
                        startActivityForResult(intent, Constants.SPEECH_KEY);
                    } catch (ActivityNotFoundException a) {
                        Snackbar.make(relativeLayout, "Sorry! Your device doesn't support speech input", Snackbar.LENGTH_LONG).show();
                    }
                }
            }

        });

        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                Intent intent = new Intent(context, OverviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.FEELING_LUCKY, false);
                intent.putExtra(Constants.INTENT_EXTRA_FOOD_ITEM, new FoodItem("",currentQuery,""));
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == Constants.SPEECH_KEY && resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            if(results != null && results.get(0) != null){

                Snackbar.make(relativeLayout, "Searching for " + results.get(0), Snackbar.LENGTH_LONG).show();

                Intent intent = new Intent(context, OverviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.FEELING_LUCKY, false);
                intent.putExtra(Constants.INTENT_EXTRA_FOOD_ITEM, new FoodItem("",results.get(0),""));
                context.startActivity(intent);
            }else{
                Snackbar.make(relativeLayout, "Something went wrong", Snackbar.LENGTH_LONG).show();
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.favorites:
                Intent intent = new Intent(context, FavoritesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
