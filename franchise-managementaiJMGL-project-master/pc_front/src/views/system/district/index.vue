<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商圈名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入商圈名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商圈地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入商圈地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属区域" prop="region">
        <el-input
          v-model="queryParams.region"
          placeholder="请输入所属区域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商圈状态" prop="statues">
        <el-select v-model="queryParams.statues" placeholder="请选择商圈状态">
          <el-option label="全部" :value="null" />
          <!-- 按需求保留的状态：注意value需与后端statues字段值匹配 -->
          <el-option label="审核中" :value="2" />    <!-- 假设后端“审核中”值为2（与原有逻辑一致） -->
          <el-option label="审核通过" :value="3" />  <!-- 假设后端“审核通过”值为3（与原有逻辑一致） -->
          <el-option label="审核不通过" :value="1" /><!-- 关键：“审核不通过”对应后端值（原“跟进中”1，需确认后端实际值，此处按逻辑映射） -->
          <el-option label="已签约" :value="4" />
        </el-select>
      </el-form-item>
      <el-form-item label="跟进人员" prop="followerId">
        <el-select v-model="queryParams.followerId" clearable placeholder="请选择跟进人员">
          <el-option v-for="item in followUpPerson" :key="item.value" :label="item.label" :value="item.value">
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
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:district:add']"
        >新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-s-operation" size="mini" :disabled="!multipleFollow "
                   @click="handleSetting" v-hasPermi="['system:client:set']">批量设置跟进人员</el-button>
      </el-col>
      <!-- 新增：批量设置客户按钮 -->
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-user-solid" size="mini" :disabled="!multipleSign"
                   @click="handleClientSetting" v-hasPermi="['system:district:batchSetClient']">签约&创建店铺</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:district:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-s-operation" size="mini" :disabled="!multipleacc" @click="commit"
                   v-hasPermi="['system:shop:remove']">提交审核</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-s-operation" size="mini" :disabled="!multipleback" @click="audit"
                   v-hasPermi="['system:shop:remove']">审核驳回</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-s-operation" size="mini" :disabled="!multiplepass" @click="auditcc"
                   v-hasPermi="['system:shop:remove']">审核通过</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-s-operation" size="mini" :disabled="!multiplefree" @click="free"
                   v-hasPermi="['system:shop:remove']">释放</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:district:remove']"
        >删除</el-button>
      </el-col>


      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="districtList" @selection-change="handleSelectionChange"  ref="multipleTable" >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="商圈ID" align="center" prop="id" />
      <el-table-column label="商圈名称" align="center" prop="name" />
      <el-table-column label="商圈地址" align="center" prop="address" />
      <el-table-column label="所属区域" align="center" prop="region" />
      <el-table-column label="跟进人员信息" align="center" prop="followerName" />
      <el-table-column label="保护期间" align="center" prop="remainingDays">
        <template slot-scope="scope">
          <span
      :class="{
        'orange-text': scope.row.remainingDays <= 30 && scope.row.remainingDays > 7,
        'red-text': scope.row.remainingDays <= 7,
        'default-text': scope.row.remainingDays > 30
      }"
    >
      <!-- 显示剩余天数，若为负数可补充说明（如“已超期”） -->
      {{ scope.row.remainingDays > 0 ? `${scope.row.remainingDays} 天` : '已超保护期' }}
    </span>
        </template>
      </el-table-column>
      <el-table-column label="客户" align="center" prop="customerName" />
      <el-table-column label="商圈状态" align="center" prop="statues" :formatter="formatstatues" />
      <el-table-column label="跟进状态" align="center" prop="followerStatus" :formatter="followerformatstatues" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updatedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createrName" />
      <el-table-column label="查看笔记" align="center" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-notebook-2"
          @click="viewNotes(scope.row)"
          v-hasPermi="['system:district:edit']"
          >
          查看笔记
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:district:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:district:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />


    <!-- 1. 商圈笔记详情弹窗 -->
    <!-- 1. 商圈笔记详情弹窗 -->
    <el-dialog
      :title="noteTitle"
      :visible.sync="noteFlag"
      width="600px"
      append-to-body
      :close-on-click-modal="false"
    >
      <div class="note-detail-container">
        <!-- 加载状态 -->
        <div v-if="noteLoading" class="loading-container">
          <el-loading-spinner class="el-icon-loading"></el-loading-spinner>
          <p class="loading-text">正在加载笔记数据...</p>
        </div>

        <!-- 无数据提示（判断数组长度） -->
        <div v-else-if="noteData.length === 0" class="empty-note">
          <el-icon class="empty-icon"><warning-outline /></el-icon>
          <p class="empty-text">暂无该商圈的笔记数据</p>
        </div>

        <!-- 有数据时，循环每条笔记 -->
        <div v-else class="note-content">
          <div
            v-for="(item, index) in noteData"
            :key="item.id"
            class="note-item"
          >
            <!-- 每条笔记的基础信息（示例：跟进人员、时间） -->
            <div class="note-base-info">
              <div class="info-item">
                <span class="info-label">跟进人员：</span>
                <span class="info-value">{{ item.followerName || '未知' }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">更新时间：</span>
                <span class="info-value">{{ parseTime(item.updatedAt, '{y}-{m}-{d} {h}:{i}') || '未知' }}</span>
              </div>
            </div>

            <!-- 笔记描述 -->
            <div class="note-description">
              <p class="desc-text">{{ item.description || '无描述信息' }}</p>
            </div>

            <!-- 媒体区域（图片+视频，此时item是“当前笔记”） -->
            <div class="note-media">
              <h4 class="section-title">相关媒体（图片/视频）</h4>
              <div
                v-if="item.imageUrl || item.videoUrl"
                class="media-container flex flex-wrap gap-4"
              >
                <!-- 图片 -->
                <div v-if="item.imageUrl" class="media-item w-1/2 md:w-1/3">
                  <el-image
                    :src="item.imageUrl"
                    alt="商圈笔记图片"
                    class="preview-media"
                    :preview-src-list="[item.imageUrl]"
                    fit="cover"
                  >
                    <div slot="error" class="media-error">
                      <el-icon><picture-outline /></el-icon>
                    </div>
                  </el-image>
                  <p class="media-tip text-center mt-2">点击预览图片</p>
                </div>

                <!-- 视频 -->
                <div v-if="item.videoUrl" class="media-item w-1/2 md:w-1/3">
                  <video
                    controls
                    :src="item.videoUrl"
                    class="preview-media"
                    poster="https://via.placeholder.com/300x200?text=视频封面"
                    preload="metadata"
                  ></video>
                  <p class="media-tip text-center mt-2">点击播放视频</p>
                </div>
              </div>
              <div v-else class="no-media text-center py-4 bg-gray-50 rounded">无媒体内容</div>
            </div>

            <!-- 笔记分隔线（最后一条不显示） -->
            <div v-if="index < noteData.length - 1" class="note-divider"></div>
          </div>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="noteCancel">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 2. 笔记图片预览弹窗（复用图片预览逻辑） -->
    <el-dialog
      :visible.sync="noteImageFlag"
      title="图片预览"
      width="800px"
      append-to-body
      :close-on-click-modal="false"
    >
      <div class="image-preview-container">
        <el-image
          :src="currentNoteImage"
          alt="大图预览"
          fit="contain"
          class="full-image"
        >
          <div slot="error" class="preview-error">
            <el-icon><picture-outline /></el-icon>
            <p>图片加载失败</p>
          </div>
        </el-image>
      </div>
    </el-dialog>

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

    <!-- 新增：批量设置客户弹窗 -->
    <el-dialog :title="clientTitle" :visible.sync="clientFlag" width="500px" append-to-body>
      <div class="sign-tip mb16">
        您正在为商圈
        <span class="highlight-text">{{ selectedDistrictNames.join('、') }}</span>
        创建店铺项目，请关联签约的加盟客户。
      </div>
      <el-form ref="clientForm" :model="clientForm" :rules="clientRules" label-width="80px">
        <el-form-item label="关联客户" prop="clientId">
          <el-select v-model="clientForm.customerId" clearable placeholder="请选择关联客户" @change="validateClient">
            <el-option
              v-for="item in clientPerson"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <div class="empty-tip" v-if="clientPerson.length === 0">
            <el-icon><warning-outline /></el-icon> 暂无可用客户数据
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitClientForm">确 定</el-button>
        <el-button @click="clientCancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改商圈信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="商圈名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商圈名称" />
        </el-form-item>
        <el-form-item label="商圈地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入商圈地址" />
        </el-form-item>
        <el-form-item label="所属区域" prop="region">
          <el-input v-model="form.region" placeholder="请输入所属区域" />
        </el-form-item>

<!--        <el-form-item label="关联客户" prop="customerId">-->
<!--          <el-select v-model="form.customerId" clearable placeholder="请选择关联客户">-->
<!--            <el-option-->
<!--              v-for="item in clientPerson"-->
<!--              :key="item.value"-->
<!--              :label="item.label"-->
<!--              :value="item.value"-->
<!--            ></el-option>-->
<!--          </el-select>-->
<!--          <div class="empty-tip" v-if="clientPerson.length === 0">-->
<!--            <el-icon><warning-outline /></el-icon> 暂无可用客户数据-->
<!--          </div>-->
<!--        </el-form-item>-->
        <!-- 跟进人员多选 -->
        <el-form-item label="跟进人员" prop="followerIdList">
          <el-checkbox-group v-model="form.followerIdList">
            <el-checkbox v-for="user in followUpPerson" :label="user.value" :key="user.value">{{user.label}}</el-checkbox>
          </el-checkbox-group>
          <div class="empty-tip" v-if="followUpPerson.length === 0">
            <el-icon><warning-outline /></el-icon> 暂无可用跟进人员
          </div>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDistrict, getDistrict, delDistrict, addDistrict, updateDistrict ,batchSetDistrictClient} from "@/api/system/district"

import {listNote} from "@/api/system/note"
import { listClient, listUser } from '@/api/system/client'
import {audit, auditAcc, BatchSetProple} from "@/api/system/shop";

export default {
  name: "District",
  data() {
    return {

      noteFlag: false, // 笔记弹窗显隐
      noteTitle: "商圈笔记详情", // 笔记弹窗标题
      noteLoading: false, // 笔记数据加载状态
      noteData: [], // 存储单个商圈的笔记数据（匹配你提供的数据集结构）
      // 笔记图片预览相关
      noteImageFlag: false, // 图片预览弹窗显隐
      currentNoteImage: "", // 当前预览的图片地址


      selectedDistrictNames: [],

      followTitle: "",//批量设置人员弹窗标题
      followFlag: false,//批量设置人员弹窗关闭标识
      clientTitle: "批量设置关联客户",
      clientFlag: false,
      // 关键修复：使用clientForm统一管理表单数据
      clientForm: {
        customerId: null // 存储选中的客户ID，与后端字段名一致
      },
      // 关键修复：校验规则的prop与v-model绑定的字段一致
      clientRules: {
        customerId: [ // 规则名与clientForm中的字段一致
          { required: true, message: "请选择关联客户", trigger: "change" }
        ]
      },
      // 跟进人员表单
      followForm: {},
      followPersonNameTwo: "",//批量设置里面的跟进人员
      showDataEdit:false,
      //商圈跟进人
      followUpPerson: [],
      //客户列表
      clientPerson:[],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: false,
      multipleFollow:false,
      multipleSign:false,

      multipleacc: false,

      multipleback: false,

      multiplepass: false,

      multiplefree: false,

      notequeryParams: {
        pageNum: 1,
        pageSize: 1000,
        businessDistrictId: null,
        followerId: null,
        description: null,
        imageUrls: null,
        videoUrl: null,
        checkinLocation: null,
        updatedAt: null
      },


      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 商圈信息表格数据
      districtList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        address: null,
        region: null,
        customerId: null,
        createdAt: null,
        updatedAt: null,
        creatorId: null,
        statues:null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "商圈名称不能为空", trigger: "blur" }
        ],
        address: [
          { required: true, message: "商圈地址不能为空", trigger: "blur" }
        ],
        region: [
          { required: true, message: "所属区域不能为空", trigger: "blur" }
        ],
        customerId: [
          { required: true, message: "关联客户ID不能为空", trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updatedAt: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
        creatorId: [
          { required: true, message: "创建人ID不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getClientPerson();
    this.getFollwPerson();
  },
  methods: {


    viewNotes(row) {
      // 1. 显示加载状态，打开弹窗
      this.noteLoading = true;
      this.noteFlag = true;
      this.noteTitle = `商圈笔记 - ${row.name || '未知商圈'}`;

      this.notequeryParams.businessDistrictId = row.id
      // 2. 调用后台接口获取笔记数据（需替换为你的实际接口）
      // 假设接口：getDistrictNotes，参数为商圈ID（row.id）
      listNote(this.notequeryParams)
        .then(response => {
          this.noteLoading = false;
          // 3.1 提取rows数组，按更新时间倒序排序（最新笔记在前）
          this.noteData = (response.rows || []).sort((a, b) => {
            return new Date(b.updatedAt) - new Date(a.updatedAt);
          });
        })
        .catch(error => {
          this.noteLoading = false;
          this.noteData = []; // 错误时清空数组
          this.$modal.msgError(`加载笔记失败：${error.message || '未知错误'}`);
        });
    },

    /**
     * 关闭商圈笔记弹窗
     */
    noteCancel() {
      this.noteFlag = false;
      this.noteData = {}; // 清空数据，避免下次打开残留
    },

    /**
     * 笔记图片预览：打开大图弹窗
     * @param {String} imageUrl - 图片地址
     */
    handleNoteImagePreview(imageUrl) {
      this.currentNoteImage = imageUrl;
      this.noteImageFlag = true;
    },



    submitClientForm() {
      // 表单验证
      this.$refs.clientForm.validate((valid) => {
        if (!valid) {
          return false;
        }

        // 获取选中的商圈数据
        const selectedRows = this.$refs.multipleTable.selection;
        if (!selectedRows || selectedRows.length === 0) {
          this.$modal.msgWarning("请先选择需要设置的商圈");
          return false;
        }

        // 构造请求参数
        const params = {
          customerId: this.clientForm.customerId,
          districtIds: selectedRows.map(row => row.id)
        };
        console.log(params)

        // 调用批量设置客户接口
        batchSetDistrictClient(params).then(response => {
          this.$modal.msgSuccess("操作成功！店铺项目已创建。");
          this.clientFlag = false;
          this.getList(); // 刷新列表
          this.clientForm.customerId = ""; // 清空选择
          this.selectedDistrictNames =[]
        }).catch(error => {
          this.$modal.msgError("批量设置客户失败：" + (error.message || "未知错误"));
        });
      });
    },


    handleClientSetting() {
      const selectedRows = this.$refs.multipleTable.selection;
      if (!selectedRows || selectedRows.length === 0) {
        this.$modal.msgWarning("请先选择需要创建店铺的商圈");
        return;
      }
      this.clientTitle = "批量设置关联客户";
      this.selectedClientId = ""; // 重置选中的客户
      this.clientFlag = true; // 显示弹窗
      this.selectedDistrictNames = selectedRows.map(row => row.name);
    },

    // 新增：关闭批量设置客户弹窗
    clientCancel() {
      this.clientFlag = false;
      this.selectedClientId = ""; // 清空选中值
    },

    // 新增：客户选择校验（可选，增强用户体验）
    validateClient() {
      this.$refs.clientForm.validateField("clientId");
    },

    // 审核驳回
    audit(row) {
      const ids = row.id || this.ids
      const param ={
        ids : ids,
        status:"1"
      }
      this.$modal.confirm('是否确认驳回商圈信息编号为"' + ids + '"的数据项？').then(function () {
        return audit(param)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("驳回成功")
      }).catch(() => { })
    },

    // 释放
    free(row) {
      const ids = row.id || this.ids
      const param ={
        ids : ids,
        status:"0"
      }
      this.$modal.confirm('是否确认释放商圈信息编号为"' + ids + '"的数据项？').then(function () {
        return audit(param)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("释放成功")
      }).catch(() => { })
    },

    // 提交审核
    commit(row) {
      const ids = row.id || this.ids
      const param ={
        ids : ids,
        status:"2"
      }
      this.$modal.confirm('是否确认提交店铺信息编号为"' + ids + '"的数据项？').then(function () {
        return audit(param)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("提交成功")
      }).catch(() => { })
    },

    //审核通过
    auditcc(row) {
      const ids = row.id || this.ids
      const param ={
        ids : ids,
        status:"3"
      }
      this.$modal.confirm('是否确认通过店铺信息编号为"' + ids + '"的数据项？').then(function () {
        return audit(param)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("审核成功")
      }).catch(() => { })
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


    handleSetting() {
      console.log("批量设置跟进人员")
      this.followFlag = true;
      this.followTitle = "批量设置跟进人员"
    },

    formatstatues(row, column) {
      const statusMap = {
        "0":"未提交",
        '1': '审核不通过',
        '2': '审核中',
        '3': '审核通过',
        '4': '已签约'
      };
      return statusMap[row.statues] || row.statues;
    },
    followerformatstatues(row, column) {
      const statusMap = {
        "1": "未跟进",
        '2': '已跟进',
      };
      return statusMap[row.followerStatus] || row.followerStatus;
    },

    getClientPerson() {
      listClient(this.commonqueryParams).then(response => {
        this.clientPerson = []; // 清空数组
        if (response && response.rows && response.rows.length > 0) {
          response.rows.forEach((item, index) => {
            // 每次循环创建新对象！
            const newOption = {
              value: Number(item.id), // 使用 item 的属性赋值
              label: item.name
            };
            console.log(item.id, item.name);
            this.clientPerson.push(newOption); // 推送新对象
          });
        }
      });
    },
    getFollwPerson() {
      listUser(this.queryParams).then(response => {
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

    /** 查询商圈信息列表 */
    getList() {
      this.loading = true
      listDistrict(this.queryParams).then(response => {
        this.districtList = response.rows
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
        region: null,
        followerIds: null,
        followerIdList: [], // 改为与多选框绑定一致的字段名
        customerId: null,
        createdAt: null,
        updatedAt: null,
        creatorId: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
      this.multipleSign = selection.length > 0 && selection.every(item => item.statues === "3");

      // 2. 提交审核：仅「待跟进」（0）或「跟进中」（1）可用（需先提交才能进入审核）
      // 改为：所有选中项的 statues 为 "0" 或 "1" 时，multipleacc 为 true
      this.multipleacc = selection.length > 0 && selection.every(item =>
        item.followerStatus === "2" && ["0", "1"].includes(item.statues)
      );

      // 3. 审核驳回/通过：仅「审核中」（2）可用（只有审核中的数据才能被驳回/通过）
      this.multipleback = selection.length > 0 && selection.every(item => item.statues === "2");
      this.multiplepass = selection.length > 0 && selection.every(item => item.statues === "2");
      this.multipleFollow = selection.length > 0 && selection.every(item => item.followerStatus === "1");
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.form.followerIdList =[]
      this.reset()
      this.open = true
      this.title = "添加商圈信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getDistrict(id).then(response => {
        this.form = response.data
        this.form.followerIdList = response.data.followerIds
          ? response.data.followerIds.split(",").map(Number)
          : [];

        this.open = true
        this.title = "修改商圈信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDistrict(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDistrict(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除商圈信息编号为"' + ids + '"的数据项？').then(function() {
        return delDistrict(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/district/export', {
        ...this.queryParams
      }, `district_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.empty-tip {
  margin-top: 8px;
  color: #999;
  display: flex;
  align-items: center;
}
.empty-tip {
  margin-top: 8px;
  color: #999;
  display: flex;
  align-items: center;
}

/* 保护期剩余天数颜色样式 */
.orange-text {
  color: #ff7d00; /* 橙色（可根据需求调整色值） */
  font-weight: 500; /* 可选：加粗突出 */
}
.red-text {
  color: #f56c6c; /* 红色（Element UI 错误色，与系统风格统一） */
  font-weight: 600; /* 可选：加粗突出紧急状态 */
}
.default-text {
  color: #606266; /* 默认文本色（与表格其他文字颜色一致） */
}

/* 原有样式... */

/* 商圈笔记弹窗样式 */
.note-detail-container {
  padding: 10px 0;
}

.loading-container {
  text-align: center;
  padding: 40px 0;
  color: #606266;
}

.loading-spinner {
  font-size: 24px;
  margin-bottom: 10px;
}

.empty-note {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}

.empty-icon {
  font-size: 32px;
  margin-bottom: 10px;
}

.note-content {
  line-height: 1.8;
}

.note-base-info {
  display: flex;
  flex-wrap: wrap;
  gap: 15px 20px;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px dashed #e6e6e6;
}

.info-item {
  flex: 1;
  min-width: 250px;
}

.info-label {
  display: inline-block;
  width: 80px;
  color: #909399;
}

.info-value {
  color: #303133;
}

.section-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin: 15px 0 8px;
}

.desc-text {
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
  color: #606266;
  min-height: 80px;
}

.note-image {
  margin-top: 15px;
}

.image-container {
  text-align: center;
  padding: 10px;
}

.preview-image {
  width: 100%;
  max-height: 400px;
  cursor: pointer;
  border-radius: 4px;
  border: 1px solid #e6e6e6;
}

.image-tip {
  margin-top: 8px;
  color: #909399;
  font-size: 12px;
}

.no-image {
  text-align: center;
  padding: 20px 0;
  color: #909399;
  background: #f5f7fa;
  border-radius: 4px;
}

/* 图片预览弹窗样式 */
.image-preview-container {
  padding: 20px;
  text-align: center;
}

.full-image {
  max-width: 100%;
  max-height: 600px;
}

.preview-error {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}
/* 多条笔记样式补充 */
.note-item {
  padding: 15px 0;
}

/* 笔记项分隔线 */
.note-divider {
  height: 1px;
  background-color: #e6e6e6;
  margin: 10px 0;
}

/* 调整基础信息间距（适配多条数据） */
.note-base-info {
  display: flex;
  flex-wrap: wrap;
  gap: 10px 15px; /* 缩小间距，避免单条笔记过高 */
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px dashed #f0f0f0; /* 单条笔记内基础信息底部加虚线 */
}


/* 媒体区域样式 */
.media-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
}

.media-item {
  flex: 1;
  min-width: 240px; /* 最小宽度，确保小屏幕下不挤压 */
}

.preview-media {
  width: 100%;
  max-height: 180px; /* 限制媒体高度，避免弹窗过高 */
  border-radius: 4px;
  object-fit: cover; /* 图片/视频保持比例填充 */
}

.media-tip {
  font-size: 12px;
  color: #666;
}

.no-media {
  color: #999;
  padding: 20px;
}
/* 1. 笔记项样式：增加笔记间间隔（重点修改） */
.note-item {
  padding: 15px 0;
  /* 每个笔记底部增加间隔，最后一个笔记去掉多余间距 */
  margin-bottom: 20px;
  /* 可选：给每个笔记加轻微边框，区分更明显 */
  border: 1px solid #f5f5f5;
  border-radius: 6px;
  padding: 15px;
}
/* 排除最后一个笔记的底部间距 */
.note-item:not(:last-child) {
  margin-bottom: 20px;
}
/* 去掉原有的分隔线（用间距+边框替代，更清爽） */
.note-divider {
  display: none;
}

/* 2. 缩小笔记图片（重点修改） */
/* 图片容器：固定宽度，避免图片过大 */
.media-item {
  /* 改为固定宽度（原 min-width:240px，现缩小到140px） */
  flex: 0 0 auto; /* 取消自动拉伸 */
  width: 140px; /* 图片容器固定宽度，控制图片大小 */
  margin-right: 12px; /* 图片之间的横向间距 */
}
/* 图片/视频本身：降低最大高度，缩小显示 */
.preview-media {
  width: 100%; /* 继承容器宽度 */
  max-height: 100px; /* 原 180px，现缩小到100px */
  border-radius: 4px;
  object-fit: cover; /* 保持图片比例，避免变形 */
}
/* 图片下方提示文字：缩小字体，更紧凑 */
.media-tip {
  font-size: 11px; /* 原 12px，略小 */
  color: #888;
  margin-top: 6px; /* 原 10px，减少上下间距 */
}

/* 3. 优化笔记内部间距，避免整体过高 */
.note-base-info {
  gap: 8px 12px; /* 原 10px 15px，缩小基础信息间距 */
  margin-bottom: 10px; /* 原 15px，减少与描述的间距 */
  padding-bottom: 8px;
}
.note-description .desc-text {
  min-height: 60px; /* 原 80px，缩小描述区域最小高度 */
  padding: 8px; /* 原 10px，减少内边距 */
  font-size: 13px; /* 原 14px，略小字体 */
}
</style>
