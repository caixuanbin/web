package cn.sessiontech.xcx.repository.sys;

import cn.sessiontech.xcx.entity.sys.TSysRole;
import cn.sessiontech.xcx.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author xbcai
 * @classname TSysRoleRepository
 * @description 角色
 * @date 2019/9/10 14:52
 */
public interface TSysRoleRepository extends BaseRepository<TSysRole> {
    /**
     * 获取用户权限
     * @param userId 用户id
     * @return 返回spring security 权限值
     */
    @Query(value = "SELECT b.role_auth,b.id,b.role_name FROM t_sys_user_role a INNER JOIN t_sys_role b ON a.role_id=b.id \n" +
            "WHERE a.user_id=?1 " ,nativeQuery = true)
    String[] getUserRoleAuths(String userId);
}
