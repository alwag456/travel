package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface FavoriteDao {
    /**
     * 查询用户收藏信息
     * @param i
     * @param uid
     * @return
     */
    public Favorite isFavorite(int rid, int uid);

    /**
     * 查询线路收藏次数
     * @param rid
     * @return
     */
    public int findCount(int rid);

    /**
     * 收藏线路功能
     * @param rid
     * @param uid
     */
    public void addFavorite(int rid, int uid);

    /**
     * 删除收藏
     * @param i
     * @param uid
     */
    public void delFavorite(int rid, int uid);

    /**
     * 查询我的收藏
     * @param uid
     * @return
     */
    public List<Route> findMyFavoriteRoutes(int uid);

    List<Route> findCountByFavorite();
}
