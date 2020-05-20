package cn.sessiontech.xcx.service.base;

import cn.sessiontech.xcx.common.PageResult;
import cn.sessiontech.xcx.common.QueryObject;
import cn.sessiontech.xcx.entity.base.BaseEntity;
import cn.sessiontech.xcx.enums.common.QueryEnum;
import cn.sessiontech.xcx.repository.base.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author xbcai
 * @classname BaseServiceImpl
 * @description service顶层实现类
 * @date 2019/5/21 10:15
 */
public class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {
    @Autowired
    private BaseRepository<T> baseRepository;
    @Override
    public T saveAndFlush(T entity) {
        return baseRepository.saveAndFlush(entity);
    }
    @Override
    public List<T> saveAll(Iterable<T> entities){return baseRepository.saveAll(entities);}

    @Override
    public List<T> findALL() {
        return baseRepository.findAll();
    }

    @Override
    public T findById(String id) {
        Optional<T> optional = baseRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public void deleteByEntity(T entity) {
        baseRepository.delete(entity);
    }

    @Override
    public void deleteById(String id) {
        baseRepository.deleteById(id);
    }
    @Override
    public List<T> findByParams(T entity){
        Example<T> example = Example.of(entity);
        return baseRepository.findAll(example);
    }

    @Override
    public <K, V> List<T> findByParams(List<QueryObject<K, V>> list, QueryEnum queryType) {
        Specification<T> specification = getSpec(list,queryType);
        return baseRepository.findAll(specification);
    }

    @Override
    public <K, V> List<T> findByParamsSort(List<QueryObject<K, V>> list, QueryEnum queryType, String sortType, String... properties) {
        Sort sort = new Sort(Sort.Direction.fromString(sortType),properties);
        Specification<T> specification = getSpec(list,queryType);
        return baseRepository.findAll(specification,sort);
    }

    @Override
    public PageResult findByParams(T entity,Pageable pageable){
        Example<T> example = Example.of(entity);
        Page<T> entities = baseRepository.findAll(example,pageable);
        return converter(entities);
    }

    @Override
    public <K,V> PageResult findByParams(List<QueryObject<K,V>> list,QueryEnum queryType, Pageable pageable) {
        Specification<T> specification = getSpec(list,queryType);
        Page<T> entities = baseRepository.findAll(specification,pageable);
        return converter(entities);
    }

    @Override
    public T findEntityByParams(T entity){
        Example<T> example = Example.of(entity);
        Optional<T> optional = baseRepository.findOne(example);
        return optional.orElse(null);
    }

    @Override
    public PageResult queryByPage(Pageable pageable) {
        Page<T> dicPage=baseRepository.findAll(pageable);
        Page<T> entities = new PageImpl<>(dicPage.getContent(),pageable,dicPage.getTotalElements());
        return converter(entities);
    }

    /**
     * 封装jpa分页的数据转化为自定义的分页返回
     * @param entities
     * @return
     */
    public PageResult converter(Page<?> entities){
        PageResult pageResult = new PageResult();
        pageResult.setData(entities.getContent());
        pageResult.setCurrentPage(entities.getNumber());
        pageResult.setPageSize(entities.getSize());
        pageResult.setFirst(entities.isFirst());
        pageResult.setTotalCount(entities.getTotalElements());
        pageResult.setTotalPage(entities.getTotalPages());
        return pageResult;
    }

    /**
     * 构造查询模块
     * @param list 查询参数
     * @param queryType and 或者 or
     * @param <K> key
     * @param <V> value
     * @return
     */
    private <K,V> Specification<T> getSpec(List<QueryObject<K,V>> list,QueryEnum queryType){
        Specification<T> specification;
        specification = (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb)-> {
            List<Predicate> predicates = new ArrayList<>();
            for (QueryObject<K,V> qb: list) {
                Class<?> cls = qb.getClass();
                try {
                    String key = cls.getDeclaredField("key").get(qb).toString();
                    Object value = cls.getDeclaredField("value").get(qb);
                    if(value==null||"".equals(value)){
                        continue;
                    }

                    //相等
                    if(QueryEnum.EQUAL.equals(qb.getQe())){
                        predicates.add(cb.equal(root.get(key).as(value.getClass()),value));
                    }
                    //不等
                    if(QueryEnum.NOT_EQUAL.equals(qb.getQe())){
                        predicates.add(cb.notEqual(root.get(key).as(value.getClass()), value));
                    }

                    if(QueryEnum.QUERY_OR.equals(qb.getQe())){
                        List<Predicate> lists = new ArrayList<>();
                        String[] keys = key.split(",");
                        for (String s:keys) {
                            lists.add(cb.equal(root.get(s),value));
                        }
                        predicates.add(cb.or(lists.toArray(new Predicate[0])));
                    }
                    //大于
                    if(QueryEnum.GREATER_THAN.equals(qb.getQe())){
                        predicates.add(cb.greaterThanOrEqualTo(root.get(key), (Comparable)value));
                    }
                    //小于
                    if(QueryEnum.LESS_THEN.equals(qb.getQe())){
                        predicates.add(cb.lessThanOrEqualTo(root.get(key), (Comparable)value));
                    }
                    //左边模糊匹配
                    if(QueryEnum.LEFT_LIEK.equals(qb.getQe())){
                        predicates.add(cb.like(root.get(key), "%" + value));
                    }
                    //右边模糊匹配
                    if(QueryEnum.RIGHT_LIKE.equals(qb.getQe())){
                        predicates.add(cb.like(root.get(key),  value+"%"));
                    }
                    //全模糊匹配
                    if(QueryEnum.ALL_LIKE.equals(qb.getQe())){
                        predicates.add(cb.like(root.get(key),  "%"+value+"%"));
                    }
                    //不包含
                    if(QueryEnum.NOT_LIKE.equals(qb.getQe())){
                        predicates.add(cb.notLike(root.get(key), "%" + value + "%"));
                    }
                    //IN 查询
                    if(QueryEnum.IN.equals(qb.getQe())){
                        CriteriaBuilder.In<Object> in = cb.in(root.get(key));
                        String[] vs = String.valueOf(value).split(",");
                        for (int i=0;i<vs.length;i++){
                            in.value(vs[i]);
                        }
                        predicates.add(cb.and(in));
                    }
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            // 以上条件and拼接查询
            if(queryType.equals(QueryEnum.QUERY_AND)){
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }else{
                //以上条件 or 拼接查询
                return cb.or(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return specification;
    }
}
