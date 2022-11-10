package com.example.ggmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import javax.xml.parsers.ParserConfigurationException;

public class SearchResultActivity<start_lat> extends AppCompatActivity {
    private TMapView tMapView;
    private ArrayList<TMapMarkerItem> markerItems = new ArrayList<>();
    private TMapPoint tMapPoint;
    public static TMapPoint tMapPointStart;
    public static TMapPoint tMapPointEnd;

    public static TMapPoint passListPoint;
    public static ArrayList<TMapPoint> passList = new ArrayList<>();
    public static ArrayList<TMapPoint> passList2 = new ArrayList<>();
    public static ArrayList<Camera> cameraList = new ArrayList<>();
    public static ArrayList<Double> numList = new ArrayList<>();
    public static ArrayList<String> keyList = new ArrayList<>();
    public static HashMap<Double, ArrayList<TMapPoint>> finalList = new HashMap<Double, ArrayList<TMapPoint>>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx59b89a7b7f8c439c99f5a86b7ec86fc6");
        tMapView.setZoomLevel(15);

        FrameLayout linearLayout = findViewById(R.id.layout_Tmap);
        linearLayout.addView(tMapView);

        TextView safteyTime = findViewById(R.id.saftey_time);
        TextView shortestTime = findViewById(R.id.shortest_time);

        Intent receiveIntent = getIntent();
        String address = receiveIntent.getStringExtra("address");

        TextView tv_search_address2 = findViewById(R.id.tv_search_address2);
        tv_search_address2.setText(address);

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

                            //출발지, 목적지 좌표값
                            double startLat = tMapPointStart.getLatitude();
                            double startLong = tMapPointStart.getLatitude();

                            double endLat = tMapPointEnd.getLatitude();
                            double endLong = tMapPointEnd.getLongitude();

                            //출발지 목적지 좌표 비교
                            double bigLat = Math.max(startLat, endLat);
                            double smallLat = Math.min(startLat, endLat);
                            double bigLong = Math.max(startLong, endLong);
                            double smallLong = Math.min(startLong, endLong);

                            //안심길찾기 경유지 설정

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://gg-map-21058.firebaseio.com");
                            firebaseDatabase.getReference("location").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        Camera camera = dataSnapshot.getValue(Camera.class);

                                        cameraList.add(camera);

                                    }
                                    //반경 n미터 이내만 불러오기
                                    for(Camera s : cameraList) {
                                        double lat = s.getLatitude();
                                        double lon = s.getLongitude();
                                        double newSmallLat = smallLat - 0.0005;
                                        double newBigLat = bigLat + 0.0005;
                                        double newSmallLong = smallLong + 0.0005;
                                        double newBigLong = bigLong+ 0.0005;

                                        if (newSmallLat <= lat && lat <= newBigLat) {

                                            System.out.println(lat);
                                            if (lon >= newSmallLong && lon <= newBigLong) {
                                                String key = s.getKey();
                                                keyList.add(key);
                                                System.out.println(key);



                                            }
                                            else {
                                                System.out.println("이건 안됨");
                                            }
                                        }
                                        else {
                                            System.out.println("이건안됨");
                                        }
                                    }

                                    firebaseDatabase.getReference("person").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                String getKey = dataSnapshot.getKey();

                                                //인적수 가져오기
                                                for (String key : keyList){
                                                    if(getKey.equals(key)){
                                                        String n1 = dataSnapshot.getValue(String.class);
                                                        int num = Integer.parseInt(n1);


                                                        //인적 5이상만 안심길찾기 경로에 넣음
                                                        if (num >= 5) {
                                                            for (Camera n : cameraList){
                                                                String s = n.getKey();
                                                                if (s.equals(key)){
                                                                    double finalLon = n.getLongitude();
                                                                    double finalLat = n.getLatitude();
                                                                    System.out.println("+++++++"+finalLat);


                                                                    passListPoint = new TMapPoint(finalLat, finalLon);
                                                                    passList.add(passListPoint);

                                                                }
                                                            }

                                                        }


                                                    }

                                                }


                                            }

                                            ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
                                            ArrayList<ArrayList<TMapPoint>> haha = new ArrayList<>();
                                            int a = passList.size();
                                            System.out.println(a);

                                            for (int inx =1; inx <= a; inx++) {
                                                pick(a, new ArrayList<Integer>(), inx, result);
                                            }




                                            for (ArrayList<Integer> arrayList : result) {
                                                ArrayList<TMapPoint> tlist = new ArrayList<>();
                                                int size = arrayList.size();

                                                for (int b = 0; b < size; b++){
                                                    int num = arrayList.get(b);
                                                    TMapPoint point = passList.get(num);

                                                    tlist.add(point);
                                                }
                                                haha.add(tlist);
                                                System.out.println("00"+ arrayList);
                                            }


                                                new Thread(){
                                                    @Override
                                                    public void run() {
                                                        try {

                                                            for(ArrayList<TMapPoint> m : haha){

                                                                System.out.println("====="+m);
                                                                TMapPolyLine line = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd, m, 0);
                                                                //tMapView.addTMapPolyLine("line", line);

                                                                double a2 = line.getDistance();
                                                                System.out.println(a2);
                                                                if (a2 != 0.0){
                                                                    finalList.put(a2, m);
                                                                    numList.add(a2);
                                                                    Thread.sleep(500);
                                                                }
                                                                //tMapView.removeAllTMapPolyLine();

                                                            }


                                                            Double minNum = Collections.min(numList);
                                                            System.out.println(minNum);
                                                            passList2 = finalList.get(minNum);

                                                            TMapPolyLine tMapPolyLine1 = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd);
                                                            tMapPolyLine1.setLineColor(Color.BLUE);
                                                            tMapPolyLine1.setLineWidth(20);
                                                            tMapPolyLine1.setOutLineWidth(20);
                                                            tMapPolyLine1.setLineColor(Color.parseColor("#3094ff"));
                                                            tMapPolyLine1.setOutLineColor(Color.parseColor("#002247"));
                                                            tMapView.addTMapPolyLine("PolyLine_streetfind", tMapPolyLine1);

                                                            double Distance = tMapPolyLine1.getDistance();
                                                            Thread.sleep(500);




                                                            //안심길찾기
                                                            TMapPolyLine tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd, passList2, 0);
                                                            tMapPolyLine.setLineColor(Color.RED);
                                                            if(passList2.size() == 0) {
                                                                System.out.println("비어있음!!");
                                                            } else {
                                                                System.out.println("안에있음@@");
                                                            }
                                                            tMapPolyLine.setLineWidth(10);
                                                            tMapPolyLine.setOutLineWidth(10);
                                                            tMapPolyLine.setLineColor(Color.parseColor("#FF0000"));
                                                            tMapPolyLine.setOutLineColor(Color.parseColor("#FF0000"));
                                                            tMapView.addTMapPolyLine("PolyLine_streetfind1", tMapPolyLine);

                                                            double Distance2 = tMapPolyLine.getDistance();



                                                            // 시간, 거리 보여주기
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    int a = (int) Math.round(Distance);
                                                                    int b = (int) Math.round(Distance2);

                                                                    shortestTime.setText((int) Math.round(a*0.016)+"분\n" +a + "m" );
                                                                    safteyTime.setText((int) Math.round(b*0.016)+"분\n" +b + "m");

                                                                    findViewById(R.id.saftey_distance_btn).setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {
                                                                            tMapView.removeAllTMapPolyLine();
                                                                            tMapView.addTMapPolyLine("saftey", tMapPolyLine);


                                                                        }
                                                                    });

                                                                    findViewById(R.id.shortest_distance_btn).setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {
                                                                            tMapView.removeAllTMapPolyLine();
                                                                            tMapView.addTMapPolyLine("short", tMapPolyLine1);
                                                                        }
                                                                    });

                                                                }
                                                            });



                                                        } catch (IOException | ParserConfigurationException | SAXException | InterruptedException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                }.start();











                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });




                                    //보행자 경로로 PolyLine 띄우기
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                //최단길찾기




                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }.start();

                                }



                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError){

                                }

                            });



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
                passList.clear();
            }
        });

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                passList.clear();
            }
        });

    }


    private static void pick(int n, ArrayList<Integer> picked, int toPick, ArrayList<ArrayList<Integer>> collection) {

        if (toPick == 0) {
            //System.out.println(picked);
            collection.add(picked);
            return;
        }

        int smallest = picked.isEmpty() ? 0 : picked.get(picked.size() - 1) + 1;

        for (int next = smallest; next < n; next++) {
            picked.add(next);
            pick(n, new ArrayList<Integer>(picked), toPick - 1, collection);
            picked.remove(picked.size() - 1);
        }
    }

}
