package com.example.vhr.fragment.staff;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.vhr.R;
import com.example.vhr.activity.StaffItemDetailActivity;
import com.example.vhr.databinding.FragmentStaffBinding;
import com.example.vhr.entity.UserBean;
import com.example.vhr.http.AjaxResult;
import com.example.vhr.http.HttpUtils;
import com.example.vhr.http.OnResponseListener;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffFragment extends Fragment {
    private ListView listView;
    private EditText text, search_text;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> ItemList;
    private ImageButton moreButton;
    private Button agree, refuse;
    private List<UserBean> userBeans;
    private FragmentStaffBinding binding;
    private PopupWindow popupWindow;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStaffBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViewId(view);
        initData();
        inAdapter();
    }

    private void findViewId(View view) {
        listView = getView().findViewById(R.id.listview);
//        moreButton = getView().findViewById(R.id.more_button);
//
//        moreButton.setOnClickListener(new onclick());
    }

    private void inAdapter() {
        simpleAdapter = new SimpleAdapter(
                getActivity(),
                ItemList,
                R.layout.fragment_staff_item,
                new String[]{"jobSeekerName", "jobName"},
                new int[]{
                        R.id.name,
                        R.id.jobName,
                });
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if ((view instanceof ImageView) && (data instanceof Bitmap)) {
                    ImageView imageView = (ImageView) view;
                    Bitmap bitmap = (Bitmap) data;
                    imageView.setImageBitmap(bitmap);
                    return true;
                }
                return false;
            }
        });
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new itemOnClick());
    }

    private void initData() {
        ItemList = new ArrayList<>();
        Log.e("test", "进入");
        Map<String, String> map1 = new HashMap<>();
//        map1.put("pageNum","1");
//        map1.put("pageSize","1000");
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                HttpUtils.getRequest("http://124.221.111.224:8081/jobSeeker/page/1/100", map1, "utf-8", new OnResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        Gson gson = new Gson();
                        Log.e("response", response);
                        AjaxResult result = gson.fromJson(response, AjaxResult.class);
                        Log.e("obiString", result.getObj().toString());
                        userBeans = JSONObject.parseArray(JSON.toJSONString(result.getObj()), UserBean.class);
                        for (int i = 0; i < userBeans.size(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id",   userBeans.get(i).getId());
                            map.put("jobSeekerName",  userBeans.get(i).getJobSeekerName());
                            map.put("jobId",  userBeans.get(i).getJobId());
                            map.put("jobSeekerEmail",  userBeans.get(i).getJobSeekerEmail());
                            map.put("jobSeekerTel",  userBeans.get(i).getJobSeekerTel());
                            map.put("jobName",  userBeans.get(i).getJobName());
                            map.put("idNumber",  userBeans.get(i).getIdNumber());
                            map.put("take",  userBeans.get(i).getTake());
                            ItemList.add(map);
                        }
                        System.out.println(2);
                        System.out.println(ItemList);
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
            while (userBeans == null) {
                //等待线程执行完毕
                Thread.sleep(100);
            }
            if (userBeans == null) {
                Log.e("message", "错误");
            }
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class itemOnClick implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String staff_id = ItemList.get(position).get("id").toString().trim();
            Toast.makeText(getActivity(), "您选择的是：" + staff_id, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("staff_id",staff_id);
            intent.setClass(getActivity(), StaffItemDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}