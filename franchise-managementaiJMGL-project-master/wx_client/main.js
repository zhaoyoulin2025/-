import App from './App'
// import request from 'utils/request.js'  //引入异步请求函数
import share from '@/utils/share.js'
Vue.mixin(share)
// #ifndef VUE3
import Vue from 'vue'
// uview
import uView from '@/uni_modules/uview-ui'
Vue.use(uView)

// 导入自定义导航栏组件
import cuCustom from './colorui/components/cu-custom.vue'
// 注册自定义导航栏组件
Vue.component('cu-custom', cuCustom)

// 设置全局状态栏高度等信息
uni.getSystemInfo({
    success: e => {
        Vue.prototype.StatusBar = e.statusBarHeight;
        let custom = wx.getMenuButtonBoundingClientRect();
        Vue.prototype.Custom = custom;
        Vue.prototype.CustomBar = custom.bottom + custom.top - e.statusBarHeight;
    }
});
// Vue.prototype.request = request.request //挂载到全局
// let imgurldev = 'http://192.168.10.5:8083'
let imgurl = 'https://ga.rasmall.cn/prod-api'
Vue.prototype.myimgurl = function () {  
  return imgurl;  
};
// 注册自定义 tabBar 组件
import CustomTabBar from './components/custom-tab-bar.vue'
Vue.component('custom-tab-bar', CustomTabBar)
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
    ...App
})
function jump(url){
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

function Godetails(v,url){
	console.log(JSON.stringify(v))
	uni.navigateTo({
		url:url+'?params='+ JSON.stringify(v)
	})
}
function ZH(json){
	return JSON.parse(json)
}
function mes(str){
	uni.showToast({
		title:str,
		icon:'none'
	})
}
Vue.prototype.jump = jump;
Vue.prototype.mes = mes;
Vue.prototype.Godetails = Godetails;
Vue.prototype.ZH = ZH;
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif