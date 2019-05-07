/**
 * Copyright (c) 2019,sunnybs. 
 * All Rights Reserved.
 * 
 * Project Name:ad-search
 * Package Name:com.imooc.ad.controller
 * File Name:SearchController.java
 * Date:2019年5月4日 下午5:07:23
 * 
 */
package com.imooc.ad.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.annotation.IgnoreResponseAdvice;
import com.imooc.ad.client.SponsorClient;
import com.imooc.ad.client.vo.AdPlan;
import com.imooc.ad.client.vo.AdPlanGetRequest;
import com.imooc.ad.vo.CommonResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName: SearchController <br/>
 * Description: TODO <br/>
 * Date: 2019年5月4日 下午5:07:23 <br/>
 * <br/>
 * 
 * @author jiang(邮箱)
 * 
 *         修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */
@Slf4j
@RestController
public class SearchController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SponsorClient sponsorClient;

    @PostMapping("/getAdPlans")
    @IgnoreResponseAdvice
    public CommonResponse<List<AdPlan>> getAd(@RequestBody AdPlanGetRequest request) {
        log.info("ad-search: getAdPlansByRibbon -> {}", JSON.toJSON(request));
        return sponsorClient.getAdPlans(request);
    }

    @PostMapping("/getByRibbon")
    @IgnoreResponseAdvice
    @SuppressWarnings("unchecked")
    public CommonResponse<List<AdPlan>> getAdPlans(@RequestBody AdPlanGetRequest request) {
        log.info("ad-search: getAdPlansByRibbon -> {}", JSON.toJSON(request));
        return restTemplate
                .postForEntity("http://eurela-client-ad-sponsor/ad-sponsor/get/adPlan", request, CommonResponse.class)
                .getBody();
    }
}
