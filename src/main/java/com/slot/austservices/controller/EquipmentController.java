package com.slot.austservices.controller;

import com.slot.austservices.result.Result;
import com.slot.austservices.utils.MyLineRegression;
import com.slot.austservices.utils.PredictData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/*
 * fileName: EquipmentController
 * author: your name
 * time:2022/10/30 6:15
 * description: none
 */
@Api(tags = "设备API")
@RestController
@RequestMapping("/equipment")
public class EquipmentController {


    /*
     *   JdbcTemplate 中会自己注入数据源,用于简化JDBC操作
     *
     * */
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/login")
    public Result<List<Map<String, Object>>> login(@RequestParam(value = "name") String name, @RequestParam(value = "password") String password) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        if (!name.equals("root")) {
            result.put("result", "error:账号错误");
        } else if (!password.equals("123456")) {
            result.put("result", "error:密码错误");
        } else {
            result.put("result", "success:登录成功");
        }
        list.add(result);
        return Result.success(list);
    }

    @GetMapping("/getEquipment")
    public Result<List<Map<String, Object>>> getEquipment(@RequestParam(value = "ID") String ID) {
        String sql = "select * from t9 where id = " + ID + " order by datetime desc limit 40 ";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return Result.success(list);
    }

    @GetMapping("/getEquipmentPredict")
    public Result<List<Map<String, Object>>> getEquipmentPredict(@RequestParam(value = "ID") String ID) {
        String sql1 = "select * from t9 where id = " + ID + " and DATE_FORMAT(times,'%i')%300 = 0 order by times desc limit 25";
        String sql = "select * from t9 where id = " + ID;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        //t:温度 z：振幅
        List<Double> t_1 = new ArrayList<Double>();
        List<Double> t_2 = new ArrayList<Double>();
        List<Double> z_1 = new ArrayList<Double>();
        List<Double> z_2 = new ArrayList<Double>();
        List<Double> z_3 = new ArrayList<Double>();
        List<Double> z_4 = new ArrayList<Double>();
        System.out.println(list.size());
        //获得预测区间数组
        for (int i = 0; i < list.size(); i++) {
            t_1.add(Double.valueOf(list.get(i).get("T1").toString()));
            t_2.add(Double.valueOf(list.get(i).get("T2").toString()));
            z_1.add(Double.valueOf(list.get(i).get("Z1").toString()));
            z_2.add(Double.valueOf(list.get(i).get("Z2").toString()));
            z_3.add(Double.valueOf(list.get(i).get("Z3").toString()));
            z_4.add(Double.valueOf(list.get(i).get("Z4").toString()));
        }

        MyLineRegression lineRegression = new MyLineRegression();
        List<Double> pt_1 = new ArrayList<Double>();
        List<Double> pt_2 = new ArrayList<Double>();
        List<Double> pz_1 = new ArrayList<Double>();
        List<Double> pz_2 = new ArrayList<Double>();
        List<Double> pz_3 = new ArrayList<Double>();
        List<Double> pz_4 = new ArrayList<Double>();

        double[] pt1data = new double[t_1.size()];
        double[] pt2data = new double[t_1.size()];
        double[] pz1data = new double[t_1.size()];
        double[] pz2data = new double[t_1.size()];
        double[] pz3data = new double[t_1.size()];
        double[] pz4data = new double[t_1.size()];
        System.out.println(t_1.size());
        for (int i = 0; i < t_1.size(); i++) {
            pt1data[i] = t_1.get(i);
            pt2data[i] = t_2.get(i);
            pz1data[i] = z_1.get(i);
            pz2data[i] = z_2.get(i);
            pz3data[i] = z_3.get(i);
            pz4data[i] = z_4.get(i);
        }
        PredictData predictData = new PredictData();
        pt1data = predictData.predictData(pt1data);
        pt2data = predictData.predictData(pt2data);
        pz1data = predictData.predictData(pz1data);
        pz2data = predictData.predictData(pz2data);
        pz3data = predictData.predictData(pz3data);
        pz4data = predictData.predictData(pz4data);




        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String,Object> result = new HashMap<>();
        result.put("pt1",pt1data);
        result.put("pt2",pt2data);
        result.put("pz1",pz1data);
        result.put("pz2",pz2data);
        result.put("pz3",pz3data);
        result.put("pz4",pz4data);
        resultList.add(result);

        return Result.success(resultList);
    }


}
