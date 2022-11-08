package ug.code.jazzonthemove.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import ug.code.jazzonthemove.R;
import ug.code.jazzonthemove.adapter.ItemsAdapter;
import ug.code.jazzonthemove.model.Items;
import ug.code.jazzonthemove.model.Slider;

public class AllActivity extends AppCompatActivity {
    CarouselView carouselView;
    List<Slider> sliders;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    ShimmerFrameLayout shimmerFrameLayout;
    ItemsAdapter itemsAdapter;
    List<Items> deals;
    LinearLayoutManager lM1;
    String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        int id = getIntent().getIntExtra("ID", 0);
        switch (id) { case 1: setTitle("Liquor"); url = "liqour"; break; case 2: url = "coffee"; setTitle("Coffee"); break;
        case 3: url = "travel"; setTitle("Travel"); break; case 4: url = "events"; setTitle("Events"); break;
        case 5: url = "lifestyle"; setTitle("Lifestyle"); break;
        }
        carouselView = findViewById(R.id.carouselView);
        carouselView.setViewListener(viewListener);
        sliders = new ArrayList<>();
        loadSlider();
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        deals = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(this, deals);
        LinearLayoutManager lM2 = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(lM2);
        recyclerView.setAdapter(itemsAdapter);

        loadDeals();
    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {

            View customView = getLayoutInflater().inflate(R.layout.view_custom, null);

            ImageView fruitImageView = customView.findViewById(R.id.fruitImageView);

            carouselView.setIndicatorGravity(Gravity.CENTER_HORIZONTAL| Gravity.TOP);

            Glide.with(AllActivity.this)
                    .load(sliders.get(position).getImage())
                    .apply(RequestOptions.placeholderOf(R.drawable.placeholder).error(R.drawable.placeholder)
                            .bitmapTransform(new RoundedCorners(14)))
                    .into(fruitImageView);

            return customView;
        }
    };

    public void loadSlider(){
        String sliderUrl = "https://empirivumatea.com/jazz/"+url+".json";
        Log.d("Sliders", sliderUrl);
        System.out.println("Sliders Code: " +sliderUrl);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(sliderUrl, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
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

    private void loadDeals() {
        db.collection("deals")
                .whereEqualTo("type", url)
                .orderBy("timestamp", Query.Direction.DESCENDING).limit(100)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult()){
                            Items model = new Items(
                                    doc.getString("id"),
                                    doc.getString("name"),
                                    doc.getString("poster"),
                                    doc.getString("banner"),
                                    doc.getString("link"),
                                    doc.getString("type"),
                                    doc.getString("desc")
                            );
                            deals.add(model);
                        }
                        itemsAdapter = new ItemsAdapter(AllActivity.this, deals);
                        recyclerView.setAdapter(itemsAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toasty.error(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    Log.d("Failed", e.toString());
                });


    }
}