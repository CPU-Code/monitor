package com.cpucode.common;

/**
 * @author : cpucode
 * @date : 2021/10/5 20:40
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
public class SystemDefinition {
    /**
     * redis中指标主题前缀
     */
    public static final String QUOTA_KEY_PREFIX = "wkj:quota:";
    /**
     * redis中gps信息key前缀
     */
    public static final String GPS_KEY_PREFIX = "wkj:gps:quota:";
    /**
     * redis中客户端状态key前缀
     */
    public static final String CLIENT_INFO = "wkj:client:";

    /**
     * redis中客户端状态key前缀
     */
    public static final String CYCLE_KEY = "wkj:cycle:";

    /**
     * redis中设备存储key前缀
     */
    public static final  String DEVICE_KEY = "wkj:device:";
    /**
     * ES中设备索引名称
     */
    public static final String ES_INDEX_NAME = "Device";
    /**
     * redis中通过主题存储的设备Id字段前缀
     */
    public static final String QUOTA_SUBJECT_DEVICE_KEY_PREFIX = "wkj:quota:deviceId:";

    /**
     * redis中设备指标存储key的前缀
     */
    public static final String DEVICE_QUOTA_KEY_PREFIX = "wkj:device:quotaList:";
}
