<template>
  <div class="overview">
    <van-nav-bar title="基金投资AI助手" left-text="返回" left-arrow @click-left="backward()" />
    <van-row class="head" justify="center">
      <van-col class="solidVanCol" span="6">持仓份额</van-col>
      <van-col class="solidVanCol" span="6">成本金额</van-col>
      <van-col class="solidVanCol" span="6">已获利</van-col>
      <van-col class="solidVanCol" span="6">零成本份额</van-col>
    </van-row>
    <van-row class="body" justify="center">
      <van-col class="solidVanCol" span="6">{{this.shares}}</van-col>
      <van-col class="solidVanCol" span="6">{{this.amount}}</van-col>
      <van-col class="solidVanCol" span="6">{{this.profit}}</van-col>
      <van-col class="solidVanCol" span="6">{{this.zeroShares}}</van-col>
    </van-row>
    <!-- <van-row class="head" justify="center">
      <van-col span="6">前日指数</van-col>
      <van-col span="6">预估指数</van-col>
      <van-col span="6">前日净值</van-col>
      <van-col span="6">预估净值</van-col>
    </van-row>
    <van-row class="body" justify="center">
      <van-col span="6">2344.23</van-col>
      <van-col span="6">2400.32</van-col>
      <van-col span="6">0.8633</van-col>
      <van-col span="6">0.8733</van-col>
    </van-row> -->
    <van-row class="body" justify="center">
      <van-col span="12">
        <van-button plain hairline type="default" size="large" @click="init">刷新</van-button>
      </van-col>
      <van-col span="12">
        <van-button plain hairline type="default" size="large" @click="clickBuyFundButton">买入
        </van-button>
      </van-col>
    </van-row>
    <!-- 添加切换栏 -->
    <van-tabs v-model="active">
      <van-tab title="份额运营">
        <van-row class="head" justify="center">
          <van-col class="solidVanCol" span="6">日期</van-col>
          <van-col class="solidVanCol" span="5">买入净值</van-col>
          <van-col class="solidVanCol" span="4">份额</van-col>
          <van-col class="solidVanCol" span="5">剩余份额</van-col>
          <van-col class="solidVanCol" span="4">无成本</van-col>
        </van-row>
        <van-row v-for="item in sharesRunningList" :key="item.id" class="body" justify="center">
          <van-swipe-cell>
            <van-col class="solidVanCol" span="6">{{item.purchaseTime}}</van-col>
            <van-col class="solidVanCol" span="5">{{item.unv}}</van-col>
            <van-col class="solidVanCol" span="4">{{item.shares}}</van-col>
            <van-col class="solidVanCol" span="5">{{item.sharesRemaining}}</van-col>
            <van-col class="solidVanCol" span="4">{{item.zeroCost}}</van-col>
            <template #right>
              <van-button class="solidVanCol" type="warning" text="卖出份额"
                @click="clickSellFundButton(item)" />
            </template>
          </van-swipe-cell>
        </van-row>
      </van-tab>
      <van-tab title="操作盈亏">
        <van-row class="head" justify="center">
          <van-col class="solidVanCol" span="6">日期</van-col>
          <van-col class="solidVanCol" span="4">份额</van-col>
          <van-col class="solidVanCol" span="5">买入净值</van-col>
          <van-col class="solidVanCol" span="5">卖出净值</van-col>
          <van-col class="solidVanCol" span="4">盈利</van-col>
        </van-row>
        <van-row v-for="item in operationProfitList" :key="item.id" class="body" justify="center">
          <van-swipe-cell>
            <van-col class="solidVanCol" span="6">{{item.createdTime}}</van-col>
            <van-col class="solidVanCol" span="4">{{item.shares}}</van-col>
            <van-col class="solidVanCol" span="5">{{item.unv}}</van-col>
            <van-col class="solidVanCol" span="5">{{item.unvSold}}</van-col>
            <van-col class="solidVanCol" span="4">{{item.profit}}</van-col>
            <template #right>
              <van-button class="solidVanCol" type="warning" square text="修改卖出基金净值"
                @click="clickModifySellFundUnvButton(item)" />
            </template>
          </van-swipe-cell>
        </van-row>
      </van-tab>
      <van-tab title="基金记录">
        <van-row class="head" justify="center">
          <van-col class="solidVanCol" span="6">日期</van-col>
          <van-col class="solidVanCol" span="5">0买1卖</van-col>
          <van-col class="solidVanCol" span="5">单位净值</van-col>
          <van-col class="solidVanCol" span="4">金额</van-col>
          <van-col class="solidVanCol" span="4">份额</van-col>
        </van-row>
        <van-row v-for="item in fundRecordList" :key="item.id" class="body" justify="center">
          <van-swipe-cell>
            <van-col class="solidVanCol" span="6">{{item.createdTime}}</van-col>
            <van-col class="solidVanCol" span="5">{{item.operation}}</van-col>
            <van-col class="solidVanCol" span="5">{{item.unv}}</van-col>
            <van-col class="solidVanCol" span="4">{{item.amount}}</van-col>
            <van-col class="solidVanCol" span="4">{{item.shares}}</van-col>
            <template #right>
              <van-button class="solidVanCol" type="warning" square text="修改日期"
                :value="updateFundDate" @click="clickModifyDateButton(item)" />
              <van-button v-if="item.operation===0" class="solidVanCol" type="warning" square
                text="修改买入净值" :value="updateFundDate" @click="clickModifyBuyFundUnvButton(item)" />
            </template>
          </van-swipe-cell>
        </van-row>
      </van-tab>
    </van-tabs>
    <van-popup class="popup" v-model="showBuyFundPopup">
      <van-form @submit="buyFund">
        <van-cell-group inset>
          <van-field v-model="updateFundUnv" type="number" label="预估单位净值" placeholder="请输入预估单位净值" />
          <van-field v-model="updateFundAmount" type="number" label="购买金额"
            placeholder="请输入需购买的金额" />
        </van-cell-group>
        <div style="margin: 16px;">
          <van-button round block type="default">
            提交
          </van-button>
        </div>
      </van-form>
    </van-popup>
    <van-popup class="popup" v-model="showSellFundPopup">
      <van-form @submit="sellFund">
        <van-cell-group inset>
          <van-field v-model="updateFundUnv" type="number" label="预估单位净值" placeholder="请输入预估单位净值" />
          <van-field v-model="updateFundShares" type="number" label="卖出份额"
            placeholder="请输入需卖出的份额" />
        </van-cell-group>
        <div style="margin: 16px;">
          <van-button round block type="default">
            提交
          </van-button>
        </div>
      </van-form>
    </van-popup>
    <van-popup class="popup" v-model="showModifyBuyFundUnvPopup">
      <van-form @submit="modifyBuyFundUnv">
        <van-cell-group inset>
          <van-field v-model="updateFundUnv" type="number" label="实际买入净值"
            placeholder="请输入要更新的买入单位净值" />
        </van-cell-group>
        <div style="margin: 16px;">
          <van-button round block type="default">
            提交
          </van-button>
        </div>
      </van-form>
    </van-popup>
    <van-popup class="popup" v-model="showModifySellFundUnvPopup">
      <van-form @submit="modifySellFundUnv">
        <van-cell-group inset>
          <van-field v-model="updateFundUnv" type="number" label="实际卖出净值"
            placeholder="请输入要更新的卖出单位净值" />
        </van-cell-group>
        <div style="margin: 16px;">
          <van-button round block type="default">
            提交
          </van-button>
        </div>
      </van-form>
    </van-popup>
    <van-calendar v-model="showModifyDatePopup" @confirm="modifyDate" :min-date="minDate" />
  </div>
  <!-- 添加基金接口 -->

</template>

<script>
import * as globalConstant from '../const/globalConstant.js';

export default {
  data() {
    return {
      BASE_API: process.env.VUE_APP_BASE_TARGET,
      minDate: new Date(2010, 0, 1),
      showModal: false,
      token: '',
      packageOrderId: '',
      active: 0,
      showBuyFundPopup: false,
      showSellFundPopup: false,
      showModifyBuyFundUnvPopup: false,
      showModifySellFundUnvPopup: false,
      showModifyDatePopup: false,
      updateFundUnv: null,
      updateFundAmount: null,
      updateFundShares: null,
      updateFundDate: '',
      updateSharesRunningId: null, // 份额运营表的id，只在卖出时使用
      updateOperationProfitId: null, // 操作利润表的id，只在修改时使用
      updateFundRecordId: null, // 基金记录表的id，只在修改日期时使用
      fundRecordList: [],
      operationProfitList: [],
      sharesRunningList: [],
      zeroShares: 0,
      shares: 0,
      profit: 0,
      amount: 0,
    };
  },
  methods: {
    init() {
      this.token = this.$route.query.token;
      this.packageOrderId = this.$route.query.packageOrderId;
      let data = {};
      let headers = {
        headers: {
          'Content-Type': 'application/json',
          token: this.token,
        },
      };
      this.$http.post(this.BASE_API + globalConstant.queryFundInfoUrl, data, headers).then(
        response => {
          console.log(response);
          let data = response.data.data;
          this.amount = data.amount;
          this.shares = data.shares;
          this.profit = data.profit;
          this.fundRecordList = data.fundRecordList;
          this.operationProfitList = data.operationProfitList;
          this.sharesRunningList = data.sharesRunningList;
          this.zeroShares = data.zeroShares;
        },
        error => {
          if (error.body.code === '400') {
            this.$toast('token过期，请重新登录');
            this.$router.push('/');
          }
          console.log(error);
        }
      );
    },
    formatDate(updateFundDate) {
      return `${updateFundDate.getFullYear()}-${
        updateFundDate.getMonth() + 1
      }-${updateFundDate.getDate()}`;
    },
    gotoPackageListPage() {
      console.log('click me');
      this.$router.push('/packageList?token=' + this.token);
    },
    clickBuyFundButton() {
      this.showBuyFundPopup = true;
    },
    clickSellFundButton(item) {
      this.showSellFundPopup = true;
      this.updateFundShares = item.sharesRemaining;
      this.updateSharesRunningId = item.id;
      console.log(item);
    },
    clickModifySellFundUnvButton(item) {
      this.showModifySellFundUnvPopup = true;
      this.updateOperationProfitId = item.id;
      console.log(item);
    },
    clickModifyBuyFundUnvButton(item) {
      this.showModifyBuyFundUnvPopup = true;
      this.updateFundRecordId = item.id;
      console.log(item);
    },
    clickModifyDateButton(item) {
      this.showModifyDatePopup = true;
      this.updateFundRecordId = item.id;
      console.log(item);
      console.log(this.updateFundDate);
    },
    modifyDate(updateFundDate) {
      this.showModifyDatePopup = false;
      this.updateFundDate = this.formatDate(updateFundDate);
      console.log(this.updateFundDate);
      let data = {
        fundRecordId: this.updateFundRecordId, // 0买入，1卖出
        buyDate: this.updateFundDate,
      };
      console.log('data:', data);
      let headers = {
        headers: {
          'Content-Type': 'application/json',
          token: this.token,
        },
      };
      this.$http.post(this.BASE_API + globalConstant.modifyFundDateUrl, data, headers).then(
        response => {
          console.log(response);
          if (response.body.code !== '200') {
            this.$toast('请求异常' + response.body.msg);
          } else {
            this.$toast('成功');
          }
          this.showModifyFundPopup = false;
          this.init();
        },
        error => {
          if (error.body.code === '400') {
            this.$toast('token过期，请重新登录');
            this.$router.push('/');
          }
          console.log(error);
        }
      );
    },
    modifySellFundUnv() {
      let data = {
        operation: 1, // 0买入，1卖出
        unv: this.updateFundUnv,
        operationProfitId: this.updateOperationProfitId,
      };
      console.log('data:', data);
      let headers = {
        headers: {
          'Content-Type': 'application/json',
          token: this.token,
        },
      };
      this.$http.post(this.BASE_API + globalConstant.modifySellFundUnvUrl, data, headers).then(
        response => {
          console.log(response);
          if (response.body.code !== '200') {
            this.$toast('请求异常' + response.body.msg);
          } else {
            this.$toast('成功');
          }
          this.showModifySellFundUnvPopup = false;
          this.init();
        },
        error => {
          if (error.body.code === '400') {
            this.$toast('token过期，请重新登录');
            this.$router.push('/');
          }
          console.log(error);
        }
      );
    },
    modifyBuyFundUnv() {
      let data = {
        operation: 1, // 0买入，1卖出
        unv: this.updateFundUnv,
        fundRecordId: this.updateFundRecordId,
      };
      console.log('data:', data);
      let headers = {
        headers: {
          'Content-Type': 'application/json',
          token: this.token,
        },
      };
      this.$http.post(this.BASE_API + globalConstant.modifyBuyFundUnvUrl, data, headers).then(
        response => {
          console.log(response);
          if (response.body.code !== '200') {
            this.$toast('请求异常' + response.body.msg);
          } else {
            this.$toast('成功');
          }
          this.showModifyBuyFundUnvPopup = false;
          this.init();
        },
        error => {
          if (error.body.code === '400') {
            this.$toast('token过期，请重新登录');
            this.$router.push('/');
          }
          console.log(error);
        }
      );
    },
    sellFund() {
      let data = {
        operation: 1, // 0买入，1卖出
        shares: this.updateFundShares,
        unv: this.updateFundUnv,
        sharesRunningId: this.updateSharesRunningId,
        serviceCharge: 0,
      };
      console.log('data:', data);
      let headers = {
        headers: {
          'Content-Type': 'application/json',
          token: this.token,
        },
      };
      this.$http.post(this.BASE_API + globalConstant.updateFundRecordUrl, data, headers).then(
        response => {
          console.log(response);
          if (response.body.code !== '200') {
            this.$toast('请求异常' + response.body.msg);
          } else {
            this.$toast('成功');
          }
          this.showSellFundPopup = false;
          this.init();
        },
        error => {
          if (error.body.code === '400') {
            this.$toast('token过期，请重新登录');
            this.$router.push('/');
          }
          console.log(error);
        }
      );
    },
    backward() {
      this.$router.go(-1);
    },
    buyFund() {
      let data = {
        operation: 0, // 0买入，1卖出
        amount: this.updateFundAmount,
        unv: this.updateFundUnv,
      };
      let headers = {
        headers: {
          'Content-Type': 'application/json',
          token: this.token,
        },
      };
      this.$http.post(this.BASE_API + globalConstant.updateFundRecordUrl, data, headers).then(
        response => {
          console.log(response);
          if (response.body.code !== '200') {
            this.$toast('请求异常' + response.body.msg);
          } else {
            this.$toast('成功');
            this.showBuyFundPopup = false;
            this.init();
          }
        },
        error => {
          if (error.body.code === '400') {
            this.$toast('token过期，请重新登录');
            this.$router.push('/');
          }
          console.log(error);
        }
      );
    },
  },
  created() {
    this.init();
  },
};
</script>

<style scoped>
.overview {
  font-size: medium;
}
.overview .head {
  color: black;
}
.overview .body {
  color: black;
  text-align: center;
  line-height: 30px;
}
.overview .button {
  font-weight: bold;
  color: black;
  background-color: lightgray;
}
.popup {
  width: 80%;
}
.solidVanCol {
  border: solid 1px lightgrey;
  height: 44px;
  line-height: 44px;
}
</style>