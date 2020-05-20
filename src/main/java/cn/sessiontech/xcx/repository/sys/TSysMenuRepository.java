package cn.sessiontech.xcx.repository.sys;

import cn.sessiontech.xcx.entity.sys.TSysMenu;
import cn.sessiontech.xcx.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author xbcai
 * @classname TSysMenuRepository
 * @description 菜单
 * @date 2019/9/10 14:51
 */
public interface TSysMenuRepository extends BaseRepository<TSysMenu> {
    /**
     * 根据角色id获取菜单id
     * @param roleId 角色id
     * @return 返回菜单ID
     */
    @Query(value = "SELECT a.menu_id FROM t_sys_menu_role a where a.role_id=?1",nativeQuery = true)
    String[] getMenuId(String roleId);
}
