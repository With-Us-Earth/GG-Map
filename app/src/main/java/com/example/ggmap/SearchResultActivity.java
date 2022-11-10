package com.example.ggmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SearchResultActivity extends AppCompatActivity {
    private TMapView tMapView;
    private ArrayList<TMapMarkerItem> markerItems = new ArrayList<>();
    private TMapPoint tMapPoint;
    public static TMapPoint tMapPointStart;
    public static TMapPoint tMapPointEnd;

    public static TMapPoint passListPoint;
    public static ArrayList<TMapPoint> passList = new ArrayList<>();
    public static ArrayList<Camera> cameraList = new ArrayList<>();
    public static ArrayList<String> keyList = new ArrayList<>();
    public static ArrayList<PersonCount> personCountList = new ArrayList<>();
    public static TMapPoint startPoint;
    public static TMapPoint endPoint;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx18a7622afffe4a6191d0850d7beae5e0");
        tMapView.setZoomLevel(15);

        FrameLayout linearLayout = findViewById(R.id.layout_Tmap);
        linearLayout.addView(tMapView);

        Intent receiveIntent = getIntent();
        String address = receiveIntent.getStringExtra("address");

        TextView tv_search_address2 = findViewById(R.id.tv_search_address2);
        tv_search_address2.setText(address);

        //길 인적 Polyline
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://gg-map-21058.firebaseio.com");
        firebaseDatabase.getReference("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //보행자 경로로 PolyLine 띄우기
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Camera camera = dataSnapshot.getValue(Camera.class);
                    String key = camera.getKey();
                    Double lat = camera.getLatitude();
                    cameraList.add(camera);
                    keyList.add(key);
                }
                for(Camera s : cameraList) {
                    double lat = s.getLatitude();
                    double lon = s.getLongitude();

                }

                firebaseDatabase.getReference("person").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String getKey = dataSnapshot.getKey();
                            System.out.println(getKey);

                            for (String key : keyList){
                                if(getKey.equals(key)){
                                    String n1 = dataSnapshot.getValue(String.class);
                                    int num = Integer.parseInt(n1);
                                    System.out.println(n1);

                                    if (num >= 0 && num <= 5) {
                                        for (Camera n : cameraList){
                                            String s = n.getKey();
                                            if (s.equals(getKey)){
                                                double finalLon = n.getLongitude();
                                                double finalLat = n.getLatitude();

                                                PersonCount personCount = new PersonCount();
                                                personCount.setStartLatitude(finalLat+0.002);
                                                personCount.setStartLongitude(finalLon+0.002);
                                                personCount.setEndLatitude(finalLat-0.002);
                                                personCount.setEndLongitude(finalLon-0.002);
                                                personCount.setCount(num);

                                                personCountList.add(personCount);

                                                for(PersonCount p : personCountList) {
                                                    final double startLt = p.getStartLatitude();
                                                    final double startLg = p.getStartLongitude();
                                                    final double endLt = p.getEndLatitude();
                                                    final double endLg = p.getEndLongitude();
                                                    System.out.println("=========="+startLt + "=========="+startLg + "=========="+endLt + "=========="+endLg);

                                                    startPoint = new TMapPoint(startLt, startLg);
                                                    endPoint = new TMapPoint(endLt,endLg);

                                                    new Thread() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                TMapPolyLine tMapPolyLineR = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint);
                                                                tMapPolyLineR.setLineColor(Color.RED);
                                                                tMapPolyLineR.setLineWidth(20);
                                                                tMapPolyLineR.setOutLineWidth(20);
                                                                tMapPolyLineR.setLineColor(Color.parseColor("#FF0000"));
                                                                tMapPolyLineR.setOutLineColor(Color.parseColor("#FF0000"));
                                                                tMapView.addTMapPolyLine("PolyLine_streetR", tMapPolyLineR);

                                                            } catch (Exception e) {
                                                                e.printStackTrace();}
                                                        }
                                                    }.start();
                                                }

                                            }}}

                                    if (num > 5 && num < 10) {
                                        for (Camera n : cameraList){
                                            String s = n.getKey();
                                            if (s.equals(getKey)){
                                                double finalLon = n.getLongitude();
                                                double finalLat = n.getLatitude();

                                                PersonCount personCount = new PersonCount();
                                                personCount.setStartLatitude(finalLat+0.002);
                                                personCount.setStartLongitude(finalLon+0.002);
                                                personCount.setEndLatitude(finalLat-0.002);
                                                personCount.setEndLongitude(finalLon-0.002);
                                                personCount.setCount(num);

                                                personCountList.add(personCount);

                                                for(PersonCount p : personCountList) {
                                                    final double startLt = p.getStartLatitude();
                                                    final double startLg = p.getStartLongitude();
                                                    final double endLt = p.getEndLatitude();
                                                    final double endLg = p.getEndLongitude();
                                                    System.out.println("=========="+startLt + "=========="+startLg + "=========="+endLt + "=========="+endLg);

                                                    startPoint = new TMapPoint(startLt, startLg);
                                                    endPoint = new TMapPoint(endLt,endLg);

                                                    new Thread() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                TMapPolyLine tMapPolyLineB = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint);
                                                                tMapPolyLineB.setLineColor(Color.BLUE);
                                                                tMapPolyLineB.setLineWidth(20);
                                                                tMapPolyLineB.setOutLineWidth(20);
                                                                tMapPolyLineB.setLineColor(Color.parseColor("#0000FF"));
                                                                tMapPolyLineB.setOutLineColor(Color.parseColor("#0000FF"));
                                                                tMapView.addTMapPolyLine("PolyLine_streetB", tMapPolyLineB);

                                                            } catch (Exception e) {
                                                                e.printStackTrace();}
                                                        }
                                                    }.start();
                                                }

                                            }}}

                                    if (num > 10) {
                                        for (Camera n : cameraList){
                                            String s = n.getKey();
                                            if (s.equals(getKey)){
                                                double finalLon = n.getLongitude();
                                                double finalLat = n.getLatitude();

                                                PersonCount personCount = new PersonCount();
                                                personCount.setStartLatitude(finalLat+0.002);
                                                personCount.setStartLongitude(finalLon+0.002);
                                                personCount.setEndLatitude(finalLat-0.002);
                                                personCount.setEndLongitude(finalLon-0.002);
                                                personCount.setCount(num);

                                                personCountList.add(personCount);

                                                for(PersonCount p : personCountList) {
                                                    final double startLt = p.getStartLatitude();
                                                    final double startLg = p.getStartLongitude();
                                                    final double endLt = p.getEndLatitude();
                                                    final double endLg = p.getEndLongitude();
                                                    System.out.println("=========="+startLt + "=========="+startLg + "=========="+endLt + "=========="+endLg);

                                                    startPoint = new TMapPoint(startLt, startLg);
                                                    endPoint = new TMapPoint(endLt,endLg);

                                                    new Thread() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                TMapPolyLine tMapPolyLineG = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint);
                                                                tMapPolyLineG.setLineColor(Color.GREEN);
                                                                tMapPolyLineG.setLineWidth(20);
                                                                tMapPolyLineG.setOutLineWidth(20);
                                                                tMapPolyLineG.setLineColor(Color.parseColor("#00ff00"));
                                                                tMapPolyLineG.setOutLineColor(Color.parseColor("#00ff00"));
                                                                tMapView.addTMapPolyLine("PolyLine_streetG", tMapPolyLineG);

                                                            } catch (Exception e) {
                                                                e.printStackTrace();}
                                                        }
                                                    }.start();
                                                }
                                            }}}
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){
            }
        });


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
                    tMapPoint = new TMapPoint(lat, lon);

                    Bitmap bitmap_start = BitmapFactory.decodeResource(getResources(), R.drawable.ic_location_result);
                    markerItems.get(i).setIcon(bitmap_start); // 마커 아이콘 지정
                    markerItems.get(i).setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    markerItems.get(i).setTMapPoint(tMapPoint); // 마커의 좌표 지정
                    markerItems.get(i).setName(item.getPOIName()); // 마커의 타이틀 지정

                    //ic_input_get
                    Bitmap right = ((BitmapDrawable) ContextCompat.getDrawable(SearchResultActivity.this, R.drawable.ic_selection)).getBitmap();
                    markerItems.get(i).setCalloutRightButtonImage(right);

                    markerItems.get(i).setCanShowCallout(true);
                    markerItems.get(i).setCalloutTitle(item.getPOIName());
                    tMapView.addMarkerItem("searchItem" + i, markerItems.get(i)); // 지도에 마커 추가


                    //출발지 선택
                    Button startSetBtn = (Button) findViewById(R.id.btn_set_start);
                    startSetBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(SearchResultActivity.this, "출발지를 선택해주세요.", Toast.LENGTH_LONG).show();

                            //오른쪽 말풍선 리스너
                            tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback(){
                                @Override
                                public void onCalloutRightButton(TMapMarkerItem markerItem){

                                    final String getName = markerItem.getName();
                                    final double getlat = markerItem.getTMapPoint().getLatitude();
                                    final double getlon = markerItem.getTMapPoint().getLongitude();

                                    System.out.println(getName + getlat + getlon);

                                    double start_lat = getlat;
                                    double start_lon = getlon;

                                    //출발지 좌표
                                    tMapPointStart = new TMapPoint(start_lat, start_lon);
                                    Toast.makeText(SearchResultActivity.this, "출발지로 설정되었습니다.", Toast.LENGTH_LONG).show();

                                    //마커 생성
                                    TMapMarkerItem markerItem_start = new TMapMarkerItem();

                                    //마커 아이콘 가져오기
                                    Bitmap bitmap_start = BitmapFactory.decodeResource(getResources(), R.drawable.location_depart);
                                    //출발지 마커
                                    markerItem_start.setIcon(bitmap_start); // 마커 아이콘 지정
                                    markerItem_start.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                                    markerItem_start.setTMapPoint(tMapPointStart); // 마커의 좌표 지정
                                    markerItem_start.setName("출발지 마커"); // 마커의 타이틀 지정
                                    tMapView.addMarkerItem("markerItem_start", markerItem_start); // 지도에 마커 추가
                                }
                            });
                        }
                    });

                    //도착지 선택
                    Button endSetBtn = (Button) findViewById(R.id.btn_set_end);
                    endSetBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(SearchResultActivity.this, "도착지를 선택해주세요.", Toast.LENGTH_LONG).show();

                            //오른쪽 말풍선 리스너
                            tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback(){
                                @Override
                                public void onCalloutRightButton(TMapMarkerItem markerItem){

                                    final String getName = markerItem.getName();
                                    final double getlat = markerItem.getTMapPoint().getLatitude();
                                    final double getlon = markerItem.getTMapPoint().getLongitude();

                                    System.out.println(getName + getlat + getlon);

                                    double end_lat = getlat;
                                    double end_lon = getlon;

                                    //도착지 좌표
                                    tMapPointEnd = new TMapPoint(end_lat, end_lon);
                                    Toast.makeText(SearchResultActivity.this, "도착지로 설정되었습니다.", Toast.LENGTH_LONG).show();


                                    //마커 생성
                                    TMapMarkerItem markerItem_end = new TMapMarkerItem();

                                    //마커 아이콘 가져오기
                                    Bitmap bitmap_end = BitmapFactory.decodeResource(getResources(), R.drawable.location_arriv);
                                    //도착지 마커
                                    markerItem_end.setIcon(bitmap_end); // 마커 아이콘 지정
                                    markerItem_end.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                                    markerItem_end.setTMapPoint(tMapPointEnd); // 마커의 좌표 지정
                                    markerItem_end.setName("도착지 마커"); // 마커의 타이틀 지정
                                    tMapView.addMarkerItem("markerItem_end", markerItem_end); // 지도에 마커 추가
                                }
                            });
                        }
                    });

                    //길찾기 버튼
                    ImageButton streetfindBtn = (ImageButton) findViewById(R.id.btn_streetfind);
                    streetfindBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            //목적지로 지도 이동
                            tMapView.setCenterPoint((float) tMapPointEnd.getLongitude(), (float) tMapPointEnd.getLatitude());

                            //경유지
                            passListPoint = new TMapPoint(37.591620,127.019373);
                            passList.add(passListPoint);

                            //보행자 경로로 PolyLine 띄우기
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        TMapPolyLine tMapPolyLine1 = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd);
                                        tMapPolyLine1.setLineColor(Color.BLUE);
                                        tMapPolyLine1.setLineWidth(20);
                                        tMapPolyLine1.setOutLineWidth(20);
                                        tMapPolyLine1.setLineColor(Color.parseColor("#3094ff"));
                                        tMapPolyLine1.setOutLineColor(Color.parseColor("#002247"));
                                        tMapView.addTMapPolyLine("PolyLine_streetfind", tMapPolyLine1);

                                        TMapPolyLine tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd, passList, 4);
                                        tMapPolyLine.setLineColor(Color.RED);
                                        tMapPolyLine.setLineWidth(10);
                                        tMapPolyLine.setOutLineWidth(10);
                                        tMapPolyLine.setLineColor(Color.parseColor("#FF0000"));
                                        tMapPolyLine.setOutLineColor(Color.parseColor("#FF0000"));
                                        tMapView.addTMapPolyLine("PolyLine_streetfind1", tMapPolyLine);

                                   } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }.start();
                        }

                    });
                }
                tMapView.setCenterPoint(tMapPoint.getLongitude(),tMapPoint.getLatitude());
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
