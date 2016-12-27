package io.burgrme.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.burgrme.Activities.DetailActivity;
import io.burgrme.Constants;
import io.burgrme.Model.FoodItem;
import io.burgrme.R;

/**
 * Created by Joe on 12/21/2016.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.FoodViewHolder>{

    List<FoodItem> items;

    Context context;

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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_list_card, viewGroup, false);
        FoodViewHolder foodViewHolder = new FoodViewHolder(v);
        return foodViewHolder;
    }

    @Override
    public void onBindViewHolder(FoodViewHolder personViewHolder, final int position) {
        personViewHolder.foodName.setText(items.get(position).getName());

        personViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.INTENT_EXTRA_FOOD_ITEM, items.get(position));
                context.startActivity(intent);
            }
        });

        setImage(personViewHolder.imageView,position);
    }

    /**
     * Sets image of card based off resource drawable identifier
     * @param imageView - imageView to set
     * @param position - position in array
     */
    private void setImage(ImageView imageView, int position){
        String uri = "@drawable/".concat(items.get(position).getDrawable());

        Log.d("D","settingImageView to " + uri);

        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable drawable = ContextCompat.getDrawable(context,imageResource);
        imageView.setImageDrawable(drawable);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView foodName;
        ImageView imageView;

        FoodViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            foodName = (TextView)itemView.findViewById(R.id.foodName);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
        }
    }

}
