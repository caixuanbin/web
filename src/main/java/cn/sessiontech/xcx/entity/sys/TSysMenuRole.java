package cn.sessiontech.xcx.entity.sys;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author xbcai
 * @classname TSysMenuRole
 * @description 角色菜单关联表
 * @date 2019/9/11 17:09
 */
@Entity(name = "t_sys_menu_role")
@Data
public class TSysMenuRole extends BaseEntity {
    @Column(length = 32)
    private String menuId;
    @Column(length = 32)
    private String roleId;
}
