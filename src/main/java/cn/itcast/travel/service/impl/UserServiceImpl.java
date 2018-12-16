package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

import javax.servlet.http.HttpServletRequest;

public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();

    /**
     * 注册,发邮件
     * @param user
     * @param request
     * @return
     */
    @Override
    public boolean regist(User user, HttpServletRequest request) {
        User user1 = dao.findUserName(user.getUsername());
        if (user1 == null) {
            //不存在该用户名,存入数据库
            user.setCode(UuidUtil.getUuid());
            user.setStatus("N");
            dao.save(user);

            String path = request.getContextPath();  // 获取虚拟路径
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path;
            String content = "<a href='" + basePath + "/user/active?code=" + user.getCode() + "'>点击激活【黑马旅游网】</a>";
//            String content="<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>";

            MailUtils.sendMail(user.getEmail(), content, "激活邮件");

            return true;
        }
        //用户名重复
        return false;
    }

    /**
     * 修改激活状态
     * @param code
     * @return
     */
    @Override
    public boolean contrast(String code) {
        User user = dao.findCode(code);
        if (user != null) {
            dao.updateStatus(user);
            return true;
        }
        return false;
    }

    @Override
    public User login(User user) {
        return dao.login(user.getUsername(), user.getPassword());
    }
}
