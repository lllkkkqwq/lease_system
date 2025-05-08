package com.atguigu.lease.web.admin.controller.apartment;


import com.atguigu.lease.common.result.Result;
import com.atguigu.lease.model.entity.PaymentType;
import com.atguigu.lease.web.admin.service.PaymentTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//全部实体都是使用了逻辑删除的功能，执行查询接口的时候会自动加上is deleted = 0这个查询条件，逻辑删除定义在BaseEntity类的@TableLogic
@Tag(name = "支付方式管理")
@RequestMapping("/admin/payment")
@RestController//将所有请求方法的返回值序列化成json字符串然后返回给前端
public class PaymentTypeController {

    @Autowired
    private PaymentTypeService service;
    @Operation(summary = "查询全部支付方式列表")
    @GetMapping("list")
    public Result<List<PaymentType>> listPaymentType() {
//        LambdaQueryWrapper<PaymentType> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(PaymentType::getIsDeleted,0);
        List<PaymentType> list = service.list();
        return Result.ok(list);
    }

    @Operation(summary = "保存或更新支付方式")
    @PostMapping("saveOrUpdate")      //@RequestBody：springMvc将请求体中的json字符串反序列化成PaymentType对象
    public Result saveOrUpdatePaymentType(@RequestBody PaymentType paymentType) {
        service.saveOrUpdate(paymentType);//更新数据就需要传入id，保存数据不需要传入id（Spring 的 saveOrUpdate 方法通常由服务层或持久层实现，特别是像 MyBatis Plus 这样的框架会提供这种方法。其工作机制如下：
        //如果传入的 PaymentType 对象的 id 为 null 或者没有在数据库中找到该 id，则执行保存操作，将对象视为一个新记录插入数据库。如果传入的 id 不为 null 并且数据库中存在该 id 对应的记录，系统会执行更新操作，将对象的其他字段值更新到该记录中。）
        return Result.ok();
    }

    @Operation(summary = "根据ID删除支付方式")
    @DeleteMapping("deleteById")
    public Result deletePaymentById(@RequestParam Long id) {
        service.removeById(id);//此项目中所有表的删除操作都是逻辑删除，逻辑删除本质是更新操作，但由于mybatisp提供了逻辑删除的支持，且前面已做好相关配置，所以现在才可以像普通删除一样执行逻辑删除
        return Result.ok();//43 虽然这里是remove方法，但msp生成的语句是更新语句
    }

}















