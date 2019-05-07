/**
 * Copyright (c) 2019,sunnybs. 
 * All Rights Reserved.
 * 
 * Project Name:ad-gateway
 * Package Name:com.imooc.ad
 * File Name:ZuulGatewayApplication.java
 * Date:2019年5月3日 下午3:39:10
 * 
 */
package com.imooc.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * ClassName: ZuulGatewayApplication <br/>
 * Description: TODO <br/>
 * Date: 2019年5月3日 下午3:39:10 <br/>
 * <br/>
 * 
 * @author jiang(邮箱)
 * 
 *         修改记录
 * @version 产品版本信息 yyyy-mm-dd 姓名(邮箱) 修改信息<br/>
 * 
 */
@EnableZuulProxy
@SpringCloudApplication
public class ZuulGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayApplication.class, args);
    }
}
