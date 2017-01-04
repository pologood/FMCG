package net.wit.controller.pad.model;

import net.wit.entity.Union;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/10.
 */
public class UnionModel {
    /*ID*/
    private Long id;

    /*name*/
    private String name;

    /*name*/
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public  void copyFrom(Union q) {
        this.id = q.getId();
        this.image = q.getImage();
        this.name = q.getName();
    }

    public static List<UnionModel> bindData(List<Union> us) {
        List<UnionModel> models = new ArrayList<UnionModel>();
        for (Union u:us) {
            UnionModel model = new UnionModel();
            model.copyFrom(u);
            models.add(model);
        }
        return models;
    }
}
