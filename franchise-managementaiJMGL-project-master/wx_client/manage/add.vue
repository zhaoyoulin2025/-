<template>
    <view class="container">
        <!-- 页面标题 -->
        <view class="header">
            <text class="title">监理报告</text>
        </view>

        <view class="form-container">
            <!-- 检查日期 -->
            <view class="form-item">
                <text class="form-label">检查日期</text>
                <picker mode="date" :value="inspectionDate" @change="onDateChange">
                    <view class="date-picker">
                        <text class="date-text" :class="{ placeholder: !inspectionDate }">
                            {{ inspectionDate || '年-月-日' }}
                        </text>
                        <text class="date-icon">📅</text>
                    </view>
                </picker>
            </view>

            <view class="form-item">
                <view class="form-label">检查项目</view>
                <view class="input-wrap">
                    <input type="text" v-model="project" class="input-field" placeholder="请输入检查项目" />
                </view>
            </view>

            <!-- 施工进度 -->
            <view class="form-item">
                <text class="form-label">施工进度</text>
                <view class="progress-container">
                    <slider :value="progress" @change="onProgressChange" min="0" max="100" step="1"
                        activeColor="#007AFF" backgroundColor="#E5E5E5" block-size="20" show-value />
                </view>
            </view>

            <!-- 施工质量 -->
            <view class="form-item">
                <text class="form-label">施工质量</text>
                <picker :range="qualityOptions" @change="onQualityChange">
                    <view class="quality-picker">
                        <text class="quality-text" :class="{ placeholder: !selectedQuality }">
                            {{ selectedQuality || '请选择' }}
                        </text>
                        <text class="picker-arrow">▼</text>
                    </view>
                </picker>
            </view>

            <!-- 发现的问题 -->
            <view class="form-item">
                <text class="form-label">发现的问题</text>
                <textarea v-model="issues" class="textarea" placeholder="请描述发现的问题..." maxlength="500"
                    :show-confirm-bar="false" />
            </view>

            <!-- 整改建议 -->
            <view class="form-item">
                <text class="form-label">整改建议</text>
                <textarea v-model="suggestions" class="textarea" placeholder="请提出整改建议..." maxlength="500"
                    :show-confirm-bar="false" />
            </view>

            <!-- 现场照片 -->
            <view class="form-item">
                <text class="form-label">现场照片</text>
                <view class="photo-container">
                    <view class="photo-list">
                        <view v-for="(photo, index) in photoList" :key="index" class="photo-item">
                            <image :src="photo" class="photo-image" @click="previewImage(photo)" />
                            <view class="photo-delete" @click="deletePhoto(index)">×</view>
                        </view>
                        <view v-if="photoList.length < 9" class="photo-add" @click="chooseImage">
                            <text class="add-icon">+</text>
                            <text class="add-text">选择文件</text>
                        </view>
                    </view>
                    <text v-if="photoList.length === 0" class="photo-placeholder">未选择任何文件</text>
                </view>
            </view>

            <!-- 提交按钮 -->
            <view class="submit-container">
                <button class="submit-btn" @click="submitReport" :disabled="submitting">
                    {{ submitting ? '提交中...' : '提交报告' }}
                </button>
            </view>
        </view>
    </view>
</template>

<script>
const util = require("@/utils/util.js")
import { getUserInfo } from '@/utils/auth'

export default {
    data() {
        return {
            inspectionDate: '',
            progress: 30,
            selectedQuality: '',
            issues: '',
            project: '',
            suggestions: '',
            photoList: [],
            submitting: false,
            qualityOptions: ['优秀', '良好', '合格', '不合格'],
            shopId: '' // 从上一页传入的店铺ID
        }
    },
    onLoad(options) {
        // 获取传入的店铺ID
        if (options.id) {
            this.shopId = options.id;
        }

        // 设置默认日期为今天
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const day = String(today.getDate()).padStart(2, '0');
        this.inspectionDate = `${year}-${month}-${day}`;
    },
    methods: {
        // 日期改变
        onDateChange(e) {
            this.inspectionDate = e.detail.value;
        },

        // 进度改变
        onProgressChange(e) {
            this.progress = e.detail.value;
        },

        // 质量选择改变
        onQualityChange(e) {
            this.selectedQuality = this.qualityOptions[e.detail.value];
        },

        // 选择图片
        chooseImage() {
            const remainCount = 9 - this.photoList.length;
            uni.chooseImage({
                count: remainCount,
                sizeType: ['compressed'],
                sourceType: ['camera', 'album'],
                success: (res) => {
                    // 遍历选择的图片，逐个上传
                    res.tempFilePaths.forEach((filePath, index) => {
                        // 先添加到显示列表
                        this.photoList.push(filePath);
                        // 上传图片
                        this.uploadImage(filePath, this.photoList.length - 1);
                    });
                },
                fail: (err) => {
                    uni.showToast({
                        title: '选择图片失败',
                        icon: 'none'
                    });
                }
            });
        },

        // 上传图片到服务器（参考 shops/collect.vue）
        uploadImage(filePath, index) {
            console.log('上传图片：', filePath, '索引：', index);

            // 保存当前页面的this引用
            const that = this;

            uni.showLoading({
                title: '上传中...'
            });

            try {
                // 微信小程序读取文件
                const base64 = 'data:image/jpeg;base64,' + wx.getFileSystemManager().readFileSync(filePath, 'base64');

                var reqData = {
                    "base64Str": base64
                };

                util.request("common/uploadImageWater", reqData, "POST")
                    .then((res) => {
                        uni.hideLoading(); // 在请求完成后隐藏加载提示

                        if (res.code === 200) {
                            // 更新对应索引位置的图片URL为服务器返回的URL
                            that.photoList[index] = res.url;
                            console.log("上传成功，图片URL:", res.url, "索引：", index);
                            uni.showToast({
                                title: '上传成功',
                                icon: 'success'
                            });
                        } else {
                            // 上传失败，从数组中移除该图片
                            that.photoList.splice(index, 1);
                            console.error('上传失败', res);
                            uni.showToast({
                                icon: "none",
                                title: res.msg || "上传失败，请稍后再试",
                                duration: 2000
                            });
                        }
                    })
                    .catch((err) => {
                        uni.hideLoading();
                        // 上传失败，从数组中移除该图片
                        that.photoList.splice(index, 1);
                        console.error('上传请求失败', err);
                        uni.showToast({
                            icon: "none",
                            title: "网络错误，请稍后再试",
                            duration: 2000
                        });
                    });
            } catch (e) {
                uni.hideLoading();
                // 上传失败，从数组中移除该图片
                that.photoList.splice(index, 1);
                console.error('上传过程发生错误', e);
                uni.showToast({
                    icon: "none",
                    title: "上传失败，请稍后再试",
                    duration: 2000
                });
            }
        },

        // 预览图片
        previewImage(current) {
            uni.previewImage({
                current: current,
                urls: this.photoList
            });
        },

        // 删除图片
        deletePhoto(index) {
            uni.showModal({
                title: '提示',
                content: '确定要删除这张照片吗？',
                success: (res) => {
                    if (res.confirm) {
                        this.photoList.splice(index, 1);
                    }
                }
            });
        },

        // 表单验证
        validateForm() {
            if (!this.inspectionDate) {
                uni.showToast({
                    title: '请选择检查日期',
                    icon: 'none'
                });
                return false;
            }

            if (!this.project) {
                uni.showToast({
                    title: '请输入检查项目',
                    icon: 'none'
                });
                return false;
            }


            if (!this.selectedQuality) {
                uni.showToast({
                    title: '请选择施工质量',
                    icon: 'none'
                });
                return false;
            }

            if (!this.issues.trim()) {
                uni.showToast({
                    title: '请填写发现的问题',
                    icon: 'none'
                });
                return false;
            }

            if (!this.suggestions.trim()) {
                uni.showToast({
                    title: '请填写整改建议',
                    icon: 'none'
                });
                return false;
            }

            return true;
        },

        // 提交报告
        submitReport() {
            if (!this.validateForm()) {
                return;
            }

            this.submitting = true;

            // 提交数据
            const reportData = {
                shopId: this.shopId,
                createDate: this.inspectionDate,
                progress: this.progress,
                quality: this.selectedQuality,
                question: this.issues.trim(),
                suggestion: this.suggestions.trim(),
                img: this.photoList.join(','),// 直接使用已上传的图片URL数组
                project: this.project

            };

            console.log(reportData)

            util.request('wx/api/insertBuildManage', reportData, 'POST', 'application/json', false, true)
            	.then(res => {
            		this.submitting = false;
            		if (res.code === 200) {
            			uni.showToast({
            				title: '提交成功',
            				icon: 'success'
            			});

            			// 延迟返回上一页
            			setTimeout(() => {
            				uni.navigateBack();
            			}, 1500);
            		} else {
            			uni.showToast({
            				title: res.msg || '提交失败，请重试',
            				icon: 'none'
            			});
            		}
            	})
            	.catch(error => {
            		this.submitting = false;
            		console.error('提交报告失败:', error);
            		uni.showToast({
            			title: '提交失败，请重试',
            			icon: 'none'
            		});
            	});
        }
    }
}
</script>

<style scoped>
.container {
    min-height: 100vh;
    background-color: #f5f5f5;
}

.header {
    background-color: #fff;
    padding: 20rpx 30rpx;
    border-bottom: 1rpx solid #eee;
}

.title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
}

.form-container {
    padding: 30rpx;
}

.form-item {
    background-color: #fff;
    margin-bottom: 30rpx;
    padding: 30rpx;
    border-radius: 12rpx;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.form-label {
    display: block;
    font-size: 32rpx;
    font-weight: 500;
    color: #333;
    margin-bottom: 20rpx;
}

/* 日期选择器样式 */
.date-picker {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20rpx;
    border: 2rpx solid #e5e5e5;
    border-radius: 8rpx;
    background-color: #fafafa;
}

.date-text {
    font-size: 30rpx;
    color: #333;
}

.date-text.placeholder {
    color: #999;
}

.date-icon {
    font-size: 32rpx;
    color: #666;
}

/* 进度条样式 */
.progress-container {
    position: relative;
}

.progress-text {
    position: absolute;
    right: 0;
    top: -10rpx;
    font-size: 28rpx;
    color: #007AFF;
    font-weight: 500;
}

/* 质量选择器样式 */
.quality-picker {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20rpx;
    border: 2rpx solid #e5e5e5;
    border-radius: 8rpx;
    background-color: #fafafa;
}

.quality-text {
    font-size: 30rpx;
    color: #333;
}

.quality-text.placeholder {
    color: #999;
}

.picker-arrow {
    font-size: 24rpx;
    color: #666;
}

/* 文本域样式 */
.textarea {
    width: 100%;
    min-height: 200rpx;
    padding: 20rpx;
    border: 2rpx solid #e5e5e5;
    border-radius: 8rpx;
    font-size: 30rpx;
    line-height: 1.5;
    background-color: #fafafa;
    box-sizing: border-box;
}

/* 照片容器样式 */
.photo-container {
    margin-top: 20rpx;
}

.photo-list {
    display: flex;
    flex-wrap: wrap;
    gap: 20rpx;
}

.photo-item {
    position: relative;
    width: 200rpx;
    height: 200rpx;
}

.photo-image {
    width: 100%;
    height: 100%;
    border-radius: 8rpx;
    object-fit: cover;
}

.photo-delete {
    position: absolute;
    top: -10rpx;
    right: -10rpx;
    width: 40rpx;
    height: 40rpx;
    background-color: #ff4757;
    color: #fff;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24rpx;
    font-weight: bold;
}

.photo-add {
    width: 200rpx;
    height: 200rpx;
    border: 2rpx dashed #ccc;
    border-radius: 8rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background-color: #fafafa;
}

.add-icon {
    font-size: 48rpx;
    color: #999;
    margin-bottom: 10rpx;
}

.add-text {
    font-size: 24rpx;
    color: #999;
}

.photo-placeholder {
    font-size: 28rpx;
    color: #999;
    text-align: center;
    padding: 40rpx 0;
}

/* 提交按钮样式 */
.submit-container {
    margin-top: 50rpx;
    padding: 0 30rpx;
}

.submit-btn {
    width: 100%;
    height: 88rpx;
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    color: #fff;
    font-size: 32rpx;
    font-weight: 500;
    border-radius: 44rpx;
    border: none;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 8rpx 20rpx rgba(0, 122, 255, 0.3);
}

.submit-btn:disabled {
    background: #ccc;
    box-shadow: none;
}

.submit-btn:active {
    transform: translateY(2rpx);
    box-shadow: 0 4rpx 12rpx rgba(0, 122, 255, 0.3);
}

/* 移除点击反馈样式 */
.date-picker,
.quality-picker,
.photo-add,
.submit-btn {
    -webkit-tap-highlight-color: transparent;
    -webkit-touch-callout: none;
    user-select: none;
    outline: none;
}

/* 输入框容器样式 */
.input-wrap {
	width: 100%;
}

/* 输入框样式 */
.input-field {
	width: 100%;
	height: 80rpx;
	padding: 0 20rpx;
	border: 2rpx solid #e5e5e5;
	border-radius: 8rpx;
	font-size: 30rpx;
	color: #333;
	background-color: #fafafa;
	box-sizing: border-box;
}
</style>