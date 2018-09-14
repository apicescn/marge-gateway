package com.szss.marge.gateway.model.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * @author XXX
 * @date 2018/7/18
 */
@Data
@TableName("gateway_ratelimit_api_control")
public class RatelimitApiControlDO implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 202738206204551311L;
    /**
     * 策略ID
     */
    private Long strategyId;
    /**
     * app id
     */
    private String appId;
    /**
     * 创建时间
     */
    private Date dateCreated;
    /**
     * 最后更新时间
     */
    private Date lastUpdated;
    /**
     * 删除标示
     */
    @TableLogic
    private Boolean deleted;
}
