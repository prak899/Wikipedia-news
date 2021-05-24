package in.pm.wikipedia.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.jb.dev.progress_indicator.fadeProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.pm.wikipedia.Adapter.FeaturedAdapter;
import in.pm.wikipedia.MainActivity;
import in.pm.wikipedia.Model.Featured;
import in.pm.wikipedia.R;

public class Landing extends AppCompatActivity {

    private static final String TAG = "sd";
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<Featured> featuredList;

    RequestQueue rq;


    com.jb.dev.progress_indicator.fadeProgressBar dotBounceProgressBar;
    TextView EmptyView, HeaderName;

    TabItem Explore, Digital, SEO;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        UIinit();

        sendFeaturedRequest();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0: sendFeaturedRequest();
                        break;
                    case 1: sendRandomRequest();
                        break;
                    case 2: sendCatagoryRequest();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void sendCatagoryRequest() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        HeaderName.clearAnimation();
        HeaderName.startAnimation(a);
        HeaderName.setText("Catagory List");
        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
    }

    private void sendRandomRequest() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        HeaderName.clearAnimation();
        HeaderName.startAnimation(a);
        HeaderName.setText("Random Articles");
        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
    }

    private void UIinit() {
        dotBounceProgressBar = (fadeProgressBar) findViewById(R.id.dotBounce);
        EmptyView = (TextView) findViewById(R.id.empty_view);
        rq = Volley.newRequestQueue(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        featuredList = new ArrayList<>();

        HeaderName= findViewById(R.id.header_name);
        tabLayout = findViewById(R.id.tab_lay);
    }

    public void sendFeaturedRequest() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        HeaderName.clearAnimation();
        HeaderName.startAnimation(a);
        HeaderName.setText("Featured Articles");

        String url = "https://commons.wikimedia.org/w/api.php?action=query&prop=imageinfo&iiprop=timestamp%7Cuser%7Curl&generator=categorymembers&gcmtype=file&gcmtitle=Category:Featured_pictures_on_Wikimedia_Commons&format=json&utf8";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        featuredList.clear();
                        Log.d("TAG", "onResponse: " + response);

                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject query = response.getJSONObject("query");
                                JSONObject query1 = query.getJSONObject("pages");

                                Iterator iterator  = query1.keys();
                                String key = null;

                                while(iterator.hasNext()){

                                    key = (String)iterator.next();

                                    JSONArray json = (JSONArray) ((JSONObject)query1.get(key)).get("imageinfo");
                                    Featured featured = new Featured();

                                    for (int j=0; j<json.length();j++){

                                        JSONObject productObjects = json.getJSONObject(i);

                                        featured.setImage_url(productObjects.getString("url"));
                                        featured.setPageid(productObjects.getString("user"));
                                        featured.setTitle(productObjects.getString("timestamp"));

                                    }
                                    featuredList.add(featured);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("TAG", "onResponse: " + e);
                            }


                        }
                        mAdapter = new FeaturedAdapter(Landing.this, featuredList);
                        mAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(mAdapter);

                        if (featuredList.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            EmptyView.setVisibility(View.VISIBLE);
                        }
                        else {
                            recyclerView.setVisibility(View.VISIBLE);
                            EmptyView.setVisibility(View.GONE);
                        }
                        dotBounceProgressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("Error", "onErrorResponse: "+error);
                    }
                });

        // Access the RequestQueue through your singleton class.
        rq.add(jsonObjectRequest);
    }

}