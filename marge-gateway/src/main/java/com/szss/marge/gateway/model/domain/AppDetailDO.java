package com.szss.marge.gateway.model.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * @author XXX
 * @date 2018/7/4
 */
@Data
@TableName("gateway_app_details")
public class AppDetailDO implements Serializable {
    
    private static final long serialVersionUID = -7870751145362385679L;
    /**
     * appId，数据来源为oauth服务的clientId
     */
    @TableId
    private String appId;
    /**
     * appSecret,数据来源为oauth服务的clientSecret
     */
    private String appSecret;
    /**
     * sessionKey,通过oauth服务获取的token，并使用md5签名得到的key值
     */
    private String sessionKey;
    /**
     * 可访问服务的token，通过oauth服务获取
     */
    private String token;
    /**
     * 超期时间
     */
    private Date expiredDate;
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
