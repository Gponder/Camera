package com.example.administrator.camera.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.camera.BaseActivity;
import com.example.administrator.camera.R;
import com.example.administrator.camera.util.MySurfaceView;

public class MainActivity extends BaseActivity {

    private MySurfaceView mySurfaceView;
    private boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySurfaceView = new MySurfaceView(this);
        setContentView(mySurfaceView);
        mySurfaceView.setOnClickListener(listener);
    }
    View.OnClickListener listener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if (flag){
                mySurfaceView.tackPicture();
                flag = false;
            }else{
                mySurfaceView.voerTack();
                flag = true;
            }
        }
    };
}
