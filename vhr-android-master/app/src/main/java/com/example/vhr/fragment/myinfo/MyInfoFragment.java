package com.example.vhr.fragment.myinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.vhr.R;
import com.example.vhr.activity.ChangeInfoActivity;
import com.example.vhr.activity.MainActivity;
import com.example.vhr.activity.ManageInfoActivity;
import com.example.vhr.databinding.FragmentMyinfoBinding;
import com.example.vhr.entity.Admin;
import com.example.vhr.entity.MainInfo;
import com.example.vhr.entity.UserBean;
import com.example.vhr.http.AjaxResult;
import com.example.vhr.http.HttpUtils;
import com.example.vhr.http.OnResponseListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MyInfoFragment extends Fragment {

    private TextView nameText;
    private TextView roleText;
    private Button exitButton;
    private ImageView avatarImage;
    private PopupWindow popupWindow;
    private LinearLayout rv1,rv2;
    private FragmentMyinfoBinding binding;
    private Admin admin;
    private  Bitmap bitmap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyinfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        admin =
        System.out.println(MainInfo.admin.getName());


        System.out.println("1111111111111111111111111111111111");
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        findViewId(view);
        initOnClick();

        nameText.setText(MainInfo.admin.getName());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                 bitmap=getImg(MainInfo.admin.getUserface());
            }
        });

        thread.start();
        try {
            while (bitmap == null) {
                //等待线程执行完毕
                Thread.sleep(100);
            }
            if (bitmap == null) {
                Log.e("message", "错误");
            }
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        avatarImage.setImageBitmap(bitmap);

    }
    private Bitmap getImg(String url){
        Bitmap bitmap=null;
        try {
            URL mapUrl=new URL(url);
            // 获取HttpURLConnection的实例对象
            HttpURLConnection connection=(HttpURLConnection)mapUrl.openConnection();
            // 设置连接超时为5秒
            connection.setConnectTimeout(5000);
            //进行链接
            connection.connect();
            if (connection.getResponseCode()==200){
                //获取从服务器返回的输入流
                InputStream inputStream=connection.getInputStream();
                //把输入流转换为Bitmap
                bitmap= BitmapFactory.decodeStream(inputStream);
                //关闭输入流
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void initOnClick(){
        rv1.setOnClickListener(new rlOnClick());
        rv2.setOnClickListener(new rlOnClick());
        exitButton.setOnClickListener(new rlOnClick());
    }

    public void findViewId(View view) {
        nameText = getView().findViewById(R.id.name);
        roleText = getView().findViewById(R.id.role);
        avatarImage = getView().findViewById(R.id.my_operator_avatar);
        exitButton = getView().findViewById(R.id.exitButton);
        rv1 = view.findViewById(R.id.my_modify_message_layout);
        rv2 = view.findViewById(R.id.my_operator_operate_layout);
    }

    private class rlOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            switch (v.getId()) {
                case R.id.my_modify_message_layout:
                    intent.setClass(getActivity(), ChangeInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.my_operator_operate_layout:
                    intent.setClass(getActivity(), ManageInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.exitButton:
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.showAlertDialog(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mainActivity.showLogin();
                            // MyFragment的事务管理是navigation的事务管理，这里如果调用MainActivity的事务管理会崩溃
                            getParentFragmentManager().beginTransaction().remove(MyInfoFragment.this).commit();
                        }
                    });
                    break;
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}