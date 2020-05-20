package cn.sessiontech.xcx.entity.sys;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xbcai
 * @classname TSysMenu
 * @description 菜单表
 * @date 2019/9/10 14:38
 */
@Entity(name = "t_sys_menu")
@Data
public class TSysMenu extends BaseEntity {
    /**
     * 菜单名称
     */
    @Column(length = 50)
    private String menuName;
    /**
     * 菜单标题
     */
    @Column(length = 50)
    private String menuTitle;
    /**
     * 父级id
     */
    @Column(length = 50)
    private String pId;
    /**
     * 菜单类型
     * menu：菜单；catalog：目录；
     */
    @Column(length = 50)
    private String menuType;
    /**
     * 菜单级别
     */
    @Column(length = 2)
    private Integer menuLevel;
    /**
     * 菜单路径
     */
    @Column(length = 100)
    private String menuPath;
    /**
     * 菜单排序
     */
    @Column(length = 3)
    private Integer sortBy;
    /**
     * 标识
     * Y:可用；N:不可用
     */
    @Column(length = 2)
    private String flag;
    /**
     * 隐藏
     * Y:隐藏；N:不隐藏
     */
    @Column(length = 2)
    private String hidden;
    /**
     * 备注
     */
    @Column(length = 100)
    private String remarks;
}
