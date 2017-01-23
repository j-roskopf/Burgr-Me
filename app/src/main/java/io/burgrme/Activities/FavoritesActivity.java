package io.burgrme.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.burgrme.Adapters.FavoriteAdapter;
import io.burgrme.Model.Business;
import io.burgrme.R;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;

public class FavoritesActivity extends AppCompatActivity {

    private Realm realm;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.noFavoritesContainer)
    LinearLayout noFavoritesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();

        //Get items
        getFavorites();
    }

    /**
     * Retreives favorites from database
     */
    private void getFavorites(){

        OrderedRealmCollection<Business> data = realm.where(Business.class).findAll();
        if(data.size() == 0){
            noFavoritesContainer.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(new FavoriteAdapter(this, data));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    /**
     * Deletes business in database. Called from FavoritesAdapter
     * @param business
     */
    public void deleteItem(final Business business) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Business.class).equalTo("name", business.name)
                        .findAll()
                        .deleteAllFromRealm();
            }
        });

    }
}
