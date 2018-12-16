package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     * 查询用户收藏信息
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public boolean isFavorite(String rid, int uid) {

        Favorite favorite = favoriteDao.isFavorite(Integer.parseInt(rid),uid);

        return favorite != null;//如果对象有值，则为true，反之，则为false
    }

    /**
     * 收藏线路
     * @param rid
     * @param uid
     */
    @Override
    public void addFavorite(String rid, int uid) {
        favoriteDao.addFavorite(Integer.parseInt(rid),uid);
    }

    /**
     * 取消收藏的方法
     * @param rid
     * @param uid
     */
    @Override
    public void delFavorite(String rid, int uid) {
        favoriteDao.delFavorite(Integer.parseInt(rid),uid);
    }
}
