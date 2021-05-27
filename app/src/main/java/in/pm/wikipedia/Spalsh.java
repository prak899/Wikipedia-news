/*
 * Created by Prakash on 2021.
 */

package in.pm.wikipedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import in.pm.wikipedia.Dashboard.Landing;

public class Spalsh extends AppCompatActivity {
    String prevStarted = "prevStarted";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);


    }

    public void getStarted(View v){
        startActivity(new Intent(this, Landing.class));
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
        } else {
            showHelp();
        }
    }

    private void showHelp() {
        startActivity(new Intent(this, Landing.class));
    }
}