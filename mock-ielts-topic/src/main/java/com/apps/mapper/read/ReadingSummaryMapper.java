package com.apps.mapper.read;

import com.apps.model.read.ReadingSummary;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ReadingSummaryMapper {

    @Insert("INSERT INTO COL_READING_SUMMARY (COL_TITLE, COL_CREATED_AT, COL_UPDATED_AT) VALUES (#{title}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ReadingSummary readingSummary);

    @Update("UPDATE COL_READING_SUMMARY SET COL_TITLE = #{title}, COL_UPDATED_AT = #{updatedAt} WHERE COL_ID = #{id}")
    void update(ReadingSummary readingSummary);

    @Delete("DELETE FROM COL_READING_SUMMARY WHERE COL_ID = #{id}")
    void deleteById(Long id);

    @Select("SELECT * FROM COL_READING_SUMMARY WHERE COL_ID = #{id}")
    ReadingSummary findById(Long id);

    @Select("SELECT * FROM COL_READING_SUMMARY")
    List<ReadingSummary> findAll();
}
