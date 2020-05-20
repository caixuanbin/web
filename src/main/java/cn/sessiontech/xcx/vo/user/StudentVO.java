package cn.sessiontech.xcx.vo.user;

import lombok.Data;

/**
 * @author Administrator
 * @classname StudentVO
 * @description 学生详细资料
 * @date 2019/10/4 14:49
 */
@Data
public class StudentVO {
    /**
     * 姓名
     */
    private String fullName;
    /**
     * 性别
     */
    private String userSex;
    /**
     * 电话
     */
    private String tel;
    /**
     * 账号--这里是微信号
     */
    private String username;
    /**
     * 家长
     */
    private StudentVO parent;
}
