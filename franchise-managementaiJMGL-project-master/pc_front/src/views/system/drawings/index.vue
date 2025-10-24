<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设计id" prop="designId">
        <el-input
          v-model="queryParams.designId"
          placeholder="请输入设计id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商铺编码" prop="shopId">
        <el-input
          v-model="queryParams.shopId"
          placeholder="请输入商铺编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图幅" prop="pageSize">
        <el-input
          v-model="queryParams.pageSize"
          placeholder="请输入图幅"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图纸编号" prop="drawingNo">
        <el-input
          v-model="queryParams.drawingNo"
          placeholder="请输入图纸编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="图纸名称" prop="drawingName">
        <el-input
          v-model="queryParams.drawingName"
          placeholder="请输入图纸名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件地址" prop="fileUrl">
        <el-input
          v-model="queryParams.fileUrl"
          placeholder="请输入文件地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建人" prop="creator">
        <el-input
          v-model="queryParams.creator"
          placeholder="请输入创建人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker clearable
          v-model="queryParams.createTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="创建时间" prop="upTime">
        <el-date-picker clearable
          v-model="queryParams.upTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="关联最新版本ID" prop="versionId">
        <el-input
          v-model="queryParams.versionId"
          placeholder="请输入关联最新版本ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否有效" prop="isActive">
        <el-input
          v-model="queryParams.isActive"
          placeholder="请输入是否有效"
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:drawings:add']"
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
          v-hasPermi="['system:drawings:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:drawings:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:drawings:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="drawingsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="图纸主表ID" align="center" prop="id" />
      <el-table-column label="设计id" align="center" prop="designId" />
      <el-table-column label="商铺编码" align="center" prop="shopId" />
      <el-table-column label="图纸类型，0封面，1目录，2施工设计说明，3室内装修材料，4材料表，5平面图，6立面图，7大图样" align="center" prop="drawingType" />
      <el-table-column label="图幅" align="center" prop="pageSize" />
      <el-table-column label="图纸编号" align="center" prop="drawingNo" />
      <el-table-column label="图纸名称" align="center" prop="drawingName" />
      <el-table-column label="文件地址" align="center" prop="fileUrl" />
      <el-table-column label="创建人" align="center" prop="creator" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="upTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.upTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="关联最新版本ID" align="center" prop="versionId" />
      <el-table-column label="是否有效" align="center" prop="isActive" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:drawings:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:drawings:remove']"
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

    <!-- 添加或修改图纸信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设计id" prop="designId">
          <el-input v-model="form.designId" placeholder="请输入设计id" />
        </el-form-item>
        <el-form-item label="商铺编码" prop="shopId">
          <el-input v-model="form.shopId" placeholder="请输入商铺编码" />
        </el-form-item>
        <el-form-item label="图幅" prop="pageSize">
          <el-input v-model="form.pageSize" placeholder="请输入图幅" />
        </el-form-item>
        <el-form-item label="图纸编号" prop="drawingNo">
          <el-input v-model="form.drawingNo" placeholder="请输入图纸编号" />
        </el-form-item>
        <el-form-item label="图纸名称" prop="drawingName">
          <el-input v-model="form.drawingName" placeholder="请输入图纸名称" />
        </el-form-item>
        <el-form-item label="文件地址" prop="fileUrl">
          <el-input v-model="form.fileUrl" placeholder="请输入文件地址" />
        </el-form-item>
        <el-form-item label="创建人" prop="creator">
          <el-input v-model="form.creator" placeholder="请输入创建人" />
        </el-form-item>
        <el-form-item label="创建时间" prop="upTime">
          <el-date-picker clearable
            v-model="form.upTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="关联最新版本ID" prop="versionId">
          <el-input v-model="form.versionId" placeholder="请输入关联最新版本ID" />
        </el-form-item>
        <el-form-item label="是否有效" prop="isActive">
          <el-input v-model="form.isActive" placeholder="请输入是否有效" />
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
import { listDrawings, getDrawings, delDrawings, addDrawings, updateDrawings } from "@/api/system/drawings"

export default {
  name: "Drawings",
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
      // 图纸信息表格数据
      drawingsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        designId: null,
        shopId: null,
        drawingType: null,
        drawingNo: null,
        drawingName: null,
        fileUrl: null,
        creator: null,
        createTime: null,
        upTime: null,
        versionId: null,
        isActive: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        designId: [
          { required: true, message: "设计id不能为空", trigger: "blur" }
        ],
        shopId: [
          { required: true, message: "商铺编码不能为空", trigger: "blur" }
        ],
        drawingType: [
          { required: true, message: "图纸类型，0封面，1目录，2施工设计说明，3室内装修材料，4材料表，5平面图，6立面图，7大图样不能为空", trigger: "change" }
        ],
        drawingNo: [
          { required: true, message: "图纸编号不能为空", trigger: "blur" }
        ],
        drawingName: [
          { required: true, message: "图纸名称不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        upTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        isActive: [
          { required: true, message: "是否有效不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询图纸信息列表 */
    getList() {
      this.loading = true
      listDrawings(this.queryParams).then(response => {
        this.drawingsList = response.rows
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
        designId: null,
        shopId: null,
        drawingType: null,
        pageSize: null,
        drawingNo: null,
        drawingName: null,
        fileUrl: null,
        creator: null,
        createTime: null,
        upTime: null,
        versionId: null,
        isActive: null
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
      this.title = "添加图纸信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getDrawings(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改图纸信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDrawings(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDrawings(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除图纸信息编号为"' + ids + '"的数据项？').then(function() {
        return delDrawings(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/drawings/export', {
        ...this.queryParams
      }, `drawings_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.el-table--enable-row-transition .el-table__body td.el-table__cell a {
  color: rgb(70, 70, 243);
}


</style>
