package com.szss.marge.gateway.model.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * @author XXX
 * @date 2018/7/9
 */
@Data
@TableName("gateway_ip_control")
public class IpControlDO implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4163386424032122940L;
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 策略ID
     */
    private Long strategyId;
    /**
     * app id
     */
    private String appId;
    /**
     * 网段配置，无类别域间路由，Classless Inter-Domain Routing
     */
    private String cidr;

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
