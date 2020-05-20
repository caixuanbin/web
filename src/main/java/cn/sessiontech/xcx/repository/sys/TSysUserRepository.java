package cn.sessiontech.xcx.repository.sys;

import cn.sessiontech.xcx.entity.sys.TSysUser;
import cn.sessiontech.xcx.repository.base.BaseRepository;

/**
 * @author xbcai
 * @classname TSysUserRepository
 * @description 系统用户表
 * @date 2019/9/10 14:46
 */
public interface TSysUserRepository extends BaseRepository<TSysUser> {
    TSysUser findByUsername(String username);
}
