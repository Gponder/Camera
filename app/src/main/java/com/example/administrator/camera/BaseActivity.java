package com.example.administrator.camera;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/13.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if (MyApplication.getAppInstance().getId()=="member"){
            Toast.makeText(this,"huiyuan", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"feihuiyuan", Toast.LENGTH_LONG).show();
        }
    }
}
