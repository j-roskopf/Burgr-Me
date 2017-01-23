package io.burgrme.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.burgrme.Activities.BusinessDetailActivity;
import io.burgrme.Activities.FavoritesActivity;
import io.burgrme.Constants;
import io.burgrme.Model.Business;
import io.burgrme.R;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Joe on 12/21/2016.
 */

public class FavoriteAdapter extends RealmRecyclerViewAdapter<Business, FavoriteAdapter.BusinessViewHolder> {



    private final FavoritesActivity activity;

    public FavoriteAdapter(FavoritesActivity activity, OrderedRealmCollection<Business> data) {
        super(activity, data, true);
        this.activity = activity;
    }



    @Override
    public BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list_card, parent, false);
        return new BusinessViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(BusinessViewHolder foodViewHolder, int position) {
        Business business = getData().get(position);
        foodViewHolder.business = business;

        foodViewHolder.setFoodName(business.name);
        foodViewHolder.setOnClickListener(business);
        foodViewHolder.setDeleteButton(business);
        setHighResImage(foodViewHolder.highResImage,business);

    }

    /**
     * Sets highres image
     * @param imageView - imageView to set
     * @param business - business object
     */
    private void setHighResImage(ImageView imageView, Business business){

        Glide.with(context).load(business.image_url).into(imageView);

    }




    public class BusinessViewHolder extends RecyclerView.ViewHolder {
        /**
         * NON UI
         */
        Context context;

        Business business;

        /**
         * UI
         */
        CardView cardView;
        TextView foodName;
        ImageView icon;
        ImageView highResImage;
        Button deleteButton;




        BusinessViewHolder(View itemView, Context context) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            foodName = (TextView) itemView.findViewById(R.id.foodName);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            highResImage = (ImageView) itemView.findViewById(R.id.highresFoodImage);
            deleteButton = (Button) itemView.findViewById(R.id.deleteButton);

            this.context = context;
        }

        /**
         * Set Food name
         *
         * @param name
         */
        private void setFoodName(String name) {
            foodName.setText(name);
        }

        /**
         * Adds logic for delete button
         *
         * @param business
         */
        private void setDeleteButton(final Business business) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Won't be able to undo!")
                            .setConfirmText("Yes,delete it!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    deleteBusiness(business);
                                }
                            })
                            .show();
                }
            });
        }

        /**
         * Deletes business from database
         */
        private void deleteBusiness(Business businessToDelete) {
            activity.deleteItem(businessToDelete);
        }


        /**
         * Set OnClick for entire vardview
         *
         * @param business - item to pass
         */
        private void setOnClickListener(final Business business) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, BusinessDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Constants.BUNDLE_EXTRA_BUSINESS, business);
                    context.startActivity(intent);
                }
            });
        }
    }


}
