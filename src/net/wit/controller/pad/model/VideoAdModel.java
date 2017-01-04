package net.wit.controller.pad.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.Qrcode;
import net.wit.entity.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/9.
 */
public class VideoAdModel extends BaseModel {
    /*ID*/
    private Long id;

    /*ID*/
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public  void copyFrom(Video q) {
        this.id = q.getId();
        this.url = q.getUrl();
    }

    public static List<VideoAdModel> bindData(List<Video> videos) {
        List<VideoAdModel> models = new ArrayList<VideoAdModel>();
        for (Video video:videos) {
            VideoAdModel model = new VideoAdModel();
            model.copyFrom(video);
            models.add(model);
        }
        return models;
    }


}
