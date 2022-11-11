package com.example.ggmap;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class GoHomeActivity extends AppCompatActivity {
    private ImageButton mAnsimCall;
    private ImageButton m112Call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_home);

        mAnsimCall = (ImageButton) findViewById(R.id.btn_ansim_call);
        m112Call = (ImageButton) findViewById(R.id.btn_112_call);

//        mAnsimCall.setOnClickListener(this);
//        m112Call.setOnClickListener(this);

        findViewById(R.id.btn_ansim_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:120")));
            }
        });

        findViewById(R.id.btn_112_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:112")));
            }
        });

    }
}
