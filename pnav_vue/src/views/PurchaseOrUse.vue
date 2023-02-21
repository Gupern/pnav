<template>
  <div class="page">
    <div class="header">
      <div class="backward">
        <van-icon name="arrow-left" size="x-large" @click="backward()" />
      </div>
      <img src="../assets/logo.png" />
    </div>
    <div class="footer">
      <p class="name">{{ this.name }}</p>
      <p class="description">
        {{ this.description }}
      </p>
      <div v-if="type === '0'">
        <div class="line">
          <p class="name">价格：</p>
          <p class="amount">￥{{ this.originPrice }}</p>
        </div>
        <div class="line">
          <p class="name">券后价：</p>
          <p class="amount">￥{{ this.reductedPrice }}</p>
        </div>
        <div class="line">
          <van-checkbox class="useCoupon" icon-size="small" checked-color="grey" shape="square" v-model="checked">使用{{
              this.reductionPrice
          }}元优惠券购买</van-checkbox>
        </div>
        <van-button class="button" @click="gotoExchange()">购买</van-button>
      </div>
      <div v-else>
        <van-button class="button" @click="gotoUse()">使用</van-button>
      </div>
    </div>
  </div>
</template>
<script>
import * as globalConstant from '../const/globalConstant.js'
import { toPay } from '@/utils/http';
export default {
  data() {
    return {
      type: 0, //0兑换, 1使用
      checked: true,
      originPrice: 100,
      reductionPrice: 99.99,
      reductedPrice: 0.01,
      name: "",
      description: "",
      clientId: "",
      commodityOrderId: "",
    };
  },
  methods: {
    init() {
      // 从url中获取参数
      this.type = this.$route.query.type;
      console.log(this.type)
      let token = this.$route.query.token;
      this.commodityOrderId = this.$route.query.commodityOrderId;
      console.log(token, this.commodityOrderId)
      let data = {
        commodityOrderId: this.commodityOrderId,
      }
      let headers = {
        'headers': {
          'Content-Type': 'application/json',
          'token': token,
        }
      }
      this.$http.post(globalConstant.getCommodityDetailByCommodityOrderIdUrl, data, headers).then(
        response => {
          console.log(response)
          let data = response.data.data
          this.originPrice = parseFloat(data.originPrice).toFixed(2)
          this.reductionPrice = parseFloat(data.reductionPrice).toFixed(2)
          this.name = this.type === 0 ? data.name1 : data.name2
          this.description = this.type === 0 ? data.description1 : data.description2
          console.log("name", this.name, this.description)
          this.reductedPrice = parseFloat(this.originPrice - this.reductionPrice).toFixed(2)
          this.clientId = data.clientId
        }, error => {
          if (error.body.code === "400") {
            this.$toast("token过期，请重新登录")
          }
          console.log(error)
        }
      )
    },
    /**
    * 测试请求后台接口
    */
    gotoExchange() {
      // clientId: 1及未，2龙腾
      if (this.clientId === '1') {
        console.log(this.commodityOrderId)
        this.exchangeJiwei(this.commodityOrderId)
      } else if (this.clientId === '2') {
        this.$toast("功能开发中，敬请期待")
      }
    },
    // 请求及未兑换接口
    exchangeJiwei(commodityOrderId) {
      // 从url中获取参数
      let token = this.$route.query.token;
      let data = {
        commodityOrderId: commodityOrderId,
      }
      let headers = {
        'headers': {
          'Content-Type': 'application/json',
          'token': token,
        }
      }
      this.$http.post(globalConstant.buyJiweiCommodityUrl, data, headers).then(
        response => {
          console.log(response)
          let payParam = response.data.data.data.payParam
          toPay(payParam);
        }, error => {
          if (error.body.code === "400") {
            this.$toast("token过期，请重新登录")
          }
          console.log(error)
        }
      )
    },
    // 去使用接口
    gotoUse() {
      // clientId: 1及未，2龙腾
      if (this.clientId === '1') {
        console.log(this.commodityOrderId)
        this.useJiweiCommodity(this.commodityOrderId)
      } else if (this.clientId === '2') {
        this.$toast("功能开发中，敬请期待")
      }
    },
    // 使用及未商品，跳转到行权H5
    useJiweiCommodity(commodityOrderId) {
      // 从url中获取参数
      let token = this.$route.query.token;
      let data = {
        commodityOrderId: commodityOrderId,
      }
      let headers = {
        'headers': {
          'Content-Type': 'application/json',
          'token': token,
        }
      }
      this.$http.post(globalConstant.gotoUseJiweiCommodityUrl, data, headers).then(
        response => {
          // TODO 获取url，进行跳转
          let url = response.data.data.url
          window.location.href = url
        }, error => {
          if (error.body.code === "400") {
            this.$toast("token过期，请重新登录")
          }
          console.log(error)
        }
      )
    },
    backward() {
      this.$router.go(-1); // 返回上一层
    },
  },
  // 生命周期
  beforeCreate() {

  },
  created() {
    this.init()
  },
  beforeMount() {

  },
  mounted() {
  },
  beforeUpdate() {

  },
  updated() {

  },
  beforeDestroy() {

  },
  destroyed() {

  }
};
</script>

<style scoped>
.page {
  max-width: 100%;
  background-color: #f9f9f9;
  font-size: medium;
  height: 100%;
  overflow: scroll;
  position: relative;
}

.header {
  width: 100%;
  height: 375px;
  background-color: black;
}

.footer {
  width: 100%;
  height: 334px;
  position: absolute;
  top: 334px;
  background-color: white;
  border-radius: 5%;
}

.footer .name {
  font-weight: bold;
  text-align: left;
  padding-left: 10px;
  padding-top: 10px;
  font-size: large;
}

.footer .description {
  padding-left: 10px;
  padding-right: 15px;
  text-align: left;
  color: grey;
  font-size: small;
}

.footer .line {
  margin-top: 15px;
  width: 100%;
  text-align: left;
  position: relative;
  float: left;

}

.footer .line .name {
  text-align: right;
  margin: 0;
  padding: 0;
  width: 69%;
  display: inline-block;
  font-size: medium;
  font-weight: bold;
}

.footer .line .amount {
  text-align: left;
  margin: 0;
  padding: 0;
  width: 30%;
  display: inline-block;
  color: #f6c74e;
}


.footer .line .useCoupon {
  margin: 0;
  padding: 0;
  padding-right: 30px;
  float: right;
  font-size: small;
  height: 20px;
}

.footer .button {
  background-color: #f5c74d;
  color: white;
  width: 84%;
  border-radius: 2%;
  position: absolute;
  top: 265px;
  left: 8%;
}

.backward {
  padding-left: 0.2rem;
  padding-top: 0.5rem;
  color: white;
  position: relative;
  text-align: left;
}
</style>
