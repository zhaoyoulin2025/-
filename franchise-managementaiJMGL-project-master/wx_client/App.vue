<script>
import { getToken, setUserInfo, getUserType, getUserInfo } from '@/utils/auth'
const util = require("@/utils/util.js")
import { baseUrl } from '@/utils/apiconfig.js'

export default {
	onLaunch: async function (e) {
		// 添加全局错误处理
		uni.onError(function (err) {
			console.error('全局错误：', err)
			uni.showModal({
				title: '错误提示',
				content: '应用出现异常，请重试',
				showCancel: false
			})
		})
		// 检测是否可以调用getUpdateManager检查更新
		if (!uni.canIUse("getUpdateManager")) return;
		const updateManager = uni.getUpdateManager();
		// console.log(updateManager)
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
						success: function (res) {
							if (res.confirm) {
								// 新的版本已经下载好，调用 applyUpdate 应用新版本并重启小程序               
								updateManager.applyUpdate();
							}
						}
					})
				})
				// 小程序有新版本，会主动触发下载操作       
				updateManager.onUpdateFailed(function () {
					//当新版本下载失败，会进行回调          
					uni.showModal({
						title: '提示',
						content: '检查到有新版本，但下载失败，请稍后尝试',
						showCancel: false,
					})
				})
			}
		});
		// 添加路由拦截
		const whiteList = ['/pages/login/login', '/pages/login/serverLogin','/pages/welcome/index','/pages/welcome/customerIndex','/pages/question/index','/pages/prebook/index']  // 白名单页面

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
		const token = getToken()
		if (token) {
			const userType = getUserType()
			console.log("userType", userType)
			const currentPages = getCurrentPages()
			const currentPage = currentPages.length > 0 ? currentPages[currentPages.length - 1].route : ''

			// 如果当前不在目标页面，才进行跳转
			if (userType === "1" && currentPage !== 'pages/index/serverIndex') {
				console.log("跳转到员工端首页")
				uni.reLaunch({
					url: '/pages/index/serverIndex'
				})
			} else if (userType !== "1" && currentPage !== 'pages/index/index') {
				console.log("跳转到普通用户首页")
				uni.reLaunch({
					url: '/pages/index/index'
				})
			}
		}


	},
	onShow: function () {
		// 如果WebSocket未连接或已关闭，重新连接
		if (!this.socketTask || this.socketTask.readyState !== 1) {
			this.initWebSocket();
		}
	},
	methods: {
		// 初始化WebSocket连接
		initWebSocket() {
			const userInfo = getUserInfo()
			if (!userInfo) {
				return;
			}
			// WebSocket服务器地址，使用与API相同的域名
			const wsUrl = baseUrl.replace('http://', 'ws://').replace('https://', 'wss://') + 'wsServer';
			// 创建WebSocket连接
			this.socketTask = uni.connectSocket({
				url: wsUrl + "/" + userInfo.userId,
				success: () => {
					console.log('WebSocket连接成功');
				},
				fail: (err) => {
					console.error('WebSocket连接失败', err);
				}
			});

			// 监听WebSocket连接打开
			this.socketTask.onOpen(() => {
				console.log('WebSocket连接已打开');


				// 监听WebSocket消息
				this.socketTask.onMessage((res) => {
					console.log('收到WebSocket消息:', res.data);
					if (res.data == '连接成功') {
						return;
					}
					try {
						// 解析消息数据
						const data = JSON.parse(res.data);
						// 获取当前用户ID
						const userInfo = getUserInfo();
						const currentUserId = userInfo ? userInfo.userId : null;

						// 判断消息是否发给当前用户
						if (data.userId && data.userId === currentUserId) {
							// 显示消息弹窗
							this.showMessagePopup(data);
						}
					} catch (e) {
						console.error('解析WebSocket消息失败', e);
					}
				});

				// 监听WebSocket关闭
				this.socketTask.onClose(() => {
					console.log('WebSocket连接已关闭');
					// 可以在这里添加重连逻辑
					setTimeout(() => {
						this.initWebSocket();
					}, 3000); // 3秒后尝试重连
				});

				// 监听WebSocket错误
				this.socketTask.onError((err) => {
					console.error('WebSocket错误', err);
				});
			});
		},

		// 显示消息弹窗
		// 修改App.vue中的showMessagePopup方法
		showMessagePopup(data) {
			uni.showModal({
				title: data.title || '新消息',
				content: data.content || '',
				showCancel: data.showCancel !== false, // 默认显示取消按钮
				cancelText: data.cancelText || '关闭',
				confirmText: data.confirmText || '查看详情',
				success: (res) => {
					if (res.confirm) {
						util.request('wx/api/readMessage', { id: data.id }, 'POST', 'application/json', false, true)
							.then(res => {
								uni.hideLoading();
								if (res.code === 200) {
									this.noticelist = res.data;
								} else {
									uni.showToast({
										title: res.msg || '获取失败',
										icon: 'none'
									});
								}
							})
							.catch(err => {
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
						this.markMessageAsRead(data.messageId);
					}
				}
			});
		},

		// 标记消息为已读
		markMessageAsRead(messageId) {
			// 发送消息已读状态到服务器


		},

		// 关闭WebSocket连接
		closeWebSocket() {
			if (this.socketTask) {
				this.socketTask.close({
					success: () => {
						console.log('WebSocket连接已关闭');
					},
					fail: (err) => {
						console.error('关闭WebSocket连接失败', err);
					}
				});
			}
		}
	},
	onHide: function () {
		console.log('App Hide')
		// 应用进入后台时不关闭WebSocket，保持连接以接收消息
	},
	onUnload: function () {
		// 应用卸载时关闭WebSocket连接
		this.closeWebSocket();
	},
	globalData: {
		userinfo: null,
		token: '',
		socketTask: null, // 存储WebSocket连接对象
	},
}
</script>

<style lang="scss">
/*每个页面公共css */
@import "@/uni_modules/uview-ui/index.scss";
@import "colorui/main.css";
@import "colorui/icon.css";
</style>
