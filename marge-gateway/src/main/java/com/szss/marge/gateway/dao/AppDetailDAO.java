package com.szss.marge.gateway.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.szss.marge.gateway.model.domain.AppDetailDO;

/**
 * AppDetailsDO实体数据操作对象
 *
 * @author XXX
 * @date 2018/7/4
 */
@Mapper
public interface AppDetailDAO extends BaseMapper<AppDetailDO> {

}
