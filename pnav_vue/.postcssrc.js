module.exports = {
    "plugins": {
      "postcss-import": {},
      "postcss-url": {},
      // to edit target browsers: use "browserslist" field in package.json
      "autoprefixer": {
        // vue2 使用overrideBrowserslist
        overrideBrowserslist: ['Android >= 4.0', 'iOS >= 8']
      },
      "postcss-pxtorem": { // 此处为添加部分
        rootValue: 37.5, // 视觉稿宽度，为全宽的10等份。比如这里写37.5，那么设计稿的宽就是375. 如果设计稿为750，则其像素需要除以2后，再除以37.5才可得出准确rem
        unitPrecision: 5, // 转换成rem后保留的小数点位数
        propList: ['*'], // 匹配css中的属性， *代表启用所有属性
        selectorBlackList: [], // 忽略的选择器
        replace: true,
        mediaQuery: false,
        minPixelValue: 0,
        exclude: ['node_modules'] // 忽略一些文件， 不进行转换
      }
    }
  }