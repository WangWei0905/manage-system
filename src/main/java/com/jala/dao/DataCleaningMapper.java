package com.jala.dao;

import com.jala.model.vo.DataCleaning;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DataCleaningMapper {

    int deleteByPrimaryKey(Integer dataId);


    int insert(DataCleaning record);


    int insertSelective(DataCleaning record);


    DataCleaning selectByPrimaryKey(Integer dataId);


    int updateByPrimaryKeySelective(DataCleaning record);


    int updateByPrimaryKey(DataCleaning record);

    /**
     * 批量插入数据清洗数据
     * @param list
     */
    void insertDataCleanBatch(List<DataCleaning> list);

    /**
     * 查询用户访问量
     * @return
     */
    List<DataCleaning> selectDataCleanListByLoginInfo();


}