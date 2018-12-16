package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * 获取总记录数
     * @param cid
     * @param rname
     * @return
     */
    public int findTotalCount(int cid, String rname);

    /**
     * 获取每页显示的数据集合
     * @param cid
     * @param rname
     * @param start_record
     * @param pageSize
     * @return
     */
    public List<Route> findPage(int cid,int start_record,int pageSize,String rname);

    /**
     * 查询单个线路信息
     * @param rid
     * @return
     */
    public Route findOne(int rid);
}
