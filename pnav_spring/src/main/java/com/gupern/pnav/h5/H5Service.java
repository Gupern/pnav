package com.gupern.pnav.h5;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;

public interface H5Service {
    Object updateFundRecord(JSONObject dto);
    Object deleteFundRecord(JSONObject dto);

    Object queryFundInfo(JSONObject dto);

    Object updateOperationProfit(JSONObject dto);

    Object updateBuyFundUnv(JSONObject dto);

    Object deleteOperationProfit(JSONObject dto);

    Object updateSharesRunning(JSONObject dto);

    Object deleteSharesRunning(JSONObject dto);

    Object updateFundDate(JSONObject dto) throws ParseException;
}
