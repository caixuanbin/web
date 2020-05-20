package cn.sessiontech.xcx.service;

import cn.sessiontech.xcx.common.PageResult;
import cn.sessiontech.xcx.entity.TTeacherIntro;
import cn.sessiontech.xcx.service.base.BaseService;
import cn.sessiontech.xcx.vo.home.TeacherDetailVO;
import org.springframework.data.domain.Pageable;

/**
 * @author xbcai
 * @classname TTeacherIntroService
 * @description 老师资料
 * @date 2019/10/5 11:23
 */
public interface TTeacherIntroService extends BaseService<TTeacherIntro> {
    /**
     * 首页老师资料
     */
    PageResult getHomeTeacherList(Pageable pageable);

    /**
     * 查询老师资料
     * @param id 老师表主键id
     */
    TeacherDetailVO getTheacherDetail(String id);
}
