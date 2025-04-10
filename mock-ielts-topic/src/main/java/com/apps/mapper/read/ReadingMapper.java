package com.apps.mapper.read;

import com.apps.model.read.Reading;
import com.apps.model.read.ReadingSummary;
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
    //查询所有阅读汇总
    List<ReadingSummary>selectAllReadingSummary();

    // 更新阅读材料
    void updateReading(Reading reading);

    // 根据 ID 删除阅读材料
    void deleteReadingById(@Param("id") Long id);

    // 插入阅读汇总
    void insertReadingSummary(ReadingSummary readingSummary);

    // 根据 ID 查询阅读汇总
    ReadingSummary selectReadingSummaryById(@Param("id") Long id);

    // 删除阅读汇总
    void deleteReadingSummaryById(@Param("id") Long id);

    // 根据阅读汇总 ID 查询关联的阅读材料
    List<Reading> selectReadingsBySummaryId(@Param("summaryId") Long summaryId);
}