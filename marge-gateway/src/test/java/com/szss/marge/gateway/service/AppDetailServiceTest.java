package com.szss.marge.gateway.service;



import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.szss.marge.gateway.GatewayApplication;
import com.szss.marge.gateway.dao.AppDetailDAO;
import com.szss.marge.gateway.model.domain.AppDetailDO;
import com.szss.marge.gateway.model.param.AppDetailParam;
import com.szss.marge.gateway.model.param.ClientParam;
import com.szss.marge.gateway.model.param.ListAppDetailParam;
import com.szss.marge.gateway.utils.AppDetailUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.stream.IntStream;

/**
 * @author XXX
 * @date 2018/7/6
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatewayApplication.class)
@TestPropertySource(properties = {"spring.profiles.active: test", "flyway.enabled: true"})
@EnableAutoConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@Slf4j
@FlywayTest
public class AppDetailServiceTest {

    /**
     * appDetailService
     */
    @Autowired
    private AppDetailService appDetailService;

    /**
     * appDetailDAO
     */
    @Autowired
    private AppDetailDAO appDetailDAO;

    /**
     * 插入
     *
     * @throws Exception 异常
     */
    @Test
    public void insertTest() throws Exception {
        String appId = "app_id_1";
        AppDetailDO appDetailDO = AppDetailUtils.fakerAppDetailDO(appId);
        AppDetailParam appDetailParam = new AppDetailParam();
        BeanUtils.copyProperties(appDetailDO, appDetailParam);
        boolean insertSuccess = appDetailService.insert(appDetailParam);
        Assertions.assertThat(insertSuccess).isEqualTo(true);

        // 删除测试数据
        appDetailService.deleteById(appId);

    }

    /**
     * 更新
     *
     * @throws Exception 异常
     */
    @Test
    public void updateTest() throws Exception {
        String appId = "app_id_2";
        AppDetailDO appDetailDO = AppDetailUtils.fakerAppDetailDO(appId);
        AppDetailParam appDetailParam = new AppDetailParam();
        BeanUtils.copyProperties(appDetailDO, appDetailParam);
        boolean insertSuccess = appDetailService.insert(appDetailParam);
        Assertions.assertThat(insertSuccess).isEqualTo(true);

        appDetailParam.setAppSecret("secret2");
        boolean updateSuccess =  appDetailService.updateSessionKey(appDetailParam);
        Assertions.assertThat(updateSuccess).isEqualTo(true);

        // 删除测试数据
        appDetailService.deleteById(appId);

    }

    /**
     * 测试分页查询
     */
    @Test
    public void listByPageTest() {
        String appId = "app_id_1";
        String sessionKey = "session_key";
        AppDetailDO appDetailDO = AppDetailUtils.fakerAppDetailDO(appId);
        AppDetailParam appDetailParam = new AppDetailParam();
        BeanUtils.copyProperties(appDetailDO, appDetailParam);

        IntStream.range(0, 15).forEach(x -> {
            appDetailParam.setAppId("app_id_" + System.currentTimeMillis() );
            boolean result = appDetailService.insert(appDetailParam);
            Assertions.assertThat(result).isEqualTo(true);
        });

        ListAppDetailParam param = new ListAppDetailParam();
        param.setPageIndex(2);
        param.setPageSize(10);
        Page<AppDetailDO> page = appDetailService.selectListPage(param);
        Assertions.assertThat(page.getTotal()).isEqualTo(15);
        Assertions.assertThat(page.getRecords().size()).isEqualTo(5);

        // 根据类型全删除测试数据
        Wrapper<AppDetailDO> wrapper = new EntityWrapper<AppDetailDO>().eq("session_key",sessionKey);
        appDetailService.delete(wrapper);
    }
}