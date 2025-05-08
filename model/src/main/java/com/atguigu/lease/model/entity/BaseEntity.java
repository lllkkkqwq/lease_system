package com.atguigu.lease.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
//Serializable接口让对象能够被序列化。序列化是一种将对象转换为字节流的过程，从而可以将对象的状态保存到文件、数据库、内存中，或通过网络传输。相应地，反序列化则是将字节流重新转换为对象的过程。
@Data
public class BaseEntity implements Serializable {//实现Serializable接口，方便对实体对象缓存（redies）

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "创建时间")
    @TableField(value = "create_time",fill= FieldFill.INSERT)//表明当前createTime字段会在插入数据时自动填充，配置填充什么内容要实现mybatis plus的一个接口，在**common模块**下创建`com.atguigu.lease.common.mybatisplus.MybatisMetaObjectHandler`类
    @JsonIgnore                                              //利用mybatis p自动填充功能（7.2.2.1.2）
    private Date createTime;

    @JsonIgnore
    @Schema(description = "更新时间")
    @TableField(value = "update_time",fill= FieldFill.UPDATE)//表明当前updateTime字段会在更新数据时自动填充
    private Date updateTime;

    @JsonIgnore
    @Schema(description = "逻辑删除")
    @TableField("is_deleted")
    @TableLogic //自动为查询操作增加`is_deleted=0`过滤条件，并将删除操作转为更新语句
    //42当某个字段（如 isDeleted）被加上 @TableLogic 注解时，MyBatis Plus 会自动将该字段处理为逻辑删除字段。在执行删除操作时，MyBatis Plus 不会真的从数据库中删除该记录，而是将这个逻辑删除字段的值更新为某个标记值（通常是 1）。
    private Byte isDeleted;

}