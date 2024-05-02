package com.example.vhr.fragment.statistics;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.vhr.R;
import com.example.vhr.databinding.FragmentStatisticsBinding;
import com.example.vhr.entity.Department;
import com.example.vhr.entity.OnTheJobBean;
import com.example.vhr.http.AjaxResult;
import com.example.vhr.http.HttpUtils;
import com.example.vhr.http.OnResponseListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;
    private PieChart mChart;
    private AjaxResult result;
    private List<Department> department;
    private List<OnTheJobBean> onTheJobBean;
    private int count_accounting = 0,count_office = 0,count_manpower = 0,count_production = 0,count_marketing = 0,count_safe = 0,count_other = 0;
    private boolean isFirstLoading = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    public void initData(){
        Map<String, String> map = new HashMap<>();
        map.put("page","1");
        map.put("size","1000");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.getRequest("http://124.221.111.224:8081/employee/basic/getEmployee", map, "utf-8", new OnResponseListener() {
                    //                HttpUtils.postRequest("http://192.168.43.143:8081/doLogin", map, "utf-8", new OnResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        Gson gson = new Gson();
                        Log.e("response", response);
                        AjaxResult result = gson.fromJson(response, AjaxResult.class);
                        Log.e("obiString", result.getObj().toString());
                        onTheJobBean = JSONObject.parseArray(JSON.toJSONString(result.getObj()), OnTheJobBean.class);
                        for (int i = 0; i < onTheJobBean.size(); i++) {
                            if (onTheJobBean.get(i).getDepartmentId() == 106) {
                                count_accounting++;
                            }else if(onTheJobBean.get(i).getDepartmentId() == 118){
                                count_office++;
                            }else if(onTheJobBean.get(i).getDepartmentId() == 119){
                                count_manpower++;
                            }else if(onTheJobBean.get(i).getDepartmentId() == 120){
                                count_production++;
                            }else if(onTheJobBean.get(i).getDepartmentId() == 121){
                                count_marketing++;
                            }else if(onTheJobBean.get(i).getDepartmentId() == 122){
                                count_safe++;
                            }else{
                                count_other++;
                            }
                            System.out.println(count_accounting);
                            System.out.println("a");
                            System.out.println(count_office);
                            System.out.println("b");
                            System.out.println(count_manpower);
                            System.out.println("c");
                            System.out.println(count_production);
                            System.out.println("d");
                            System.out.println(count_marketing);
                            System.out.println("e");
                            System.out.println(count_safe);
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
            if (onTheJobBean == null) {
//                    Log.e("message", "错误");
            }
            while (onTheJobBean == null) {
                //等待线程执行完毕
                Thread.sleep(100);
            }
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mChart = getView().findViewById(R.id.chart);

        initPieChart();
        List<PieEntry> strings = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();


        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
//
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }

        for (int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
        }
        initData();
        System.out.println(count_production);
        strings.add(new PieEntry(count_office,"行政办公室"));
        strings.add(new PieEntry(count_manpower,"人力资源部"));
        strings.add(new PieEntry(count_accounting,"财务部"));
        strings.add(new PieEntry(count_production,"生产技术部"));
        strings.add(new PieEntry(count_marketing,"营销部"));
        strings.add(new PieEntry(count_safe,"安全监督部"));
//        strings.add(new PieEntry(count_other,"未分配部门"));
        showSolidPieChart(strings, colors);
    }

    public void showSolidPieChart(List<PieEntry> dataList, List<Integer> colors) {
        //数据集合
        PieDataSet dataSet = new PieDataSet(dataList, "");
        //填充每个区域的颜色
        dataSet.setColors(colors);
        //是否在图上显示数值
        dataSet.setDrawValues(true);
//        文字的大小
        dataSet.setValueTextSize(15);
//        文字的颜色
        dataSet.setValueTextColor(R.color.black);
//        文字的样式
        dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);

//      当值位置为外边线时，表示线的前半段长度。
        dataSet.setValueLinePart1Length(0.5f);
//      当值位置为外边线时，表示线的后半段长度。
        dataSet.setValueLinePart2Length(0.6f);
//      当ValuePosits为OutsiDice时，指示偏移为切片大小的百分比
        dataSet.setValueLinePart1OffsetPercentage(80f);
        // 当值位置为外边线时，表示线的颜色。

        //dataSet.setValueLineColor(R.color.green);

        //设置Y值的位置是在圆内还是圆外
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        设置Y轴描述线和填充区域的颜色一致
        dataSet.setUsingSliceColorAsValueLineColor(true);
//        设置每条之前的间隙
        dataSet.setSliceSpace(1);

        //设置饼状Item被选中时变化的距离
        dataSet.setSelectionShift(15f);
        //填充数据
        PieData pieData = new PieData(dataSet);
        //格式化显示的数据为%百分比
        pieData.setValueFormatter(new PercentFormatter());
        //显示试图
        mChart.setData(pieData);
        mChart.animateXY(1400, 1400);
        mChart.invalidate();

    }

    private void initPieChart() {
        //  是否显示中间的洞
        mChart.setDrawHoleEnabled(true);
        //设置中间洞的大小
        mChart.setHoleRadius(40f);
//        // 半透明圈
//        mChart.setTransparentCircleRadius(30f);
//        //设置半透明圆圈的颜色
//        mChart.setTransparentCircleColor(Color.WHITE);
//        //设置半透明圆圈的透明度
//        mChart.setTransparentCircleAlpha(100);

        //饼状图中间可以添加文字
        mChart.setDrawCenterText(true);
        //设置中间文字
        mChart.setCenterText("部门人数");
        //中间问题的颜色
        mChart.setCenterTextColor(R.color.black);
        //中间文字的大小px
        mChart.setCenterTextSizePixels(35);
        mChart.setCenterTextRadiusPercent(5f);
        //中间文字的样式
        mChart.setCenterTextTypeface(Typeface.DEFAULT);
        //中间文字的偏移量
        mChart.setCenterTextOffset(0, 0);

        // 初始旋转角度
        mChart.setRotationAngle(0);
        // 可以手动旋转
        mChart.setRotationEnabled(true);
        //显示成百分比
        mChart.setUsePercentValues(false);
        //取消右下角描述
        mChart.getDescription().setEnabled(false);

        //是否显示每个部分的文字描述
        mChart.setDrawEntryLabels(true);
        //描述文字的颜色
        mChart.setEntryLabelColor(R.color.white);
        //描述文字的大小
        mChart.setEntryLabelTextSize(16);
        //描述文字的样式
        mChart.setEntryLabelTypeface(Typeface.MONOSPACE);
        //图相对于上下左右的偏移
        mChart.setExtraOffsets(20, 0, 20, 0);
        //图标的背景色
        mChart.setBackgroundColor(Color.WHITE);
        //设置mChart图表转动阻力摩擦系数[0,1]
        mChart.setDragDecelerationFrictionCoef(0.75f);

        //获取图例
        Legend legend = mChart.getLegend();
        //设置图例水平显示
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //顶部
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        //右对齐
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        //x轴的间距
        legend.setXEntrySpace(7f);
        //y轴的间距
        legend.setYEntrySpace(10f);
        //图例的y偏移量
        legend.setYOffset(15f);
        //图例x的偏移量
        legend.setXOffset(0);
        //图例文字的颜色
        legend.setTextColor(R.color.violet);
        //图例文字的大小
        legend.setTextSize(13);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}