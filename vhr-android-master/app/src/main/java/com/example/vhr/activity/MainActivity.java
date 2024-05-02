package com.example.vhr.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vhr.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.vhr.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();//隐藏顶部标题栏
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_staff, R.id.navigation_manage, R.id.navigation_publish, R.id.navigation_statistics, R.id.navigation_myinfo)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void showAlertDialog(DialogInterface.OnClickListener positiveListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 中间的信息以一个view的形式设置进去
        TextView message = new TextView(this);
        message.setTextColor(Color.BLACK);
        message.setHeight(300);

        message.setText("你确定退出登录？");
        message.setTextSize(20);
        message.setGravity(Gravity.CENTER);
        message.setPadding(20, 40, 20, 0);
        builder.setView(message);

        builder.setPositiveButton("确认", positiveListener).setNegativeButton("取消", null);
        // 调用 show()方法后得到 dialog对象
        AlertDialog dialog = builder.show();
        final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        final Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        // 安卓下面有三个位置的按钮，默认权重为 1,设置成 500或更大才能让两个按钮看起来均分
        LinearLayout.LayoutParams positiveParams = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveParams.gravity = Gravity.LEFT;
        positiveParams.width = 0;
        positiveParams.weight = 500;

        LinearLayout.LayoutParams negativeParams = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
        negativeParams.gravity = Gravity.RIGHT;
        negativeParams.width = 0;
        negativeParams.weight = 500;

        // 设置确认按钮和取消按钮的具体参数
        positiveButton.setLayoutParams(positiveParams);
        negativeButton.setLayoutParams(negativeParams);

        // 背景设置是已经写好的颜色选择器，会根据选中变换设定好的颜色
        positiveButton.setTextSize(20);
        positiveButton.setBackgroundResource(R.drawable.button_pressed_selector);


        negativeButton.setTextSize(20);
        negativeButton.setBackgroundResource(R.drawable.button_pressed_selector);
    }

    public void showLogin() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}