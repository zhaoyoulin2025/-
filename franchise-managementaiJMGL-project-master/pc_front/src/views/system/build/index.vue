<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="跟进员工" prop="followerId">
        <el-select v-model="queryParams.follower" clearable placeholder="请选择跟进员工">
          <el-option v-for="item in builduserList" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="店铺名称" prop="shopId">
        <el-select v-model="queryParams.shopId" clearable placeholder="请选择店铺">
          <el-option v-for="item in shopList" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="客户姓名" prop="clientId">
        <el-select v-model="queryParams.clientId" clearable placeholder="请选择客户">
          <el-option v-for="item in clientPerson" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="创建时间" prop="sTime">
        <el-date-picker clearable
                        v-model="queryParams.sTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table ref="multipleTable" v-loading="loading" :data="buildList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="店铺名称" align="center" prop="name" />
      <el-table-column label="营建进度" align="center" prop="rate" />
      <el-table-column label="跟随人姓名" align="center" prop="followerName" />
      <el-table-column label="客户姓名" align="center" prop="clientName" />
      <el-table-column label="创建时间" align="center" prop="sTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.sTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="营建详情" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="showShopDetail(scope.row)"
            v-hasPermi="['system:build:edit']"
          >查看营建进度</el-button>
        </template>
      </el-table-column>
      <el-table-column label="监理巡检" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="showsuperviseList(scope.row)"
            v-hasPermi="['system:build:edit']"
          >查看监理巡检</el-button>
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


    <el-dialog :title="buildtitle" :visible.sync="detailopen" width="90%" append-to-body>
          <el-table ref="multipleTable" v-loading="loading" :data="buildingList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="主键ID" align="center" prop="id" />
            <el-table-column label="监理姓名" align="center" prop="superviserName" />
            <el-table-column label="跟随人姓名" align="center" prop="followerName" />
            <el-table-column label="店铺名称" align="center" prop="shopName" />
            <el-table-column label="客户姓名" align="center" prop="clientName" />
            <el-table-column label="营建阶段" align="center" prop="periodId"  :formatter="formatPrioderStatus"/>
            <el-table-column label="营建状态" align="center" prop="superviseStatus" :formatter="formatBuildStatus"/>
            <el-table-column label="店铺照片" align="center" prop="fileUrl">
              <template slot-scope="scope">
                <a href="javascript:void(0);" @click.stop="handleLinkClick(scope.row.fileUrl)">监理照片</a>
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
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['system:build:edit']"
                >修改</el-button>
              </template>
            </el-table-column>
          </el-table>
    </el-dialog>



    <el-dialog :title="tt" :visible.sync="superviseopen" width="60%" append-to-body>
      <el-table ref="multipleTable" v-loading="loading" :data="superviseList" @selection-change="handleSelectionChange">
<!--        <el-table-column type="selection" width="55" align="center" />-->
        <el-table-column label="商户" align="center" prop="shopName" />
        <el-table-column label="监理" align="center" prop="superviserName" />
<!--        <el-table-column label="id" align="center" prop="id" />-->
        <el-table-column label="检查时间" align="center" prop="createDate" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="检查项目" align="center" prop="project" />
        <el-table-column label="检查质量" align="center" prop="quality" />
        <el-table-column label="发现问题" align="center" prop="question" />
        <el-table-column label="建议" align="center" prop="suggestion" />
        <el-table-column label="图片" align="center" prop="img">
          <template slot-scope="scope">
            <a href="javascript:void(0);" @click.stop="handleLinkClick(scope.row.img)">监理照片</a>
          </template>
        </el-table-column>

        <el-table-column label="进度" align="center" prop="progress" />
<!--        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--          <template slot-scope="scope">-->
<!--            <el-button-->
<!--              size="mini"-->
<!--              type="text"-->
<!--              icon="el-icon-edit"-->
<!--              @click="handleUpdate(scope.row)"-->
<!--              v-hasPermi="['system:manage:edit']"-->
<!--            >修改</el-button>-->
<!--            <el-button-->
<!--              size="mini"-->
<!--              type="text"-->
<!--              icon="el-icon-delete"-->
<!--              @click="handleDelete(scope.row)"-->
<!--              v-hasPermi="['system:manage:remove']"-->
<!--            >删除</el-button>-->
<!--          </template>-->
<!--        </el-table-column>-->

      </el-table>
    </el-dialog>



    <!-- 添加或修改建设管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="监理姓名" prop="superviserId">
          <el-select v-model="form.superviserId" clearable placeholder="请选择监理姓名">
            <el-option v-for="item in superviser" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>


        <el-form-item label="跟进人员" prop="followerId">
          <el-select v-model="form.followerId" clearable placeholder="请选择跟进人员">
            <el-option v-for="item in builduserList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>


        <el-form-item label="店铺名称" prop="shopId">
          <el-select v-model="form.shopId" clearable placeholder="请选择店铺名称">
            <el-option v-for="item in shopList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>


        <el-form-item label="客户姓名" prop="clientId">
          <el-select v-model="form.clientId" clearable placeholder="请选择客户姓名">
            <el-option v-for="item in clientPerson" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>


        <el-form-item label="照片地址" prop="imgUrl">
          <el-upload class="upload-demo" action="/manage/common/upload" :on-success="handleImgUploadSuccess"
                     :show-file-list="false" list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>
          <div v-if="imgUrl && imgUrl.length" style="margin-top: 10px;">
            <div v-for="(url, index) in imgUrl" :key="index"
                 style="display: inline-block; margin-right: 10px; position: relative;">
              <img :src="url" alt="图片预览" style="max-width: 200px; max-height: 200px; display: block;">
              <el-button size="mini" icon="el-icon-delete" circle style="position: absolute; top: -10px; right: -10px;"
                         @click.stop="removeImage(index)"></el-button>
            </div>
          </div>
        </el-form-item>



        <el-form-item label="营建状态" prop="superviseStatus">
          <el-select v-model="form.superviseStatus" clearable placeholder="请选择营建状态">
            <el-option v-for="item in buildStatus" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="supervisertitle" :visible.sync="superviseropen" width="500px" append-to-body>
      <el-form ref="superviserform" :model="superviserform" :rules="rules" label-width="80px">
        <el-form-item label="监理人员" prop="followPeople">
          <el-select v-model="batchsuperviserId" clearable placeholder="请选择监理人员">
            <el-option v-for="item in superviser" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSuperviser">确 定</el-button>
        <el-button @click="cancelSuperviser">取 消</el-button>
      </div>
    </el-dialog>


    <el-dialog :title="batchfollowertitle" :visible.sync="batchfolloweropen" width="500px" append-to-body>
      <el-form ref="batchfollowerform" :model="batchfollowerform" :rules="rules" label-width="80px">
        <el-form-item label="跟进人员" prop="followPeople">
          <el-select v-model="batchfollowerId" clearable placeholder="请选择跟进人员">
            <el-option v-for="item in builduserList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFollwer">确 定</el-button>
        <el-button @click="cancelFollwer">取 消</el-button>
      </div>
    </el-dialog>


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
    </el-dialog>

  </div>
</template>

<script>
import { listBuild, getBuild, delBuild, addBuild, updateBuild ,addSuperviser,addFollower,shoplistBuild} from "@/api/system/build"

import {listSuperviser,listUser} from "@/api/system/user";
import {listClient} from "@/api/system/client";
import { listShop } from '@/api/system/shop'

import { listManageshop } from '@/api/system/manage'

export default {
  name: "Build",
  data() {
    return {
      commonqueryParams: {
        pageNum: 1,
        pageSize: 1000
      },
      superviser:[],
      clientPerson:[],
      buildPrioderList:[],
      buildStatus:[],
      shopList:[],
      builduserList:[],
      detailopen:false,
      superviseopen:false,
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
      // 建设管理表格数据
      buildList: [],
      buildingList: [],
      superviseList: [],
      // 弹出层标题
      title: "",
      supervisertitle:"",
      batchfollowertitle:"",
      // 是否显示弹出层
      open: false,
      superviseropen:false,
      batchfolloweropen:false,
      batchsuperviserId: "",
      batchfollowerId:"",
      buildtitle:"",
      tt:"",


      currentImageUrl: '', // 当前查看的图片地址
      imgAndVideoTitle: "",
      imgFlag: false, //展示视频区域
      dialogVisible : false,

      imgUrl:[],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shopId: null,
        clientId: null,
        sTime: null
      },
      queryBuildParams: {
        pageNum: 1,
        pageSize: 10,
        shopId: null,
        clientId: null,
        sTime: null
      },
      querysuperviseParams: {
        pageNum: 1,
        pageSize: 10,
        shopId: null,
        clientId: null,
        sTime: null
      },
      // 表单参数
      form: {},
      superviserform: {},
      batchfollowerform: {},
      // 表单校验
      rules: {
        followerId: [
          { required: true, message: "跟随人不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList();
    this.getSuperviser();
    this.getShopList();
    this.getClientPerson();
    this.getUserList();
    this.getBuildStatus();
    this.getBuildPrioderList();
  },
  methods: {
    handleLinkClick(url) {
      console.log(url, "链接地址")
      this.dialogVisible = true;
      this.imgFlag = true;
      this.imgAndVideoTitle = "查看图片"
      this.currentImageUrl = url.split(','); // 设置当前图片地址
    },


    handleImgUploadSuccess(response) {
      // 假设返回的数据结构为 { url: '上传后的图片地址' }
      if (response.code !== 200) {
        this.$message.error('照片上传失败');
        return;
      }
      this.imgUrl.push(response.url); // 将上传的图片地址添加到 form.imgUrl 数组中
      this.$message.success('照片上传成功');
    },
    // 删除指定索引的图片
    removeImage(index) {
      this.imgUrl.splice(index, 1);
    },




    cancelFollwer(){
      this.batchfolloweropen = false;
      this.followerreset();
    },

    submitFollwer(){
      this.batchfolloweropen = false;
      // 直接从表格实例获取选中数据
      const selectedRows = this.$refs.multipleTable.selection;
      console.log('选中的数据：', selectedRows);
      var followArrForSet = {
        "followerId": this.batchfollowerId,
        "measureIds": []
      }
      selectedRows.forEach((item, index) => {
        followArrForSet.measureIds.push(item.id)
      })
      addFollower(followArrForSet).then((response) => {
        this.$modal.msgSuccess("批量添加跟进人员成功")
        this.getList();
      })
    },
    addFlollowers(row) {
      this.followerreset()
      this.batchfolloweropen = true
      this.batchfollowertitle = "批量添加跟进人员"
    },
    followerreset() {
      this.batchsuperviserId = ""
      this.resetForm("batchfollowerform")
    },

    cancelSuperviser(){
      this.superviseropen = false;
      this.superviserreset();
    },

    submitSuperviser(){
      this.superviseropen = false;
      // 直接从表格实例获取选中数据
      const selectedRows = this.$refs.multipleTable.selection;
      console.log('选中的数据：', selectedRows);
      var followArrForSet = {
        "superviserId": this.batchsuperviserId,
        "measureIds": []
      }
      selectedRows.forEach((item, index) => {
        followArrForSet.measureIds.push(item.id)
      })
      addSuperviser(followArrForSet).then((response) => {
        this.$modal.msgSuccess("批量添加监理人员成功")
        this.getList();
      })
    },
    handleAdd(row) {
      this.superviserreset()
      this.superviseropen = true
      this.supervisertitle = "批量添加监理人员"
    },
    superviserreset() {
      this.batchsuperviserId = ""
      this.resetForm("superviserform")
    },





    formatPrioderStatus(row, column) {

      const statusItem = this.buildPrioderList.find(
        item => item.value === row.periodId
      );
      return statusItem ? statusItem.label : `未知状态(${row.periodId})`;
    },

    formatBuildStatus(row, column) {
      const statusItem = this.buildStatus.find(
        item => item.value === row.superviseStatus
      );
      return statusItem ? statusItem.label : `未知状态(${row.superviseStatus})`;


      return this.buildStatus[row.superviseStatus] || row.superviseStatus;
    },

    getBuildStatus(){
      this.buildStatus.push({value: 0, label: "未开始"});
      this.buildStatus.push({value: 1, label: "营建中"})
      this.buildStatus.push({value: 2, label: "已完成"})
    },
    getBuildPrioderList(){
      this.buildPrioderList.push({value: 1, label: "设计图纸确认"});
      this.buildPrioderList.push({value: 2, label: "施工队进场"})
      this.buildPrioderList.push({value: 3, label: "水电改造"})
      this.buildPrioderList.push({value: 4, label: "装修施工"})
      this.buildPrioderList.push({value: 5, label: "竣工验收"})
    },



    getUserList(){
      listUser(this.commonqueryParams).then(response => {
        this.builduserList = []; // 清空数组
        if (response && response.rows && response.rows.length > 0) {
          response.rows.forEach((item, index) => {
            // 每次循环创建新对象！
            const newOption = {
              value: item.userId, // 使用 item 的属性赋值
              label: item.userName
            };
            this.builduserList.push(newOption); // 推送新对象
          });
        }
      });
    },

    getShopList() {
      listShop(this.commonqueryParams).then(response => {
        this.shopList = []; // 清空数组
        if (response && response.rows && response.rows.length > 0) {
          response.rows.forEach((item, index) => {
            // 每次循环创建新对象！
            const newOption = {
              value: item.id, // 使用 item 的属性赋值
              label: item.name
            };
            this.shopList.push(newOption); // 推送新对象
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

    getSuperviser(){
      this.loading = true
      this.superviser =[];
      listSuperviser(this.queryParams).then(response => {
        response.rows.forEach((item, index) => {
          // 每次循环创建新对象！
          const newOption = {
            value: item.userId, // 使用 item 的属性赋值
            label: item.userName
          };
          console.log(item.userId, "item.userId");
          this.superviser.push(newOption); // 推送新对象
        });
      })


    },
    /** 查询建设管理列表 */
    getList() {
      this.loading = true
      shoplistBuild(this.queryParams).then(response => {
        this.buildList = response.rows
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
        superviserId: null,
        followerId: null,
        shopId: null,
        clientId: null,
        sTime: null,
        upTime: null,
        superviseStatus: null,
        fileUrl:null
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
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getBuild(id).then(response => {
        this.form = response.data
        if(this.form.fileUrl){
          this.imgUrl = this.form.fileUrl.split(",")
        }else{
          this.imgUrl =[]
        }
        this.open = true
        this.title = "修改建设管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.form.fileUrl = this.imgUrl.join(',')
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBuild(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addBuild(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
      this.imgUrl =[];
    },
    /** 查看营建进度 */
    showShopDetail(row){
      this.detailopen = true
      this.buildtitle = "查看营建详情"
      this.queryBuildParams.shopId = row.id
      listBuild(this.queryBuildParams).then(response => {
        this.buildingList = response.rows
      })
    },
    showsuperviseList(row){
      this.superviseopen = true
      this.tt = "监理巡检记录"
      this.querysuperviseParams.shopId = row.id
      listManageshop(this.querysuperviseParams).then(response => {
        this.superviseList = response.rows
      })
    },



    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除建设管理编号为"' + ids + '"的数据项？').then(function() {
        return delBuild(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/build/export', {
        ...this.queryParams
      }, `build_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>


<style scoped>
.el-table--enable-row-transition .el-table__body td.el-table__cell a {
  color: rgb(70, 70, 243);
}


</style>

