package com.atguigu.lease.web.admin.custom.converter;

import com.atguigu.lease.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
    //能够实现从String到BaseEnum每个子类的类型转换，
    // 将来springMvc框架需要用String到BaseEnum某个子类的转换器，框架就会调用getConverter方法，将子类对象传到targetType
    //最终就会返回一个String类型到目标类型的转换器 String - T
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {


        return new Converter<String, T>() {
            @Override
            public T convert(String code) {
                T[] enumConstants = targetType.getEnumConstants(); //拿到目标类型的全部枚举实例
                for (T enumConstant : enumConstants) {
                 if(enumConstant.getCode().equals(Integer.valueOf(code)))   {
                     return enumConstant;
                 }
                }
                throw new IllegalArgumentException("code"+code+"非法");
            }
        };
    }
}
