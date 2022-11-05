package com.slot.austservices.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * fileName: MyLineRegression
 * author: your name
 * time:2022/10/29 22:53
 * description: 最小二乘法：y'= ax+b
 */
public class MyLineRegression {

    public Map<String, Double> lineRegression(Double[] X, Double[] Y)
    {
        if(null == X || null == Y || 0 == X.length
                || 0 == Y.length || X.length != Y.length)
        {
            throw new RuntimeException();
        }

        // x平方差和
        double Sxx = varianceSum(X);
        // y平方差和
        double Syy = varianceSum(Y);
        // xy协方差和
        double Sxy = covarianceSum(X, Y);

        double xAvg = arraySum(X) / X.length;
        double yAvg = arraySum(Y) / Y.length;

        double a = Sxy / Sxx;
        double b = yAvg - a * xAvg;

        // 相关系数
        double r = Sxy / Math.sqrt(Sxx * Syy);
        Map<String, Double> result = new HashMap<String, Double>();
        result.put("a", a);
        result.put("b", b);
        result.put("r", r);

        return result;
    }

    /*
    *  预测结果
    *
    * */
    public List<Double> predictData(List<Double> X, List<Double> Y,double [] X0){

        Double[] tempX = new Double[X.size()];
        X.toArray(tempX);
        Double[] tempY = new Double[X.size()];
        X.toArray(tempY);

        Map<String,Double> result = lineRegression(tempX,tempY);
        double a = result.get("a");
        double b = result.get("b");
        double temp = 0;
        List<Double> xl = new ArrayList<>();
        for (int i = 0; i < X0.length; i++) {
            temp = a*X0[i]+b;
            xl.add(temp);
        }

        return xl;

    }

    /**
     * 计算方差和
     * @param X
     * @return
     */
    private double varianceSum(Double[] X)
    {
        double xAvg = arraySum(X) / X.length;
        return arraySqSum(arrayMinus(X, xAvg));
    }

    /**
     * 计算协方差和
     * @param X
     * @param Y
     * @return
     */
    private double covarianceSum(Double[] X, Double[] Y)
    {
        double xAvg = arraySum(X) / X.length;
        double yAvg = arraySum(Y) / Y.length;
        return arrayMulSum(arrayMinus(X, xAvg), arrayMinus(Y, yAvg));
    }

    /**
     * 数组减常数
     * @param X
     * @param x
     * @return
     */
    private Double[] arrayMinus(Double[] X, Double x)
    {
        int n = X.length;
        Double[] result = new Double[n];
        for(int i = 0; i < n; i++)
        {
            result[i] = X[i] - x;
        }

        return result;
    }

    /**
     * 数组求和
     * @param X
     * @return
     */
    private double arraySum(Double[] X)
    {
        double s = 0 ;
        for( double x : X )
        {
            s = s + x ;
        }
        return s ;
    }

    /**
     * 数组平方求和
     * @param X
     * @return
     */
    private double arraySqSum(Double[] X)
    {
        double s = 0 ;
        for( double x : X )
        {
            s = s + Math.pow(x, 2) ; ;
        }
        return s ;
    }

    /**
     * 数组对应元素相乘求和
     * @param X
     * @return
     */
    private double arrayMulSum(Double[] X, Double[] Y)
    {
        double s = 0 ;
        for( int i = 0 ; i < X.length ; i++ )
        {
            s = s + X[i] * Y[i] ;
        }
        return s ;
    }
}
