<template>
    <view class="detail-container">
        <!-- 顶部标题：xxx的详情 -->
        <view class="header-title">{{ schoolInfo.name || '' }}</view>

        <view class="form-group">
            <!-- 电话号码 -->
            <view class="form-item">
                <text class="label">电话号码</text>
                <view class="input-box">
                    <text>{{ schoolInfo.phone || '-' }}</text>
                </view>
            </view>

            <!-- 投资预算 -->
            <view class="form-item">
                <text class="label">投资预算</text>
                <view class="input-box">
                    <text>{{ schoolInfo.investBudget  }}</text>
                </view>
            </view>

            <!-- 计划开店市（意向城市） -->
            <view class="form-item">
                <text class="label">期望开店市</text>
                <view class="input-box">
                    <text>{{ safeText(schoolInfo.expectedCity || '-') }}</text>
                </view>
            </view>

            <!-- 租金预算 -->
            <view class="form-item">
                <text class="label">租金预算</text>
                <view class="input-box">
                    <text>{{ safeText(schoolInfo.rentBudget || '-') }}</text>
                </view>
            </view>

            <!-- 是否转让 -->
            <view class="form-item">
                <text class="label">是否接受转让</text>
                <view class="input-box">
                    <text>{{ formatYesNo(schoolInfo.isTransfor) }}</text>
                </view>
            </view>

            <!-- 经营参与方式 -->
            <view class="form-item">
                <text class="label">门店经营参与方式</text>
                <view class="input-box">
                    <text>{{ schoolInfo.joinType }}</text>
                </view>
            </view>

            <!-- 意向房产 -->
            <view class="form-item">
                <text class="label">您正在做的餐饮品牌</text>
                <view class="input-box">
                    <text>{{schoolInfo.industry }}</text>
                </view>
            </view>

            <!-- 项目经营情况 -->
            <view class="form-item">
                <text class="label">所做项目经营情况</text>
                <view class="input-box">
                    <text>{{ safeText(schoolInfo.projectCategory || schoolInfo.businessCategory) }}</text>
                </view>
            </view>

            <!-- 门店经营方式 -->
            <view class="form-item">
                <text class="label">门店投资方式</text>
                <view class="input-box">
                    <text>{{ schoolInfo.storeType}}</text>
                </view>
            </view>

            <!-- 加盟合作方式 -->
            <view class="form-item">
                <text class="label">品牌合作方式</text>
                <view class="input-box">
                    <text>{{schoolInfo.cooperationMode}}</text>
                </view>
            </view>

            <!-- 三项统计块：首次时间 / 预约时间 / 联系次数 -->
            <view class="stats-group">
                <view class="stats-item">
                    <text class="stats-label">首次咨询时间</text>
                    <text class="stats-value">{{ schoolInfo.firstTime }}</text>
                </view>
                <view class="stats-item">
                    <text class="stats-label">成交时间</text>
                    <text class="stats-value">{{ schoolInfo.dealTime || '-' }}</text>
                </view>
                <view class="stats-item">
                    <text class="stats-label">到访次数</text>
                    <text class="stats-value">{{ schoolInfo.comeTime || 0 }}</text>
                </view>
            </view>

            <!-- 所在地 -->
            <view class="form-item">
                <text class="label">所在地</text>
                <view class="input-box">
                    <text>{{ schoolInfo.residence  }}</text>
                </view>
            </view>

            <!-- 意向业态 -->
            <view class="form-item">
                <text class="label">如何看待转让费</text>
                <view class="input-box">
                    <text>{{schoolInfo.otherThink || '-' }}</text>
                </view>
            </view>

        </view>

        <!-- 底部写跟进按钮 -->
        <button class="follow-btn" @click="goToFollow">写跟进</button>
    </view>
</template>

<script>
const util = require("@/utils/util.js")
export default {
    data() {
        return {
            id: '', // 存储路由传递的id参数
            schoolInfo: {
                clientStatus: '',
                name: '',
                phone: '',
                clientLevel: ''
            },
        }
    },
    onLoad(options) {
        if (options.id) {
            this.id = options.id;
            this.getSchoolDetail();
        }
    },
    methods: {
        getSchoolDetail() {
            // 这里是模拟数据，实际开发时替换为接口调用
            let that = this;
            uni.showLoading({ title: '加载中', mask: true });
            util.request('wx/api/clientDetail', { id: this.id }, 'GET', 'application/json', false, true)
                .then(res => {
                    uni.hideLoading()
                    if (res.code === 200) {
                        that.schoolInfo = res.data;
                    } else {
                        uni.showToast({
                            title: res.msg,
                            icon: 'none'
                        })
                    }
                })
                .catch(err => {
                    uni.hideLoading()
                    uni.showToast({
                        title: '网络异常，请稍后再试',
                        icon: 'none'
                    })

                })

        },
        // 根据状态获取对应的文本
        getStatusText(status) {
            console.log('status', status);
            const statusTexts = {
                0: '潜在',
                2: '已邀约',
                3: '已面谈',
                4: '已签约',
                10: "已流失"
            };
            return statusTexts[status] || '待跟进';
        },

        getLevelText(level) {
            const levelTexts = {
                1: '普通客户',
                2: 'VIP客户',
            };
            return levelTexts[level] || '普通客户';
        },
        safeText(val) {
            return (val === null || val === undefined || val === '') ? '-' : val
        },
        formatYesNo(val) {
            if (val === 1) return '是'
            if (val === 0) return '否'
            return '-'
        },
        goToFollow() {
            if (!this.id) return
            uni.navigateTo({ url: `/customer/follow?id=${this.id}` })
        },
    }
}
</script>

<style scoped>
.detail-container {
    padding: 20rpx;
    background: #f5f5f5;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    align-items: stretch;
}
.header-title {
    width: 100%;
    text-align: center;
    font-size: 28rpx;
    color: #333;
    background: #fff;
    padding: 16rpx 0;
    border-radius: 12rpx;
}
.form-group {
    border-radius: 16rpx;
    padding: 0 30rpx;
    width: 100%;
    background-color: #fff;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
    margin-top: 16rpx;
}
.form-item {
    display: flex;
    align-items: center;
    padding: 26rpx 0;
    border-bottom: 1rpx solid #eee;
}
.form-item:last-child { border-bottom: none; }
.label {
    width: 240rpx;
    font-size: 26rpx;
    color: #666;
}
.input-box {
    flex: 1;
    font-size: 26rpx;
    color: #333;
    text-align: right;
}

/* 三项统计块样式 */
.stats-group {
    margin: 20rpx 0;
    padding: 20rpx;
    background: #f7f7f7;
    border-radius: 12rpx;
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 12rpx;
}
.stats-item {
    background: #fff;
    border-radius: 12rpx;
    padding: 16rpx;
    display: flex;
    flex-direction: column;
    align-items: center; /* 列方向的水平居中 */
}
.stats-label {
    font-size: 24rpx;
    color: #666;
    margin-bottom: 6rpx;
}
.stats-value {
    display: block;      /* 让文本占满一行 */
    width: 100%;
    text-align: center;  /* 水平居中 */
    font-size: 26rpx;
    color: #2979ff;
}

/* 底部按钮 */
.follow-btn {
    margin: 24rpx 20rpx 40rpx;
    height: 80rpx;
    line-height: 80rpx;
    border-radius: 12rpx;
    background: #2979ff;
    color: #fff;
    font-size: 28rpx;
}
</style>