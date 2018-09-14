package com.szss.marge.gateway.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.szss.marge.common.core.constant.CodeEnum;
import com.szss.marge.common.core.model.dto.SimplePageDTO;
import com.szss.marge.gateway.dao.RatelimitStrategyDAO;
import com.szss.marge.gateway.model.domain.CidrDO;
import com.szss.marge.gateway.model.domain.RatelimitStrategyDO;
import com.szss.marge.gateway.model.dto.RatelimitStrategyDTO;
import com.szss.marge.gateway.model.param.ListRatelimitStrategyParam;
import com.szss.marge.gateway.model.param.RatelimitStrategyParam;
import com.szss.marge.gateway.rest.constant.RestConstant;

/**
 * @author XXX
 * @date 2018/7/18
 */
@Service
public class RatelimitStrategyCacheService extends ServiceImpl<RatelimitStrategyDAO, RatelimitStrategyDO> {
    /**
     * 缓存的名称
     */
    public static final String RATELIMIT_STRATEGY_CACHE_NAME = "gateway.strategy.ratelimit.";
    /**
     * RatelimitStrategyDAO
     */
    @Autowired
    private RatelimitStrategyDAO ratelimitStrategyDAO;

    /**
     * 流量控制策略缓存
     */
    @CreateCache(name = RATELIMIT_STRATEGY_CACHE_NAME, expire = 3600)
    private Cache<String, List<CidrDO>> ratelimitStrategyCache;

    /**
     * 根据访问API获取访问频次控制，并且后加配置优先匹配
     *
     * @param serviceId 服务ID
     * @param path 服务的请求路径
     * @return List<RatelimitStrategyDO>
     */
    @Cached(name = RATELIMIT_STRATEGY_CACHE_NAME, key = "#serviceId + '.' + #path", expire = 3600)
    public RatelimitStrategyDO listRatelimitStrategyByServiceIdAndPath(String serviceId, String path) {
        List<RatelimitStrategyDO> ratelimitStrategyDOList =
            ratelimitStrategyDAO.listRatelimitStrategyByServiceIdAndPath(serviceId, path);
        if (!CollectionUtils.isEmpty(ratelimitStrategyDOList)) {
            return ratelimitStrategyDOList.get(0);
        }
        return null;
    }

    /**
     * 根据serviceId和path删除缓存,在修改数据前，先执行删除缓存
     *
     * @param serviceId 服务ID
     * @param path 服务的请求路径
     */
    public void deleteCache(String serviceId, String path) {
        ratelimitStrategyCache.remove(serviceId + "." + path);
    }

    /**
     * 根据查询条件查询限流策略列表
     *
     * @param ratelimitStrategyParam 查询条件
     * @return
     */
    public SimplePageDTO<List<RatelimitStrategyDTO>>
        listRatelimitStrategyByPage(ListRatelimitStrategyParam ratelimitStrategyParam) {
        if (ratelimitStrategyParam.getPageIndex() == 0) {
            ratelimitStrategyParam.setPageIndex(RestConstant.DEFAULT_PAGE_INDEX);
        }
        if (ratelimitStrategyParam.getPageSize() == 0) {
            ratelimitStrategyParam.setPageSize(RestConstant.DEFAULT_PAGE_SIZE);
        }

        Wrapper<RatelimitStrategyDO> wrapper = new EntityWrapper<RatelimitStrategyDO>();
        if (StringUtils.isNotEmpty(ratelimitStrategyParam.getName())) {
            wrapper = wrapper.eq("name", ratelimitStrategyParam.getName());
        }
        Page<RatelimitStrategyDO> page = selectPage(
            new Page<RatelimitStrategyDO>(ratelimitStrategyParam.getPageIndex(), ratelimitStrategyParam.getPageSize()),
            wrapper);
        List<RatelimitStrategyDTO> results = new ArrayList<>();
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            page.getRecords().stream().forEach(u -> {
                RatelimitStrategyDTO ratelimitStrategyDTO = new RatelimitStrategyDTO();
                BeanUtils.copyProperties(u, ratelimitStrategyDTO);
                results.add(ratelimitStrategyDTO);
            });
        }
        SimplePageDTO<List<RatelimitStrategyDTO>> listRatelimitStrategyDTO =
            new SimplePageDTO<List<RatelimitStrategyDTO>>(CodeEnum.SUCCESS);
        listRatelimitStrategyDTO.setData(results);
        listRatelimitStrategyDTO.setTotal(page.getTotal());
        listRatelimitStrategyDTO.setPageIndex(page.getCurrent());
        listRatelimitStrategyDTO.setPageSize(page.getSize());
        return listRatelimitStrategyDTO;
    }

    /**
     * 更新限流策略
     * 
     * @param ratelimitStrategyParam 限流策略
     */
    public void updateRetelimitStrategy(RatelimitStrategyParam ratelimitStrategyParam) {
        RatelimitStrategyDO ratelimitStrategyDO = selectById(ratelimitStrategyParam.getId());
        BeanUtils.copyProperties(ratelimitStrategyParam, ratelimitStrategyDO);
        ratelimitStrategyDO.setLastUpdated(new Date());
        Boolean result = updateById(ratelimitStrategyDO);
        // 删除关联信息
        ratelimitStrategyDAO.deleteByRatelimitStrategyId(ratelimitStrategyParam.getId());
        // 保存关联关系
        if (StringUtils.isNotEmpty(ratelimitStrategyParam.getApiIds())) {
            String[] appids = ratelimitStrategyParam.getApiIds().split(",");
            for (String appid : appids) {
                ratelimitStrategyDAO.insertRatelimitIdAndApiId(ratelimitStrategyParam.getId(), Long.valueOf(appid));
            }
        }
    }



    /**
     * 根据限流策略Id查询绑定的后台服务IP
     *
     * @param ratelimitStrategyId 限流策略ID
     * @return
     */
    public List<Long> getListApiIdByRatelimitStrategyId(Long ratelimitStrategyId) {
        List<Long> listApiId = ratelimitStrategyDAO.getListApiIdByRatelimitStrategyId(ratelimitStrategyId);
        return listApiId;
    }
}
