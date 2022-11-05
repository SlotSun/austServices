package com.slot.austservices.model;

import lombok.Data;

/*
 * fileName: Equipment
 * author: your name
 * time:2022/10/29 23:01
 * description: none
 */
@Data
public class Equipment {

    //主机id
    private String ID;
    //时间
    private String Time;
//   温度：℃
    private double temperature_1;
    private double temperature_2;
//  振幅：mmm/ms
    private double amplitude_1;
    private double amplitude_2;
    private double amplitude_3;
    private double amplitude_4;
}
