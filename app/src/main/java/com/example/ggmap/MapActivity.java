package com.example.ggmap;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.skt.Tmap.TMapView;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        TMapView tMapView = new TMapView(this);

        LinearLayout linearLayout = findViewById(R.id.layout_Tmap);
        linearLayout.addView(tMapView);

    }
}
