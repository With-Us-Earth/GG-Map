package com.example.ggmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchResultActivity extends AppCompatActivity {
    private TMapView tMapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx18a7622afffe4a6191d0850d7beae5e0");

        FrameLayout linearLayout = findViewById(R.id.layout_Tmap);
        linearLayout.addView(tMapView);

        Intent receiveIntent = getIntent();
        String address = receiveIntent.getStringExtra("address");

        TextView tv_search_address2 = findViewById(R.id.tv_search_address2);
        tv_search_address2.setText(address);

        ArrayList<TMapMarkerItem> markerItems = new ArrayList<>();
        TMapData tMapData = new TMapData();
        tMapData.findAllPOI(address, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                for (int i = 0; i < arrayList.size(); i++) {
                    TMapPOIItem item = arrayList.get(i);

                    ArrayList<String> point = new ArrayList<>(Arrays.asList(item.getPOIPoint().toString().split(" ")));
                    float lat = Float.parseFloat(point.get(1));
                    float lon = Float.parseFloat(point.get(3));

                    //마커 생성
                    markerItems.add(new TMapMarkerItem());
                    TMapPoint tMapPointStart = new TMapPoint(lat, lon);

                    Bitmap bitmap_start = BitmapFactory.decodeResource(getResources(), R.drawable.ic_location_result);
                    markerItems.get(i).setIcon(bitmap_start); // 마커 아이콘 지정
                    markerItems.get(i).setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    markerItems.get(i).setTMapPoint(tMapPointStart); // 마커의 좌표 지정
                    markerItems.get(i).setName(item.getPOIName()); // 마커의 타이틀 지정
                    tMapView.addMarkerItem("searchItem" + Integer.toString(i), markerItems.get(i)); // 지도에 마커 추가
                }
            }
        });


        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

}
