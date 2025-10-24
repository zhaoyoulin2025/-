    <template>
      <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
          <el-form-item label="设计人员" prop="designerId">
            <el-select v-model="queryParams.designerId" clearable placeholder="请选择设计人员">
              <el-option v-for="item in followUpPerson" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="设计状态" prop="designStatus">
            <el-select v-model="queryParams.designStatus" placeholder="请选择设计状态">
              <el-option label="待分配" :value="0"/>
              <el-option label="待测量" :value="1"/>
              <el-option label="平面-设计中" :value="2"/>
              <el-option label="平面-审核中" :value="3"/>
              <el-option label="平面-驳回" :value="4"/>
              <el-option label="效果-设计中" :value="5"/>
              <el-option label="效果-审核中" :value="6"/>
              <el-option label="效果-驳回" :value="7"/>
              <el-option label="施工-设计中" :value="8"/>
              <el-option label="施工-审核中" :value="9"/>
              <el-option label="施工-驳回" :value="10"/>
              <el-option label="完成" :value="11"/>
            </el-select>
          </el-form-item>
          <el-form-item label="设计类型" prop="designType">
            <el-select v-model="queryParams.designType" placeholder="请选择设计类型">
              <el-option label="平面设计" :value="1"/>
              <el-option label="效果设计" :value="2"/>
              <el-option label="施工设计" :value="3"/>
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
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['system:design:edit']"
            >点击查看详情
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="assignasks"
              v-hasPermi="['system:design:edit']"
            >分配任务
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['system:design:remove']"
            >删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['system:design:export']"
            >导出
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-s-operation" size="mini" :disabled="multiple" @click="toMeasure"
                       v-hasPermi="['system:build:remove']">转营建</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="designList" ref="designTable" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column label="主键ID" align="center" prop="id"/>
          <el-table-column label="店铺名称" align="center" prop="shopName"/>
          <el-table-column label="客户姓名" align="center" prop="clientName"/>
          <el-table-column label="设计阶段" align="center" prop="designType" :formatter="formatShopType"/>
          <el-table-column label="设计状态" align="center" prop="designStatus" :formatter="formatShopStatus"/>
          <el-table-column label="责任人" align="center" prop="current_designer_name"/>
          <el-table-column label="创建时间" align="center" prop="sTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.sTime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:design:edit']"
              >设计详情
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:design:remove']"
              >删除
              </el-button>
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

        <!-- 分配任务弹窗 -->
        <el-dialog :title="followTitle" :visible.sync="followFlag" width="500px" append-to-body>
          <el-form ref="followForm" :model="followForm" :rules="rules" label-width="80px">
            <el-form-item label="测量人" prop="measurePeople">
              <el-select v-model="followForm.measurePeople" clearable placeholder="请选择">
                <el-option v-for="item in followUpPerson" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="平面图设计师" prop="dimensional">
              <el-select v-model="followForm.dimensional" clearable placeholder="请选择">
                <el-option v-for="item in followUpPerson" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="效果图设计师" prop="visualization">
              <el-select v-model="followForm.visualization" clearable placeholder="请选择">
                <el-option v-for="item in followUpPerson" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="施工图设计师" prop="construction">
              <el-select v-model="followForm.construction" clearable placeholder="请选择">
                <el-option v-for="item in followUpPerson" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="handleFollowConfirm">确认并启动项目</el-button>
            <el-button @click="followCancel">取 消</el-button>
          </div>
        </el-dialog>

        <!-- 设计详情弹窗 -->
        <!-- 设计详情弹窗 -->
        <el-dialog
          :title="detailTitle"
          :visible.sync="detailDialogVisible"
          width="900px"
          append-to-body
          :close-on-click-modal="false"
          class="design-detail-dialog"
        >
          <!-- 加载状态 -->
          <div v-if="detailLoading" class="detail-loading">
            <el-loading-spinner class="el-icon-loading"></el-loading-spinner>
            <p class="loading-text">加载详情中...</p>
          </div>

          <!-- 详情内容 -->
          <div class="detail-content" v-else-if="detailData">
            <!-- 1. 基础信息区域（核心优化：标签对齐+值对齐） -->
            <div class="base-info-card">
              <h3 class="section-title">基础信息</h3>
              <el-row :gutter="24" class="info-row-wrap">
                <!-- 设计ID -->
                <el-col :span="8" class="info-item">
                  <div class="info-label-wrap">
                    <span class="info-label">设计ID：</span>
                  </div>
                  <div class="info-value-wrap">
                    <span class="info-value">{{ detailData.id || '-' }}</span>
                  </div>
                </el-col>
                <!-- 店铺名称 -->
                <el-col :span="8" class="info-item">
                  <div class="info-label-wrap">
                    <span class="info-label">店铺名称：</span>
                  </div>
                  <div class="info-value-wrap">
                    <span class="info-value">{{ detailData.shopName || '-' }}</span>
                  </div>
                </el-col>
                <!-- 客户姓名 -->
                <el-col :span="8" class="info-item">
                  <div class="info-label-wrap">
                    <span class="info-label">客户姓名：</span>
                  </div>
                  <div class="info-value-wrap">
                    <span class="info-value">{{ detailData.clientName || '-' }}</span>
                  </div>
                </el-col>
                <!-- 设计类型 -->
                <el-col :span="8" class="info-item">
                  <div class="info-label-wrap">
                    <span class="info-label">设计类型：</span>
                  </div>
                  <div class="info-value-wrap">
                    <span class="info-value">{{ formatShopType(detailData) || '-' }}</span>
                  </div>
                </el-col>
                <!-- 当前状态 -->
                <el-col :span="8" class="info-item">
                  <div class="info-label-wrap">
                    <span class="info-label">当前状态：</span>
                  </div>
                  <div class="info-value-wrap">
                    <el-tag
                      :type="getStatusTagType(detailData.designStatus)"
                      size="mini"
                      class="status-tag"
                    >
                      {{ formatShopStatus(detailData) || '-' }}
                    </el-tag>
                  </div>
                </el-col>
                <!-- 当前责任人 -->
                <el-col :span="8" class="info-item">
                  <div class="info-label-wrap">
                    <span class="info-label">责任人：</span>
                  </div>
                  <div class="info-value-wrap">
                    <span class="info-value">{{ detailData.current_designer_name || '-' }}</span>
                  </div>
                </el-col>
                <!-- 创建时间 -->
                <el-col :span="8" class="info-item">
                  <div class="info-label-wrap">
                    <span class="info-label">创建时间：</span>
                  </div>
                  <div class="info-value-wrap">
                    <span class="info-value">{{ parseTime(detailData.sTime, '{y}-{m}-{d} {h}:{i}') || '-' }}</span>
                  </div>
                </el-col>
                <!-- 阶段停留时间 -->
                <el-col :span="8" class="info-item warning-item">
                  <div class="info-label-wrap">
                    <span class="info-label">阶段停留：</span>
                  </div>
                  <div class="info-value-wrap">
                <span class="info-value" :style="detailData.stageWarn ? 'color:#f56c6c;font-weight:500' : ''">
                  {{ detailData.stageStayTime || 0 }} 小时
                  <el-tag v-if="detailData.stageWarn" type="danger" size="mini" class="warn-tag">预警</el-tag>
                </span>
                  </div>
                </el-col>
                <!-- 更新时间 -->
                <el-col :span="8" class="info-item">
                  <div class="info-label-wrap">
                    <span class="info-label">更新时间：</span>
                  </div>
                  <div class="info-value-wrap">
                    <span class="info-value">{{ parseTime(detailData.upTime, '{y}-{m}-{d} {h}:{i}') || '-' }}</span>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 2. 左右分栏：文件资源 + 流程推进（保持原有功能，样式微调） -->
            <el-row :gutter="24" class="detail-main-layout mt-20">
              <!-- 左侧：文件资源中心 -->
              <el-col :span="10">
                <div class="file-resource-card">
                  <h3 class="section-title">文件资源中心</h3>

                  <!-- 测量文件卡片 -->
                  <div class="file-card measure-file-card">
                    <div class="file-card-header">
                      <i class="el-icon-picture-outline file-card-icon"></i>
                      <span class="file-card-title">测量文件</span>
                    </div>
                    <div class="file-card-body">
                      <div class="file-preview-list" v-if="detailData.measureFiles && detailData.measureFiles.length">
                        <div
                          v-for="(file, idx) in detailData.measureFiles"
                          :key="idx"
                          class="file-preview-item"
                          @click="handlePicturePreview(Object.values(file)[0])"
                        >
                          <el-image
                            :src="Object.values(file)[0]"
                            :preview-src-list="[Object.values(file)[0]]"
                            class="file-preview-img"
                          >
                            <template #error>
                              <i class="el-icon-picture-outline file-error-icon"></i>
                            </template>
                          </el-image>
                          <div class="file-name">{{ Object.keys(file)[0] || '未知文件名' }}</div>
                        </div>
                      </div>
                      <div class="no-file-tip" v-else>暂无测量文件</div>

                      <el-upload
                        v-if="isCurrentDesigner && detailData.designStatus === 1"
                        action="/manage/common/upload"
                        :file-list="measureFileList"
                        :on-success="(res) => handleFileUpload(res, 'measure')"
                        accept=".pdf,.jpg,.png"
                        :limit="1"
                        class="file-upload-btn mt-10"
                      >
                        <el-button size="mini" type="primary" icon="el-icon-upload">上传测量文件</el-button>
                      </el-upload>
                    </div>
                  </div>

                  <!-- 平面文件卡片 -->
                  <div class="file-card floor-file-card mt-16">
                    <div class="file-card-header">
                      <i class="el-icon-file-pdf file-card-icon"></i>
                      <span class="file-card-title">平面图文件</span>
                    </div>
                    <div class="file-card-body">
                      <div v-if="detailData.floorFiles && detailData.floorFiles.length">
                        <el-link
                          v-for="(file, idx) in detailData.floorFiles"
                          :key="file.name"
                          :href="file.url"
                          target="_blank"
                          class="file-link-item"
                        >
                          <i class="el-icon-file-pdf file-link-icon"></i>
                          <span class="file-link-name">{{ file.name }}</span>
                        </el-link>
                      </div>
                      <div class="no-file-tip" v-else>暂无平面图文件</div>

                      <el-upload
                        v-if="isCurrentDesigner && detailData.designStatus === 2"
                        action="/manage/common/upload"
                        :file-list="floorFileList"
                        :on-success="(res) => handleFileUpload(res, 'floor')"
                        accept=".pdf,.jpg,.png"
                        :limit="1"
                        class="file-upload-btn mt-10"
                        list-type="picture-card"
                      >
                        <el-button size="mini" type="primary" icon="el-icon-upload">上传平面图</el-button>
                      </el-upload>
                    </div>
                  </div>

                  <!-- 效果文件卡片 -->
                  <div class="file-card effect-file-card mt-16">
                    <div class="file-card-header">
                      <i class="el-icon-file-pdf file-card-icon"></i>
                      <span class="file-card-title">效果图文件</span>
                    </div>
                    <div class="file-card-body">
                      <div v-if="detailData.effectFiles && detailData.effectFiles.length">
                        <el-link
                          v-for="(file, idx) in detailData.effectFiles"
                          :key="file.name"
                          :href="file.url"
                          target="_blank"
                          class="file-link-item"
                        >
                          <i class="el-icon-file-pdf file-link-icon"></i>
                          <span class="file-link-name">{{ file.name }}</span>
                        </el-link>
                      </div>
                      <div class="no-file-tip" v-else>暂无效果图文件</div>

                      <el-upload
                        v-if="isCurrentDesigner && detailData.designStatus === 5"
                        action="/manage/common/upload"
                        :file-list="effectFileList"
                        :on-success="(res) => handleFileUpload(res, 'effect')"
                        accept=".pdf"
                        :limit="1"
                        class="file-upload-btn mt-10"
                      >
                        <el-button size="mini" type="primary" icon="el-icon-upload">上传效果图</el-button>
                      </el-upload>
                    </div>
                  </div>

                  <!-- 施工文件卡片 -->
                  <div class="file-card construction-file-card mt-16">
                    <div class="file-card-header">
                      <i class="el-icon-file-pdf file-card-icon"></i>
                      <span class="file-card-title">施工图文件</span>
                    </div>
                    <div class="file-card-body">
                      <div v-if="detailData.constructionFiles && detailData.constructionFiles.length">
                        <el-link
                          v-for="(file, idx) in detailData.constructionFiles"
                          :key="file.name"
                          :href="file.url"
                          target="_blank"
                          class="file-link-item"
                        >
                          <i class="el-icon-file-pdf file-link-icon"></i>
                          <span class="file-link-name">{{ file.name }}</span>
                        </el-link>
                      </div>
                      <div class="no-file-tip" v-else>暂无施工图文件</div>

                      <el-upload
                        v-if="isCurrentDesigner && detailData.designStatus === 8"
                        action="/manage/common/upload"
                        :file-list="constructionFileList"
                        :on-success="(res) => handleFileUpload(res, 'construction')"
                        accept=".pdf"
                        :limit="1"
                        class="file-upload-btn mt-10"
                      >
                        <el-button size="mini" type="primary" icon="el-icon-upload">上传施工图</el-button>
                      </el-upload>
                    </div>
                  </div>
                </div>
              </el-col>

              <!-- 右侧：流程推进 -->
              <!-- 右侧：流程推进 -->
              <el-col :span="14">
                <div class="process-progress-card">
                  <h3 class="section-title">流程推进</h3>

                  <!-- 1. 提交按钮：移到 v-if 外面，确保始终渲染 -->
                  <div class="process-operation mt-16">
                    <el-button
                      type="primary"
                      size="mini"
                      @click="submitStepAudit()"
                      class="submit-audit-btn"
                      v-if="this.isCurrentDesigner&&isCurrentStep()"
                    >
                    完成并提交审核
                    </el-button>
                  </div>

                  <!-- 2. 流程步骤：仅 processRecords 有数据时显示 -->
                  <div v-if="detailData.processRecords && detailData.processRecords.length">
                    <div class="process-steps">
                      <div
                        class="process-step"
                        v-for="(record, idx) in detailData.processRecords"
                        :key="record.id || idx"
                      >
                        <!-- 流程步骤内容（不变）... -->
                        <div class="step-icon-container">
                          <i :class="getStepIcon(record.statusDesc)" :style="getStepIconStyle(record.statusDesc)" class="step-icon"></i>
                          <div class="step-line" v-if="idx < detailData.processRecords.length - 1"></div>
                        </div>
                        <div class="step-content-wrap">
                          <div class="step-stage">{{ record.stageName || '未知阶段' }}</div>
                          <div class="step-status" :style="getStatusTextStyle(record.statusDesc)">{{ record.statusDesc }}</div>
                          <div class="step-meta">
                            <span v-if="record.operatorName" class="step-operator">操作人：{{ record.operatorName }}</span>
                            <span v-if="record.operateTime" class="step-time">{{ parseTime(record.operateTime, '{y}-{m}-{d} {h}:{i}') }}</span>
                          </div>
                          <div class="step-audit-feedback" v-if="record.auditFeedback">
                            <span class="feedback-label">审核意见：</span>
                            <span class="feedback-content">{{ record.auditFeedback }}</span>
                          </div>
                        </div>
                      </div>
                    </div>

                    <!-- 3. 审核按钮：仅 processRecords 有数据时显示 -->
                    <div class="process-operation mt-8">
                      <div class="audit-buttons" v-if="detailData.auditor && isShowAuditButtons()">
                        <el-button
                          type="success"
                          size="mini"
                          @click="handleAuditPass(getLastRecordId())"
                          class="audit-pass-btn"
                        >
                          审核通过
                        </el-button>
                        <el-button
                          type="danger"
                          size="mini"
                          @click="handleAuditReject(getLastRecordId())"
                          class="audit-reject-btn ml-8"
                        >
                          审核驳回
                        </el-button>
                      </div>
                    </div>
                  </div>

                  <!-- 4. 无流程记录提示 -->
                  <div class="no-process-tip" v-else>
                    {{ isCurrentDesigner ? '当前阶段暂无流程记录，等待文件上传后提交审核' : '当前阶段暂无流程记录' }}
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>

          <div slot="footer" class="dialog-footer">
            <el-button @click="detailDialogVisible = false" class="cancel-btn">关闭</el-button>
            <el-button type="primary" @click="handleRefreshDetail()" class="refresh-btn ml-8">刷新详情</el-button>
          </div>
        </el-dialog>

        <el-dialog :visible.sync="filePreviewVisible" :title="filePreviewTitle" width="800px">
          <div class="file-preview-container">
            <iframe :src="filePreviewUrl" width="100%" height="600px" v-if="filePreviewUrl.includes('.pdf')"></iframe>
            <img :src="filePreviewUrl" width="100%" v-else-if="filePreviewUrl.match(/\.(jpg|png|jpeg)$/i)">
            <div class="no-preview-tip" v-else>不支持该格式文件预览</div>
          </div>
        </el-dialog>
      </div>
    </template>

    <script>
    import { listDesign, getDesign, delDesign, updateDesign, assignDesignTask, addDesignFlowRecord, submitDesignAudit } from "@/api/system/design"
    import { listMeasure, addMeasure } from '@/api/system/measure'
    import { toBuild } from '@/api/system/build'
    import { listClient, listUser } from "@/api/system/client";
    import { listShop } from '@/api/system/shop'

    export default {
      name: "Design",
      data() {
        return {



          tempFileInfo: {
            measure: null,   // 测量文件
            floor: null,     // 平面文件
            effect: null,    // 效果文件
            construction: null // 施工文件
          },


          filePreviewVisible: false,
          filePreviewTitle: "文件预览",
          filePreviewUrl: "",
          // 详情弹窗相关
          detailDialogVisible: false,
          detailTitle: "设计详情",
          detailData: {},
          detailLoading: false,
          // 文件上传列表（对应各阶段）
          measureFileList: [],
          floorFileList: [],
          effectFileList: [],
          constructionFileList: [],
          // 是否为当前责任人（通过后端del字段判断）
          isCurrentDesigner: false,

          // 分配任务弹窗相关
          followFlag: false,
          followTitle: "",
          followForm: {
            designId: "",
            measurePeople: "",
            dimensional: "",
            visualization: "",
            construction: ""
          },

          // 基础列表相关
          loading: true,
          ids: [],
          single: true,
          multiple: true,
          showSearch: true,
          total: 0,
          designList: [],
          // 查询参数
          queryParams: {
            pageNum: 1,
            pageSize: 10,
            shopId: null,
            clientId: null,
            designStatus: null,
            designType: null,
            designerId: null,
            sTime: null,
            upTime: null
          },

          // 下拉选项数据
          followUpPerson: [], // 设计人员列表
          clientPerson: [],   // 客户列表
          shopList: [],       // 店铺列表

          // 表单校验规则
          rules: {
            measurePeople: [
              { required: true, message: '请选择测量人', trigger: 'change' }
            ],
            dimensional: [
              { required: true, message: '请选择平面图设计师', trigger: 'change' }
            ],
            visualization: [
              { required: true, message: '请选择效果图设计师', trigger: 'change' }
            ],
            construction: [
              { required: true, message: '请选择施工图设计师', trigger: 'change' }
            ]
          }
        }
      },
      created() {
        this.getList();             // 加载设计列表
        this.getBaseSelectData();   // 加载下拉选项数据
      },
      methods: {

        getCurrentStage() {
          // current_design_stage 是后端返回的当前阶段标识（1=测量、2=平面、3=效果、4=施工）
          return this.detailData.current_design_stage;
        },
        // 判断是否显示审核按钮（保持不变）
        isShowAuditButtons() {
          if (!this.detailData.auditor) return false;
          if (!this.detailData.processRecords || this.detailData.processRecords.length === 0) {
            return false;
          }
          const lastRecord = this.detailData.processRecords[this.detailData.processRecords.length - 1];
          const result = lastRecord.auditId === null;
          console.log("isShowAuditButtons最终结果:", result);

          console.log(this.detailData.auditor&& result)
          return result;
        },

        getLastRecordId() {
          if (!this.detailData.processRecords || this.detailData.processRecords.length === 0) {
            return null;
          }
          return this.detailData.processRecords[this.detailData.processRecords.length - 1].id;
        },

        /**
         * 修正：判断是否为可提交状态（只在1、2、5、8设计状态显示）
         */
        isSubmitAbleStatus() {
          const submitAbleStatus = [1, 2, 5, 8]; // 待测量、平面设计中、效果设计中、施工设计中
          return submitAbleStatus.includes(this.detailData.designStatus);
        },

        /**
         * 判断是否为当前阶段（用于显示提交按钮）
         */
        isCurrentStep() {

          console.log("会输出吗", this.isCurrentDesigner);  // 这里一定会正确输出
          if(!this.isSubmitAbleStatus()){
            return false;
          }
          if (!this.detailData.processRecords || this.detailData.processRecords.length === 0) {
            return true;
          }

          // 条件2：最后一条记录的auditStatus不为null
          const lastRecord = this.detailData.processRecords[this.detailData.processRecords.length - 1];
          return lastRecord.auditId !== null;
        },



        handlePicturePreview(url) {
          this.filePreviewTitle = "测量图预览";
          this.filePreviewUrl = url;
          this.filePreviewVisible = true;  // 显示预览弹窗
        },
        /**
         * 提交审核（设计师操作）
         */
        submitStepAudit() {
          console.log("123")

          this.$confirm(`确定提交审核吗？`, "确认提交", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(


            () => {
            console.log("345")
            const stage = this.getCurrentStage();
            console.log(stage)

            // 1. 获取当前阶段对应的暂存文件信息
            const currentFileType = this.getFileTypeByStage(stage);
            const fileInfo = this.tempFileInfo[currentFileType];

            // 2. 构造文件记录参数
            const flowDetail = {
              designId: this.detailData.id,
              fileType: this.getCurrentStage(),
              fileName: fileInfo?.fileName || "",
              fileUrl: fileInfo?.fileUrl || "",
              flowStage: stage,
              operateTime: new Date().getTime()
            };

            // 3. 保存文件记录并提交审核
            addDesignFlowRecord(flowDetail).then(res => {
              if (res.code !== 200) {
                this.$modal.msgError("保存文件记录失败：" + res.msg);
                return;
              }

              // // 调用提交审核接口生成待审核记录
              // submitDesignAudit({
              //   designId: this.detailData.id,
              //   currentStage: stage
              // }).then(submitRes => {
              //   if (submitRes.code === 200) {
              //     this.$message.success("提交审核成功，等待审核");
              //     this.handleRefreshDetail();
              //   } else {
              //     this.$modal.msgError("提交审核失败：" + submitRes.msg);
              //   }
              // });
            }).catch(fileErr => {
              console.error("保存文件记录接口失败：", fileErr);
              this.$modal.msgError("系统异常，保存文件记录失败");
            });
          }).catch(() => {console.log("error")});
        },

        /**
         * 根据阶段获取对应的文件类型
         */
        getFileTypeByStage(stage) {
          const stageFileTypeMap = {
            1: "measure",    // 阶段1（测量）→ 文件类型measure
            2: "floor",      // 阶段2（平面）→ 文件类型floor
            3: "effect",     // 阶段3（效果）→ 文件类型effect
            4: "construction"// 阶段4（施工）→ 文件类型construction
          };
          return stageFileTypeMap[stage] || "unknown";
        },

        /**
         * 文件上传成功处理
         */
        handleFileUpload(response, fileType) {
          if (response.code !== 200) {
            this.$message.error("文件上传失败：" + (response.msg || "未知错误"));
            return;
          }

          const fileInfo = {
            fileName: response.newFileName,
            fileUrl: response.url,
            flowStage: this.detailData.current_design_stage
          };

          this.tempFileInfo[fileType] = fileInfo;
          if (fileType === 'measure') {
            const measureFile = { [fileInfo.fileName]: fileInfo.fileUrl };
            this.detailData.measureFiles = this.detailData.measureFiles || [];
            this.detailData.measureFiles.push(measureFile);

            // 新增：更新上传组件的file-list，确保即时显示缩略图
            this.measureFileList = [{
              name: fileInfo.fileName,
              url: fileInfo.fileUrl
            }];
          }

          this.$message.success("文件上传成功，可提交审核");
        },

        /**
         * 查看详情
         */
        handleUpdate(row) {
          if (!row || !row.id) {
            this.$modal.msgWarning("请选择有效设计数据");
            return;
          }
          this.detailDialogVisible = true;
          this.detailTitle = `设计详情 - ${row.shopName || '未知店铺'}`;
          this.detailLoading = true;

          getDesign(row.id).then(res => {
            this.detailData = { ...res.data };
            // 初始化空列表避免undefined
            this.detailData.measureFiles = this.detailData.measureFiles || [];
            this.detailData.floorFiles = this.detailData.floorFiles || [];
            this.detailData.effectFiles = this.detailData.effectFiles || [];
            this.detailData.constructionFiles = this.detailData.constructionFiles || [];
            this.detailData.processRecords = this.detailData.processRecords || [];

            // 通过后端返回的del字段判断是否为当前责任人
            this.isCurrentDesigner = this.detailData.del || false;
            console.log("detailData:", this.detailData);  // 这里一定会正确输出

            console.log("this.isCurrentDesigner", this.isCurrentDesigner);  // 这里一定会正确输出
            this.detailLoading = false;
          }).catch(err => {
            this.$modal.msgError("获取详情失败，请重试");
            this.detailDialogVisible = false;
            this.detailLoading = false;
          });
        },

        /**
         * 刷新详情
         */
        handleRefreshDetail() {
          if (!this.detailData.id) return;
          this.detailLoading = true;
          getDesign(this.detailData.id).then(res => {
            this.detailData = { ...res.data };
            this.isCurrentDesigner = this.detailData.del || false;
            this.detailLoading = false;
            this.$message.success("详情已刷新");
          }).catch(err => {
            this.$modal.msgError("刷新详情失败");
            this.detailLoading = false;
          });
        },

        /**
         * 审核通过（审核员操作）
         */
        handleAuditPass(recordId) {
          this.$confirm("确定通过该审核吗？", "审核确认", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "success"
          }).then(() => {
            const auditData = {
              recordId: recordId,
              auditResult: 1, // 1=通过
              auditOpinion: "审核通过"
            };
            this.callAuditApi(auditData);
          }).catch(() => {});
        },

        /**
         * 审核驳回（审核员操作）
         */
        handleAuditReject(recordId) {
          this.$prompt("请输入驳回原因", "审核驳回", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            required: true
          }).then(({ value }) => {
            const auditData = {
              recordId: recordId,
              auditResult: 2, // 2=驳回
              auditOpinion: value
            };
            this.callAuditApi(auditData);
          }).catch(() => {});
        },

        /**
         * 统一调用审核接口
         */
        callAuditApi(auditData) {
          submitDesignAudit(auditData).then(res => {
            if (res.code === 200) {
              this.$modal.msgSuccess("审核操作成功");
              this.handleRefreshDetail();
            } else {
              this.$modal.msgError("审核失败：" + res.msg);
            }
          }).catch(err => {
            console.error("审核接口调用失败：", err);
            this.$modal.msgError("系统异常，审核失败");
          });
        },



        /**
         * 格式化阶段名称
         */
        formatStageName(stage) {
          const stageMap = { 1: "测量阶段", 2: "平面设计阶段", 3: "效果设计阶段", 4: "施工设计阶段" };
          return stageMap[stage] || "未知阶段";
        },

        /**
         * 获取流程步骤图标
         */
        getStepIcon(statusDesc) {
          if (statusDesc.includes("审核通过") || statusDesc.includes("已完成")) return "el-icon-check-circle";
          if (statusDesc.includes("待审核") || statusDesc.includes("进行中")) return "el-icon-arrow-right";
          return "el-icon-circle-outline";
        },

        /**
         * 获取流程步骤图标样式
         */
        getStepIconStyle(statusDesc) {
          if (statusDesc.includes("审核通过") || statusDesc.includes("已完成")) return "color: #67c23a;";
          if (statusDesc.includes("待审核") || statusDesc.includes("进行中")) return "color: #409eff;";
          return "color: #ccc;";
        },

        /**
         * 获取状态文本样式
         */
        getStatusTextStyle(statusDesc) {
          if (statusDesc.includes("驳回")) return "color: #f56c6c;";
          if (statusDesc.includes("审核通过")) return "color: #67c23a;";
          if (statusDesc.includes("待审核")) return "color: #e6a23c;";
          return "color: #333;";
        },

        /**
         * 获取状态标签类型（基础信息用）
         */
        getStatusTagType(status) {
          const tagMap = {
            0: "info",    // 待分配
            1: "primary", // 待测量
            2: "primary", // 平面-设计中
            3: "warning", // 平面-审核中
            4: "danger",  // 平面-驳回
            5: "primary", // 效果-设计中
            6: "warning", // 效果-审核中
            7: "danger",  // 效果-驳回
            8: "primary", // 施工-设计中
            9: "warning", // 施工-审核中
            10: "danger", // 施工-驳回
            11: "success" // 完成
          };
          return tagMap[status] || "info";
        },

        /**
         * 分配任务确认
         */
        handleFollowConfirm() {
          this.$refs["followForm"].validate(valid => {
            if (!valid) return;
            assignDesignTask(this.followForm).then(response => {
              this.$modal.msgSuccess("任务分配成功，已启动项目");
              this.followFlag = false;
              this.getList();
              this.followReset();
            }).catch(error => {
              this.$modal.msgError("任务分配失败，请重试");
            });
          });
        },

        /**
         * 分配任务取消
         */
        followCancel() {
          this.followFlag = false;
          this.followReset();
        },

        /**
         * 重置分配表单
         */
        followReset() {
          this.followForm = {
            designId: "",
            measurePeople: "",
            dimensional: "",
            visualization: "",
            construction: ""
          };
          this.$refs["followForm"]?.resetFields();
        },

        /**
         * 分配任务弹窗打开
         */
        assignasks() {
          if (this.ids.length !== 1) {
            this.$modal.msgWarning("请选中1条设计数据进行分配");
            return;
          }
          const selectedRow = this.designList.find(item => item.id === this.ids[0]);
          if (!selectedRow) {
            this.$modal.msgWarning("选中数据不存在");
            return;
          }
          this.followFlag = true;
          this.followTitle = `${selectedRow.shopName}-设计团队指派`;
          this.followForm.designId = selectedRow.id;
        },

        /**
         * 格式化设计类型
         */
        formatShopType(row) {
          const typeMap = { 1: "平面设计", 2: "效果设计", 3: "施工设计" };
          return typeMap[row.designType] || row.designType || "-";
        },

        /**
         * 格式化设计状态
         */
        formatShopStatus(row) {
          const statusMap = {
            0: "待分配", 1: "待测量", 2: "平面-设计中", 3: "平面-审核中", 4: "平面-驳回",
            5: "效果-设计中", 6: "效果-审核中", 7: "效果-驳回", 8: "施工-设计中",
            9: "施工-审核中", 10: "施工-驳回", 11: "完成"
          };
          return statusMap[row.designStatus] || row.designStatus || "-";
        },

        /**
         * 转营建
         */
        toMeasure(row) {
          const ids = row ? row.id : this.ids;
          if (!ids || (Array.isArray(ids) && ids.length === 0)) {
            this.$modal.msgWarning("请选择数据");
            return;
          }
          this.$modal.confirm(`是否确认流转编号为"${ids}"的数据到营建？`).then(() => {
            return toBuild(ids);
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess("营建流转成功");
          }).catch(() => { });
        },

        /**
         * 查询设计列表
         */
        getList() {
          this.loading = true;
          listDesign(this.queryParams).then(response => {
            this.designList = response.rows;
            this.total = response.total;
            this.loading = false;
          }).catch(() => {
            this.loading = false;
          });
        },

        /**
         * 搜索
         */
        handleQuery() {
          this.queryParams.pageNum = 1;
          this.getList();
        },

        /**
         * 重置搜索
         */
        resetQuery() {
          this.$refs["queryForm"]?.resetFields();
          this.handleQuery();
        },

        /**
         * 表格选中事件
         */
        handleSelectionChange(selection) {
          this.ids = selection.map(item => item.id);
          this.single = selection.length !== 1;
          this.multiple = selection.length === 0;
        },

        /**
         * 删除
         */
        handleDelete(row) {
          const ids = row ? row.id : this.ids;
          if (!ids || (Array.isArray(ids) && ids.length === 0)) {
            this.$modal.msgWarning("请选择数据");
            return;
          }
          this.$modal.confirm(`是否确认删除编号为"${ids}"的设计数据？`).then(() => {
            return delDesign(ids);
          }).then(() => {
            this.getList();
            this.$modal.msgSuccess("删除成功");
          }).catch(() => { });
        },

        /**
         * 导出
         */
        handleExport() {
          this.download('system/design/export', { ...this.queryParams }, `design_${new Date().getTime()}.xlsx`);
        },

        /**
         * 加载基础下拉选项数据
         */
        getBaseSelectData() {
          const commonParams = { pageNum: 1, pageSize: 1000 };
          // 加载店铺列表
          listShop(commonParams).then(res => {
            this.shopList = res.rows.map(item => ({
              value: item.id,
              label: item.name
            }));
          });
          // 加载客户列表
          listClient(commonParams).then(res => {
            this.clientPerson = res.rows.map(item => ({
              value: item.id,
              label: item.name
            }));
          });
          // 加载设计人员列表
          listUser(commonParams).then(res => {
            this.followUpPerson = res.rows.map(item => ({
              value: item.userId,
              label: item.nickName
            }));
          });
        }
      }
    }
    </script>

    <style scoped>
    /* 基础样式 */
    .app-container {
      padding: 16px;
    }
    .el-table--enable-row-transition .el-table__body td.el-table__cell a {
      color: #409eff;
    }

    /* 详情弹窗样式 */
    .detail-content {
      padding: 8px 0;
    }
    .section-title {
      font-size: 16px;
      font-weight: 700;
      color: #1f2d3d;
      margin: 15px 0 10px;
      border-left: 4px solid #409eff;
      padding-left: 8px;
    }
    .info-row {
      margin-bottom: 12px;
      font-size: 14px;
    }
    .info-label {
      color: #666;
      display: inline-block;
      width: 80px;
      text-align: right;
      margin-right: 5px;
    }
    .info-value {
      color: #333;
    }
    .mt-10 {
      margin-top: 10px;
    }

    /* 文件卡片样式 */
    .file-card {
      border: 1px solid #e6e6e6;
      border-radius: 4px;
      padding: 12px;
      height: 160px;
      display: flex;
      flex-direction: column;
      background: #fafafa;
    }
    .file-card-header {
      font-weight: 700;
      color: #409eff;
      margin-bottom: 10px;
      border-bottom: 1px dashed #e6e6e6;
      padding-bottom: 8px;
    }
    .file-card-body {
      flex: 1;
      overflow-y: auto;
      padding: 5px 0;
    }
    .file-link {
      display: block;
      margin: 8px 0;
      color: #409eff;
      cursor: pointer;
    }
    .no-file-tip, .no-process-tip {
      color: #999;
      text-align: center;
      padding: 15px 0;
      font-size: 14px;
    }
    .upload-btn {
      margin-top: 10px;
    }

    /* 流程步骤样式 */
    .process-step {
      margin-bottom: 20px;
      padding-left: 24px;
      position: relative;
    }
    .process-step::before {
      content: '';
      position: absolute;
      left: 8px;
      top: 24px;
      bottom: -20px;
      width: 1px;
      background: #e6e6e6;
      z-index: 1;
    }
    .process-step:last-child::before {
      display: none;
    }
    .step-header {
      font-weight: 500;
      color: #333;
      margin-bottom: 8px;
      position: relative;
      z-index: 2;
    }
    .step-content {
      color: #666;
      font-size: 14px;
      line-height: 1.6;
    }
    .audit-feedback {
      color: #f56c6c;
      margin-top: 5px;
    }
    .operation-panel {
      margin-top: 10px;
    }

    /* 文件预览样式 */
    .file-preview-container {
      padding: 10px;
    }
    .no-preview-tip {
      text-align: center;
      padding: 50px 0;
      color: #999;
    }

    /* 弹窗底部样式 */
    .dialog-footer {
      text-align: right;
    }
    /* 空流程记录时的提交按钮样式 */
    .empty-process-submit {
      text-align: center;
      padding: 20px 0;
      border: 1px dashed #e6e6e6;
      border-radius: 4px;
      margin-bottom: 10px;
    }

    .empty-tip {
      color: #999;
      font-size: 12px;
      margin-top: 8px;
    }
    /* 基础信息区域核心对齐样式 */
    .base-info-card {
      background: #fff;
      border-radius: 8px;
      padding: 16px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
      margin-bottom: 20px;
    }

    .info-row-wrap {
      margin-top: 10px;
    }

    /* 每个信息项：flex布局实现标签和值并排 */
    .info-item {
      display: flex;
      align-items: flex-start; /* 从 center 改为 flex-start，让文字顶部对齐 */
      min-height: 32px;
      padding: 4px 0;
    }
    .info-label-wrap {
      width: 130px; /* 从 100px 调大，确保长标签完整显示 */
      text-align: left;
      font-size: 14px;
      color: #666;
      flex-shrink: 0;
    }


    /* 值容器：自适应剩余宽度+左对齐 */
    .info-value-wrap {
      flex: 1; /* 占满剩余空间 */
      text-align: left;
      font-size: 14px;
      color: #333; /* 值文字深色，突出内容 */
      padding-left: 8px; /* 与标签保持间距 */
      word-break: break-all; /* 长文本自动换行 */
    }

    /* 预警项特殊样式 */
    .warning-item .info-value {
      transition: color 0.3s;
    }

    /* 其他样式优化（保持整体美观） */
    .section-title {
      font-size: 16px;
      font-weight: 600;
      color: #1f2d3d;
      margin: 0 0 16px 0;
      padding-left: 8px;
      border-left: 3px solid #409eff;
    }

    .file-resource-card, .process-progress-card {
      background: #fff;
      border-radius: 8px;
      padding: 16px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
      height: 100%;
    }

    .file-card {
      background: #fafafa;
      border-radius: 6px;
      padding: 12px;
      border: 1px solid #f0f0f0;
    }

    .process-step {
      display: flex;
      margin-bottom: 24px;
      position: relative;
    }

    .step-icon {
      font-size: 18px;
    }

    .step-content-wrap {
      margin-left: 16px;
      padding-bottom: 24px;
    }

    .detail-loading {
      text-align: center;
      padding: 40px 0;
    }

    .loading-text {
      margin-top: 12px;
      color: #666;
    }

    .mt-20 {
      margin-top: 20px;
    }
    </style>
