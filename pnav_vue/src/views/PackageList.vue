<template>
  <div class="page">
    <!-- 1. 头图 -->
    <div>
      <div>
        <img class="headPic" src="../assets/headPic.png">
      </div>
      <div class="background"></div>
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
    </div>
    <!-- 券列表 TODO 改为for循环，从后台读取列表参数-->
    <div class="packageList">
      <div class="packageItem" v-for="(packageOrder, orderId, index) in packageList" :key="index">
        <h2 class="packageName" @click="gotoItemListPage(orderId)">{{ packageOrder.packageName }}></h2>
        <p class="packageIntro">获取方式：赠送 有效期：{{ packageOrder.effectiveStartTime }}-{{ packageOrder.effectiveEndTime }}
        </p>
        <div class="swiper-container">
          <div class="swiper-wrapper">
            <div class="swiper-slide" v-for="commodityInfo in packageOrder.commodityList" :key="commodityInfo">
              <img class="slidePic" src="../assets/2.png">
              <p class="slideText">{{ commodityInfo }}</p>
            </div>
          </div>
        </div>
      </div>
      <div class="nomore">没有更多了，看看别的吧</div>
    </div>
  </div>
</template>
<script>
import 'swiper/dist/js/swiper'
import 'swiper/dist/css/swiper.css'
import Swiper from "swiper"
import * as globalConstant from '../const/globalConstant.js'
export default {
  data() {
    return {
      isShowDelOrderPopup: false, // 删除订单弹窗
      packageList: {},
    };
  },
  methods: {
    gotoItemListPage(orderId) {
      let token = this.$route.query.token;
      this.$router.push("/itemList?orderId=" + orderId + "&token=" + token);
    },
    init() {
      // 从url中获取参数
      let token = this.$route.query.token;
      console.log(token)

      let data = {
        packageId: '1'
      }
      let headers = {
        'headers': {
          'Content-Type': 'application/json',
          'token': token,
        }
      }
      this.$http.post(globalConstant.getPackageListUrl, data, headers).then(
        response => {
          console.log(response)
          this.packageList = response.data.data
          // TODO 进行渲染
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
  beforeCreate() {

  },
  created() {
    this.init();

  },
  beforeMount() {

  },
  mounted() {
    new Swiper('.swiper-container', {
      direction: 'horizontal', // 垂直水平切换选项
      slidesPerView: 2,  // 展示个数
      //mousewheel: true, //滚轮
      // autoplay: { //自动开始
      //   delay: 2500, //时间间隔
      //   disableOnInteraction: false, //*手动操作轮播图后不会暂停*
      // },
      loop: false, // 循环模式选项

      // 如果需要分页器
      // pagination: {
      //   el: '.swiper-pagination',
      //   clickable: true, // 分页器可以点击
      // },

      // 如果需要前进后退按钮
      // navigation: {
      //   nextEl: '.swiper-button-next',
      //   prevEl: '.swiper-button-prev',
      // },

      // 如果需要滚动条
      scrollbar: {
        el: '.swiper-scrollbar',
      },
    })
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
  height: auto;
  max-width: 100%;
  margin: 0;
  padding: 0;
}

.background {
  background-color: #f9f9f9;
  height: 12.25vw;
}

.userInfo {
  /* margin-top: 9.06rem; */
  width: 93.3vw;
  height: 24.5vw;
  position: absolute;
  margin: 0;
  padding: 0;
  background: white;
  z-index: 1;
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  /* top: 45.3vw; */
  top: 4.53rem;
  left: 3.3vw;
}

.avator {
  margin: 0;
  padding: 0;
  z-index: 2;
  width: 17vw;
  height: 17vw;
  position: absolute;
  top: 36.8vw;
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
  margin-left: 5.3%;
  margin-top: 2vw;
  width: 94.7%;
}

.packageIntro {
  margin: 0;
  padding: 0;
  margin-top: 2vw;
  margin-left: 10%;
  color: grey;
  text-align: left;
  width: 80%;
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
  margin-left: 0.3rem;
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
  font-size: small;
}
</style>
