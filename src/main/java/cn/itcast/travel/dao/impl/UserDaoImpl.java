package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 查询用户是否存在
     * @param username
     * @return
     */
    @Override
    public User findUserName(String username) {
        //防止出现异常try
        User user = null;
        try {
            //1.定义sql
            String sql = "select * from tab_user where username = ?";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (Exception e) {
//            System.out.println("该用户名可以注册");
        }

        return user;
    }

    /**
     * 保存用户信息
     * @param user
     */
    @Override
    public void save(User user) {
        //用户名不存在  ,存入数据库
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        template.update(sql,user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode());

    }

    /**
     * 查询激活码
     * @param code
     * @return
     */
    @Override
    public User findCode(String code) {
        User user = null ;
        try {
            String sql = "select * from tab_user where code =?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        } catch (DataAccessException e) {
//            System.out.println("激活码有误");
        }
        return user;
    }

    /**
     * 修改激活状态
     * @param user
     */
    @Override
    public void updateStatus(User user) {
        String sql = "update tab_user set status = 'Y' where username = ?";
        template.update(sql,user.getUsername());

    }

    /**
     * 登录查询
     * @param username
     * @param password
     * @return
     */
    @Override
    public User login(String username, String password) {
        User user = null;
        try {
            String sql = "select * from tab_user where username = ? and password = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }
}
