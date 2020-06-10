package com.yuyi.tea.mapper;

import com.yuyi.tea.bean.BoxReservation;
import com.yuyi.tea.bean.Reservation;
import com.yuyi.tea.bean.ShopBox;
import com.yuyi.tea.bean.ShopBoxInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ShopBoxMapper {

    @Select("select * from shopBox where shopId=#{shopId} and enforceTerminal=false")
    @ResultMap("shopBox")
    List<ShopBox> getShopBoxByShopId(int shopId);

    /**
     * 新增包厢
     * @param shopBox
     */
    @Insert("insert into shopBox(name,description,shopId,boxNum,priceId,duration,showOnHome) " +
            "values(#{name},#{description},#{shop.uid},#{boxNum},#{price.uid},#{duration},#{showOnHome})")
    @Options(useGeneratedKeys=true, keyProperty="uid")
    void saveShopBox(ShopBox shopBox);

    /**
     * 获取包厢列表
     * @return
     */
    @Select("select uid,name,description,shopId,enforceTerminal from shopBox")
    @Results({
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="shopId",property="shop",
                    many=@Many(select="com.yuyi.tea.mapper.ShopMapper.getShopOfShopBox",
                            fetchType= FetchType.LAZY))
    })
    List<ShopBox> getShopBoxes();

    /**
     * 失效包厢
     * @param uid
     */
    @Update("update shopBox set enforceTerminal=true where uid=#{uid}")
    void terminalShopBoxByUid(int uid);

    /**
     * 根据uid获取包厢详情
     * @param uid
     * @return
     */
    @Select("select * from shopBox where uid=#{uid}")
    @Results(id = "shopBox",
            value = {
            @Result(id = true,column = "uid",property = "uid"),
            @Result(column="shopId",property="shop",
                    many=@Many(select="com.yuyi.tea.mapper.ShopMapper.getShopOfShopBox",
                            fetchType= FetchType.LAZY)),
            @Result(column="priceId",property="price",
                    many=@Many(select="com.yuyi.tea.mapper.PriceMapper.getPrice",
                            fetchType= FetchType.LAZY)),
            @Result(column="uid",property="photos",
                    many=@Many(select="com.yuyi.tea.mapper.PhotoMapper.getPhotosByShopBoxId",
                            fetchType= FetchType.LAZY)),
                @Result(column="uid",property="infos",
                        many=@Many(select="com.yuyi.tea.mapper.ShopBoxMapper.getShopBoxInfo",
                                fetchType= FetchType.LAZY)),
    })
    ShopBox getShopBoxByUid(int uid);

    /**
     * 获取包厢预约须知
     * @param boxId
     * @return
     */
    @Select("select * from shopBoxInfo where boxId=#{boxId}")
    List<ShopBoxInfo> getShopBoxInfo(int boxId);

    @Update("update shopBox set name=#{name},description=#{description},priceId=#{price.uid},duration=#{duration},boxNum=#{boxNum},shopId=#{shop.uid},showOnHome=#{showOnHome} where uid=#{uid}")
    void updateShopBox(ShopBox shopBox);

    @Update("update shopBox set shopId=#{shopId} where uid=#{uid}")
    void updateShopBoxShopId(int uid, int shopId);

    @Update("update shopBox set shopId=null where uid=#{uid}")
    void setShopIdNull(int uid);

    /**
     * 根据orderId获取包厢预约列表
     * @param orderId
     * @return
     */
    @Select("select * from reservation where orderId=#{orderId} order by reservationTime")
    @Results(
            @Result(column = "boxId",property = "box",
                    one = @One(select="com.yuyi.tea.mapper.ShopBoxMapper.getShopBoxByUid",
                            fetchType = FetchType.LAZY))
    )
    List<Reservation> getReservationByOrderId(int orderId);

    /**
     * 根据boxId,开始时间，结束时间获取包厢预约列表
     * @param boxId
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("<script>" +
            "select * from reservation where boxId=#{boxId} and  " +
            "<if test='startTime!=-1'> <![CDATA[reservationTime>= #{startTime}]]> and </if>" +
            "<if test='endTime!=-1'> <![CDATA[reservationTime<= #{endTime}]]></if>" +
            "</script>")
    List<Reservation> getReservationByBoxId(int boxId,long startTime,long endTime);


    /**
     * 查找对应时间和包厢的预约
     * @param reservationTime
     * @param boxId
     * @return
     */
    @Select("select * from reservation where reservationTime=#{reservationTime} and boxId=#{boxId}")
    Reservation findReservation(long reservationTime, int boxId);

    /**
     * 获取小程序走马灯展示的包厢
     * @return
     */
    @Select("select * from shopBox where isShowOnHome=true")
    @ResultMap("shopBox")
    List<ShopBox> getSwiperList();

    /**
     * 获取近期热门包厢
     * @return
     */
    @Select("select * from lastMonthReservationView order by number desc")
    @Results(id = "boxReservation",
            value = {
                    @Result(id = true,column = "boxId",property = "boxId"),
                    @Result(column="boxId",property="box",
                            one=@One(select="com.yuyi.tea.mapper.ShopBoxMapper.getShopBoxByUid",
                                    fetchType= FetchType.LAZY))
            })
    List<BoxReservation> getHotBoxes();

    @Select("select * from shopBox where name like #{value} or description like #{value}")
    @ResultMap("shopBox")
    List<ShopBox> search(String value);

    /**
     * 保存包厢须知
     * @param info
     */
    @Insert("insert into shopBoxInfo(title,info,boxId) values(#{title},#{info},#{boxId})")
    void saveBoxInfo(ShopBoxInfo info);
}


