package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();

    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       // System.out.println("进入分页查询....");
        //获取当前页数
        String rq_currentPage = request.getParameter("currentPage");
        //获取每页显示的个数
        String rq_pageSize = request.getParameter("pageSize");
        //获取关联id
        String rq_cid = request.getParameter("cid");
        //获取线路名称
        String rq_rname = request.getParameter("rname");
        //解决乱码问题
        String rname = new String(rq_rname.getBytes("iso-8859-1"), "utf-8");

        //判断rq_cid是否有值，将其赋值给cid
        int cid = 0;
        if (rq_cid != null && rq_cid.length() > 0 && !"null".equals(rq_cid)) {
            cid = Integer.parseInt(rq_cid);
        }
       // System.out.println("cid:" + cid);

        //判断当前rq_currentPage，是否在范围内，如果不是就设置成默认页面
        int currentPage = 0;
        if (rq_currentPage != null && rq_currentPage.length() > 0 && !"null".equals(currentPage)) {
            currentPage = Integer.parseInt(rq_currentPage);
        } else {
            currentPage = 1;
        }
       // System.out.println("currentPage:" + currentPage);

        //设置每页展示数量
        int pageSize = 0;
        if (rq_pageSize != null && rq_pageSize.length() > 0) {
            pageSize = Integer.parseInt(rq_pageSize);
        } else {
            pageSize = 5;
        }
        //调用service层方法,将数据存入PageBean
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize, rname);
       // System.out.println(pb);

        //写回前端
        writeValue(pb, response);

    }

    /**
     * 查询线路详细信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取线路id
        String rid = request.getParameter("rid");
        //查询返回Route对象
        Route route = routeService.findOne(rid);
        //写回前端
        writeValue(route, response);
    }

    /**
     * 根据线路rid和用户的查询用户是否收藏当前线路
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取线路rid
        String rid = request.getParameter("rid");
        //从session域中获取存储的user对象
        User user = (User) request.getSession().getAttribute("user");
        //判断用户是否登陆
        int uid;
        if (user != null) {
            //有登陆
            uid = user.getUid();
        } else {
            //用户没有登陆
            uid = 0;
        }
        //查询收藏信息
        boolean flag = favoriteService.isFavorite(rid, uid);

        //将结果写回前端
        writeValue(flag, response);

    }

    /**
     * 收藏线路功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取线路rid
        String rid = request.getParameter("rid");
        //从session域中获取存储的user对象
        User user = (User) request.getSession().getAttribute("user");
        //判断用户是否登陆
        int uid;
        if (user != null) {
            //有登陆
            uid = user.getUid();
        } else {
            //用户没有登陆
            return;
        }
        //收藏线路
       favoriteService.addFavorite(rid, uid);
    }

    /**
     * 删除收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void delFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取线路rid
        String rid = request.getParameter("rid");
        //从session域中获取存储的user对象
        User user = (User) request.getSession().getAttribute("user");
        //判断用户是否登陆
        int uid;
        if (user != null) {
            uid = user.getUid();
        } else {
            return;
        }
        favoriteService.delFavorite(rid, uid);
    }

    /**
     * 我的收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void myFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session域中获取存储的user对象
        System.out.println("进入我的收藏查询");
        User user = (User) request.getSession().getAttribute("user");
        //判断用户是否登陆
        int uid;
        if (user != null) {
            uid = user.getUid();
        } else {
            return;
        }
        List<Route> list = routeService.findMyFavoriteRoutes(uid);

        writeValue(list,response);
    }

    public void findCountByFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入收藏排行榜");
        List<Route> list = routeService.findCountByFavorite();

        writeValue(list,response);
    }
}