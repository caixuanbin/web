package cn.sessiontech.xcx.repository;

import cn.sessiontech.xcx.entity.TTeacherIntro;
import cn.sessiontech.xcx.repository.base.BaseRepository;
import cn.sessiontech.xcx.vo.home.TeacherDetailVO;
import cn.sessiontech.xcx.vo.home.TeacherInfoLbVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
 * @author xbcai
 * @classname TTeacherIntroRepository
 * @description 老师资料
 * @date 2019/10/5 11:22
 */

public interface TTeacherIntroRepository extends BaseRepository<TTeacherIntro> {

    /**
     * 查询首页老师信息
     */
    @Query(value = "select new cn.sessiontech.xcx.vo.home.TeacherInfoLbVO(a.teacherTitle,b.fileThumPath,a.id) from cn.sessiontech.xcx.entity.TTeacherIntro a inner join cn.sessiontech.xcx.entity.TSysFile b on a.teacherImageId=b.id")
    Page<TeacherInfoLbVO> getHomeTeacherList(Pageable pageable);

    /**
     *  查询老师资料信息
     * @param id 老师资料表主键id
     */
    @Query(value = "select new cn.sessiontech.xcx.vo.home.TeacherDetailVO(a.teacherUserName,a.teacherTitle,a.teacherJj,a.teacherRemarks,b.fileThumPath) " +
            "from cn.sessiontech.xcx.entity.TTeacherIntro a inner join cn.sessiontech.xcx.entity.TSysFile b on a.teacherImageId=b.id " +
            "where a.id=?1")
    TeacherDetailVO getTheacherDetail(String id);
}
