package com.atguigu.lease.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;


public enum ItemType implements BaseEnum {

    APARTMENT(1, "公寓"),

    ROOM(2, "房间");

//（44）在 Spring 中，当你使用 @RequestParam 注解时，Spring MVC 框架会自动将前端传入的请求参数与后端的参数类型进行匹配。由于 ItemType 被定义为枚举类型，Spring 会根据传入的数值自动将其转换为对应的枚举实例。这是基于 @EnumValue 注解的规则，也就是说，当传入 1 时，Spring 会将其转换为 ItemType.APARTMENT，因为 1 是 APARTMENT 的 code。
    @EnumValue //用于枚举对象ItemType到属性code之间的相互映射（双向的）。 （TypeHandler枚举类型转换 如枚举转数字）将标注的字段值（即 code 字段的值）作为枚举类型存储到数据库中。（MyBatis Plus 提供的一个特性）
    @JsonValue//用于ItemType对象到code属性之间的相互映射 从当你在类或枚举的某个方法或字段上使用 @JsonValue 注解时，Jackson 会将这个方法或字段的返回值作为对象的 JSON 表示形式，而不是序列化整个对象。也就是说，带有 @JsonValue 注解的方法或字段的值会替代对象本身，成为序列化的输出内容。
    private Integer code;
    private String name;

    @Override
    public Integer getCode() {
        return this.code;
    }


    @Override
    public String getName() {
        return name;
    }

    ItemType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

}
