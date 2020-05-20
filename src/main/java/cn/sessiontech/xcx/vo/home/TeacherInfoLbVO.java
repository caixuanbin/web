package cn.sessiontech.xcx.vo.home;

import lombok.Data;

/**
 * @author Administrator
 * @classname TeacherInfoLbVO
 * @description 首页老师资料
 * @date 2019/10/5 11:41
 */
@Data
public class TeacherInfoLbVO {
    private String teacherTitle;
    private String netImageUrl;
    private String id;

    public TeacherInfoLbVO(String teacherTitle, String netImageUrl, String id) {
        this.teacherTitle = teacherTitle;
        this.netImageUrl = netImageUrl;
        this.id = id;
    }
}
