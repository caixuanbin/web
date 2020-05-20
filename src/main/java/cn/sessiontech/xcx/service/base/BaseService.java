package cn.sessiontech.xcx.service.base;


import cn.sessiontech.xcx.common.PageResult;
import cn.sessiontech.xcx.common.QueryObject;
import cn.sessiontech.xcx.enums.common.QueryEnum;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author xbcai
 * @classname BaseService
 * @description service 顶层接口
 * @date 2019/5/21 10:04
 */
public interface BaseService<T> {
    /**
     * 新增或修改
     * @param entity
     * @return
     */
    T saveAndFlush(T entity);

    /**
     * 批量保存
     * @param entities 待保存实体集合
     * @return 返回保存的实体集合
     */
    List<T> saveAll(Iterable<T> entities);

    /**
     * 查询所有数据
     * @return
     */
    List<T> findALL();

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    T findById(String id);

    /**
     * 删除实体
     * @param entity
     */
    void deleteByEntity(T entity);

    /**
     * 根据ID删除实体
     * @param id
     */
    void deleteById(String id);

    /**
     * 查询符合条件的一条数据返回
     * @param entity
     * @return
     */
    public T findEntityByParams(T entity);

    /**
     * 查询符合条件的所有数据
     * @param entity
     * @return
     */
    List<T> findByParams(T entity);

    /**
     * 动态查询符合条件的所有数据
     * @param list 查询条件
     * @param queryType 查询方式
     * @param <K> 键
     * @param <V> 值
     * @return
     *
     */
    <K,V>  List<T> findByParams(List<QueryObject<K,V>> list,QueryEnum queryType);

    /**
     * 动态查询符合条件的所有数据
     * @param list 查询条件
     * @param queryType 查询方式
     * @param sortType 排序方式 desc 或 asc
     * @param properties 排序的字段
     * @param <K> 键
     * @param <V> 值
     * @return
     *
     */
    <K,V>  List<T> findByParamsSort(List<QueryObject<K,V>> list,QueryEnum queryType,String sortType,String ...properties);

    /**
     * 按实体属性值查询符合条件的所有数据分页返回
     * @param entity
     * @param pageable
     * @return
     */
    PageResult findByParams(T entity,Pageable pageable);

    /**
     * 动态查询数据并分页返回
     * @param list 参数值
     * @param queryType 查询方式  and 查询  或 or 查询
     * @param pageable 分页返回
     * @param <K> key
     * @param <V> value
     * @return
     */
    <K,V> PageResult findByParams(List<QueryObject<K,V>> list , QueryEnum queryType, Pageable pageable);



    /**
     * 分页查询
     * @param pageable
     * @return
     */
    PageResult queryByPage(Pageable pageable);
}
