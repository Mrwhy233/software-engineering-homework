package com.example.vhr.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vhr.R;
import com.example.vhr.fragment.Publish.PublishFragment;
import com.example.vhr.fragment.myinfo.MyInfoFragment;
import com.example.vhr.http.AjaxResult;
import com.example.vhr.http.HttpUtils;
import com.example.vhr.http.OnResponseListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private Button cancel,publish;
    private AjaxResult result;
    private EditText positionName,base,salary,description,departmentId;
    private PopupWindow popupWindow;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        findViewId();
    }

    public void findViewId(){
        cancel = (Button) findViewById(R.id.tv_cancel);
        publish = (Button) findViewById(R.id.btn_publish);
        positionName = findViewById(R.id.positionName);
        base = findViewById(R.id.place);
        salary = findViewById(R.id.salary);
        description = findViewById(R.id.description);
        departmentId = findViewById(R.id.departmentId);

        departmentId.setOnClickListener(new Click());
        cancel.setOnClickListener(new OnClick());
        publish.setOnClickListener(new OnClick());
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            switch (view.getId()){
                case R.id.tv_cancel:
                    AddActivity.this.finish();
                    break;
                case R.id.btn_publish:
                    System.out.println("aaaaaaa");
                    Map<String, String> map = new HashMap<>();
                    String positionName1 = positionName.getText().toString().trim();
                    String base1 = base.getText().toString().trim();
                    String salary1 = salary.getText().toString().trim();
                    String description1 = description.getText().toString().trim();
                    String departmentId1 = departmentId.getText().toString().trim();
                    System.out.println("11111111111111");
                    System.out.println(id);
                    map.put("positionName",positionName1);
                    map.put("base",base1);
                    map.put("salary",salary1);
                    map.put("departmentId", String.valueOf(id));
                    map.put("description",description1);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpUtils.postRequestJson("http://124.221.111.224:8081/positionRequire", map, "utf-8", new OnResponseListener() {
                                //                HttpUtils.postRequest("http://192.168.43.143:8081/doLogin", map, "utf-8", new OnResponseListener() {
                                @Override
                                public void onSuccess(String response) {
                                    System.out.println(response);
                                    result = new Gson().fromJson(response, AjaxResult.class);
                                    System.out.println(result.getObj());
                                    System.out.println("bbbbb");
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
                    if (result.getStatus() == 200) {
                        Toast.makeText(AddActivity.this, "发布信息成功", Toast.LENGTH_SHORT).show();
                        AddActivity.this.finish();
                    }
                    break;
            }
        }
    }



    private void showPopupWindow(){
        View contentView = LayoutInflater.from(AddActivity.this).inflate(R.layout.screening,null);
        popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        TextView office = (TextView) contentView.findViewById(R.id.office);
        TextView manpower = (TextView) contentView.findViewById(R.id.manpower);
        TextView accounting = (TextView) contentView.findViewById(R.id.accounting);
        TextView production = (TextView) contentView.findViewById(R.id.production);
        TextView marketing = (TextView) contentView.findViewById(R.id.marketing);
        TextView safe = (TextView) contentView.findViewById(R.id.safe);
        TextView quit_button = (TextView)contentView.findViewById(R.id.quit_button);

        quit_button.setOnClickListener(view -> popupWindow.dismiss());
        office.setOnClickListener(view -> {
            departmentId.setText("行政办公室");
            id = 118;
            popupWindow.dismiss();
        });
        manpower.setOnClickListener(view -> {
            departmentId.setText("人力资源部");
            id = 119;
            popupWindow.dismiss();
        });
        accounting.setOnClickListener(view -> {
            departmentId.setText("财务部");
            id = 106;
            popupWindow.dismiss();
        });
        production.setOnClickListener(view -> {
            departmentId.setText("生产技术部");
            id = 120;
            popupWindow.dismiss();
        });
        marketing.setOnClickListener(view -> {
            departmentId.setText("营销部");
            id = 121;
            popupWindow.dismiss();
        });
        safe.setOnClickListener(view -> {
            departmentId.setText("安全监督部");
            id = 122;
            popupWindow.dismiss();
        });
        popupWindow.showAsDropDown(departmentId, 0, 0);
    }

    private class Click implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            showPopupWindow();
        }
    }
}