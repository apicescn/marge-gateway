package com.szss.marge.gateway.filter;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.szss.marge.common.core.model.dto.RestDTO;
import com.szss.marge.gateway.constant.Constants;
import com.szss.marge.gateway.constant.GatewayCodeEnum;
import com.szss.marge.gateway.model.domain.RatelimitStrategyDO;
import com.szss.marge.gateway.service.RatelimitStrategyCacheService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author XXX
 * @date 2018/7/19
 */
@Slf4j
@Component
public class RatelimitFilter extends ZuulFilter {
    /**
     * 缓存的名称
     */
    public static final String RATELIMIT_CACHE_NAME = "gateway.ratelimit.";
    /**
     * 秒
     */
    public static final String TIME_UNIT_SECOND = "second";
    /**
     * 分钟
     */
    public static final String TIME_UNIT_MINUTE = "minute";
    /**
     * 小时
     */
    public static final String TIME_UNIT_HOUR = "hour";
    /**
     * 天
     */
    public static final String TIME_UNIT_DAY = "day";

    /**
     * SimpleRouteLocator
     */
    @Autowired
    private SimpleRouteLocator routeLocator;

    /**
     * RatelimitStrategyCacheService
     */
    @Autowired
    private RatelimitStrategyCacheService ratelimitStrategyCacheService;
    /**
     * redis template
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (StringUtils.isNotEmpty(contextPath)) {
            path = StringUtils.remove(path, contextPath);
        }
        Route route = routeLocator.getMatchingRoute(path);
        String serviceId = route.getLocation();
        path = "/" + route.getId() + route.getPath();
        String appId = request.getParameter(Constants.REQUEST_APP_ID_PARAMETER_NAME);
        RatelimitStrategyDO strategy
            = ratelimitStrategyCacheService.listRatelimitStrategyByServiceIdAndPath(serviceId, path);
        if (strategy != null) {
            String ratelimitKey = RATELIMIT_CACHE_NAME + strategy.getName();
            String hashKey = serviceId + "." + path;
            TimeUnit timeUnit = null;
            switch (strategy.getTimeUnit()) {
                case TIME_UNIT_SECOND:
                    ratelimitKey += "." + TIME_UNIT_SECOND;
                    timeUnit = TimeUnit.SECONDS;
                    break;
                case TIME_UNIT_MINUTE:
                    ratelimitKey += "." + TIME_UNIT_MINUTE;
                    timeUnit = TimeUnit.MINUTES;
                    break;
                case TIME_UNIT_HOUR:
                    ratelimitKey += "." + TIME_UNIT_HOUR;
                    timeUnit = TimeUnit.HOURS;
                    break;
                case TIME_UNIT_DAY:
                    ratelimitKey += "." + TIME_UNIT_DAY;
                    timeUnit = TimeUnit.DAYS;
                    break;
                default:
                    ratelimitKey += "." + TIME_UNIT_SECOND;
                    timeUnit = TimeUnit.SECONDS;
            }
            // api限流次数不为空的话，下面的api进行限流
            if (strategy.getApiRatelimitCount() != null && strategy.getApiRatelimitCount() > 0) {
                // 对hash字段数值加1，并返回结果
                Long count = redisTemplate.opsForHash().increment(ratelimitKey, hashKey, 1);
                // 如果count为1，说明hash值新新建的，同时这个hash数据也可能是新建的
                if (count == 1) {
                    // 获取key的超期时间
                    Long expire = redisTemplate.getExpire(ratelimitKey, timeUnit);
                    // 如果超期时间为-1,说明没有设置超期时间
                    if (expire == -1) {
                        // 设置超期时间，可能是1秒、1分钟、1小时、1天
                        redisTemplate.expire(ratelimitKey, 1, timeUnit);
                    }
                }
                // 如果超出限流数，返回失败信息
                if (count > strategy.getApiRatelimitCount()) {
                    // 过滤该请求，不对其进行路由
                    ctx.setSendZuulResponse(false);
                    // 返回错误码
                    ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
                    // 返回错误内容
                    RestDTO restDTO = new RestDTO(GatewayCodeEnum.BEYOND_API_RATE_LINIT);
                    ctx.setResponseBody(JSON.toJSONString(restDTO));
                }
            }
            // APP限流次数不为空的话，下面的APP进行限流
            if (strategy.getAppRatelimitCount() != null && strategy.getAppRatelimitCount() > 0) {
                // APP限流，redis的key新增appId
                ratelimitKey += "." + appId;
                // 对hash字段数值加1，并返回结果
                Long count = redisTemplate.opsForHash().increment(ratelimitKey, hashKey, 1);
                // 如果count为1，说明hash值新新建的，同时这个hash数据也可能是新建的
                if (count == 1) {
                    // 设置超期时间，可能是1秒、1分钟、1小时、1天
                    Long expire = redisTemplate.getExpire(ratelimitKey, timeUnit);
                    // 如果超期时间为-1,说明没有设置超期时间
                    if (expire == -1) {
                        redisTemplate.expire(ratelimitKey, 1, timeUnit);
                    }
                }
                // 如果超出限流数，返回失败信息
                if (count > strategy.getApiRatelimitCount()) {
                    // 过滤该请求，不对其进行路由
                    ctx.setSendZuulResponse(false);
                    // 返回错误码
                    ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
                    // 返回错误内容
                    RestDTO restDTO = new RestDTO(GatewayCodeEnum.BEYOND_APP_RATE_LINIT);
                    ctx.setResponseBody(JSON.toJSONString(restDTO));
                }
            }
        }

        return null;
    }
}
