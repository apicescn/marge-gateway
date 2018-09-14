package com.szss.marge.gateway.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.szss.marge.common.core.model.dto.SimpleDTO;
import com.szss.marge.common.core.model.dto.SimplePageDTO;
import com.szss.marge.gateway.model.domain.AppDetailDO;
import com.szss.marge.gateway.model.dto.AppDetailDTO;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.springframework.beans.BeanUtils;
import com.szss.marge.common.core.constant.CodeEnum;

/**
 * 工具类
 * 
 * @author XXX
 * @date 2018/7/6
 */
public class AppDetailUtils {

    /**
     * COUNT_SECRET
     */
    private static final int COUNT_SECRET = 6;

    /**
     * 模拟AppDetailDO数据
     *
     * @param appId appId
     * @return AppDetailDO
     */
    public static AppDetailDO fakerAppDetailDO(String appId) {

        AppDetailDO appDetailDO = new AppDetailDO();
        appDetailDO.setAppId(appId);
        appDetailDO.setAppSecret("app_secret");
        appDetailDO.setSessionKey("session_key");
        appDetailDO.setToken("token");
        appDetailDO.setDateCreated(new Date());
        appDetailDO.setLastUpdated(new Date());
        appDetailDO.setExpiredDate(new Date());
        return appDetailDO;
    }

    /**
     * 根据AppDetailDO数据模拟SimpleDTO<AppDetailDTO>
     *
     * @param appDetailDO AppDetailDO数据
     * @return SimpleDTO<AppDetailDTO>数据
     */
    public static SimpleDTO<AppDetailDTO> fakerGetAppDetailDTO(AppDetailDO appDetailDO) {
        SimpleDTO<AppDetailDTO> simpleDTO = new SimpleDTO<AppDetailDTO>(CodeEnum.SUCCESS);
        AppDetailDTO appDetailDTO = new AppDetailDTO();
        BeanUtils.copyProperties(appDetailDO, appDetailDTO);
        simpleDTO.setData(appDetailDTO);
        return simpleDTO;
    }

    /**
     * 模拟获取AppDetailDO列表
     *
     * @param count 列表大小
     * @return AppDetailDO列表
     */
    public static List<AppDetailDO> fakerAppDetailDOList(int count) {
        List<AppDetailDO> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add(fakerAppDetailDO("app_id_"+i));
        }
        return list;
    }

    /**
     * 根据AppDetailDTO数据模拟SimplePageDTO<List<AppDetailDTO>>数据
     *
     * @param appDetailDOList AppDetailDTO数据
     * @return SimplePageDTO<List<AppDetailDTO>>数据
     */
    public static SimplePageDTO<List<AppDetailDTO>> fakerListAppDetailDTO(List<AppDetailDO> appDetailDOList) {
        Assert.assertNotEquals("appDetailDOList不能为null", appDetailDOList, null);
        SimplePageDTO<List<AppDetailDTO>> simplePageDTO = new SimplePageDTO<List<AppDetailDTO>>(CodeEnum.SUCCESS);
        List<AppDetailDTO> appDetailDTOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(appDetailDOList)) {
            appDetailDOList.stream().forEach(u -> {
                AppDetailDTO appDetailDTO = new AppDetailDTO();
                BeanUtils.copyProperties(u, appDetailDTO);
                appDetailDTOList.add(appDetailDTO);
            });
        }
        simplePageDTO.setPageSize(appDetailDOList.size());
        simplePageDTO.setPageIndex(1);
        simplePageDTO.setTotal(appDetailDOList.size());
        simplePageDTO.setData(appDetailDTOList);
        return simplePageDTO;
    }
}
