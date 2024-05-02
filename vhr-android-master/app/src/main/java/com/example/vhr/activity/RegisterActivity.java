package com.example.vhr.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.vhr.R;

public class RegisterActivity extends AppCompatActivity {
    private Button btn_reg = null, btn_back_login = null;
    private EditText username = null, password = null, psd = null;
    private ImageView mClearUserNameView, mClearPasswordView, mClearPasswordView1;
    private String regname = "", regpass = "", regphone = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findId();

        //清空输入按钮
        mClearUserNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
            }
        });
        mClearPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText("");
            }
        });
        mClearPasswordView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psd.setText("");
            }
        });

        //返回登录按钮
        btn_back_login.setOnClickListener(new backLogin());

        //注册按钮
        btn_reg.setOnClickListener(new regClick());
    }

    public void findId(){
        btn_reg = (Button)findViewById(R.id.btn_reg);
        btn_back_login = (Button)findViewById(R.id.back_login);

        username = (EditText)findViewById(R.id.username_text1);
        password = (EditText)findViewById(R.id.userpass_text1);
        psd = (EditText)findViewById(R.id.userpass_text2);

        mClearUserNameView = (ImageView)findViewById(R.id.iv_clear_username);
        mClearPasswordView = (ImageView)findViewById(R.id.iv_clear_password);
        mClearPasswordView1 = (ImageView)findViewById(R.id.iv_clear_password2);
    }
    private class backLogin implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 跳回登陆界面
            Intent intent = new Intent();
            intent.setClass(RegisterActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
    @SuppressLint("ResourceType")
    private class regClick implements View.OnClickListener {
        public void onClick(View view){

        }
    }
}