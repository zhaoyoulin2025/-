<template>
    <view class="container">

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>商圈名称：</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.name" placeholder="请输入商圈名称" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>商圈地址：</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.address" placeholder="请输入商圈地址" />

            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>所属区域</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.region" placeholder="请输入所属区域" />
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
                name: '', // 保留商圈名称用于显示
                address: '',
                region:''
            },
        }
    },
    onLoad() {
    },
    methods: {



        // 表单验证
        validateForm() {
            if (!this.formData.name.trim()) {
                uni.showToast({
                    title: '请输入商圈名称',
                    icon: 'none'
                });
                return false;
            }

            if (!this.formData.address.trim()) {
                uni.showToast({
                    title: '请输入商圈地址',
                    icon: 'none'
                });
                return false;
            }

            if (!this.formData.region.trim()) {
                uni.showToast({
                    title: '请输入所属区域',
                    icon: 'none'
                });
                return false;
            }



            return true;
        },

        // 提交表单
        submitForm() {
            if (!this.validateForm()) {
                return;
            }

            uni.showLoading({
                title: '提交中...'
            });

            const submitData = {
                name: this.formData.name,
                address: this.formData.address,
                region: this.formData.region

            };

            util.request("wx/api/wxInsertDistrict", submitData, "POST")
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