package org.jfinger.cloud.entity.vo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SysUserCacheInfo {

    private String sysUserCode;

    private String sysUserName;

    private String sysOrgCode;

    private List<String> sysMultiOrgCode;

    private boolean oneDepart;

    public boolean isOneDepart() {
        return oneDepart;
    }

    public void setOneDepart(boolean oneDepart) {
        this.oneDepart = oneDepart;
    }

    public String getSysDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    public String getSysTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public String getSysUserCode() {
        return sysUserCode;
    }

    public void setSysUserCode(String sysUserCode) {
        this.sysUserCode = sysUserCode;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getSysOrgCode() {
        return sysOrgCode;
    }

    public void setSysOrgCode(String sysOrgCode) {
        this.sysOrgCode = sysOrgCode;
    }

    public List<String> getSysMultiOrgCode() {
        return sysMultiOrgCode;
    }

    public void setSysMultiOrgCode(List<String> sysMultiOrgCode) {
        this.sysMultiOrgCode = sysMultiOrgCode;
    }

}
