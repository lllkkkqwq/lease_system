package com.atguigu.lease.web.app.controller.appointment;


import com.atguigu.lease.common.login.LoginUserHolder;
import com.atguigu.lease.common.result.Result;
import com.atguigu.lease.model.entity.ViewAppointment;
import com.atguigu.lease.web.app.service.ViewAppointmentService;
import com.atguigu.lease.web.app.vo.appointment.AppointmentDetailVo;
import com.atguigu.lease.web.app.vo.appointment.AppointmentItemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "看房预约信息")
@RestController
@RequestMapping("/app/appointment")
public class ViewAppointmentController {
    @Autowired
    private ViewAppointmentService service;
    @Operation(summary = "保存或更新看房预约")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ViewAppointment viewAppointment) {

//        viewAppointment.setUserId(LoginUserHolder.getLoginUser().getUserId());
//        service.saveOrUpdate(viewAppointment);
        Long userId = LoginUserHolder.getLoginUser().getUserId();
        viewAppointment.setUserId(userId);//设置当前用户id
        service.saveOrUpdate(viewAppointment);//保存或更新预约信息。
        return Result.ok();
    }

    @Operation(summary = "查询个人预约看房列表")
    @GetMapping("listItem")
    public Result<List<AppointmentItemVo>> listItem() {
        List<AppointmentItemVo> list = service.listItemByUserId(LoginUserHolder.getLoginUser().getUserId());
        return Result.ok(list);
    }

    @GetMapping("getDetailById")
    @Operation(summary = "根据ID查询预约详情信息")
    public Result<AppointmentDetailVo> getDetailById(Long id) {
        AppointmentDetailVo appointmentDetailVo = service.getDetailById(id);
        return Result.ok(appointmentDetailVo);
    }

}

