package ug.code.jazzonthemove.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class CoffeeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    CarouselView carouselView;
    List<Slider> sliders;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    ShimmerFrameLayout shimmerFrameLayout;
    ItemsAdapter itemsAdapter;
    List<Items> deals;
    LinearLayoutManager lM1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all, container, false);
        // Inflate the layout for this fragment

        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        deals = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(getContext(), deals);
        LinearLayoutManager lM2 = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(lM2);
        recyclerView.setAdapter(itemsAdapter);
        carouselView = view.findViewById(R.id.carouselView);
        carouselView.setViewListener(viewListener);
        sliders = new ArrayList<>();
        loadSlider();
        loadDeals();

        return view;
    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {

            View customView = getLayoutInflater().inflate(R.layout.view_custom, null);

            ImageView fruitImageView = customView.findViewById(R.id.fruitImageView);

            carouselView.setIndicatorGravity(Gravity.CENTER_HORIZONTAL| Gravity.TOP);

            Glide.with(getContext())
                    .load(sliders.get(position).getImage())
                    .apply(RequestOptions.placeholderOf(R.drawable.placeholder).error(R.drawable.placeholder)
                            .bitmapTransform(new RoundedCorners(14)))
                    .into(fruitImageView);

            return customView;
        }
    };

    public void loadSlider(){
        String sliderUrl = "https://empirivumatea.com/jazz/coffee.json";
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
                .whereEqualTo("type", "coffee")
                .orderBy("timestamp", Query.Direction.DESCENDING).limit(100)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult()){
                            Log.d("Successful", doc.toString());
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
                        itemsAdapter = new ItemsAdapter(getContext(), deals);
                        recyclerView.setAdapter(itemsAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toasty.error(getContext().getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    Log.d("Failed", e.toString());
                });


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
