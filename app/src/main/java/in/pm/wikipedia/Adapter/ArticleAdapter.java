/*
 * Created by Prakash on 2021.
 */
package in.pm.wikipedia.Adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.pm.wikipedia.Model.Article;
import in.pm.wikipedia.Model.Featured;
import in.pm.wikipedia.R;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private Context context;
    private List<Article> articles;

    public ArticleAdapter(Context context, List articles) {
        this.context = context;
        this.articles = articles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.random_article, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(articles.get(position));

        Article pu = articles.get(position);

        holder.pId.setText(pu.getTitle());
        holder.pNs.setText(pu.getContent());

        //.pNs.setText(Html.fromHtml(pu.getContent(), Html.FROM_HTML_MODE_COMPACT));

        //Glide.with(context).load(pu.getImage_url()).into(holder.imageView);

        /*Glide.with(context)
                .load(pu.getImage_url())
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);*/

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView pId, pNs;

        public ViewHolder(View itemView) {
            super(itemView);

            pId = (TextView) itemView.findViewById(R.id.promoter_name);
            pNs = (TextView) itemView.findViewById(R.id.promoter_address);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Article cpu = (Article) view.getTag();
                }
            });

        }
    }

}
