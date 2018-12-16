package cn.itcast.travel.web.servlet.old;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/registUserServlet")
public class RegistUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户输入验证码
        String check = request.getParameter("check");

        System.out.println(check);

        HttpSession session = request.getSession();
        //从session域中获取匹配验证码
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");

        System.out.println(checkcode_server);

        //保证验证码只能用一次
        session.removeAttribute("CHECKCODE_SERVER");

        if (checkcode_server == null  || !checkcode_server.equalsIgnoreCase(check)) {
            //验证码错误或者为空
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误,请重新输入!");

            //将结果序列化入json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
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
        UserService service = new UserServiceImpl();
        boolean flag = service.regist(user,request);

        System.out.println(flag);
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
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        //将json写回客户端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
