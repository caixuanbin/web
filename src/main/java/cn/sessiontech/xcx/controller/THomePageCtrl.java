package cn.sessiontech.xcx.controller;

import cn.sessiontech.xcx.common.PageResult;
import cn.sessiontech.xcx.common.Result;
import cn.sessiontech.xcx.constant.GlobalConstants;
import cn.sessiontech.xcx.controller.base.BaseCtrl;
import cn.sessiontech.xcx.dto.home.TeacherQueryDTO;
import cn.sessiontech.xcx.enums.ResultCodeEnum;
import cn.sessiontech.xcx.service.TTeacherIntroService;
import cn.sessiontech.xcx.utils.JpaUtils;
import cn.sessiontech.xcx.vo.home.TeacherDetailVO;
import cn.sessiontech.xcx.vo.home.TeacherInfoLbVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xbcai
 * @classname THomePageCtrl
 * @description 首页
 * @date 2019/10/5 11:25
 */
@RestController
@RequestMapping("/home")
public class THomePageCtrl extends BaseCtrl {
    @Autowired
    private TTeacherIntroService tTeacherIntroService;
    @Value("${file.networkPath}")
    private String networkPath;

    /**
     * 查询首页老师资料
     * @param dto
     * @param bindingResult
     * @return
     */
    @SuppressWarnings("all")
    @PostMapping("/getTeacherInfoList")
    public Result getTeacherInfoList(@RequestBody @Valid TeacherQueryDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Result.fail(ResultCodeEnum.LOCK_NECESSARY_PARAMS);
        }
        Pageable pageable =new JpaUtils().getPageable(dto.getCurrentPage(),dto.getPageSize(), GlobalConstants.DESC,"createTime");
        PageResult teacherResult = tTeacherIntroService.getHomeTeacherList(pageable);
        if(teacherResult.getCode()==0){
            teacherResult.setCode(200);
        }
        List<TeacherInfoLbVO> lbVOList=(List<TeacherInfoLbVO>) teacherResult.getData();
        lbVOList.forEach(item->item.setNetImageUrl(networkPath+item.getNetImageUrl()));
        teacherResult.setData(lbVOList);
        return teacherResult;
    }

    /**
     * 查询老师详细信息
     * @param id 主键id
     */
    @GetMapping("/getTeacherById/{id}")
    public Result getTeacherById(@PathVariable("id") String id){
        TeacherDetailVO theacherDetail = tTeacherIntroService.getTheacherDetail(id);
        theacherDetail.setNetImageUrl(networkPath+theacherDetail.getNetImageUrl());
        return Result.success(theacherDetail);
    }
}
