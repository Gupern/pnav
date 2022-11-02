## 1. 新增/修改基金操作记录接口文档
### 接口说明
新增基金操作记录，输入id、基金估值、购买金额、日期即可
接口会自动计算单位净值。
若不输入id，则为创建新记录。
### 接收参数
```json
{
  "openid": "会根据sessionKey进行权限判断",
  "id":1,
  "net_value_purchased":"基金估值",
  "amount_purchased":"购买金额",
  "operation_date":"操作日期",
  "operation": 0
}
```
### 返回参数
```json
{
  "code": "200",
  "msg": "请求处理成功",
  "data": {
  }
}
```

## 2. 删除基金操作记录接口文档
### 接口说明
删除基金操作记录，输入id
### 接收参数
```json
{
"openid": "会根据sessionKey进行权限判断",
"id":1
}
```

### 返回参数
```json
{
  "code": "200",
  "msg": "请求处理成功",
  "data": {
  }
}
```

## 3. 查询基金信息接口文档
### 接口说明
小程序权限控制
### 接收参数
openid，会根据sessionKey进行权限判断
### 返回参数
```json
{
  "code": "200",
  "msg": "请求处理成功",
  "data":{
    "etfChange": "创业板涨跌幅",
    "todayFUNVPredict": "今日基金单位净值预计",
    "positionShares": "持仓份额",
    "positionAmount": "持仓金额",
    "earning": "已获利金额，不算浮亏",
    "earningRatio": "已获利百分比，不算浮亏",
    "operationRecord": {
      "date": "操作时间",
      "amount": "金额",
      "shares": "份数",
      "unv": "单位净值",
      "status": "状态：0-红色，卖出需手续费；1-黄色，卖出不需手续费但不盈利；2-绿色，持有超过7天可盈利；3-灰色，已套利卖出"
    }
  }
}
```