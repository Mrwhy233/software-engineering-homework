package com.example.vhr.fragment.Publish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.vhr.R;
import com.example.vhr.activity.AddActivity;
import com.example.vhr.activity.PublishItemInfoActivity;
import com.example.vhr.activity.StaffItemDetailActivity;
import com.example.vhr.databinding.FragmentPublishBinding;
import com.example.vhr.entity.InfoBean;
import com.example.vhr.entity.UserBean;
import com.example.vhr.fragment.staff.StaffFragment;
import com.example.vhr.http.AjaxResult;
import com.example.vhr.http.HttpUtils;
import com.example.vhr.http.OnResponseListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PublishFragment extends Fragment {

    private FragmentPublishBinding binding;
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> ItemList;
    private List<InfoBean> infoBeans;
    private ImageButton imageButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPublishBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        imageButton = getView().findViewById(R.id.btn_publish_item);
        listView = getView().findViewById(R.id.listview);
        imageButton.setOnClickListener(new OnClick());
        initData();
        inAdapter();
    }

    private void inAdapter() {
        simpleAdapter = new SimpleAdapter(
                getActivity(),
                ItemList,
                R.layout.fragment_info_item,
                new String[]{"positionName", "salary"},
                new int[]{
                        R.id.listname,
//                        R.id.positionName,
                        R.id.salary,
//                        R.id.base,
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

    private void initData(){
        ItemList = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.getRequest("http://124.221.111.224:8081/positionRequire/page/1/100", map1, "utf-8", new OnResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        Gson gson = new Gson();
                        Log.e("response", response);
                        AjaxResult result = gson.fromJson(response, AjaxResult.class);
                        Log.e("obiString", result.getObj().toString());
                        infoBeans = JSONObject.parseArray(JSON.toJSONString(result.getObj()), InfoBean.class);
                        for (int i = 0; i < infoBeans.size(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id",   infoBeans.get(i).getId());
                            map.put("positionName",  infoBeans.get(i).getPositionName());
                            map.put("salary",  infoBeans.get(i).getSalary());
                            map.put("createDate",  infoBeans.get(i).getCreateDate());
                            map.put("base",  infoBeans.get(i).getBase());
                            map.put("description",  infoBeans.get(i).getDescription());
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
            while (infoBeans == null) {
                //等待线程执行完毕
                Thread.sleep(100);
            }
            if (infoBeans == null) {
                Log.e("message", "错误");
            }
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class itemOnClick implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String info_id = ItemList.get(position).get("id").toString().trim();
            Toast.makeText(getActivity(), "您选择的是：" + info_id, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("info_id",info_id);
            intent.setClass(getActivity(), PublishItemInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            switch (view.getId()){
                case R.id.btn_publish_item:
                    intent.setClass(getActivity(),AddActivity.class);
                    startActivity(intent);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}