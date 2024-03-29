package com.gupern.pnav.h5;

import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.common.bean.ResponseEnum;
import com.gupern.pnav.common.bean.ResultMsg;
import com.gupern.pnav.common.util.RedisUtil;
import com.gupern.pnav.common.util.UserRequest;
import com.gupern.pnav.h5.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class H5ServiceImpl implements H5Service {
    private static Logger log = LoggerFactory.getLogger(H5ServiceImpl.class);
    @Autowired
    private RepositoryFundRecord repositoryFundRecord;

    @Autowired
    private RepositoryOperationProfit repositoryOperationProfit;

    @Autowired
    private RepositorySharesRunning repositorySharesRunning;

    @Autowired
    private RedisUtil redisUtil;
    /*
     * @author: Gupern
     * @date: 2022/11/3 20:22
     * @description: 新增基金操作记录
     */
    public Object updateFundRecord(JSONObject dto) {
        String userId = UserRequest.getUserIdInToken(redisUtil);
        DaoFundRecord newFundRecord = new DaoFundRecord();
        newFundRecord.setUserId(userId);
        newFundRecord.setUnv(dto.getFloatValue("unv"));
        // 获取操作
        int operation = dto.getIntValue("operation");
        newFundRecord.setOperation(operation);
        // 0是买入，则计算份额
        if (operation == 0) {
            // 计算份额 基金是截取法，同花顺小数点两位后面的数字直接截取
            double shares = Math.floor(dto.getFloatValue("amount") / dto.getFloatValue("unv") * 100) / 100.0;
            newFundRecord.setShares((float) shares);
            newFundRecord.setAmount(dto.getFloatValue("amount"));
        } else if (operation == 1){ // 1是卖出，则计算金额
            double amount = Math.floor(dto.getFloatValue("shares") * dto.getFloatValue("unv")*100) / 100.0;
            newFundRecord.setAmount((float) amount);
            newFundRecord.setShares(dto.getFloatValue("shares"));
        }
        log.info(dto.toString());
        // 更新基金记录表
        DaoFundRecord resSaveFundRecord = repositoryFundRecord.save(newFundRecord);

        // 同步新增份额运营表
        if (operation==0) {
            DaoSharesRunning newSharesRunning = new DaoSharesRunning();
            newSharesRunning.setShares(newFundRecord.getShares());
            newSharesRunning.setUnv(newFundRecord.getUnv());
            newSharesRunning.setUserId(userId);
            newSharesRunning.setFundRecordId(resSaveFundRecord.getId());
            newSharesRunning.setSharesRemaining(newFundRecord.getShares());
            newSharesRunning.setPurchaseTime(new Date(System.currentTimeMillis()));
            repositorySharesRunning.save(newSharesRunning);
        } else if (operation==1) {
            // 判断是否含有份额运营表的ID
            int sharesRunningId = dto.getIntValue("sharesRunningId");
            if (sharesRunningId==0) {
                return ResultMsg.fail("400", "请求错误，未携带运营表ID");
            }
            DaoSharesRunning newSharesRunning = repositorySharesRunning.findById(sharesRunningId);
            // 计算还剩多少份额
            newSharesRunning.setSharesRemaining(newSharesRunning.getSharesRemaining() - dto.getFloatValue("shares"));
            repositorySharesRunning.save(newSharesRunning);

            // 卖出时，还需更新操作盈利表
            DaoOperationProfit newOperationProfit = new DaoOperationProfit();
            newOperationProfit.setShares(newFundRecord.getShares());
            // 卖出操作时基金记录表的unv就是unvSold
            newOperationProfit.setUnvSold(newFundRecord.getUnv());
            newOperationProfit.setUserId(userId);
            newOperationProfit.setServiceCharge(dto.getFloatValue("serviceCharge")); // 前端传入，默认手续费为0
            // 获取unv - 通过份额运营表找到该unv
            newOperationProfit.setUnv(newSharesRunning.getUnv());
            newOperationProfit.setFundRecordId(resSaveFundRecord.getId());
            double profit = Math.floor((newOperationProfit.getUnvSold() - newOperationProfit.getUnv()) * newFundRecord.getShares() * 100) / 100.0;
            newOperationProfit.setProfit((float) profit);
            repositoryOperationProfit.save(newOperationProfit);
        }
        JSONObject returnObj = new JSONObject();
        returnObj.put("msg", "success");
        returnObj.put("code", "200");
        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, returnObj);
    }

    /*
     * @author: Gupern
     * @date: 2022/3/15 20:22
     * @description: 删除基金操作记录
     */
    public Object deleteFundRecord(JSONObject dto) {
        int id = dto.getIntValue("id");
        String openid = dto.getString("openid");
        log.info("id:{}, openid:{}", id, openid);
        repositoryFundRecord.deleteLogically(id, openid);
        JSONObject returnObj = new JSONObject();
        returnObj.put("msg", "success");
        returnObj.put("code", "200");
        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, returnObj);
    }

    /*
     * @author: Gupern
     * @date: 2022/3/15 20:22
     * @description: 查询基金信息接口文档
     */
    public Object queryFundInfo(JSONObject dto) {
        String userId = UserRequest.getUserIdInToken(redisUtil);
        // 获取基金记录表所有数据
        List<DaoFundRecord> fundRecordList = repositoryFundRecord.findAllByUserId(userId);
        // 获取份额运营表
        List<DaoOperationProfit> operationProfitList = repositoryOperationProfit.findAllByUserId(userId);
        // 获取操作盈亏表
        List<DaoSharesRunning> sharesRunningList = repositorySharesRunning.findAllByUserId(userId);
        log.info("fundRecordList: {}", fundRecordList.toString());
        log.info("operationProfitList: {}", operationProfitList.toString());
        log.info("sharesRunningList: {}", sharesRunningList.toString());
        // 1. 计算出shares 总的持仓份额 = sum(基金记录表买入) - sum(基金记录表卖出)
        float shares = 0;
        for (DaoFundRecord item:fundRecordList) {
            if (item.getStatus()!=0) {
                continue;
            }
            if (item.getOperation()==0) {
                shares += item.getShares();
            } else {
                shares -= item.getShares();
            }
        }
        log.info("shares: {}", shares);

        // 2. 计算出amount 持仓金额 = sum(买入净值 * 剩余份额)
        float amount = 0;
        // 计算出零成本份额
        float zeroShares = 0;
        for (DaoSharesRunning item:sharesRunningList) {
            amount += item.getUnv() * item.getSharesRemaining();
            // 判断时间是否大于7天
            Date buyDate  = item.getCreatedTime();
            int passDays = Math.abs((int) ((new Date(System.currentTimeMillis()).getTime() - buyDate.getTime()) / (1000 * 3600 * 24)));
            if (passDays >=7 ) {
                zeroShares += item.getShares();
            }
        }
        log.info("amount: {}", amount);

        // 3. 计算出profit 已获利
        float profit = 0;
        for (DaoOperationProfit item:operationProfitList) {
            profit += item.getProfit();
        }
        log.info("profit: {}", profit);


        JSONObject returnObj = new JSONObject();
        returnObj.put("shares", shares);
        returnObj.put("amount", amount);
        returnObj.put("profit", profit);
        returnObj.put("zeroShares", zeroShares);
        returnObj.put("fundRecordList", fundRecordList);
        returnObj.put("operationProfitList", operationProfitList);
        returnObj.put("sharesRunningList", sharesRunningList);

        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, returnObj);
    }
    /*
     * @author: Gupern
     * @date: 2022/11/6 16:00
     * @description:
     */
    public Object updateOperationProfit(JSONObject dto) {
        // 获取userId
        String userId = UserRequest.getUserIdInToken(redisUtil);
        log.info(dto.toString());
        int operationProfitId = dto.getIntValue("operationProfitId");
        // 查询该操作盈利表数据
        DaoOperationProfit operationProfit = repositoryOperationProfit.findById(operationProfitId);
        // 更新卖出净值
        operationProfit.setUnvSold(dto.getFloatValue("unv"));
        // 更新盈利
        double profit =
                Math.floor((operationProfit.getUnvSold() - operationProfit.getUnv()) * operationProfit.getShares() * 100) / 100.0;
        operationProfit.setProfit((float) profit);
        repositoryOperationProfit.save(operationProfit);

        // 同步更新基金操作记录表
        int fundRecordId = operationProfit.getFundRecordId();
        DaoFundRecord fundRecord = repositoryFundRecord.findById(fundRecordId);
        // 更新卖出净值
        fundRecord.setUnv(operationProfit.getUnvSold());
        // 更新卖出金额
        double amount = Math.floor(fundRecord.getShares()* fundRecord.getUnv()*100) / 100.0;
        fundRecord.setAmount((float) amount);
        repositoryFundRecord.save(fundRecord);

        JSONObject returnObj = new JSONObject();
        returnObj.put("msg", "success");
        returnObj.put("code", "200");
        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, returnObj);
    }
    /*
     * @author: Gupern
     * @date: 2022/11/6 16:00
     * @description:
     */
    public Object deleteOperationProfit(JSONObject dto) {
        int id = dto.getIntValue("id");
        log.info("id:{}", id);
        repositoryOperationProfit.deleteById(id);
        JSONObject returnObj = new JSONObject();
        returnObj.put("msg", "success");
        returnObj.put("code", "200");
        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, returnObj);
    }
    /*
     * @author: Gupern
     * @date: 2022/11/6 16:00
     * @description:
     */
    public Object updateSharesRunning(JSONObject dto) {
        log.info(dto.toString());
        repositorySharesRunning.save(JSONObject.toJavaObject(dto, DaoSharesRunning.class));
        JSONObject returnObj = new JSONObject();
        returnObj.put("msg", "success");
        returnObj.put("code", "200");
        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, returnObj);
    }
    /*
     * @author: Gupern
     * @date: 2022/11/6 16:00
     * @description:
     */
    public Object deleteSharesRunning(JSONObject dto) {
        int id = dto.getIntValue("id");
        log.info("id:{}", id);
        repositorySharesRunning.deleteById(id);
        JSONObject returnObj = new JSONObject();
        returnObj.put("msg", "success");
        returnObj.put("code", "200");
        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, returnObj);
    }

    @Override
    public Object updateFundDate(JSONObject dto) {
        log.info(dto.toString());
        int fundRecordId = dto.getIntValue("fundRecordId");
        // 查询该数据
        DaoFundRecord fundRecord = repositoryFundRecord.findById(fundRecordId);
        // 更新数据
        Date buyDate = Date.valueOf(dto.getString("buyDate"));
        fundRecord.setCreatedTime(buyDate);
        repositoryFundRecord.save(fundRecord);

        // 如果该基金是买的基金，则同步更新份额运营表
        if (fundRecord.getOperation()==0) {
            DaoSharesRunning sharesRunning = repositorySharesRunning.findByFundRecordId(fundRecordId);
            sharesRunning.setPurchaseTime(buyDate);
            repositorySharesRunning.save(sharesRunning);
        }

        JSONObject returnObj = new JSONObject();
        returnObj.put("msg", "success");
        returnObj.put("code", "200");
        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, returnObj);
    }
}
