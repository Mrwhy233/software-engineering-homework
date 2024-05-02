package com.example.vhr.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.vhr.R;
import com.example.vhr.entity.OnTheJobBean;
import com.example.vhr.entity.UserBean;
import com.example.vhr.http.AjaxResult;
import com.example.vhr.http.HttpUtils;
import com.example.vhr.http.OnResponseListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StaffItemDetailActivity extends AppCompatActivity {
    private Integer Staff_id;
    private TextView jobSeekerName,idNumber,email,tel,jobName,back;
    private Button agree,refuse;
    private UserBean userBean;
    private AjaxResult result;
    private AjaxResult result1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_item_detail);

        findById();
        initData();
    }

    private void findById(){
        jobSeekerName = findViewById(R.id.jobSeekerName);
        idNumber = findViewById(R.id.idNumber);
        email = findViewById(R.id.email);
        tel = findViewById(R.id.tel);
        jobName = findViewById(R.id.jobName);

        back = (TextView) findViewById(R.id.tv_back);
        agree = findViewById(R.id.agree);
        refuse = findViewById(R.id.refuse);

        back.setOnClickListener(new click());
        agree.setOnClickListener(new click());
        refuse.setOnClickListener(new click());
    }

    private void initData(){
        Staff_id = Integer.parseInt(getIntent().getExtras().getString("staff_id"));
        Map<String, String> map1 = new HashMap<>();
        //map1.put("id",job_id);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                HttpUtils.getRequest("http://124.221.111.224:8081/jobSeeker/"+Staff_id, map1, "utf-8", new OnResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        Gson gson = new Gson();
                        Log.e("response", response);
                        AjaxResult result = gson.fromJson(response, AjaxResult.class);
                        Log.e("obiString", result.getObj().toString());
                        userBean = JSONObject.parseObject(JSON.toJSONString(result.getObj()), UserBean.class);
                        jobSeekerName.setText(userBean.getJobSeekerName());
                        idNumber.setText(userBean.getIdNumber());
                        email.setText(userBean.getJobSeekerEmail());
                        tel.setText(userBean.getJobSeekerTel());
                        jobName.setText(userBean.getJobName());
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
            while (userBean == null) {
                //等待线程执行完毕
                Thread.sleep(1000);
            }
            if (userBean == null) {
                Log.e("message", "错误");
            }
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class click implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(StaffItemDetailActivity.this,MainActivity.class);
            switch (view.getId()){
                case R.id.tv_back:
                    StaffItemDetailActivity.this.finish();
                    break;
                case R.id.agree:
                    Staff_id = Integer.parseInt(getIntent().getExtras().getString("staff_id"));
                    Date date = new Date();
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                    String date1 = f.format(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.YEAR,3);
                    Date date3 = calendar.getTime();
                    String date2 = f.format(date3);
                    System.out.println(1);
                    System.out.println(date1);
                    System.out.println(1);
                    System.out.println(date2);
                    Map<String, String> map2 = new HashMap<>();
                    map2.put("beginContract",date1);
                    map2.put("endContract",date2);
                    map2.put("id", String.valueOf(Staff_id));
                    Thread thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpUtils.postRequestJson("http://124.221.111.224:8081/jobSeeker/takeJobSeeker", map2, "utf-8", new OnResponseListener() {
                                @Override
                                public void onSuccess(String response) {
                                    result1 = new Gson().fromJson(response, AjaxResult.class);
                                }
                                @Override
                                public void onError(String error) {
                                    System.out.println("请求失败：" + error);
                                }
                            });
                        }
                    });

                    thread1.start();
                    try {
                        while (result1 == null) {
                            //等待线程执行完毕
                            Thread.sleep(100);
                        }
                        if (result1 == null) {
                            Log.e("message", "错误");
                        }
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("mes", String.valueOf(result1.getStatus()));
                    if (result1.getStatus() == 200) {
                        Toast.makeText(StaffItemDetailActivity.this, "入职成功", Toast.LENGTH_SHORT).show();
                        Delete();
                        StaffItemDetailActivity.this.finish();
                    }else{
                        Toast.makeText(StaffItemDetailActivity.this, "入职失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.refuse:
                    Delete();
            }

        }
    }
    private void Delete(){
        Staff_id = Integer.parseInt(getIntent().getExtras().getString("staff_id"));
        Map<String, String> map1 = new HashMap<>();
        //map1.put("id",job_id);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                HttpUtils.deleteRequest("http://124.221.111.224:8081/jobSeeker/"+Staff_id, map1, "utf-8", new OnResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        result = new Gson().fromJson(response, AjaxResult.class);
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
            while (result == null) {
                //等待线程执行完毕
                Thread.sleep(100);
            }
            if (result == null) {
                Log.e("message", "错误");
            }
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result.getStatus() == 200) {
            Toast.makeText(StaffItemDetailActivity.this, "删除信息成功", Toast.LENGTH_SHORT).show();
            StaffItemDetailActivity.this.finish();
        }else{
            Toast.makeText(StaffItemDetailActivity.this, "删除信息失败", Toast.LENGTH_SHORT).show();
        }
    }
}