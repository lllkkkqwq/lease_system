package com.atguigu.lease.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaseStatus implements BaseEnum {
    //假设数据库中的 status 字段值为 1，ORM 框架会自动将其转换为 Java 中的 LeaseStatus.SIGNING 枚举实例。同样地，当你将 LeaseStatus.SIGNING 保存到数据库时，它会将 1 存入 status 字段。
    SIGNING(1, "签约待确认"),
    SIGNED(2, "已签约"),
    CANCELED(3, "已取消"),
    EXPIRED(4, "已到期"),
    WITHDRAWING(5, "退租待确认"),
    WITHDRAWN(6, "已退租"),
    RENEWING(7, "续约待确认");

    @EnumValue//35
    @JsonValue
    private Integer code;

    private String name;

    LeaseStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
