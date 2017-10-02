package com.jala.dao;

import com.jala.model.vo.AnnouncementInfoUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 公告用户信息DAO
 *
 * @author yangxiaobing
 * @date   2017/9/1
 */
@Mapper
public interface AnnouncementInfoUserMapper {

    int deleteByPrimaryKey(Integer announcementInfoUserId);


    int insert(AnnouncementInfoUser record);

    int insertSelective(AnnouncementInfoUser record);

    AnnouncementInfoUser selectByPrimaryKey(Integer announcementInfoUserId);

    int updateByPrimaryKeySelective(AnnouncementInfoUser record);

    int updateByPrimaryKey(AnnouncementInfoUser record);

    void deleteAnnInfoUserByAnnouncementId(@Param("announcementId") Integer announcementId);


}