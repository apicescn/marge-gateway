package com.szss.marge.gateway.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.szss.marge.gateway.constant.Constants;
import com.szss.marge.gateway.model.domain.CidrDO;
import com.szss.marge.gateway.service.IpStrategyCacheService;

import lombok.extern.slf4j.Slf4j;

/**
 * ip白名单
 * 
 * @author XXX
 * @date 2018/7/8
 */
@Slf4j
@Component
public class IpControlFilter extends ZuulFilter {

    /**
     * SimpleRouteLocator
     */
    @Autowired
    private SimpleRouteLocator routeLocator;

    /**
     * IpStrategyCacheService
     */
    @Autowired
    private IpStrategyCacheService ipStrategyCacheService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
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
        List<CidrDO> list = ipStrategyCacheService.listCidr(appId, serviceId, path);
        String requestIp = this.getIpAddress(request);

        Boolean allowAccess = null;

        for (CidrDO cidrDO : list) {
            String temp = cidrDO.getCidr();
            if (StringUtils.isEmpty(temp)) {
                continue;
            }
            String[] cidrArray = StringUtils.split(temp, ",");
            for (String cidr : cidrArray) {
                Boolean match = false;
                if (StringUtils.contains(cidr, "/")) {
                    match = cidrRange(requestIp, cidr);
                } else {
                    match = StringUtils.equals(requestIp, cidr);
                }
                if (match) {
                    allowAccess = match && (cidrDO.getType() == 1);
                    break;
                }
            }
            if (allowAccess != null) {
                break;
            }
        }
        if (allowAccess == null) {
            allowAccess = (list.size() == 0);
        }
        if (!allowAccess) {
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody("Ip address is forbidden!");
        }
        return null;

    }

    /**
     * 获取Ip地址
     * 
     * @param request http请求
     * @return ip地址
     */
    public String getIpAddress(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * ip和cidr是否匹配
     *
     * @param ip ip地址
     * @param cidr 无类别域间路由
     * @return ip和cidr是否匹配
     */
    public boolean cidrRange(String ip, String cidr) {
        String[] ips = ip.split("\\.");
        int ipAddr = (Integer.parseInt(ips[0]) << 24) | (Integer.parseInt(ips[1]) << 16)
            | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String cidrIp = cidr.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24) | (Integer.parseInt(cidrIps[1]) << 16)
            | (Integer.parseInt(cidrIps[2]) << 8) | Integer.parseInt(cidrIps[3]);
        return (ipAddr & mask) == (cidrIpAddr & mask);
    }

}
