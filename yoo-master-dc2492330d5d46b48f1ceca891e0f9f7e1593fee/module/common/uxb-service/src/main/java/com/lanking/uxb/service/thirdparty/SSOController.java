package com.lanking.uxb.service.thirdparty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.thirdparty.eduyun.Client;

/**
 * 用于拦截第三方sso请求.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月26日
 */
@RestController("thirdpartySSOController")
@RequestMapping("sso")
@RolesAllowed(anyone = true)
public class SSOController {
	@Autowired
	private Client client;

	/**
	 * 教育云登陆请求.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "/eduyun/yoomath")
	private ModelAndView ssoYoomath(HttpServletRequest request, HttpServletResponse response) {
		String url = client.getSsoUrl(Product.YOOMATH, "");
		return new ModelAndView("redirect:" + url);
	}
}
