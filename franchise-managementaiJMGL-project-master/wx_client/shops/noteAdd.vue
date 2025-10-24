<template>
    <view class="container">
        <!-- 商圈名称 -->
        <view class="question-item">
            <view class="question-title"><text class="required">*</text>商圈名称</view>
            <view class="input-wrap">
                <picker @change="businessChange" :value="businessIndex" :range="businessList" range-key="name">
                    <view class="picker-content">
                        {{ selectedBusinessName || '请选择商圈' }}
                    </view>
                </picker>
            </view>
        </view>

        <!-- 描述 -->
        <view class="question-item">
            <view class="question-title"><text class="required">*</text>描述</view>
            <view class="input-wrap">
                <textarea v-model="formData.description" placeholder="请输入描述信息" maxlength="500"
                    class="textarea-input"></textarea>
                <view class="char-count">{{ formData.description.length }}/500</view>
            </view>
        </view>


        <!-- 图片上传 -->
        <view class="question-item">
            <view class="question-title">图片</view>
            <view class="photo-wrap">
                <!-- 已上传的图片列表 -->
                <view class="photo-list">
                    <view class="photo-preview" v-for="(img, index) in formData.images" :key="index">
                        <image :src="img" mode="aspectFill"></image>
                        <view class="photo-delete" @tap="deleteImage(index)">×</view>
                    </view>

                    <!-- 添加图片按钮，当图片数量少于9张时显示 -->
                    <view class="photo-upload" @tap="takePhoto">
                        <text class="cuIcon-camera"></text>
                        <text>拍摄照片</text>
                    </view>
                </view>
            </view>
        </view>

        <!-- 视频上传 -->
        <view class="question-item">
            <view class="question-title">视频</view>
            <view class="video-wrap">
                <!-- 多视频预览 -->
                <view class="video-preview" v-for="(url, idx) in formData.videoUrls" :key="idx">
                    <video :src="url" controls></video>
                    <view class="video-controls">
                        <view class="video-delete-btn" @tap="deleteVideo(idx)">删除视频</view>
                    </view>
                </view>

                <!-- 上传入口：始终显示 -->
                <view class="video-upload" @tap="takeVideo">
                    <text class="cuIcon-video"></text>
                    <text>拍摄视频</text>
                </view>
                <view class="video-tip">可上传多段，每段不超过60秒</view>
            </view>
        </view>

        <!-- 定位地点 -->
        <view class="question-item">
            <view class="question-title"><text class="required">*</text>定位地点</view>
            <view class="location-wrap">
                
                <view class="form-item">
                    <view class="label">定位地点</view>
                    <view class="location-info">
                        <text class="location-text">{{ formData.checkinLocation || '正在获取位置...' }}</text>
                        <text class="refresh-btn cuIcon-refresh" @tap="refreshLocation"></text>
                    </view>
                </view>
                <button class="location-btn" @tap="getLocation">
                    {{ formData.checkinLocation ? '重新定位' : '获取当前位置' }}
                </button>
            </view>
        </view> 


        <view class="submit-wrap">
            <button @tap="submitForm" class="submit-btn">提交</button>
        </view>
    </view>
</template>

<script>
const util = require("@/utils/util.js");
import { baseUrl } from '../utils/apiconfig';

export default {
    data() {
        return {
            formData: {
                businessDistrictId: '', // 改为存储商圈ID
                businessName: '', // 保留商圈名称用于显示
                description: '',
                checkinLocation: '',
                latitude: 0,
                longitude: 0,
                images: [],
                videoUrl: '',   // 提交到后端：逗号拼接的字符串
                videoUrls: []   // 前端多视频列表
            },
            businessList: [], // 商圈列表
            businessIndex: 0, // 选中的商圈索引
            selectedBusinessName: '' // 选中的商圈名称
        }
    },
    onLoad() {
        // 页面加载时获取商圈列表和位置
        this.getBusinessList();
        this.getLocation();
    },
    methods: {
        // 获取商圈列表
        getBusinessList() {
            uni.showLoading({
                title: '加载中...'
            });

            util.request("wx/api/districtList", {}, "GET")
                .then(res => {
                    uni.hideLoading();
                    if (res.code === 200) {
                        this.businessList = res.data || [];
                        console.log('商圈列表：', this.businessList);
                    } else {
                        uni.showToast({
                            title: res.msg || '获取商圈列表失败',
                            icon: 'none'
                        });
                    }
                })
                .catch(err => {
                    uni.hideLoading();
                    console.error('获取商圈列表失败', err);
                    uni.showToast({
                        title: '获取商圈列表失败',
                        icon: 'none'
                    });
                });
        },

        // 商圈选择改变
        businessChange(e) {
            const index = e.detail.value;
            this.businessIndex = index;

            if (this.businessList[index]) {
                this.formData.businessDistrictId = this.businessList[index].id;
                this.formData.businessName = this.businessList[index].name;
                this.selectedBusinessName = this.businessList[index].name;
                console.log('选中商圈：', this.businessList[index]);
            }
        },

        // 获取当前位置
        getLocation() {
            uni.showLoading({
                title: '定位中...'
            });

            uni.getLocation({
                type: 'gcj02',
                geocode: true,
                success: (res) => {
                    console.log('获取位置成功', res);
                    this.formData.latitude = res.latitude;
                    this.formData.longitude = res.longitude;

                    // 使用逆地理编码获取位置详情
                    this.getLocationName(res.latitude, res.longitude);
                },
                fail: (err) => {
                    console.error('获取位置失败', err);
                    uni.hideLoading();
                    this.formData.location = '无法获取位置信息';

                    // 提示用户开启位置权限
                    uni.showModal({
                        title: '提示',
                        content: '需要获取您的位置信息，请开启位置权限',
                        confirmText: '去设置',
                        cancelText: '取消',
                        success: (res) => {
                            if (res.confirm) {
                                uni.openSetting();
                            }
                        }
                    });
                }
            });
        },

        // 获取位置名称
        getLocationName(latitude, longitude) {
            let that = this;

            util.request("wx/api/getLocation", {
                latitude: latitude,
                longitude: longitude
            }, "GET").then(function (res) {
                uni.hideLoading();
                console.log('获取地址信息', res);
                if (res.code === 200) {
                    that.formData.checkinLocation = res.data;
                } else {
                    // 如果接口获取失败，使用坐标作为备选
                    that.formData.checkinLocation = `${latitude.toFixed(6)}, ${longitude.toFixed(6)}`;
                    uni.showToast({
                        title: '无法获取详细地址，已使用坐标信息',
                        icon: 'none'
                    });
                }
            }).catch(function (err) {
                uni.hideLoading();
                console.error('获取地址失败', err);
                // 如果接口调用失败，使用坐标作为备选
                that.formData.checkinLocation = `${latitude.toFixed(6)}, ${longitude.toFixed(6)}`;
                uni.showToast({
                    title: '无法获取详细地址，已使用坐标信息',
                    icon: 'none'
                });
            });
        },

        // 重新获取位置（用户手动触发）
        refreshLocation() {
            this.getLocation();
        },

        // 拍摄照片（参考 collect.vue 的逻辑）
        takePhoto() {
            uni.chooseImage({
                count: 100, // 最多9张图片
                sizeType: ['compressed'],
                sourceType: ['camera', 'album'],
                success: (res) => {
                    const tempFilePath = res.tempFilePaths[0];
                    // 先将本地图片路径添加到数组，让用户立即看到拍摄的照片
                    const tempIndex = this.formData.images.length;
                    this.formData.images.push(tempFilePath);
                    // 然后上传图片到服务器
                    this.uploadImage(tempFilePath, tempIndex);
                },
                fail: (err) => {
                    console.error('选择图片失败', err);
                    uni.showToast({
                        title: '选择图片失败',
                        icon: 'none'
                    });
                }
            });
        },

        // 上传图片到服务器（参考 collect.vue 的逻辑）
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
                            that.formData.images[index] = res.url;
                            console.log("上传成功，图片URL:", res.url, "索引：", index);
                            uni.showToast({
                                title: '上传成功',
                                icon: 'success'
                            });
                        } else {
                            // 上传失败，从数组中移除该图片
                            that.formData.images.splice(index, 1);
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
                        that.formData.images.splice(index, 1);
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
                that.formData.images.splice(index, 1);
                console.error('上传过程发生错误', e);
                uni.showToast({
                    icon: "none",
                    title: "上传失败，请稍后再试",
                    duration: 2000
                });
            }
        },

        // 删除图片
        deleteImage(index) {
            this.formData.images.splice(index, 1);
        },

        // 拍摄视频（参考 collect.vue 的逻辑）
        takeVideo() {
            uni.chooseVideo({
                sourceType: ['camera', 'album'], // 从相机拍摄或相册选择
                maxDuration: 60, // 最长拍摄时间，单位秒
                camera: 'back', // 默认拉起后置摄像头
                compressed: true, // 是否压缩所选视频
                success: (res) => {
                    // 返回选定视频的本地文件路径
                    const tempFilePath = res.tempFilePath;
                    // 然后上传视频到服务器
                    this.uploadVideo(tempFilePath);
                },
                fail: (err) => {
                    console.error('选择视频失败', err);
                    uni.showToast({
                        title: '选择视频失败',
                        icon: 'none'
                    });
                }
            });
        },

        // 上传视频到服务器（参考 collect.vue 的逻辑）
        uploadVideo(filePath) {
            console.log('上传视频：', filePath);

            // 保存当前页面的this引用
            const that = this;

            uni.showLoading({
                title: '上传中...'
            });

            // 使用uni.uploadFile上传视频文件
            uni.uploadFile({
                url: baseUrl + 'common/upload', // SpringBoot接收视频的接口
                filePath: filePath, // 临时视频路径
                name: 'file', // 文件对应的 key，开发者在服务器端通过这个 key 获取文件的二进制内容
                header: {
                    'Authorization': util.getToken()
                },
                success: (res) => {
                    uni.hideLoading();

                    console.log('上传响应：', res);

                    // 解析服务器返回的JSON数据
                    const resData = JSON.parse(res.data);
                    if (resData.code === 200) {
                        const url = resData.url;
                        // 多视频：加入数组并同步拼接字符串
                        this.formData.videoUrls.push(url);
                        this.formData.videoUrl = this.formData.videoUrls.join(',');
                        uni.showToast({ title: '上传成功', icon: 'success' });
                    } else {
                        console.error('上传失败', res);
                        uni.showToast({
                            icon: "none",
                            title: resData.msg || "上传失败，请稍后再试",
                            duration: 2000
                        });
                    }
                },
                fail: (err) => {
                    uni.hideLoading();
                    console.error('上传请求失败', err);
                    uni.showToast({
                        icon: "none",
                        title: "网络错误，请稍后再试",
                        duration: 2000
                    });
                }
            });
        },

        deleteVideo(index) {
            this.formData.videoUrls.splice(index, 1);
            this.formData.videoUrl = this.formData.videoUrls.join(',');
            uni.showToast({ title: '视频已删除', icon: 'success' });
        },

        validateForm() {
            if (!this.formData.businessDistrictId) {
                uni.showToast({ title: '请选择商圈', icon: 'none' });
                return false;
            }
            if (!this.formData.description || !this.formData.description.trim()) {
                uni.showToast({ title: '请输入描述信息', icon: 'none' });
                return false;
            }
            if (!this.formData.checkinLocation) {
                uni.showToast({ title: '请获取定位信息', icon: 'none' });
                return false;
            }
             if (!this.formData.images) {
                uni.showToast({ title: '请上传图片', icon: 'none' });
                return false;
            }
             if (!this.formData.videoUrl) {
                uni.showToast({ title: '请上传视频', icon: 'none' });
                return false;
            }
            return true;
        },

        submitForm() {
            if (!this.validateForm()) return;
            uni.showLoading({ title: '提交中...' });

            // 提交前确保字符串与数组同步
            this.formData.videoUrl = this.formData.videoUrls.join(',');

            const submitData = {
                businessDistrictId: this.formData.businessDistrictId,
                description: this.formData.description,
                checkinLocation: this.formData.checkinLocation,
                imageUrl: this.formData.images.join(','),
                videoUrl: this.formData.videoUrl
            };

            util.request("wx/api/addNote", submitData, "POST")
                .then(res => {
                    uni.hideLoading();
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
                            title: res.msg || '提交失败',
                            icon: 'none'
                        });
                    }
                })
                .catch(err => {
                    uni.hideLoading();
                    console.error('提交失败', err);
                    uni.showToast({
                        title: '提交失败，请稍后再试',
                        icon: 'none'
                    });
                });
        }
    }
}
</script>

<style>
.container {
    padding: 30rpx;
    background: #f5f5f5;
    min-height: 100vh;
}

.question-item {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    padding: 30rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.question-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
}

.required {
    color: #ff0000;
}

.input-wrap {
    position: relative;
}

.input-wrap input {
    width: 100%;
    height: 80rpx;
    background: #f8f8f8;
    border-radius: 8rpx;
    padding: 0 20rpx;
    font-size: 28rpx;
    border: none;
}

.textarea-input {
    width: 100%;
    min-height: 120rpx;
    background: #f8f8f8;
    border-radius: 8rpx;
    padding: 20rpx;
    font-size: 28rpx;
    border: none;
    resize: none;
}

.char-count {
    position: absolute;
    bottom: 10rpx;
    right: 20rpx;
    font-size: 24rpx;
    color: #999;
}

/* 定位相关样式 */
.location-wrap {
    display: flex;
    flex-direction: column;
    gap: 20rpx;
}

.location-info {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20rpx;
    background-color: #f8f8f8;
    border-radius: 8rpx;
    margin-top: 10rpx;
}

.location-text {
    flex: 1;
    color: #666;
    font-size: 28rpx;
}

.refresh-btn {
    color: #007aff;
    font-size: 32rpx;
    padding: 10rpx;
}

.location-info .cuIcon-location {
    color: #1890ff;
    font-size: 32rpx;
    margin-right: 10rpx;
}

.location-text {
    flex: 1;
    font-size: 28rpx;
    color: #333;
}

.location-btn {
    height: 80rpx;
    line-height: 80rpx;
    background: #1890ff;
    color: #fff;
    border-radius: 8rpx;
    font-size: 28rpx;
    border: none;
}

/* 图片上传样式 */
.photo-wrap {
    margin-top: 20rpx;
}

.photo-list {
    display: flex;
    flex-wrap: wrap;
    margin: -10rpx;
}

.photo-preview {
    position: relative;
    width: 200rpx;
    height: 200rpx;
    border-radius: 8rpx;
    overflow: hidden;
    margin: 10rpx;
}

.photo-preview image {
    width: 100%;
    height: 100%;
}

.photo-delete {
    position: absolute;
    top: 0;
    right: 0;
    width: 40rpx;
    height: 40rpx;
    background: rgba(0, 0, 0, 0.5);
    color: #fff;
    text-align: center;
    line-height: 40rpx;
    font-size: 32rpx;
}

.photo-upload {
    width: 200rpx;
    height: 200rpx;
    background: #f8f8f8;
    border-radius: 8rpx;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: #999;
    margin: 10rpx;
}

.photo-upload .cuIcon-camera {
    font-size: 48rpx;
    margin-bottom: 10rpx;
}

.photo-tip {
    font-size: 24rpx;
    color: #999;
    margin-top: 10rpx;
}

/* 视频上传样式 */
.video-wrap {
    margin-top: 20rpx;
}

.video-preview {
    position: relative;
    width: 100%;
    border-radius: 8rpx;
    overflow: hidden;
    margin-bottom: 20rpx;
}

.video-preview video {
    width: 100%;
    height: 400rpx;
}

.video-controls {
    display: flex;
    justify-content: flex-end;
    padding: 10rpx 0;
}

.video-delete-btn {
    background-color: #ff4d4f;
    color: #fff;
    padding: 10rpx 20rpx;
    border-radius: 8rpx;
    font-size: 28rpx;
}

.video-upload {
    width: 100%;
    height: 200rpx;
    background: #f8f8f8;
    border-radius: 8rpx;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: #999;
}

.video-upload .cuIcon-video {
    font-size: 48rpx;
    margin-bottom: 10rpx;
}

.video-tip {
    font-size: 24rpx;
    color: #999;
    margin-top: 10rpx;
}

.submit-wrap {
    margin-top: 40rpx;
    padding: 0 20rpx;
}

.submit-btn {
    width: 100%;
    height: 88rpx;
    line-height: 88rpx;
    background: #2979FF;
    color: #fff;
    font-size: 32rpx;
    border-radius: 8rpx;
    border: none;
}
</style>