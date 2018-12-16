package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    private UserService service = new UserServiceImpl();

    /**
     * 注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户输入验证码
        String check = request.getParameter("check");

        //System.out.println(check);

        HttpSession session = request.getSession();
        //从session域中获取匹配验证码
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");

       // System.out.println(checkcode_server);

        //保证验证码只能用一次
        session.removeAttribute("CHECKCODE_SERVER");

        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            //验证码错误或者为空
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误,请重新输入!");

            //将结果序列化入json
            String json = writeValueAsString(info);
            //将json写回客户端
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);

            return;
        }
        //验证成功
        //获取所有的信息
        Map<String, String[]> map = request.getParameterMap();

        User user = new User();
        try {
            //将获取的user对象封装
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        boolean flag = service.regist(user, request);

       // System.out.println(flag);
        //封装前端返回的对象
        ResultInfo info = new ResultInfo();
        if (flag) {
            //注册成功
            info.setFlag(true);
            System.out.println("注册成功");
        } else {
            info.setFlag(false);
            System.out.println("注册失败");
        }
        //将结果序列化入json
        String json = writeValueAsString(info);
        //将json写回客户端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

    }

    /**
     * 激活状态查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取激活码
        String code = request.getParameter("code");
        if (code != null) {
            boolean flag = service.contrast(code);
            //判断激活码
            String msg = null;
            if (flag) {
                //激活成功
                msg = "激活成功,请<a href ='login.html'>登陆</a>";
            } else {
                //激活失败
                msg = "激活失败,请联系管理员";
            }
            //将数据写回前端
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);

        }
    }

    /**
     * 登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //验证校验
        String check = request.getParameter("check");
        //从sesion中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次
        //比较
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            //验证码错误
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            String json = writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }
        //获取输入用户名和密码
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用方法查询数据库
        User login_user = service.login(user);
        ResultInfo info = new ResultInfo();
        if (login_user == null) {
            //查询不存在该用户
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误!");
        }
        if (login_user != null && !"Y".equals(login_user.getStatus())) {
            //如果用户存在,再查询激活状态
            info.setFlag(false);
            info.setErrorMsg("用户未激活,请激活后再登陆!");
        }
        if (login_user != null && "Y".equals(login_user.getStatus())) {
            //登陆成功
            request.getSession().setAttribute("user", login_user);
            info.setFlag(true);

        }

        response.setContentType("application/json;charset=utf-8");
//        String json = mapper.writeValueAsString(info);
//        response.getWriter().write(json);
        //序列化学回前端
        writeValue(info, response);

    }

    /**
     * 登录功能-姓名展示
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取登陆存入域中的对象
        Object user = request.getSession().getAttribute("user");
        response.setContentType("application/json;charset=utf-8");
//        String json = mapper.writeValueAsString(user);
//        response.getWriter().write(json);
        //序列化写回表头
        writeValue(user, response);
    }

    /**
     * 退出功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //退出,清除或者销毁session里的数据
        request.getSession().invalidate();
        //重定向到登陆页面
        response.sendRedirect(request.getContextPath() + "/login.html");
    }


}
