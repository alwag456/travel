package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 查询所有分类名称
     * @return
     */
    @Override
    public List<Category> findAll() {

        String sql = "select * from tab_category ORDER  BY cid";
        return template.query(sql, new BeanPropertyRowMapper<Category>(Category.class));


    }
}