/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import net.wit.Filter;
import net.wit.Template;
import net.wit.Filter.Operator;
import net.wit.dao.AreaDao;
import net.wit.dao.ArticleDao;
import net.wit.dao.BrandDao;
import net.wit.dao.NavigationDao;
import net.wit.dao.ProductCategoryDao;
import net.wit.dao.ProductDao;
import net.wit.dao.PromotionDao;
import net.wit.dao.TagDao;
import net.wit.entity.Area;
import net.wit.entity.Article;
import net.wit.entity.Authen;
import net.wit.entity.Brand;
import net.wit.entity.Product;
import net.wit.entity.Promotion;
import net.wit.entity.Tag;
import net.wit.entity.Tag.Type;
import net.wit.service.StaticService;
import net.wit.service.TemplateService;
import net.wit.util.FreemarkerUtils;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * Service - 静态化
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("staticServiceImpl")
public class StaticServiceImpl implements StaticService, ServletContextAware {

	/** Sitemap最大地址数 */
	private static final Integer SITEMAP_MAX_SIZE = 40000;

	/** servletContext */
	private ServletContext servletContext;

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Resource(name = "templateServiceImpl")
	private TemplateService templateService;
	@Resource(name = "articleDaoImpl")
	private ArticleDao articleDao;
	@Resource(name = "productDaoImpl")
	private ProductDao productDao;
	@Resource(name = "tagDaoImpl")
	private TagDao tagDao;
	@Resource(name = "brandDaoImpl")
	private BrandDao brandDao;
	@Resource(name = "areaDaoImpl")
	private AreaDao areaDao;
	@Resource(name = "promotionDaoImpl")
	private PromotionDao promotionDao;
	@Resource(name = "navigationDaoImpl")
	private NavigationDao navigationDao;
	@Resource(name = "productCategoryDaoImpl")
	private ProductCategoryDao productCategoryDao;
	
	

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Transactional(readOnly = true)
	public int build(String templatePath, String staticPath, Map<String, Object> model) {
		Assert.hasText(templatePath);
		Assert.hasText(staticPath);

		FileOutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		Writer writer = null;
		try {
			freemarker.template.Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templatePath);
			template.setEncoding("UTF-8");
			File staticFile = new File(servletContext.getRealPath(staticPath));
			File staticDirectory = staticFile.getParentFile();
			if (!staticDirectory.exists()) {
				staticDirectory.mkdirs();
			}
			fileOutputStream = new FileOutputStream(staticFile);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			writer = new BufferedWriter(outputStreamWriter);
			template.process(model, writer);
			writer.flush();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(writer);
			IOUtils.closeQuietly(outputStreamWriter);
			IOUtils.closeQuietly(fileOutputStream);
		}
		return 0;
	}

	@Transactional(readOnly = true)
	public int build(String templatePath, String staticPath) {
		return build(templatePath, staticPath, null);
	}

	@Transactional(readOnly = true)
	public int build(Article article) {
		/**
		Assert.notNull(article);

		delete(article);
		Template template = templateService.get("articleContent");
		int buildCount = 0;
		if (article.getIsPublication()) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("article", article);
			for (int pageNumber = 1; pageNumber <= article.getTotalPages(); pageNumber++) {
				article.setPageNumber(pageNumber);
				buildCount += build(template.getTemplatePath(), article.getPath(), model);
			}
			article.setPageNumber(null);
		}
		return buildCount;
		*/
		return 0;
	}
	
	@Transactional(readOnly = true)
	public int build(Product product) {
		/**
		Assert.notNull(product);

		delete(product);
		Template template = templateService.get("productContent");
		int buildCount = 0;
		if (product.getIsMarketable()) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("product", product);
			buildCount += build(template.getTemplatePath(), product.getPath(), model);
		}
		
		return buildCount;
		*/
		return 0;
	}
	
	@Transactional(readOnly = true)
	public int build(Authen authen) {
		/**
		Assert.notNull(authen);

		delete(authen);
		Template template = templateService.get("productContent");
		int buildCount = 0;
		if (product.getIsMarketable()) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("authen", authen);
			buildCount += build(template.getTemplatePath(), product.getPath(), model);
		}
		
		return buildCount;
		*/
		return 0;
	}

	@Transactional(readOnly = true)
	public int buildIndex() {
		Template index = templateService.get("index");
	    build(index.getTemplatePath(), index.getStaticPath());
		//Template shoping = templateService.get("shoping");
		//return build(shoping.getTemplatePath(), shoping.getStaticPath());
		return 0;
	}

	@Transactional(readOnly = true)
	public int buildSitemap() {
		int buildCount = 0;
		/**
		Template sitemapIndexTemplate = templateService.get("sitemapIndex");
		Template sitemapTemplate = templateService.get("sitemap");
		Map<String, Object> model = new HashMap<String, Object>();
		List<String> staticPaths = new ArrayList<String>();
		for (int step = 0, index = 0, first = 0, count = SITEMAP_MAX_SIZE;;) {
			try {
				model.put("index", index);
				String templatePath = sitemapTemplate.getTemplatePath();
				String staticPath = FreemarkerUtils.process(sitemapTemplate.getStaticPath(), model);
				if (step == 0) {
					List<Article> articles = articleDao.findList(first, count, null, null);
					model.put("articles", articles);
					if (articles.size() < count) {
						step++;
						first = 0;
						count -= articles.size();
					} else {
						buildCount += build(templatePath, staticPath, model);
						articleDao.clear();
						articleDao.flush();
						staticPaths.add(staticPath);
						model.clear();
						index++;
						first += articles.size();
						count = SITEMAP_MAX_SIZE;
					}
				} else if (step == 1) {
					List<Product> products = productDao.findList(first, count, null, null);
					model.put("products", products);
					if (products.size() < count) {
						step++;
						first = 0;
						count -= products.size();
					} else {
						buildCount += build(templatePath, staticPath, model);
						productDao.clear();
						productDao.flush();
						staticPaths.add(staticPath);
						model.clear();
						index++;
						first += products.size();
						count = SITEMAP_MAX_SIZE;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		**/
		return buildCount;
	}

	@Transactional(readOnly = true)
	public int buildOther() {
		int buildCount = 0;
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("total", productDao.count());
		model.put("tags", tagDao.findList(Tag.Type.brand));
	    String p="\"热门\"";
	    List<Brand> brands = brandDao.findList(null, null, null, null);
	    String zm = "abcdefghijklmnopqrstuvwxyz";
	    for (char c:zm.toCharArray()) {
			if (Brand.at(brands, c) ) {
			   p = p +",\""+String.valueOf(c)+"\"";
			}
	    }
	   
		model.put("brandPhonetic","["+ p+"]");
		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter("code", Operator.like, "%00"));
	    List<Area> areas = areaDao.findList(null, null, filters, null);
		model.put("areas",areas);
		Template adminCommonJsTemplate = templateService.get("adminCommonJs");
		//Template b2cCommonJsTemplate = templateService.get("b2cCommonJs");
		Template b2bCommonJsTemplate = templateService.get("b2bCommonJs");
		Template mobileCommonJsTemplate = templateService.get("mobileCommonJs");
		//Template boxCommonJsTemplate = templateService.get("boxCommonJs");
//		Template weixinCommonJsTemplate = templateService.get("weixinCommonJs");
		Template wapCommonJsTemplate = templateService.get("wapCommonJs");
		Template dataTemplate = templateService.get("data");
		Template categoryTemplate = templateService.get("category");
		Template categoryJsonTemplate = templateService.get("categoryJson");
		Template tenantCategoryTemplate = templateService.get("tenantCategory");
		Template tenantCategoryJsonTemplate = templateService.get("tenantCategoryJson");
		Template areaTemplate = templateService.get("area");
		Template areaJsonTemplate = templateService.get("areaJson");
		Template cityTemplate = templateService.get("city");
		Template appdataTemplate = templateService.get("appdata");
		buildCount += build(dataTemplate.getTemplatePath(), dataTemplate.getStaticPath(), model);
		buildCount += build(categoryTemplate.getTemplatePath(), categoryTemplate.getStaticPath(), model);
		buildCount += build(categoryJsonTemplate.getTemplatePath(), categoryJsonTemplate.getStaticPath(), model);
		buildCount += build(tenantCategoryTemplate.getTemplatePath(), tenantCategoryTemplate.getStaticPath(), model);
		buildCount += build(tenantCategoryJsonTemplate.getTemplatePath(), tenantCategoryJsonTemplate.getStaticPath(), model);
		buildCount += build(areaTemplate.getTemplatePath(), areaTemplate.getStaticPath(), model);
		buildCount += build(areaJsonTemplate.getTemplatePath(), areaJsonTemplate.getStaticPath(), model);
		buildCount += build(cityTemplate.getTemplatePath(), cityTemplate.getStaticPath(), model);
		buildCount += build(appdataTemplate.getTemplatePath(), appdataTemplate.getStaticPath(), model);
		//buildCount += build(boxCommonJsTemplate.getTemplatePath(), boxCommonJsTemplate.getStaticPath());
		buildCount += build(adminCommonJsTemplate.getTemplatePath(), adminCommonJsTemplate.getStaticPath());
		//buildCount += build(b2cCommonJsTemplate.getTemplatePath(), b2cCommonJsTemplate.getStaticPath());
		buildCount += build(b2bCommonJsTemplate.getTemplatePath(), b2bCommonJsTemplate.getStaticPath());
		buildCount += build(mobileCommonJsTemplate.getTemplatePath(), mobileCommonJsTemplate.getStaticPath());
//		buildCount += build(weixinCommonJsTemplate.getTemplatePath(), weixinCommonJsTemplate.getStaticPath());
		buildCount += build(wapCommonJsTemplate.getTemplatePath(), wapCommonJsTemplate.getStaticPath());
		return buildCount;
	}

	@Transactional(readOnly = true)
	public int buildAll() {
		int buildCount = 0;
		for (int i = 0; i < articleDao.count(); i += 20) {
			List<Article> articles = articleDao.findList(i, 20, null, null);
			for (Article article : articles) {
				buildCount += build(article);
			}
			articleDao.clear();
		}
		for (int i = 0; i < productDao.count(); i += 20) {
			List<Product> products = productDao.findList(i, 20, null, null);
			for (Product product : products) {
				buildCount += build(product);
			}
			productDao.clear();
		}
		buildIndex();
		buildSitemap();
		buildOther();
		return buildCount;
	}

	@Transactional(readOnly = true)
	public int delete(String staticPath) {
		Assert.hasText(staticPath);

		File staticFile = new File(servletContext.getRealPath(staticPath));
		if (staticFile.exists()) {
			staticFile.delete();
			return 1;
		}
		return 0;
	}

	@Transactional(readOnly = true)
	public int delete(Article article) {
		Assert.notNull(article);

		int deleteCount = 0;
		for (int pageNumber = 1; pageNumber <= article.getTotalPages() + 1000; pageNumber++) {
			article.setPageNumber(pageNumber);
			int count = delete(article.getPath());
			if (count < 1) {
				break;
			}
			deleteCount += count;
		}
		article.setPageNumber(null);
		return deleteCount;
	}
	@Transactional(readOnly = true)
	public int delete(Product product) {
		Assert.notNull(product);

		return delete(product.getPath());
	}

	@Transactional(readOnly = true)
	public int deleteIndex() {
		Template template = templateService.get("index");
		return delete(template.getStaticPath());
	}

	@Transactional(readOnly = true)
	public int deleteOther() {
		int deleteCount = 0;
		Template shopCommonJsTemplate = templateService.get("shopCommonJs");
		Template adminCommonJsTemplate = templateService.get("adminCommonJs");
		deleteCount += delete(shopCommonJsTemplate.getStaticPath());
		deleteCount += delete(adminCommonJsTemplate.getStaticPath());
		return deleteCount;
	}

}