/*
 * Created by Prakash on 2021.
 */

package in.pm.wikipedia.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import com.jb.dev.progress_indicator.dotGrowProgressBar;
import com.jb.dev.progress_indicator.fadeProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.pm.wikipedia.Adapter.ArticleAdapter;
import in.pm.wikipedia.Adapter.CatagoryAdapter;
import in.pm.wikipedia.Adapter.FeaturedAdapter;
import in.pm.wikipedia.MainActivity;
import in.pm.wikipedia.Model.Article;
import in.pm.wikipedia.Model.Catagory;
import in.pm.wikipedia.Model.Featured;
import in.pm.wikipedia.R;

public class Landing extends AppCompatActivity {

    private static final String TAG = "sd";
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<Featured> featuredList;
    List<Article> articleList;
    List<Catagory> catagoryList;

    RequestQueue rq;


    com.jb.dev.progress_indicator.fadeProgressBar dotBounceProgressBar;
    TextView EmptyView, HeaderName;

    TabLayout tabLayout;

    String featuredContinue="file%7C3030313131372031352d34342d323030322d544f2d475255505045522d524f53412d51414a41522d464c49534552322e4a50470a3030313131372031352d34342d323030322d544f2d475255505045522d524f53412d51414a41522d464c49534552322e4a5047%7C24259710";

    String randomContinue="0.733352296815|0.733352296815|0|0";
    String featuredContinueChanged, randomContinueChanged;

    private dotGrowProgressBar loadingPB;
    private NestedScrollView nestedSV;

    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        UIinit();

        sendFeaturedRequest(featuredContinue);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0: sendFeaturedRequest(featuredContinue);
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

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    loadingPB.setVisibility(View.VISIBLE);
                    sendFeaturedRequest(featuredContinueChanged);
                }
            }
        });
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
        articleList = new ArrayList<>();
        catagoryList = new ArrayList<>();


        HeaderName= findViewById(R.id.header_name);
        tabLayout = findViewById(R.id.tab_lay);

        loadingPB = findViewById(R.id.dotGrowProgressBar);
        nestedSV = findViewById(R.id.nestedScrll);

    }
    private void sendCatagoryRequest() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        HeaderName.clearAnimation();
        HeaderName.startAnimation(a);
        HeaderName.setText("Catagory List");

        recyclerView.setVisibility(View.GONE);
        dotBounceProgressBar.setVisibility(View.VISIBLE);

        String url="https://en.wikipedia.org/w/api.php?action=query&list=allcategories&acprefix=List%20of&formatversion=2&format=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //featuredList.clear();
                        Log.d("TAG", "onResponse: " + response);
                        Catagory catagory = new Catagory();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                catagoryList.clear();
                                JSONObject nextpage = response.getJSONObject("continue");

                                //randomContinueChanged= nextpage.getString("grncontinue");
                                Log.d("XvalX", "onResponse: "+nextpage.getString("accontinue"));

                                JSONObject query = response.getJSONObject("query");
                                JSONArray qu = query.getJSONArray("allcategories");

                                Log.d("XdfgXZ", "onResponse: "+ query.getString("category"));

                                /*Iterator iterator  = qu.keys();
                                String key = null;

                                while(iterator.hasNext()){

                                    key = (String)iterator.next();

                                    //JSONObject json = (JSONObject) ((JSONArray)query.get(key)).get(0);

                                    Log.d("XtitleX", "onResponse: "+((JSONObject)query.get(key)).get("category"));

                                    //catagory.setTitle(json.getString("category"));
                                    *//*for (int j=0; j<json.length();j++){

                                        JSONObject productObjects = json.getJSONObject("category");



                                    }*//*

                                }*/

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("TAG", "onResponse: " + e);
                            }

                            catagoryList.add(catagory);
                        }
                        mAdapter = new CatagoryAdapter(Landing.this, catagoryList);
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
                        loadingPB.setVisibility(View.GONE);
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

    private void sendRandomRequest() {
        recyclerView.setVisibility(View.GONE);
        dotBounceProgressBar.setVisibility(View.VISIBLE);

        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        HeaderName.clearAnimation();
        HeaderName.startAnimation(a);
        HeaderName.setText("Random Articles");
        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();


        String url="https://en.wikipedia.org/w/api.php?format=json&action=query&generator=random&grnnamespace=0&prop=revisions%7Cimages&rvprop=content&grnlimit=10&gcmcontinue="+
                randomContinue;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //featuredList.clear();
                        Log.d("TAG", "onResponse: " + response);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject nextpage = response.getJSONObject("continue");

                                randomContinueChanged= nextpage.getString("grncontinue");
                                Log.d("XvalX", "onResponse: "+nextpage.getString("grncontinue"));

                                JSONObject query = response.getJSONObject("query");
                                JSONObject query1 = query.getJSONObject("pages");

                                Iterator iterator  = query1.keys();
                                String key = null;

                                while(iterator.hasNext()){

                                    key = (String)iterator.next();

                                    JSONArray json = (JSONArray) ((JSONObject)query1.get(key)).get("revisions");

                                    //JSONObject jsonObj = (JSONObject) ((JSONObject)query1.get(key)).get("title");

                                    Log.d("XtitleX", "onResponse: "+((JSONObject)query1.get(key)).get("title"));
                                    Article article = new Article();
                                    article.setTitle((String) ((JSONObject)query1.get(key)).get("title"));
                                    for (int j=0; j<json.length();j++){

                                        JSONObject productObjects = json.getJSONObject(i);


                                        article.setContent(productObjects.getString("*"));

                                    }
                                    articleList.add(article);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("TAG", "onResponse: " + e);
                            }


                        }
                        mAdapter = new ArticleAdapter(Landing.this, articleList);
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
                        loadingPB.setVisibility(View.GONE);
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

    public void sendFeaturedRequest(String defVal) {
        /*Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        HeaderName.clearAnimation();
        HeaderName.startAnimation(a);
        HeaderName.setText("Featured Articles");*/

        String url="https://commons.wikimedia.org/w/api.php?action=query&prop=imageinfo&iiprop=timestamp%7Cuser%7Curl&generator=categorymembers&gcmtype=file&gcmtitle=Category:Featured_pictures_on_Wikimedia_Commons&format=json&utf8&gcmcontinue="+
                defVal;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //featuredList.clear();
                        Log.d("TAG", "onResponse: " + response);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject nextpage = response.getJSONObject("continue");

                                featuredContinueChanged= nextpage.getString("gcmcontinue");
                                Log.d("XvalX", "onResponse: "+nextpage.getString("gcmcontinue"));

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

                                        featured.setImage_url(productObjects.getString("user"));
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
                        loadingPB.setVisibility(View.GONE);
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