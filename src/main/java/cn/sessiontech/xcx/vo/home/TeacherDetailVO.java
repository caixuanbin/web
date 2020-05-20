package cn.sessiontech.xcx.vo.home;

import lombok.Data;

/**
 * @author xbcai
 * @classname TeacherDetailVO
 * @description 老师详细资料
 * @date 2019/10/5 12:08
 */
@Data
public class TeacherDetailVO {
    public TeacherDetailVO(String teacherUserName, String teacherTitle, String teacherJj, String teacherRemarks, String netImageUrl) {
        this.teacherUserName = teacherUserName;
        this.teacherTitle = teacherTitle;
        this.teacherJj = teacherJj;
        this.teacherRemarks = teacherRemarks;
        this.netImageUrl = netImageUrl;
    }

    /**
     * 老师姓名
     */
    private String teacherUserName;
    /**
     * 标题
     */
    private String teacherTitle;
    /**
     * 简介
     */
    private String teacherJj;
    /**
     *
     * 备注
     */
    private String teacherRemarks;
    /**
     * 老师图片
     */
    private String netImageUrl;

}
