package cn.sessiontech.xcx.controller.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xbcai
 * @classname BaseCtrl
 * @description ctrl 层顶层
 * @date 2019/5/21 15:46
 */
@Slf4j
public class BaseCtrl {
    /**
     * 默认从0页开始
     */
    public final static int CURRENTPAGE = 0;
    /**
     * 默认每页展示10页
     */
    public final static int PAGESIZE = 10;

    public  Pageable initPage(Integer currentPage, Integer pageSize, Sort sort){
        if(currentPage ==null || currentPage<0){
            currentPage = CURRENTPAGE;
        }
        if(pageSize ==null || pageSize<=0){
            pageSize = PAGESIZE ;
        }
        PageRequest page=null;
        if(sort==null){
            page =  PageRequest.of(currentPage,pageSize);
        }else{
           page =  PageRequest.of(currentPage,pageSize,sort);
        }


        return page;
    }

    @InitBinder
    public void initDate(WebDataBinder bind){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(fmt, true);
        bind.registerCustomEditor(Date.class, editor);
    }

}
