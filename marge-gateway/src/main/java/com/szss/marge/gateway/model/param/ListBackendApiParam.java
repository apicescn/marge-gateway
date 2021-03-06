package com.szss.marge.gateway.model.param;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 后台Api查询参数
 *
 * @author XXX
 * @date 2018/7/22
 */
@Data
public class ListBackendApiParam {
  /**
   * api名称
   */
  @ApiModelProperty(value = "api名称", example = "1", position = 7)
  private String name;

  /**
   * 服务ID
   */
  @ApiModelProperty(value = "服务ID", example = "1", position = 7)
  private String serviceId;
  /**
   * 限流Id
   */
  @ApiModelProperty(value = "限流Id", example = "1", position = 7)
  private Long ratelimitStrategyId;

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
