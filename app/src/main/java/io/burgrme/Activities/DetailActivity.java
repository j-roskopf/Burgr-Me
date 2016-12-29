package io.burgrme.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.burgrme.BuildConfig;
import io.burgrme.Constants;
import io.burgrme.Dagger.Components.AppComponent;
import io.burgrme.Dagger.Components.DaggerAppComponent;
import io.burgrme.Dagger.Modules.AppModule;
import io.burgrme.Model.FoodItem;
import io.burgrme.R;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    FoodItem currentFoodItem;

    YelpAPI yelpAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppComponent component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        component.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        currentFoodItem = getIntent().getParcelableExtra(Constants.INTENT_EXTRA_FOOD_ITEM);

        Log.d("D","detailDebug");

        if(currentFoodItem != null){
            YelpAPIFactory apiFactory = new YelpAPIFactory(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET, BuildConfig.TOKEN, BuildConfig.TOKEN_SECRET);
            yelpAPI = apiFactory.createAPI();
            searchBusinesses("Minneapolis");

        }else{

        }

    }

    public void searchBusinesses(final String location) {

        Observable.fromCallable(new Callable<Response<SearchResponse>>() {
            @Override
            public Response<SearchResponse> call() throws Exception {
                Map<String, String> params = new HashMap<>();
                params.put("term", currentFoodItem.getName());
                Call<SearchResponse> call = yelpAPI.search(location, params);
                Response<SearchResponse> response = null;
                try{
                    Log.d("D","detailDebug making call");
                    response = call.execute();
                }catch (Exception e){
                    Log.d("D","detailDebug exception with e = " + e.getMessage() + " " + e);
                }
                return response;
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<SearchResponse>>() {
            @Override
            public void onCompleted() {
                Log.d("D","detailDebug on Completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("D","detailDebug on Error ");
            }

            @Override
            public void onNext(Response<SearchResponse> response) {
                Log.d("D","detailDebug onNext with size = " + response.body().businesses().size());
            }
        });

    }

}
