package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.model.entity.*;
import com.atguigu.lease.web.admin.mapper.*;
import com.atguigu.lease.web.admin.service.LeaseAgreementService;
import com.atguigu.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.atguigu.lease.web.admin.vo.agreement.AgreementVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {

    @Autowired
    private LeaseAgreementMapper leaseAgreementMapper;//idea显示问题不用管
    @Override
    public IPage<AgreementVo> pageAgreementByQuery(IPage<AgreementVo> page, AgreementQueryVo queryVo) {
        return leaseAgreementMapper.pageAgreement(page,queryVo);
    }
    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;//idea显示问题不用管
    @Autowired
    private RoomInfoMapper roomInfoMapper;//idea显示问题不用管
    @Autowired
    private PaymentTypeMapper paymentTypeMapper;//idea显示问题不用管
    @Autowired
    private LeaseTermMapper leaseTermMapper;//idea显示问题不用管
    @Override
    public AgreementVo getAgreementById(Long id) {
        //1.查询租约信息
        LeaseAgreement leaseAgreement = leaseAgreementMapper.selectById(id);

        //2.查询公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(leaseAgreement.getApartmentId());

        //3.查询房间信息
        RoomInfo roomInfo = roomInfoMapper.selectById(leaseAgreement.getRoomId());

        //4.查询支付方式
        PaymentType paymentType = paymentTypeMapper.selectById(leaseAgreement.getPaymentTypeId());

        //5.查询租期
        LeaseTerm leaseTerm = leaseTermMapper.selectById(leaseAgreement.getLeaseTermId());

        AgreementVo adminAgreementVo = new AgreementVo();
        BeanUtils.copyProperties(leaseAgreement, adminAgreementVo);
        adminAgreementVo.setApartmentInfo(apartmentInfo);
        adminAgreementVo.setRoomInfo(roomInfo);
        adminAgreementVo.setPaymentType(paymentType);
        adminAgreementVo.setLeaseTerm(leaseTerm);
        return adminAgreementVo;
    }
}




