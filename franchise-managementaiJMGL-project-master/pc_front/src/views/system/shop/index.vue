<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="店铺名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入店铺名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="queryParams.address" placeholder="请输入地址" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
<!--      <el-form-item label="店铺状态" prop="shopStatus">-->
<!--        <el-select v-model="queryParams.shopStatus" placeholder="请选择店铺状态">-->
<!--          <el-option label="待审核" :value="0" />-->
<!--          <el-option label="审核通过" :value="1" />-->
<!--          <el-option label="审核不通过" :value="2" />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item label="运行状态" prop="runStatus">
        <el-select v-model="queryParams.runStatus" placeholder="请选择店铺运行状态">
          <el-option label="全部" :value="null"/>
          <el-option label="待分配设计" :value="1"/>
          <el-option label="现场测量" :value="2"/>
          <el-option label="平面图设计" :value="3"/>
          <el-option label="施工图设计" :value="4"/>
          <el-option label="效果图设计" :value="5"/>
          <el-option label="施工中" :value="6"/>
          <el-option label="门店验收" :value="7"/>
          <el-option label="已开店" :value="8"/>
        </el-select>
      </el-form-item>
      <el-form-item label="区域" prop="area">
        <el-input v-model="queryParams.area" placeholder="请输入区域" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="跟随人" prop="guideId">
        <el-select v-model="queryParams.follower" clearable placeholder="请选择跟进人员">
          <el-option v-for="item in followUpPerson" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="商圈" prop="districtId">
        <el-select v-model="queryParams.districtId" clearable placeholder="请选择商圈">
          <el-option v-for="item in businesSq" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="客户姓名" prop="guideId">
        <el-select v-model="queryParams.clientId" clearable placeholder="请客户">
          <el-option v-for="item in clientPerson" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:shop:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['system:shop:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:shop:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:shop:export']">导出</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button type="danger" plain icon="el-icon-s-operation" size="mini" :disabled="multiple"-->
<!--                   @click="handleSetting" v-hasPermi="['system:client:set']">批量设置跟进人员</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleupdatye"-->
<!--          v-hasPermi="['system:shop:remove']">释放</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="auditAcc"-->
<!--          v-hasPermi="['system:shop:remove']">审核通过</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="audit"-->
<!--          v-hasPermi="['system:shop:remove']">审核驳回</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-s-operation" size="mini" :disabled="multiple" @click="toDesgin"
                   v-hasPermi="['system:shop:remove']">转设计</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading"  ref="multipleTable" :data="shopList" @selection-change="handleSelectionChange" @sort-change="handleSortChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="商圈" align="center" prop="districtName" />
      <el-table-column label="店铺名称" align="center" prop="name" />
      <el-table-column label="地址" align="center" prop="address" />
      <el-table-column label="区域" align="center" prop="area" />
<!--      <el-table-column label="店铺状态" align="center" prop="shopStatus" :formatter="formatShopStatus" />-->
      <el-table-column label="跟进人员" align="center" prop="followerName" />
      <el-table-column label="客户" align="center" prop="clientName" />
      <el-table-column label="阶段停留" align="center" prop="warningDay" sortable="true" >
        <!-- 自定义列内容：通过scoped-slot获取当前行数据 -->
        <template slot-scope="scope">
          <!-- 处理停留时间：兼容null/undefined，默认显示“-” -->
          <span :class="getStayTimeClass(scope.row.warningDay)">
      {{ scope.row.warningDay ? `${scope.row.warningDay}天` : '-' }}
    </span>
        </template>
      </el-table-column>
      <el-table-column label="进度" align="center" prop="process"  sortable="true"/>
      <!--      <el-table-column label="地区ID" align="center" prop="regionId" />-->
      <el-table-column label="租金" align="center" prop="rent" />
      <el-table-column label="店铺描述" align="center" prop="shopDesc" />
      <el-table-column label="周边描述" align="center" prop="surroundDesc" />
      <el-table-column label="交通描述" align="center" prop="trafficDesc" />
      <el-table-column label="店铺照片" align="center" prop="imgUrl">
        <template slot-scope="scope">
          <a href="javascript:void(0);" @click.stop="handleLinkClick(scope.row.imgUrl)">点击查看店铺照片</a>
        </template>
      </el-table-column>
      <el-table-column label="视频地址" align="center" prop="videoUrl">
        <template slot-scope="scope">
          <a href="javascript:void(0);" @click.stop="handleVideoView(scope.row.videoUrl)">点击查看视频</a>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" align="center" prop="sTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.sTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="upTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.upTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <!--      <el-table-column label="创建人" align="center" prop="owner" />-->

      <el-table-column label="pdf文件地址" align="center" prop="pdfUrl">
        <template slot-scope="scope">
          <a href="javascript:void(0);" @click.stop="handleDownLoadPdf(scope.row.pdfUrl)">下载PDF文件</a>
        </template>
      </el-table-column>
      <el-table-column label="商铺面积" align="center" prop="shopSize" />
      <el-table-column label="动线" align="center" prop="dongxian" />
      <el-table-column label="户型" align="center" prop="huxing" />
      <el-table-column label="门宽" align="center" prop="menkuan" />
      <el-table-column label="外摆" align="center" prop="waibai" />
      <el-table-column label="年限" align="center" prop="nianxian" />
      <el-table-column label="递增" align="center" prop="dizneg" />
      <el-table-column label="装修免租期" align="center" prop="norent" />
      <el-table-column label="电费" align="center" prop="dian" />
      <el-table-column label="水费" align="center" prop="shui" />
      <el-table-column label="装修押金" align="center" prop="yajin" />
      <el-table-column label="装让费" align="center" prop="zhuanrangfei" />
      <el-table-column label="工程条件" align="center" prop="condition" />
      <el-table-column label="广告位" align="center" prop="guanggao" />
      <el-table-column label="总结" align="center" prop="allDesc" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="130">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:shop:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:shop:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-dialog :title="followTitle" :visible.sync="followFlag" width="500px" append-to-body>
      <el-form ref="followForm" :model="followForm" :rules="rules" label-width="80px">
        <el-form-item label="跟进人员" prop="followPeople">
          <el-select v-model="followPersonNameTwo" clearable placeholder="请选择">
            <el-option v-for="item in followUpPerson" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFollowForm">确 定</el-button>
        <el-button @click="followCancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改标准作业流程/店铺信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

        <el-form-item label="商圈" prop="districtId">
          <el-select v-model="form.districtId" clearable placeholder="请选择商圈">
            <el-option v-for="item in businesSq" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="店铺名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入店铺名称" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="区域" prop="area">
          <el-input v-model="form.area" placeholder="请输入区域" />
        </el-form-item>
        <el-form-item label="地区ID" prop="regionId">
          <el-input v-model="form.regionId" placeholder="请输入地区ID" />
        </el-form-item>
        <el-form-item label="租金" prop="rent">
          <el-input v-model="form.rent" placeholder="请输入租金" />
        </el-form-item>
        <el-form-item label="店铺描述" prop="shopDesc">
          <el-input v-model="form.shopDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="周边描述" prop="surroundDesc">
          <el-input v-model="form.surroundDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="交通描述" prop="trafficDesc">
          <el-input v-model="form.trafficDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <!-- 在交通描述表单项下方添加以下代码 -->
        <el-form-item label="照片地址" prop="imgUrl">
          <el-upload class="upload-demo" action="/manage/common/upload" :on-success="handleImgUploadSuccess"
            :show-file-list="false" list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>
          <div v-if="form.imgUrl && form.imgUrl.length" style="margin-top: 10px;">
            <div v-for="(url, index) in form.imgUrl" :key="index"
              style="display: inline-block; margin-right: 10px; position: relative;">
              <img :src="url" alt="图片预览" style="max-width: 200px; max-height: 200px; display: block;">
              <el-button size="mini" icon="el-icon-delete" circle style="position: absolute; top: -10px; right: -10px;"
                @click.stop="removeImage(index)"></el-button>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="视频地址" prop="videoUrl">
          <el-upload class="avatar-uploader" action="/manage/common/upload" v-bind:on-progress="uploadVideoProcess"
            v-bind:on-success="handleVideoSuccess" v-bind:before-upload="beforeUploadVideo"
            v-bind:show-file-list="false">
            <div>
              <el-button size="small" type="primary">点击上传</el-button>
              <el-button v-if="form.videoUrl!== ''" size="small" type="danger" @click.stop="deleteVideo">删除视频</el-button>
            </div>
            <video v-if="form.videoUrl != '' && !videoFlag" v-bind:src="form.videoUrl" class="avatar video-avatar"
              controls="controls">
              您的浏览器不支持视频播放
            </video>
            <i v-else-if="form.videoUrl == '' && !videoFlag" class="el-icon-plus avatar-uploader-icon"></i>
            <el-progress v-if="videoFlag == true" type="circle" v-bind:percentage="videoUploadPercent"
              style="margin-top: 7px"></el-progress>
          </el-upload>
        </el-form-item>
<!--        <el-form-item label="店铺状态" prop="shopStatus">-->
<!--          <el-select v-model="form.shopStatus" clearable placeholder="请选择">-->
<!--            <el-option-->
<!--              v-for="item in shopStatusArr"-->
<!--              :key="item.value"-->
<!--              :label="item.label"-->
<!--              :value="item.value"-->
<!--            />-->
<!--          </el-select>-->
<!--        </el-form-item>-->
        <el-form-item label="创建时间" prop="sTime">
          <el-date-picker clearable v-model="form.sTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="更新时间" prop="upTime">
          <el-date-picker clearable v-model="form.upTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择更新时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="创建人" prop="owner">
          <el-input v-model="form.owner" placeholder="请输入创建人" />
        </el-form-item>
        <el-form-item label="跟随人" prop="follower">
          <el-input v-model="form.follower" placeholder="请输入跟随人" />
        </el-form-item>
<!--        <el-form-item label="客户id" prop="clientId">-->
<!--          <el-input v-model="form.clientId" placeholder="请输入客户id" />-->
<!--        </el-form-item>-->
        <el-form-item label="pdf文件地址" prop="pdfUrl">
          <el-upload accept=".pdf" action="/manage/common/upload" :file-list="pdfFileList"
            :before-upload="beforePdfUpload" :on-change="handlePdfChange" :on-success="handlePdfSuccess"
            :on-remove="handlePdfRemove" :multiple="false" :show-file-list="true">
            <div class="results_pdfFile">
              <el-button size="small" type="primary">上传附件</el-button>
            </div>
            <div slot="tip" class="el-upload__tip">只能上传一个PDF文件</div>
          </el-upload>
          <!-- 显示已上传的PDF文件 -->
          <!-- <div v-if="form.pdfUrl" class="uploaded-pdf" style="margin-top: 10px;">
            <el-link type="primary" :href="form.pdfUrl" target="_blank">
              <i class="el-icon-document"></i> 查看PDF文件
            </el-link>
            <el-button size="mini" type="danger" icon="el-icon-delete" circle style="margin-left: 10px;"
              @click="deletePdf">
            </el-button>
          </div> -->
        </el-form-item>
        <el-form-item label="商铺面积" prop="shopSize">
          <el-input v-model="form.shopSize" placeholder="请输入商铺面积" />
        </el-form-item>
        <el-form-item label="动线" prop="dongxian">
          <el-input v-model="form.dongxian" placeholder="请输入动线" />
        </el-form-item>
        <el-form-item label="户型" prop="huxing">
          <el-input v-model="form.huxing" placeholder="请输入户型" />
        </el-form-item>
        <el-form-item label="门宽" prop="menkuan">
          <el-input v-model="form.menkuan" placeholder="请输入门宽" />
        </el-form-item>
        <el-form-item label="外摆" prop="waibai">
          <el-input v-model="form.waibai" placeholder="请输入外摆" />
        </el-form-item>
        <el-form-item label="年限" prop="nianxian">
          <el-input v-model="form.nianxian" placeholder="请输入年限" />
        </el-form-item>
        <el-form-item label="递增" prop="dizneg">
          <el-input v-model="form.dizneg" placeholder="请输入递增" />
        </el-form-item>
        <el-form-item label="装修免租期" prop="norent">
          <el-input v-model="form.norent" placeholder="请输入装修免租期" />
        </el-form-item>
        <el-form-item label="电费" prop="dian">
          <el-input v-model="form.dian" placeholder="请输入电费" />
        </el-form-item>
        <el-form-item label="水费" prop="shui">
          <el-input v-model="form.shui" placeholder="请输入水费" />
        </el-form-item>
        <el-form-item label="装修押金" prop="yajin">
          <el-input v-model="form.yajin" placeholder="请输入装修押金" />
        </el-form-item>
        <el-form-item label="装让费" prop="zhuanrangfei">
          <el-input v-model="form.zhuanrangfei" placeholder="请输入装让费" />
        </el-form-item>
        <el-form-item label="工程条件" prop="condition">
          <el-input v-model="form.condition" placeholder="请输入工程条件" />
        </el-form-item>
        <el-form-item label="广告位" prop="guanggao">
          <el-input v-model="form.guanggao" placeholder="请输入广告位" />
        </el-form-item>
        <el-form-item label="总结" prop="allDesc">
          <el-input v-model="form.allDesc" placeholder="请输入总结" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 图片以及视频查看对话框 -->
    <el-dialog :visible.sync="dialogVisible" :title="imgAndVideoTitle" :close-on-click-modal="false">
      <div class="demo-image__lazy" v-if="imgFlag">
        <!-- <el-image v-for="url in currentImageUrl" :key="url" :src="url" lazy></el-image> -->
        <!-- 轮播图预览区域 -->
        <div v-if="currentImageUrl && currentImageUrl.length" style="margin-top: 20px;">
          <el-carousel :interval="3000" height="300px" indicator-position="outside" arrow="always">
            <el-carousel-item v-for="(url, index) in currentImageUrl" :key="index">
              <div style="position: relative; height: 100%;">
                <img :src="url" alt="图片预览" style="max-height: 100%; max-width: 100%; display: block; margin: 0 auto;">
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
      <div class="video-container" style="height: 500px;" v-else>
        <video ref="videoPlayer" controls class="w-full h-full object-cover">
          <source :src="currentVideoUrl" type="video/mp4">
          您的浏览器不支持HTML5视频播放。
        </video>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listShop, getShop, delShop, addShop, updateShop, updatenullShop,auditAcc,audit ,toDesgin,BatchSetProple} from "@/api/system/shop"
import { listUser, listClient } from '@/api/system/client'
import { listDistrictout } from '@/api/system/district'

export default {
  name: "Shop",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      followPersonNameTwo: "",//批量设置里面的跟进人员
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 标准作业流程/店铺信息表格数据
      shopList: [],
      // 跟进人员表单
      followForm: {},
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      businesSq:[],
      // 查询参数
      queryParams: {
        districtId :null,
        shopStatus:null,
        runStatus:null,
        pageNum: 1,
        pageSize: 10,
        name: null,
        address: null,
        area: null,
        regionId: null,
        rent: null,
        shopDesc: null,
        surroundDesc: null,
        trafficDesc: null,
        sTime: null,
        upTime: null,
        owner: null,
        follower: null,
        clientId: null,
        pdfUrl: null,
        shopSize: null,
        dongxian: null,
        huxing: null,
        menkuan: null,
        waibai: null,
        nianxian: null,
        dizneg: null,
        norent: null,
        dian: null,
        shui: null,
        yajin: null,
        zhuanrangfei: null,
        condition: null,
        guanggao: null,
        allDesc: null,
        sortProp: "",  // 新增：排序字段（如warningDay、process）
        sortOrder: ""
      },

      commonqueryParams: {
        pageNum: 1,
        pageSize: 1000
      },
      followUpPerson: [],
      clientPerson: [],
      dialogVisible: false, // 图片查看对话框
      currentImageUrl: '', // 当前查看的图片地址
      currentVideoUrl: "", //当前视频地址
      imgFlag: false, //展示视频区域
      imgAndVideoTitle: "",
      detailForm: {},//列表详情数据
      pdfFileList: [], // PDF文件列表
      readListDetailTitle: "查看店铺列表详情",
      // readListDetailFlag: false, //查看列表详情弹窗标识
      // 视频上传相关
      videoFlag: false,
      //是否显示进度条
      videoUploadPercent: "",
      //进度条的进度，
      isShowUploadVideo: false,
      // 表单参数
      form: {},
      shopStatusArr: [
        { label: "未签约", value: "0" },
        { label: "审核通过", value: "1" },
        { label: "审核驳回", value: "2" }
      ],
      // 表单校验
      rules: {
        name: [
          { required: true, message: "店铺名称不能为空", trigger: "blur" }
        ],
        owner: [
          { required: true, message: "创建人不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
    this.getFollwPerson();
    this.getClientPerson();
    this.getBusinessSq();
  },
  methods: {
    handleSortChange({ prop, order }) {
      // 1. 转换排序方向（Element UI的order值转为后端需要的asc/desc）
      const sortOrder = order === "ascending" ? "asc" : order === "descending" ? "desc" : "";

      // 2. 更新查询参数中的排序条件
      this.queryParams.sortProp = prop;    // 排序字段（warningDay/process）
      this.queryParams.sortOrder = sortOrder;  // 排序方向

      // 3. 重新拉取数据（带排序条件）
      this.getList();
    },

    getStayTimeClass(stayDay) {
      // 处理异常值：无停留时间时返回空类（显示默认文本样式）
      if (!stayDay || isNaN(Number(stayDay))) {
        return '';
      }
      // 转换为数字（避免字符串类型的数值判断错误）
      const day = Number(stayDay);
      // 按规则返回样式类
      if (day >= 1 && day < 3) {
        return 'stay-time-green'; // 1-3天（不含3天）绿色
      } else if (day >= 3 && day < 6) {
        return 'stay-time-orange'; // 3-6天（不含6天）橙色
      } else if (day >= 6) {
        return 'stay-time-red'; // ≥6天红色加粗
      } else {
        return ''; // 0天或负数，显示默认样式
      }
    },

    getBusinessSq() {
      listDistrictout(this.commonqueryParams).then(response => {
        this.businesSq = []; // 清空数组
        if (response && response.rows && response.rows.length > 0) {
          response.rows.forEach((item, index) => {
            // 每次循环创建新对象！
            const newOption = {
              value: item.id, // 使用 item 的属性赋值
              label: item.name
            };
            console.log(item.userId, "item.userId");
            this.businesSq.push(newOption); // 推送新对象
          });
        }
      });
    },


    submitFollowForm() {
      this.followFlag = false;
      // 直接从表格实例获取选中数据
      const selectedRows = this.$refs.multipleTable.selection;
      console.log('选中的数据：', selectedRows);
      var followArrForSet = {
        "follower": this.followPersonNameTwo,
        "shopIds": []
      }
      selectedRows.forEach((item, index) => {
        followArrForSet.shopIds.push(item.id)
      })
      BatchSetProple(followArrForSet).then((response) => {
        this.$modal.msgSuccess("新增成功")
        this.getList();
      })
    },
    //批量设置跟进人员----取消操作
    followCancel() {
      this.followFlag = false;
    },




    getFollwPerson() {

      listUser(this.commonqueryParams).then(response => {
        this.followUpPerson = []; // 清空数组
        if (response && response.rows && response.rows.length > 0) {
          response.rows.forEach((item, index) => {
            // 每次循环创建新对象！
            const newOption = {
              value: item.userId, // 使用 item 的属性赋值
              label: item.nickName
            };
            console.log(item.userId, "item.userId");
            this.followUpPerson.push(newOption); // 推送新对象
          });
        }
      });
    },

    getClientPerson() {
      listClient(this.commonqueryParams).then(response => {
        this.clientPerson = []; // 清空数组
        if (response && response.rows && response.rows.length > 0) {
          response.rows.forEach((item, index) => {
            // 每次循环创建新对象！
            const newOption = {
              value: item.id, // 使用 item 的属性赋值
              label: item.name
            };
            console.log(item.userId, "item.userId");
            this.clientPerson.push(newOption); // 推送新对象
          });
        }
      });
    },

    formatShopStatus(row, column) {
      const statusMap = {
        "0": "待审核",
        '1': '审核通过',
        '2': '审核不通过'
      };
      return statusMap[row.shopStatus] || row.shopStatus;
    },

    /** 查询标准作业流程/店铺信息列表 */
    getList() {
      this.loading = true
      listShop(this.queryParams).then(response => {
        this.shopList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        address: null,
        area: null,
        regionId: null,
        rent: null,
        shopDesc: null,
        surroundDesc: null,
        trafficDesc: null,
        shopStatus: null,
        sTime: null,
        upTime: null,
        owner: null,
        follower: null,
        clientId: null,
        pdfUrl: null,
        shopSize: null,
        dongxian: null,
        huxing: null,
        menkuan: null,
        waibai: null,
        nianxian: null,
        dizneg: null,
        norent: null,
        dian: null,
        shui: null,
        yajin: null,
        zhuanrangfei: null,
        condition: null,
        guanggao: null,
        allDesc: null,
        imgUrl: [],
        videoUrl: '',
      }
      this.resetForm("form")
    },
    // 查看店铺照片点击事件
    handleLinkClick(url) {
      console.log(url, "链接地址")
      this.dialogVisible = true;
      this.imgFlag = true;
      this.imgAndVideoTitle = "查看图片"
      this.currentImageUrl = url.split(','); // 设置当前图片地址
    },
    //查看店铺视频的事件
    handleVideoView(videoUrl) {
      console.log(videoUrl, "视频地址")
      this.imgFlag = false;
      this.imgAndVideoTitle = "查看视频"
      this.dialogVisible = true;
      this.currentVideoUrl = videoUrl; // 设置当前视频地址
    },
    // 上传图片部分-start
    // 图片上传成功回调
    handleImgUploadSuccess(response) {
      // 假设返回的数据结构为 { url: '上传后的图片地址' }
      if (response.code !== 200) {
        this.$message.error('照片上传失败');
        return;
      }
      this.form.imgUrl.push(response.url); // 将上传的图片地址添加到 form.imgUrl 数组中
      this.$message.success('照片上传成功');
    },
    // 删除指定索引的图片
    removeImage(index) {
      this.form.imgUrl.splice(index, 1);
    },
    // 上传视频部分-start
    //上传前回调
    beforeUploadVideo(file) {
      var fileSize = file.size / 1024 / 1024 < 50;   //控制大小  修改50的值即可
      if (
        [
          "video/mp4",
          "video/ogg",
          "video/flv",
          "video/avi",
          "video/wmv",
          "video/rmvb",
          "video/mov",
        ].indexOf(file.type) == -1     //控制格式
      ) {
        layer.msg("请上传正确的视频格式");
        return false;
      }
      if (!fileSize) {
        layer.msg("视频大小不能超过50MB");
        return false;
      }
      this.isShowUploadVideo = false;
    },
    //进度条
    uploadVideoProcess(event, file) {    //注意在data中添加对应的变量名
      this.videoFlag = true;
      this.videoUploadPercent = file.percentage.toFixed(0) * 1;
    },
    deleteVideo(){
      this.form.videoUrl = '';
      // 可以根据需要进行其他重置操作，如设置videoFlag为false等
      this.videoFlag = false;
    },
    //上传成功回调
    handleVideoSuccess(res, file) {
      this.isShowUploadVideo = true;
      this.videoFlag = false;
      this.videoUploadPercent = 0;

      console.log(res);
      //后台上传数据
      if (res.code == 200) {
        this.$message.success("上传成功！");
        this.form.videoUrl = res.url;    //上传成功后端返回视频地址 回显
      } else {
        this.$message.error("上传失败！");
      }
    },
    // 上传视频部分-end

    // 上传pdf部分-start
    // PDF上传前的校验
    beforePdfUpload(file) {
      const isPDF = file.type === 'application/pdf';
      if (!isPDF) {
        this.$message.error('只能上传PDF格式的文件');
      }
      return isPDF;
    },

    // PDF上传成功处理
    handlePdfSuccess(response, file, fileList) {
      if (response.code === 200) {
        this.form.pdfUrl = response.url;
        this.$message.success('PDF上传成功');
      } else {
        this.$message.error('PDF上传失败');
      }
    },

    // 删除PDF文件
    deletePdf() {
      this.$confirm('确定要删除这个PDF文件吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.form.pdfUrl = '';
        this.pdfFileList = [];
        this.$message.success('PDF文件已删除');
      }).catch(() => {
        // 取消操作
      });
    },

    // PDF文件变化处理
    handlePdfChange(file, fileList) {
      this.pdfFileList = fileList.slice(-1); // 只保留最新上传的文件
    },

    // 处理PDF文件移除
    handlePdfRemove(file, fileList) {
      this.pdfFileList = fileList;
      if (fileList.length === 0) {
        this.form.pdfUrl = '';
      }
    },
    // 下载PDF文件
    handleDownLoadPdf(pdfUrl) {
      if (!pdfUrl) {
        this.$message.error('没有可下载的PDF文件');
        return;
      }
      // 使用浏览器下载功能
      const link = document.createElement('a');
      link.href = pdfUrl;
      link.download = 'shop_info.pdf'; // 设置下载文件名
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },
    // 上传pdf部分-end
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
      this.queryParams.sortProp = "";
      this.queryParams.sortOrder = "";
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加店铺信息"
    },
    // 查看详情按钮操作
    // handleReadDetail(row) {
    //   this.readListDetailFlag = true;
    //   this.detailForm = row;
    // },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getShop(id).then(response => {
        this.form = response.data
        this.form.imgUrl = this.form.imgUrl ? this.form.imgUrl.split(',') : [] // 将逗号分隔的字符串转换为数组
        this.open = true
        this.title = "修改店铺信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.imgUrl = this.form.imgUrl.join(',') // 将图片数组转换为逗号分隔的字符串
          if (this.form.id != null) {
            updateShop(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addShop(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除标准作业流程/店铺信息编号为"' + ids + '"的数据项？').then(function () {
        return delShop(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => { })
    },

    handleupdatye(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认释放标准作业流程/店铺信息编号为"' + ids + '"的数据项？').then(function () {
        return updatenullShop(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("释放成功")
      }).catch(() => { })
    },

    audit(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认驳回店铺信息编号为"' + ids + '"的数据项？').then(function () {
        return audit(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("驳回成功")
      }).catch(() => { })
    },

    toDesgin(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认流转店铺信息编号为"' + ids + '"的数据项到设计？').then(function () {
        return toDesgin(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("流转成功")
      }).catch(() => {})
    },


    /** 导出按钮操作 */
    handleExport() {
      this.download('system/shop/export', {
        ...this.queryParams
      }, `shop_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>


<style scoped>
.el-table--enable-row-transition .el-table__body td.el-table__cell a {
  color: rgb(70, 70, 243);
}

.avatar-uploader video {
  width: 100%;
}

.el-dialog__body .video-container video {
  width: 100%;
}

.video-container video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  /* 保持视频比例填充容器 */
}

.stay-time-green {
  color: #67c23a; /* Element UI默认成功色（绿色） */
}
/* 3-6天橙色 */
.stay-time-orange {
  color: #e6a23c; /* Element UI默认警告色（橙色） */
}
/* >6天红色+加粗 */
.stay-time-red {
  color: #f56c6c; /* Element UI默认危险色（红色） */
  font-weight: bold; /* 加粗 */
}


</style>
