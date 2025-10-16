package com.ruoyi.system.service.impl;

import java.util.*;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.DesignAssignRecord;
import com.ruoyi.system.domain.DesignFlowDetail;
import com.ruoyi.system.domain.WxDesignUpdateStatusDTO;
import com.ruoyi.system.domain.vo.DesignTaskAssignDTO;
import com.ruoyi.system.domain.vo.WxDesignListVO;
import com.ruoyi.system.mapper.DesignAssignRecordMapper;
import com.ruoyi.system.mapper.DesignFlowDetailMapper;
import com.ruoyi.system.service.IFileService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DesignMapper;
import com.ruoyi.system.domain.Design;
import com.ruoyi.system.service.IDesignService;

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
        Design design = designMapper.selectDesignById(id);
        List<DesignFlowDetail> designFlowDetails =
                designFlowDetailMapper.selectByDesignIdAndStageList(id);
        for (DesignFlowDetail designFlowDetail : designFlowDetails) {
            if(designFlowDetail.getFlowStage() ==1){
                design.setMeasureFiles(getList(designFlowDetail));
            }
            if(designFlowDetail.getFlowStage() ==2){
                design.setFloorFiles(getList(designFlowDetail));
            }
            if(designFlowDetail.getFlowStage() ==3){
                design.setEffectFiles(getList(designFlowDetail));
            }
            if(designFlowDetail.getFlowStage() ==4){
                design.setConstructionFiles(getList(designFlowDetail));
            }

        }
        //再就是 阶段过程数据






        return designMapper.selectDesignById(id);
    }

    private List<Map<String, String>> getList(DesignFlowDetail designFlowDetail) {
        List<Map<String, String>> list = new ArrayList<>();
        List<String> files = Arrays.asList(designFlowDetail.getFileUrl().split(","));

        if (CollectionUtils.isNotEmpty(files)) {
            for (int i = 0; i < files.size(); i++) {
                Map<String,String> map = new HashMap<>();
                map.put("文件"+1,files.get(i));
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
}
