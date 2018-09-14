package com.szss.marge.gateway.filter;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.monitoring.MonitoringHelper;

/**
 * @author XXX
 * @date 2018/7/20
 */
@RunWith(MockitoJUnitRunner.class)
public class SignRequestFilterTest {

    private SignRequestFilter signRequestFilter;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        MonitoringHelper.initMocks();
    }

    @Test
    public void testSort() {
        signRequestFilter.shouldFilter();
    }

}
