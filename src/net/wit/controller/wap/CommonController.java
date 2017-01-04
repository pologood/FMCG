/**
 * @Title：CommonController.java 
 * @Package：net.wit.controller.wap 
 * @Description：
 * @author：Chenlf
 * @date：2015年2月11日 下午9:36:08 
 * @version：V1.0   
 */

package net.wit.controller.wap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName：CommonController
 * @Description：
 * @author：Chenlf
 * @date：2015年2月11日 下午9:36:08
 */
@RequestMapping(value = "/wap/common")
@Controller("wapCommonController")
public class CommonController extends BaseController {

	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String error() {
		return ERROR_VIEW;
	}
}
