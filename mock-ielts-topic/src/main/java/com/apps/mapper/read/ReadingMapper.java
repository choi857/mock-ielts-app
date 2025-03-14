package com.apps.mapper.read;

import com.apps.model.read.Reading;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReadingMapper {

    // 插入阅读材料
    void insertReading(Reading reading);

    // 根据 ID 查询阅读材料
    Reading selectReadingById(@Param("id") Long id);

    // 查询所有阅读材料
    List<Reading> selectAllReadings();

    // 更新阅读材料
    void updateReading(Reading reading);

    // 根据 ID 删除阅读材料
    void deleteReadingById(@Param("id") Long id);
}