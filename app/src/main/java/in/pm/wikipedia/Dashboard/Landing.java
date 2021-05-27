/*
 * Created by Prakash on 2021.
 */

package in.pm.wikipedia.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.jb.dev.progress_indicator.dotGrowProgressBar;
import com.jb.dev.progress_indicator.fadeProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import in.pm.wikipedia.Adapter.ArticleAdapter;
import in.pm.wikipedia.Adapter.CatagoryAdapter;
import in.pm.wikipedia.Adapter.FeaturedAdapter;
import in.pm.wikipedia.Model.Article;
import in.pm.wikipedia.Model.Catagory;
import in.pm.wikipedia.Model.Featured;
import in.pm.wikipedia.Network.NetworkChangeReceiver;
import in.pm.wikipedia.R;

public class Landing extends AppCompatActivity {

    private BroadcastReceiver mNetworkReceiver;
    private static final String TAG = "sd";
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<Featured> featuredList;
    List<Article> articleList;
    List<Catagory> catagoryList;

    RequestQueue rq;


    com.jb.dev.progress_indicator.fadeProgressBar dotBounceProgressBar;
    TextView EmptyView;
    static TextView HeaderName;

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
        /*Fragment fragment = new Fragment1();
        loadFragment(fragment);*/
        sendFeaturedRequest(featuredContinue);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch(tab.getPosition()) {
                    case 0: recyclerView.setVisibility(View.GONE);
                        sendFeaturedRequest(featuredContinue);
                        break;
                    case 1: recyclerView.setVisibility(View.GONE);
                        sendRandomRequest(randomContinue);

                        break;
                    case 2: recyclerView.setVisibility(View.GONE);
                        sendCatagoryRequest();

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

        nestedSV.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {

                page++;
                loadingPB.setVisibility(View.VISIBLE);


                switch(tabLayout.getSelectedTabPosition()) {
                    case 0:
                        sendFeaturedRequest(featuredContinueChanged);
                        break;
                    case 1:
                        sendRandomRequest(randomContinueChanged);
                        break;
                    case 2:
                        sendCatagoryRequest();
                        break;
                }
                //sendFeaturedRequest(featuredContinueChanged);
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
        nestedSV = findViewById(R.id.nested);

        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();

    }

    public void sendFeaturedRequest(String defVal) {
        dotBounceProgressBar.setVisibility(View.VISIBLE);
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
                        loadingPB.setVisibility(View.GONE);
                    }
                }, error -> {
                    // TODO: Handle error
                    Log.d("Error", "onErrorResponse: "+error);
                });

        // Access the RequestQueue through your singleton class.
        rq.add(jsonObjectRequest);
    }
    private void sendRandomRequest(String randomContinue) {
        //recyclerView.setVisibility(View.GONE);
        dotBounceProgressBar.setVisibility(View.VISIBLE);


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

                                randomContinueChanged = nextpage.getString("grncontinue");
                                JSONObject query = response.getJSONObject("query");
                                JSONObject query1 = query.getJSONObject("pages");

                                Iterator iterator  = query1.keys();
                                String key = null;

                                while(iterator.hasNext()){

                                    key = (String)iterator.next();

                                    JSONArray json = (JSONArray) ((JSONObject)query1.get(key)).get("revisions");


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

                        if (articleList.isEmpty()) {
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
                }, error -> {
                    // TODO: Handle error
                    Log.d("Error", "onErrorResponse: "+error);
                });

        // Access the RequestQueue through your singleton class.
        rq.add(jsonObjectRequest);
    }
    private void sendCatagoryRequest() {

        recyclerView.setVisibility(View.GONE);
        dotBounceProgressBar.setVisibility(View.VISIBLE);

        String url="https://en.wikipedia.org/w/api.php?action=query&list=allcategories&acprefix=List%20of&formatversion=2&format=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    //featuredList.clear();
                    Log.d("TAG", "onResponse: " + response);
                    Catagory catagory = new Catagory();
                    try {
                        catagoryList.clear();
                        //JSONObject nextpage = response.getJSONObject("continue");

                        //randomContinueChanged= nextpage.getString("grncontinue");
                        //Log.d("XvalX", "onResponse: "+nextpage.getString("accontinue"));

                        JSONObject query = response.getJSONObject("query");
                        JSONArray query1 = query.getJSONArray("allcategories");

                        for (int j=0; j<query1.length();j++){

                            JSONObject productObjects = query1.getJSONObject(j);

                            Log.d("XcvcX", "onResponse: "+productObjects.getString("category"));

                            catagory.setTitle(productObjects.getString("category"));

                        }
                        catagoryList.add(catagory);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("TAG", "onResponse: " + e);
                    }



                    mAdapter = new CatagoryAdapter(Landing.this, catagoryList);
                    //mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(mAdapter);

                    if (catagoryList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        EmptyView.setVisibility(View.VISIBLE);
                    }
                    else {
                        recyclerView.setVisibility(View.VISIBLE);
                        EmptyView.setVisibility(View.GONE);
                    }
                    dotBounceProgressBar.setVisibility(View.GONE);
                    loadingPB.setVisibility(View.GONE);
                }, error -> {
                    // TODO: Handle error
                    Log.d("Error", "onErrorResponse: "+error);
                });

        // Access the RequestQueue through your singleton class.
        rq.add(jsonObjectRequest);
    }

    @SuppressLint("ResourceAsColor")
    public static void dialog(boolean value){

        if(value){

            HeaderName.setText("Explore");
            Handler handler = new Handler();
            Runnable delayrunnable = new Runnable() {
                @Override
                public void run() {

                }
            };
            handler.postDelayed(delayrunnable, 3000);
        }else {
            HeaderName.setText("No internet!");
        }
    }


    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}