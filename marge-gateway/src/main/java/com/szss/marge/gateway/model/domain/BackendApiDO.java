package com.szss.marge.gateway.model.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * @author XXX
 * @date 2018/7/6
 */
@Data
@TableName("gateway_backend_api")
public class BackendApiDO implements Serializable {

    private static final long serialVersionUID = -6506081079702511016L;
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * api名称
     */
    private String name;
    /**
     * 服务ID
     */
    private String serviceId;
    /**
     * 路径(使用模式匹配)，例如/foo/*
     */
    private String path;
    /**
     * 是否使用参数签名
     */
    private Boolean signature;
    /**
     * 是否使用token
     */
    private Boolean token;
    /**
     * 逻辑删除
     */
    @TableLogic
    private Boolean deleted;

}
