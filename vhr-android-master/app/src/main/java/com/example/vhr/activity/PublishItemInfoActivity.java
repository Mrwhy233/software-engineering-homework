package com.example.vhr.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.vhr.R;
import com.example.vhr.entity.InfoBean;
import com.example.vhr.entity.UserBean;
import com.example.vhr.http.AjaxResult;
import com.example.vhr.http.HttpUtils;
import com.example.vhr.http.OnResponseListener;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishItemInfoActivity extends AppCompatActivity {
    private TextView positionName,salary,createDate,base,description,tv_back;
    private Button revamp,delete;
    private Integer info_id;
    private AjaxResult result;
    private InfoBean infoBean;
    private List<Map<String, Object>> ItemList;
    private String departmentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_item_info);

        findById();
        initData();
    }

    private void findById(){
        positionName = findViewById(R.id.positionName);
        salary = findViewById(R.id.salary);
        createDate = findViewById(R.id.createDate);
        base = findViewById(R.id.base);
        description = findViewById(R.id.description);
        tv_back = findViewById(R.id.tv_back);
        revamp = findViewById(R.id.revamp);
        delete = findViewById(R.id.delete);

        tv_back.setOnClickListener(new click());
        revamp.setOnClickListener(new click());
        delete.setOnClickListener(new deleteItem());
    }

    private void initData(){
        info_id = Integer.parseInt(getIntent().getExtras().getString("info_id"));
        System.out.println(1);
        System.out.println(info_id);
        Map<String, String> map1 = new HashMap<>();
        //map1.put("id",job_id);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                HttpUtils.getRequest("http://124.221.111.224:8081/positionRequire/"+info_id, map1, "utf-8", new OnResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        Gson gson = new Gson();
                        Log.e("response", response);
                        AjaxResult result = gson.fromJson(response, AjaxResult.class);
                        Log.e("obiString", result.getObj().toString());
                        infoBean = JSONObject.parseObject(JSON.toJSONString(result.getObj()), InfoBean.class);
                        positionName.setText(infoBean.getPositionName());
                        salary.setText(infoBean.getSalary());
                        createDate.setText(infoBean.getCreateDate());
                        base.setText(infoBean.getBase());
                        description.setText(infoBean.getDescription());
                        departmentId=infoBean.getDepartmentId();
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
            while (infoBean == null) {
                //等待线程执行完毕
                Thread.sleep(100);
            }
            if (infoBean == null) {
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
            switch (view.getId()) {
                case R.id.tv_back:
                    PublishItemInfoActivity.this.finish();
                    break;
                case R.id.revamp:
                    intent.setClass(PublishItemInfoActivity.this, ChangePublishActivity.class);
                    String positionName1 = positionName.getText().toString().trim();
                    String salary1 = salary.getText().toString().trim();
                    String createDate1 = createDate.getText().toString().trim();
                    String base1 = base.getText().toString().trim();
                    String description1 = description.getText().toString().trim();
                    String departmentId1 = departmentId;
                    Bundle bundle = new Bundle();
                    bundle.putString("positionName",positionName1);
                    bundle.putString("salary",salary1);
                    bundle.putString("createDate",createDate1);
                    bundle.putString("base",base1);
                    bundle.putString("description",description1);
                    bundle.putString("departmentId",departmentId1);
                    bundle.putInt("info_id",info_id);
                    intent.putExtras(bundle);
                    Log.e("eeeer", String.valueOf(bundle));
                    System.out.println(info_id);
                    startActivity(intent);
//                    Toast.makeText(PublishItemInfoActivity.this, "修改信息成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private class deleteItem implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            info_id = Integer.parseInt(getIntent().getExtras().getString("info_id"));
            Map<String, String> map1 = new HashMap<>();
            //map1.put("id",job_id);
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    HttpUtils.deleteRequest("http://124.221.111.224:8081/positionRequire/" + info_id, map1, "utf-8", new OnResponseListener() {
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
                Toast.makeText(PublishItemInfoActivity.this, "删除信息成功", Toast.LENGTH_SHORT).show();
                PublishItemInfoActivity.this.finish();
            } else {
                Toast.makeText(PublishItemInfoActivity.this, "删除信息失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}