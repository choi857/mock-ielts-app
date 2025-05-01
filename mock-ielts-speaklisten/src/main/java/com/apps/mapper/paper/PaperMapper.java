package com.apps.mapper.paper;

import com.apps.model.paper.Paper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PaperMapper {

    // 获取所有试卷
    @Select("SELECT * FROM COL_PAPER")
    @Results({
            @Result(property = "paperId", column = "PAPER_ID", id = true),
            @Result(property = "paperName", column = "PAPER_NAME"),
            @Result(property = "paperDescription", column = "PAPER_DESCRIPTION"),
            @Result(property = "readingSummaryId", column = "READING_SUMMARY_ID"),
            @Result(property = "listenId", column = "LISTEN_ID"),
            @Result(property = "writeId", column = "WRITE_ID"),
            @Result(property = "speakId", column = "SPEAK_ID"),
            @Result(property = "createdAt", column = "CREATED_AT"),
            @Result(property = "updatedAt", column = "UPDATED_AT")
    })
    List<Paper> getAllPapers();

    // 根据ID获取试卷
    @Select("SELECT * FROM COL_PAPER WHERE PAPER_ID = #{id}")
    @Results({
            @Result(property = "paperId", column = "PAPER_ID", id = true),
            @Result(property = "paperName", column = "PAPER_NAME"),
            @Result(property = "paperDescription", column = "PAPER_DESCRIPTION"),
            @Result(property = "readingSummaryId", column = "READING_SUMMARY_ID"),
            @Result(property = "listenId", column = "LISTEN_ID"),
            @Result(property = "writeId", column = "WRITE_ID"),
            @Result(property = "speakId", column = "SPEAK_ID"),
            @Result(property = "createdAt", column = "CREATED_AT"),
            @Result(property = "updatedAt", column = "UPDATED_AT")
    })
    Paper getPaperById(@Param("id") Long id);

    // 新增试卷
    @Insert("INSERT INTO COL_PAPER (PAPER_NAME, PAPER_DESCRIPTION, READING_SUMMARY_ID, LISTEN_ID, WRITE_ID, SPEAK_ID, CREATED_AT, UPDATED_AT) " +
            "VALUES (#{paperName}, #{paperDescription}, #{readingSummaryId}, #{listenId}, #{writeId}, #{speakId}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "paperId", keyColumn = "PAPER_ID")
    void createPaper(Paper paper);

    @Update("UPDATE COL_PAPER SET PAPER_NAME = #{paperName}, PAPER_DESCRIPTION = #{paperDescription}, " +
            "READING_SUMMARY_ID = #{readingSummaryId}, LISTEN_ID = #{listenId}, WRITE_ID = #{writeId}, " +
            "SPEAK_ID = #{speakId}, UPDATED_AT = #{updatedAt} WHERE PAPER_ID = #{paperId}")
    void updatePaper(Paper paper);

    @Delete("DELETE FROM COL_PAPER WHERE PAPER_ID = #{id}")
    void deletePaper(@Param("id") Long id);

}
