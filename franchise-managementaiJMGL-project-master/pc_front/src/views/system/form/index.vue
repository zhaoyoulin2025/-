<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电话号码" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入电话号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所在地点" prop="residence">
        <el-input
          v-model="queryParams.residence"
          placeholder="请输入所在地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="经营品类" prop="businessCategory">
        <el-input
          v-model="queryParams.businessCategory"
          placeholder="请输入经营品类"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投资预算" prop="investmentBudget">
        <el-input
          v-model="queryParams.investmentBudget"
          placeholder="请输入投资预算"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--<el-col :span="1.5">-->
      <!--  <el-button-->
      <!--    type="primary"-->
      <!--    plain-->
      <!--    icon="el-icon-plus"-->
      <!--    size="mini"-->
      <!--    @click="handleAdd"-->
      <!--    v-hasPermi="['system:form:add']"-->
      <!--  >新增</el-button>-->
      <!--</el-col>-->
      <!--<el-col :span="1.5">-->
      <!--  <el-button-->
      <!--    type="success"-->
      <!--    plain-->
      <!--    icon="el-icon-edit"-->
      <!--    size="mini"-->
      <!--    :disabled="single"-->
      <!--    @click="handleUpdate"-->
      <!--    v-hasPermi="['system:form:edit']"-->
      <!--  >修改</el-button>-->
      <!--</el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:form:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:form:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="formList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="电话号码" align="center" prop="phone" />
      <el-table-column label="地点" align="center" prop="residence" />
      <el-table-column label="经营品类" align="center" prop="businessCategory" />
      <el-table-column label="投资预算" align="center" prop="investmentBudget" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!--<el-button-->
          <!--  size="mini"-->
          <!--  type="text"-->
          <!--  icon="el-icon-edit"-->
          <!--  @click="handleUpdate(scope.row)"-->
          <!--  v-hasPermi="['system:form:edit']"-->
          <!--&gt;修改</el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:form:remove']"
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

    <!-- 添加或修改加盟投资意向对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="电话号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话号码" />
        </el-form-item>
        <el-form-item label="省份" prop="province">
          <el-input v-model="form.province" placeholder="请输入省份" />
        </el-form-item>
        <el-form-item label="居住地" prop="residence">
          <el-input v-model="form.residence" placeholder="请输入居住地" />
        </el-form-item>
        <el-form-item label="经常光顾店" prop="frequentStore">
          <el-input v-model="form.frequentStore" placeholder="请输入经常光顾店" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="投资预算" prop="investmentBudget">
          <el-input v-model="form.investmentBudget" placeholder="请输入投资预算" />
        </el-form-item>
        <el-form-item label="合作模式" prop="cooperationMode">
          <el-input v-model="form.cooperationMode" placeholder="请输入合作模式" />
        </el-form-item>
        <el-form-item label="期望开店地址" prop="expectedStoreAddress">
          <el-input v-model="form.expectedStoreAddress" placeholder="请输入期望开店地址" />
        </el-form-item>
        <el-form-item label="期望开店省份" prop="expectedProvince">
          <el-input v-model="form.expectedProvince" placeholder="请输入期望开店省份" />
        </el-form-item>
        <el-form-item label="期望开店市" prop="expectedCity">
          <el-input v-model="form.expectedCity" placeholder="请输入期望开店市" />
        </el-form-item>
        <el-form-item label="期望开店区" prop="expectedDistrict">
          <el-input v-model="form.expectedDistrict" placeholder="请输入期望开店区" />
        </el-form-item>
        <el-form-item label="从事行业" prop="industry">
          <el-input v-model="form.industry" placeholder="请输入从事行业" />
        </el-form-item>
        <el-form-item label="如何看待发展" prop="developmentOpinion">
          <el-input v-model="form.developmentOpinion" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="选择我的原因" prop="selectionReason">
          <el-input v-model="form.selectionReason" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="我们提升的建议" prop="improvementSuggestion">
          <el-input v-model="form.improvementSuggestion" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="你认为的商圈" prop="businessDistrict">
          <el-input v-model="form.businessDistrict" placeholder="请输入你认为的商圈" />
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
import { listForm, getForm, delForm, addForm, updateForm } from "@/api/system/form"

export default {
  name: "Form",
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
      // 加盟投资意向表格数据
      formList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        phone: null,
        province: null,
        residence: null,
        frequentStore: null,
        storeType: null,
        remarks: null,
        investmentBudget: null,
        cooperationMode: null,
        expectedStoreAddress: null,
        expectedProvince: null,
        expectedCity: null,
        expectedDistrict: null,
        industry: null,
        developmentOpinion: null,
        selectionReason: null,
        improvementSuggestion: null,
        businessDistrict: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "电话号码不能为空", trigger: "blur" }
        ],
        province: [
          { required: true, message: "省份不能为空", trigger: "blur" }
        ],
        residence: [
          { required: true, message: "居住地不能为空", trigger: "blur" }
        ],
        storeType: [
          { required: true, message: "门店形式不能为空", trigger: "change" }
        ],
        investmentBudget: [
          { required: true, message: "投资预算不能为空", trigger: "blur" }
        ],
        cooperationMode: [
          { required: true, message: "合作模式不能为空", trigger: "blur" }
        ],
        industry: [
          { required: true, message: "从事行业不能为空", trigger: "blur" }
        ],
        developmentOpinion: [
          { required: true, message: "如何看待发展不能为空", trigger: "blur" }
        ],
        selectionReason: [
          { required: true, message: "选择我的原因不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询加盟投资意向列表 */
    getList() {
      this.loading = true
      listForm(this.queryParams).then(response => {
        this.formList = response.rows
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
        phone: null,
        province: null,
        residence: null,
        frequentStore: null,
        storeType: null,
        remarks: null,
        investmentBudget: null,
        cooperationMode: null,
        expectedStoreAddress: null,
        expectedProvince: null,
        expectedCity: null,
        expectedDistrict: null,
        industry: null,
        developmentOpinion: null,
        selectionReason: null,
        improvementSuggestion: null,
        businessDistrict: null,
        createTime: null
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
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加加盟投资意向"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getForm(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改加盟投资意向"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateForm(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addForm(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除加盟投资意向编号为"' + ids + '"的数据项？').then(function() {
        return delForm(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/form/export', {
        ...this.queryParams
      }, `form_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
