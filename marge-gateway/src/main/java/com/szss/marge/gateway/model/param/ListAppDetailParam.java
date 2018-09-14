package com.szss.marge.gateway.model.param;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Log查询参数
 *
 * @author XXX
 * @date 2018/4/12
 */
@Data
public class ListAppDetailParam {

    /**
     * appId，数据来源为oauth服务的clientId
     */
    @ApiModelProperty(value = "appId", example = "appId", required = true)
    private String appId;
    /**
     * appSecret,数据来源为oauth服务的clientSecret
     */
    @ApiModelProperty(value = "appSecret", example = "appSecret", position = 1)
    private String appSecret;
    /**
     * sessionKey,通过oauth服务获取的token，并使用md5签名得到的key值
     */
    @ApiModelProperty(value = "sessionKey", example = "sessionKey", position = 2)
    private String sessionKey;
    /**
     * 可访问服务的token，通过oauth服务获取
     */
    @ApiModelProperty(value = "token", example = "token", position = 3)
    private String token;
    /**
     * 超期时间
     */
    @ApiModelProperty(value = "超期时间", example = "2018-03-09 11:36:21", position = 4)
    @SuppressFBWarnings("EI_EXPOSE_REP")
    private Date expiredDate;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", example = "2018-03-09 11:36:21", position = 5)
    @SuppressFBWarnings("EI_EXPOSE_REP")
    private Date dateCreated;
    /**
     * 最后更新时间
     */
    @ApiModelProperty(value = "最后更新时间", example = "2018-03-09 11:36:21", position = 6)
    @SuppressFBWarnings("EI_EXPOSE_REP")
    private Date lastUpdated;
    /**
     * 删除标示
     */
    @ApiModelProperty(value = "删除标示", example = "1", position = 7)
    private Boolean deleted;
    /**
     * 当前页码
     */
    @Min(value = 1, message = "页面最小为1")
    @NotNull(message = "pageIndex不能为空")
    @ApiModelProperty(value = "当前页码", example = "1", position = 8)
    private int pageIndex;

    /**
     * 当前页面大小
     */
    @Min(value = 1, message = "页面大小最小为1")
    @NotNull(message = "pageSize不能为空")
    @ApiModelProperty(value = "当前页面大小", example = "10", position = 9)
    private int pageSize;

}
