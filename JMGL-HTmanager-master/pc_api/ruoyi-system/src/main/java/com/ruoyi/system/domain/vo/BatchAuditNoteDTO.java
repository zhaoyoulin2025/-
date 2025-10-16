package com.ruoyi.system.domain.vo;

import java.util.List;

public class BatchAuditNoteDTO {
    private List<Long> noteIds;



    private Integer statusss;


    public List<Long> getNoteIds() {
        return noteIds;
    }

    public void setNoteIds(List<Long> noteIds) {
        this.noteIds = noteIds;
    }

    public Integer getStatusss() {
        return statusss;
    }

    public void setStatusss(Integer statusss) {
        this.statusss = statusss;
    }
}
