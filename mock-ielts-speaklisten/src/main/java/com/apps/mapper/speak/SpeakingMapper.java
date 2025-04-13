package com.apps.mapper.speak;

import com.apps.model.speak.Speaking;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 口语材料Mapper接口
 */
@Mapper
public interface SpeakingMapper {

    @Insert("INSERT INTO COL_SPEAKING (COL_TITLE, COL_AUDIO_URL, COL_TRANSCRIPT,COL_ID) VALUES (#{title}, #{imageUrl}, #{transcript},#{id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertSpeaking(Speaking speaking);

    @Update("UPDATE COL_SPEAKING SET COL_TITLE = #{title}, COL_AUDIO_URL = #{imageUrl}, COL_TRANSCRIPT = #{transcript} WHERE COL_ID = #{id}")
    void updateSpeaking(Speaking speaking);

    @Delete("DELETE FROM COL_SPEAKING WHERE COL_ID = #{id}")
    void deleteSpeaking(Long id);

    @Select("SELECT * FROM COL_SPEAKING WHERE COL_ID = #{id}")
    @Results({
            @Result(property = "id", column = "COL_ID"),
            @Result(property = "title", column = "COL_TITLE"),
            @Result(property = "imageUrl", column = "COL_AUDIO_URL"),
            @Result(property = "transcript", column = "COL_TRANSCRIPT"),
            @Result(property = "createdAt", column = "COL_CREATED_AT"),
            @Result(property = "updatedAt", column = "COL_UPDATED_AT")
    })
    Speaking findSpeakingById(Long id);

    @Select("SELECT * FROM col_reading_summary")
    @Results({
            @Result(property = "id", column = "COL_ID"),
            @Result(property = "title", column = "COL_TITLE"),
            @Result(property = "imageUrl", column = "COL_AUDIO_URL"),
            @Result(property = "transcript", column = "COL_TRANSCRIPT"),
            @Result(property = "createdAt", column = "COL_CREATED_AT"),
            @Result(property = "updatedAt", column = "COL_UPDATED_AT")
    })
    List<Speaking> findAllSpeakings();
}