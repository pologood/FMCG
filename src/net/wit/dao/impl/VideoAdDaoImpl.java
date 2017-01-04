package net.wit.dao.impl;

import net.wit.dao.VideoAdDao;
import net.wit.entity.Qrcode;
import net.wit.entity.Tenant;
import net.wit.entity.Video;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Administrator on 2016/11/9.
 */
@Repository("videoAdDaoImpl")
public class VideoAdDaoImpl extends BaseDaoImpl<Video, Long> implements VideoAdDao {

    public List<Video> findList(Tenant tenant) {
        if (tenant == null) {
            return null;
        }
        String jpql = "select video FROM Video video where video.tenant = :tenant";
        try {
            TypedQuery<Video> query = entityManager.createQuery(jpql, Video.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant);
            if(query.getResultList().size()==0){
                //播放平台视频
            }
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
