<template>
  <div class="about">
    <h1>保司页面</h1>
    <div class="buyButton">
      <van-button type="primary" @click="showDialog">购买券包</van-button>
    </div>
  </div>

</template>

<script>
import * as globalConstant from '../const/globalConstant.js'

export default {
  data() {
    return {
      showModal: false
    };
  },
  methods: {
    showDialog() {
      console.log(process.env.VUE_APP_BASE_TARGET)
      console.log(process.env.NODE_ENV)

      // 购买券包 TODO 后面取消掉这个获取token的请求
      this.$http.get(globalConstant.testGetTokenUrl).then(
        response => {
          console.log("成功", response)
          let token = response.body.data.token;

          let data = {
            packageId: '1'
          }
          let headers = {
            'headers': {
              'Content-Type': 'application/json',
              'token': token,
            }
          }
          this.$http.post(globalConstant.buyPackageUrl, data, headers).then(
            response => {
              console.log(response)
              this.$dialog.confirm({
                title: '购买成功',
                message: '是否立即去兑换？'
              }).then(() => {
                // on confirm TODO 跳转到券包-商品列表页
                this.$router.push("/packageList?token=" + token)
              }).catch(() => {
                // on cancel
                console.log("user cancel")
              });
            }, error => {
              if (error.body.code === "400") {
                this.$toast("token过期，请重新登录")
              }
              console.log(error)
            }
          )
        },
        error => {
          console.log("失败", error)
        }
      )
    },
    gotoPackagePage() {
      console.log("click me");
    }
  }
}

</script>

<style scoped>
.buyButton {
  position: fixed;
  top: 50%;
  left: 50%;
  width: 50%;
  height: 50%;
  transform: none;
  -webkit-transform: translateX(-50%) translateY(-50%);
}
</style>