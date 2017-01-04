package net.wit.dao;

import net.wit.entity.Qrcode;
import net.wit.entity.Tenant;
import net.wit.entity.Video;

import java.util.List;

/**
 * Created by Administrator on 2016/11/9.
 */
public interface VideoAdDao extends BaseDao<Video, Long> {

   List<Video> findList(Tenant tenant);
}
