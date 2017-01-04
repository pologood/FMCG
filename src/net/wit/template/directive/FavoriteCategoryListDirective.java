package net.wit.template.directive;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.wit.entity.Member;
import net.wit.service.MemberService;
import net.wit.util.FreemarkerUtils;

@Component("favoriteCategoryListDirective")
public class FavoriteCategoryListDirective extends BaseDirective{

	/** 变量名称 */
	private static final String VARIABLE_NAME = "favoriteCategories";
	
	/** "标签ID"参数名称 */
	private static final String TAG_ID_PARAMETER_NAME = "favoriteCategoryTagId";
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long productCategoryTagId = FreemarkerUtils.getParameter(TAG_ID_PARAMETER_NAME, Long.class, params);
		String cacheRegion = getCacheRegion(env, params);
		Member member = memberService.getCurrent();
		Integer count = getCount(params);
		//Tag tag = tagService.find(productCategoryTagId);
		//Long tenantId = FreemarkerUtils.getParameter(TENANT_ID_PARAMETER_NAME, Long.class, params);
		//Long productChannelId = FreemarkerUtils.getParameter(PRODUCTCHANNEL_ID_PARAMETER_NAME, Long.class, params);
		//Tenant tenant = tenantService.find(tenantId);
		//ProductChannel channel = productChannelService.find(productChannelId);
//		if (channel != null) {
//			List<ProductCategory> productCategories;
//			boolean useCache = useCache(env, params);
//			String cacheRegion = getCacheRegion(env, params);
//			Integer count = getCount(params);
//			if (useCache) {
//				productCategories = productCategoryService.findRootsByChannel(channel, tag, count, cacheRegion);
//			} else {
//				productCategories = productCategoryService.findRootsByChannel(channel, tag, count);
//			}
//			setLocalVariable(VARIABLE_NAME, productCategories, env, body);
//		}
		setLocalVariable(VARIABLE_NAME, null, env, body);
	}
}
