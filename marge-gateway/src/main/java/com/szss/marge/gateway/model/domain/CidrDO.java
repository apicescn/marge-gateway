package com.szss.marge.gateway.model.domain;

import java.io.Serializable;

import lombok.Data;

/**
 * @author XXX
 * @date 2018/7/17
 */
@Data
public class CidrDO implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2902765757096169546L;
    /**
     * 策略类型,0是黑名单，1是白名单
     */
    private Integer type;
    /**
     * CIDR
     */
    private String cidr;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 服务ID
     */
    private String serviceId;
    /**
     * 服务的请求地址
     */
    private String path;
}
