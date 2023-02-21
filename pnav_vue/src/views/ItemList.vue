<template>
  <div class="page">
    <div class="part1">
      <!-- 0. 背景 -->
      <div class="headPic">
        <!-- 1. 返回按钮 -->
        <div class="backward">
          <van-icon name="arrow-left" size="x-large" @click="backward()" />
        </div>
      </div>
      <!-- 2. 头像 -->
      <div>
        <img class="avator" src="../assets/defaultAvator.png">
      </div>
      <!-- 3. 字段信息 需在父元素增加除static外的position值，如relative，否则会随着滑动而滑动-->
      <div class="userInfo">
        <div class="divLine1">
          <p class="userInfoTextLeft">用户名：小神仙 </p>
          <p class="userInfoTextRight">ID：1123123</p>
        </div>
        <div class="divLine2">
          <p class="userInfoTextLeft">未使用券包: 18 </p>
          <p class="userInfoTextRight">电话：18888888888</p>
        </div>
      </div>

      <!-- 优惠券描述 -->
      <div class="couponIntro">
        <p class="couponName">
          {{ packageName }}：
        </p>
        <p class="couponDetail">
          有效期：{{ packageEffectiveStartTime }} - {{ packageEffectiveEndTime }}
        </p>
      </div>
    </div>
    <!-- 分割区域 -->
    <div class="divideZone"></div>

    <!-- 券列表和商品列表切换-->
    <van-tabs v-model:active="active" @click-tab="onClickTab">
      <van-tab v-for="tabInfo in tabList" :title="tabInfo.tabName" :key="tabInfo.tabName">
        <div class="itemList" v-if="active === 0">
          <div v-for="(itemStatus0, index) in commodityInfoStatus0List" :key="index">
            <div class="item">
              <img class="img" src="../assets/3.png">
              <div class="itemRight">
                <h2 class="name">{{ itemStatus0.commodityName1 }}</h2>
                <div class="itemButton">
                  <van-button class="detail"
                    @click="gotoPurchaseOrUsePage(itemStatus0.commodityOrderId, 0)">查看详情</van-button>
                  <van-button class="toUse"
                    @click="gotoExchange(itemStatus0.commodityOrderId, itemStatus0.clientId)">去兑换</van-button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="itemList">
          <div v-for="(itemStatus2, index) in commodityInfoStatus2List" :key="index">
            <div class="item">
              <img class="img" src="../assets/4.png">
              <div class="itemRight">
                <h2 class="name">{{ itemStatus2.commodityName2 }}</h2>
                <div class="itemButton">
                  <van-button class="detail"
                    @click="gotoPurchaseOrUsePage(itemStatus2.commodityOrderId, 1)">查看详情</van-button>
                  <van-button class="toUse"
                    @click="gotoUse(itemStatus2.commodityOrderId, itemStatus2.clientId)">去使用</van-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </van-tab>
    </van-tabs>

    <div class="nomore">没有更多了，看看别的吧</div>
  </div>
</template>
<script>
import 'swiper/dist/js/swiper'
import 'swiper/dist/css/swiper.css'
import * as globalConstant from '../const/globalConstant.js'
import { toPay } from '@/utils/http';
export default {
  data() {
    return {
      isShowDelOrderPopup: false, // 删除订单弹窗
      tabList: [{ "tabName": "券列表" },
      { "tabName": "商品列表" }],
      active: 0, // 0券列表， 1商品列表
      packageName: "",
      packageEffectiveStartTime: "",
      packageEffectiveEndTime: "",
      packageOrderId: "",
      commodityInfoJsonList: [],
      commodityInfoStatus0List: [],
      commodityInfoStatus2List: [],
    };
  },

  methods: {
    onClickTab(title) {
      this.active = 1 ? this.active === 0 : 0;
      console.log("tetset")
      showToast(title);
    },
    backward() {
      this.$router.go(-1); // 返回上一层
    },
    // 跳转到详情页 type: 0兑换，1使用
    gotoPurchaseOrUsePage(commodityOrderId, type) {
      console.log("type:", type)
      let token = this.$route.query.token;
      this.$router.push("/purchaseOrUse?commodityOrderId=" + commodityOrderId + "&type=" + type + "&token=" + token);
    },
    // 兑换
    gotoExchange(commodityOrderId, clientId) {
      // clientId: 1及未，2龙腾
      if (clientId === '1') {
        console.log(commodityOrderId)
        this.exchangeJiwei(commodityOrderId)
      } else if (clientId === '2') {
        this.$toast("功能开发中，敬请期待")
      }
    },
    // 请求及未接口
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
    gotoUse(commodityOrderId, clientId) {
      // clientId: 1及未，2龙腾
      if (clientId === '1') {
        console.log(commodityOrderId)
        this.useJiweiCommodity(commodityOrderId)
      } else if (clientId === '2') {
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

    init() {
      // 从url中获取参数
      let token = this.$route.query.token;
      let packageOrderId = this.$route.query.orderId;
      console.log(token, packageOrderId)

      let data = {
        packageOrderId: packageOrderId,
      }
      let headers = {
        'headers': {
          'Content-Type': 'application/json',
          'token': token,
        }
      }
      this.$http.post(globalConstant.getCommodityListByPackageOrderIdUrl, data, headers).then(
        response => {
          console.log(response)
          this.commodityInfoJsonList = response.data.data.commodityInfoJsonList
          this.commodityInfoJsonList.forEach(e => {
            if (e.orderStatus === 0) {
              this.commodityInfoStatus0List.push(e)
            } else if (e.orderStatus === 2) {
              this.commodityInfoStatus2List.push(e)
            }
          });
          console.log("0", this.commodityInfoStatus0List)
          console.log("2", this.commodityInfoStatus2List)
          this.packageName = response.data.data.packageName
          this.packageEffectiveStartTime = response.data.data.effectiveStartTime
          this.packageEffectiveEndTime = response.data.data.effectiveEndTime
          this.packageOrderId = response.data.data.packageOrderId
          console.log(this.packageEffectiveEndTime, this.packageEffectiveStartTime)
        }, error => {
          if (error.body.code === "400") {
            this.$toast("token过期，请重新登录")
          }
          console.log(error)
        }
      )
    }
  },
  // 生命周期
  setup() {
  },
  beforeCreate() {

  },
  created() {
    this.init();
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

.headPic {
  display: block;
  margin: 0;
  padding: 0;
  width: 100%;
  height: 4.18667rem;
  background-color: #fccc1e;
  border-bottom-right-radius: 20%;
  border-bottom-left-radius: 20%;
}

.userInfo {
  /* margin-top: 9.06rem; */
  width: 93.3vw;
  height: 21.5vw;
  position: absolute;
  margin: 0;
  padding: 0;
  background: white;
  z-index: 1;
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  /* top: 4.53rem; */
  top: 2.053rem;
  left: 3.3vw;
  box-shadow: 0 0 2px grey;
}

.avator {
  margin: 0;
  padding: 0;
  z-index: 2;
  width: 1.73rem;
  height: 1.73rem;
  position: absolute;
  top: 1.1863rem;
  left: 41.466vw;
}

.userInfoTextLeft {
  float: left;
  margin: 0;
  margin-left: 2vw;
}

.userInfoTextRight {
  float: right;
  margin: 0;
  margin-right: 2vw;
}

.divLine1 {
  margin-top: 4vw;
  width: 100%;
  height: 30%;
}

.divLine2 {
  margin-top: 3vw;
  width: 100%;
  height: 30%;
}

.packageList {
  width: 100%;
  background-color: #f9f9f9;
  margin: 0;
  padding: 0;
}

.packageItem {
  margin: 0;
  padding: 0;
}

.packageName {
  margin: 0;
  padding: 0;
  text-align: left;
  margin-left: 5.3vw;
  margin-top: 2vw;
  width: 100%;
}

.packageIntro {
  margin: 0;
  padding: 0;
  margin-top: 2vw;
  margin-left: 10vw;
  color: grey;
  text-align: left;
  width: 100%;
  font-size: small;
}

.swiper-container {
  /* margin-top: 1rem; */
  height: 4.453rem;

}

.swiper-slide {
  width: auto;
  height: 3.353rem;
  margin-top: 1rem;
  margin-left: 0.1rem;
  /*根据内容调整宽度*/
}

.slidePic {
  margin: 0;
  padding: 0;
  width: 1.6rem;
  height: 1.6rem;
}

.slideText {
  font-size: small;
}

.nomore {
  position: relative;
  color: grey;
  margin-bottom: 1rem;
  margin-top: 1rem;
  font-size: small;
}

.backward {
  padding-left: 0.2rem;
  padding-top: 0.5rem;
  color: white;
  position: relative;
  text-align: left;
}

.part1 {
  height: 6.5rem;
  background-color: white;
}

.couponIntro {
  width: 337.5px;
  margin-left: 0.5rem;
  margin-right: 0.5rem;
  border: 0.5px solid #fccc1e;
  height: 63.5px;
  position: absolute;
  top: 170px;
}

.couponName {
  margin: 0;
  padding: 0;
  margin-top: 0.1rem;
  margin-left: 0.2rem;
  text-align: left;
  font-weight: bold;
}

.couponDetail {
  margin: 0;
  padding: 0;
  margin-top: 0.2rem;
  text-align: right;
  margin-right: 0.2rem;
}

.divideZone {
  height: 0.3rem;
}

.item {
  margin: 0;
  margin-top: 0.3rem;
  width: 100%;
  height: 103px;

}

.item .img {
  /* margin: 0; */
  /* padding: 0; */
  margin-left: 12.5px;
  width: 100px;
  height: 103px;
  float: left;
}

.item .itemRight {
  /* margin: 0; */
  /* padding: 0; */
  margin-right: 12.5px;
  background-color: white;
  width: 250px;
  float: left;
  border-radius: 5%;
  height: 103px;
}

.item .name {
  font-size: medium;
  text-align: left;
  padding-left: 12.5px;
}

.item .itemButton {
  margin-top: 25px;
  height: 30px;
  position: relative;

}

.item .detail {
  float: left;
  margin-left: 0.5rem;
  color: #48a995;
  border-color: white;
  height: 30px;
}

.item .toUse {
  float: right;
  margin-right: 0.5rem;
  color: #48a995;
  border-color: white;
  background-color: #e9f7f5;
  border-radius: 40%;
  height: 30px;
}
</style>
