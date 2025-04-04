package com.apps.mapper.listen;

import com.apps.model.listen.ListeningQuestion;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ListeningQuestionMapper {
    @Insert("INSERT INTO COL_LISTENING_QUESTION (COL_ID, COL_LISTENING_ID, COL_TYPE, COL_CONTENT, COL_PLACEHOLDER_FORMAT, COL_PART, COL_IMAGE_URL, COL_CREATED_AT, COL_UPDATED_AT) VALUES (#{id}, #{listeningId}, #{type}, #{content}, #{placeholderFormat}, #{part}, #{colImageUrl}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
    void insertQuestion(ListeningQuestion question);

    @Select("SELECT * FROM COL_LISTENING_QUESTION WHERE COL_LISTENING_ID = #{listeningId} AND COL_PART = #{part}")
    @Results({
            @Result(property = "id", column = "COL_ID"),
            @Result(property = "listeningId", column = "COL_LISTENING_ID"),
            @Result(property = "type", column = "COL_TYPE"),
            @Result(property = "content", column = "COL_CONTENT"),
            @Result(property = "placeholderFormat", column = "COL_PLACEHOLDER_FORMAT"),
            @Result(property = "createdAt", column = "COL_CREATED_AT"),
            @Result(property = "updatedAt", column = "COL_UPDATED_AT"),
            @Result(property = "part", column = "COL_PART"),
            @Result(property = "colImageUrl", column = "COL_IMAGE_URL")
    })
    List<ListeningQuestion> findQuestionsByListeningIdAndPart(Long listeningId, String part);

    @Update("UPDATE COL_LISTENING_QUESTION SET COL_TYPE = #{type}, COL_CONTENT = #{content}, COL_PLACEHOLDER_FORMAT = #{placeholderFormat}, COL_PART = #{part}, COL_IMAGE_URL = #{colImageUrl}, COL_UPDATED_AT = CURRENT_TIMESTAMP WHERE COL_ID = #{id}")
    void updateQuestion(ListeningQuestion question);

    @Select("SELECT COL_PART FROM COL_LISTENING_QUESTION WHERE COL_LISTENING_ID = #{listeningId}")
    List<String> findPartsByListeningId(Long listeningId);

    @Select("SELECT * FROM COL_LISTENING_QUESTION WHERE COL_LISTENING_ID = #{listeningId}")
    @Results({
            @Result(property = "id", column = "COL_ID"),
            @Result(property = "listeningId", column = "COL_LISTENING_ID"),
            @Result(property = "type", column = "COL_TYPE"),
            @Result(property = "content", column = "COL_CONTENT"),
            @Result(property = "placeholderFormat", column = "COL_PLACEHOLDER_FORMAT"),
            @Result(property = "createdAt", column = "COL_CREATED_AT"),
            @Result(property = "updatedAt", column = "COL_UPDATED_AT"),
            @Result(property = "part", column = "COL_PART"),
            @Result(property = "colImageUrl", column = "COL_IMAGE_URL")
    })
    List<ListeningQuestion> findQuestionsByListeningId(Long listeningId);
}