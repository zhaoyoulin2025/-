<template>
  <div class="app-container" style="min-width: 1600px;">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="电话号码" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入电话号码" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="客户等级" prop="clientLevel">
        <el-select v-model="queryParams.clientLevel" clearable placeholder="请选择客户等级">
          <el-option v-for="item in clientLevelArr" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="跟进人员" prop="guideId">
        <el-select v-model="queryParams.guideId" clearable placeholder="请选择跟进人员">
          <el-option v-for="item in followUpPerson" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="群号" prop="groupNum">
        <el-input
          v-model="queryParams.groupNum"
          placeholder="请输入群号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="意向城市" prop="likeCity">
        <el-input
          v-model="queryParams.likeCity"
          placeholder="请输入意向城市"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预算" prop="budget">
        <el-input
          v-model="queryParams.budget"
          placeholder="请输入预算"
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPermi="['system:client:add']">新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPermi="['system:client:edit']">修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                   v-hasPermi="['system:client:remove']">删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   v-hasPermi="['system:client:export']">导出
        </el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      ref="multipleTable"
      :data="clientList"
      @selection-change="handleSelectionChange"
      class="custom-table-header"
      border
      :resizeable="true"
    >
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="姓名" align="center" prop="name" fixed width="120"/>
      <el-table-column label="群号" align="center" prop="groupNum" width="100"/>
      <el-table-column label="微信号" align="center" prop="wechat" width="150"/>
      <el-table-column label="电话号码" align="center" prop="phone" width="130"/>
      <el-table-column label="首次咨询时间" align="center" prop="firstTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.firstTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="经常光顾店" align="center" prop="frequentStore" min-width="150"/>
      <el-table-column label="到访次数" align="center" prop="comeTime" width="100"/>
      <el-table-column label="客户等级" align="center" prop="clientLevel" :formatter="formatClientLevel" width="100"/>
      <el-table-column label="期望开店市" align="center" prop="expectedCity" min-width="120"/>
      <el-table-column label="品牌合作方式" align="center" prop="cooperationMode" min-width="150"/>
      <el-table-column label="投资预算" align="center" prop="investmentBudget" min-width="120"/>
      <el-table-column label="门店投资方式" align="center" prop="storeType" min-width="120"/>
      <el-table-column label="您正在做的餐饮品牌" align="center" prop="industry" min-width="180"/>
      <el-table-column label="成交时间" align="center" prop="dealTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dealTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="居住地" align="center" prop="residence" min-width="150"/>
      <el-table-column label="门店经营参与方式" align="center" prop="joinType" min-width="150"/>
      <el-table-column label="是否接受转让" align="center" prop="isTransfor" :formatter="formatIsTransfor" width="120"/>
      <el-table-column label="如何看待有转让费" align="center" prop="otherThink" min-width="180"/>
      <el-table-column label="那你认为柒小螺的优势" align="center" prop="selectionReason" min-width="200" show-overflow-tooltip/>
      <el-table-column label="您觉得柒小螺哪些方面需要提升或优化" align="center" prop="improvementSuggestion" min-width="200" show-overflow-tooltip/>
      <el-table-column label="备注" align="center" prop="remarks" min-width="180" show-overflow-tooltip/>
      <el-table-column label="铺位查看权限" align="center" prop="shopResource" :formatter="formatShopResource" width="120"/>
      <el-table-column label="客户状态" align="center" prop="clientStatus" :formatter="formatStatus" width="100"/>
      <el-table-column label="跟进人员" align="center" prop="guideName" min-width="120"/>
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
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        fixed="right"
        width="230"
        min-width="200"
      >
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-user" @click="handleSetFollow(scope.row)"
                     v-hasPermi="['system:client:follow']">跟进
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:client:edit']">修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:client:remove']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>

    <!-- 添加或修改客户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="群号" prop="groupNum">
          <el-input v-model="form.groupNum" placeholder="请输入群号"/>
        </el-form-item>
        <el-form-item label="微信号" prop="wechat">
          <el-input v-model="form.wechat" placeholder="请输入微信"/>
        </el-form-item>
        <el-form-item label="电话号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话号码"/>
        </el-form-item>
        <el-form-item label="经常光顾店" prop="frequentStore">
          <el-input v-model="form.frequentStore" placeholder="请输入经常光顾店"/>
        </el-form-item>
        <el-form-item label="到访次数" prop="comeTime">
          <el-input v-model="form.comeTime" placeholder="请输入到访次数"/>
        </el-form-item>
        <el-form-item label="客户等级" prop="clientLevel">
          <el-select v-model="form.clientLevel" clearable placeholder="请选择客户等级">
            <el-option v-for="item in clientLevelArr" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="期望开店市" prop="expectedCity">
          <el-input v-model="form.expectedCity" placeholder="请输入期望开店市"/>
        </el-form-item>
        <el-form-item label="品牌合作方式" prop="cooperationMode">
          <el-input v-model="form.cooperationMode" placeholder="请输入品牌合作方式"/>
        </el-form-item>
        <el-form-item label="投资预算" prop="investmentBudget">
          <el-input v-model="form.investmentBudget" placeholder="请输入投资预算"/>
        </el-form-item>
        <el-form-item label="门店投资方式" prop="investmentBudget">
          <el-input v-model="form.storeType" placeholder="请输入门店投资方式"/>
        </el-form-item>
        <el-form-item label="您正在做的餐饮品牌" prop="industry">
          <el-input v-model="form.industry" placeholder="请输入从事行业"/>
        </el-form-item>
        <el-form-item label="成交时间" prop="dealTime">
          <el-date-picker clearable
                          v-model="form.dealTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择成交时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="居住地" prop="residence">
          <el-input v-model="form.residence" placeholder="请输入居住地"/>
        </el-form-item>
        <el-form-item label="门店经营参与方式" prop="businessCategory">
          <el-input v-model="form.joinType" placeholder="请输入门店经营参与方式"/>
        </el-form-item>
        <el-form-item label="是否接受转让" prop="isTransfor">
          <el-select
            v-model="form.isTransfor"
            clearable
            placeholder="请选择是否接受转让"
            style="width: 100%"
          >
            <el-option
              v-for="item in isTransforOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="如何看待有转让费" prop="otherThink">
          <el-input v-model="form.otherThink" placeholder="请输入如何看待有转让费"/>
        </el-form-item>
        <el-form-item label="那你认为柒小螺的优势" prop="selectionReason">
          <el-input v-model="form.selectionReason" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="您觉得柒小螺哪些方面需要提升或优化" prop="improvementSuggestion">
          <el-input v-model="form.improvementSuggestion" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="铺位查看权限" prop="shopResource">
          <el-select v-model="form.shopResource" clearable placeholder="请选择客户是否具有铺位查看权限">
            <el-option v-for="item in shopResourceMapArr" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客户状态" prop="clientStatus">
          <el-select v-model="form.clientStatus" clearable placeholder="请选择客户状态">
            <el-option v-for="item in statusMapArr" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="跟进人员" prop="guideId">
          <el-select v-model="form.guideId" clearable placeholder="请选择跟进人员">
            <el-option v-for="item in followUpPerson" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
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

    <el-dialog :visible.sync="dialogVisible" title="跟进记录" width="60%">
      <div class="modal-header">
        <el-button type="primary" @click="handleFollowHistroy">新增</el-button>
      </div>
      <el-table v-loading="loading" :data="upList">
        <el-table-column label="跟进用户" align="center" prop="nickName"/>
        <el-table-column label="客户名称" align="center" prop="clientName"/>
        <el-table-column label="跟进时间" align="center" prop="followUpTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.followUpTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="跟进详情描述" align="center" prop="followUpDescription"/>
      </el-table>

      <pagination v-show="followArrtotal > 0" :total="followArrtotal" :page.sync="followQueryParams.pageNum"
                  :limit.sync="followQueryParams.pageSize" @pagination="getFollowList"></pagination>
    </el-dialog>

    <el-dialog :visible.sync="followHistroyDes" title="新增跟进记录" width="35%" @closed="handleFollowClose">
      <el-form :model="fromFollowAdd" :rules="ruleFollowAdds" ref="followAddForm">
        <el-form-item label="跟进时间" prop="followHistroyTime">
          <el-date-picker v-model="fromFollowAdd.followHistroyTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="选择跟进时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="跟进记录描述" prop="followHistroyMsg">
          <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="fromFollowAdd.followHistroyMsg">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleFollowDesc">新增</el-button>
      </div>
    </el-dialog>

    <!-- 预览详情记录弹窗 -->
    <el-dialog :visible.sync="listDetailFlag" title="列表详情数据展示" width="50%" @closed="handleFollowClose">
      <el-form ref="form" :model="detailListForm" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="detailListForm.name" placeholder="请输入姓名" :disabled="showDetailFlag"/>
        </el-form-item>
        <el-form-item label="电话号码" prop="phone">
          <el-input v-model="detailListForm.phone" placeholder="请输入电话号码"/>
        </el-form-item>
        <el-form-item label="客户等级" prop="clientLevel">
          <el-select v-model="detailListForm.clientLevel" clearable placeholder="请选择客户等级">
            <el-option v-for="item in clientLevelArr" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="铺位查看权限" prop="shopResource">
          <el-select v-model="detailListForm.shopResource" clearable placeholder="请选择是否有铺位查看权限">
            <el-option v-for="item in shopResourceMapArr" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="跟进人员" prop="guideId">
          <el-select v-model="detailListForm.guideId" clearable placeholder="请选择跟进人员">
            <el-option v-for="item in followUpPerson" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间" prop="sTime">
          <el-date-picker clearable v-model="detailListForm.sTime" type="date" value-format="yyyy-MM-dd"
                          placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="群号" prop="groupNum">
          <el-input
            v-model="detailListForm.groupNum"
            placeholder="请输入群号"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="意向城市" prop="likeCity">
          <el-input
            v-model="detailListForm.likeCity"
            placeholder="请输入意向城市"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="预算" prop="budget">
          <el-input
            v-model="detailListForm.budget"
            placeholder="请输入预算"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="是否接受转让" prop="isTransfor" class="custom-label-line-height">
          <el-select
            v-model="detailListForm.isTransfor"
            clearable
            placeholder="请选择是否接受转让"
            style="width: 100%"
            :disabled="showDetailFlag">
            <el-option
              v-for="item in isTransforOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="后期打算" prop="plan">
          <el-input
            v-model="detailListForm.plan"
            placeholder="请输入后期打算"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="其他项目经验" prop="otherProject" class="custom-label-line-height">
          <el-input
            v-model="detailListForm.otherProject"
            placeholder="请输入其他项目经验"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="独资还是合伙" prop="single" class="custom-label-line-height">
          <el-select
            v-model="detailListForm.single"
            clearable
            placeholder="请选择独资还是合伙"
            style="width: 100%"
            :disabled="showDetailFlag"
          >
            <el-option
              v-for="item in singleOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否有开多店的打算" prop="ismore" class="custom-label-line-height">
          <el-select
            v-model="detailListForm.ismore"
            clearable
            placeholder="请选择是否有开多店的打算"
            style="width: 100%"
            :disabled="showDetailFlag"
          >
            <el-option
              v-for="item in ismoreOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="首次咨询时间" prop="firstTime" class="custom-label-line-height">
          <el-date-picker clearable
                          v-model="detailListForm.firstTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择首次咨询时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="成交时间" prop="dealTime">
          <el-date-picker clearable
                          v-model="detailListForm.dealTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择成交时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="到访次数" prop="comeTime">
          <el-input
            v-model="detailListForm.comeTime"
            placeholder="请输入到访次数"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="现在居住地" prop="nowHouse" class="custom-label-line-height">
          <el-input
            v-model="detailListForm.nowHouse"
            placeholder="请输入现在居住地"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="籍贯" prop="oldHouse">
          <el-input
            v-model="detailListForm.oldHouse"
            placeholder="请输入籍贯"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="如何看待有转让费或者其他原因" prop="otherThink" class="custom-label-line-height">
          <el-input
            v-model="detailListForm.otherThink"
            placeholder="请输入如何看待有转让费或者其他原因"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 调查问卷的弹窗 -->
    <el-dialog :visible.sync="questionListFlag" title="调查问卷数据展示列表" width="35%" @closed="handleFollowClose">
      <el-form ref="questionListForm" :model="questionListForm" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="questionListForm.name" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="电话号码" prop="phone">
          <el-input v-model="questionListForm.phone" placeholder="请输入电话号码"/>
        </el-form-item>
        <el-form-item label="居住地" prop="residence">
          <el-input v-model="questionListForm.residence" placeholder="请输入居住地"/>
        </el-form-item>
        <el-form-item label="经常光顾店" prop="frequentStore" class="question-label-line-height">
          <el-input v-model="questionListForm.frequentStore" placeholder="请输入经常光顾店"/>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="questionListForm.remarks" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="投资预算" prop="investmentBudget">
          <el-input v-model="questionListForm.investmentBudget" placeholder="请输入投资预算"/>
        </el-form-item>
        <el-form-item label="合作模式" prop="cooperationMode">
          <el-input v-model="questionListForm.cooperationMode" placeholder="请输入合作模式"/>
        </el-form-item>
        <el-form-item label="期望开店地址" prop="expectedStoreAddress" class="question-label-line-height">
          <el-input v-model="questionListForm.expectedStoreAddress" placeholder="请输入期望开店地址"/>
        </el-form-item>
        <el-form-item label="期望开店省份" prop="expectedProvince" class="question-label-line-height">
          <el-input v-model="questionListForm.expectedProvince" placeholder="请输入期望开店省份"/>
        </el-form-item>
        <el-form-item label="期望开店市" prop="expectedCity" class="question-label-line-height">
          <el-input v-model="questionListForm.expectedCity" placeholder="请输入期望开店市"/>
        </el-form-item>
        <el-form-item label="期望开店区" prop="expectedDistrict" class="question-label-line-height">
          <el-input v-model="questionListForm.expectedDistrict" placeholder="请输入期望开店区"/>
        </el-form-item>
        <el-form-item label="从事行业" prop="industry">
          <el-input v-model="questionListForm.industry" placeholder="请输入从事行业"/>
        </el-form-item>
        <el-form-item label="如何看待发展" prop="developmentOpinion" class="question-label-line-height">
          <el-input v-model="questionListForm.developmentOpinion" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="选择我的原因" prop="selectionReason" class="question-label-line-height">
          <el-input v-model="questionListForm.selectionReason" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="我们提升的建议" prop="improvementSuggestion" class="question-label-line-height">
          <el-input v-model="questionListForm.improvementSuggestion" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {
  listClient,
  getClient,
  delClient,
  addClient,
  updateClient,
  listUser,
  BatchSetProple,
  clientLevel,
  clientFollowData,
  getQuestionListData
} from "@/api/system/client"
import {listUp, getUp, delUp, addUp, updateUp} from "@/api/system/up"

export default {
  name: "Client",
  data() {
    return {
      // 是否接受转让 - 下拉选项
      isTransforOptions: [
        {value: 1, label: '是'},
        {value: 0, label: '否'}
      ],
      // 独资还是合伙 - 下拉选项
      singleOptions: [
        {value: 1, label: '独资'},
        {value: 2, label: '合伙'}
      ],
      // 是否有开多店的打算 - 下拉选项
      ismoreOptions: [
        {value: 1, label: '是'},
        {value: 0, label: '否'}
      ],
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
      // 客户信息表格数据
      clientList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      statusMap: {
        0: '潜在',
        2: '已邀约',
        3: '已面谈',
        4: '已签约',
        5: '已流失'
      },
      shopResourceMap: {
        0: '无权限',
        1: '有权限',
      },
      statusMapArr: [
        {value: 0, label: '潜在'},
        {value: 2, label: '已邀约'},
        {value: 3, label: '已面谈'},
        {value: 4, label: '已签约'},
        {value: 5, label: '已流失'}
      ],
      shopResourceMapArr: [
        {value: 0, label: '无权限'},
        {value: 1, label: '有权限'}
      ],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        phone: null,
        userId: null,
        clientLevel: null,
        clientStatus: null,
        guideId: null,
        sTime: null,
        upTime: null,
        groupNum: null,
        likeCity: null,
        budget: null,
        isTransfor: null,
        plan: null,
        otherProject: null,
        single: null,
        ismore: null,
        firstTime: null,
        dealTime: null,
        comeTime: null,
        nowHouse: null,
        oldHouse: null,
        otherThink: null
      },
      questionListFlag: false, // 调查问卷展示标识
      questionListForm: {}, //调查问卷数据
      listDetailFlag: false, //预览弹窗打开关闭标识

      showDetailFlag: false,
      detailListForm: {}, // 预览详情参数
      clientLevelArr: [], //客户等级数据
      followUpPerson: [],
      followPersonNameTwo: "",//批量设置里面的跟进人员
      followPersonName: "", //查询的跟进人员
      followTitle: "",//批量设置人员弹窗标题
      followFlag: false,//批量设置人员弹窗关闭标识
      followArrForSet: {},//批量设置入参
      dialogVisible: false, //跟进记录弹窗关闭标识
      followHistroyDes: false, //新增跟进记录弹窗标识
      fromFollowAdd: {
        followHistroyTime: new Date(), // 跟进记录时间
        followHistroyMsg: ""   // 跟进记录描述内容
      },
      // 表单参数
      form: {},
      // 跟进人员表单
      followForm: {},
      // 跟进记录表单
      upList: [],
      // 跟进记录专用分页参数
      followQueryParams: {
        pageNum: 1,
        pageSize: 10,
        clientId: ""
      },
      followArrtotal: 0, // 跟进记录总数
      // 跟进人员选中的数据
      upListSelected: {},
      // 表单校验
      rules: {
        name: [
          {required: true, message: "姓名不能为空", trigger: "blur"}
        ]
      },
      ruleFollowAdds: {
        followHistroyTime: [
          {required: true, message: '请选择跟进时间', trigger: 'change'}
        ],
        followHistroyMsg: [
          {required: true, message: '请输入跟进记录描述', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.getList();
    this.getFollwPerson();
    this.getClientLevel();
  },
  mounted() {
    // 确保表格渲染完成后刷新布局
    this.$nextTick(() => {
      this.refreshTableLayout();
    });
  },
  methods: {
    // 刷新表格布局
    refreshTableLayout() {
      this.$nextTick(() => {
        if (this.$refs.multipleTable) {
          this.$refs.multipleTable.doLayout();
        }
      });
    },

    formatIsTransfor(row) {
      const statusMap = {1: '是', 0: '否'};
      return statusMap[row.isTransfor] || '未设置';
    },

    // 格式化「独资还是合伙」
    formatSingle(row) {
      const typeMap = {1: '独资', 2: '合伙'};
      return typeMap[row.single] || '未设置';
    },

    // 格式化「是否有开多店的打算」
    formatIsmore(row) {
      const planMap = {1: '是', 0: '否'};
      return planMap[row.ismore] || '未设置';
    },
    /** 查询客户信息列表 */
    getList() {
      this.loading = true
      listClient(this.queryParams).then(response => {
        this.clientList = response.rows
        this.total = response.total
        this.loading = false
        this.refreshTableLayout(); // 数据加载后刷新布局
      })
    },
    // 获取跟进人员信息
    getFollwPerson() {
      listUser(this.queryParams).then(response => {
        this.followUpPerson = [];
        if (response && response.rows && response.rows.length > 0) {
          response.rows.forEach((item) => {
            this.followUpPerson.push({
              value: item.userId,
              label: item.nickName
            });
          });
        }
      });
    },
    // 获取客户等级数据
    getClientLevel() {
      this.queryParams.dictType = "clientlevel"
      clientLevel(this.queryParams).then(response => {
        this.clientLevelArr = [];
        if (response && response.rows && response.rows.length > 0) {
          response.rows.forEach((item) => {
            this.clientLevelArr.push({
              value: item.dictValue,
              label: item.dictLabel
            });
          });
        }
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
        userId: null,
        clientLevel: null,
        clientStatus: null,
        guideId: null,
        sTime: null,
        upTime: null
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
      this.reset()
      this.open = true
      this.title = "添加客户信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.showDetailFlag = false;
      this.reset()
      const id = row.id || this.ids
      getClient(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改客户信息"
      })
    },
    //查看详情数据
    handleReadDetail(row) {
      this.showDetailFlag = true;
      this.listDetailFlag = true;
      this.detailListForm = row;
    },

    handQustionary(row) {
      //获取 调查问卷
      let tempData = {
        clientid: row.id
      }
      getQuestionListData(tempData).then(res => {
        this.questionListFlag = true;
        this.questionListForm = res.data;
      })
    },
    //跟进按钮操作
    handleSetFollow(row) {
      this.upListSelected = row;
      this.dialogVisible = true;
      this.getFollowList(row);
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateClient(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addClient(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除客户信息编号为"' + ids + '"的数据项？').then(function () {
        return delClient(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/client/export', {
        ...this.queryParams
      }, `client_${new Date().getTime()}.xlsx`)
    },
    // 客户等级格式化函数
    formatClientLevel(row) {
      const levelMap = {
        1: 'A',
        2: 'B',
        3: 'C',
        4: 'D'
      };
      return levelMap[row.clientLevel] || '未知等级';
    },
    // 客户状态格式化函数
    formatStatus(row) {
      return this.statusMap[row.clientStatus];
    },
    formatShopResource(row) {
      return this.shopResourceMap[row.shopResource];
    },
    //批量设置跟进人员方法
    handleSetting() {
      this.followFlag = true;
      this.followTitle = "批量设置跟进人员"
    },
    //批量设置跟进人员---确认操作
    submitFollowForm() {
      this.followFlag = false;
      const selectedRows = this.$refs.multipleTable.selection;
      const followArrForSet = {
        "follower": this.followPersonNameTwo,
        "clientIds": selectedRows.map(item => item.id)
      }
      BatchSetProple(followArrForSet).then(() => {
        this.$modal.msgSuccess("设置成功")
        this.getList();
      })
    },
    //批量设置跟进人员----取消操作
    followCancel() {
      this.followFlag = false;
    },
    /** 查询客户跟进记录列表 */
    getFollowList(row) {
      this.loading = true
      if (row && row.id) this.followQueryParams.clientId = row.id;
      listUp(this.followQueryParams).then(response => {
        this.upList = response.rows
        this.followArrtotal = response.total
        this.loading = false
      })
    },
    // 新增跟进人员信息
    handleFollowHistroy(row) {
      this.followHistroyDes = true
      this.fromFollowAdd = {
        followHistroyTime: new Date(),
        followHistroyMsg: ""
      }
    },
    //调用新增跟进记录接口
    handleFollowDesc() {
      this.$refs["followAddForm"].validate(valid => {
        if (valid) {
          const fromFollowAddParams = {
            clientId: this.upListSelected.id,
            followUpTime: this.fromFollowAdd.followHistroyTime,
            followUpDescription: this.fromFollowAdd.followHistroyMsg
          };
          clientFollowData(fromFollowAddParams).then(res => {
            this.followHistroyDes = false;
            this.getFollowList({id: res.clientId});
          })
        }
      })
    },
    // 关闭新增跟进记录弹窗
    handleFollowClose() {
      this.$refs.followAddForm?.resetFields();
      this.fromFollowAdd = {
        followHistroyTime: new Date(),
        followHistroyMsg: ""
      }
    }
  }
}
</script>

<style scoped>
/* 修复列宽拖动条显示 */
::v-deep .el-table th.gutter {
  display: table-cell !important;
  width: 8px !important;
  pointer-events: auto !important;
  cursor: col-resize !important;
}

::v-deep .el-table colgroup col[name="gutter"] {
  display: table-cell !important;
  width: 8px !important;
}

.modal-header {
  margin: 20px;
}

::v-deep .custom-label-line-height .el-form-item__label {
  line-height: 15px;
}

::v-deep .custom-table-header .el-table__header th {
  line-height: 15px;
  height: 20px;
}

::v-deep .custom-table-header .cell {
  line-height: 15px;
}

::v-deep .custom-table-header .el-table__body tr,
::v-deep .custom-table-header .el-table__body td {
  height: 35px;
}

::v-deep .question-label-line-height .el-form-item__label {
  line-height: 15px;
}
</style>
