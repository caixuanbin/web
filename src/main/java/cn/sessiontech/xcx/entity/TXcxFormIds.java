package cn.sessiontech.xcx.entity;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xbcai
 * @classname TXcxFormIds
 * @description 小程序表单库
 * @date 2019/10/21 21:46
 */
@Data
@Entity(name = "t_xcx_form_ids")
public class TXcxFormIds extends BaseEntity {
    @Column(length = 50)
    private String formId;
    /**
     * 1：有效；2：失效；
     */
    @Column(length = 5)
    private String isValid;
}
