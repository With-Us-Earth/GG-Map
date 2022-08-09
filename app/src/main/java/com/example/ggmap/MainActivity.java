package com.example.ggmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TMapView tMapView = new TMapView(this);

        FrameLayout linearLayout = findViewById(R.id.layout_Tmap);
        linearLayout.addView(tMapView);

        drawerLayout = findViewById(R.id.drawer_view);
        navigationView = findViewById(R.id.navigation_view);

        findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        //초기 지도 중심점
        tMapView.setCenterPoint(126.985302, 37.570841);

        //길찾기 버튼
        ImageButton streetfindBtn = (ImageButton) findViewById(R.id.btn_streetfind);
        streetfindBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //길찾기 버튼 작동 테스트
                Toast.makeText(MainActivity.this, "길찾기 버튼은 클릭했습니다.", Toast.LENGTH_SHORT).show();

//                //티맵 앱 길안내
//                TMapTapi tMapTapiStreet = new TMapTapi(MapActivity.this);
//                tMapTapiStreet.invokeRoute("T타워", 126.984098f, 37.566385f);


                //마커 생성
                TMapMarkerItem markerItem_start = new TMapMarkerItem();
                TMapMarkerItem markerItem_end = new TMapMarkerItem();

                //출발지와 목적지 좌표
                TMapPoint tMapPointStart = new TMapPoint(37.570841, 126.985302); // SKT타워(출발지)
                TMapPoint tMapPointEnd = new TMapPoint(37.551135, 126.988205); // N서울타워(목적지)

                //마커 아이콘 가져오기
                Bitmap bitmap_start = BitmapFactory.decodeResource(getResources(), R.drawable.ic_location);
                //출발지 마커
                markerItem_start.setIcon(bitmap_start); // 마커 아이콘 지정
                markerItem_start.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem_start.setTMapPoint(tMapPointStart); // 마커의 좌표 지정
                markerItem_start.setName("출발지 마커"); // 마커의 타이틀 지정
                tMapView.addMarkerItem("markerItem_start", markerItem_start); // 지도에 마커 추가


                //마커 아이콘 가져오기
                Bitmap bitmap_end = BitmapFactory.decodeResource(getResources(), R.drawable.ic_location);
                //목적지 마커
                markerItem_end.setIcon(bitmap_end); // 마커 아이콘 지정
                markerItem_end.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem_end.setTMapPoint(tMapPointEnd); // 마커의 좌표 지정
                markerItem_end.setName("목적지 마커"); // 마커의 타이틀 지정
                tMapView.addMarkerItem("markerItem_end", markerItem_end); // 지도에 마커 추가


                //앱 지도 이동 (왜안되지)
//                TMapTapi tMapTapiMove = new TMapTapi(MapActivity.this);
//                tMapTapiMove.invokeSetLocation("map_move", (float) tMapPointEnd.getLongitude(), (float) tMapPointEnd.getLatitude());

                //목적지로 지도 이동
                tMapView.setCenterPoint((float) tMapPointEnd.getLongitude(), (float) tMapPointEnd.getLatitude());


                //보행자 경로로 PolyLine 띄우기
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            TMapPolyLine tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd);
                            tMapPolyLine.setLineColor(Color.BLUE);
                            tMapPolyLine.setLineWidth(5);
                            tMapView.addTMapPolyLine("PolyLine_streetfind", tMapPolyLine);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });



//        //내 위치 GPS
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//
//            gpsTracker = new GpsTracker(MainActivity.this);
//            double latitude = gpsTracker.getLatitude(); // 위도
//            double longitude = gpsTracker.getLongitude();   //경도
//            //필요시  String address = getCurrentAddress(latitude, longitude); 대한민국 서울시 종로구 ~~
//        }




        //검색 버튼
        Button searchBtn = (Button) findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "검색 버튼은 클릭했습니다.", Toast.LENGTH_SHORT).show();

                //통합 검색
                String s = String.valueOf(R.id.search_address);
                TMapTapi tMapTapiSearch = new TMapTapi(MainActivity.this);
                tMapTapiSearch.invokeSearchPortal(s);

            }
        });

    }






    //좌표
//        TMapPoint tpoint1 = new TMapPoint(37.570841, 126.985302);
//        double katech_x1= tpoint1.getKatechLat();
//        double katech_y1 = tpoint1.getKatechLon();
//
//        TMapPoint tpoint2 = new TMapPoint(37.566385, 126.984098);
//        double katech_x2= tpoint2.getKatechLat();
//        double katech_y2 = tpoint2.getKatechLon();
















/*
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
/*
                switch (item.getItemId()){
                    case R.id.item_sujeong:
                        break;
                }*/

    //drawerLayout = findViewById(R.id.drawer_view);
    //drawerLayout.closeDrawer(GravityCompat.START);
    //return true;
    //}
    //});
/*
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String provider = location.getProvider();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                tMapView.setCenterPoint(location.getLatitude(), location.getLongitude());
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1500, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1500, 10, locationListener);
*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }



}
