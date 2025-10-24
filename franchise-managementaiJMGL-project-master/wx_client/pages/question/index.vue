<template>
	<view class="container">

		<!-- 原有的内容 -->

		<!-- 添加提示语 -->
		<view class="notice-banner">
			<text class="notice-text">为方便更高效的服务您，请协助我们进一步了解您</text>
		</view>

		<view class="question-item">
			<view class="question-title"><text class="required">*</text>1：您的姓名</view>
			<view class="input-wrap">
				<input type="text" v-model="formData.name" placeholder="请输入您的姓名" />
			</view>
		</view>

		<view class="question-item">
			<view class="question-title"><text class="required">*</text>2：您的电话</view>
			<view class="input-wrap">
				<input type="text" v-model="formData.phone" placeholder="请输入您的电话" disabled />
			</view>
		</view>

		<view class="question-item">
			<view class="question-title"><text class="required">*</text>3：您的微信号</view>
			<view class="input-wrap">
				<input type="text" v-model="formData.wechat" placeholder="请输入您的微信号" />
			</view>
		</view>


		<view class="question-item">
			<view class="question-title"><text class="required">*</text>4：您所在的城市</view>
			<view class="select-wrap">
				<view class="city-selector" @tap="showCurrentCityPicker">
					<view class="picker-content">
						{{ selectedCurrentCityName || '请选择您所在的城市' }}
					</view>
					<view class="picker-arrow">></view>
				</view>
			</view>
		</view>

		<view class="question-item">
			<view class="question-title"><text class="required">*</text>5：您经常光顾哪几家店</view>
			<view class="input-wrap">
				<input type="text" v-model="formData.frequentStore" placeholder="请输入您经常光顾哪几家店" />
			</view>
		</view>

		<view class="question-item">
			<view class="question-title"><text class="required">*</text>6：门店投资方式</view>
			<view class="select-wrap">
				<picker @change="storyTypeChange" :value="storeTypeIndex" :range="storeTypeOptions" range-key="text">
					<view class="picker-content">
						{{ storeTypeOptions[storeTypeIndex].text }}
					</view>
				</picker>
			</view>
			<!-- <view class="select-wrap">
				<picker @change="firstLevelChange" :value="firstLevelIndex" :range="firstLevelOptions" range-key="text">
					<view class="picker-content">
						{{ firstLevelIndex !== null ? firstLevelOptions[firstLevelIndex].text : '请选择一级选项' }}
					</view>
				</picker>
			</view>
			<view class="select-wrap" style="margin-top: 20rpx;"
				v-if="firstLevelIndex !== null && currentSecondLevelOptions && currentSecondLevelOptions.length > 0">
				<picker @change="secondLevelChange" :value="secondLevelIndex" :range="currentSecondLevelOptions"
					range-key="text">
					<view class="picker-content">
						{{ secondLevelIndex !== null ? currentSecondLevelOptions[secondLevelIndex].text : '请选择二级选项' }}
					</view>
				</picker>
			</view>
			<view class="select-wrap" style="margin-top: 20rpx;"
				v-if="secondLevelIndex !== null && currentThirdLevelOptions && currentThirdLevelOptions.length > 0">
				<picker @change="thirdLevelChange" :value="thirdLevelIndex" :range="currentThirdLevelOptions"
					range-key="text">
					<view class="picker-content">
						{{ thirdLevelIndex !== null ? currentThirdLevelOptions[thirdLevelIndex].text : '请选择三级选项' }}
					</view>
				</picker>
			</view>
			<view class="select-wrap" style="margin-top: 20rpx;"
				v-if="thirdLevelIndex !== null && currentFourthLevelOptions && currentFourthLevelOptions.length > 0">
				<picker @change="fourthLevelChange" :value="fourthLevelIndex" :range="currentFourthLevelOptions"
					range-key="text">
					<view class="picker-content">
						{{ fourthLevelIndex !== null ? currentFourthLevelOptions[fourthLevelIndex].text : '请选择四级选项' }}
					</view>
				</picker>
			</view> -->
		</view>

		<!-- <view class="question-item" v-if="shouldShowRemarks">
			<view class="input-wrap">
				<input type="text" v-model="formData.remarks" placeholder="请输入您加盟过的品牌" />
			</view>
		</view> -->

		<view class="question-item">
			<view class="question-title"><text class="required">*</text>7.门店经营参与方式</view>
			<view class="select-wrap">
				<picker @change="joinTypeChange" :value="joinTypeIndex" :range="joinTypeOptions" range-key="text">
					<view class="picker-content">
						{{ joinTypeOptions[joinTypeIndex].text }}
					</view>
				</picker>
			</view>
		</view>

		<view class="question-item">
			<view class="question-title"><text class="required">*</text>8：品牌合作方式</view>
			<view class="input-wrap">
				<picker @change="budgetChange" :value="budgetIndex" :range="budgetOptions" range-key="text">
					<view class="picker-content">
						{{ budgetOptions[budgetIndex].text }}
					</view>
				</picker>
			</view>
		</view>

		<view class="question-item">
			<view class="question-title"><text class="required">*</text>9：您的投资预算</view>
			<view class="select-wrap">
				<picker @change="investmentBudgetChange" :value="investmentBudgetIndex" :range="investmentBudgetOptions"
					range-key="text">
					<view class="picker-content">
						{{ investmentBudgetOptions[investmentBudgetIndex].text }}
					</view>
				</picker>
			</view>
		</view>

		

		<view class="question-item">
			<view class="question-title"><text class="required">*</text>10：期望开店城市</view>
			<view class="select-wrap">
				<view class="city-selector" @tap="showCityPicker">
					<view class="picker-content">
						{{ selectedCityName || '请选择开店城市' }}
					</view>
					<view class="picker-arrow">></view>
				</view>
			</view>
		</view>

		<!-- 省市联动选择弹窗 -->
		<view v-if="showCityModal" class="city-modal" @tap="hideCityPicker">
			<view class="city-modal-content" @tap.stop>
				<view class="city-modal-header">
					<text class="city-modal-title">{{ cityPickerType === 'current' ? '选择当前城市' : '选择期望开店城市' }}</text>
					<text class="city-modal-close" @tap="hideCityPicker">×</text>
				</view>
				<view class="province-city-container">
					<!-- 省份列表 -->
					<scroll-view class="province-list" scroll-y>
						<view v-for="(province, index) in provinceList" :key="index" class="province-item"
							:class="{ active: selectedProvinceIndex === index }" @tap="selectProvince(index)">
							<text class="province-name">{{ province.name }}</text>
						</view>
					</scroll-view>
					<!-- 城市列表 -->
					<scroll-view class="city-list" scroll-y>
						<view v-for="(city, index) in currentCityList" :key="index" class="city-item"
							@tap="selectCity(city)">
							<text class="city-name">{{ city }}</text>
						</view>
						<view v-if="currentCityList.length === 0" class="no-data">
							<text>请先选择省份</text>
						</view>
					</scroll-view>
				</view>
			</view>
		</view>

		<view class="question-item">
			<view class="question-title"><text class="required">*</text>11.您正在做哪些餐饮品牌</view>
			<view class="input-wrap">
				<input type="text" v-model="formData.industry" placeholder="请输入" />
			</view>
			<!-- <view class="radio-group">
				<view class="radio-item" @tap="selectIndustry(0)">
					<view class="radio-circle" :class="{ active: industryIndex === 0 }"></view>
					<text>餐饮行业</text>
				</view>
				<view class="radio-item" @tap="selectIndustry(1)">
					<view class="radio-circle" :class="{ active: industryIndex === 1 }"></view>
					<text>零售行业</text>
				</view>
				<view class="radio-item" @tap="selectIndustry(2)">
					<view class="radio-circle" :class="{ active: industryIndex === 2 }"></view>
					<text>备注</text>
					<input v-if="industryIndex === 2" type="text" v-model="industryRemark" placeholder="请输入备注"
						class="remark-input" />
				</view>
			</view> -->
		</view>

		<!-- <view class="question-item">
			<view class="question-title"><text class="required">*</text>10.如何看待螺蛳粉在餐饮行业内的发展？</view>
			<view class="input-wrap">
				<input type="text" v-model="formData.developmentOpinion" placeholder="请输入" />
			</view>
		</view> -->

		<view class="question-item">
			<view class="question-title"><text class="required">*</text>12.您认为柒小螺的优势是什么？</view>
			<view class="input-wrap">
				<input type="text" v-model="formData.selectionReason" placeholder="请输入" />
			</view>
		</view>

		<view class="question-item">
			<view class="question-title"><text class="required">*</text>13.您觉得柒小螺哪些方面需要提升或优化？</view>
			<view class="input-wrap">
				<input type="text" v-model="formData.improvementSuggestion" placeholder="请输入" />
			</view>
		</view>

		<!-- <view class="question-item">
			<view class="question-title"><text class="required">*</text>14.您认为的商圈？</view>
			<view class="radio-group">
				<view class="radio-item" @tap="selectBusiness(0)">
					<view class="radio-circle" :class="{ active: businessIndex === 0 }"></view>
					<text>同商圈，晚上8点，火锅品牌A比快餐品牌B人气高，所以B品牌不如A品牌效益好。</text>
				</view>
				<view class="radio-item" @tap="selectBusiness(1)">
					<view class="radio-circle" :class="{ active: businessIndex === 1 }"></view>
					<text>同商圈，同时间段，A品牌和B品牌人气看起来都很一般，所以A品牌和B品牌都是差的品牌。</text>
				</view>
				<view class="radio-item" @tap="selectBusiness(2)">
					<view class="radio-circle" :class="{ active: businessIndex === 2 }"></view>
					<text>同商圈，同时间段，同品类，快餐A品牌堂食人气比B品牌堂食人气高10%左右，所以A比B更赚钱。</text>
				</view>
				<view class="radio-item" @tap="selectBusiness(3)">
					<view class="radio-circle" :class="{ active: businessIndex === 3 }"></view>
					<text>同商圈，同时间段，同品类，A品牌用户群体更年轻、学生偏多，B品牌比A品牌用户群体年龄更大一些，所以A品牌优于B品牌。</text>
				</view>
				<view class="radio-item" @tap="selectBusiness(4)">
					<view class="radio-circle" :class="{ active: businessIndex === 4 }"></view>
					<text>同商圈，同品类，A品牌有外卖，B品牌没有外卖，原因就是B品牌堂食生意足够好，没必要上外卖，没有其他不上外卖的原因。</text>
				</view>
				<view class="radio-item" @tap="selectBusiness(5)">
					<view class="radio-circle" :class="{ active: businessIndex === 5 }"></view>
					<text>不同商圈，A商圈A品牌人气高，B商圈B品牌人气一般，所以A品牌效益优于B品牌。</text>
				</view>
			</view>
		</view> -->












		<view class="submit-wrap">
			<button @tap="submitForm" class="submit-btn">提交</button>
		</view>
	</view>
</template>

<script>
const util = require("@/utils/util.js")
import {
	getUserInfo, setUserInfo
} from '@/utils/auth';
// 省市联动数据
const provinceData = {
	"北京市": ["北京市"],
	"天津市": ["天津市"],
	"上海市": ["上海市"],
	"重庆市": ["重庆市"],
	"河北省": ["石家庄市", "张家口市", "承德市", "秦皇岛市", "唐山市", "廊坊市", "保定市", "衡水市", "沧州市", "邢台市", "邯郸市"],
	"山西省": ["太原市", "朔州市", "大同市", "阳泉市", "长治市", "晋城市", "忻州市", "晋中市", "临汾市", "吕梁市", "运城市"],
	"内蒙古自治区": ["呼和浩特市", "包头市", "乌海市", "赤峰市", "通辽市", "呼伦贝尔市", "鄂尔多斯市", "乌兰察布市", "巴彦淖尔市"],
	"辽宁省": ["沈阳市", "朝阳市", "阜新市", "铁岭市", "抚顺市", "本溪市", "辽阳市", "鞍山市", "丹东市", "大连市", "营口市", "盘锦市", "锦州市", "葫芦岛市"],
	"吉林省": ["长春市", "白城市", "松原市", "吉林市", "四平市", "辽源市", "通化市", "白山市"],
	"黑龙江省": ["哈尔滨市", "齐齐哈尔市", "七台河市", "黑河市", "大庆市", "鹤岗市", "伊春市", "佳木斯市", "双鸭山市", "鸡西市", "牡丹江市", "绥化市"],
	"江苏省": ["南京市", "徐州市", "连云港市", "宿迁市", "淮安市", "盐城市", "扬州市", "泰州市", "南通市", "镇江市", "常州市", "无锡市", "苏州市"],
	"浙江省": ["杭州市", "湖州市", "嘉兴市", "舟山市", "宁波市", "绍兴市", "衢州市", "金华市", "台州市", "温州市", "丽水市"],
	"安徽省": ["合肥市", "宿州市", "淮北市", "亳州市", "阜阳市", "蚌埠市", "淮南市", "滁州市", "马鞍山市", "芜湖市", "铜陵市", "安庆市", "黄山市", "六安市", "巢湖市", "池州市", "宣城市"],
	"福建省": ["福州市", "南平市", "莆田市", "三明市", "泉州市", "厦门市", "漳州市", "龙岩市", "宁德市"],
	"江西省": ["南昌市", "九江市", "景德镇市", "鹰潭市", "新余市", "萍乡市", "赣州市", "上饶市", "抚州市", "宜春市", "吉安市"],
	"山东省": ["济南市", "青岛市", "聊城市", "德州市", "东营市", "淄博市", "潍坊市", "烟台市", "威海市", "日照市", "临沂市", "枣庄市", "济宁市", "泰安市", "莱芜市", "滨州市", "菏泽市"],
	"河南省": ["郑州市", "开封市", "三门峡市", "洛阳市", "焦作市", "新乡市", "鹤壁市", "安阳市", "濮阳市", "商丘市", "许昌市", "漯河市", "平顶山市", "南阳市", "信阳市", "周口市", "驻马店市", "济源市"],
	"湖北省": ["武汉市", "十堰市", "襄樊市", "荆门市", "孝感市", "黄冈市", "鄂州市", "黄石市", "咸宁市", "荆州市", "宜昌市", "随州市"],
	"湖南省": ["长沙市", "张家界市", "常德市", "益阳市", "岳阳市", "株洲市", "湘潭市", "衡阳市", "郴州市", "永州市", "邵阳市", "怀化市", "娄底市"],
	"广东省": ["广州市", "深圳市", "清远市", "韶关市", "河源市", "梅州市", "潮州市", "汕头市", "揭阳市", "汕尾市", "惠州市", "东莞市", "珠海市", "中山市", "江门市", "佛山市", "肇庆市", "云浮市", "阳江市", "茂名市", "湛江市"],
	"广西壮族自治区": ["南宁市", "桂林市", "柳州市", "梧州市", "贵港市", "玉林市", "钦州市", "北海市", "防城港市", "崇左市", "百色市", "河池市", "来宾市", "贺州市"],
	"海南省": ["海口市", "三亚市"],
	"四川省": ["成都市", "广元市", "绵阳市", "德阳市", "南充市", "广安市", "遂宁市", "内江市", "乐山市", "自贡市", "泸州市", "宜宾市", "攀枝花市", "巴中市", "达州市", "资阳市", "眉山市", "雅安市"],
	"贵州省": ["贵阳市", "六盘水市", "遵义市", "安顺市"],
	"云南省": ["昆明市", "曲靖市", "玉溪市", "保山市", "昭通市", "丽江市", "思茅市", "临沧市"],
	"西藏自治区": ["拉萨市"],
	"陕西省": ["西安市", "延安市", "铜川市", "渭南市", "咸阳市", "宝鸡市", "汉中市", "榆林市", "安康市", "商洛市"],
	"甘肃省": ["兰州市", "嘉峪关市", "白银市", "天水市", "武威市", "酒泉市", "张掖市", "庆阳市", "平凉市", "定西市", "陇南市"],
	"青海省": ["西宁市"],
	"宁夏回族自治区": ["银川市", "石嘴山市", "吴忠市", "固原市", "中卫市"],
	"新疆维吾尔自治区": ["乌鲁木齐市", "克拉玛依市"]
};

export default {
	data() {
		return {
			formData: {
				investmentBudget: '50w以下',
				storeType: '个人单独投资',
				phone: '',
				name: '',
				residence: '',
				improvementSuggestion: '',
				developmentOpinion: '',
				selectionReason: '',
				businessDistrict: '',
				cooperationMode: '单店加盟',
				remarks: '',
				industry: '',
				expectedCity: '',
				expectedStoreAddress: '',
				clientId: '',
				wechat: '',
				joinType: '在店经营',
			},
			businessIndex: null,
			investmentBudgetIndex: 0,
			investmentBudgetOptions: [{
				text: '20w-40w',
				value: '20w-40w'
			}, {
				text: '40w-60w',
				value: '40w-60w'
			}, {
				text: '60w-80w',
				value: '60w-80w'
			}],
			storeTypeIndex: 0,
			storeTypeOptions: [{
				text: '个人单独投资',
				value: '个人单独投资'
			}, {
				text: '合伙人共同投资',
				value: '合伙人共同投资'
			}],
			joinTypeIndex: 0,
			joinTypeOptions: [{
				text: '在店经营',
				value: '在店经营'
			}, {
				text: '聘请职业经理人',
				value: '聘请职业经理人'
			}],
			region: [], // 存储省市区
			regionValue: [], // 省市区选择器的值
			expectRegion: [], // 期望开店城市选择器的值
			expectRegionValue: [], // 期望开店城市选择器的值
			budgetIndex: 0,
			budgetOptions: [{
				text: '单店加盟',
				value: '单店加盟'
			}, {
				text: '多店加盟',
				value: '多店加盟'
			}, {
				text: '城市合伙人',
				value: '城市合伙人'
			}],
			businessOptions: [{
				text: '同商圈，晚上8点，火锅品牌A比快餐品牌B人气高，所以B品牌不如A品牌效益好。',
				value: '同商圈，晚上8点，火锅品牌A比快餐品牌B人气高，所以B品牌不如A品牌效益好。'
			}, {
				text: '同商圈，同时间段，A品牌和B品牌人气看起来都很一般，所以A品牌和B品牌都是差的品牌。',
				value: '同商圈，同时间段，A品牌和B品牌人气看起来都很一般，所以A品牌和B品牌都是差的品牌。'
			}, {
				text: '同商圈，同时间段，同品类，快餐A品牌堂食人气比B品牌堂食人气高10%左右，所以A比B更赚钱。',
				value: '同商圈，同时间段，同品类，快餐A品牌堂食人气比B品牌堂食人气高10%左右，所以A比B更赚钱。'
			},
			{
				text: '同商圈，同时间段，同品类，A品牌用户群体更年轻、学生偏多，B品牌比A品牌用户群体年龄更大一些，所以A品牌优于B品牌。',
				value: '同商圈，同时间段，同品类，A品牌用户群体更年轻、学生偏多，B品牌比A品牌用户群体年龄更大一些，所以A品牌优于B品牌。'
			},
			{
				text: '同商圈，同品类，A品牌有外卖，B品牌没有外卖，原因就是B品牌堂食生意足够好，没必要上外卖，没有其他不上外卖的原因。',
				value: '同商圈，同品类，A品牌有外卖，B品牌没有外卖，原因就是B品牌堂食生意足够好，没必要上外卖，没有其他不上外卖的原因。'
			},
			{
				text: '不同商圈，A商圈A品牌人气高，B商圈B品牌人气一般，所以A品牌效益优于B品牌。',
				value: '不同商圈，A商圈A品牌人气高，B商圈B品牌人气一般，所以A品牌效益优于B品牌。'
			}],
			// 四级联动选择器相关数据
			firstLevelIndex: null,
			secondLevelIndex: null,
			thirdLevelIndex: null,
			fourthLevelIndex: null,
			// 存储每级选择的值，用于最终拼接
			selectedOptions: {
				first: '',
				second: '',
				third: '',
				fourth: ''
			},
			// 一级选项
			firstLevelOptions: [
				{ text: '我来投资/做副业', value: '我来投资/做副业' },
				{ text: '有人给我投资', value: '有人给我投资' },
				{ text: '又是投资者又是经营者', value: '又是投资者又是经营者' },
			],
			// 二级选项（根据一级选项动态变化）
			secondLevelOptionsMap: {
				'我来投资/做副业': [
					{ text: '独资', value: '独资' },
					{ text: '合伙', value: '合伙' }
				],
				'有人给我投资': [
					{ text: '有餐饮加盟经验', value: '有餐饮加盟经验' },
					{ text: '无餐饮加盟经验', value: '无餐饮加盟经验' }
				],
				'又是投资者又是经营者': [
					{ text: '自己在店', value: '自己在店' },
					{ text: '亲属/朋友在店', value: '亲属/朋友在店' }
				]
			},
			// 三级选项（根据二级选项动态变化）
			thirdLevelOptionsMap: {
				'独资': [
					{ text: '聘请职业经理人', value: '聘请职业经理人' },
				],
				'合伙': [
					{ text: '合伙人有经验', value: '合伙人有经验' },
					{ text: '合伙人没有经验', value: '合伙人没有经验' }
				],

			},
			// 四级选项（根据三级选项动态变化）
			fourthLevelOptionsMap: {
				'合伙人没有经验': [
					{ text: '协助股东管理此项目或参与到具体岗位', value: '协助股东管理此项目或参与到具体岗位' },
				],
				'有加盟经验': [
					{ text: '具体加盟过哪些品牌（请写在备注，谢谢）', value: '具体加盟过哪些品牌（请写在备注，谢谢）' },
				],
			},
			currentSecondLevelOptions: [],
			currentThirdLevelOptions: [],
			currentFourthLevelOptions: [],
			industryIndex: null, // 行业选择索引
			industryRemark: '', // 行业备注
			industryOptions: [
				{ text: '餐饮行业', value: '餐饮行业' },
				{ text: '零售行业', value: '零售行业' },
				{ text: '备注', value: '备注' }
			],
			// 省市联动选择器相关数据
			provinceList: [],
			currentCityList: [],
			selectedProvinceIndex: null,
			selectedCityName: '', // 期望开店城市
			selectedCurrentCityName: '', // 当前所在城市
			showCityModal: false,
			cityPickerType: '', // 'current' 或 'expect'，用于区分是选择当前城市还是期望城市
			provinceData: provinceData
		}
	},
	computed: {
		shouldShowRemarks() {
			// 检查是否选择了"具体加盟过哪些品牌（请写在备注，谢谢）"
			return this.selectedOptions.fourth === '具体加盟过哪些品牌（请写在备注，谢谢）' || this.selectedOptions.second === '有餐饮加盟经验';
		}
	},
	watch: {
		// 监听备注变化
		industryRemark(newVal) {
			if (this.industryIndex === 2) {
				this.updateIndustryWithRemark();
			}
		}
	},
	onLoad() {
		// 初始化城市选择器
		const userInfo = getUserInfo()
		console.log("进入问卷",userInfo)
		if (!userInfo) {
			uni.navigateTo({
				url: '/pages/login/login',
			})
		}else{
			if (userInfo) {
				if (userInfo.customerType != 0) {
					uni.navigateTo({
						url: '/pages/welcome/customerIndex'
					})
				} 
			}
		}
		this.formData.phone = userInfo.phonenumber


		this.initProvinceList();
	},
	methods: {

		inputClick(e) {
			const o = e.detail.value
			const inputRule = /[^\d]/g
			this.$nextTick(() => {
				this.formData.phone = o.replace(inputRule, '');
				if (this.formData.phone == '') {
					return
				} else {
					//执行对应代码
				}
			})
		},
		// 省市区选择变化
		regionChange(e) {
			this.region = e.detail.value;
			this.regionValue = e.detail.value;
		},

		// 投资预算选择变化
		storyTypeChange(e) {
			this.storeTypeIndex = e.detail.value;
			this.formData.storeType = this.storeTypeOptions[e.detail.value].value;
		},

		joinTypeChange(e) {
			this.joinTypeIndex = e.detail.value;
			this.formData.joinType = this.joinTypeOptions[e.detail.value].value;
		},

		// 投资预算选择变化
		investmentBudgetChange(e) {
			this.investmentBudgetIndex = e.detail.value;
			this.formData.investmentBudget = this.investmentBudgetOptions[e.detail.value].value;
		},

		// 合作方式选择变化
		budgetChange(e) {
			this.budgetIndex = e.detail.value;
			this.formData.cooperationMode = this.budgetOptions[e.detail.value].value;
		},

		// 商圈选择变化
		businessChange(e) {
			this.businessIndex = e.detail.value;
			this.formData.businessDistrict = this.businessOptions[e.detail.value].value;
		},

		// 一级选项变化
		firstLevelChange(e) {
			this.firstLevelIndex = e.detail.value;
			const selectedValue = this.firstLevelOptions[this.firstLevelIndex].value;
			this.selectedOptions.first = selectedValue;

			// 更新二级选项
			this.currentSecondLevelOptions = this.secondLevelOptionsMap[selectedValue] || [];

			// 重置后续级别的选择
			this.secondLevelIndex = null;
			this.thirdLevelIndex = null;
			this.fourthLevelIndex = null;
			this.currentThirdLevelOptions = [];
			this.currentFourthLevelOptions = [];
			this.selectedOptions.second = '';
			this.selectedOptions.third = '';
			this.selectedOptions.fourth = '';

			// 如果没有下一级选项，直接更新表单数据
			this.updateStoreType();
		},

		// 二级选项变化
		secondLevelChange(e) {
			this.secondLevelIndex = e.detail.value;
			const selectedValue = this.currentSecondLevelOptions[this.secondLevelIndex].value;
			this.selectedOptions.second = selectedValue;

			// 更新三级选项
			this.currentThirdLevelOptions = this.thirdLevelOptionsMap[selectedValue] || [];

			// 重置后续级别的选择
			this.thirdLevelIndex = null;
			this.fourthLevelIndex = null;
			this.currentFourthLevelOptions = [];
			this.selectedOptions.third = '';
			this.selectedOptions.fourth = '';

			// 更新表单数据
			this.updateStoreType();
		},

		// 三级选项变化
		thirdLevelChange(e) {
			this.thirdLevelIndex = e.detail.value;
			const selectedValue = this.currentThirdLevelOptions[this.thirdLevelIndex].value;
			this.selectedOptions.third = selectedValue;

			// 更新四级选项
			this.currentFourthLevelOptions = this.fourthLevelOptionsMap[selectedValue] || [];

			// 重置后续级别的选择
			this.fourthLevelIndex = null;
			this.selectedOptions.fourth = '';

			// 更新表单数据
			this.updateStoreType();
		},

		// 四级选项变化
		fourthLevelChange(e) {
			this.fourthLevelIndex = e.detail.value;
			const selectedValue = this.currentFourthLevelOptions[this.fourthLevelIndex].value;
			this.selectedOptions.fourth = selectedValue;

			// 更新表单数据
			this.updateStoreType();
		},

		// 更新门店形式字段，将所有选择的选项用/拼接
		updateStoreType() {
			// 过滤掉空值，然后用/拼接
			const values = Object.values(this.selectedOptions).filter(val => val !== '');
			this.formData.storeType = values.join('/');
		},

		// selectIndustry(index) {
		// 	this.industryIndex = index;

		// 	// 更新表单数据
		// 	if (index === 0 || index === 1) {
		// 		this.formData.industry = this.industryOptions[index].value;
		// 		this.industryRemark = ''; // 清空备注
		// 	} else if (index === 2) {
		// 		// 备注选项，等待用户输入备注
		// 		this.updateIndustryWithRemark();
		// 	}
		// },

		// 商圈选择方法
		selectBusiness(index) {
			this.businessIndex = index;
			this.formData.businessDistrict = this.businessOptions[index].value;
		},

		// 更新带备注的行业
		updateIndustryWithRemark() {
			if (this.industryRemark) {
				this.formData.industry = `备注（${this.industryRemark}）`;
			} else {
				this.formData.industry = '备注';
			}
		},

		submitForm() {
			const userInfo = getUserInfo()
			if (!userInfo) {
				uni.showToast({
					icon: "none",
					title: "请先登录",
					duration: 2000,
					complete: () => {
						// 在提示完成后跳转
						uni.navigateTo({
							url: '/pages/login/login',
						})
					}
				})
				return;
			}
			if (!this.formData.name) {
				uni.showToast({
					title: '请输入姓名',
					icon: 'none'
				});
				return;
			}
			if (!this.formData.phone) {
				uni.showToast({
					title: '请输入手机号',
					icon: 'none'
				});
				return;
			}
			// 添加11位电话号码校验
			// if (!/^1[3-9]\d{9}$/.test(this.formData.phone)) {
			// 	uni.showToast({
			// 		title: '请输入正确的11位手机号',
			// 		icon: 'none'
			// 	});
			// 	return;
			// }
			if (!this.formData.wechat) {
				uni.showToast({
					title: '请输入微信号',
					icon: 'none'
				});
				return;
			}
			if (!this.selectedCurrentCityName) {
				uni.showToast({
					title: '请选择您所在城市',
					icon: 'none'
				});
				return;
			}
			if (!this.formData.frequentStore) {
				uni.showToast({
					title: '请输入经常关顾哪几家店',
					icon: 'none'
				});
				return;
			}


			if (!this.formData.storeType) {
				uni.showToast({
					title: '请选择门店投资方式',
					icon: 'none'
				});
				return;
			}
			if (!this.formData.cooperationMode) {
				uni.showToast({
					title: '请选择合作方式',
					icon: 'none'
				});
				return;
			}
			if (!this.formData.joinType) {
				uni.showToast({
					title: '请选择门店经营参与方式',
					icon: 'none'
				});
				return;
			}
			if (!this.formData.investmentBudget) {
				uni.showToast({
					title: '请选择投资预算',
					icon: 'none'
				});
				return;
			}

			if (!this.selectedCityName) {
				uni.showToast({
					title: '请选择期望开店城市',
					icon: 'none'
				});
				return;
			}
			// if (!this.selectedCityName) {
			// 	uni.showToast({
			// 		title: '请选择期望开店城市',
			// 		icon: 'none'
			// 	});
			// 	return;
			// }

			// if (this.industryIndex === null) {
			// 	uni.showToast({
			// 		title: '请选择您从事的行业',
			// 		icon: 'none'
			// 	});
			// 	return;
			// }

			// if (!this.formData.developmentOpinion) {
			// 	uni.showToast({
			// 		title: '请输入发展意见',
			// 		icon: 'none'
			// 	});
			// 	return;
			// }

			if (!this.formData.selectionReason) {
				uni.showToast({
					title: '您认为柒小螺的优势是什么',
					icon: 'none'
				});
				return;
			}
			if (!this.formData.industry) {
				uni.showToast({
					title: '请输入行业',
					icon: 'none'
				});
				return;
			}
			if (!this.formData.improvementSuggestion) {
				uni.showToast({
					title: '您觉得柒小螺哪些方面需要提升或优化',
					icon: 'none'
				});
				return;
			}
			// if (!this.formData.businessDistrict) {
			// 	uni.showToast({
			// 		title: '请输入商圈',
			// 		icon: 'none'
			// 	});
			// 	return;
			// }
			// if (!this.formData.improvementSuggestion) {
			// 	uni.showToast({
			// 		title: '请输入改进建议',
			// 		icon: 'none'
			// 	});
			// 	return;
			// }

			this.formData.residence = this.selectedCurrentCityName
			this.formData.expectedCity = this.selectedCityName
			this.formData.expectedStoreAddress = this.selectedCityName;
			this.formData.clientId = getUserInfo().clientId
			this.formData.userId = getUserInfo().userId
			console.log('表单数据：', this.formData);

			uni.showLoading({
				title: '提交中',
				mask: true
			})
			util.request("wx/api/saveQuestion", this.formData, "POST").then(function (res) {
				uni.hideLoading();
				if (res.code === 200) {
					util.request("wxGetInfo", "", "GET").then(function (wxGetInfoRes) {
						setUserInfo(wxGetInfoRes.user)
						uni.redirectTo({
							url: '/pages/welcome/customerIndex',
						})
					})

				} else {
					uni.showToast({
						icon: "none",
						title: res.msg,
						duration: 2000
					})
				}
			})
		},

		// 初始化省份列表
		initProvinceList() {
			this.provinceList = Object.keys(this.provinceData).map(province => ({
				name: province
			}));
		},

		// 选择省份
		selectProvince(index) {
			this.selectedProvinceIndex = index;
			const provinceName = this.provinceList[index].name;
			this.currentCityList = this.provinceData[provinceName] || [];
		},

		// 显示当前城市选择器
		showCurrentCityPicker() {
			this.cityPickerType = 'current';
			this.showCityModal = true;
			this.selectedProvinceIndex = null;
			this.currentCityList = [];
		},

		// 显示期望城市选择器
		showCityPicker() {
			this.cityPickerType = 'expect';
			this.showCityModal = true;
			this.selectedProvinceIndex = null;
			this.currentCityList = [];
		},

		hideCityPicker() {
			this.showCityModal = false;
			this.selectedProvinceIndex = null;
			this.currentCityList = [];
		},

		selectCity(cityName) {
			if (this.cityPickerType === 'current') {
				this.selectedCurrentCityName = cityName;
			} else {
				this.selectedCityName = cityName;
			}
			this.hideCityPicker();
		},
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
	/* 增加最小高度到160rpx */
	height: auto;
	/* 允许自动调整高度 */
	line-height: 1.6;
	/* 增加行高，改善文本可读性 */
	background: #f8f8f8;
	border-radius: 8rpx;
	padding: 24rpx 20rpx;
	/* 增加上下内边距 */
	font-size: 28rpx;
	color: #333;
	word-wrap: break-word;
	white-space: normal;
	display: flex;
	align-items: flex-start;
	/* 改为顶部对齐，避免长文本时的居中问题 */
	box-sizing: border-box;
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
}

.required {
	color: #ff0000;
}

.radio-group {
	padding: 20rpx;
}

.radio-item {
	display: flex;
	align-items: center;
	margin-bottom: 30rpx;
}

.radio-circle {
	width: 40rpx;
	height: 40rpx;
	border-radius: 50%;
	border: 2rpx solid #999;
	margin-right: 20rpx;
	position: relative;
}

.radio-circle.active {
	border-color: #2979FF;
}

.radio-circle.active:after {
	content: '';
	position: absolute;
	width: 24rpx;
	height: 24rpx;
	background-color: #2979FF;
	border-radius: 50%;
	top: 8rpx;
	left: 8rpx;
}

.remark-input {
	flex: 1;
	height: 60rpx;
	background: #f8f8f8;
	border-radius: 8rpx;
	padding: 0 20rpx;
	font-size: 28rpx;
	margin-left: 20rpx;
}

.notice-banner {
	background-color: #FFF3CD;
	border: 1px solid #FFEAA7;
	border-radius: 8rpx;
	padding: 24rpx 32rpx;
	margin-bottom: 40rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

.notice-text {
	color: #856404;
	font-size: 28rpx;
	font-weight: 500;
	text-align: center;
	line-height: 1.4;
}

/* 城市选择器样式 */
.city-selector {
	display: flex;
	align-items: center;
	justify-content: space-between;
	min-height: 120rpx;
	background: #f8f8f8;
	border-radius: 8rpx;
	padding: 20rpx;
	font-size: 28rpx;
	color: #333;
}

.picker-arrow {
	color: #999;
	font-size: 24rpx;
	transform: rotate(90deg);
}

/* 城市选择弹窗样式 */
.city-modal {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: rgba(0, 0, 0, 0.5);
	z-index: 9999;
	display: flex;
	align-items: center;
	justify-content: center;
}

.city-modal-content {
	width: 80%;
	max-height: 70%;
	background: #fff;
	border-radius: 16rpx;
	overflow: hidden;
}

.city-modal-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 30rpx;
	border-bottom: 1rpx solid #eee;
}

.city-modal-title {
	font-size: 32rpx;
	font-weight: bold;
	color: #333;
}

.city-modal-close {
	font-size: 40rpx;
	color: #999;
	width: 60rpx;
	height: 60rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

.city-search-wrap {
	padding: 20rpx 30rpx;
	border-bottom: 1rpx solid #eee;
}

.city-search-input {
	width: 100%;
	height: 80rpx;
	background: #f8f8f8;
	border-radius: 8rpx;
	padding: 0 20rpx;
	font-size: 28rpx;
	box-sizing: border-box;
}

.province-city-container {
	display: flex;
	height: 600rpx;
}

.province-list {
	width: 40%;
	height: 100%;
	border-right: 1rpx solid #eee;
}

.province-item {
	padding: 30rpx 20rpx;
	border-bottom: 1rpx solid #f5f5f5;
}

.province-item.active {
	background-color: #f0f0f0;
	color: #2979FF;
	font-weight: bold;
}

.city-list {
	width: 60%;
	height: 100%;
}

.city-item {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 30rpx;
	border-bottom: 1rpx solid #f5f5f5;
}

.city-item:last-child {
	border-bottom: none;
}

.city-name {
	font-size: 28rpx;
	color: #333;
}

.city-selected {
	color: #2979FF;
	font-size: 32rpx;
	font-weight: bold;
}

.no-data {
	padding: 60rpx 30rpx;
	text-align: center;
	color: #999;
	font-size: 28rpx;
}
</style>
