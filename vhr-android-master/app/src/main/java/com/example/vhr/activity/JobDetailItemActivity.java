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
import com.example.vhr.http.AjaxResult;
import com.example.vhr.http.HttpUtils;
import com.example.vhr.http.OnResponseListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JobDetailItemActivity extends AppCompatActivity {

    private Integer job_id;
    private TextView back;
    private TextView name,idNumber,email,tel,base,jobName,beginContract,endContract;
    private Button promotion,fire;
    private OnTheJobBean onTheJobBean;
    private AjaxResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_item_detail);

        findById();
        initData();
    }

    private void findById(){
        name = findViewById(R.id.name);
        idNumber = findViewById(R.id.idNumber);
        email = findViewById(R.id.email);
        tel = findViewById(R.id.tel);
        jobName = findViewById(R.id.jobName);
        beginContract = findViewById(R.id.begincontract);
        endContract = findViewById(R.id.endcontract);
        promotion = findViewById(R.id.promotion);
        base = findViewById(R.id.base);
        fire = findViewById(R.id.fire);
        back = (TextView) findViewById(R.id.tv_back);

        back.setOnClickListener(new click());
        promotion.setOnClickListener(new click());
        fire.setOnClickListener(new click());
    }

    private void initData() {
        //
        job_id = Integer.parseInt(getIntent().getExtras().getString("job_id"));
        Map<String, String> map1 = new HashMap<>();
        map1.put("id", String.valueOf(job_id));
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                HttpUtils.getRequest("http://124.221.111.224:8081/employee/basic/getEmployeeById", map1, "utf-8", new OnResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        Gson gson = new Gson();
                        Log.e("response", response);
                        AjaxResult result = gson.fromJson(response, AjaxResult.class);
                        Log.e("obiString", result.getObj().toString());
                        onTheJobBean = JSONObject.parseObject(JSON.toJSONString(result.getObj()), OnTheJobBean.class);
                        name.setText(onTheJobBean.getName());
                        idNumber.setText(onTheJobBean.getIdCard());
                        email.setText(onTheJobBean.getEmail());
                        tel.setText(onTheJobBean.getPhone());
                        base.setText(onTheJobBean.getNativePlace());
                        jobName.setText(onTheJobBean.getJobName());
                        String startTime = onTheJobBean.getBeginContract();
                        String endTime = onTheJobBean.getEndContract();
                        beginContract.setText(startTime);
                        endContract.setText(endTime);
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
            while (onTheJobBean == null) {
                //等待线程执行完毕
                Thread.sleep(100);
            }
            if (onTheJobBean == null) {
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
            intent.setClass(JobDetailItemActivity.this,MainActivity.class);
            switch (view.getId()){
                case R.id.tv_back:
                    JobDetailItemActivity.this.finish();
                    break;
                case R.id.promotion:
                    Toast.makeText(JobDetailItemActivity.this, "升职成功", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fire:
                    job_id = Integer.parseInt(getIntent().getExtras().getString("job_id"));
                    Map<String, String> map1 = new HashMap<>();
                    //map1.put("id",job_id);
                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            HttpUtils.deleteRequest("http://124.221.111.224:8081/employee/basic/"+job_id, map1, "utf-8", new OnResponseListener() {
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
                        Toast.makeText(JobDetailItemActivity.this, "开除员工成功", Toast.LENGTH_SHORT).show();
                        JobDetailItemActivity.this.finish();
                    }else{
                        Toast.makeText(JobDetailItemActivity.this, "开除员工失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    }
}