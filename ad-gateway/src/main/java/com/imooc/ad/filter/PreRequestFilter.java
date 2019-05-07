/**
 * Copyright (c) 2019,sunnybs. 
 * All Rights Reserved.
 * 
 * Project Name:ad-gateway
 * Package Name:com.imooc.ad.filter
 * File Name:PreRequestFilter.java
 * Date:2019年5月3日 下午3:47:56
 * 
 */
package com.imooc.ad.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.slf4j.Slf4j;
/**
 * ClassName: PreRequestFilter <br/>
 * Description: TODO <br/>
 * Date: 2019年5月3日 下午3:47:56 <br/>
 * <br/>
 * 
 * @author jiang(邮箱)
 * 
 *         修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */
@Slf4j
@Component
public class PreRequestFilter extends ZuulFilter {

    public boolean shouldFilter() {
        return true;
    }

    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("startTime", System.currentTimeMillis());
        return null;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

}
