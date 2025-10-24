(global["webpackJsonp"] = global["webpackJsonp"] || []).push([["common/main"],{

/***/ 0:
/*!********************************************************************!*\
  !*** D:/上海永熵/franchise-managementaiJMGL-project/wx_client/main.js ***!
  \********************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(wx, uni, createApp) {

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ 4);
var _defineProperty2 = _interopRequireDefault(__webpack_require__(/*! @babel/runtime/helpers/defineProperty */ 11));
__webpack_require__(/*! uni-pages */ 26);
var _App = _interopRequireDefault(__webpack_require__(/*! ./App */ 27));
var _share = _interopRequireDefault(__webpack_require__(/*! @/utils/share.js */ 39));
var _vue = _interopRequireDefault(__webpack_require__(/*! vue */ 25));
var _uviewUi = _interopRequireDefault(__webpack_require__(/*! @/uni_modules/uview-ui */ 40));
function ownKeys(object, enumerableOnly) { var keys = Object.keys(object); if (Object.getOwnPropertySymbols) { var symbols = Object.getOwnPropertySymbols(object); enumerableOnly && (symbols = symbols.filter(function (sym) { return Object.getOwnPropertyDescriptor(object, sym).enumerable; })), keys.push.apply(keys, symbols); } return keys; }
function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = null != arguments[i] ? arguments[i] : {}; i % 2 ? ownKeys(Object(source), !0).forEach(function (key) { (0, _defineProperty2.default)(target, key, source[key]); }) : Object.getOwnPropertyDescriptors ? Object.defineProperties(target, Object.getOwnPropertyDescriptors(source)) : ownKeys(Object(source)).forEach(function (key) { Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key)); }); } return target; }
// @ts-ignore
wx.__webpack_require_UNI_MP_PLUGIN__ = __webpack_require__;
_vue.default.mixin(_share.default);
_vue.default.use(_uviewUi.default);

// 导入自定义导航栏组件
var cuCustom = function cuCustom() {
  __webpack_require__.e(/*! require.ensure | colorui/components/cu-custom */ "colorui/components/cu-custom").then((function () {
    return resolve(__webpack_require__(/*! ./colorui/components/cu-custom.vue */ 419));
  }).bind(null, __webpack_require__)).catch(__webpack_require__.oe);
};
// 注册自定义导航栏组件
_vue.default.component('cu-custom', cuCustom);

// 设置全局状态栏高度等信息
uni.getSystemInfo({
  success: function success(e) {
    _vue.default.prototype.StatusBar = e.statusBarHeight;
    var custom = wx.getMenuButtonBoundingClientRect();
    _vue.default.prototype.Custom = custom;
    _vue.default.prototype.CustomBar = custom.bottom + custom.top - e.statusBarHeight;
  }
});
// Vue.prototype.request = request.request //挂载到全局
// let imgurldev = 'http://192.168.10.5:8083'
var imgurl = 'https://ga.rasmall.cn/prod-api';
_vue.default.prototype.myimgurl = function () {
  return imgurl;
};
// 注册自定义 tabBar 组件
var CustomTabBar = function CustomTabBar() {
  __webpack_require__.e(/*! require.ensure | components/custom-tab-bar */ "components/custom-tab-bar").then((function () {
    return resolve(__webpack_require__(/*! ./components/custom-tab-bar.vue */ 426));
  }).bind(null, __webpack_require__)).catch(__webpack_require__.oe);
};
_vue.default.component('custom-tab-bar', CustomTabBar);
_vue.default.config.productionTip = false;
_App.default.mpType = 'app';
var app = new _vue.default(_objectSpread({}, _App.default));
function jump(url) {
  uni.navigateTo({
    url: url
  });
}
// function GoLogin(url){
// 	uni.redirectTo({
// 		url: '/pages/login/login'
// 	});
// uni.switchTab({
//     url: '/pages/index/index'
// });
// }

function Godetails(v, url) {
  console.log(JSON.stringify(v));
  uni.navigateTo({
    url: url + '?params=' + JSON.stringify(v)
  });
}
function ZH(json) {
  return JSON.parse(json);
}
function mes(str) {
  uni.showToast({
    title: str,
    icon: 'none'
  });
}
_vue.default.prototype.jump = jump;
_vue.default.prototype.mes = mes;
_vue.default.prototype.Godetails = Godetails;
_vue.default.prototype.ZH = ZH;
createApp(app).$mount();
/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/wx.js */ 1)["default"], __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/index.js */ 2)["default"], __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/index.js */ 2)["createApp"]))

/***/ }),

/***/ 27:
/*!********************************************************************!*\
  !*** D:/上海永熵/franchise-managementaiJMGL-project/wx_client/App.vue ***!
  \********************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./App.vue?vue&type=script&lang=js& */ 28);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));
/* harmony import */ var _App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./App.vue?vue&type=style&index=0&lang=scss& */ 36);
/* harmony import */ var _tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_runtime_componentNormalizer_js__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/runtime/componentNormalizer.js */ 38);
var render, staticRenderFns, recyclableRender, components
var renderjs





/* normalize component */

var component = Object(_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_runtime_componentNormalizer_js__WEBPACK_IMPORTED_MODULE_2__["default"])(
  _App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__["default"],
  render,
  staticRenderFns,
  false,
  null,
  null,
  null,
  false,
  components,
  renderjs
)

component.options.__file = "App.vue"
/* harmony default export */ __webpack_exports__["default"] = (component.exports);

/***/ }),

/***/ 28:
/*!*********************************************************************************************!*\
  !*** D:/上海永熵/franchise-managementaiJMGL-project/wx_client/App.vue?vue&type=script&lang=js& ***!
  \*********************************************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! -!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/babel-loader/lib!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--13-1!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/script.js!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!./App.vue?vue&type=script&lang=js& */ 29);
/* harmony import */ var _tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));
 /* harmony default export */ __webpack_exports__["default"] = (_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0___default.a); 

/***/ }),

/***/ 29:
/*!****************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** ./node_modules/babel-loader/lib!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--13-1!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/script.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!D:/上海永熵/franchise-managementaiJMGL-project/wx_client/App.vue?vue&type=script&lang=js& ***!
  \****************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(uni) {

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ 4);
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _regenerator = _interopRequireDefault(__webpack_require__(/*! @babel/runtime/regenerator */ 30));
var _asyncToGenerator2 = _interopRequireDefault(__webpack_require__(/*! @babel/runtime/helpers/asyncToGenerator */ 32));
var _auth = __webpack_require__(/*! @/utils/auth */ 33);
var _apiconfig = __webpack_require__(/*! @/utils/apiconfig.js */ 34);
var util = __webpack_require__(/*! @/utils/util.js */ 35);
var _default = {
  onLaunch: function () {
    var _onLaunch = (0, _asyncToGenerator2.default)( /*#__PURE__*/_regenerator.default.mark(function _callee(e) {
      var updateManager, whiteList, token, userType, currentPages, currentPage;
      return _regenerator.default.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              // 添加全局错误处理
              uni.onError(function (err) {
                console.error('全局错误：', err);
                uni.showModal({
                  title: '错误提示',
                  content: '应用出现异常，请重试',
                  showCancel: false
                });
              });
              // 检测是否可以调用getUpdateManager检查更新
              if (uni.canIUse("getUpdateManager")) {
                _context.next = 3;
                break;
              }
              return _context.abrupt("return");
            case 3:
              updateManager = uni.getUpdateManager(); // console.log(updateManager)
              // 获取全局唯一的版本更新管理器，用于管理小程序更新
              updateManager.onCheckForUpdate(function (res) {
                // 监听向微信后台请求检查更新结果事件 
                console.log("是否有新版本：" + res.hasUpdate);
                if (res.hasUpdate) {
                  //如果有新版本                
                  // 小程序有新版本，会主动触发下载操作        
                  updateManager.onUpdateReady(function () {
                    //当新版本下载完成，会进行回调          
                    uni.showModal({
                      title: '更新提示',
                      content: '新版本已经准备好，单击确定重启小程序',
                      showCancel: false,
                      success: function success(res) {
                        if (res.confirm) {
                          // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启小程序               
                          updateManager.applyUpdate();
                        }
                      }
                    });
                  });
                  // 小程序有新版本，会主动触发下载操作       
                  updateManager.onUpdateFailed(function () {
                    //当新版本下载失败，会进行回调          
                    uni.showModal({
                      title: '提示',
                      content: '检查到有新版本，但下载失败，请稍后尝试',
                      showCancel: false
                    });
                  });
                }
              });
              // 添加路由拦截
              whiteList = ['/pages/login/login', '/pages/login/serverLogin', '/pages/welcome/index', '/pages/welcome/customerIndex', '/pages/question/index', '/pages/prebook/index']; // 白名单页面
              // 拦截 navigateTo
              // uni.addInterceptor('navigateTo', {
              // 	invoke(e) {
              // 		if (!whiteList.includes(e.url.split('?')[0])) {
              // 			const token = getToken()
              // 			if (!token) {
              // 				uni.redirectTo({
              // 					url: '/pages/login/login'
              // 				})
              // 				return false
              // 			}
              // 			// 只在用户信息不存在时才请求
              // 			return true
              // 		}
              // 		return true
              // 	}
              // })
              // // 拦截 redirectTo
              // uni.addInterceptor('redirectTo', {
              // 	invoke(e) {
              // 		if (!whiteList.includes(e.url.split('?')[0])) {
              // 			const token = getToken()
              // 			if (!token) {
              // 				uni.redirectTo({
              // 					url: '/pages/login/login'
              // 				})
              // 				return false
              // 			}
              // 			return true
              // 		}
              // 		return true
              // 	}
              // })
              // 获取用户类型并跳转到对应页面
              token = (0, _auth.getToken)();
              if (token) {
                userType = (0, _auth.getUserType)();
                console.log("userType", userType);
                currentPages = getCurrentPages();
                currentPage = currentPages.length > 0 ? currentPages[currentPages.length - 1].route : ''; // 如果当前不在目标页面，才进行跳转
                if (userType === "1" && currentPage !== 'pages/index/serverIndex') {
                  console.log("跳转到员工端首页");
                  uni.reLaunch({
                    url: '/pages/index/serverIndex'
                  });
                } else if (userType !== "1" && currentPage !== 'pages/index/index') {
                  console.log("跳转到普通用户首页");
                  uni.reLaunch({
                    url: '/pages/index/index'
                  });
                }
              }
            case 8:
            case "end":
              return _context.stop();
          }
        }
      }, _callee);
    }));
    function onLaunch(_x) {
      return _onLaunch.apply(this, arguments);
    }
    return onLaunch;
  }(),
  onShow: function onShow() {
    // 如果WebSocket未连接或已关闭，重新连接
    if (!this.socketTask || this.socketTask.readyState !== 1) {
      this.initWebSocket();
    }
  },
  methods: {
    // 初始化WebSocket连接
    initWebSocket: function initWebSocket() {
      var _this = this;
      var userInfo = (0, _auth.getUserInfo)();
      if (!userInfo) {
        return;
      }
      // WebSocket服务器地址，使用与API相同的域名
      var wsUrl = _apiconfig.baseUrl.replace('http://', 'ws://').replace('https://', 'wss://') + 'wsServer';
      // 创建WebSocket连接
      this.socketTask = uni.connectSocket({
        url: wsUrl + "/" + userInfo.userId,
        success: function success() {
          console.log('WebSocket连接成功');
        },
        fail: function fail(err) {
          console.error('WebSocket连接失败', err);
        }
      });

      // 监听WebSocket连接打开
      this.socketTask.onOpen(function () {
        console.log('WebSocket连接已打开');

        // 监听WebSocket消息
        _this.socketTask.onMessage(function (res) {
          console.log('收到WebSocket消息:', res.data);
          if (res.data == '连接成功') {
            return;
          }
          try {
            // 解析消息数据
            var data = JSON.parse(res.data);
            // 获取当前用户ID
            var _userInfo = (0, _auth.getUserInfo)();
            var currentUserId = _userInfo ? _userInfo.userId : null;

            // 判断消息是否发给当前用户
            if (data.userId && data.userId === currentUserId) {
              // 显示消息弹窗
              _this.showMessagePopup(data);
            }
          } catch (e) {
            console.error('解析WebSocket消息失败', e);
          }
        });

        // 监听WebSocket关闭
        _this.socketTask.onClose(function () {
          console.log('WebSocket连接已关闭');
          // 可以在这里添加重连逻辑
          setTimeout(function () {
            _this.initWebSocket();
          }, 3000); // 3秒后尝试重连
        });

        // 监听WebSocket错误
        _this.socketTask.onError(function (err) {
          console.error('WebSocket错误', err);
        });
      });
    },
    // 显示消息弹窗
    // 修改App.vue中的showMessagePopup方法
    showMessagePopup: function showMessagePopup(data) {
      var _this2 = this;
      uni.showModal({
        title: data.title || '新消息',
        content: data.content || '',
        showCancel: data.showCancel !== false,
        // 默认显示取消按钮
        cancelText: data.cancelText || '关闭',
        confirmText: data.confirmText || '查看详情',
        success: function success(res) {
          if (res.confirm) {
            util.request('wx/api/readMessage', {
              id: data.id
            }, 'POST', 'application/json', false, true).then(function (res) {
              uni.hideLoading();
              if (res.code === 200) {
                _this2.noticelist = res.data;
              } else {
                uni.showToast({
                  title: res.msg || '获取失败',
                  icon: 'none'
                });
              }
            }).catch(function (err) {
              uni.hideLoading();
              uni.showToast({
                title: '网络异常，请稍后再试',
                icon: 'none'
              });
            });
            // 如果用户点击确认且有跳转链接，则跳转
            uni.navigateTo({
              url: "/pages/me/mynotice/mynotice"
            });
          }

          // 如果需要，可以在这里添加回调通知服务器消息已读
          if (data.messageId) {
            _this2.markMessageAsRead(data.messageId);
          }
        }
      });
    },
    // 标记消息为已读
    markMessageAsRead: function markMessageAsRead(messageId) {
      // 发送消息已读状态到服务器
    },
    // 关闭WebSocket连接
    closeWebSocket: function closeWebSocket() {
      if (this.socketTask) {
        this.socketTask.close({
          success: function success() {
            console.log('WebSocket连接已关闭');
          },
          fail: function fail(err) {
            console.error('关闭WebSocket连接失败', err);
          }
        });
      }
    }
  },
  onHide: function onHide() {
    console.log('App Hide');
    // 应用进入后台时不关闭WebSocket，保持连接以接收消息
  },

  onUnload: function onUnload() {
    // 应用卸载时关闭WebSocket连接
    this.closeWebSocket();
  },
  globalData: {
    userinfo: null,
    token: '',
    socketTask: null // 存储WebSocket连接对象
  }
};
exports.default = _default;
/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/index.js */ 2)["default"]))

/***/ }),

/***/ 36:
/*!******************************************************************************************************!*\
  !*** D:/上海永熵/franchise-managementaiJMGL-project/wx_client/App.vue?vue&type=style&index=0&lang=scss& ***!
  \******************************************************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_2_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_3_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_sass_loader_dist_cjs_js_ref_8_oneOf_1_4_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_5_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! -!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/mini-css-extract-plugin/dist/loader.js??ref--8-oneOf-1-0!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/css-loader/dist/cjs.js??ref--8-oneOf-1-1!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/stylePostLoader.js!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--8-oneOf-1-2!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/postcss-loader/src??ref--8-oneOf-1-3!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/sass-loader/dist/cjs.js??ref--8-oneOf-1-4!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--8-oneOf-1-5!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!../../../tools/HBuilderX.3.99.2023122611/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!./App.vue?vue&type=style&index=0&lang=scss& */ 37);
/* harmony import */ var _tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_2_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_3_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_sass_loader_dist_cjs_js_ref_8_oneOf_1_4_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_5_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_2_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_3_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_sass_loader_dist_cjs_js_ref_8_oneOf_1_4_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_5_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0__);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_2_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_3_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_sass_loader_dist_cjs_js_ref_8_oneOf_1_4_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_5_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_2_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_3_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_sass_loader_dist_cjs_js_ref_8_oneOf_1_4_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_5_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));
 /* harmony default export */ __webpack_exports__["default"] = (_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_2_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_3_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_sass_loader_dist_cjs_js_ref_8_oneOf_1_4_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_8_oneOf_1_5_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_tools_HBuilderX_3_99_2023122611_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0___default.a); 

/***/ }),

/***/ 37:
/*!**********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** ./node_modules/mini-css-extract-plugin/dist/loader.js??ref--8-oneOf-1-0!./node_modules/css-loader/dist/cjs.js??ref--8-oneOf-1-1!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/stylePostLoader.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--8-oneOf-1-2!./node_modules/postcss-loader/src??ref--8-oneOf-1-3!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/sass-loader/dist/cjs.js??ref--8-oneOf-1-4!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--8-oneOf-1-5!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!D:/上海永熵/franchise-managementaiJMGL-project/wx_client/App.vue?vue&type=style&index=0&lang=scss& ***!
  \**********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

// extracted by mini-css-extract-plugin
    if(false) { var cssReload; }
  

/***/ })

},[[0,"common/runtime","common/vendor"]]]);
//# sourceMappingURL=../../.sourcemap/mp-weixin/common/main.js.map