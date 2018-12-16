package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    /**
     * 查询线路信息的详细图片
     * @param rid
     * @return
     */
   public List<RouteImg> findAllByRid(int rid);
}
