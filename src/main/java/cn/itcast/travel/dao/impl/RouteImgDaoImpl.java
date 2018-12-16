package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RouteImgDaoImpl implements RouteImgDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 查询线路详细图片集合
     * @param rid
     * @return
     */
    @Override
    public List<RouteImg> findAllByRid(int rid) {
        List<RouteImg> list = null;
        try {
            String sql = "select * from tab_route_img where rid = ?";
            list = template.query(sql, new BeanPropertyRowMapper<>(RouteImg.class), rid);
        } catch (DataAccessException e) {
//            System.out.println("查询的集合为空!");
        }
        return list;
    }
}
