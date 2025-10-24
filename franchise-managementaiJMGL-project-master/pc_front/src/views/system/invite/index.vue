<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="客户姓名" prop="clientName">
        <el-select v-model="queryParams.clientName" clearable placeholder="请选择客户姓名" :disabled="clientNameFlag">
          <el-option v-for="item in clientNameArr" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="跟进人员" prop="guideName">
        <el-input v-model="queryParams.guideName" placeholder="请输入跟进人员" clearable
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="邀约状态" prop="inviteStatus">
        <el-select v-model="queryParams.inviteStatus" clearable placeholder="请选择">
          <el-option v-for="item in inviteStatusArr" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="邀请时间" prop="invitationTime">
        <el-date-picker clearable v-model="queryParams.invitationTime" type="date" value-format="yyyy-MM-dd"
                        placeholder="请选择邀请时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="创建时间" prop="sTime">
        <el-date-picker clearable v-model="queryParams.sTime" type="date" value-format="yyyy-MM-dd"
                        placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPermi="['system:invite:add']">新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPermi="['system:invite:edit']">修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                   v-hasPermi="['system:invite:remove']">删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   v-hasPermi="['system:invite:export']">导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inviteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <!--      <el-table-column label="主键ID" align="center" prop="id" />-->
      <el-table-column label="客户姓名" align="center" prop="clientName"/>
      <el-table-column label="跟进人员" align="center" prop="guideName"/>
      <el-table-column label="邀请时间" align="center" prop="invitationTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.invitationTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="时间点" align="center" prop="timeMoment" width="100"/>
      <el-table-column label="邀请描述" align="center" prop="invitationDesc"/>
      <el-table-column label="邀约地点" align="center" prop="invitePlace"/>
      <el-table-column label="邀约状态" align="center" prop="inviteStatus" :formatter="inviteStatusFun"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:invite:edit']">修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:invite:remove']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 添加或修改邀请记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="客户姓名" prop="clientId">
          <el-select v-model="form.clientId" clearable placeholder="请选择客户姓名" :disabled="clientNameFlag">
            <el-option v-for="item in clientNameArr" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="邀请时间" prop="invitationTime">
          <el-date-picker clearable v-model="form.invitationTime" type="date" value-format="yyyy-MM-dd"
                          placeholder="请选择邀请时间" @change="handleDateChange">
          </el-date-picker>
        </el-form-item>

        <!--<el-form-item label="邀约状态" prop="inviteStatus">-->
        <!--  <el-select v-model="form.inviteStatus" clearable placeholder="请选择邀约状态">-->
        <!--    <el-option v-for="item in inviteStatusArr" :key="item.value" :label="item.label" :value="item.value">-->
        <!--    </el-option>-->
        <!--  </el-select>-->
        <!--</el-form-item>-->


        <el-form-item label="时间点" prop="timeMoment">
          <el-radio-group v-model="form.timeMoment">
            <el-radio v-for="item in timeMomentArr" :label="item" :key="item" border>{{ item }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="邀请描述" prop="invitationDesc">
          <el-input v-model="form.invitationDesc" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="邀约地点" prop="invitePlace">
          <el-input v-model="form.invitePlace" type="textarea" placeholder="请输入内容"/>
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
import {listInvite, getInvite, delInvite, addInvite, updateInvite, queryTimeByDate} from "@/api/system/invite"
import {
  listClient
} from "@/api/system/client"

export default {
  name: "Invite",
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
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 邀请记录表格数据
      inviteList: [],
      // 客户姓名标识
      clientNameFlag: false,
      // 客户列表
      clientList: [],
      clientNameArr: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      commonqueryParams: {
        pageNum: 1,
        pageSize: 1000,
        clientId: null,
        guideId: null,
        invitationTime: null,
        sTime: null,
        invitationDesc: null,
        inviteStatus: null,
        invitePlace: null
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        clientId: null,
        guideId: null,
        invitationTime: null,
        sTime: null,
        invitationDesc: null,
        inviteStatus: null
      },
      timeMomentArr: ["10:00", "11:00", "14:00", "15:00", "16:00", "17:00"],
      // 表单参数
      form: {},
      checkboxGroupLockArr: [],
      inviteStatusArr: [{
        value: 0,
        label: '待确认'
      }, {
        value: 1,
        label: '已确认'
      }, {
        value: 2,
        label: '推迟计划'
      }],
      // 表单校验
      rules: {
        clientId: [
          {required: true, message: '请选择客户姓名', trigger: 'change'}
        ],
        guideName: [
          {required: true, message: '请输入跟进人员', trigger: 'blur'}
        ],
        invitationTime: [
          {required: true, message: '请选择邀请时间', trigger: 'change'}
        ],
        timeMoment: [
          {required: true, message: '请选择时间点', trigger: 'change'}
        ],
        invitationDesc: [
          {required: true, message: '请输入邀请描述', trigger: 'blur'}
        ],
        invitePlace: [
          {required: true, message: '请输入邀约地点', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询邀请记录列表 */
    getList() {
      this.loading = true
      listInvite(this.queryParams).then(response => {
        this.inviteList = response.rows
        this.total = response.total
        this.loading = false
      });
      // 查询
      listClient(this.commonqueryParams).then(response => {
        this.clientList = response.rows
        this.clientNameArr = this.clientList.map(item => {
          return {value: item.id, label: item.name}
        })
      })
    },
    // 选择创建时间触发
    handleDateChange(val) {
      if (!val) return; // 空值不处理
      queryTimeByDate({
        dateStr: val
      }).then(res => {
        this.form.timeMoment = res.data;
        this.checkboxGroupLockArr = res.data;
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
        clientId: null,
        guideId: null,
        clientName: null,
        guideName: null,
        invitationTime: null,
        sTime: null,
        invitationDesc: null,
        inviteStatus: null,
        timeMoment: []
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.clientNameFlag = false;
      this.reset()
      this.open = true
      this.title = "添加邀请记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.clientNameFlag = true
      this.reset()
      const id = row.id || this.ids
      getInvite(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改邀请记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateInvite(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addInvite(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除邀请记录编号为"' + ids + '"的数据项？').then(function () {
        return delInvite(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/invite/export', {
        ...this.queryParams
      }, `invite_${new Date().getTime()}.xlsx`)
    },

    //   邀约状态格式化
    inviteStatusFun(row, column) {
      const inviteMap = {
        0: '待确认',
        1: '已确认',
        2: '推迟计划'
      };
      return inviteMap[row.inviteStatus] || '不明原因';
    }
  }
}
</script>

<style scoped>
/deep/ .el-radio-group {
  display: flex;
  flex-wrap: wrap;

  .el-radio:first-child {
    margin-left: 10px;
  }

  .el-radio {
    margin-bottom: 10px;
  }
}
</style>

