<template>
    <view class="container">

        <!-- 示例：为输入框添加禁用状态 -->
        <view class="question-item">
            <view class="question-title"><text class="required">*</text>1：店铺名称</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.name" placeholder="请输入店铺名称" :disabled="isReadOnly" />
            </view>
        </view>
        
        <!-- 为地区选择器添加禁用状态 -->
        <view class="question-item">
            <view class="question-title"><text class="required">*</text>2：店铺所在的地区</view>
            <view class="input-wrap">
                <picker mode="region" @change="regionChange" :value="region" :disabled="isReadOnly">
                    <view class="picker-content">
                        {{ region[0] + ' ' + region[1] + ' ' + region[2] || '请选择店铺所在的地区' }}
                    </view>
                </picker>
            </view>
        </view>
        
    
        
        <view class="question-item">
            <view class="question-title"><text class="required">*</text>3：店铺具体地址</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.address" placeholder="请输入店铺具体地址" />
            </view>
        </view>

       

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>5：店铺面积</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.shopSize" placeholder="请输入店铺面积(m²)" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>6：店铺门宽</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.menkuan" placeholder="请输入店铺门宽" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>7：店铺外摆</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.waibai" placeholder="请输入店铺外摆" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>8：店铺年限</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.nianxian" placeholder="请输入店铺年限" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>9：店铺租金 （元/月）</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.rent" placeholder="请输入店铺租金" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>10：店铺租金年递增</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.dizneg" placeholder="请输入店铺租金年递增" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>11：店铺装修免租期</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.norent" placeholder="请输入店铺装修免租期" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>12：店铺电费</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.dian" placeholder="请输入店铺电费" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>13：店铺水费</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.shui" placeholder="请输入店铺水费" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>14：店铺装修押金</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.yajin" placeholder="请输入店铺装修押金" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>15：店铺转让费</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.zhuanrangfei" placeholder="请输入店铺转让费" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>16：店铺工程条件</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.acondition" placeholder="请输入店铺工程条件" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>17：店铺广告位</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.guanggao" placeholder="请输入店铺广告位" />
            </view>
        </view>


        <view class="question-item">
            <view class="question-title"><text class="required">*</text>18：店铺总结</view>
            <view class="input-wrap">
                <input type="text" v-model="formData.allDesc" placeholder="请输入店铺总结" />
            </view>
        </view>

        <view class="question-item">
            <view class="question-title"><text class="required">*</text>19：店铺照片</view>
            <view class="photo-wrap">
                <!-- 已上传的图片列表 -->
                <view class="photo-list">
                    <view class="photo-preview" v-for="(img, index) in formData.shopImages" :key="index">
                        <image :src="img" mode="aspectFill"></image>
                        <view  v-if="!isReadOnly" class="photo-delete" @tap="deleteImage(index)">×</view>
                    </view>

                    <!-- 添加图片按钮，当图片数量少于3张时显示 -->
                    <view class="photo-upload" @tap="takePhoto" v-if="formData.shopImages.length < 3">
                        <text class="cuIcon-camera"></text>
                        <text>拍摄照片</text>
                    </view>
                </view>
                <view class="photo-tip">最多可上传3张照片</view>
            </view>
        </view>

        <!-- 新增视频上传部分 -->
        <view class="question-item">
            <view class="question-title"><text class="required">*</text>20：店铺视频</view>
            <view class="video-wrap">
                <!-- 已上传的视频预览 -->
                <view class="video-preview" v-if="formData.videoUrl">
                    <video :src="formData.videoUrl" controls></video>
                    <view class="video-controls" v-if="!isReadOnly"> 
                        <view class="video-delete-btn" @tap="deleteVideo">删除视频</view>
                    </view>
                </view>

                <!-- 添加视频按钮，当没有视频时显示 -->
                <view class="video-upload" @tap="takeVideo" v-if="!formData.videoUrl">
                    <text class="cuIcon-video"></text>
                    <text>拍摄视频</text>
                </view>
                <view class="video-tip">可上传一段不超过60秒的视频</view>
            </view>
        </view>



        <view class="submit-wrap" v-if="!isReadOnly">
            <button @tap="submitForm" class="submit-btn">修改</button>
        </view>
    </view>
</template>

<script>
const util = require("@/utils/util.js")
import {
    setUserInfo,
    getUserInfo
} from '@/utils/auth';
import { baseUrl } from '../utils/apiconfig';
export default {
    data() {
        return {
            shopId: null, // 店铺ID
            mode: 'view', // 页面模式：view(查看) 或 edit(编辑)
            isReadOnly: true, // 是否只读模式
            formData: {
                id:null,
                address: '',
                name: '',
                residence: '',
                province: '', // 新增省份字段
                city: '', // 新增城市字段
                district: '', // 新增区县字段
                shopImages: [], // 修改为数组，存储多张图片
                videoUrl: '', // 新增视频字段
                rent:'',
                area:'',
                imgUrl:'',
                shopSize:'',
                menkuan:'',
                waibai:'',
                nianxian:'',
                dizneg:'',
                norent:'',
                dian:"",
                shui:"",
                yajin:"",
                zhuanrangfei:"",
                acondition:"",
                guanggao:"",
                allDesc:"",


            },
            region: ['', '', ''], // 存储省市区选择结果
        }
    },
    // 添加 onLoad 生命周期钩子
    onLoad(options) {
        console.log('页面参数：', options);
        
        // 获取页面参数
        this.shopId = options.id;
        this.mode = options.mode || 'view';
        this.isReadOnly = this.mode === 'view';
        
        // 设置页面标题
        uni.setNavigationBarTitle({
            title: this.mode === 'view' ? '店铺详情' : '编辑店铺'
        });
        
        // 如果有店铺ID，则获取店铺详情
        if (this.shopId) {
            this.getShopDetail();
        }
    },
    methods: {
        // 新增省市区选择器变化事件处理函数
        regionChange(e) {
            this.region = e.detail.value;
            this.formData.province = this.region[0];
            this.formData.city = this.region[1];
            this.formData.district = this.region[2];
            // 同时更新residence字段，保持兼容性
            this.formData.residence = this.region.join(' ');
        },
        // 拍照方法
        takePhoto() {
            // 如果已经有3张图片，不再允许上传
            if (this.formData.shopImages.length >= 3) {
                uni.showToast({
                    title: '最多只能上传3张图片',
                    icon: 'none'
                });
                return;
            }

            uni.chooseImage({
                count: 1, // 每次选择1张图片
                sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
                sourceType: ['camera', 'album'], // 从相机拍摄或相册选择
                success: (res) => {
                    // 返回选定照片的本地文件路径列表
                    const tempFilePath = res.tempFilePaths[0];
                    // 先将本地图片路径添加到数组，让用户立即看到拍摄的照片
                    const tempIndex = this.formData.shopImages.length;
                    this.formData.shopImages.push(tempFilePath);
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

        // 上传图片到服务器
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
                            that.formData.shopImages[index] = res.url;
                            console.log("上传成功，图片URL:", res.url, "索引：", index);
                            uni.showToast({
                                title: '上传成功',
                                icon: 'success'
                            });
                        } else {
                            // 上传失败，从数组中移除该图片
                            that.formData.shopImages.splice(index, 1);
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
                        that.formData.shopImages.splice(index, 1);
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
                that.formData.shopImages.splice(index, 1);
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
            this.formData.shopImages.splice(index, 1);
        },
        // 拍摄视频方法
        takeVideo() {
            uni.chooseVideo({
                sourceType: ['camera', 'album'], // 从相机拍摄或相册选择
                maxDuration: 60, // 最长拍摄时间，单位秒
                camera: 'back', // 默认拉起后置摄像头
                compressed: true, // 是否压缩所选视频
                success: (res) => {
                    // 返回选定视频的本地文件路径
                    const tempFilePath = res.tempFilePath;
                    // 先将本地视频路径添加到数据模型，让用户立即看到拍摄的视频
                    // this.formData.videoUrl = tempFilePath;
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

        // 上传视频到服务器
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
                        // 更新视频URL为服务器返回的URL
                        that.formData.videoUrl = resData.url;
                        console.log("上传成功，视频URL:", resData.url);
                        uni.showToast({
                            title: '上传成功',
                            icon: 'success'
                        });
                    } else {
                        console.error('上传失败', res);
                        uni.showToast({
                            icon: "none",
                            title: res.msg || "上传失败，请稍后再试",
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
        // 删除视频
        deleteVideo() {
            this.formData.videoUrl = '';
            uni.showToast({
                title: '视频已删除',
                icon: 'success'
            });
        },
        getShopDetail() {
            uni.showLoading({
                title: '加载中...'
            });
            
            util.request(`wx/api/shopDetail`, { id: this.shopId }, "GET")
                .then((res) => {
                    uni.hideLoading();
                    
                    if (res.code === 200 && res.data) {
                        const shopData = res.data;
                        this.formData.id = shopData.id;
                        // 赋值基本信息
                        this.formData.name = shopData.name || '';
                        this.formData.address = shopData.address || '';
                        this.formData.shopSize = shopData.shopSize || '';
                        this.formData.menkuan = shopData.menkuan || '';
                        this.formData.waibai = shopData.waibai || '';
                        this.formData.nianxian = shopData.nianxian || '';
                        this.formData.rent = shopData.rent || '';
                        this.formData.dizneg = shopData.dizneg || '';
                        this.formData.norent = shopData.norent || '';
                        this.formData.dian = shopData.dian || '';
                        this.formData.shui = shopData.shui || '';
                        this.formData.yajin = shopData.yajin || '';
                        this.formData.zhuanrangfei = shopData.zhuanrangfei || '';
                        this.formData.acondition = shopData.acondition || '';
                        this.formData.guanggao = shopData.guanggao || '';
                        this.formData.allDesc = shopData.allDesc || '';
                        
                        // 处理地区信息
                        if (shopData.area) {
                            const areaArray = shopData.area.split(',');
                            if (areaArray.length >= 3) {
                                this.region = [areaArray[0], areaArray[1], areaArray[2]];
                                this.formData.province = areaArray[0];
                                this.formData.city = areaArray[1];
                                this.formData.district = areaArray[2];
                                this.formData.residence = shopData.area;
                            }
                        }
                        
                        // 处理图片信息
                        if (shopData.imgUrl) {
                            this.formData.shopImages = shopData.imgUrl.split(',').filter(img => img.trim());
                        }
                        
                        // 处理视频信息
                        this.formData.videoUrl = shopData.videoUrl || '';
                        
                        console.log('店铺详情加载完成：', this.formData);
                    } else {
                        uni.showToast({
                            title: res.msg || '获取店铺详情失败',
                            icon: 'none'
                        });
                    }
                })
                .catch((err) => {
                    uni.hideLoading();
                    console.error('获取店铺详情失败：', err);
                    uni.showToast({
                        title: '网络错误，请稍后重试',
                        icon: 'none'
                    });
                });
        },
        // 修改 submitForm 方法，添加照片验证
        submitForm() {
            if (!this.formData.name) {
                uni.showToast({
                    title: '请输入店铺名称',
                    icon: 'none'
                });
                return;
            }
            if (!this.formData.province || !this.formData.city || !this.formData.district) {
                uni.showToast({
                    title: '请选择店铺所在地区',
                    icon: 'none'
                });
                return;
            }
            
            if (!this.formData.address) {
                uni.showToast({
                    title: '请输入店铺详细地址',
                    icon: 'none'
                });
                return; 
            }
            
            if (!this.formData.shopSize) {
                uni.showToast({
                    title: '请输入店铺面积',
                    icon: 'none'
                });
                return; 
            }
            if (!this.formData.menkuan) {
                uni.showToast({
                    title: '请输入店铺门宽',
                    icon: 'none'
                });
                return; 
            }
            if (!this.formData.waibai) {
                uni.showToast({
                    title: '请输入店铺外摆',
                    icon: 'none'
                });
                return; 
            }
            if (!this.formData.nianxian) {
                uni.showToast({
                    title: '请输入店铺年限',
                    icon: 'none'
                });
                return; 
            }
            if (!this.formData.rent) {
                uni.showToast({
                    title: '请输入店铺租金',
                    icon: 'none'
                });
                return; 
            }
            if (!this.formData.dizneg) {
                uni.showToast({
                    title: '请输入店铺租金年递增',
                    icon: 'none'
                });
                return; 
            }
            if (!this.formData.norent) {
                uni.showToast({
                    title: '请输入店铺装修免租期',
                    icon: 'none'
                });
                return; 
            }
            if (!this.formData.dian) {
                uni.showToast({
                    title: '请输入店铺电费',
                    icon: 'none'
                });
                return; 
            }
            if (!this.formData.shui) {
                uni.showToast({
                    title: '请输入店铺水费',
                    icon: 'none'
                });
                return;
            }
            if(!this.formData.yajin){
                uni.showToast({
                    title: '请输入店铺押金',
                    icon: 'none'
                });
                return;
            }
            if(!this.formData.zhuanrangfei){
                uni.showToast({
                    title: '请输入店铺转让费',
                    icon: 'none'
                });
                return;
            }
            if(!this.formData.acondition){
                uni.showToast({
                    title: '请输入店铺工程条件',
                    icon: 'none'
                });
                return;
            }
            if(!this.formData.guanggao){
                uni.showToast({
                    title: '请输入店铺广告位',
                    icon: 'none'
                });
                return;
            }
            if(this.formData.shopImages.length<=0){
                uni.showToast({
                    title: '请上传店铺照片',
                    icon: 'none'
                });
                return;
            }
            if (!this.formData.videoUrl) {
                uni.showToast({
                    title: '请上传店铺视频',
                    icon: 'none'
                });
                return; 
            }
            
            this.formData.area = this.formData.province + ',' + this.formData.city + ',' + this.formData.district;
            this.formData.imgUrl = this.formData.shopImages.join(',');
            console.log('表单数据：', this.formData);
            
            uni.showLoading({
                title: '提交中',
                mask: true
            })
            util.request("wx/api/updateShop", this.formData, "POST").then(function (res) {
                uni.hideLoading();
                if (res.code === 200) {
                    uni.navigateBack({
                        delta: 1
                    });
                } else {
                    uni.showToast({
                        icon: "none",
                        title: res.msg,
                        duration: 2000
                    })
                }
            })
        }
    }
}
</script>

<style>
.container {
    padding: 30rpx;
    background: #fff;
}

.question-item {
    margin-bottom: 40rpx;
}

.question-title {
    font-size: 28rpx;
    color: #333;
    margin-bottom: 20rpx;
}

.select-wrap {
    padding: 0 20rpx;
}

.picker-content {
    height: 80rpx;
    line-height: 80rpx;
    background: #f8f8f8;
    border-radius: 8rpx;
    padding: 0 20rpx;
    font-size: 28rpx;
    color: #333;
}

.input-wrap {
    padding: 20rpx;
}

.input-wrap input {
    width: 100%;
    height: 80rpx;
    background: #f8f8f8;
    border-radius: 8rpx;
    padding: 0 20rpx;
    font-size: 28rpx;
}

.submit-wrap {
    margin-top: 60rpx;
    padding: 0 40rpx;
}

.submit-btn {
    width: 100%;
    height: 88rpx;
    line-height: 88rpx;
    background: #2979FF;
    color: #fff;
    font-size: 32rpx;
    border-radius: 8rpx;
    margin-bottom: 40rpx;
}

.required {
    color: #ff0000;
}

.photo-wrap {
    padding: 20rpx;
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
    padding-left: 10rpx;
}

/* 视频上传相关样式 */
.video-wrap {
    padding: 20rpx;
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
    padding-left: 10rpx;
}
</style>
