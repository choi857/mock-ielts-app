package com.apps.mapper.listen;

import com.apps.model.listen.Listening;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ListeningMapper {
    @Insert("INSERT INTO COL_LISTENING (COL_ID,COL_TITLE, COL_CONTENT, COL_AUDIO_URL, COL_CREATED_AT, COL_UPDATED_AT) VALUES (#{id},#{title}, #{content}, #{audioUrl}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
    void insertListening(Listening listening);

    @Select("SELECT * FROM COL_LISTENING WHERE COL_ID = #{id}")
    @Results({
            @Result(property = "id", column = "COL_ID"),
            @Result(property = "title", column = "COL_TITLE"),
            @Result(property = "content", column = "COL_CONTENT"),
            @Result(property = "audioUrl", column = "COL_AUDIO_URL"),
            @Result(property = "createdAt", column = "COL_CREATED_AT"),
            @Result(property = "updatedAt", column = "COL_UPDATED_AT")
    })
    Listening findListeningById(Long id);

    @Select("SELECT * FROM COL_LISTENING")
    @Results({
            @Result(property = "id", column = "COL_ID"),
            @Result(property = "title", column = "COL_TITLE"),
            @Result(property = "audioUrl", column = "COL_AUDIO_URL"),
            @Result(property = "createdAt", column = "COL_CREATED_AT"),
            @Result(property = "updatedAt", column = "COL_UPDATED_AT")
    })
    List<Listening> selectAll();

    @Update("UPDATE COL_LISTENING SET COL_TITLE = #{title}, COL_CONTENT = #{content}, COL_AUDIO_URL = #{audioUrl}, COL_UPDATED_AT = CURRENT_TIMESTAMP WHERE COL_ID = #{id}")
    void updateListening(Listening listening);

}