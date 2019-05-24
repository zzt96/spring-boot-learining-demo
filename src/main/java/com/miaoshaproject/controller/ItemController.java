package com.miaoshaproject.controller;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.miaoshaproject.controller.viewObject.ItemVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller("/item")
@RequestMapping("/item")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class ItemController extends BaseController {


    @Autowired
    private ItemService itemService;
    //创建商品的controller
    public CommonReturnType createItem(@RequestParam(name="title") String title,
                                       @RequestParam(name="price") BigDecimal price,
                                       @RequestParam(name="itemStock") Integer itemStock,
                                       @RequestParam(name="description") String description,
                                       @RequestParam(name="imgUrl") String imgUrl) throws BusinessException {

        //封装service请求用来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setPrice(price);
        itemModel.setItemStock(itemStock);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModelForReturn = itemService.createItem(itemModel);
        ItemVO itemVo = convertItemVOFromItemModel(itemModelForReturn);

        return CommonReturnType.create(itemVo);
    }

    //Vo层是必须的
    private ItemVO convertItemVOFromItemModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        return itemVO;
    }
}
