package net.wit.service.impl;

import net.wit.dao.VideoAdDao;
import net.wit.entity.Qrcode;
import net.wit.entity.Tenant;
import net.wit.entity.Video;
import net.wit.service.VideoAdService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016/11/9.
 */
@Service("videoAdServiceImpl")
public class VideoAdServiceImpl extends BaseServiceImpl<Video, Long> implements VideoAdService {
    @Resource(name = "videoAdDaoImpl")
    private VideoAdDao videoAdDao;
    @Transactional(readOnly = true)
    public List<Video> findList(Tenant tenant){
        return videoAdDao.findList(tenant);
    }
}
