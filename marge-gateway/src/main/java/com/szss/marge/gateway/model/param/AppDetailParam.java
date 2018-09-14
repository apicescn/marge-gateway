package com.szss.marge.gateway.model.param;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author XXX
 * @date 2018/4/12
 */
@Data
public class AppDetailParam {

    /**
     * appId，数据来源为oauth服务的clientId
     */
    @ApiParam(value = "appId", example = "appId", required = true)
    private String appId;
    /**
     * appSecret,数据来源为oauth服务的clientSecret
     */
    @ApiParam(value = "appSecret", example = "appSecret", required = true)
    private String appSecret;
    /**
     * sessionKey,通过oauth服务获取的token，并使用md5签名得到的key值
     */
    @ApiParam(value = "sessionKey", example = "sessionKey")
    private String sessionKey;
    /**
     * 可访问服务的token，通过oauth服务获取
     */
    @ApiParam(value = "token", example = "token")
    private String token;
    /**
     * 有效期
     */
    @ApiParam(value = "有效期", example = "100000")
    private Long expiresIn;

}
