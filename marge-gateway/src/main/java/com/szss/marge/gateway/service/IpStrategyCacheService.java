package com.szss.marge.gateway.service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.CreateCache;
import com.szss.marge.gateway.dao.IpStrategyDAO;
import com.szss.marge.gateway.model.domain.CidrDO;

/**
 * @author XXX
 * @date 2018/7/17
 */
@Service
public class IpStrategyCacheService {
    /**
     * 缓存的名称
     */
    public static final String IP_STRATEGY_CACHE_NAME = "gateway.strategy.cidr.";
    /**
     * IpControlDAO
     */
    @Autowired
    private IpStrategyDAO ipStrategyDAO;
    /**
     * ip策略缓存
     */
    @CreateCache(name = IP_STRATEGY_CACHE_NAME, expire = 3600)
    private Cache<String, List<CidrDO>> ipStrategyCache;

    /**
     * 根据appId、serviceId和path查询CIDR，并且后加配置优先匹配
     *
     * @param appId 应用ID
     * @param serviceId 服务ID
     * @param path 服务的请求路径
     * @return List<CidrDO>
     */
    @Cached(name = IP_STRATEGY_CACHE_NAME, key = "#appId + '.' +#serviceId + '.' + #path", expire = 3600)
    public List<CidrDO> listCidr(String appId, String serviceId, String path) {
        return ipStrategyDAO.listCidrByAppIdAndServiceIdAndPath(appId, serviceId, path);
    }

    /**
     * 根据appId删除缓存,在修改数据前，先执行删除缓存
     * 
     * @param appId 应用ID
     * @param serviceId 服务ID
     * @param path 服务的请求路径
     */
    public void deleteCache(String appId, String serviceId, String path) {
        List<CidrDO> list = ipStrategyDAO.listCidrByAppIdAndServiceIdAndPath(appId, serviceId, path);
        Set<String> keys = new TreeSet();
        list.forEach(x -> {
            String key = x.getAppId() + "." + x.getServiceId() + "." + x.getPath();
            keys.add(key);
        });
        ipStrategyCache.removeAll(keys);
    }
}
