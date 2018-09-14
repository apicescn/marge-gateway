package com.szss.marge.gateway.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.szss.marge.gateway.model.domain.BackendApiDO;

/**
 * @author XXX
 * @date 2018/7/6
 */
@Mapper
public interface BackendApiDAO extends BaseMapper<BackendApiDO> {
    /**
     * 根据策略ID查询API
     * 
     * @param strategyId 策略ID
     * @return List<BackendApiDO>
     */
    @Select("SELECT ba.id as id,ba.service_id as serviceId,ba.path,ba.signature,ba.token,ba.deleted "
        + "from gateway_ip_api_control ac,gateway_backend_api ba "
        + "WHERE ac.api_id=ba.id and ac.deleted=0 and ba.deleted=0 where ac.strategy_id=#{strategyId}")
    List<BackendApiDO> getByStrategyId(@Param("strategyId") Long strategyId);

    /**
     * 根据策略ID查询API
     *
     * @param ratelimitStrategyId 策略ID
     * @param pageIndex 第几页
     * @param pageSize 页面大小
     * @return List<BackendApiDO>
     */
    @Select("SELECT w1.* FROM gateway_backend_api w1, (   "
        + "  SELECT TOP (#{pageSize}) api_id FROM(   "
        + "    SELECT TOP ((#{pageIndex}+1)*#{pageSize}) api_id, strategy_id FROM gateway_ratelimit_api_control where strategy_id=#{ratelimitStrategyId} ORDER BY api_id DESC ) w ORDER BY w.api_id ASC "
        + ") w2 WHERE w1.id = w2.api_id ")
    List<BackendApiDO> getByRatelimitStrategyId(@Param("ratelimitStrategyId") Long ratelimitStrategyId,
        @Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

    /**
     * 根据策略ID查询API数量
     *
     * @param ratelimitStrategyId 策略ID
     * @return List<BackendApiDO>
     */
    @Select("SELECT  count(api_id)   FROM gateway_ratelimit_api_control where strategy_id=#{ratelimitStrategyId}")
    Integer getCountByRatelimitStrategyId(@Param("ratelimitStrategyId") Long ratelimitStrategyId);
}
