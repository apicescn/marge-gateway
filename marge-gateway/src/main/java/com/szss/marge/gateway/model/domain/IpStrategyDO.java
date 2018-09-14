package com.szss.marge.gateway.model.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * @author XXX
 * @date 2018/7/9
 */
@Data
@TableName("gateway_ip_strategy")
public class IpStrategyDO implements Serializable {

    private static final long serialVersionUID = -6111763980624018252L;
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
     * 策略类型,0是黑名单，1是白名单
     */
    private Integer type;
    /**
     * IP控制策略列表
     */
    private List<IpControlDO> ipControlList;
    /**
     * 绑定API列表
     */
    private List<BackendApiDO> backendApiList;

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
