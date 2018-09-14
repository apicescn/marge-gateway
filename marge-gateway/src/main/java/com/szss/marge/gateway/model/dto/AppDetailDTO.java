package com.szss.marge.gateway.model.dto;

import java.util.Date;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XXX
 * @date 2018/7/5
 */
@Data
@ApiModel(value = "AppDetailDTO", description = "AppDetailDTO")
public class AppDetailDTO {
    /**
     * appId，数据来源为oauth服务的clientId
     */
    @ApiModelProperty(value = "appId", example = "appId", required = true)
    private String appId;
    /**
     * appSecret,数据来源为oauth服务的clientSecret
     */
    @ApiModelProperty(value = "appSecret", example = "appSecret", required = true, position = 1)
    private String appSecret;
    /**
     * sessionKey,通过oauth服务获取的token，并使用md5签名得到的key值
     */
    @ApiModelProperty(value = "sessionKey", example = "sessionKey", required = true, position = 2)
    private String sessionKey;
    /**
     * 可访问服务的token，通过oauth服务获取
     */
    @ApiModelProperty(value = "token", example = "token", required = true, position = 3)
    private String token;
    /**
     * 超期时间
     */
    @ApiModelProperty(value = "超期时间", example = "2018-03-09 11:36:21", required = true, position = 4)
    @SuppressFBWarnings("EI_EXPOSE_REP")
    private Date expiredDate;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", example = "2018-03-09 11:36:21", required = true, position = 5)
    @SuppressFBWarnings("EI_EXPOSE_REP")
    private Date dateCreated;
    /**
     * 最后更新时间
     */
    @ApiModelProperty(value = "最后更新时间", example = "2018-03-09 11:36:21", required = true, position = 6)
    @SuppressFBWarnings("EI_EXPOSE_REP")
    private Date lastUpdated;
    /**
     * 删除标示
     */
    @ApiModelProperty(value = "删除标示", example = "1", required = true, position = 7)
    private Boolean deleted;
}
