package cn.sessiontech.xcx.vo.sys;

import lombok.Data;

import java.util.List;

/**
 * @author xbcai
 * @classname TSaasHomeSysMenuVO
 * @description 菜单
 * @date 2019/9/11 17:51
 */
@Data
public class TSaasHomeSysMenuVO {

    private String id;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单标题
     */
    private String menuTitle;
    /**
     * 父级id
     */
    private String pId;
    /**
     * 菜单类型
     * menu：菜单；catalog：目录；
     */
    private String menuType;
    /**
     * 菜单级别
     */
    private Integer menuLevel;
    /**
     * 菜单路径
     */
    private String menuPath;
    /**
     * 排序
     */
    private Integer sortBy;

    private List<TSaasHomeSysMenuVO> childMenus;
}
