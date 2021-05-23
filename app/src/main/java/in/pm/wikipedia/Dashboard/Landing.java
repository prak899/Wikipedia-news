package in.pm.wikipedia.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.pm.wikipedia.Adapter.FeaturedAdapter;
import in.pm.wikipedia.MainActivity;
import in.pm.wikipedia.Model.Featured;
import in.pm.wikipedia.R;

public class Landing extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<Featured> featuredList;

    RequestQueue rq;

    String request_url = "https://commons.wikimedia.org/w/api.php?action=query&prop=imageinfo&iiprop=timestamp%7Cuser%7Curl&generator=categorymembers&gcmtype=file&gcmtitle=Category:Featured_pictures_on_Wikimedia_Commons&format=json&utf8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);


        rq = Volley.newRequestQueue(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        featuredList = new ArrayList<>();

        sendRequest();
    }
    public void sendRequest(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                for(int i = 0; i < response.length(); i++){

                    Featured featured = new Featured();


                    try {
                        JSONObject jsonObject = response.getJSONObject(String.valueOf(i));
                        Log.d("TAG", "here");
                        Log.d("XrespX", "onResponse: "+jsonObject);
                        featured.setNs(jsonObject.getString("ns"));
                        featured.setPageid(jsonObject.getString("pageid"));
                        featured.setTitle(jsonObject.getString("title"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    featuredList.add(featured);

                }

                mAdapter = new FeaturedAdapter(Landing.this, featuredList);

                recyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley Error: ", String.valueOf(error));
            }
        });

        rq.add(jsonObjectRequest);

    }

}