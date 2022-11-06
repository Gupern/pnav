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
    "shares": "持仓份额",
    "zeroShares": "零成本份额",
    "amount": "持仓金额",
    "profit": "已获利金额，不算浮亏",
    "fundRecordList": [{
      "id": 1,
      "date": "2022-11-06",
      "amount": 100,
      "unv": 0.8878,
      "shares": 22.21,
      "operation": 0
    }],
    "operationProfitList": [{}],
    "sharesRunningList": [{}]
  }
}
```