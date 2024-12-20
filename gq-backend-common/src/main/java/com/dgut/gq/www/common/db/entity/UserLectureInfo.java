package com.dgut.gq.www.common.db.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户讲座关系表
 *
 * @author hyj
 * @version 1.0
 * @since 2024-12-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("gq_user_lecture_info")
public class UserLectureInfo {
    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 讲座id
     */
    private String lectureId;


    /**
     * 用户的openid
     */
    private String openid;

    /**
     * 观看讲座状态
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer isDeleted;
}
