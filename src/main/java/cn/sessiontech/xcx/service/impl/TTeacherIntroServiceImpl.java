package cn.sessiontech.xcx.service.impl;

import cn.sessiontech.xcx.common.PageResult;
import cn.sessiontech.xcx.entity.TTeacherIntro;
import cn.sessiontech.xcx.repository.TTeacherIntroRepository;
import cn.sessiontech.xcx.service.TTeacherIntroService;
import cn.sessiontech.xcx.service.base.BaseServiceImpl;
import cn.sessiontech.xcx.vo.home.TeacherDetailVO;
import cn.sessiontech.xcx.vo.home.TeacherInfoLbVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author xbcai
 * @classname TTeacherIntroServiceImpl
 * @description 老师资料
 * @date 2019/10/5 11:24
 */
@Service
public class TTeacherIntroServiceImpl extends BaseServiceImpl<TTeacherIntro> implements TTeacherIntroService {
    @Autowired
    private TTeacherIntroRepository tTeacherIntroRepository;
    @Override
    public PageResult getHomeTeacherList(Pageable pageable) {
        Page<TeacherInfoLbVO> homeTeacherList = tTeacherIntroRepository.getHomeTeacherList(pageable);
        return this.converter(homeTeacherList);
    }

    @Override
    public TeacherDetailVO getTheacherDetail(String id) {
        return tTeacherIntroRepository.getTheacherDetail(id);
    }
}
