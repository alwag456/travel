package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao  = new RouteDaoImpl();

    private RouteImgDao routeImgDao  = new RouteImgDaoImpl();

    private SellerDao sellerDao = new SellerDaoImpl();

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     * 设置PageBean参数
     * @param cid
     * @param currentPage
     * @param pageSize
     * @param rname
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        PageBean<Route> pb  = new PageBean<Route>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示数量
        pb.setPageSize(pageSize);
        //调用方法查询数据库旅游线路数据的总数并存入pb
        int totalCount = routeDao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);
        //设置当前页面显示的数据集合
        int start_record = (currentPage-1)*pageSize;
        List<Route> list = routeDao.findPage(cid,start_record,pageSize,rname);
        //将集合存入pb
        pb.setList(list);
        //通过总数和每页显示的数量计算总页码数并存入pb
        int totalPage = totalCount % pageSize == 0 ? totalCount/pageSize : (totalCount/pageSize)+1;
        pb.setTotalPage(totalPage);

        return pb;
    }

    /**
     * 查询单个线路详细信息
     * @param rid
     * @return
     */
    @Override
    public Route findOne(String rid) {

        //调用dao层方法查询信息
        Route route = routeDao.findOne(Integer.parseInt(rid));
        //根据route的rid查询商品信息图片集合
        List<RouteImg> list = routeImgDao.findAllByRid(route.getRid());
        route.setRouteImgList(list);

        //根据route的sid查询所属商家信息
        Seller seller = sellerDao.findSellerBySid(route.getSid());
        route.setSeller(seller);

        //根据rid到收藏表中查询收藏次数
        int count = favoriteDao.findCount(route.getRid());
//        System.out.println(count);
        route.setCount(count);

        return  route;
    }

    /**
     * 查询我的收藏
     * @param uid
     * @return
     */
    @Override
    public List<Route> findMyFavoriteRoutes(int uid) {

        return favoriteDao.findMyFavoriteRoutes(uid) ;
    }

    @Override
    public List<Route> findCountByFavorite() {
        return favoriteDao.findCountByFavorite();
    }

}
