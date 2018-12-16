package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteService {
    /**
     * 分页查询
     * @param cid
     * @param currentPage
     * @param pageSize
     * @param rname
     * @return
     */
    public PageBean<Route> pageQuery(int cid,int currentPage,int pageSize,String rname);

    /**
     * 查询单个线路详细信息
     * @param rid
     * @return
     */
    public Route findOne(String rid);

    /**
     * 查询我的收藏的集合
     * @param uid
     * @return
     */
    public List<Route> findMyFavoriteRoutes(int uid);

    List<Route> findCountByFavorite();
}
