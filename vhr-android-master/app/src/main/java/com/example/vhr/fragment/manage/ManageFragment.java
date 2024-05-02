package com.example.vhr.fragment.manage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.vhr.R;
import com.example.vhr.activity.JobDetailItemActivity;
import com.example.vhr.activity.StaffItemDetailActivity;
import com.example.vhr.databinding.FragmentManageBinding;
import com.example.vhr.entity.OnTheJobBean;
import com.example.vhr.entity.UserBean;
import com.example.vhr.fragment.staff.StaffFragment;
import com.example.vhr.http.AjaxResult;
import com.example.vhr.http.HttpUtils;
import com.example.vhr.http.OnResponseListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ManageFragment extends Fragment {
    private ListView listView;
    private EditText text, search_text;
    private ImageButton moreButton;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> ItemList;
    private List<Map<String, Object>> DepartmentItemList = new ArrayList<>();
    private FragmentManageBinding binding;
    private List<OnTheJobBean> onTheJobBean;
    private PopupWindow popupWindow;
    private boolean select = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        findViewId(view);
        initData();
        inAdapter();
    }

    private void findViewId(View view){
        listView = getView().findViewById(R.id.listview);
        moreButton = getView().findViewById(R.id.more_button);
        moreButton.setOnClickListener(new onclick());
    }

    private void inAdapter() {
//        System.out.println(select);
//        System.out.println(DepartmentItemList);
//        if(DepartmentItemList == null){
//            select = false;
//            Toast.makeText(getActivity(), "该部门没有人", Toast.LENGTH_SHORT).show();
//        }
//        if(!select) {
            simpleAdapter = new SimpleAdapter(
                    getActivity(),
                    ItemList,
                    R.layout.fragment_staff_item,
                    new String[]{"name", "jobName"},
                    new int[]{
                            R.id.name,
                            R.id.jobName,
                    });
//        }else{
//            simpleAdapter = new SimpleAdapter(
//                    getActivity(),
//                    DepartmentItemList,
//                    R.layout.fragment_staff_item,
//                    new String[]{"name", "jobName"},
//                    new int[]{
//                            R.id.name,
//                            R.id.jobName,
//                    });
//        }
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
        map1.put("page","1");
        map1.put("size","1000");
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                HttpUtils.getRequest("http://124.221.111.224:8081/employee/basic/getEmployee", map1, "utf-8", new OnResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        Gson gson = new Gson();
                        Log.e("response", response);
                        AjaxResult result = gson.fromJson(response, AjaxResult.class);
                        Log.e("obiString", result.getObj().toString());
                        onTheJobBean = JSONObject.parseArray(JSON.toJSONString(result.getObj()), OnTheJobBean.class);
                        for (int i = 0; i < onTheJobBean.size(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id",   onTheJobBean.get(i).getId());
                            map.put("name",   onTheJobBean.get(i).getName());
                            map.put("idCard",   onTheJobBean.get(i).getIdCard());
                            map.put("email",   onTheJobBean.get(i).getEmail());
                            map.put("phone",   onTheJobBean.get(i).getPhone());
                            map.put("nativePlace",   onTheJobBean.get(i).getNativePlace());
                            map.put("departmentId",   onTheJobBean.get(i).getDepartmentId());
                            map.put("jobName",   onTheJobBean.get(i).getJobName());
                            map.put("beginContract",   onTheJobBean.get(i).getBeginContract());
                            map.put("endContract",   onTheJobBean.get(i).getEndContract());
                            ItemList.add(map);
                        }
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

    private class itemOnClick implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String job_id = ItemList.get(position).get("id").toString().trim();
            Toast.makeText(getActivity(), "您选择的是：" + job_id, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("job_id",job_id);
            intent.putExtra("ItemList", (Serializable) ItemList);
            intent.setClass(getActivity(), JobDetailItemActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class onclick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            showPopupWindow();
        }
    }

    private void showPopupWindow(){
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.screening,null);
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
            selectStaff(118);
            select = true;
            inAdapter();
            Toast.makeText(getActivity(), "行政办公室", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
        manpower.setOnClickListener(view -> {
            selectStaff(119);
            select = true;
            inAdapter();
            Toast.makeText(getActivity(), "人力资源部", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
        accounting.setOnClickListener(view -> {
            selectStaff(106);
            select = true;
            inAdapter();
            Toast.makeText(getActivity(), "财务部", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
        production.setOnClickListener(view -> {
            selectStaff(120);
            select = true;
            inAdapter();
            Toast.makeText(getActivity(), "生产技术部", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
        marketing.setOnClickListener(view -> {
            selectStaff(121);
            select = true;
            inAdapter();
            Toast.makeText(getActivity(), "营销部", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
        safe.setOnClickListener(view -> {
            selectStaff(122);
            select = true;
            inAdapter();
            Toast.makeText(getActivity(), "安全监督部", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
        popupWindow.showAsDropDown(moreButton, 0, 0);
    }

    public void selectStaff(int jobId){
        Map<String, String> map1 = new HashMap<>();
        for (int i = ItemList.size() - 1; i >= 0 ; i--) {
            ItemList.remove(i);
        }
        Log.e("Test", String.valueOf(ItemList));
        map1.put("page","1");
        map1.put("size","1000");
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                HttpUtils.getRequest("http://124.221.111.224:8081/employee/basic/getEmployee", map1, "utf-8", new OnResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        Gson gson = new Gson();
                        Log.e("response", response);
                        AjaxResult result = gson.fromJson(response, AjaxResult.class);
                        Log.e("obiString", result.getObj().toString());
                        onTheJobBean = JSONObject.parseArray(JSON.toJSONString(result.getObj()), OnTheJobBean.class);
                        for (int i = 0; i < onTheJobBean.size(); i++) {
                            System.out.println("test001");
                            System.out.println(jobId);
                            Map<String, Object> map = new HashMap<>();
                            System.out.println(onTheJobBean.get(i).getDepartmentId());
                            if(onTheJobBean.get(i).getDepartmentId() == jobId) {
                                System.out.println("test002");
                                map.put("id", onTheJobBean.get(i).getId());
                                map.put("name", onTheJobBean.get(i).getName());
                                map.put("idCard", onTheJobBean.get(i).getIdCard());
                                map.put("email", onTheJobBean.get(i).getEmail());
                                map.put("phone", onTheJobBean.get(i).getPhone());
                                map.put("nativePlace", onTheJobBean.get(i).getNativePlace());
                                map.put("departmentId", onTheJobBean.get(i).getDepartmentId());
                                map.put("jobName", onTheJobBean.get(i).getJobName());
                                map.put("beginContract", onTheJobBean.get(i).getBeginContract());
                                map.put("endContract", onTheJobBean.get(i).getEndContract());
                                System.out.println(map);
                                ItemList.add(map);
                            }
                        }
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
}