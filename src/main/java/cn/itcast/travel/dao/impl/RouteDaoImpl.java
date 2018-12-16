package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 查询总记录数
     * @param cid
     * @param rname
     * @return
     */
    @Override
    public int findTotalCount(int cid, String rname) {

        //1.定义sql模板,用StringBuilder进行条件拼接
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        //System.out.println(sb);

        //查询所需要的参数存入集合
        List params = new ArrayList();
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");
            //添加问号对应的值
            params.add(cid);
        }

        if(rname != null && rname.length() > 0 && !"null".equals(rname)){
            sb.append(" and rname like ? ");
            //添加问号对应的值
            params.add("%"+rname+"%");
        }
//        System.out.println(params);
//       System.out.println(sb);

        sql = sb.toString();


        return template.queryForObject(sql,Integer.class,params.toArray());
    }

    /**
     * 查询每页显示的记录集合
     * @param cid
     * @param rname
     * @param start_record
     * @param pageSize
     * @return
     */
    @Override
    public List<Route> findPage(int cid,int start_record,int pageSize,String rname) {

        //1.定义sql模板,用StringBuilder进行条件拼接
        String sql = " select * from tab_route where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);
        //System.out.println(sb);
        //查询所需要的参数存入集合
        List params = new ArrayList();

        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");
            //添加问号对应的值
            params.add(cid);
        }

        if(rname != null && rname.length() > 0 && !"null".equals(rname)){
            sb.append(" and rname like ? ");
            //添加问号对应的值
            params.add("%"+rname+"%");
        }
        //分页条件
        sb.append(" limit ? , ? ");

        sql = sb.toString();

        params.add(start_record);
        params.add(pageSize);

//        System.out.println(sql);
//        System.out.println(params);

        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
    }

    /**
     * 查询单个线路信息
     * @param rid
     * @return
     */
    @Override
    public Route findOne(int rid) {
        Route route = null;
        try {
            String sql = "select * from tab_route where rid = ?";
            route = template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
        } catch (DataAccessException e) {
//            e.printStackTrace();
        }
        return route;
    }
}
