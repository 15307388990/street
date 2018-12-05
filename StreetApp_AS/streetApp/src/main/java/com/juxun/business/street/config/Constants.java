/**
 *
 */
package com.juxun.business.street.config;

/**
 * 项目名称：Street 类名称：Constants 类描述： 常量类 创建人：WuJianhua 创建时间：2015年5月28日 上午9:13:15
 * 修改人：WuJianhua 修改时间：2015年5月28日 上午9:13:15 修改备注：
 */
public class Constants {
    /**
     * 微信模块参数
     */
    public static final String APP_ID = "wxb82bb2ec521219f2";
    public static final String APP_SECRET = "550034813429bfdd067360a962f24f74";
    public static final String GET_WECHAT_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String GET_WECHAT_USER_INFO = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * 图片地址
     * http://image.efengshe.com/511dda38-4b17-44e3-86c0-5f5bf2a3a42c
     */
    public static final String imageUrl = "http://image.efengshe.com/";
    /**
     * 测试域名
     */
    public static final String mainUrl = "http://pos.api.efengshe.com";
    /** 主域名 **/
    //public static final String mainUrl = "http://api.pos.efengshe.com/";
    /**
     * 关于地址
     */
    public static final String aboutUrl = "http://api.jiebian.me/about.htm";
    /**
     * 申请入驻
     */
    public static final String settled = "http://api.jiebian.me/settled.htm";
    /**
     * 常见问题地址
     */
    public static final String problemUrl = "http://api.jiebian.me/help.htm";
    /**
     * 登录
     * POST 开发完成 /api/shop/admin/login
     */
    public static final String userLogin = "/api/shop/admin/login";
    /**
     * 查找帐号是否存在以及返回安全手机号
     */
    public static final String findPhoneByAccount = "/api/pos/findPhoneByAccount.json";
    /**
     * 忘记密码修改
     */
    public static final String updatePassWord = "/api/pos/updatePassWord.json";
    /**
     * 获取支付配置
     */
    public static final String paySetting = "/api/linepay/paySetting.action";
    /**
     * 微信支付
     */
    public static final String wxLineOrderPay = "/api/pos/pay/wepayScanCodePay.action";
    /**
     * 支付宝支付
     */
    public static final String aliLineOrderPay = "/api/pos/pay/alipayScanCodePay.action";
    /**
     * 微信、支付宝支付二维码生成
     */
    public static final String codePay = "/api/shop/pos/codePay";

    /**
     * 查询支付订单状态
     */
    public static final String lineOrderState = "/api/pos/pay/orderQuery.action";
    /**
     * 查询订单状态   POST 开发完成 /api/shop/pos/orderQuery
     */
//    public static final String orderQuery = "/api/pos/supplier/order/orderQuery.json";
    public static final String orderQuery = "/api/shop/pos/orderQuery";
    /**
     * 查询订单状态2
     */
    public static final String orderQuery2 = "/api/pos/supplier/recharge/orderQuery.json";
    /**
     * 团购列表
     */
    public static final String teamBuyList = "/api/teambuy/teamBuyList.action";
    /**
     * 团购上架 下架
     */
    public static final String teamBuyAdded = "/api/teambuy/teamBuyAdded.action";
    /**
     * 团购延期
     */
    public static final String teamBuyExtension = "/api/teambuy/teamBuyExtension.action";
    /**
     * 团购劵核销
     */
    public static final String teamBuyWriteoff = "/api/teambuy/teamBuyWriteoff.action";
    /**
     * 优惠券核销
     */
    public static final String couponWriteoff = "/api/coupon/couponWriteoff.action";
    /**
     * 需要核销的团购劵详情
     */
    public static final String teamBuyTicketInfo = "/api/teambuy/teamBuyTicketInfo.action";
    /**
     * pos订单列表
     */
    public static final String lineOrderList = "/api/lineOrderList.action";
    /**
     * 团购订单列表
     */
    public static final String teambuyoder = "/api/teambuy/teambuyoder.action";
    /**
     * 团购订单详情
     */
    public static final String teambuyoderinfo = "/api/teambuy/teambuyoderinfo.action";
    /**
     * 团购劵延期
     */
    public static final String teamBuyTicketExtension = "/api/teambuy/teamBuyTicketExtension.action";
    /**
     * 会员卡
     */
    public static final String meberinterests = "/api/member/meberinterests.action";
    /**
     * 电商订单列表
     */
    public static final String shopOrderList = "/api/shop/mall/order/list";
    /**
     * 电商订单详情
     */
    public static final String shopOrderInfo = "/api/shop/mall/order/info";
    /**
     * 电商订单发货
     */
    public static final String updateOrderExpress = "/api/shop/mall/order/sent";
    /**
     * 订单追踪
     */
    public static final String logisticsSearch = "/logisticsSearch.action";
    /**
     * 优惠券
     */
    public static final String couponIF = "/api/member/couponIF.action";
    /**
     * 微信卡劵列表接口
     */
    public static final String giveVoucherList = "/sweep/giveVoucherList.action";
    /**
     * 扫描二维码获取优惠劵详情
     */
    public static final String couponInfo = "/api/coupon/couponInfo.action";
    /**
     * 线下pos订单账单
     */
    public static final String lineOrderMonthlyBilling = "/api/order/lineOrderMonthlyBilling.action";
    /**
     * 线下订单每月总额
     */
    public static final String lineOrderTotalPrice = "/api/order/lineOrderTotalPrice.action";
    /**
     * 团购订单账单
     */
    public static final String teamBuyOrderMonthlyBilling = "/api/order/teamBuyOrderMonthlyBilling.action";
    /**
     * 团购订单每月总额
     */
    public static final String teamBuyOrderTotalPrice = "/api/order/teamBuyOrderTotalPrice.action";
    /**
     * 商城订单账单
     */
    public static final String shopOrderMonthlyBilling = "/api/order/shopOrderMonthlyBilling.action";
    /**
     * 商城订单每月总额
     */
    public static final String shopOrderTotalPrice = "/api/order/shopOrderTotalPrice.action";
    /**
     * 首页
     */
    public static final String totalmian = "/api/pos/order/getIndexAction.action";
    /**
     * 支付宝支付条码下单接口
     */
    public static final String AlipayMoneyAction = "/api/pos/pay/alipayCodePay.action";
    /**
     * 微信支付条码下单接口
     */
    public static final String WeixinMoneyAction = "/api/pos/pay/weChatCodePay.action";

    /**
     * 微信支付宝支付、付款的扫码支付
     */
    public static final String scanPay = "/api/shop/pos/scanPay";


    /**
     * 盒子刷卡支付下单请求接口
     */
    public static final String toPayByBoxAction = "/api/pos/pay/boxPay.action?";
    /**
     * 获取盒子key接口
     */
    public static final String giveMyKeyAction = "/api/pos/pay/boxPayCode.action";
    /**
     * 订单状态查询接口
     */
    public static final String giveOrder = "/sweep/giveOrderAction.action";
    /**
     * 检测版本
     */
    public static final String updateAPP = "/api/shop/version";
    /**
     * 设备推送注册
     **/
    public static final String posReg = "/api/pos/posReg.action";
    /**
     * 发货
     **/
    public static final String sendOut = "/api/shop/mall/order/sent";
    /**
     * 微信卡劵核销接口
     */
    public static final String veriFicsAction = "/sweep/veriFicsAction.action";
    /**
     * 12、财务首页
     */
    public static final String financeIndex = "/api/pos/finance/financeIndex.action";
    /**
     * 13、实收总额详细列表
     */
    public static final String financePaid = "/api/pos/finance/financePaid.action";
    /**
     * 14、结算详情
     */
    public static final String paidInfo = "/api/pos/finance/paidInfo.action";
    /**
     * 16、提现记录列表
     */
    public static final String withdrawList = "/api/shop/finance/index";
    /**
     * 20、已经完成提现的列表
     */
    public static final String completeWithdraw = "/api/shop/finance/withdrawList";
    /**
     * 15、冻结金额  财务首页
     */
//    public static final String freezeList = "/api/pos/finance/financePaid.action";
    public static final String freezeList = "/api/shop/finance/index";
    /**
     * 21、营业分析
     */
    public static final String businessAnalysis = "/api/pos/finance/businessAnalysis.action";
    /**
     * 27、分佣分析
     */
    public static final String commissionAnalysis = "/api/pos/finance/commissionAnalysis.action";
    /**
     * 某日营业分析
     */
    public static final String businessWithDay = "/api/pos/finance/businessWithDay.action";
    /**
     * 28、某日分佣分析
     */
    public static final String commissionWithDay = "/api/pos/finance/commissionWithDay.action";
    /**
     * 23、某笔订单营业分析
     */
    public static final String businessWithOrder = "/api/pos/finance/businessWithOrder.action";
    /**
     * 18、提现账户列表
     */
    public static final String withdrawAccess = "/api/shop/agencyaccount/list";
    /**
     * 19、申请提现
     */
    public static final String applyWithdraw = "/api/shop/finance/withraw";
    /**
     * 19、提现详情
     */
    public static final String withdrawInfo = "/api/pos/finance/withdrawInfo.action";
    /**
     * 38.获取订单信息
     */
    public static final String getOrderInfo = "/api/shop/mall/order/info";
    /**
     * 获取商品模板
     **/
    public static final String getTemplates = "/api/shop/mall/commodity/template";
    /**
     * 模板详情
     **/
    public static final String getTemplateInfo = "/api/pos/mall/commodity/getTemplateInfo.action";
    /**
     * 获取合伙人已开启的频道
     **/
    public static final String getOpenChannels = "/api/shop/mall/channel/list";
    /**
     * 商品管理列表
     **/
    public static final String getInventories = "/api/shop/mall/commodity/list";
    /**
     * 获取各个状态下SKU数量
     **/
    public static final String getSkuNumberWithCommodityState = "/api/shop/mall/commodity/stautsCount";
    /**
     * 获取七牛图片上传TOKEN
     **/
    public static final String getuploadtoken = "/api/shop/qiniuToken";
    /**
     * 添加或编辑商品(只生成单规格商品)
     **/
    public static final String addOrEditCommodity = "/api/shop/mall/commodity/add";
    /**
     * 修改商品
     **/
    public static final String update = "/api/shop/mall/commodity/update";
    /**
     * 获取商品详情
     **/
    public static final String getCommodityInfo = "/api/shop/mall/commodity/info";
    /**
     * 修改库存或过期时间
     **/
    public static final String editInvOrExp = "/api/pos/mall/commodity/editInvOrExp.action";
    /**
     * 审核通过商品上架
     **/
    public static final String upShelf = "/api/shop/mall/commodity/up";
    /**
     * 商品下架
     **/
    public static final String downSelf = "/api/shop/mall/commodity/down";
    /**
     * 删除商品
     **/
    public static final String delInventory = "/api/shop/mall/commodity/del";
    /**
     * 获取过期商品和少库存商品数量
     **/
    public static final String getExpAndLessCnt = "/api/pos/mall/commodity/getExpAndLessCnt.action";
    /**
     * 获取其他状态商品（待审核，未通过，已撤销）
     **/
    public static final String getCommodities = "/api/pos/mall/commodity/getCommodities.action";
    /**
     * 搜索全部商品
     **/
    public static final String searchAll = "/api/pos/mall/commodity/searchAll.action";
    /**
     * 购物车列表
     **/
    public static final String shoppingCart = "/api/shop/supplier/shopcar/list";

    /**
     * 购物车添加商品
     */
    public static final String addGoodsToCart = "/api/shop/supplier/shopcar/add";
    /**
     * 购物车删除
     */
    public static final String delGoodsToCart = "/api/shop/supplier/shopcar/remove";
    /**
     * 购物车商品清除
     */
    public static final String clearGoodsToCart = "/api/shop/supplier/shopcar/clear";
    /**
     * 购物车商品修改
     */
    public static final String updateGoodsToCart = "/api/shop/supplier/shopcar/update";

    /**
     * 添加或者修改购物车
     **/
//    public static final String addOrUpdate = "/api/pos/supplier/shoppingCart/addOrUpdate.json";
    /**
     * 根据id得到供应商商品对象
     **/
    public static final String findCommodityItem = "/api/pos/supplier/commodity/findCommodityItem.json";
    /**
     * 得到合伙人购物车数量
     **/
    public static final String getPartnerShoppingCartNumber = "/api/pos/supplier/shoppingCart/getPartnerShoppingCartNumber.json";
    /**
     * 合伙人采购订单列表
     **/
    public static final String findOrderList = "/api/shop/supplier/order/list";
    /**
     * 合伙人采购订单详情
     **/
    public static final String findOrder = "/api/pos/supplier/order/findOrder.json";
    /**
     * 供应商商品详情web
     **/
    public static final String commodityInfo = "/mall/commodityInfo.html";
    /**
     * 合伙人红包网页
     **/
    public static final String getRedpacket = "/partner/getRedpacket.html";
    /**
     * 红包列表 POST 开发完成 /api/shop/supplier/activity/red/list
     **/
    public static final String drawRedPacketList = "/api/shop/supplier/activity/red/list";
    /**
     * 修改订单状态
     **/
    public static final String modifyOrderState = "/api/pos/supplier/order/modifyOrderState.json";

    //供应链确认收货
    public static final String supplierOrderComplete = "/api/shop/supplier/order/complete";

    /**
     * 删除购物车商品
     **/
//    public static final String delItem = "/api/pos/supplier/shoppingCart/delItem.json";
    /**
     * 合伙人地址列表
     **/
    public static final String findAddressList = "/api/shop/partner/address/list";
    /**
     * 添加合伙人地址
     **/
    public static final String addAddress = "/api/shop/partner/address/add";
    /**
     * 修改合伙人地址
     */
    public static final String updateAddress = "/api/shop/partner/address/update";
    /**
     * 删除合伙人地址
     **/
    public static final String delAddress = "/api/shop/partner/address/del";
    /**
     * 获取默认地址   POST 开发完成 /api/shop/partner/address/default_address
     **/
    public static final String findAddress = "/api/shop/partner/address/default_address";
    /**
     * 确认下单
     **/
    public static final String confirmPlaceAnOrder = "/api/shop/supplier/order/create";
    /**
     * 发起支付
     */
    public static final String orderPay = "/api/shop/supplier/order/pay";

    /**
     * 设置默认地址
     **/
    public static final String defaultAddressSet = "/api/shop/partner/address/setDefault";
    /**
     * 查看物流详情
     **/
    public static final String searchExpress = "/mall/order/searchExpress.action";
    /**
     * 重新支付
     **/
    public static final String againPay = "/api/shop/supplier/order/pay";
    /**
     * 添加购物车，用于详情里的添加
     **/
    public static final String addItem = "/api/pos/supplier/shoppingCart/addItem.json";
    /**
     * 商户贷
     **/
    public static final String business = "/business/business.html";
    /**
     * 发送验证码
     * POST 开发完成 /api/shop/admin/sendRegMsg
     **/
    public static final String sendPhoneMsg = "/api/shop/admin/sendRegMsg";
    /**
     * 获取图片验证码流
     **/
    public static final String imageValidStream = "/api/shop/admin/imageValidStream";
    /**
     * 修改登录密码发送手机验证码
     **/
    public static final String sendAdminSettingPhoneMsg = "/api/pos/info/admin/sendAdminSettingPhoneMsg.json";
    /**
     * 验证手机号码
     **/
    public static final String verificationAdminSettingPhone = "/api/pos/info/admin/verificationAdminSettingPhone.json";
    /**
     * 修改登录密码
     **/
    public static final String updateAdminPass = "/api/shop/adminsetting/updateLoginPass";
    /**
     * 账户列表
     **/
    public static final String accountList = "/api/shop/agencyaccount/list";
    /**
     * 添加提现账号
     **/
    public static final String addAccount = "/api/shop/agencyaccount/add";
    /**
     * 根据卡号前6位 获取所属银行
     **/
    public static final String getBankName = "/api/shop/agencyaccount/findBank";
    /**
     * 注册验证
     **/
    public static final String validRegister = "/api/shop/admin/verifyAccount";
    /**
     * 获取城市
     **/
    public static final String getCityList = "/api/pos/getCityList.json";
    /**
     * 注册接口
     **/
    public static final String register = "/api/shop/admin/storeRegister";
    /**
     * 店铺详情
     * POST 开发完成 /api/shop/admin/info
     */
    public static final String mallSetInfo = "/api/shop/admin/info";

    /**
     * 切换店铺营业状态
     */
    public static final String changeBusiness = "/api/shop/business/updateBusiness";
    /**
     * 修改店铺营业时间
     */
    public static final String updateBusinessTime = "/api/shop/business//updateBusinesstime";
    /**
     * 修改店铺配送电话
     */
    public static final String updateDeliveryPhone = "/api/shop/business/updateDeliveryPhone";
    /**
     * 设置运费
     */
    public static final String updateDeliveryPrice = "/api/shop/business/updateDeliveryPrice";
    /**
     * 修改营业范围
     */
    public static final String updateBusinessRange = "/api/shop/business/updateBusinessRange";
    /**
     * 设置支付密码
     */
    public static final String setPayPass = "/api/pos/info/pay/setPayPass.json";
    /**
     * 为店铺绑定安全手机
     */
    public static final String bindPhone = "/api/pos/info/mall/bindPhone.json";
    /**
     * 手机验证换取手机验证操作令牌
     */
    public static final String verificationSettingPhone = "/api/pos/info/pay/verificationSettingPhone.json";
    /**
     * 获取充值记录及余额
     */
    public static final String findRechargeRecordList = "/api/shop/supplier/activity/recharge/record";
    /**
     * 充值活动选项列表     /api/shop/supplier/activity/recharge/items
     */
    public static final String findRechargeItemList = "/api/shop/supplier/activity/recharge/items";
    /**
     * 充值下单接口
     */
    public static final String rechargeOrder = "/api/shop/supplier/activity/recharge/recharge";
    /**
     * 首页地址
     * http://postest.efengshe.com/3/index.html
     */
    public static final String posindex = "/index.html?";
    /**
     * 供应商品
     */
    public static final String purchase = "/supplier/purchase.html?";
    /**
     * 修改店铺照片
     */
    public static final String updateShopIcon = "/api/shop/business/updateShopIcon";
    /**
     * 验证支付密码（修改支付密码时使用）
     */
    public static final String verificationPayPass = "/api/shop/agencypayinfo/validPayPass";
    /**
     * 删除提现账户
     */
    public static final String delAccount = "/api/shop/agencyaccount//del";
    /**
     * 消息中心
     */
    public static final String messageList = "/api/shop/message/list";
    /**
     * 红包兑换
     */
    public static final String drawRedPacketWithSN = "/api/shop/supplier/activity/red/draw";
    /**
     * 规则说明
     */
    public static final String supplierRules = "/supplier/supplierRules.html";
    /**
     * 注册协议
     * http://postest.efengshe.com/3/registerRules.html
     */
    public static final String registerRules = "/supplier/registerRules.html";
    /**
     * 修改支付密码
     */
    public static final String updatePayPass = "/api/shop/agencypayinfo/updatePayPass";
    /**
     * 采购搜索
     */
    public static final String purchaseSales = "/supplier/purchaseSales.html?";
    /**
     * 余额支付验证密码后的接口请求
     */
    public static final String balancePayNotify = "/api/pos/balance/notify/balancePayNotify.json";
    /**
     * 白条支付下单
     */
    public static final String whitebarPayNotify = "/api/pos/whitebar/notify//whitebarPayNotify.json";
    /**
     * 消息详情
     */
    public static final String messageInfo = "/message/messageInfo.html?";
    /**
     * 白条首页
     */
    public static final String whiteBar = "/whitebar/whiteBar.html?";
    /**
     * 重新发起支付
     */
    public static final String continuePay = "/supplier/continuePay.html?";
    /**
     * 重新审核获取相应信息
     */
    public static final String getMallSet = "/api/pos/mall/set/getMallSet.json";
    /**
     * 重新审核提交
     **/
    public static final String reExamination = "api/pos/mall/set/reExamination.json";
    /**
     * 设置店铺公告
     **/
    public static final String setMallNotice = "/api/shop/business/updateNotice";
    /**
     * 白条帮助
     **/
    public static final String help = "/whitebar/help.html";
    /**
     * 填写台卡申请资料
     **/
    public static final String editDecometerApplyInfo = "/api/decometer/apply/editDecometerApplyInfo2.json";
    /**
     * 获取填写的台卡申请信息
     **/
    public static final String getDecometerApplyInfo = "/api/decometer/apply/getDecometerApplyInfo2.json";
    /**
     * 获取所有城市
     **/
    public static final String getAllBankCity = "/api/shop/agencyaccount/cityList";
    /**
     * 根据城市编号和银行名称获取支行列表
     * POST 开发完成 /api/shop/agencyaccount/findBranchBanck
     **/
    public static final String getBankBranchsByCityCode = "/api/shop/agencyaccount/findBranchBanck";
    /**
     * 添加账户
     **/
    public static final String addAgencyAccountAndApply = "/api/decometer/apply/addAgencyAccountAndApply.json";
    /**
     * 提交审核
     **/
    public static final String applyDecometer = "/api/decometer/apply/applyDecometer.json";
    /**
     * 售后申请采购订单列表
     **/
    public static final String aftersaleRequestList = "/api/shop/supplier/order/service/allowAfterSalesOrderList";
    /**
     * 获取供应链订单下的售后服务商品列表情况
     **/
    public static final String requestAfterSale = "/api/shop/supplier/order/service/getAllowRefundsCommodity";
    /**
     * 获取售后退款原因列表
     **/
    public static final String getRefundRemarkList = "/api/shop/supplier/order/service/getRefundsReason";
    /**
     * 获取采购订单取消原因列表
     **/
    public static final String getCloseOrderTextList = "/api/shop/supplier/order/cancelOrderTextList";
    /**
     * 提交申请售后
     **/
    public static final String customerServiceApply = "/api/shop/supplier/order/service/createServiceOrder";
    /**
     * 获取申请记录列表
     **/
    public static final String getApplyRecordList = "/api/shop/supplier/order/service/list";
    /**
     * 获取单个订单的申请记录列表
     **/
    public static final String getOrderApplyRecordList = "/api/pos/supplier/customerservice/getOrderApplyRecordList.json";
    /**
     * 售后申请取消
     **/
    public static final String closeCustomerServiceApply = "/api/shop/supplier/order/service/cancelServiceOrder";
    /**
     * 售后申请记录详情
     **/
    public static final String getApplyRecordInfo = "/api/pos/supplier/customerservice/getApplyRecordInfo.json";
    /**
     * 删除采购订单
     **/
    public static final String deleteOrder = "/api/shop/supplier/order/del";
    /**
     * 取消采购订单
     **/
    public static final String closeOrder = "/api/shop/supplier/order/cancel";
    /**
     * 营业分析
     **/
    public static final String shopStatistics = "/api/shop/finance/businessStatistics";
    /**
     * 钱包
     **/
    public static final String shopWallet = "/api/shop/finance/wallet";
    /**
     * 统计合伙人邀请用户的数量
     **/
    public static final String getRecommendedNumber = "/api/pos/mall/commodity/getRecommendedNumber.json";
    /**
     * 获取推荐用户列表
     **/
    public static final String getRecommendedMember = "/api/pos/mall/commodity/getRecommendedMember.json";
    /**
     * 删除订单【订单】
     **/
    public static final String orDeleteOrder = "/api/shop/supplier/order/del";
    /**
     * 取消订单【订单】
     **/
    public static final String orRejectOrder = "/api/shop/mall/order/rejectOrders";
    /**
     * 取消订单理由【订单】
     **/
    public static final String orRejectOrderReason = "/api/shop/supplier/order/cancelOrderTextList";
    /**
     * 电商取消订单理
     **/
    public static final String rejectOrdersInfo = "/api/shop/mall/order/rejectOrdersInfo";
    /**
     * 获取用户储值记录列表
     **/
    public static final String getMemberRechargeRecordList = "/api/shop/mall/activity/recharge/rechargeRecordList";
    /**
     * 储值充值活动首页
     **/
    public static final String storeValueCenterIndex = "/api/shop/mall/activity/recharge/index";
    /**
     * 添加储值充值活动
     **/
    public static final String addOrEditMemberReChargeActivity = "/api/shop/mall/activity/recharge/add";
    /**
     * 修改储值充值活动
     **/
    public static final String rechargeupdate = "/api/shop/mall/activity/recharge/update";
    /**
     * 充值活动历史列表
     **/
    public static final String getMemberRechargeActivityHistoryList = "/api/shop/mall/activity/recharge/historyActivityList";
    /**
     * 储值活动内容页面
     **/
    public static final String getMemberRechargeActivityInfo = "/api/shop/mall/activity/recharge/getActivityInfo";
    /**
     * 开启或关闭储值活动
     **/
    public static final String enbleMemberReChargeActivity = "/api/shop/mall/activity/recharge/changeStatus";
    /**
     * 删除储值活动
     **/
    public static final String delMemberReChargeActivity = "/api/shop/mall/activity/recharge/delete";
    /**
     * 用户统计列表
     **/
    public static final String getMemberRecordStatices = "/api/shop/mall/activity/recharge/memberStatistics";
    /**
     * 获取用户统计详情
     **/
    public static final String getMemberRecordInfo = "/api/pos/recharge/getMemberRecordInfo.json";
    /**
     * 获取小程序推荐码
     **/
    public static final String getMicroshopCodeStream = "/api/pos/mall/set/getMicroshopCodeStream.json";

    //获取供应商商品列表
    public static final String getSupplierCommodityList = "/api/shop/supplier/commodity/list";
    //供应链频道列表
    public static final String getSupplierChannelList = "/api/shop/supplier/channel/list";
    //供应链商品详情
    public static final String getSupplierCommodityInfo = "/api/shop/supplier/commodity/info";

    //验证安全手机验证码
    public static final String sendSafeMsg = "/api/shop/agencypayinfo/sendSafeMsg";
    //安全手机图形验证码
    public static final String safeImageValidStream = "/api/shop/agencypayinfo/safeImageValidStream";
    //安全手机验证
    public static final String validSafePhone = "/api/shop/agencypayinfo/validSafePhone";

    //代金券列表活动 【代金券活动】
    public static final String getCouponList = "/api/shop/mall/activity/red/list";
    //开启/暂停红包活动【代金券活动】
    public static final String openOrPauseCouponActs = "/api/shop/mall/activity/red/openOrPause";
    //删除红包活动【代金券活动】
    public static final String delCouponActs = "/api/shop/mall/activity/red/del";
    //红包领取记录【代金券活动】
    public static final String getCouponHistory = "/api/shop/mall/activity/red/drawInfo";
    //添加红包活动【代金券活动】
    public static final String addCouponActs = "/api/shop/mall/activity/red/add";
    //修改代金券
    public static final String redupdate = "/api/shop/mall/activity/red/update";

}
