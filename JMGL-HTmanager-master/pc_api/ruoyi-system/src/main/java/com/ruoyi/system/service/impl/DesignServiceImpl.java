package com.ruoyi.system.service.impl;

import java.util.*;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.domain.vo.AuditDTO;
import com.ruoyi.system.domain.vo.DesignProcessRecord;
import com.ruoyi.system.domain.vo.DesignTaskAssignDTO;
import com.ruoyi.system.domain.vo.WxDesignListVO;
import com.ruoyi.system.mapper.DesignAssignRecordMapper;
import com.ruoyi.system.mapper.DesignFlowDetailMapper;
import com.ruoyi.system.mapper.ShopMapper;
import com.ruoyi.system.service.IFileService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DesignMapper;
import com.ruoyi.system.service.IDesignService;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * 设计管理Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-18
 */
@Service
public class DesignServiceImpl implements IDesignService
{

    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private DesignMapper designMapper;

    @Autowired
    private DesignAssignRecordMapper designAssignRecordMapper;


    @Autowired
    private DesignFlowDetailMapper designFlowDetailMapper;



    /**
     * 查询设计管理
     *
     * @param id 设计管理主键
     * @return 设计管理
     */
    @Override
    public Design selectDesignById(Long id)
    {

        LoginUser loginUser = getLoginUser();
        Long userId = loginUser.getUser().getUserId();
        Design design = designMapper.selectDesignById(id);
        design.setAuditor(false);
        design.setDel(userId.equals(design.getCurrent_designer_id()));
        List<SysRole> roles = loginUser.getUser().getRoles();
        for (SysRole role : roles) {
            if("设计总监".equals(role.getRoleName())){
                design.setAuditor(true);
            }

        }
        List<DesignFlowDetail> designFlowDetails =
                designFlowDetailMapper.selectByDesignIdAndStageList(id);
        List<DesignProcessRecord> processRecords = new ArrayList<>();
        //从过程数据提取 文件按
        for (DesignFlowDetail designFlowDetail : designFlowDetails) {
            //只获取当前有用的信息
            if (designFlowDetail.getFlowStage() ==1) {
                if(designFlowDetail.getAuditStatus()!=2){
                    design.setMeasureFiles(getList(designFlowDetail));
                }
            }
            if (designFlowDetail.getFlowStage() ==2) {
                if(designFlowDetail.getAuditStatus()!=2){
                    design.setFloorFiles(getList(designFlowDetail));
                }
            }
            if (designFlowDetail.getFlowStage() ==3) {
                if(designFlowDetail.getAuditStatus()!=2){
                    design.setEffectFiles(getList(designFlowDetail));
                }
            }
            if (designFlowDetail.getFlowStage() ==4) {
                if(designFlowDetail.getAuditStatus()!=2){
                    design.setConstructionFiles(getList(designFlowDetail));
                }
            }
            // 3.2 构建流程历史记录
            DesignProcessRecord processRecord = buildProcessRecord(designFlowDetail);
            processRecords.add(processRecord);

        }
        //再从过程数据提起，流程历史列表
        List<DesignProcessRecord> sortedProcessRecords = processRecords.stream()
                .sorted((r1, r2) -> {
                    // 按阶段升序
                    int stageCompare = r1.getStage().compareTo(r2.getStage());
                    if (stageCompare != 0) {
                        return stageCompare;
                    }
                    // 同阶段按操作时间降序（空值放后面）
                    Date time1 = r1.getOperateTime();
                    Date time2 = r2.getOperateTime();
                    if (time1 == null) return 1;
                    if (time2 == null) return -1;
                    return time2.compareTo(time1);
                })
                .collect(Collectors.toList());
        design.setProcessRecords(sortedProcessRecords);
        return design;
    }

    private DesignProcessRecord buildProcessRecord(DesignFlowDetail detail) {
        DesignProcessRecord record = new DesignProcessRecord();
        record.setId(detail.getId());
        record.setDesignId(detail.getDesignId());
        // 如果是
        record.setStage(detail.getFlowStage()); // 阶段编号
        record.setStageName(getStatgeName(detail.getFlowStage())); // 阶段名称

        record.setAuditId(detail.getAuditorId());
        // 设置操作时间（优先取提交时间，无则取创建时间）
        record.setOperateTime(detail.getSubmitTime() != null ?
                detail.getSubmitTime() : detail.getCreateTime());

        // 设置状态信息
        record.setSubmitStatus(detail.getSubmitStatus());
        record.setAuditStatus(detail.getAuditStatus());
        record.setStatusDesc(getStatusDesc(detail.getSubmitStatus(), detail.getAuditStatus()));

        // 设置操作人信息（如果有）
//        record.setOperator(detail.getOperator());
        return record;
    }

    private String getStatusDesc(Integer submitStatus, Integer auditStatus) {
        if (submitStatus == null) {
            return "未提交";
        }

        // 提交状态描述
        String submitDesc = submitStatus == 1 ? "已提交" :
                (submitStatus == 2 ? "重新提交" : "未提交");

        // 审核状态描述
        if (auditStatus == null) {
            return submitDesc;
        }
        String auditDesc = auditStatus == 1 ? "审核通过" :
                (auditStatus == 2 ? "审核驳回" : "待审核");

        return submitDesc + " - " + auditDesc;
    }

    private String getStatgeName(Integer flowStage) {
        if(1==flowStage){
            return "测量阶段";
        }
        if(2==flowStage){
            return "平面设计阶段";
        }
        if(3==flowStage){
            return "效果设计阶段";
        }
        if(4==flowStage){
            return "施工设计阶段";
        }
        return "unKonw";
    }

    private List<Map<String, String>> getList(DesignFlowDetail designFlowDetail) {
        List<Map<String, String>> list = new ArrayList<>();
        List<String> files = Arrays.asList(designFlowDetail.getFileUrl().split(","));
        if (CollectionUtils.isNotEmpty(files)) {
            for (int i = 0; i < files.size(); i++) {
                Map<String,String> map = new HashMap<>();
                map.put("url",files.get(i));
                map.put("name",designFlowDetail.getFileName());
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 查询设计管理列表
     *
     * @param design 设计管理
     * @return 设计管理
     */
    @Override
    public List<Design> selectDesignList(Design design)
    {
        // 1. 查询所有设计数据（使用原有SQL）
        List<Design> allDesigns = designMapper.selectDesignList(design);

        // 2. 获取当前登录用户ID
        Long currentLoginUserId = SecurityUtils.getUserId();

        // 3. 存储筛选后的设计列表（设计师负责任意阶段的设计）
        List<Design> filteredDesigns = new ArrayList<>();

        for (Design design1 : allDesigns) {
            // 4. 查询当前设计师负责的所有阶段（支持多个阶段，返回列表）
            List<Integer> responsibleStages = designAssignRecordMapper.getResponsibleStages(
                    design1.getId(),currentLoginUserId
                    );
            // 5. 筛选逻辑：只要负责任意阶段，就保留该设计
            if (responsibleStages != null && !responsibleStages.isEmpty()) {
                // 6. 生成带颜色的标签（根据当前设计状态匹配对应的负责阶段）
                design1.setCurrentStageTag(
                        buildColoredTag(design1, responsibleStages)
                );
                //设置天数
            }
        }
        return allDesigns;
    }
    private String buildColoredTag(Design design1, List<Integer> responsibleStages) {
        String statusText = design1.getCurrent_stage_desc(); // 状态文字（来自SQL）
        Integer designStatus = design1.getDesignStatus();  // 状态码（0-11）

        // 根据当前设计状态，判断对应的阶段是否在设计师的负责范围内
        Integer currentStage = getStageByStatus(designStatus);
        if (currentStage != null && responsibleStages.contains(currentStage)) {
            // 是负责的阶段，按状态显示对应颜色（用if-else替换switch表达式）
            if (designStatus == 1) {
                return "待测量|blue";
            } else if (designStatus == 2) {
                return "平面-设计中|blue";
            } else if (designStatus == 3) {
                return "平面-审核中|yellow";
            } else if (designStatus == 4) {
                return "平面-驳回|red";
            } else if (designStatus == 5) {
                return "效果-设计中|blue";
            } else if (designStatus == 6) {
                return "效果-审核中|yellow";
            } else if (designStatus == 7) {
                return "效果-驳回|red";
            } else if (designStatus == 8) {
                return "施工-设计中|blue";
            } else if (designStatus == 9) {
                return "施工-审核中|yellow";
            } else if (designStatus == 10) {
                return "施工-驳回|red";
            } else {
                // 其他未匹配的状态（如11=完成），显示灰色
                return statusText + "|gray";
            }
        } else {
            // 不是负责的阶段，显示灰色
            return statusText + "|gray";
        }
    }

    /**
     * 辅助方法：根据设计状态码（0-11）获取对应的阶段（1-4）
     */
    private Integer getStageByStatus(Integer designStatus) {
        if (designStatus == 1) return 1;                  // 待测量 → 阶段1
        if (designStatus >= 2 && designStatus <= 4) return 2; // 平面相关 → 阶段2
        if (designStatus >= 5 && designStatus <= 7) return 3; // 效果相关 → 阶段3
        if (designStatus >= 8 && designStatus <= 10) return 4; // 施工相关 → 阶段4
        return null; // 待分配（0）、完成（11）等状态无对应阶段
    }

    /**
     * 新增设计管理
     *
     * @param design 设计管理
     * @return 结果
     */
    @Override
    public int insertDesign(Design design)
    {
        return designMapper.insertDesign(design);
    }

    /**
     * 修改设计管理
     *
     * @param design 设计管理
     * @return 结果
     */
    @Override
    public int updateDesign(Design design)
    {
        return designMapper.updateDesign(design);
    }

    /**
     * 批量删除设计管理
     *
     * @param ids 需要删除的设计管理主键
     * @return 结果
     */
    @Override
    public int deleteDesignByIds(Long[] ids)
    {
        return designMapper.deleteDesignByIds(ids);
    }

    /**
     * 删除设计管理信息
     *
     * @param id 设计管理主键
     * @return 结果
     */
    @Override
    public int deleteDesignById(Long id)
    {
        return designMapper.deleteDesignById(id);
    }

    @Override
    public Design clientOwnShop(Long clientId, Long shopId) {
        return designMapper.clientOwnShop(clientId,shopId);
    }

    @Override
    public List<WxDesignListVO> wxDesignList(Long userId) {
        List<WxDesignListVO> result =designMapper.wxDesignList(userId);
        if(null != result && !result.isEmpty()){
            for (WxDesignListVO wxDesignListVO : result) {
                ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                String[] statusSplit = wxDesignListVO.getDesignStatus().split(",");
                String[] typeSplit = wxDesignListVO.getDesignType().split(",");
                String[] idSplit = wxDesignListVO.getDesignIdList().split(",");
                for (int i = 0; i < typeSplit.length; i++) {
                    HashMap<String, String> map = new HashMap<>();
                    String type = typeSplit[i];
                    if(StringUtils.isNotEmpty(type)){
                        String status = statusSplit[i];
                        String id = idSplit[i];
                        String typeName = "";
                        switch (type){
                            case "1":
                                typeName = "平面设计";
                                break;
                            case "2":
                                typeName = "效果设计";
                                break;
                            case "3":
                                typeName = "施工设计";
                                break;
                        }
                        map.put("name",typeName);
                        map.put("id",id);
                        map.put("status","2".equals(status)?"completed":"designing");
                        arrayList.add(map);
                    }
                }
                wxDesignListVO.setDesignPhases(arrayList);

            }
        }
        return result;
    }

    @Override
    public void updateDesignStatus(WxDesignUpdateStatusDTO wxDesignUpdateStatusDTO) {
        int status = "designing".equals(wxDesignUpdateStatusDTO.getSelectedStatus())?1:2;
        designMapper.updateDesignStatus(wxDesignUpdateStatusDTO.getDesignId(),status);
    }

    @Override
    public int assignDesignTask(DesignTaskAssignDTO designTaskAssignDTO) {

        DesignAssignRecord designAssignRecord = new DesignAssignRecord();

        designAssignRecord.setAssignTime(new Date());
        designAssignRecord.setDesignId(designTaskAssignDTO.getDesignId());
        designAssignRecord.setDesignStage(1);
        designAssignRecord.setDesignerId(designTaskAssignDTO.getMeasurePeople());
        designAssignRecordMapper.insertDesignAssignRecord(designAssignRecord);
        designAssignRecord.setDesignStage(2);
        designAssignRecord.setDesignerId(designTaskAssignDTO.getDimensional());
        designAssignRecordMapper.insertDesignAssignRecord(designAssignRecord);
        designAssignRecord.setDesignStage(3);
        designAssignRecord.setDesignerId(designTaskAssignDTO.getVisualization());
        designAssignRecordMapper.insertDesignAssignRecord(designAssignRecord);
        designAssignRecord.setDesignStage(4);
        designAssignRecord.setDesignerId(designTaskAssignDTO.getConstruction());
        designAssignRecordMapper.insertDesignAssignRecord(designAssignRecord);
        Design design = designMapper.selectDesignById(designTaskAssignDTO.getDesignId());
        design.setDesignStatus(1);
        designMapper.updateDesign(design);
        return 4;
    }

    @Override
    public int add(DesignFlowDetail detail) {

        //先查一下
        Long designId = detail.getDesignId();
        Integer flowStage = detail.getFlowStage(); // 当前阶段（1=测量，2=平面，3=效果，4=施工）

        //设置 店铺的状态



        // 1. 查询是否存在现有记录
        DesignFlowDetail existingDetail = designFlowDetailMapper.selectByDesignIdAndStage(designId, flowStage);

        if (existingDetail != null && existingDetail.getAuditorId() == 0) {
            // 2. 重新上传（已存在记录且未审核）：更新文件信息，状态保持不变
            existingDetail.setFileName(detail.getFileName());
            existingDetail.setFileUrl(detail.getFileUrl());
            existingDetail.setUpdateTime(new Date());
            designFlowDetailMapper.updateDesignFlowDetail(existingDetail);
        } else {
            // 3. 首次上传：新增记录，并推进Design状态到“当前阶段待审核”
            detail.setDesignerId(getLoginUser().getUserId());
            detail.setCreateTime(new Date());
            detail.setSubmitStatus(1); // 已提交
            detail.setAuditStatus(0); // 待审核
            designFlowDetailMapper.insertDesignFlowDetail(detail);

            // 4. 根据当前阶段，更新Design的designStatus为“待审核”状态
            Design design = designMapper.selectDesignById(designId);
            if (design != null) {

                Shop shop = shopMapper.selectShopById(design.getShopId());
                if (!(""+flowStage).equals(shop.getRunStatus())) {
                    shop.setUpTime(new Date());
                    shop.setRunStatus(""+flowStage);
                    shopMapper.updateShop(shop);
                }
                // 阶段与待审核状态的映射（根据你的业务定义）
                // 例如：测量阶段提交后→平面-待审核（3）；平面提交后→效果-待审核（6）等
                Integer targetStatus = getAuditStatusByStage(flowStage);
                if (targetStatus != null) {
                    design.setDesignStatus(targetStatus);
                    design.setUpdateTime(new Date());
                    designMapper.updateDesign(design); // 同步更新Design状态
                }
            }
        }
        return 1;
    }

    /**
     * 根据阶段获取对应的“待审核”状态码
     * （根据你的designStatus定义调整，示例如下）
     */
    private Integer getAuditStatusByStage(Integer flowStage) {
        switch (flowStage) {
            case 1: return 3; // 测量阶段提交 → 平面-待审核（假设3是平面审核中）
            case 2: return 6; // 平面阶段提交 → 效果-待审核（假设6是效果审核中）
            case 3: return 9; // 效果阶段提交 → 施工-待审核（假设9是施工审核中）
            case 4: return 11; // 施工阶段提交 → 完成（假设11是完成）
            default: return null;
        }
    }

    /**
     * 审核设计流程记录
     */
    @Transactional // 事务保证：确保状态更新的原子性
    @Override
    public void auditDesignRecord(AuditDTO auditDTO) {
        // 1. 校验参数
        Long recordId = auditDTO.getRecordId();
        Integer auditResult = auditDTO.getAuditResult();
        if (recordId == null || auditResult == null || (auditResult != 1 && auditResult != 2)) {
            throw new ServiceException("参数错误：recordId和auditResult（1=通过，2=驳回）为必填项");
        }
        // 2. 查询待审核的记录（DesignFlowDetail）
        DesignFlowDetail detail = designFlowDetailMapper.select(recordId);
        if (detail == null) {
            throw new ServiceException("待审核记录不存在");
        }

        // 3. 校验记录状态：只能审核“待审核”状态的记录
        if (detail.getAuditStatus() != 0) { // 假设0=待审核，1=通过，2=驳回
            throw new ServiceException("该记录已审核，无需重复操作");
        }
        // 4. 更新审核状态
        detail.setAuditStatus(auditResult); // 1=通过，2=驳回
        detail.setAuditFeedback(auditDTO.getAuditOpinion()); // 审核意见
        detail.setAuditTime(new Date()); // 审核时间
        detail.setAuditorId(getLoginUser().getUser().getUserId()); // 审核人ID（从当前登录用户获取）
        designFlowDetailMapper.updateDesignFlowDetail(detail);
        // 5. 根据审核结果，更新设计的整体状态（核心业务逻辑）
        Design design = designMapper.selectDesignById(detail.getDesignId());
        if (design == null) {
            throw new ServiceException("设计信息不存在");
        }

        // 5.1 审核通过：推进到下一阶段
        if (auditResult == 1) {
            // 例如：当前阶段是“平面-审核中”（status=3）→ 推进到“效果-设计中”（status=5）
            design.setDesignStatus(getNextStatus(detail.getFlowStage()));
        }
        // 5.2 审核驳回：退回当前阶段的“设计中”状态
        else if (auditResult == 2) {
            // 例如：当前阶段是“平面-审核中”（status=3）→ 退回“平面-设计中”（status=2）
            design.setDesignStatus(getRejectStatus(detail.getFlowStage()));
        }

        designMapper.updateDesign(design);
    }

    /**
     * 审核通过后，获取下一阶段的状态码
     * （根据你的业务状态流转规则定义，示例如下）
     */
    private Integer getNextStatus(Integer flowStage) {
        // flowStage：1=测量，2=平面，3=效果，4=施工
        switch (flowStage) {
            case 1: return 2; // 测量通过 → 平面-设计中（status=2）
            case 2: return 5; // 平面通过 → 效果-设计中（status=5）
            case 3: return 8; // 效果通过 → 施工-设计中（status=8）
            case 4: return 11; // 施工通过 → 完成（status=11）
            default: return null;
        }
    }

    /**
     * 审核驳回后，获取退回的状态码
     */
    private Integer getRejectStatus(Integer flowStage) {
        switch (flowStage) {
            case 1: return 1; // 测量驳回 → 待测量（status=1）
            case 2: return 2; // 平面驳回 → 平面-设计中（status=2）
            case 3: return 5; // 效果驳回 → 效果-设计中（status=5）
            case 4: return 8; // 施工驳回 → 施工-设计中（status=8）
            default: return null;
        }
    }
}
