package com.wre.game.api.dao;

import com.wre.game.api.data.entity.PaymentTransaction;
import com.wre.game.api.data.entity.UserGameData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PaymentTransactionDao {

    int insertPaymentTransaction(PaymentTransaction paymentTransaction);

    List<PaymentTransaction> selectPaymentTransaction(@Param("appId") String appId, @Param("transactionId") String transactionId);

}