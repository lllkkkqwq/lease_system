package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import com.atguigu.lease.model.entity.*;
import com.atguigu.lease.model.enums.ItemType;
import com.atguigu.lease.web.admin.mapper.*;
import com.atguigu.lease.web.admin.service.*;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.atguigu.lease.web.admin.vo.fee.FeeValueVo;
import com.atguigu.lease.web.admin.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    @Autowired
    private  RoomInfoMapper roomInfoMapper;//idea显示问题不用管

    @Autowired
    private FeeValueMapper feeValueMapper;//idea显示问题不用管

    @Autowired
    private FacilityInfoMapper facilityInfoMapper;//idea显示问题不用管

    @Autowired
    private LabelInfoMapper labelInfoMapper;//idea显示问题不用管

    @Autowired
    private GraphInfoMapper graphInfoMapper;//idea显示问题不用管

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;//idea显示问题不用管

    @Autowired
    private GraphInfoService graphInfoService;

    @Autowired
    private ApartmentFacilityService apartmentFacilityService;

    @Autowired
    private ApartmentLabelService apartmentLabelService;

    @Autowired
    private ApartmentFeeValueService apartmentFeeValueService;

    @Autowired
    private ProvinceInfoMapper provinceInfoMapper;//idea显示问题不用管
    @Autowired
    private CityInfoMapper cityInfoMapper;//idea显示问题不用管
    @Autowired
    private DistrictInfoMapper districtInfoMapper;//idea显示问题不用管

    /**
     * 保存或更新公寓信息
     * @param apartmentSubmitVo
     */
    @Override
    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {
        //根据vo对象的id区分，有id是修改，无id是新增
        boolean isUpdate = apartmentSubmitVo.getId() != null;

        //bug修改，前端是根据xxxname来展示地址的，所以新增时，虽然传入了地址id，但没有具体的name。
        //根据省份id查询省份name
        LambdaQueryWrapper<ProvinceInfo> provinceQueryWrapper = new LambdaQueryWrapper<>();
        provinceQueryWrapper.eq(ProvinceInfo::getId, apartmentSubmitVo.getProvinceId());//查询条件
        ProvinceInfo provinceInfo = provinceInfoMapper.selectOne(provinceQueryWrapper);
        apartmentSubmitVo.setProvinceName(provinceInfo.getName());
        //根据市id查询市name
        LambdaQueryWrapper<CityInfo> cityQueryWrapper = new LambdaQueryWrapper<>();
        cityQueryWrapper.eq(CityInfo::getId, apartmentSubmitVo.getCityId());//查询条件
        CityInfo cityInfo = cityInfoMapper.selectOne(cityQueryWrapper);
        apartmentSubmitVo.setCityName(cityInfo.getName());
        //根据地区id查询地区name
        LambdaQueryWrapper<DistrictInfo> districtQueryWrapper = new LambdaQueryWrapper<>();
        districtQueryWrapper.eq(DistrictInfo::getId, apartmentSubmitVo.getDistrictId());//查询条件
        DistrictInfo districtInfo = districtInfoMapper.selectOne(districtQueryWrapper);
        apartmentSubmitVo.setDistrictName(districtInfo.getName());


        super.saveOrUpdate(apartmentSubmitVo);//传入子类实例，即能保存或修改公寓信息表。
        //先移除相关信息，再添加信息。
        if (isUpdate) {
            //1.删除图片列表
            LambdaQueryWrapper<GraphInfo> graphInfoQueryWrapper = new LambdaQueryWrapper<>();
            graphInfoQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);//type
            graphInfoQueryWrapper.eq(GraphInfo::getItemId, apartmentSubmitVo.getId());//id
            graphInfoService.remove(graphInfoQueryWrapper);//删掉的是符合条件的所有记录。

            //2.删除配套列表
            LambdaQueryWrapper<ApartmentFacility> facilityQueryWrapper = new LambdaQueryWrapper<>();
            facilityQueryWrapper.eq(ApartmentFacility::getApartmentId,apartmentSubmitVo.getId());
            apartmentFacilityService.remove(facilityQueryWrapper);

            //3.删除标签列表
            LambdaQueryWrapper<ApartmentLabel> labelQueryWrapper = new LambdaQueryWrapper<>();
            labelQueryWrapper.eq(ApartmentLabel::getApartmentId,apartmentSubmitVo.getId());
            apartmentLabelService.remove(labelQueryWrapper);

            //4.删除杂费列表
            LambdaQueryWrapper<ApartmentFeeValue> feeQueryWrapper = new LambdaQueryWrapper<>();
            feeQueryWrapper.eq(ApartmentFeeValue::getApartmentId,apartmentSubmitVo.getId());
            apartmentFeeValueService.remove(feeQueryWrapper);
        }
        //插入图片列表
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();//获取所有图片
        if (!CollectionUtils.isEmpty(graphVoList)){
            ArrayList<GraphInfo> graphInfoList = new ArrayList<>();//新建集合，将图片vo对象转成实体类对象
            for (GraphVo graphVo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();//图片实体类对象
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setItemId(apartmentSubmitVo.getId());
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfoList.add(graphInfo);
            }
            graphInfoService.saveBatch(graphInfoList);//传入图片实体类集合，进行批量插入。
        }
        //插入配套列表
        List<Long> facilityInfoIdList = apartmentSubmitVo.getFacilityInfoIds();
        if (!CollectionUtils.isEmpty(facilityInfoIdList)){
            ArrayList<ApartmentFacility> facilityList = new ArrayList<>();
            for (Long facilityId : facilityInfoIdList) {
                ApartmentFacility apartmentFacility = ApartmentFacility.builder().build();
                apartmentFacility.setApartmentId(apartmentSubmitVo.getId());
                apartmentFacility.setFacilityId(facilityId);
                facilityList.add(apartmentFacility);
            }
            apartmentFacilityService.saveBatch(facilityList);//批量插入
        }
        //插入标签列表
        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        if (!CollectionUtils.isEmpty(labelIds)) {
            List<ApartmentLabel> apartmentLabelList = new ArrayList<>();
            for (Long labelId : labelIds) {
                ApartmentLabel apartmentLabel = ApartmentLabel.builder().build();
                apartmentLabel.setApartmentId(apartmentSubmitVo.getId());
                apartmentLabel.setLabelId(labelId);
                apartmentLabelList.add(apartmentLabel);
            }
            apartmentLabelService.saveBatch(apartmentLabelList);//批量插入
        }
        //插入杂费列表
        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        if (!CollectionUtils.isEmpty(feeValueIds)) {
            ArrayList<ApartmentFeeValue> apartmentFeeValueList = new ArrayList<>();
            for (Long feeValueId : feeValueIds) {
                ApartmentFeeValue apartmentFeeValue = ApartmentFeeValue.builder().build();
                apartmentFeeValue.setApartmentId(apartmentSubmitVo.getId());
                apartmentFeeValue.setFeeValueId(feeValueId);
                apartmentFeeValueList.add(apartmentFeeValue);
            }
            apartmentFeeValueService.saveBatch(apartmentFeeValueList);//批量插入
        }
    }


    @Override
    public IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo) {
        return apartmentInfoMapper.pageItem(page,queryVo);
    }

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        //1.查询公寓信息
//        ApartmentInfo apartmentInfo = this.getById(id);
//        if (apartmentInfo == null) {
//            return null;
//        }
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);

        //2.查询图片列表
        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT, id);
//
        //3.查询标签列表
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByApartmentId(id);
//
        //4.查询配套列表
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByApartmentId(id);
//
//        //5.查询杂费列表
        List<FeeValueVo> feeValueVoList = feeValueMapper.selectListByApartmentId(id);

           //6.组装结果
        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();

        BeanUtils.copyProperties(apartmentInfo, apartmentDetailVo);
        apartmentDetailVo.setGraphVoList(graphVoList);
        apartmentDetailVo.setLabelInfoList(labelInfoList);
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        apartmentDetailVo.setFeeValueVoList(feeValueVoList);


        return apartmentDetailVo;
    }

    @Override
    public void removeApartmentById(Long id) {
        LambdaQueryWrapper<RoomInfo> roomQueryWrapper = new LambdaQueryWrapper<>();
        roomQueryWrapper.eq(RoomInfo::getApartmentId,id);
        Long count = roomInfoMapper.selectCount(roomQueryWrapper);//count:id为id的公寓下的房间的个数
        if (count>0){
            //公寓里还有房间，中止删除并响应提示信息
            throw new LeaseException(ResultCodeEnum.ADMIN_APARTMENT_DELETE_ERROR);
        }
        super.removeById(id);

        //1.删除图片列表
        LambdaQueryWrapper<GraphInfo> graphQueryWrapper = new LambdaQueryWrapper<>();
        graphQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
        graphQueryWrapper.eq(GraphInfo::getItemId, id);
        graphInfoService.remove(graphQueryWrapper);

        //2.删除配套列表
        LambdaQueryWrapper<ApartmentFacility> facilityQueryWrapper = new LambdaQueryWrapper<>();
        facilityQueryWrapper.eq(ApartmentFacility::getApartmentId,id);
        apartmentFacilityService.remove(facilityQueryWrapper);

        //3.删除标签列表
        LambdaQueryWrapper<ApartmentLabel> labelQueryWrapper = new LambdaQueryWrapper<>();
        labelQueryWrapper.eq(ApartmentLabel::getApartmentId,id);
        apartmentLabelService.remove(labelQueryWrapper);

        //4.删除杂费列表
        LambdaQueryWrapper<ApartmentFeeValue> feeQueryWrapper = new LambdaQueryWrapper<>();
        feeQueryWrapper.eq(ApartmentFeeValue::getApartmentId,id);
        apartmentFeeValueService.remove(feeQueryWrapper);}

}







