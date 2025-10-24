<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商圈" prop="businessDistrictId">

        <el-select v-model="queryParams.businessDistrictId" clearable placeholder="请选择商圈">
          <el-option v-for="item in businesSq" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
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
          v-hasPermi="['system:note:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:note:edit']"
        >修改</el-button>
      </el-col>

      <!-- 新增：批量审核按钮 -->
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-check-circle"
          size="mini"
          :disabled="multiple"
        @click="handleBatchAudit"
        v-hasPermi="['system:note:batchAudit']"
        >批量审核</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:note:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:note:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="noteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="笔记ID" align="center" prop="id" />
      <el-table-column label="商圈" align="center" prop="businessName" />
      <el-table-column label="跟进人员" align="center" prop="followerName" />
      <el-table-column label="状态" align="center" prop="statusss" :formatter="formatstatuess"  />
      <el-table-column label="文字描述" align="center" prop="description" />
      <el-table-column label="图片URL集合" align="center" prop="imageUrl">
        <template slot-scope="scope">
          <a href="javascript:void(0);" @click.stop="handleLinkClick(scope.row.imageUrl)">点击查看笔记照片</a>
        </template>
      </el-table-column>
      <el-table-column label="视频URL" align="center" prop="videoUrl" >
        <template slot-scope="scope">
          <a href="javascript:void(0);" @click.stop="handleVideoView(scope.row.videoUrl)">点击查看视频</a>
        </template>

      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updatedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:note:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:note:remove']"
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


    <!-- 新增：批量审核弹窗 -->
    <el-dialog :title="auditTitle" :visible.sync="auditFlag" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" :rules="auditRules" label-width="80px">
        <!-- 审核结果选择 -->
        <el-form-item label="审核结果" prop="auditResult" required>
          <el-radio-group v-model="auditForm.auditResult">
            <el-radio label="1">审核通过</el-radio>
            <el-radio label="2">审核驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 审核备注（可选） -->
<!--        <el-form-item label="审核备注" prop="auditRemark">-->
<!--          <el-input-->
<!--            v-model="auditForm.auditRemark"-->
<!--            type="textarea"-->
<!--            placeholder="请输入审核备注（可选）"-->
<!--            rows="3"-->
<!--          />-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBatchAudit">确 定</el-button>
        <el-button @click="cancelBatchAudit">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改商圈跟进笔记（含打卡）对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>

      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="商圈" prop="businessDistrictId">
          <el-select v-model="form.businessDistrictId" clearable placeholder="请选择">
            <el-option v-for="item in businesSq" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="跟进人员" prop="followerId">
          <el-select v-model="form.followerId" clearable placeholder="请选择">
            <el-option v-for="item in followUpPerson" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="文字描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>

        <el-form-item label="照片地址" prop="imageUrls">
          <el-upload class="upload-demo" action="/manage/common/upload" :on-success="handleImgUploadSuccess"
                     :show-file-list="false" list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>
          <div v-if="form.imageUrls && form.imageUrls.length" style="margin-top: 10px;">
            <div v-for="(url, index) in form.imageUrls" :key="index"
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogVisible" :title="imgAndVideoTitle" :close-on-click-modal="false">
      <div class="demo-image__lazy" v-if="imgFlag">
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
import { listNote, getNote, delNote, addNote, updateNote,batchAuditNote } from "@/api/system/note"
import { listDistrict } from '@/api/system/district'
import {  listUser } from '@/api/system/client'


export default {
  name: "Note",
  data() {
    return {

      auditFlag: false, // 审核弹窗显示/隐藏标识
      auditTitle: "批量审核商圈跟进笔记", // 弹窗标题
      auditForm: {
        auditResult: "", // 审核结果：1=通过，0=驳回
        auditRemark: ""  // 审核备注
      },
      // 审核表单校验规则
      // auditRules: {
      //   auditResult: [
      //     { required: true, message: "请选择审核结果", trigger: "change" }
      //   ]
      // },
      auditRules: {
        auditResult: [
          { required: true, message: "请选择审核结果", trigger: "change" }
        ]
      },

      videoFlag:false,
      isShowUploadVideo: false,
      videoUploadPercent: "",

      dialogVisible:false,
      imgAndVideoTitle:"",
      currentImageUrl:"",

      followUpPerson :[],
      businesSq :[],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 商圈跟进笔记（含打卡）表格数据
      noteList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      commonqueryParams: {
        pageNum: 1,
        pageSize: 1000
      },


      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        businessDistrictId: null,
        followerId: null,
        description: null,
        imageUrls: null,
        videoUrl: null,
        checkinLocation: null,
        updatedAt: null
      },
      // 表单参数
      form: {
        imageUrls:[]},
      // 表单校验
      rules: {
        businessDistrictId: [
          { required: true, message: "关联商圈ID不能为空", trigger: "blur" }
        ],
        followerId: [
          { required: true, message: "跟进人员ID不能为空", trigger: "blur" }
        ],
        description: [
          { required: true, message: "文字描述不能为空", trigger: "blur" }
        ],
        checkinLocation: [
          { required: true, message: "打卡位置不能为空", trigger: "blur" }
        ],
        updatedAt: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList();
    this.getBusinessSq();
    this.getFollwPerson();

  },
  methods: {

    // 新增：提交批量审核
    submitBatchAudit() {
      // 1. 表单校验（确保选择审核结果）
      this.$refs.auditForm.validate((valid) => {
        if (!valid) return;


        if (this.ids.length === 0) {
          this.$modal.msgWarning("请先在表格中选择需要审核的笔记");
          return;
        }



        // 3. 构造接口请求参数（与后端字段对齐）
        const batchAuditParams = {
          noteIds: this.ids, // 直接使用已选中的ID数组，无需筛选表格
          statusss: this.auditForm.auditResult // 审核结果（1=通过，0=驳回
        };
        console.log("提交的审核参数：", batchAuditParams);

        // 4. 调用后端批量审核接口（需先导入接口）
        batchAuditNote(batchAuditParams)
          .then((response) => {
            this.$modal.msgSuccess("批量审核成功");
            this.auditFlag = false; // 关闭弹窗
            this.getList(); // 刷新笔记列表，更新状态显示
            this.auditForm = { // 重置表单
              auditResult: "",
            };
          })
          .catch((error) => {
            this.$modal.msgError("批量审核失败：" + (error.msg || "网络异常，请重试"));
          });
      });
    },

    // 原有方法（formatstatuess、handleVideoView等）保持不变...


    handleBatchAudit() {
      // 重置审核表单
      this.auditForm = {
        auditResult: "",
        auditRemark: ""
      };
      this.$refs.auditForm && this.$refs.auditForm.resetFields();
      // 显示弹窗
      this.auditFlag = true;
    },

    // 新增：关闭批量审核弹窗
    cancelBatchAudit() {
      this.auditFlag = false;
      this.auditForm = {
        auditResult: "",
        auditRemark: ""
      };
    },

    formatstatuess(row, column) {

      console.log("笔记数据中的状态字段：", row.statusss);
      const statusMap = {
        "0": "审核中",
        "2": "审核驳回",
        '1': '审核通过'
      };
      return statusMap[row.statusss] || row.statusss;
    },

    handleVideoView(videoUrl) {
      console.log(videoUrl, "视频地址")
      this.imgFlag = false;
      this.imgAndVideoTitle = "查看视频"
      this.dialogVisible = true;
      this.currentVideoUrl = videoUrl; // 设置当前视频地址
    },


    handleLinkClick(url) {
      console.log(url, "链接地址")
      this.dialogVisible = true;
      this.imgFlag = true;
      this.imgAndVideoTitle = "查看图片"
      this.currentImageUrl = url.split(','); // 设置当前图片地址
    },


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

    handleImgUploadSuccess(response) {
      // 假设返回的数据结构为 { url: '上传后的图片地址' }
      if (response.code !== 200) {
        this.$message.error('照片上传失败');
        return;
      }
      this.form.imageUrls.push(response.url); // 将上传的图片地址添加到 form.imgUrl 数组中
      this.$message.success('照片上传成功');
    },

    removeImage(index) {
      this.form.imageUrls.splice(index, 1);
    },

    getBusinessSq() {
      listDistrict(this.commonqueryParams).then(response => {
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


    /** 查询商圈跟进笔记（含打卡）列表 */
    getList() {
      this.loading = true
      listNote(this.queryParams).then(response => {
        this.noteList = response.rows
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
        businessDistrictId: null,
        followerId: null,
        description: null,
        imageUrl: null,
        videoUrl: null,
        checkinLocation: null,
        updatedAt: null,
        imageUrls:[]
      }
      this.form.videoUrl ="";
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
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加商圈跟进笔记"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getNote(id).then(response => {
        this.form = response.data
        this.form.imageUrls = response.data.imageUrl
          ? response.data.imageUrl.split(",")
          : [];
        this.open = true
        this.title = "修改商圈跟进笔记"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateNote(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addNote(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除商圈跟进笔记（含打卡）编号为"' + ids + '"的数据项？').then(function() {
        return delNote(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/note/export', {
        ...this.queryParams
      }, `note_${new Date().getTime()}.xlsx`)
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
</style>
