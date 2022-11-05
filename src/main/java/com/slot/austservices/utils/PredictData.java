package com.slot.austservices.utils;

import java.util.*;

/*
 * fileName: PredictData
 * author: your name
 * time:2022/11/1 21:42
 * description: none
 */
public class PredictData {
    //拟合方程的阶数
    private int n =7;

    public double[] predictData(double[] data) {
        int step = 300;
        System.out.println(data.length);
        int k = data.length / step;//12
        System.out.println(k);


        //初始化x
        int[] x = new int[k];
        for (int i = 1; i <= k; i++) {
            x[i-1] = i;
        }


        double s = 0;
        //每隔三百求平均数存进y
        double[][] y = createMatric(1, k); //12个点
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < step; j++) {
                s = s + data[i * step + j];
            }
            y[0][i] = s / step;
            s = 0;
        }
        double[][] yr = matricReserve(y);
        double[][] X0 = createMatric(n + 1, k);

        for (int k0 = 0; k0 < k; k0++) {
            for (int n0 = 0; n0 <= n; n0++) {
                X0[n0][k0] = Math.pow(x[k0], n - n0);
            }
        }
        double[][] X = matricReserve(X0);
        double[][] ANSS1 = matricCheng(X0, X);

        double[][] ANSS2 = inverse(ANSS1);
        double[][] ANSS3 = matricCheng(ANSS2, X0);
//        double[][] ANSS = matricCheng(matricCheng(X0, X), inverse(matricCheng(X0, yr)));
        double[][] ANSS = matricCheng(ANSS3,yr);
        double[][] x0 = createMatric(1, k);
        for (int i = 1; i <= k; i++) {
            x0[0][i-1] = i*1.0;
        }
        double[][] y0 = new double[1][k];

        /*
        *
        *        for num=2:1:n+1
                     y0=y0+ANSS(num)*x0.^(n+1-num);
                 end
        * */
        for (int num = 2; num <= n+1; num++) {
            y0 =  matricAdd(y0,matricShuCheng(matricPow(x0,n+1-num),ANSS[num-1][0]));
        }
        double[] y1 = new double[y0[0].length];
        for (int i = 0; i < y0[0].length; i++) {
            y1[i] = y0[0][i];
        }
        System.out.println(y1);
        return y1;
    }


    public double[][] matricPow(double matric1[][], int n) {
        double matric3[][] = new double[matric1.length][matric1[0].length];

        for (int i = 0; i < matric1[0].length; i++){
            matric3[0][i] =  Math.pow(matric1[0][i],n);
        }
        return matric3;
    }
    //矩阵加法
    public double[][] matricShuCheng(double matric[][],double x){
        for(int i=0;i<matric.length;i++)
            for(int j=0;j<matric[0].length;j++) {
                matric[i][j]=matric[i][j]*x;
            }
        return matric;
    }



    public double[][] matricAdd(double matric1[][], double matric2[][]) {
        double matric3[][] = new double[matric1.length][matric1[0].length];
        if (matric1.length != matric2.length || matric1[0].length != matric2[0].length) {
            System.out.println("输入格式有误");
        } else {
            for (int i = 0; i < matric1.length; i++)
                for (int j = 0; j < matric1[0].length; j++)
                    matric3[i][j] = matric1[i][j] + matric2[i][j];
        }
        return matric3;
    }


    //获取矩阵的逆
    public static double[][] inverse(double[][] A) {
        double[][] B = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                B[i][j] = A[i][j];
            }
        }
        double[][] C = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                C[i][j] = Math.pow(-1, i + j) * determinant(minor(B, j, i));
            }
        }
        double[][] D = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                D[i][j] = C[i][j] / determinant(A);
            }
        }
        return D;
    }

    //求解矩阵的行列式
    private static double determinant(double[][] a) {
        if (a.length == 1) {
            return a[0][0];
        }
        double det = 0;
        for (int i = 0; i < a[0].length; i++) {
            det += Math.pow(-1, i) * a[0][i] * determinant(minor(a, 0, i));
        }
        return det;
    }

    //求解二维矩阵在某一位置的伴随矩阵
    private static double[][] minor(double[][] b, int i, int j) {
        double[][] a = new double[b.length - 1][b[0].length - 1];
        for (int x = 0, y = 0; x < b.length; x++) {
            if (x == i) {
                continue;
            }
            for (int m = 0, n = 0; m < b[0].length; m++) {
                if (m == j) {
                    continue;
                }
                a[y][n] = b[x][m];
                n++;
            }
            y++;
        }
        return a;
    }


    //创建矩阵
    public double[][] createMatric(int row, int colum) {
        @SuppressWarnings("resource")
        double array[][] = new double[row][colum];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = 0;
            }
        return array;
    }

    //矩阵乘法
    public double[][] matricCheng(double matric1[][], double matric2[][]) {
        double matric3[][] = new double[matric1.length][matric2[0].length];
        System.out.println(matric3.length+"  "+matric3[0].length);
        if (matric1[0].length != matric2.length) {
            System.out.println("输入格式有误");
            System.exit(0);//退出虚拟机
        } else {
            for (int i = 0; i < matric1.length; i++)
                for (int j = 0; j < matric2[0].length; j++)
                    for (int k = 0; k < matric2.length; k++)
                        matric3[i][j] += matric1[i][k] * matric2[k][j];
        }
        return matric3;
    }

    //矩阵转置
    public double[][] matricReserve(double matric[][]) {
        double matric3[][] = new double[matric[0].length][matric.length];
        for (int i = 0; i < matric.length; i++) {
            for (int j = 0; j < matric[0].length; j++) {
                matric3[j][i] = matric[i][j];
            }
        }
        return matric3;
    }


}
