package com.atguigu.lease.web.admin.controller.lease;


import com.atguigu.lease.common.result.Result;
import com.atguigu.lease.model.entity.ViewAppointment;
import com.atguigu.lease.model.enums.AppointmentStatus;
import com.atguigu.lease.web.admin.service.ViewAppointmentService;
import com.atguigu.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.atguigu.lease.web.admin.vo.appointment.AppointmentVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "预约看房管理")
@RequestMapping("/admin/appointment")
@RestController
//ViewAppointment类是AppointmentVo的一部分，
// AppointmentVo关联了包含了ApartmentInfo和ViewAppointment两张表以展示列表全部信息
public class ViewAppointmentController {

    @Autowired
    private ViewAppointmentService service;
    @Operation(summary = "分页查询预约信息")
    @GetMapping("page")
    public Result<IPage<AppointmentVo>> page(@RequestParam long current, @RequestParam long size, AppointmentQueryVo queryVo) {
        IPage<AppointmentVo> page = new Page<>(current, size); //IPage 是 MyBatis Plus 提供的分页查询结果的接口，代表分页的数据结构。通常用于封装分页的结果集。
        IPage<AppointmentVo> result = service.pageAppointmentByQuery(page, queryVo);
        return Result.ok(result);
    }

    @Operation(summary = "根据id更新预约状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam AppointmentStatus status) {
        LambdaUpdateWrapper<ViewAppointment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ViewAppointment::getId, id);
        updateWrapper.set(ViewAppointment::getAppointmentStatus, status);
        service.update(updateWrapper);
        return Result.ok();
    }

}
