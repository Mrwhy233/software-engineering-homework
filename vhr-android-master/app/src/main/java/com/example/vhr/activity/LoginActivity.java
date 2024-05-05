package com.example.vhr.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.vhr.R;
import com.example.vhr.entity.Admin;
import com.example.vhr.entity.MainInfo;
import com.example.vhr.entity.UserBean;
import com.example.vhr.http.AjaxResult;
import com.example.vhr.http.HttpUtils;
import com.example.vhr.http.OnResponseListener;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private String realCode;
    private ImageButton btn_login;
    private ImageView mClearUserNameView, mClearPasswordView;
    private EditText et_input_username, et_input_password;
    private CheckBox mEyeView, autologin;
    private String username = "";
    private String userpass = "";
    private TransitionDrawable transitionDrawable;
    private Admin admin;
    //private boolean isAutoLogin = false;
    private EditText mEtloginactivityPhonecodes;
    private ImageView mIvloginactivityShowcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewId();
        init();
        initView();
        mEyeView.setOnCheckedChangeListener(new eyeClick());
        btn_login.setOnClickListener(new loginButton());
        mIvloginactivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();


    }
    private void initView(){
        mEtloginactivityPhonecodes = findViewById(R.id.et_loginactivity_phoneCodes);
        mIvloginactivityShowcode = findViewById(R.id.iv_loginactivity_showCode);

        mIvloginactivityShowcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.iv_loginactivity_showCode:
                        mIvloginactivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
                        realCode = Code.getInstance().getCode().toLowerCase();
                        break;
                }
            }
        });
    }
    private void init() {
        SharedPreferences spf = getSharedPreferences("loginData", MODE_PRIVATE);
//        boolean isAutoLogin = spf.getBoolean("isAutoLogin", false);
//        if (isAutoLogin) {
//            //登录成功
//            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
//            //跳转到主页面
//            Intent intent = new Intent();
//            intent.setClass(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            LoginActivity.this.finish();
//        }

        //清空输入按钮
        mClearUserNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input_username.setText("");
            }
        });
        mClearPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input_password.setText("");
            }
        });



    }

    private void findViewId() {
        btn_login = (ImageButton) findViewById(R.id.btn_login);

        et_input_username = (EditText) findViewById(R.id.et_input_username);
        et_input_password = (EditText) findViewById(R.id.et_input_password);
//        autologin = (CheckBox) findViewById(R.id.login_auto);

        mClearUserNameView = findViewById(R.id.iv_clear_username);
        mClearPasswordView = findViewById(R.id.iv_clear_password);
        mEyeView = findViewById(R.id.cb_login_open_eye);

    }

    //登录判 定
    private class loginButton implements View.OnClickListener {
        private AjaxResult result;

        @Override
        public void onClick(View v) {

//        发送http请求做登录判断
//        http://localhost:8081/doLogin
            Map<String, String> map = new HashMap<>();
            Log.e("message",et_input_username.getText().toString().trim());
            Log.e("message",et_input_password.getText().toString().trim());
            String user = et_input_username.getText().toString().trim();
            String psd = et_input_password.getText().toString().trim();
            //测试
//            String user = "admin";
//            String psd = "123";
            map.put("username", user);
            map.put("password", psd);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpUtils.postRequest("http://124.221.111.224:8081/doLogin", map, "utf-8", new OnResponseListener() {
                        //                HttpUtils.postRequest("http://192.168.43.143:8081/doLogin", map, "utf-8", new OnResponseListener() {
                        @Override
                        public void onSuccess(String response) {
                            System.out.println(response);
                            result = new Gson().fromJson(response, AjaxResult.class);
                            System.out.println(result.getObj());
                            Log.e("obiString", result.getObj().toString());
                            admin = JSONObject.parseObject(JSON.toJSONString(result.getObj()), Admin.class);
                            MainInfo.init(admin);
                            System.out.println(admin);
                            System.out.println("aaaaa");
                            Log.e("doLogin", result.getMsg());
                        }

                        @Override
                        public void onError(String error) {
                            System.out.println("请求失败：" + error);
                        }
                    });
                }
            });

            thread.start();
            try {
                if (result == null) {
//                    Log.e("message", "错误");
                }
                while (result == null) {
                    //等待线程执行完毕
                    Thread.sleep(100);
                }
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//        登录判断
            if (result.getStatus() == 200) {
                Log.e("doLogin", result.getMsg());
                // 处理逻辑业务
                loginSucces();

                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences spf = getSharedPreferences("loginData", MODE_PRIVATE);
                        SharedPreferences.Editor edit = spf.edit();
                        //isAutoLogin设为true则下次打开app会自动登录
                        edit.putString("localName", username);
                        edit.putString("localPass", userpass);
                        edit.putBoolean("isAutoLogin", true);
                        //跳转到主页面,并存储admin的值
                        Intent intent = new Intent();
//                        intent.putExtra("admin", (Serializable) admin);
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        edit.apply();
                    }
                }, 500);
            } else {
                Log.e("doLogin", result.getMsg());
                loginFail();
                Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                //清空密码
                et_input_password.setText("");
            }
        }
    }

    private void loginSucces() {
        Drawable[] drawableArray1 = {
                getResources().getDrawable(R.drawable.login_begin, null),
                getResources().getDrawable(R.drawable.login_mid, null)
        };
        transitionDrawable = new TransitionDrawable(drawableArray1);
        transitionDrawable.setCrossFadeEnabled(true);
        btn_login.setBackground(transitionDrawable);
        transitionDrawable.startTransition(500);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Drawable[] drawableArray2 = {
                        getResources().getDrawable(R.drawable.login_mid, null),
                        getResources().getDrawable(R.drawable.login_success, null)
                };
                transitionDrawable = new TransitionDrawable(drawableArray2);
                transitionDrawable.setCrossFadeEnabled(true);
                btn_login.setBackground(transitionDrawable);
                transitionDrawable.startTransition(500);
            }
        }, 500);
    }

    private void loginFail() {
        Drawable[] drawableArray1 = {
                getResources().getDrawable(R.drawable.login_begin, null),
                getResources().getDrawable(R.drawable.login_mid, null)
        };
        transitionDrawable = new TransitionDrawable(drawableArray1);
        transitionDrawable.setCrossFadeEnabled(true);
        btn_login.setBackground(transitionDrawable);
        transitionDrawable.startTransition(500);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Drawable[] drawableArray2 = {
                        getResources().getDrawable(R.drawable.login_mid, null),
                        getResources().getDrawable(R.drawable.login_failure, null)
                };
                transitionDrawable = new TransitionDrawable(drawableArray2);
                transitionDrawable.setCrossFadeEnabled(true);
                btn_login.setBackground(transitionDrawable);
                transitionDrawable.startTransition(500);
            }
        }, 500);

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                Drawable[] drawableArray2 = {
                        getResources().getDrawable(R.drawable.login_failure, null),
                        getResources().getDrawable(R.drawable.login_mid, null)
                };
                transitionDrawable = new TransitionDrawable(drawableArray2);
                transitionDrawable.setCrossFadeEnabled(true);
                btn_login.setBackground(transitionDrawable);
                transitionDrawable.startTransition(500);
            }
        }, 500);

        Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            @Override
            public void run() {
                Drawable[] drawableArray2 = {
                        getResources().getDrawable(R.drawable.login_mid, null),
                        getResources().getDrawable(R.drawable.login_begin, null)
                };
                transitionDrawable = new TransitionDrawable(drawableArray2);
                transitionDrawable.setCrossFadeEnabled(true);
                btn_login.setBackground(transitionDrawable);
                transitionDrawable.startTransition(500);
            }
        }, 500);
    }


    //显示或隐藏密码
    private class eyeClick implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                et_input_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                et_input_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
//    //是否自动登录
//    private class autoLogin implements CompoundButton.OnCheckedChangeListener {
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            if (isChecked) {
//                a = 1;
//                return a;
//            } else {
//                et_input_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
//            }
//        }
//    }

    //跳转到注册界面
    private class regTextView implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}