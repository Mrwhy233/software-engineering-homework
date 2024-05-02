package com.example.vhr.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.example.vhr.R;

public class ManageInfoActivity extends AppCompatActivity {
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_info);
        findViewId();
        initOnClick();
    }
    private void initOnClick(){
        back.setOnClickListener(new OnClick());
    }

    public void findViewId() {
        back = (ImageButton)findViewById(R.id.back);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            switch (v.getId()) {
                case R.id.back:
                    ManageInfoActivity.this.finish();
//                    intent.setClass(ManageInfoActivity.this,MainActivity.class);
//                    startActivity(intent);
            }
        }
    }
}