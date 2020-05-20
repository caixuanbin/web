package cn.sessiontech.xcx.common;

import cn.sessiontech.xcx.enums.common.QueryEnum;
import lombok.Data;

/**
 * @author xbcai
 * @classname QueryObject
 * @description Specification 构建查询参数
 * @date 2019/6/22 14:30
 */
@Data
public class QueryObject<K,V> {
    public K key;
    public V value;
    public QueryEnum qe=QueryEnum.EQUAL;

    public QueryObject(K key, V value, QueryEnum qe) {
        this.key = key;
        this.value = value;
        this.qe = qe;
    }

    public QueryObject(K key, V value) {
        this.key = key;
        this.value = value;
    }

}
