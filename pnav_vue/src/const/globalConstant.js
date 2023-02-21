module.exports = {
    // 获取测试token
    testGetTokenUrl: "/api/h5/testGetToken",
    // 购买券包
    buyPackageUrl: "/api/h5/buyPackage",
    // 获取券包列表
    getPackageListUrl: "/api/h5/getPackageList",
    // 获取商品列表 通过券包订单id
    getCommodityListByPackageOrderIdUrl: "/api/h5/getCommodityListByPackageOrderId",
    // 获取商品详情 通过商品订单id
    getCommodityDetailByCommodityOrderIdUrl: "/api/h5/getCommodityDetailByCommodityOrderId",

    // 使用及未商品
    gotoUseJiweiCommodityUrl: "/api/suppliers/jiwei/gotoUseCommodity",
    // 购买及未商品
    buyJiweiCommodityUrl: "/api/suppliers/jiwei/distribution/coupon/api/package/buy",
};