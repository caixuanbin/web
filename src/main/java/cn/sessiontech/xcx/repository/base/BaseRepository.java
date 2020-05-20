package cn.sessiontech.xcx.repository.base;

import cn.sessiontech.xcx.entity.base.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * @author xbcai
 * @classname BaseRepository
 * @description 所有dao接口层父类
 * @date 2019/5/21 9:44
 */
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T,String>, JpaSpecificationExecutor<T> {


}
