package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 查询用户收藏信息
     *
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public Favorite isFavorite(int rid, int uid) {
        Favorite favorite = null;
        try {
            String sql = "select * from tab_favorite where rid = ? and uid = ?";
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
//            System.out.println("没有查到收藏信息!");
        }
        return favorite;
    }

    /**
     * 查询线路收藏次数
     *
     * @param rid
     * @return
     */
    @Override
    public int findCount(int rid) {
        String sql = "select count(*) from tab_favorite  where rid = ? ";

        return template.queryForObject(sql, Integer.class, rid);

    }

    /**
     * 收藏线路功能
     * @param rid
     * @param uid
     */
    @Override
    public void addFavorite(int rid, int uid) {
        String sql = "insert into tab_favorite values(?,?,?)";
        template.update(sql,rid,new Date(),uid);

        String sql1 = "update tab_route set count = (count +1) where rid = ?";
        template.update(sql1,rid);
    }

    /**
     * 删除收藏
     * @param rid
     * @param uid
     */
    @Override
    public void delFavorite(int rid, int uid) {
        String sql = "delete from tab_favorite  where rid = ? and uid = ?";
        template.update(sql,rid,uid);

        String sql1 = "update tab_route set count = (count -1) where rid = ?";
        template.update(sql1,rid);

    }

    /**
     * 查询我的收藏
     * @param uid
     * @return
     */
    @Override
    public List<Route> findMyFavoriteRoutes(int uid) {
        List<Route> list = null;
        try {
            String sql = "SELECT * FROM tab_favorite f LEFT JOIN tab_route r ON f.rid = r.rid WHERE uid = ?";
            list = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), uid);
        } catch (DataAccessException e) {
            System.out.println("还没有收藏");
        }
        System.out.println(list);
        return list;
    }

    public List<Route> findCountByFavorite(){
        List<Route> list = null;
        try {
            String sql = "SELECT *  FROM (SELECT rid,COUNT(*) FROM tab_favorite GROUP BY rid) f LEFT JOIN tab_route r ON f.rid = r.rid ";
            list = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class));
        } catch (DataAccessException e) {
     //       e.printStackTrace();
        }
        return list;
    }
}
