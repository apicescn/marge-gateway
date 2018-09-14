package com.szss.marge.gateway.service;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.util.TextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.szss.marge.common.crypto.Md5Crypt;
import com.szss.marge.gateway.dao.AppDetailDAO;
import com.szss.marge.gateway.model.domain.AppDetailDO;
import com.szss.marge.gateway.model.param.AppDetailParam;
import com.szss.marge.gateway.model.param.ListAppDetailParam;

/**
 * @author XXX
 * @date 2018/7/4
 */
@Service
public class AppDetailService extends ServiceImpl<AppDetailDAO, AppDetailDO> {

    /**
     * 根据appId查询gateway_app_details表信息，redis缓存默认时间为24小时
     *
     * @param appId appId
     * @return AppDetailsDO
     */
    @Cached(name = "gateway.appDetails.", key = "#appId", expire = 3600)
    @Override
    public AppDetailDO selectById(Serializable appId) {
        return super.selectById(appId);
    }

    /**
     * 更加appId修改gateway_app_details表信息，并对redis缓存数据进行更新
     *
     * @param appDetails
     * @return
     */
    @CacheInvalidate(name = "gateway.appDetails.", key = "#appDetails.appId")
    @Override
    public boolean updateById(AppDetailDO appDetails) {
        return super.updateById(appDetails);
    }

    /**
     * 删除数据
     *
     * @param appId appId
     * @return 返回影响记录条数
     */
    @CacheInvalidate(name = "gateway.appDetails.", key = "#appId")
    @Override
    public boolean deleteById(Serializable appId) {
        return super.deleteById(appId);
    }

    /**
     * 分页查询
     *
     * @param listAppDetailParam 查询参数
     * @return Page<AppDetailDO> 分页数据
     */
    public Page<AppDetailDO> selectListPage(ListAppDetailParam listAppDetailParam) {
        AppDetailDO appDetailDO = new AppDetailDO();
        BeanUtils.copyProperties(listAppDetailParam, appDetailDO);
        Wrapper<AppDetailDO> wrapper = new EntityWrapper<AppDetailDO>();
        wrapper
            = wrapper.like(!TextUtils.isEmpty(listAppDetailParam.getAppId()), "app_id", listAppDetailParam.getAppId());

        Page<AppDetailDO> page = selectPage(
            new Page<AppDetailDO>(listAppDetailParam.getPageIndex(), listAppDetailParam.getPageSize()), wrapper);
        return page;
    }

    /**
     * 新增appDetail信息
     *
     * @param appDetailParam appDetail信息
     * @return 是否成功
     */
    public boolean insert(AppDetailParam appDetailParam) {
        AppDetailDO appDetailDO = new AppDetailDO();
        BeanUtils.copyProperties(appDetailParam, appDetailDO);
        appDetailDO.setDeleted(false);
        appDetailDO.setDateCreated(new Date());
        appDetailDO.setLastUpdated(new Date());
        return insert(appDetailDO);
    }

    /**
     * 更新appDetail信息
     *
     * @param appDetailParam appDetail信息
     * @return 是否成功
     */
    public boolean update(AppDetailParam appDetailParam) {
        AppDetailDO appDetailDO = selectById(appDetailParam.getAppId());
        BeanUtils.copyProperties(appDetailParam, appDetailDO);
        appDetailDO.setLastUpdated(new Date());
        if (appDetailParam.getExpiresIn() != null && appDetailParam.getExpiresIn() > 0L) {
            Date expireDate = new Date(System.currentTimeMillis() + appDetailParam.getExpiresIn());
            appDetailDO.setExpiredDate(expireDate);
        }
        return updateById(appDetailDO);
    }

    /**
     * 更新sessionKey信息再更新appDetail信息
     *
     * @param appDetailParam appDetail信息
     * @return 是否成功
     */
    public boolean updateSessionKey(AppDetailParam appDetailParam) {
        // 生成sessionKey这个步骤放在service层中因为这个System.currentTimeMillis()不方便mock
        String clientId = appDetailParam.getAppId();
        String clientSecret = appDetailParam.getAppSecret();
        String auth = clientId + ":" + clientSecret + System.currentTimeMillis();
        String basicAuth = "Basic " + Base64.encodeBase64String(auth.getBytes());
        String sessionKey = Md5Crypt.encode(basicAuth);
        appDetailParam.setSessionKey(sessionKey);

        return update(appDetailParam);
    }

}
