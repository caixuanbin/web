package cn.sessiontech.xcx.vo.user;

import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 * @classname StudentVO
 * @description 老师详细资料
 * @date 2019/10/4 14:49
 */
@Data
public class UserVO {
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
     * 孩子
     */
    private List<UserVO> childs;
}
