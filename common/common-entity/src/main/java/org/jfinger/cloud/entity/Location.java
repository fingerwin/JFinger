package org.jfinger.cloud.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @Description 位置坐标
 * @Author finger
 * @Date 2020/12/23 0023
 * @Version 1.0
 */
@Data
@RequiredArgsConstructor
public class Location implements Serializable {

    private static final long serialVersionUID = 8661772677678316280L;

    //坐标系
    private String coordType = "wgs84";

    //经度
    @NonNull
    private Double longitude;

    //纬度
    @NonNull
    private Double latitude;

    //海拔
    private Double altitude = 0.0;

    //误差
    private Float accuracy = 0.0f;

    //地址
    private String address;
}
