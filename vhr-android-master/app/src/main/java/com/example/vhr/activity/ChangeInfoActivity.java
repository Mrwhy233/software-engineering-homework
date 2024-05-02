package com.example.vhr.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vhr.R;

public class ChangeInfoActivity extends AppCompatActivity {
    private ImageButton back,save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        findViewId();
        initOnClick();
    }

    private void initOnClick(){
        back.setOnClickListener(new OnClick());
        save.setOnClickListener(new OnClick());
    }

    public void findViewId() {
        back = (ImageButton)findViewById(R.id.back);
        save = (ImageButton)findViewById(R.id.save);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            switch (v.getId()) {
                case R.id.back:
                    ChangeInfoActivity.this.finish();
//                    intent.setClass(ChangeInfoActivity.this,MainActivity.class);
//                    startActivity(intent);
                    break;
                case R.id.save:
                    Toast.makeText(ChangeInfoActivity.this,"保存信息成功",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}