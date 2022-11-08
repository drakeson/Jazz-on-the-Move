package ug.code.jazzonthemove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import ug.code.jazzonthemove.R;


public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] gridViewString;
    private final String[] gridDesc;
    private final int[] gridViewImageId;

    public GridViewAdapter(Context context, String[] gridViewString, int[] gridViewImageId, String[] gridDesc) {
        mContext = context;
        this.gridViewImageId = gridViewImageId;
        this.gridViewString = gridViewString;
        this.gridDesc = gridDesc;
    }

    @Override
    public int getCount() {
        return gridViewString.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.grid_item, null);
            TextView textViewAndroid = gridViewAndroid.findViewById(R.id.android_gridview_text);
            TextView textViewDesc = gridViewAndroid.findViewById(R.id.android_gridDesc);
            ImageView imageViewAndroid = gridViewAndroid.findViewById(R.id.img);
            CardView linearLayout = gridViewAndroid.findViewById(R.id.card_view);

            textViewAndroid.setText(gridViewString[i]);
            imageViewAndroid.setImageResource(gridViewImageId[i]);
            textViewDesc.setText(gridDesc[i]);

            if (i == 0){
                linearLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            } else if (i == 1) {
                linearLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            }else if (i == 2) {
                linearLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.txr));
            }else if (i == 3) {
                linearLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.txrz));
            }else if (i == 4) {
                linearLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.txry));
            }else if (i == 5) {
                linearLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.txrzc));
            }
        } else {
            gridViewAndroid = convertView;
        }

        return gridViewAndroid;
    }
}