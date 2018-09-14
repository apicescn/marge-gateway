package com.szss.marge.gateway.model.param;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XXX
 * @date 2018/6/4
 */
@Data
public class ClientParam {

    /**
     * clientId
     */
    @NotEmpty(message = "clientId不能为空")
    @ApiModelProperty(value = "clientId", example = "admin", required = true)
    private String clientId;
    /**
     * clientSecret
     */
    @NotEmpty(message = "clientSecret")
    @ApiModelProperty(value = "clientSecret", example = "123456", required = true, position = 1)
    private String clientSecret;
}
