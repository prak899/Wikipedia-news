package in.pm.wikipedia.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import in.pm.wikipedia.Model.Featured;
import in.pm.wikipedia.R;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.ViewHolder> {

    private Context context;
    private List<Featured> featured;

    public FeaturedAdapter(Context context, List featured) {
        this.context = context;
        this.featured = featured;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(featured.get(position));

        Featured pu = featured.get(position);

        holder.pId.setText(pu.getPageid());
        holder.pTitle.setText(pu.getTitle());

        Glide.with(context).load(pu.getImage_url()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return featured.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView pId, pNs;
        public TextView pTitle;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            pId = (TextView) itemView.findViewById(R.id.promoter_name);
            pNs = (TextView) itemView.findViewById(R.id.promoter_address);
            pTitle = (TextView) itemView.findViewById(R.id.promoter_address1);
            imageView = (ImageView) itemView.findViewById(R.id.promoter_user);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Featured cpu = (Featured) view.getTag();


                }
            });

        }
    }

}
