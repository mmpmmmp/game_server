package com.wre.game.api.dao;

import com.wre.game.api.data.entity.RechargeDataInfo;
import com.wre.game.api.entity.AddRecharge;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RechargeDataInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RechargeDataInfo record);

    int insertSelective(RechargeDataInfo record);

    int insertSelectiveLog(RechargeDataInfo record);

    RechargeDataInfo selectByPrimaryKey(Long id);

    List<AddRecharge> selectByUserIdAndAdd(@Param("userId") String userId);

    int updateByPrimaryKeySelective(RechargeDataInfo record);

    int updateByPrimaryKeyWithBLOBs(RechargeDataInfo record);

    int updateByPrimaryKey(RechargeDataInfo record);

    RechargeDataInfo selectByRoleId(@Param("roleId")String purchaseToken);

    Long getSBDueTime(@Param("roleId")String roleId, @Param("serialId")String serialId);
}