package com.gupern.pnav.wechat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.common.bean.Constant;
import com.gupern.pnav.common.bean.ResponseEnum;
import com.gupern.pnav.common.bean.ResultMsg;
import com.gupern.pnav.common.util.CryptoUtil;
import com.gupern.pnav.wechat.bean.*;
import com.gupern.pnav.wechat.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

import static com.gupern.pnav.wechat.util.WechatUtil.*;

@Service
public class WechatServiceImpl implements WechatService {
    private static Logger log = LoggerFactory.getLogger(WechatServiceImpl.class);

    // 公众号appid 从properties文件中获取
    @Value("${wechat.miniprogram.appid}")
    private String appId = "appId";
    // 公众号appsecert
    @Value("${wechat.miniprogram.appsecret.encoded}")
    private String appSecretEncoded = "asdfasdfasdf";

    // 与接口配置信息中的Token要一致
    @Value("${wechat.miniprogram.token}")
    private String token = "pnav007";

    @Value("${wechat.miniprogram.push.encoding.aes.key}")
    private String encodingAesKey = "";

    // This means to get the bean called userRepository Which is auto-generated by Spring, we will use it to handle the data
    @Autowired
    private RepositorySubscribeMsg repositorySubscribeMsg;
    @Autowired
    private RepositoryTaskInfoMsg repositoryTaskInfoMsg;

    @Autowired
    private RepositoryFundRecord repositoryFundRecord;

    @Autowired
    private RepositoryOperationProfit repositoryOperationProfit;

    @Autowired
    private RepositorySharesRunning repositorySharesRunning;

    public Object sayHelloWorld() {
        return "hello world";
    }

    /*
     * @author: Gupern
     * @date: 2022/3/6 2:26
     * @description: 获取access_token TODO 不可暴露到互联网，只开发测试用
     */
    public Object getAccessToken() {
        try {
            return getMiniProgramAccessToken(appId, CryptoUtil.getInstance(null).Base64Decode(appSecretEncoded));
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMsg.fail(ResponseEnum.REQUEST_FAILED.getCode(), ResponseEnum.REQUEST_FAILED.getMsg());
        }
    }

    /*
     * @author: Gupern
     * @date: 2022/3/6 2:27
     * @description: 给小程序前端调用，获取openid
     */
    public Object getSession(JSONObject dto) {
        log.info("dto:{}", dto);
        String code = dto.getString("code");
        log.info("dto.code:{}", code);
        try {
            return getMiniProgramSession(code, appId, CryptoUtil.getInstance(null).Base64Decode(appSecretEncoded));
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMsg.fail(ResponseEnum.REQUEST_FAILED.getCode(), ResponseEnum.REQUEST_FAILED.getMsg());
        }
    }

    /*
     * @author: Gupern
     * @date: 2022/3/5 18:41
     * @description: 微信小程序推送消息
     */
    public Object miniprogramPush(HttpServletRequest request, JSONObject dto) {
        try {
            String echostr = request.getParameter("echostr");
            String nonce = request.getParameter("nonce");
            String timestamp = request.getParameter("timestamp");
            String signature = request.getParameter("signature");
            String msgSignature = request.getParameter("msg_signature");
            log.info("nonce:{}, timestamp:{}, signature:{}, encodingAesKey:{}, msg_signature:{}", nonce, timestamp, signature, encodingAesKey, msgSignature);
            // 解密
            WechatUtil wechatUtil = new WechatUtil(token, encodingAesKey, appId);
            JSONObject planeJson = JSONObject.parseObject(wechatUtil.decryptMsg(msgSignature, timestamp, nonce, dto));
            log.info("planeJson:{}", planeJson.toJSONString());
            // 接收入库
            DaoSubscribeMsg subscribeMsg = new DaoSubscribeMsg();
            subscribeMsg.setToUserName(planeJson.getString("ToUserName"));
            subscribeMsg.setFromUserName(planeJson.getString("FromUserName"));
            subscribeMsg.setCreateTime(planeJson.getString("CreateTime"));
            subscribeMsg.setEvent(planeJson.getString("Event"));
            subscribeMsg.setMsgType(planeJson.getString("MsgType"));
            subscribeMsg.setEvent(planeJson.getString("Event"));
            JSONObject subscribeJson = planeJson.getJSONObject("List");
            // 只有一个模板的时候，不是[]，直接获取Template
            if (!subscribeJson.getString("TemplateId").isEmpty()) {
                subscribeMsg.setTemplateId(subscribeJson.getString("TemplateId"));
                subscribeMsg.setSubscribeStatusString(subscribeJson.getString("SubscribeStatusString"));
                subscribeMsg.setPopupScene(subscribeJson.getString("PopupScene"));
                repositorySubscribeMsg.save(subscribeMsg);
            } else {
                for (JSONObject item : Collections.singletonList(subscribeJson)) {
                    log.info("item:{}", item);
                    subscribeMsg.setTemplateId(item.getString("TemplateId"));
                    subscribeMsg.setSubscribeStatusString(item.getString("SubscribeStatusString"));
                    subscribeMsg.setPopupScene(item.getString("PopupScene"));
                    repositorySubscribeMsg.save(subscribeMsg);
                }
            }
            /*
             接入验证时：若确认此次 GET 请求来自微信服务器，请原样返回 echostr 参数内容，则接入生效，成为开发者成功，否则接入失败。
             return echostr;
             接入成功后，服务器收到请求必须做出下述回复，这样微信服务器才不会对此作任何处理，并且不会发起重试，否则，将出现严重的错误提示。详见下面说明：
             1. 直接回复success（推荐方式）
             2. 直接回复空串（指字节长度为0的空字符串，而不是结构体中content字段的内容为空）
             3. 若接口文档有指定返回内容，应按文档说明返回
             */
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail(Constant.RESPONSE_FAILED_CODE, Constant.RESPONSE_WECHAT_CHECK_FAILED_MSG);
        }
    }

    /*
     * @author: Gupern
     * @date: 2022/3/13 14:32
     * @description: 小程序前端调用，获取一个任务
     */
    public Object getTask(JSONObject dto) {
        List<JSONObject> allTasksList = getTaskListByOpenidAndTaskId(dto);
        // 如果返回一个，一个元素的列表的随机也是该元素
        JSONObject task = WechatUtil.getRandomTask(allTasksList);
        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, task);
    }

    /*
     * @author: Gupern
     * @date: 2022/3/15 20:22
     * @description: 接收openid，返回用户的项目信息
     * [{
            "projectName": "project name",
            "taskList": [{
                "name": "task name",
                "count": 3
            }, {
                "name": "夸人",
                "count": 4
            }]

        }, {

            "projectName": "project name",
            "taskList": [{
                "name": "task name",
                "count": 3
            }, {
                "name": "夸人",
                "count": 4
            }]

        }]
     */
    public Object getPersonalProjectInfo(JSONObject dto) {

        List<JSONObject> allTasksList = getTaskListByOpenidAndTaskId(dto);
        ;

        // 建立一个map 与返回的格式不同，便于查找projectName
        JSONObject projectInfoTmp = new JSONObject();
        for (JSONObject item : allTasksList) {
            String project = item.getString("project");
            String task = item.getString("task");
            String count = item.getString("count");
            String changeCount = item.getString("change_count");

            JSONArray itemOrigin = projectInfoTmp.getJSONArray(project);
            JSONArray itemTmp = new JSONArray();
            // 如果已经包含该project，则添加
            if (itemOrigin != null) {
                itemTmp = itemOrigin;
                log.info("itemOrigin is not null:{}", itemTmp.toString());
            }
            JSONObject tmp = new JSONObject();
            tmp.put("name", task);
            tmp.put("count", count);
            tmp.put("changeCount", changeCount);
            itemTmp.add(tmp);
            // 更新数据
            projectInfoTmp.put(project, itemTmp);
        }
        log.info("projectInfoTemp:{}", projectInfoTmp.toString());

        // 拼装数据
        JSONArray returnObj = new JSONArray();

        for (Map.Entry<String, Object> stringObjectEntry : projectInfoTmp.entrySet()) {
            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();
            log.info(key + "----" + value);
            JSONObject tmp = new JSONObject();
            tmp.put("projectName", key);
            tmp.put("taskList", value);
            returnObj.add(tmp);
        }
        log.info("returnObj:{}", returnObj);
        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, returnObj);
    }

    /*
     * @author: Gupern
     * @date: 2022/3/16 21:18
     * @description: 通过openid和taskId获取任务
     * 如果openid为空，则返回vistor的
     * 如果taskId不为空，则返回一个
     * 如果taskId为空，则返回全部
     */
    private List<JSONObject> getTaskListByOpenidAndTaskId(JSONObject dto) {
        String openid = dto.getString("openid");
        String taskId = dto.getString("taskId");

        List<JSONObject> allTasksList;
        if (taskId == null || taskId.equals("")) {
            log.info("findAllTasksByOpenid");
            allTasksList = repositoryTaskInfoMsg.findAllTasksByOpenid(openid);
        } else {
            log.info("findAllTasksByOpenidAndTaskId");
            allTasksList = repositoryTaskInfoMsg.findAllTasksByOpenidAndTaskId(openid, taskId);
        }
        log.info("openid:{}, taskId:{}, allTasksList:{}", openid, taskId, allTasksList);
        return allTasksList;
    }

    /*
     * @author: Gupern
     * @date: 2022/3/16 0:33
     * @description: 添加task信息
     */
    public Object updatePersonalProjectInfo(JSONObject dto) {
        String openid = dto.getString("openid");
        String project = dto.getString("project");
        String task = dto.getString("task");
        DaoTaskInfo daoTaskInfo = new DaoTaskInfo();
        daoTaskInfo.setOpenid(openid);
        daoTaskInfo.setProject(project);
        daoTaskInfo.setTask(task);
        repositoryTaskInfoMsg.save(daoTaskInfo);
        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, "success");
    }

    /*
     * @author: Gupern
     * @date: 2022/3/16 23:11
     * @description: 完成任务，count + 1，返回新任务
     */
    @Transactional
    public Object finishTask(JSONObject dto) {
        String taskId = dto.getString("taskId");
        String openid = dto.getString("openid");
        List<JSONObject> tmpList = repositoryTaskInfoMsg.findAllTasksByOpenidAndTaskId(openid, taskId);
        JSONObject daoTaskInfo = tmpList.get(0);
        daoTaskInfo.put("count", daoTaskInfo.getLongValue("count") + 1);
        repositoryTaskInfoMsg.save(JSONObject.toJavaObject(daoTaskInfo, DaoTaskInfo.class));

        List<JSONObject> allTasksList = repositoryTaskInfoMsg.findAllTasksByOpenid(openid);
        JSONObject returnObj = getRandomTask(allTasksList);
        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, returnObj);
    }

    /*
     * @author: Gupern
     * @date: 2022/3/16 23:12
     * @description: 更换任务，change_count + 1， 返回新任务
     */
    public Object changeTask(JSONObject dto) {
        String taskId = dto.getString("taskId");
        String openid = dto.getString("openid");
        List<JSONObject> tmpList = repositoryTaskInfoMsg.findAllTasksByOpenidAndTaskId(openid, taskId);
        JSONObject daoTaskInfo = tmpList.get(0);
        daoTaskInfo.put("change_count", daoTaskInfo.getLongValue("change_count") + 1);
        repositoryTaskInfoMsg.save(JSONObject.toJavaObject(daoTaskInfo, DaoTaskInfo.class));
        List<JSONObject> allTasksList = repositoryTaskInfoMsg.findAllTasksByOpenid(openid);
        JSONObject returnObj = getRandomTask(allTasksList);
        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, returnObj);
    }

    /*
     * @author: Gupern
     * @date: 2022/11/3 20:22
     * @description: 新增基金操作记录
     */
    public Object updateFundRecord(JSONObject dto) {
        // 获取操作
        int operation = dto.getIntValue("operation");
        // 如果是买入，则计算份额
        if (operation == 0) {
            // 计算份额 基金是截取法，同花顺小数点两位后面的数字直接截取
            double shares = Math.floor(dto.getFloatValue("amount") / dto.getFloatValue("unv") * 100) / 100.0;
            dto.put("shares", shares);
        } else { // 如果是卖出，则计算金额
            double amount = Math.floor(dto.getFloatValue("shares") * dto.getFloatValue("unv")*100) / 100.0;
            dto.put("amount", amount);
        }
        log.info(dto.toString());

        JSONObject returnObj = new JSONObject();
        repositoryFundRecord.save(JSONObject.toJavaObject(dto, DaoFundRecord.class));
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
        String openid = dto.getString("openid");
        // 筛选出所有status=0的有效基金买卖记录
        // 获取基金记录表所有数据 TODO 增加openid
        List<DaoFundRecord> fundRecordList = repositoryFundRecord.findAllByOpenid(openid);
        // 获取份额运营表
        List<DaoOperationProfit> operationProfitList = repositoryOperationProfit.findAllByOpenid(openid);
        // 获取操作盈亏表
        List<DaoSharesRunning> sharesRunningList = repositorySharesRunning.findAllByOpenid(openid);
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
        for (DaoSharesRunning item:sharesRunningList) {
            amount += item.getUnv() * item.getSharesRemaining();
        }
        log.info("amount: {}", amount);

        // 3. 计算出profit 已获利
        float profit = 0;
        for (DaoOperationProfit item:operationProfitList) {
            profit += item.getProfit();
        }
        log.info("profit: {}", profit);


        // 4. TODO 计算出零成本份额
        float zeroShares = 0;
        // 5. TODO 计算出估值
        // 6. TODO 计算出推荐

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
        log.info(dto.toString());
        repositoryOperationProfit.save(JSONObject.toJavaObject(dto, DaoOperationProfit.class));
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
//  新建接口时的template
//    /*
//     * @author: Gupern
//     * @date: 2022/11/6 16:00
//     * @description:
//     */
//    public Object getPersonalProjectInfo(JSONObject dto) {
//        JSONObject returnObj = new JSONObject();
//        returnObj.put("msg", "success");
//        returnObj.put("code", "200");
//        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, returnObj);
//    }
}
