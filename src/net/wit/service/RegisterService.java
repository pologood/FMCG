/**
 *====================================================
 * 文件名称: RegisterService.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年7月10日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * @ClassName: RegisterService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author Administrator 
 * @date 2014年7月10日 上午9:20:51  
 */
public interface RegisterService {
	
	public Map<String, Object> index(HttpServletRequest request, HttpServletResponse response);
	
	public boolean checkUsername(String username, HttpServletRequest request);
	
	public boolean checkEmail(String email, HttpServletRequest request);
	
	public boolean checkMobile(String mobile, HttpServletRequest request);
	
	public String getCheckCode(String mobile, HttpServletRequest request);
	
	public String sendEmail(String email, HttpServletRequest request);
	
	public String submit(String captchaId, String captcha, String username, String mobile, String email, String checkCode, Long areaId, String password, HttpServletRequest request, HttpServletResponse response);

}
