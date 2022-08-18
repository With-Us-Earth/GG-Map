package com.example.ggmap;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TMapView tMapView;
    private LocationManager locationManager;
    private ActivityResultLauncher<String[]> locationPermissionRequest;

    private boolean keep = true;
    private final Runnable runner = new Runnable() {
        @Override
        public void run() {
            keep = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Splash
        /*splashScreen.setKeepOnScreenCondition(new SplashScreen.KeepOnScreenCondition() {
            @Override
            public boolean shouldKeepOnScreen() {
                return keep;
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(runner, 3000);*/

        //TMap
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx18a7622afffe4a6191d0850d7beae5e0");
        tMapView.setHttpsMode(true);

        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        FrameLayout linearLayout = findViewById(R.id.layout_Tmap);
        linearLayout.addView(tMapView);

        //Navigation
        drawerLayout = findViewById(R.id.drawer_view);
        navigationView = findViewById(R.id.navigation_view);

        findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //GPS 권한 요청
        locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);

                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                                Toast.makeText(this, "GPS 권한 요청이 거부되었습니다.", Toast.LENGTH_SHORT).show();

                            }
                        }
                );

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1500, 10, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1500, 10, locationListener);
            }
        }

        findViewById(R.id.tv_search_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_myLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getMyLocation();
                //tMapView.setTrackingMode(true);

                if(tMapView.getIsCompass())
                    tMapView.setCompassMode(false);
                else
                    tMapView.setCompassMode(true);
            }
        });

    }

    public void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1500, 10, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1500, 10, locationListener);
            }
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            String provider = location.getProvider();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            //마커 생성
            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            TMapPoint tMapPointStart = new TMapPoint(latitude, longitude);

            Bitmap bitmap_start = BitmapFactory.decodeResource(getResources(), R.drawable.ic_location);

            TextView textView = findViewById(R.id.tv_search_address);
            textView.setText("위도: "+latitude+"경도: "+longitude);
            //출발지 마커
            tMapMarkerItem.setIcon(bitmap_start); // 마커 아이콘 지정
            tMapMarkerItem.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
            tMapMarkerItem.setTMapPoint(tMapPointStart); // 마커의 좌표 지정
            tMapMarkerItem.setName("GPGP"); // 마커의 타이틀 지정
            tMapView.addMarkerItem("myLocation", tMapMarkerItem); // 지도에 마커 추가
            tMapView.setCenterPoint(tMapPointStart.getLongitude(), tMapPointStart.getLatitude());
        }
    };

    //좌표
//        TMapPoint tpoint1 = new TMapPoint(37.570841, 126.985302);
//        double katech_x1= tpoint1.getKatechLat();
//        double katech_y1 = tpoint1.getKatechLon();
//
//        TMapPoint tpoint2 = new TMapPoint(37.566385, 126.984098);
//        double katech_x2= tpoint2.getKatechLat();
//        double katech_y2 = tpoint2.getKatechLon();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
