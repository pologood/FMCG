package net.wit.controller.app.model;

import net.wit.entity.ProductChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thwapp on 2016/2/22.
 */
public class ProductChannelModel extends  BaseModel {
    /* 编号*/
    private Long id;
    /* 名称*/
    private String name;
    /* 描述*/
    private String description;
    /* 图片*/
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void copyFrom(ProductChannel productChannel){
        this.id = productChannel.getId();
        this.name = productChannel.getName();
        this.description = productChannel.getDescription();
        this.image = productChannel.getImage();

    }

    public static List<ProductChannelModel> bindData(List<ProductChannel> productChannels){
        List<ProductChannelModel> productChannelModels = new ArrayList<ProductChannelModel>();
        for(ProductChannel productChannel:productChannels){
            ProductChannelModel productChannelModel = new ProductChannelModel();
            productChannelModel.copyFrom(productChannel);
            productChannelModels.add(productChannelModel);
        }
        return productChannelModels;
    }
}
