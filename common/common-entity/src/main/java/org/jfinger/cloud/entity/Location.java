package cn.hzyoupu.common.entity;

import java.io.Serializable;

/**
 * 位置坐标
 */
public class Location implements Serializable {

    private static final long serialVersionUID = 8661772677678316280L;

    //坐标系
    private String coordType = "bd09ll";

    //经度
    private Double longitude;

    //纬度
    private Double latitude;

    //海拔
    private Double altitude;

    //误差
    private Float accuracy;

    public String getCoordType() {
        return coordType;
    }

    public void setCoordType(String coordType) {
        this.coordType = coordType;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }
}
