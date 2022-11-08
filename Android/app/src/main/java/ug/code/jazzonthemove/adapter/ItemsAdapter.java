package ug.code.jazzonthemove.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import ug.code.jazzonthemove.R;
import ug.code.jazzonthemove.activity.DetailsActivity;
import ug.code.jazzonthemove.model.Items;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private Context mContext;
    private List<Items> dealsList;

    public ItemsAdapter(Context context, List<Items> deal){
        mContext = context;
        dealsList = deal;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameLayout;
        public CardView cardView;
        public ImageView image;
        public TextView name, price, oldprice;

        public ViewHolder(View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.frame);
            cardView = itemView.findViewById(R.id.card);
            image = itemView.findViewById(R.id.imageMain);
            name = itemView.findViewById(R.id.title);
            oldprice = itemView.findViewById(R.id.loveCount);
            itemView.setTag(this);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_gallery14, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Items deals = dealsList.get(position);
        holder.name.setText(deals.getName());
        holder.cardView.setCardBackgroundColor(Color.TRANSPARENT);
        holder.cardView.setCardElevation(0);
        holder.frameLayout.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        holder.oldprice.setText(deals.getType().toUpperCase(Locale.ROOT));

        Glide.with(mContext).load(deals.getPoster())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
                .into(holder.image);

        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putExtra("ID", deals.getId());
            intent.putExtra("NAME", deals.getName());
            intent.putExtra("BANNER", deals.getBanner());
            intent.putExtra("DESC", deals.getDesc());
            intent.putExtra("LINK", deals.getLink());
            intent.putExtra("TYPE", deals.getType());
            intent.putExtra("POSTER", deals.getPoster());
            mContext.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() { return dealsList.size(); }

    private Object getItem(int location) { return dealsList.get(location); }
}
