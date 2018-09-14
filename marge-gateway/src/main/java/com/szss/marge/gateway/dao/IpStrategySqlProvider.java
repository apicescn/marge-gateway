package com.szss.marge.gateway.dao;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author XXX
 * @date 2018/7/17
 */
public class IpStrategySqlProvider {
    /**
     * 根据appId、serviceId和path查询CIDR，并且后加配置优先匹配
     * 
     * @param param dao方法参数
     * @return 拼接sql语句
     */
    public String sql(Map<String, String> param) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.type,c.cidr,c.app_id as appId,ba.service_id serviceId,ba.path "
            + " from gateway_ip_control c,gateway_ip_strategy s,gateway_ip_api_control ac,gateway_backend_api ba "
            + " where c.strategy_id=s.id and ac.strategy_id=s.id and ac.api_id=ba.id "
            + " and c.deleted=0 and s.deleted =0 and ac.deleted=0 and ba.deleted=0 ");
        String appId = param.get("appId");
        if (StringUtils.isNotEmpty(appId)) {
            sql.append(" and c.app_id=#{appId} ");
        }
        String serviceId = param.get("serviceId");
        if (StringUtils.isNotEmpty(serviceId)) {
            sql.append(" and ba.service_id=#{serviceId} ");
        }
        String path = param.get("path");
        if (StringUtils.isNotEmpty(path)) {
            sql.append(" and ba.path=#{path} ");
        }
        sql.append(" order by s.id desc,c.id desc ");
        return sql.toString();
    }
}
