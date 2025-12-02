package com.example.moviesapp.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviesapp.R;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivPosterDetail;
    private TextView tvTitleDetail, tvOverviewDetail, tvRatingDetail, tvDateDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivPosterDetail = findViewById(R.id.ivPosterDetail);
        tvTitleDetail = findViewById(R.id.tvTitleDetail);
        tvOverviewDetail = findViewById(R.id.tvOverviewDetail);
        tvRatingDetail = findViewById(R.id.tvRatingDetail);
        tvDateDetail = findViewById(R.id.tvDateDetail);

        String title = getIntent().getStringExtra("title");
        String overview = getIntent().getStringExtra("overview");
        String posterPath = getIntent().getStringExtra("poster_path");
        float rating = getIntent().getFloatExtra("rating", 0f);
        String releaseDate = getIntent().getStringExtra("release_date");

        tvTitleDetail.setText(title);
        tvOverviewDetail.setText(overview);
        tvRatingDetail.setText("★ " + rating);
        tvDateDetail.setText("Fecha de estreno: " + releaseDate);

        String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(ivPosterDetail);
    }
}
