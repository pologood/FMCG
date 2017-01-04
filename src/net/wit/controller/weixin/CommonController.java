/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin;

import net.wit.controller.weixin.model.DataBlock;
import net.wit.service.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller - 共用
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinCommonController")
@RequestMapping("weixin/common")
public class CommonController {

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    /**
     * 公钥
     */
    @RequestMapping(value = "/public_key", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock publicGetKey(HttpServletRequest request) {
        RSAPublicKey publicKey = rsaService.generateKey(request);
        Map<String, String> data = new HashMap<>();
        data.put("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
        data.put("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
        return DataBlock.success(data,"执行成功");
    }


}