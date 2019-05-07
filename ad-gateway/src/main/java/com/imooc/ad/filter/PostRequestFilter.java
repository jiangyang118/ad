/**
 * Copyright (c) 2019,sunnybs. 
 * All Rights Reserved.
 * 
 * Project Name:ad-gateway
 * Package Name:com.imooc.ad.filter
 * File Name:PostRequestFilter.java
 * Date:2019年5月3日 下午3:51:48
 * 
 */
package com.imooc.ad.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName: PostRequestFilter <br/>
 * Description: TODO <br/>
 * Date: 2019年5月3日 下午3:51:48 <br/>
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
public class PostRequestFilter extends ZuulFilter {

    public boolean shouldFilter() {
        return true;
    }

    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        Long startTime = (Long) ctx.get("startTime");
        String url = request.getRequestURI();
        long dur = System.currentTimeMillis() - startTime;
        log.info("uri: " + url + " , duration:" + dur);
        return null;
    }

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }

}
