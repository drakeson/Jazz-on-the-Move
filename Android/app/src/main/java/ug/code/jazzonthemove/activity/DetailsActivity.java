package ug.code.jazzonthemove.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import ug.code.jazzonthemove.R;

public class DetailsActivity extends AppCompatActivity {

    TextView mName, mPrice, mDesc;
    ImageView cover;
    Button mBtn, nBtn;
    String typeF, linkF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mName = findViewById(R.id.name);
        mPrice = findViewById(R.id.price);
        mDesc = findViewById(R.id.desc);
        cover = findViewById(R.id.coverImg);
        mBtn = findViewById(R.id.book);
        nBtn = findViewById(R.id.call);

        setTitle(getIntent().getStringExtra("NAME"));
        typeF = getIntent().getStringExtra("TYPE");
        linkF = getIntent().getStringExtra("LINK");

        mName.setText(getIntent().getStringExtra("NAME"));
        mPrice.setText(getIntent().getStringExtra("POSTER"));
        mDesc.setText(getIntent().getStringExtra("DESC"));
        mPrice.setVisibility(View.GONE);
        Glide.with(this).load(getIntent().getStringExtra("BANNER"))
                .apply(new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
                .into(cover);

        //if ((typeF.equals("events")) || (typeF.equals("entertainment"))) { nBtn.setVisibility(View.GONE); }
        nBtn.setVisibility(View.GONE);
        mBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra("URL", getIntent().getStringExtra("LINK"));
            intent.putExtra("NAME", getIntent().getStringExtra("NAME"));
            startActivity(intent);
//            if ((typeF == "events") && (linkF.contains("mp3"))){
//
//            } else if ((typeF == "events") && (linkF.contains("youtube"))){
//
//            } else {
//                Intent intent = new Intent(this, WebActivity.class);
//                intent.putExtra("URL", getIntent().getStringExtra("LINK"));
//                intent.putExtra("NAME", getIntent().getStringExtra("NAME"));
//                startActivity(intent);
//            }
        });


        nBtn.setOnClickListener(v -> {

        });
    }
}