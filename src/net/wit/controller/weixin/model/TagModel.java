package net.wit.controller.weixin.model;

import net.wit.entity.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagModel extends BaseModel {
    /*ID*/
    private Long id;
    /*标签名称*/
    private String name;

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void copyFrom(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }

    public static List<TagModel> bindData(List<Tag> tags) {
        List<TagModel> models = new ArrayList<>();
        for (Tag tag : tags) {
            TagModel model = new TagModel();
            model.copyFrom(tag);
            models.add(model);
        }
        return models;
    }

}
