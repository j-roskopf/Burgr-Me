package io.burgrme.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.burgrme.Adapters.MainAdapter;
import io.burgrme.Dagger.Components.AppComponent;
import io.burgrme.Dagger.Components.DaggerAppComponent;
import io.burgrme.Dagger.Modules.AppModule;
import io.burgrme.Logging.Logger;
import io.burgrme.Model.FoodItem;
import io.burgrme.R;

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
