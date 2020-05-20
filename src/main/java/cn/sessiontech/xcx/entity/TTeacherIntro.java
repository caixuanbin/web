package cn.sessiontech.xcx.entity;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xbcai
 * @classname TTeacherIntro
 * @description 老师的宣传资料
 * @date 2019/10/5 11:15
 */
@Data
@Entity(name = "t_teacher_intro")
public class TTeacherIntro extends BaseEntity {
    /**
     * 老师姓名
     */
    @Column(length = 50)
    private String teacherUserName;
    /**
     * 对应用户表的id
     */
    @Column(length = 50)
    private String teacherUserId;
    /**
     * 老师封面图片
     */
    @Column(length = 50)
    private String teacherImageId;
    /**
     * 标题
     */
    @Column(length = 50)
    private String teacherTitle;
    /**
     * 简介
     */
    @Column(length = 500)
    private String teacherJj;
    /**
     * 1：有效；2：失效；
     */
    @Column(length = 5)
    private String isValid;
    /**
     * 排序
     */
    private Integer teacherSort;
    /**
     *
     * 备注
     */
    @Column(length = 500)
    private String teacherRemarks;
    /**
     * 预留字段01
     */
    private String teacherStr01;
    /**
     * 预留字段02
     */
    private String teacherStr02;
    /**
     * 预留字段03
     */
    private String teacherStr03;
}
