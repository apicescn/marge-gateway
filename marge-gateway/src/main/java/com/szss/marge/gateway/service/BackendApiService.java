package com.szss.marge.gateway.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.szss.marge.common.core.constant.CodeEnum;
import com.szss.marge.common.core.model.dto.SimplePageDTO;
import com.szss.marge.gateway.dao.BackendApiDAO;
import com.szss.marge.gateway.model.domain.BackendApiDO;

/**
 * @author XXX
 * @date 2018/7/6
 */
@Service
public class BackendApiService extends ServiceImpl<BackendApiDAO, BackendApiDO> {
    /**
     * backendApiDAO
     */
    @Autowired
    private BackendApiDAO backendApiDAO;

    /**
     * 根据serviceId服务ID和path服务路径查询BackendApiDO，并新增缓存gateway.backendApi
     *
     * @param serviceId 服务ID
     * @param path 服务路径
     * @return BackendApiDO
     */
    @Cached(name = "gateway.api.", key = "#serviceId + '.' + #path", expire = 3600)
    public BackendApiDO getByServiceIdAndPath(String serviceId, String path) {
        BackendApiDO backendApiDO = new BackendApiDO();
        backendApiDO.setServiceId(serviceId);
        backendApiDO.setPath(path);
        Wrapper<BackendApiDO> wrapper = new EntityWrapper<BackendApiDO>(backendApiDO);
        return selectOne(wrapper);
    }

    /**
     * 更新是删除缓存gateway.backendApi
     *
     * @param entity
     * @return
     */
    @CacheInvalidate(name = "gateway.api.", key = "#entity.serviceId + '.' + #entity.path")
    @Override
    public boolean updateById(BackendApiDO entity) {
        return super.updateById(entity);
    }

    /**
     * 根据限流策略Id查询绑定的后台服务IP
     * 
     * @param ratelimitStrategyId 限流策略Id
     * @param pageIndex 页码
     * @param pageSize 页面大小
     * @return
     */
    public SimplePageDTO<List<BackendApiDO>> getListBackendApiDTOByRatelimitStrategyId(Long ratelimitStrategyId,
        Integer pageIndex, Integer pageSize) {
        List<BackendApiDO> listBackendApiDO =
            backendApiDAO.getByRatelimitStrategyId(ratelimitStrategyId, pageIndex, pageSize);
        Integer total = backendApiDAO.getCountByRatelimitStrategyId(ratelimitStrategyId);
        SimplePageDTO<List<BackendApiDO>> listBackendApiDTO = new SimplePageDTO(CodeEnum.SUCCESS);
        listBackendApiDTO.setData(listBackendApiDO);
        listBackendApiDTO.setTotal(total);
        listBackendApiDTO.setPageIndex(pageIndex);
        listBackendApiDTO.setPageSize(pageSize);
        return listBackendApiDTO;
    }

    /**
     * 查询所有后台服务IP
     * 
     * @return
     */
    public List<BackendApiDO> getAll() {
        EntityWrapper<BackendApiDO> eWrapper = new EntityWrapper<BackendApiDO>();
        List<BackendApiDO> backendApiDOList = selectList(eWrapper);
        return backendApiDOList;
    }
}
