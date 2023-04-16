<template>
  <div class="loginPage">
    <h2>量化投资BI</h2>
    <van-form class="form" @submit="toLoginOrRegister(phone, password, name)">
      <van-cell-group inset>
        <van-field v-model="phone" name="phone" label="手机号" placeholder="请输入你的手机号" :rules="[
        { required: true, message: '请填写手机号' },
        { pattern: /^1[3456789]\d{9}$/, message: '手机号码格式错误！' }]" type="tel" clearable />
        <van-field v-model="password" type="password" name="password" label="密码"
          placeholder="请输入你的密码" :rules="[{ required: true, message: '请填写密码' }]" clearable />
      </van-cell-group>
      <div style="margin: 16px;">
        <van-button round block type="primary" native-type="submit">
          登录/注册
        </van-button>
        <!-- 写一个PC的登录界面 -->

      </div>
    </van-form>
  </div>

</template>

<script>
import * as globalConstant from '../const/globalConstant.js';
import { Encrypt } from '../utils/cryptoUtils.js';

export default {
  data() {
    return {
      BASE_API: process.env.VUE_APP_BASE_TARGET,
      showModal: false,
      phone: '',
      password: '',
      name: '',
      token: '',
    };
  },
  methods: {
    toLoginOrRegister(phone, password, name) {
      let requestJson = {
        phone: phone,
        password: Encrypt(password),
        name: name,
      };
      console.log('click me', phone, Encrypt(password));
      let headers = {
        headers: {
          'Content-Type': 'application/json;charset=utf-8',
        },
      };
      // TODO 请求登录接口，获取token
      this.$http.post(this.BASE_API + globalConstant.loginOrRegisterUrl, requestJson, headers).then(
        response => {
          console.log(response);
          let data = response.data.data;
          let type = data.type;
          let token = data.token;
          if (type === 'login') {
            this.$toast('登录成功');
          } else if (type === 'register') {
            this.$toast('注册成功');
          }
          window.sessionStorage.setItem('token', token);
          this.$router.push('/StockAI?token=' + token);
        },
        error => {
          if (error.body.code === '400') {
            this.$toast(error.body.msg);
          }
          console.log(error);
        }
      );
    },
  },
};
</script>

<style scoped>
.loginPage {
  margin-top: 180px;
}
.form {
  margin-top: 70px;
}
</style>