package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();

    /**
     * 查询所有分类名称
     * @return
     */
    @Override
    public List<Category> findAll(){
        ObjectMapper mapper = new ObjectMapper();
        //1.从redis中查询
        //1.1获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        //1.3查询sortedset中的分数(cid)和值(cname)   //在dao排序已完成
        String json = jedis.get("category");
        //System.out.println("-------redis---" + json);
        List<Category> cs = null;
        //2.判断查询的集合是否为空,如果为空，从数据库查询，不为空从缓存查询
        if (json == null || json.length() == 0) {
//            System.out.println("从数据库查询....");
            //3.如果为空,需要从数据库查询,在将数据存入redis
            cs = categoryDao.findAll();
            //将查到数据写入到json
            String csJson = null;
            try {
                csJson = mapper.writeValueAsString(cs);
            } catch (JsonProcessingException e) {
//                System.out.println("数据库查询数据为null");
            }
//            System.out.println("数据库查询数据" + cs);
            //将集合数据存储到redis中的 category的key
            jedis.set("category", csJson);
        } else {
//            System.out.println("从redis中查询.....");
            //4.如果不为空,数据存入list
            try {
                cs = mapper.readValue(json, List.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cs;
    }
}
