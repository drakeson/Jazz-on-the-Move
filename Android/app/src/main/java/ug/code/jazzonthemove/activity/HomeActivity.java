package ug.code.jazzonthemove.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import cz.msebera.android.httpclient.Header;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ug.code.jazzonthemove.R;
import ug.code.jazzonthemove.adapter.GridViewAdapter;
import ug.code.jazzonthemove.model.Slider;

public class HomeActivity extends AppCompatActivity {

    CarouselView carouselView;
    List<Slider> sliders;
    List<Menu> menus;
    private GridView gridView;
    private GridViewAdapter adapter;
    int[] gridViewImage = {
            R.drawable.ic_liquor, R.drawable.ic_coffee, R.drawable.ic_travel,
            R.drawable.ic_events, R.drawable.ic_entertainment, R.drawable.ic_air};
    String[] gridViewMenu = {"Liquor", "Coffee", "Travel", "Events", "Entertainment", "ON AIR"};
    String[] gridDesc = {"Tickle your\ntaste buds", "The fine brew of\nauthentic coffee",
            "From the ends of\nthe world all the way", "What is hot\non the Streets",
            "Top watched\nshows on TV", "Listen to\nOur Online Radio"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        carouselView =  findViewById(R.id.carouselView);
        carouselView.setViewListener(viewListener);

        //gridView = findViewById(R.id.gridView);
        adapter = new GridViewAdapter(this, gridViewMenu, gridViewImage, gridDesc);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((a, v, position, id) -> {
            if (position == 5){
                Toasty.info(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, AllActivity.class);
                intent.putExtra("ID", position);
                startActivity(intent);
            }
        });

        sliders = new ArrayList<>();
        loadSlider();
    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {

            View customView = getLayoutInflater().inflate(R.layout.view_custom, null);

            ImageView fruitImageView = customView.findViewById(R.id.fruitImageView);

            carouselView.setIndicatorGravity(Gravity.CENTER_HORIZONTAL| Gravity.TOP);

            Glide.with(HomeActivity.this)
                    .load(sliders.get(position).getImage())
                    .apply(RequestOptions.placeholderOf(R.drawable.placeholder).error(R.drawable.placeholder)
                            .bitmapTransform(new RoundedCorners(14)))
                    .into(fruitImageView);

            return customView;
        }
    };


    public void loadSlider(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://myhouseug.com/jazz/slider.json", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("Status Code: " +statusCode);
                Log.d("Categories", response.toString());

                if (statusCode == 200){
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Slider slider = new Slider();
                            slider.setId(obj.getString("id"));
                            slider.setImage(obj.getString("image"));
                            slider.setName(obj.getString("name"));
                            sliders.add(slider);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    carouselView.setPageCount(sliders.size());
                    Log.d("Slider", sliders.get(0).getImage());
                    carouselView.setSlideInterval(5000);
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println("Status Code: " +statusCode);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}