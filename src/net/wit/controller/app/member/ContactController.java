package net.wit.controller.app.member;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Filter.Operator;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.ContactListModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.MemberListModel;
import net.wit.controller.app.model.MemberModel;
import net.wit.controller.app.model.TenantListModel;
import net.wit.entity.Contact.Type;
import net.wit.weixin.main.MenuManager;

/**
 * 社交圈控制层
 * 
 * @author thwapp
 *
 */
@Controller("appMemberContactController")
@RequestMapping("/app/member/contact")
public class ContactController extends BaseController {
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "contactServiceImpl")
	private ContactService contactService;

	@Resource(name = "contactProductServiceImpl")
	private ContactProductService contactProductService;

	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;

	@Resource(name = "consumerServiceImpl")
	private ConsumerService consumerService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;
	/**
	 * 列表 type=index  首页    type=discover 发现  
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long id,String type, Integer pageNumber, Integer pageSize) {
		Map<String, Object> data = new HashMap<String, Object>();
		Long[] ids = {4L,5L,6L,7L,12L,17L,20L,21L,32L};
		Pageable pageable = new Pageable();
		pageable.setPageNumber(pageNumber);
		pageable.setPageSize(pageSize);

		List<Tag> tags = tagService.findList(ids);
		List<Long> tenants= new ArrayList<Long>();
		Area area =areaService.getCurrent();
		Member member = memberService.find(id);
		List<Tenant> tenantPage=tenantService.openList(null,area,null,tags,null,null,null,null, Tenant.OrderType.hitsDesc);

		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Page<Contact> page = null;
		if("index".equals(type)){
			for(Tenant tenant:tenantPage){
				tenants.add(tenant.getId());
			}
			page =  contactService.findPage(null, null,null,true,null,tenants, pageable);
		}else if("discover".equals(type)){
			page =  contactService.findPage(null, null,null,true,area,null, pageable);
		}

		data.put("contact", ContactListModel.bindData(page.getContent()));
		return DataBlock.success(data, "执行成功");
	}

	/**
	 * 列表 type=topic  我的话题    type=camera 我的魔拍秀  
	 */
	@RequestMapping(value = "/my/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock myList(Long id,String type, Integer pageNumber, Integer pageSize) {
		Map<String, Object> data = new HashMap<String, Object>();
		Member owner = memberService.getCurrent();
		Tenant ownertenant = owner.getTenant();
		if (ownertenant == null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		List<Filter> filters = new ArrayList<Filter>();
		Filter filter = new Filter("tenant", Operator.eq, ownertenant);
		filters.add(filter);
		List<Consumer> consumer = consumerService.findList(null, filters, null);
		Member member = memberService.find(id);

		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		if (consumer.contains(member)) {
			data.put("consumer", true);
		} else {
			data.put("consumer", false);
		}

		Pageable pageable = new Pageable();
		pageable.setPageNumber(pageNumber);
		pageable.setPageSize(pageSize);
		Page<Contact> page = null;
		if ("camera".equals(type)) {
			page = contactService.findPage(Type.camera, member,null,false,null,null, pageable);
		} else {
			page = contactService.findPage(null, member,null,true,null,null, pageable);
		}
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		Long concern = consumerService.count();
		Long fans = consumerService.count(new Filter("tenant", Operator.eq, tenant));
		
		TenantListModel tenantModel= new TenantListModel();
		tenantModel.copyFrom(tenant);
		
		boolean focus =  member.getFavoriteTenants().contains(tenant);
		data.put("tenant", tenantModel);
		data.put("contact", ContactListModel.bindData(page.getContent()));
		data.put("fans", fans);
		data.put("concern", concern);
		data.put("focus", focus);
		return DataBlock.success(data, "执行成功");
	}

	/**
	 * 消息列表（我发表的和我参与的所有帖子）
	 */
	@RequestMapping(value = "/message", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock message(Long id,Integer pageNumber, Integer pageSize) {
		Member member = memberService.find(id);
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Pageable pageable = new Pageable();
		pageable.setPageNumber(pageNumber);
		pageable.setPageSize(pageSize);
		Page<Contact> page = contactService.findMessage(member,pageable);
		return DataBlock.success(ContactListModel.bindMessage(page.getContent()), "执行成功");
	}
	/**
	 * 查看评论
	 */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock view(Long id) {
		Contact contact = contactService.find(id);
		if (contact == null) {
			return DataBlock.error("无效文章编号");
		}
		Long hits = contact.getHits();
		hits += 1;
		contact.setHits(hits);
		contactService.update(contact);

		TenantListModel tenantModel= new TenantListModel();
		tenantModel.copyFrom(contact.getMember().getTenant());
		
		Map<String, Object> data = new HashMap<String, Object>();
		ContactListModel model = new ContactListModel();
		model.copyAll(contact);
		data.put("contact", model);
		data.put("tenant", tenantModel);
		return DataBlock.success(data, "执行成功");
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock edit(Long id) {
		Contact contact = contactService.find(id);
		if (contact == null) {
			return DataBlock.error("无效文章编号");
		}
		Map<String, Object> data = new HashMap<String, Object>();
		ContactListModel model = new ContactListModel();
		model.copyAll(contact);
		data.put("contact", model);
		return DataBlock.success(data, "执行成功");
	}

	/**
	 * 查看关注我的人
	 */
	@RequestMapping(value = "/concernView", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock concernView(Long id) {
		Member member = memberService.find(id);
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		Tenant tenant = member.getTenant();
		if (tenant == null) {
			DataBlock.error(DataBlock.TENANT_INVAILD);
		}

		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("tenant", Operator.eq, tenant));
		List<Consumer> consumers = consumerService.findList(10, filters, null);

		Map<String, Object> data = new HashMap<String, Object>();
		MemberModel model = new MemberModel();
		if (consumers.size() == 0) {
			model.copyFrom(member);
		} else {
			Consumer consumer = consumers.get(0);
			model.copyConsumer(consumer);
		}
		data.put("member", model);
		data.put("orders", member.getOrders().size());
		data.put("amount", member.getAmount());
		return DataBlock.success(data, "执行成功");
	}

	/**
	 * 查看我的粉丝
	 */
	@RequestMapping(value = "/fansView", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock fansView(Long id, Integer pageNumber, Integer pageSize) {
		Member member = memberService.find(id);
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		Tenant tenant = member.getTenant();
		if (tenant == null) {
			DataBlock.error(DataBlock.TENANT_INVAILD);
		}

		Pageable pageable = new Pageable();
		pageable.setPageNumber(pageNumber);
		pageable.setPageSize(pageSize);
		Page<Consumer> concernPage = consumerService.findPage(tenant, null, pageable);
		return DataBlock.success(MemberListModel.bindConsumer(concernPage.getContent(), null), "执行成功");
	}

	/**
	 * 点赞
	 */
	@RequestMapping(value = "/praises", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock praises(Long id) {
		Contact contact = contactService.find(id);
		Member member = memberService.getCurrent();
		if (!contact.getPraises().contains(member)) {
			contact.getPraises().add(member);
			contactService.save(contact);
			return DataBlock.success("success", "执行成功");
		} else {
			contact.getPraises().remove(member);
			contactService.update(contact);
			return DataBlock.success("success", "error");
		}
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody DataBlock save(Contact contact, Long[] ids, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (contact == null) {
			return DataBlock.error("无效文章编号");
		}

		for (Iterator<ProductImage> iterator = contact.getImages().iterator(); iterator.hasNext();) {
			ProductImage productImage = iterator.next();
			if (productImage == null || productImage.isEmpty()) {
				iterator.remove();
				continue;
			}
		}

		for (ProductImage productImage : contact.getImages()) {
			if (productImage.getLocal() != null) {
				productImage.setLocalFile(new File(productImage.getLocal()));
				productImageService.build(productImage);
			}
		}

		List<Product> products = productService.findList(ids);
		List<ContactProduct> contactProducts = new ArrayList<ContactProduct>();
		for (Product product : products) {
			ContactProduct cp = new ContactProduct();
			cp.setSn(product.getSn());
			cp.setName(product.getName());
			cp.setFullName(product.getFullName());
			cp.setPrice(product.getPrice());
			cp.setMarketPrice(product.getMarketPrice());
			cp.setLarge(product.getLarge());
			cp.setMedium(product.getMedium());
			cp.setThumbnail(product.getThumbnail());
			//cp.setContact(contact);
			contactProducts.add(cp);
		}
		contact.setProducts(contactProducts);
		contact.setHits(0L);
		contact.setMember(member);
		contact.setIp(request.getRemoteAddr());
		contactService.save(contact);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", contact.getId());
		return DataBlock.success(data, "执行成功");
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/updateCamera", method = RequestMethod.POST)
	public @ResponseBody DataBlock updateCamera(Contact contact, String[] sns, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (contact == null) {
			return DataBlock.error("无效文章编号");
		}

		for (Iterator<ProductImage> iterator = contact.getImages().iterator(); iterator.hasNext();) {
			ProductImage productImage = iterator.next();
			if (productImage == null || productImage.isEmpty()) {
				iterator.remove();
				continue;
			}
		}

		for (ProductImage productImage : contact.getImages()) {
			if (productImage.getLocal() != null) {
				productImage.setLocalFile(new File(productImage.getLocal()));
				productImageService.build(productImage);
			}
		}

		List<ContactProduct> contactProducts = new ArrayList<ContactProduct>();
		for(String sn:sns){
			boolean snExists = productService.snExists(sn);
			if(snExists){
				Product product = productService.findBySn(sn);
				ContactProduct cp = new ContactProduct();
				cp.setSn(product.getSn());
				cp.setName(product.getName());
				cp.setFullName(product.getFullName());
				cp.setPrice(product.getPrice());
				cp.setMarketPrice(product.getMarketPrice());
				cp.setLarge(product.getLarge());
				cp.setMedium(product.getMedium());
				cp.setThumbnail(product.getThumbnail());
				//cp.setContact(contact);
				contactProducts.add(cp);
			}
		}
		contact.setProducts(contactProducts);
		contact.setHits(0L);
		contact.setMember(member);
		contact.setIp(request.getRemoteAddr());
		contactService.update(contact);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", contact.getId());
		return DataBlock.success(data, "执行成功");
	}
	
	/**
	 * 发表
	 */
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	public @ResponseBody DataBlock publish(Long id, String content, Long[] ids, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		if (!isValid(Contact.class, "content", content)) {
			return DataBlock.error("无效文章编号");
		}
		Contact relcontact = new Contact();
		List<ContactProduct> product = contactProductService.findList(ids);
		if (product.size() > 0) {
			relcontact.setProducts(product);
		}
		Contact contact = contactService.find(id);
		Member receive= contact.getMember() ;
		relcontact.setContent(content);
		relcontact.setHits(0L);
		relcontact.setMember(member);
		relcontact.setIsShow(true);
		relcontact.setType(contact.getType());
		relcontact.setIp(request.getRemoteAddr());
		relcontact.setForContact(contact);
		contactService.save(relcontact);
		Message message = EntitySupport.createInitMessage(Message.Type.contact,
				member.getUsername()+"：" + contact.getContent(), contact.getId().toString(),receive,member);
		message.setWay(Message.Way.member);
		messageService.save(message);
		return DataBlock.success("success", "执行成功");
	}

	/**
	 * 回复列表
	 */
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public @ResponseBody DataBlock manager(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Page<Contact> page = contactService.findPage(null, member,null, true,null,null, pageable);
		return DataBlock.success(ContactListModel.bindRelData(page.getContent()), "执行成功");
	}

	/**
	 * 回复
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public @ResponseBody DataBlock reply(Long id, String content, HttpServletRequest request) {
		if (!isValid(Contact.class, "content", content)) {
			return DataBlock.error("无效回复");
		}
		Contact contact = contactService.find(id);
		if (contact == null) {
			return DataBlock.error("无效编号");
		}
		Contact replyContact = new Contact();
		Member member = memberService.getCurrent();
		Member receive= contact.getMember();
		replyContact.setMember(member);
		replyContact.setHits(0L);
		replyContact.setContent(content);
		replyContact.setType(contact.getType());
		replyContact.setIp(request.getRemoteAddr());
		replyContact.setIsShow(true);
		Long hits = contact.getHits();
		hits += 1;
		replyContact.setHits(hits);
		replyContact.setForContact(contact);
		//replyContact.setIsShow(true);
		contactService.save(replyContact);
		Message message = EntitySupport.createInitMessage(Message.Type.contact,
				member.getUsername()+"：" + content, id.toString(),receive,member);
		messageService.save(message);
		message.setWay(Message.Way.member);
		return DataBlock.success("success", "成功回复");
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody DataBlock update(Contact contact, Long[] ids, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();

		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (member.getTenant() == null) {
			DataBlock.error(DataBlock.TENANT_INVAILD);
		}

		for (Iterator<ProductImage> iterator = contact.getImages().iterator(); iterator.hasNext();) {
			ProductImage productImage = iterator.next();
			if (productImage == null || productImage.isEmpty()) {
				iterator.remove();
				continue;
			}
		}
		List<ContactProduct> products = contactProductService.findList(ids);
		if (products.size() > 0) {
			contact.setProducts(products);
		}
		contact.setForContact(contact);
		contact.setHits(0L);
		contact.setMember(member);
		contact.setIp(request.getRemoteAddr());
		contactService.update(contact);
		return DataBlock.success("success", "信息更新成功");
	}

	/**
	 * 删除回复
	 */
	@RequestMapping(value = "/delete_reply", method = RequestMethod.POST)
	public @ResponseBody DataBlock deleteReply(Long id) {
		Contact contact = contactService.find(id);
		if (contact == null || contact.getForContact() == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		contactService.delete(contact);
		return DataBlock.success("success", "执行成功");
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody DataBlock delete(Long[] ids) {
		if (ids != null) {
			contactService.delete(ids);
		}
		return DataBlock.success("success", "执行成功");
	}

	/**
	 * 分享地址
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/share", method = RequestMethod.GET)
	public @ResponseBody DataBlock share(Long id, String type) {
		Contact contact = contactService.find(id);
		if (contact == null) {
			return DataBlock.error("ID不存在");
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String url = "";
		if ("weixin".equals(type)) {
			url = MenuManager.codeUrlO2(
					URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/social_circles/comment.jhtml?id=" + contact.getId().toString()
							+ "&extension=" + (member != null ? member.getUsername() : "")));
		} else if ("preview".equals(type)) {
			url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/social_circles/comment.jhtml?id=" + contact.getId().toString())) ;
		} else {
			url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/social_circles/comment.jhtml?id=" + contact.getId().toString()
					+ "&extension=" + (member != null ? member.getUsername() : "")));
		}

		List<ProductImage> images = contact.getImages();
		Map<String, String> data = new HashMap<String, String>();
		data.put("url", url);
		data.put("title","打破传统社交的限制，进入价值更高的社交圈。");
		data.put("thumbnail", images.get(0).getThumbnail()!=null?images.get(0).getThumbnail():"");
		data.put("description", contact.getContent());
		return DataBlock.success(data, "执行成功");
	}

	/**
	 * 分享地址
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/qrcode", method = RequestMethod.GET)
	public @ResponseBody DataBlock qrcode(String url,HttpServletRequest request) {
//		ByteArrayOutputStream out = QRCode.from(url).to(ImageType.PNG).stream();
//		try {
//			FileInfo.FileType fileType = FileInfo.FileType.image;
//			String originalFileName = "10.jpg";
//			File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
//			if (!tempFile.getParentFile().exists()) {
//				tempFile.getParentFile().mkdirs();
//			}
//			FileOutputStream fout = new FileOutputStream(tempFile);
//			fout.write(out.toByteArray());
//			fout.flush();
//			fout.close();
//
//			String contentType="";
//			InputStream in = request.getInputStream();
//			if (FileInfo.FileType.image.equals(fileType) ) {
//				contentType = "image/jpeg";
//			} else {
//				contentType = "image/jpeg";
//			}
//			if (!fileService.isValid(fileType,in.available(), originalFileName)) {
//				return DataBlock.error("admin.upload.invalid");
//			} else {
//				String qrcodeUrl = fileService.upload(fileType,tempFile,false, FilenameUtils.getExtension(originalFileName),contentType);
//				if (qrcodeUrl == null) {
//					return DataBlock.warn("admin.upload.error");
//				} else {
//					return DataBlock.success(qrcodeUrl, "success");
//				}
//			}
//		} catch (FileNotFoundException e) {
//			// Do Logging
//			e.printStackTrace();
//			return DataBlock.error("二维码生成失败！");
//		} catch (IOException e) {
//			// Do Logging
//			e.printStackTrace();
//			return DataBlock.error("二维码生成失败！");
//		}
		return DataBlock.error("二维码生成失败！");
	}


	/**
	 * 获取二维码
	 */
	@RequestMapping(value = "/qrcode/json", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock qrcodeJson(HttpServletRequest request, HttpServletResponse response) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// 第三方用户唯一凭证
			String url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/share/index.jhtml?extension="
					+ (member != null ? member.getUsername() : "")));
			return DataBlock.success(url, "获取成功");
		} catch (Exception e) {
			return DataBlock.error("获取二维码失败");
		}
	}
}
