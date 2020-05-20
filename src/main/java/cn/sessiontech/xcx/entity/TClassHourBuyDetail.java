package cn.sessiontech.xcx.entity;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xbcai
 * @classname TClassHourBuyDetail
 * @description 课时购买
 * @date 2019/10/4 20:18
 */
@Data
@Entity(name = "t_class_hour_buy_detail")
public class TClassHourBuyDetail extends BaseEntity {
    @Column(length = 50)
    private String studentUserId;
    @Column(length = 50)
    private String studentUserName;
    @Column(length = 50)
    private String teacherUserId;
    @Column(length = 50)
    private String teacherUserName;
    @Column(length = 50)
    private String operatorUserId;
    /**
     * 1：购买；2：赠送；3：扣减
     */
    @Column(length = 5)
    private String buyStatus;
    /**
     * 课节数
     */
    private Integer classNum;
    @Column(length = 250)
    private String classRemarks;
    /**
     * 1：未安排；2：安排中；3：已安排
     */
    @Column(length = 5)
    private String isArranged;
    /**
     * 首次上课时间 yyyy-MM-dd
     */
    @Column(length = 20)
    private String firstOnClassTime;
}
