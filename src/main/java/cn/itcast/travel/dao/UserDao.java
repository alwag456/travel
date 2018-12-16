package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 查找用户名是否存在
     * @param username
     * @return
     */
    public User findUserName(String username);

    /**
     * 存储用户信息
     * @param user
     */
    public void save(User user);

    /**
     * 查找激活号是否存在
     * @param code
     * @return
     */
    public User findCode(String code);

    /**
     * 修改激活状态
     * @param user
     */
    public void updateStatus(User user);

    /**
     * 登陆方法
     * @param username
     * @param password
     * @return
     */
    public User login(String username, String password);
}
