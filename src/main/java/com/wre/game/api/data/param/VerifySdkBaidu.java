package com.wre.game.api.data.param;

public class VerifySdkBaidu {
    /**应用 ID*/
    private Integer AppId;
    /**SDK 系统内部订单号    */
    private String OrderSerial;
    /**CP 订单号    */
    private String CooperatorOrderSerial;
    /**签名    */
    private String Sign;
    /****/
    private Content Content;

    @Override
    public String toString() {
        return "VerifySdkBaidu{" +
                "AppId=" + AppId +
                ", OrderSerial='" + OrderSerial + '\'' +
                ", CooperatorOrderSerial='" + CooperatorOrderSerial + '\'' +
                ", Sign='" + Sign + '\'' +
                ", Content=" + Content +
                '}';
    }

    public Integer getAppId() {
        return AppId;
    }

    public void setAppId(Integer appId) {
        AppId = appId;
    }

    public String getOrderSerial() {
        return OrderSerial;
    }

    public void setOrderSerial(String orderSerial) {
        OrderSerial = orderSerial;
    }

    public String getCooperatorOrderSerial() {
        return CooperatorOrderSerial;
    }

    public void setCooperatorOrderSerial(String cooperatorOrderSerial) {
        CooperatorOrderSerial = cooperatorOrderSerial;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public Content getContent() {
        return Content;
    }

    public void setContent(Content content) {
        Content = content;
    }

    public static class  Content{
        private String UID;
        private String MerchandiseName;
        private Double OrderMoney;
        private String StartDateTime;
        private String BankDateTime;
        private Integer OrderStatus;
        private String StatusMsg;
        private String ExtInfo;
        private String VoucherMoney;

        @Override
        public String toString() {
            return "Content{" +
                    "UID='" + UID + '\'' +
                    ", MerchandiseName='" + MerchandiseName + '\'' +
                    ", OrderMoney=" + OrderMoney +
                    ", StartDateTime='" + StartDateTime + '\'' +
                    ", BankDateTime='" + BankDateTime + '\'' +
                    ", OrderStatus=" + OrderStatus +
                    ", StatusMsg='" + StatusMsg + '\'' +
                    ", ExtInfo='" + ExtInfo + '\'' +
                    ", VoucherMoney='" + VoucherMoney + '\'' +
                    '}';
        }

        public String getUID() {
            return UID;
        }

        public void setUID(String UID) {
            this.UID = UID;
        }

        public String getMerchandiseName() {
            return MerchandiseName;
        }

        public void setMerchandiseName(String merchandiseName) {
            MerchandiseName = merchandiseName;
        }

        public Double getOrderMoney() {
            return OrderMoney;
        }

        public void setOrderMoney(Double orderMoney) {
            OrderMoney = orderMoney;
        }

        public String getStartDateTime() {
            return StartDateTime;
        }

        public void setStartDateTime(String startDateTime) {
            StartDateTime = startDateTime;
        }

        public String getBankDateTime() {
            return BankDateTime;
        }

        public void setBankDateTime(String bankDateTime) {
            BankDateTime = bankDateTime;
        }

        public Integer getOrderStatus() {
            return OrderStatus;
        }

        public void setOrderStatus(Integer orderStatus) {
            OrderStatus = orderStatus;
        }

        public String getStatusMsg() {
            return StatusMsg;
        }

        public void setStatusMsg(String statusMsg) {
            StatusMsg = statusMsg;
        }

        public String getExtInfo() {
            return ExtInfo;
        }

        public void setExtInfo(String extInfo) {
            ExtInfo = extInfo;
        }

        public String getVoucherMoney() {
            return VoucherMoney;
        }

        public void setVoucherMoney(String voucherMoney) {
            VoucherMoney = voucherMoney;
        }
    }
}
