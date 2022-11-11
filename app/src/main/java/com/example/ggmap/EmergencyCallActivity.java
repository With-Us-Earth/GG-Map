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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class EmergencyCallActivity extends AppCompatActivity {
    TextView numTextView;
    EditText numEditText;

    public ImageButton baroCallBtn;
    public String tel_str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);

        numTextView = (TextView) findViewById(R.id.tv_phone_num);
        numEditText = (EditText) findViewById(R.id.et_phone_num);

        baroCallBtn = (ImageButton) findViewById(R.id.btn_baro_call);
        tel_str = numTextView.getText().toString();

        findViewById(R.id.btn_baro_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_str)));
            }
        });
    }

    public void onButtonClick(View v) {
        String returnEditText;
        returnEditText = numEditText.getText().toString();
        numTextView.setText(returnEditText);
        tel_str = returnEditText;
    }

}
