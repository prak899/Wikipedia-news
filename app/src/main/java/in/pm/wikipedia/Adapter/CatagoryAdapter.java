/*
 * Created by Prakash on 2021.
 */

package in.pm.wikipedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.sql.Timestamp;
import java.util.List;

import in.pm.wikipedia.Model.Catagory;
import in.pm.wikipedia.Model.Featured;
import in.pm.wikipedia.R;

public class CatagoryAdapter extends RecyclerView.Adapter<CatagoryAdapter.ViewHolder> {

    private Context context;
    private List<Catagory> catagories;

    public CatagoryAdapter(Context context, List catagories) {
        this.context = context;
        this.catagories = catagories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.catagory_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(catagories.get(position));

        Catagory pu = catagories.get(position);

        holder.Title.setText(pu.getTitle());

        /*Glide.with(context)
                .load(pu.getImage_url())
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);*/

    }

    @Override
    public int getItemCount() {
        return catagories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Title;

        public ViewHolder(View itemView) {
            super(itemView);

            Title = (TextView) itemView.findViewById(R.id.formTitleTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Catagory cpu = (Catagory) view.getTag();
                }
            });

        }
    }

}
