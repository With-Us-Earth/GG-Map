package com.example.ggmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText search_address = findViewById(R.id.et_search_address);
        //검색창
        search_address.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (i){
                    case KeyEvent.KEYCODE_ENTER:
                        String address = search_address.getText().toString();
                        if(address.length()==0){
                            Toast.makeText(getApplicationContext(),"검색어를 입력해주세요",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(search_address.getWindowToken(), 0);

                            Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                            intent.putExtra("address", address);


                            startActivity(intent);

                        }
                        break;
                }
                return true;
            }
        });

        findViewById(R.id.ll_myLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //위치 받아오기
            }
        });


        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}