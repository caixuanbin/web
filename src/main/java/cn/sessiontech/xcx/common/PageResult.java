package cn.sessiontech.xcx.common;

import lombok.Data;


/**
 * @author xbcai
 * @classname PageResult
 * @description 分页显示
 * @date 2019/5/21 14:18
 */
@Data
public class PageResult extends Result{

    private static final long serialVersionUID = -5397966081581629020L;
    /**
     * 当前页从0开始
     */
    private int currentPage;
    /**
     * 一页显示多少条
     */
    private int pageSize;
    /**
     * 总条数
     */
    private long totalCount;
    /**
     * 总页码
     */
    private long totalPage;
    /**
     * 是否第一页
     */
    private boolean isFirst;
}
