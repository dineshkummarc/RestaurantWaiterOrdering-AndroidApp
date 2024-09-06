package fortin.restaurant.digital.menu.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reeva.restaurant.R;
import fortin.restaurant.digital.menu.model.CustCuisineListPojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reeva on 9/24/2015.
 */
public class RecycleCuisineAdapter extends RecyclerView.Adapter<RecycleCuisineAdapter.ContentViewHolder> {
    View view;
    ContentViewHolder contentviewholder;
    public static List<CustCuisineListPojo> cuisinelist = new ArrayList<CustCuisineListPojo>();
    ArrayList<String> cuisinenamelist = new ArrayList<String>();
    ArrayList<String> cuisineimglist = new ArrayList<>();
    public static Context context;
    AdapterView.OnItemClickListener onItemClickListener;


        public RecycleCuisineAdapter(List<CustCuisineListPojo> cuisinelist, ArrayList<String> cuisinenamelist, ArrayList<String> cuisineimglist, Context context) {
        this.cuisinelist=cuisinelist;
        this.cuisinenamelist = cuisinenamelist;
        this.cuisineimglist = cuisineimglist;
        this.context = context;
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtcuisinename;
        ImageView imgcuisineimg;

        public ContentViewHolder(View itemView) {
            super(itemView);
            txtcuisinename = (TextView) itemView.findViewById(R.id.txtcustcuisinename);
            imgcuisineimg = (ImageView) itemView.findViewById(R.id.imgcustcuisineimg);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(v == itemView)
            {
                int pos = getPosition();
                CustCuisine.cuiposid = cuisinelist.get(pos).getCusineid();
                        Intent intent=new Intent(context,CustDishes.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
            }
        }
    }


    @Override
    public RecycleCuisineAdapter.ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_custcuisinelist, parent, false);

           contentviewholder = new ContentViewHolder(view);


        return contentviewholder;

    }

    @Override
    public void onBindViewHolder(final RecycleCuisineAdapter.ContentViewHolder holder, final int position) {




        holder.txtcuisinename.setText(cuisinelist.get(position).getCusinename());

        Picasso.with(context)
                .load(DataManager.URL+cuisinelist.get(position).getCusineimage())
                .into(holder.imgcuisineimg);
    }


    @Override
    public int getItemCount() {
        return cuisinelist.size();

    }

}