package io.burgrme.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.burgrme.Activities.OverviewActivity;
import io.burgrme.Constants;
import io.burgrme.Model.FoodItem;
import io.burgrme.R;

/**
 * Created by Joe on 12/21/2016.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.FoodViewHolder>{

    List<FoodItem> items;

    Context context;

    View v;

    public MainAdapter(List<FoodItem> items, Context context){
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_list_card, viewGroup, false);
        FoodViewHolder foodViewHolder = new FoodViewHolder(v,context);
        return foodViewHolder;
    }

    @Override
    public void onBindViewHolder(final FoodViewHolder foodViewHolder, int position) {

        foodViewHolder.setFoodName(items.get(position).getName());
        foodViewHolder.setOnClickListener(items.get(position));
        foodViewHolder.setShareButton(items.get(position).getName());
        foodViewHolder.setLuckyButton(items.get(position));
        setHighResImage(foodViewHolder.highResImage,position);
    }

    /**
     * Sets image of card based off resource drawable identifier
     * @param imageView - imageView to set
     * @param position - position in array
     */
    private void setIcon(ImageView imageView, int position){
        String uri = "@drawable/".concat(items.get(position).getDrawable());

        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable drawable = ContextCompat.getDrawable(context,imageResource);
        imageView.setImageDrawable(drawable);
    }

    /**
     * Sets highres image
     * @param imageView - imageView to set
     * @param position - position in array
     */
    private void setHighResImage(ImageView imageView, int position){
        String uri = "@drawable/".concat(items.get(position).getDrawable()).concat("_highres");

        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable drawable = ContextCompat.getDrawable(context,imageResource);
        imageView.setImageDrawable(drawable);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        /**
         * NON UI
         */
        Context context;

        /**
         * UI
         */
        CardView cardView;
        TextView foodName;
        ImageView icon;
        ImageView highResImage;
        Button shareButton;
        Button luckyButton;


        FoodViewHolder(View itemView, Context context) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            foodName = (TextView)itemView.findViewById(R.id.foodName);
            icon = (ImageView)itemView.findViewById(R.id.icon);
            highResImage = (ImageView)itemView.findViewById(R.id.highresFoodImage);
            shareButton = (Button)itemView.findViewById(R.id.shareButton);
            luckyButton = (Button)itemView.findViewById(R.id.luckyButton);

            this.context = context;
        }

        /**
         * Set Food name
         * @param name
         */
        private void setFoodName(String name){
            foodName.setText(name);
        }


        /**
         * Set OnClick for entire vardview
         * @param foodItem - item to pass
         */
        private void setOnClickListener(final FoodItem foodItem){
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OverviewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Constants.FEELING_LUCKY, false);
                    intent.putExtra(Constants.INTENT_EXTRA_FOOD_ITEM, foodItem);
                    context.startActivity(intent);
                }
            });
        }

        /**
         * Sets logic for share button
         * @param foodName
         */
        private void setShareButton(String foodName){
            final String shareText = context.getString(R.string.main_card_share_text).replace("%s", foodName);

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);
                }
            });
        }

        /**
         * Starts overview activity, but adds flag to just navigate the user straight to the first result
         * @param foodItem
         */
        private void setLuckyButton(final FoodItem foodItem){
            luckyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OverviewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Constants.FEELING_LUCKY, true);
                    intent.putExtra(Constants.INTENT_EXTRA_FOOD_ITEM, foodItem);
                    context.startActivity(intent);
                }
            });
        }
    }

}
