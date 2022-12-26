# README

## 前言

jm前端页面

## 授权

本项目遵守 AGPLv3 的相关条款

## 技术介绍
采用vue2 + vant开发
- swiper使用4.5.1

## 常用命令
`npm run serve`
`npm build`
`npm run lint`

## post请求代码
```
      let token = this.$route.params.token;
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
      this.$http.post(globalConstant.testGetTokenUrl, data, headers).then(
        response => {
          console.log(response)
        }, error => {
          if (error.body.code === "400") {
            this.$toast("token过期，请重新登录")
          }
          console.log(error)
        }
      )
```

### 登录加解密
`https://www.cnblogs.com/xiaozhaoboke/p/15789123.html`

```javascript
// 3.login.vue引入加密方法

import {Encrypt} from '../utils/cryptoUtils.js'
// 4.login.vue登录请求发起，对密码参数加密
 let param={
          username:this.loginForm.username,
          password:Encrypt(this.loginForm.password)  //密码加密
        }

```