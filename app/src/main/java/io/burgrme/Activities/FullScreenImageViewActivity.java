package io.burgrme.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ftinc.scoop.Scoop;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.burgrme.Constants;
import io.burgrme.R;
import uk.co.senab.photoview.PhotoViewAttacher;

public class FullScreenImageViewActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Scoop.getInstance().apply(this);
        setContentView(R.layout.activity_full_screen_image_view);
        ButterKnife.bind(this);



        if(getIntent().getExtras().getString(Constants.IMAGE_URL) != null){
            try{
                Glide.with(this).load(getIntent().getExtras().getString(Constants.IMAGE_URL)).into(imageView);
                PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
                mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        finish();
                    }

                    @Override
                    public void onOutsidePhotoTap() {

                    }
                });
                mAttacher.update();

            }catch (Exception e){
                Log.d("D","imageDebug with e  = " + e.getMessage());
            }

        }else{
            finish();
        }
    }
}
