package com.szss.marge.gateway.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.szss.marge.gateway.model.domain.RatelimitStrategyDO;

/**
 * @author XXX
 * @date 2018/7/18
 */
@Mapper
public interface RatelimitStrategyDAO extends BaseMapper<RatelimitStrategyDO> {
    /**
     * 根据访问API获取访问频次控制
     * 
     * @param serviceId 服务ID
     * @param path 访问路径
     * @return List<RatelimitStrategyDO>
     */
    @Select("select s.id,s.name,s.time_unit as timeUnit,s.api_ratelimit_count as apiRatelimitCount, "
        + "  s.app_ratelimit_count as appRatelimitCount from gateway_ratelimit_strategy s, "
        + " gateway_ratelimit_api_control c,gateway_backend_api a "
        + " where s.id=c.strategy_id and c.api_id=a.id and a.service_id=#{serviceId} and a.path=#{path} "
        + " and s.deleted=0 and c.deleted=0 and a.deleted=0 order by s.id desc")
    List<RatelimitStrategyDO> listRatelimitStrategyByServiceIdAndPath(@Param("serviceId") String serviceId,
        @Param("path") String path);

    /**
     * 根据ratelimitStrategyId删除策略与api的绑定关系
     * 
     * @param ratelimitStrategyId 策略Id
     */
    @Delete("delete from gateway_ratelimit_api_control where strategy_id=#{ratelimitStrategyId}")
    void deleteByRatelimitStrategyId(@Param("ratelimitStrategyId") Long ratelimitStrategyId);

    /**
     * 插入策略与api关系
     * 
     * @param strategyId 策略id
     * @param apiId apiId
     */
    @Insert("insert into gateway_ratelimit_api_control(strategy_id,api_id) values(#{strategyId},#{apiId})")
    void insertRatelimitIdAndApiId(@Param("strategyId") Long strategyId, @Param("apiId") Long apiId);

    /**
     * 根据限流策略Id查询绑定的后台服务IpId
     *
     * @param strategyId 限流策略ID
     * @return
     */
    @Select("select api_id from gateway_ratelimit_api_control where strategy_id=#{strategyId}")
    List<Long> getListApiIdByRatelimitStrategyId(@Param("strategyId") Long strategyId);
}
