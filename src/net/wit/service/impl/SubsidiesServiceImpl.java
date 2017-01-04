package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.dao.AccountDao;
import net.wit.dao.DepositDao;
import net.wit.dao.MemberDao;
import net.wit.dao.SubsidiesDao;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.util.SettingUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import static net.wit.FileInfo.FileType.file;

@Service("subsidiesServiceImpl")
public class SubsidiesServiceImpl extends BaseServiceImpl<Subsidies, Long> implements SubsidiesService {
	@Resource(name="subsidiesDaoImpl")
	private SubsidiesDao subsidiesDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "subsidiesItemServiceImpl")
	private SubsidiesItemService subsidiesItemService;

	@Resource(name = "subsidiesDaoImpl")
	public void setBaseDao(SubsidiesDao subsidiesDao) {
		super.setBaseDao(subsidiesDao);
	}

	public List<Map<String,String>> readExl(MultipartFile file){
		List<Map<String,String>> lists=new ArrayList<Map<String,String>>();
		try {
			InputStream is=file.getInputStream();
			HSSFWorkbook wb = new HSSFWorkbook(is);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row;
			HSSFCell col;
			DecimalFormat df = new DecimalFormat("#");
			DecimalFormat dft = new DecimalFormat("0.00");
			for(int i=1;i<=sheet.getLastRowNum();i++){
				row = sheet.getRow(i);
				if(row==null){
					break;
				}
				Map<String,String> map=new TreeMap<String,String>();
				List<String> ls=new ArrayList<>();
				for(int j=0;j<=row.getLastCellNum();j++){
					col=row.getCell(j);
					if(col==null){
						break;
					}
					if(col.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
						if(j==5){
							map.put("obj"+j,dft.format(col.getNumericCellValue()));
						}else{
							map.put("obj"+j,df.format(col.getNumericCellValue()));
						}
					}else if(col.getCellType()==HSSFCell.CELL_TYPE_BLANK){
						map.put("obj"+j,"--");
					}else{
						map.put("obj"+j,col.getStringCellValue());
					}
				}
				lists.add(map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lists;
	}

	public Map<String,Object> recharge(Subsidies subsidies){
		subsidiesDao.lock(subsidies,LockModeType.PESSIMISTIC_WRITE);
		Map<String,Object> map=new HashMap<String,Object>();
		Integer sucCount=0;
		Integer errorCount=0;
		List<SubsidiesItem> subsidiesItems=subsidies.getSubsidiesItems();
		for(SubsidiesItem subsidiesItem:subsidiesItems){
			String username=subsidiesItem.getUsername();
			Member member=memberService.findByUsername(username);
			if(member!=null&&subsidiesItem.getStatus()== SubsidiesItem.Status.no){
				memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
				try {
					member.setBalance(member.getBalance().add(subsidiesItem.getAmount()));
					memberDao.merge(member);
					subsidiesItem.setStatus(SubsidiesItem.Status.yes);
					sucCount=sucCount+1;
				} catch (Exception e) {
					e.printStackTrace();
					subsidiesItem.setStatus(SubsidiesItem.Status.fail);
					errorCount=errorCount+1;
					subsidiesItem.setFailReason("充值错误");
				}
				Deposit deposit = new Deposit();
				Deposit.Type typ=null;
				if(subsidies.getType()!=null){
					if(subsidies.getType()==Subsidies.Type.recharge){
						typ= Deposit.Type.recharge;
					}else if(subsidies.getType()==Subsidies.Type.receipts){
						typ= Deposit.Type.receipts;
					}else if(subsidies.getType()==Subsidies.Type.profit){
						typ= Deposit.Type.profit;
					}else{
						typ= Deposit.Type.income;
					}
				}
				deposit.setType(typ);
				deposit.setCredit(subsidiesItem.getAmount());
				deposit.setDebit(BigDecimal.ZERO);
				deposit.setBalance(member.getBalance());
				deposit.setOperator("--");
				deposit.setMember(member);
				deposit.setMemo("平台奖励补贴");
				deposit.setOrder(null);
				deposit.setStatus(Deposit.Status.complete);
				depositDao.persist(deposit);
				Setting setting = SettingUtils.get();

				try {
					String mess=subsidies.getMessage();
					mess=mess.replace("name",subsidiesItem.getUsername()).replace("money",subsidiesItem.getAmount().toString());
					Message mes = EntitySupport.createInitMessage(Message.Type.account,mess,null, member, null);
					mes.setWay(Message.Way.all);
					messageService.save(mes);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				if(member==null){
					subsidiesItem.setStatus(SubsidiesItem.Status.fail);
					errorCount=errorCount+1;
					subsidiesItem.setFailReason("用户不存在");
				}

			}
			subsidiesItemService.update(subsidiesItem);
		}
		map.put("successCount",sucCount);
		map.put("errorCount",errorCount);
		return map;
	}

}
