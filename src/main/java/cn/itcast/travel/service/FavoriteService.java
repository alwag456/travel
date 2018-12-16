package cn.itcast.travel.service;

public interface FavoriteService {

    /**
     * 查询用户收藏信息
     * @param rid
     * @param uid
     * @return
     */
    public boolean isFavorite(String rid, int uid);

    /**
     * 收藏线路功能
     * @param rid
     * @param uid
     */
    public void addFavorite(String rid, int uid);

    /**
     * 取消收藏的方法
     * @param rid
     * @param uid
     */
    public void delFavorite(String rid, int uid);
}
