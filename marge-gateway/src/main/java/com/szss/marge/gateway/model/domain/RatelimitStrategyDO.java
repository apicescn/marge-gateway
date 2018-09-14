package com.szss.marge.gateway.model.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * @author XXX
 * @date 2018/7/18
 */
@Data
@TableName("gateway_ratelimit_strategy")
public class RatelimitStrategyDO implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5153564624323529234L;
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 策略名称
     */
    private String name;
    /**
     * 时间单位
     */
    private String timeUnit;
    /**
     * api调用限制次数
     */
    private Integer apiRatelimitCount;
    /**
     * app调用限制次数，appRatelimitCount < apiRatelimitCount
     */
    private Integer appRatelimitCount;
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
