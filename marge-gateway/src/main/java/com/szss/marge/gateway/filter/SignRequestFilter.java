package com.szss.marge.gateway.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.szss.marge.common.core.constant.CodeEnum;
import com.szss.marge.common.core.model.dto.RestDTO;
import com.szss.marge.common.util.GatewayUtils;
import com.szss.marge.gateway.constant.Constants;
import com.szss.marge.gateway.constant.GatewayCodeEnum;
import com.szss.marge.gateway.exception.GatewayException;
import com.szss.marge.gateway.model.domain.AppDetailDO;
import com.szss.marge.gateway.model.domain.BackendApiDO;
import com.szss.marge.gateway.service.AppDetailService;
import com.szss.marge.gateway.service.BackendApiService;

import lombok.extern.slf4j.Slf4j;

/**
 * 参数验证
 *
 * @author XXX
 * @date 2018/6/11 18:54
 */
@Slf4j
@Component
public class SignRequestFilter extends ZuulFilter {
    /**
     * 一千毫秒
     */
    private static final int ONE_THOUSAND_MILLISECONDS = 1000;
    /**
     * 参数签名默认有效时间间隔，允许客户端请求最大时间误差为10分钟
     */
    private static final int DEFAULT_TIMESTAMP_VALIDITY = 600000;

    /**
     * url签名有效时间间隔
     */
    private int timestampValidity = DEFAULT_TIMESTAMP_VALIDITY;

    /**
     * 注入zuul配置
     */
    @Autowired
    protected ZuulProperties zuulProperties;
    /**
     * AppDetailService
     */
    @Autowired
    private AppDetailService appDetailService;
    /**
     * BackendApiService
     */
    @Autowired
    private BackendApiService backendApiService;
    /**
     * SimpleRouteLocator
     */
    @Autowired
    private SimpleRouteLocator routeLocator;

    /**
     * pre：可以在请求被路由之前调用
     *
     * routing：在路由请求时候被调用
     *
     * post：在routing和error过滤器之后被调用
     *
     * error：处理请求时发生错误时被调用
     *
     * 指定filter类型为PRE
     * 
     * @return pre
     */
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (StringUtils.isNotEmpty(contextPath)) {
            path = StringUtils.remove(path, contextPath);
        }
        Route route = routeLocator.getMatchingRoute(path);
        String serviceId = route.getLocation();
        path = getServiceURI(request);
        BackendApiDO backendApiDO = backendApiService.getByServiceIdAndPath(serviceId, path);
        // 如果没有找到对应的API，说明API不需要签名认证，直接跳过该过滤器
        if (backendApiDO == null) {
            return false;
        }
        // 先判断是否支持token验证
        if (backendApiDO.getToken()) {
            // 如果需要token验证，判断请求头是否有token，有token则跳过该过滤器
            String token = request.getHeader("Authorization");
            if (StringUtils.isNotEmpty(token)) {
                return false;
            }
        }
        // 如果上面的条件都不满足，那么看接口是否需要参数签名来判断是否执行该过滤器
        return backendApiDO.getSignature();
    }

    /**
     * 过滤器执行方法
     * 
     * @return
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        // 获取请求参数
        Map<String, String> params = getSignRequestParams(request);
        try {
            // 检测参数
            checkSignRequestParams(params);
            String appId = request.getParameter(Constants.REQUEST_APP_ID_PARAMETER_NAME);
            AppDetailDO appDetailDO = appDetailService.selectById(appId);
            // 加强加密
            checkSign(params, appDetailDO);
            // 将token添加到请求头部，并向后端服务透传
            ctx.addZuulRequestHeader("Authorization", "Bearer " + appDetailDO.getToken());
        } catch (GatewayException e) {
            log.error(e.getMessage());
            // 过滤该请求，不对其进行路由
            ctx.setSendZuulResponse(false);
            // 返回错误码
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            // 返回错误内容
            RestDTO restDTO = new RestDTO(e.getCodeEnum());
            ctx.setResponseBody(JSON.toJSONString(restDTO));
        } catch (Exception e) {
            log.error(e.getMessage());
            // 过滤该请求，不对其进行路由
            ctx.setSendZuulResponse(false);
            // 返回错误码
            ctx.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            // 返回错误内容
            RestDTO restDTO = new RestDTO(CodeEnum.UNKNOWN_ERROR);
            restDTO.setMessage(e.getMessage());
            ctx.setResponseBody(JSON.toJSONString(restDTO));
        }
        return null;
    }

    /**
     * 对http请求参数进行处理
     *
     * @param request HttpServletRequest http请求参数
     * @return 返回处理完成的http请求参数map
     */
    private Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap(request.getParameterMap().size());
        Map<String, String[]> requestParams = request.getParameterMap();
        requestParams.forEach((k, v) -> {
            if (v != null && v[0] != null) {
                params.put(k, v[0]);
            } else {
                params.put(k, null);
            }
        });
        return params;
    }

    /**
     * 获取加密的请求参数
     * 
     * @param request HttpServletRequest http请求参数
     * @return 返回需要加密的http请求参数map
     */
    private Map<String, String> getSignRequestParams(HttpServletRequest request) {
        Map<String, String> params = getRequestParams(request);
        // 获取http method方法，get或post
        String httpMethod = request.getMethod();
        // 删除url的zuul.prefix前缀 删除URI结尾的斜杠"/"
        String uri = getServiceURI(request);
        // 获取http method参数
        params.put(Constants.REQUEST_HTTP_METHOD_PARAMETER_NAME, httpMethod);
        // 获取URL路径
        params.put(Constants.REQUEST_URI_PARAMETER_NAME, uri);
        return params;
    }

    /**
     * 获取获取的URI,不包含gateway服务zuul的前缀和结尾的斜杠
     * 
     * @param request HttpServletRequest http请求参数
     * @return 返回服务URI
     */
    private String getServiceURI(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (StringUtils.isNotEmpty(contextPath)) {
            uri = StringUtils.remove(uri, contextPath);
        }
        String slash = "/";
        // 删除url的zuul.prefix前缀
        if (StringUtils.isNotEmpty(zuulProperties.getPrefix()) && uri.startsWith(zuulProperties.getPrefix())) {
            uri = StringUtils.removeFirst(uri, zuulProperties.getPrefix());
        }
        // 删除uri结尾的斜杠
        if (uri.endsWith(slash)) {
            uri = uri.substring(0, uri.lastIndexOf(slash));
        }
        return uri;
    }

    /**
     * 请求参数校验
     *
     * @param params 经过处理的请求参数
     */
    private void checkSignRequestParams(Map<String, String> params) {
        String appId = params.get(Constants.REQUEST_APP_ID_PARAMETER_NAME);
        String sessionKey = params.get(Constants.REQUEST_SESSION_KEY_PARAMETER_NAME);
        String sign = params.get(Constants.REQUEST_SIGN_PARAMETER_NAME);
        String signMethod = params.get(Constants.REQUEST_SIGN_METHOD_PARAMETER_NAME);
        String timestampValue = params.get(Constants.REQUEST_TIMESTAMP_PARAMETER_NAME);
        if (StringUtils.isEmpty(appId)) {
            throw new GatewayException(GatewayCodeEnum.APP_ID_EMPTY);
        }
        if (StringUtils.isEmpty(sessionKey)) {
            throw new GatewayException(GatewayCodeEnum.SESSION_KEY_EMPTY);
        }
        if (StringUtils.isEmpty(timestampValue)) {
            throw new GatewayException(GatewayCodeEnum.TIMESTAMP_EMPTY);
        }
        if (StringUtils.isEmpty(signMethod)) {
            throw new GatewayException(GatewayCodeEnum.SIGN_METHOD_EMPTY);
        } else {
            boolean md5 = GatewayUtils.MD5.equals(signMethod);
            boolean hmac = GatewayUtils.HMAC.equals(signMethod);
            boolean hmacSha256 = GatewayUtils.HMAC_SHA256.equals(signMethod);
            if (!(md5 || hmac || hmacSha256)) {
                throw new GatewayException(GatewayCodeEnum.SIGN_METHOD_ERROR);
            }
        }
        if (StringUtils.isEmpty(sign)) {
            throw new GatewayException(GatewayCodeEnum.SIGN_EMPTY);
        }
        try {
            long timestamp = Long.parseLong(timestampValue);
            Date clientTime = new Date(timestamp * ONE_THOUSAND_MILLISECONDS);
            Date endTime = DateUtils.addMinutes(clientTime, timestampValidity);
            Date currentTime = new Date();
            // 用户发起请求时的unix时间戳，本次请求签名的有效时间为该时间戳+10分钟。
            if (endTime.before(currentTime)) {
                throw new GatewayException(GatewayCodeEnum.REQUEST_URL_TIMEOUT);
            }
        } catch (NumberFormatException e) {
            throw new GatewayException(GatewayCodeEnum.TIMESTAMP_ERROR);
        }
    }

    /**
     * 校验签名
     * 
     * @param params 需要加密的请求参数
     * @param appDetailDO app id相关信息
     * @throws IOException 加密异常
     */
    private void checkSign(Map<String, String> params, AppDetailDO appDetailDO) throws IOException {
        if (appDetailDO == null) {
            throw new GatewayException(GatewayCodeEnum.APP_ID_ERROR);
        }
        if (!StringUtils.equals(appDetailDO.getSessionKey(),
            params.get(Constants.REQUEST_SESSION_KEY_PARAMETER_NAME))) {
            throw new GatewayException(GatewayCodeEnum.SESSION_KEY_ERROR);
        }
        if (StringUtils.isEmpty(appDetailDO.getToken())) {
            throw new GatewayException(GatewayCodeEnum.TOKEN_NOT_FOUND);
        }
        // 客户端参数签名字符串
        String clientSign = params.remove(Constants.REQUEST_SIGN_PARAMETER_NAME);
        log.info("clientSign-->" + clientSign);
        // 服务端签名字符串，根据请求参数进行签名
        String serverSign = GatewayUtils.signRequest(params, appDetailDO.getAppSecret(),
            params.get(Constants.REQUEST_SIGN_METHOD_PARAMETER_NAME));
        log.info("serverSign-->" + serverSign);
        // 比较服务端签名和客户端签名
        if (!StringUtils.equalsAnyIgnoreCase(serverSign, clientSign)) {
            throw new GatewayException(GatewayCodeEnum.INVALID_SIGN);
        }
    }

}
