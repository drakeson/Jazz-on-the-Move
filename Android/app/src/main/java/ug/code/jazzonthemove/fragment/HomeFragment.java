package ug.code.jazzonthemove.fragment;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import ug.code.jazzonthemove.activity.AllActivity;
import ug.code.jazzonthemove.activity.HomeActivity;
import ug.code.jazzonthemove.activity.VideoActivity;
import ug.code.jazzonthemove.adapter.ItemsAdapter;
import ug.code.jazzonthemove.model.Items;
import ug.code.jazzonthemove.model.Slider;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    CarouselView carouselView;
    List<Slider> sliders;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    ShimmerFrameLayout shimmerFrameLayout;
    ItemsAdapter itemsAdapter;
    List<Items> deals;
    FloatingActionButton fab, fab2, fab3;
    LinearLayout  fabLayout2, fabLayout3;
    View fabBGLayout;
    boolean isFABOpen = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        carouselView = view.findViewById(R.id.carouselView);
        carouselView.setViewListener(viewListener);
        sliders = new ArrayList<>();
        loadSlider();
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        deals = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(getContext(), deals);
        LinearLayoutManager lM2 = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(lM2);
        recyclerView.setAdapter(itemsAdapter);

        fabLayout2 = view.findViewById(R.id.fabLayout2);
        fabLayout3 = view.findViewById(R.id.fabLayout3);
        fab = view.findViewById(R.id.fab);
        fab2 = view.findViewById(R.id.fab2);
        fab3 = view.findViewById(R.id.fab3);
        fabBGLayout = view.findViewById(R.id.fabBGLayout);

        fab.setOnClickListener(view1 -> {
            if (!isFABOpen) {
                showFABMenu();
            } else {
                closeFABMenu();
            }
        });

        fab2.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), VideoActivity.class);
            startActivity(intent);
        });

        fabBGLayout.setOnClickListener(view12 -> closeFABMenu());

        loadDeals();
        return view;
    }

    private void showFABMenu() {
        isFABOpen = true;
        fabLayout2.setVisibility(View.VISIBLE);
        fabLayout3.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);
        fab.animate().rotationBy(180);
        float baseTranslation = fabLayout2.getTranslationY(); // The translationYs are based on the existing position, relatively
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_55) + baseTranslation);
        fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_100) + baseTranslation);
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotation(0);
        fabLayout2.animate().translationY(0);
        fabLayout3.animate().translationY(0);
        fabLayout3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fabLayout2.setVisibility(View.GONE);
                    fabLayout3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
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
        //String url = "https://empirivumatea.com/jazz/"+
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://myhouseug.com/jazz/slider.json", new JsonHttpResponseHandler(){
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
                .orderBy("timestamp", Query.Direction.DESCENDING).limit(4)
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        mListener = null;
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