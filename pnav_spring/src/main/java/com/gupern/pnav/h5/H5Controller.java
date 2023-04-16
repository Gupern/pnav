package com.gupern.pnav.h5;

import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.common.bean.TokenValidator;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/h5")
public class H5Controller {

    private final H5ServiceImpl service;

    public H5Controller(H5ServiceImpl h5Service) {
        this.service = h5Service;
    }

    @RequestMapping(value = "/updateFundRecord", method = POST)
    @ResponseBody
    @TokenValidator
    public Object updateFundRecord(@RequestBody JSONObject dto) {
        return service.updateFundRecord(dto);
    }
//    @RequestMapping(value = "/deleteFundRecord", method = POST)
//    @ResponseBody
//    @TokenValidator
//    public Object deleteFundRecord(@RequestBody JSONObject dto) {
//        return service.deleteFundRecord(dto);
//    }
    @RequestMapping(value = "/queryFundInfo", method = POST)
    @ResponseBody
    @TokenValidator
    public Object queryFundInfo(@RequestBody JSONObject dto) {
        return service.queryFundInfo(dto);
    }

    @RequestMapping(value = "/updateOperationProfit", method = POST)
    @ResponseBody
    @TokenValidator
    public Object updateOperationProfit(@RequestBody JSONObject dto) {
        return service.updateOperationProfit(dto);
    }
    @RequestMapping(value = "/updateFundDate", method = POST)
    @ResponseBody
    @TokenValidator
    public Object updateFundDate(@RequestBody JSONObject dto) {
        return service.updateFundDate(dto);
    }
    @RequestMapping(value = "/updateBuyFundUnv", method = POST)
    @ResponseBody
    @TokenValidator
    public Object updateBuyFundUnv(@RequestBody JSONObject dto) {
        return service.updateBuyFundUnv(dto);
    }

    @RequestMapping(value = "/queryTodayStock", method = POST)
    @ResponseBody
    @TokenValidator
    public Object queryTodayStock(@RequestBody JSONObject dto) {
        return service.queryTodayStock(dto);
    }
//    @RequestMapping(value = "/deleteOperationProfit", method = POST)
//    @ResponseBody
//    @TokenValidator
//    public Object deleteOperationProfit(@RequestBody JSONObject dto) {
//        return service.deleteOperationProfit(dto);
//    }
//    @RequestMapping(value = "/updateSharesRunning", method = POST)
//    @ResponseBody
//    @TokenValidator
//    public Object updateSharesRunning(@RequestBody JSONObject dto) {
//        return service.updateSharesRunning(dto);
//    }
//    @RequestMapping(value = "/deleteSharesRunning", method = POST)
//    @ResponseBody
//    @TokenValidator
//    public Object deleteSharesRunning(@RequestBody JSONObject dto) {
//        return service.deleteSharesRunning(dto);
//    }
}
