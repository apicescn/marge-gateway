package com.szss.marge.gateway.dao;

import com.szss.marge.gateway.model.domain.CidrDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.szss.marge.gateway.model.domain.IpStrategyDO;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * @author XXX
 * @date 2018/7/9
 */
@Mapper
public interface IpStrategyDAO extends BaseMapper<IpStrategyDO> {

    /**
     *
     * 根据appId、serviceId和path查询CIDR，并且后加配置优先匹配
     *
     * @param appId 应用ID
     * @param serviceId 服务ID
     * @param path 服务的请求路径
     * @return List<CidrDO>
     */
    @SelectProvider(type = IpStrategySqlProvider.class, method = "sql")
    List<CidrDO> listCidrByAppIdAndServiceIdAndPath(@Param("appId") String appId, @Param("serviceId") String serviceId,
        @Param("path") String path);

}
