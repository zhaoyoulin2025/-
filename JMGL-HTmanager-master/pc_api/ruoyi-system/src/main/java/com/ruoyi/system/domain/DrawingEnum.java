package com.ruoyi.system.domain;

public enum DrawingEnum {

    封面(0l, "封面","F"),
    目录(1l, "目录","I"),
    施工设计说明(2l, "施工设计说明","B"),

    室内装修材料(3l, "室内装修材料","M"),
    材料表(4l, "材料表","T"),


    平面图(5l, "平面图","P"),

    立面图(6l, "立面图","E"),

    大图样(7l, "大图样","D"),
    未知(8l, "未知","X"),;

    private Long drawtype;

    private String drawtypename;

    private String drawNm;

    public String getDrawNm() {
        return drawNm;
    }

    public void setDrawNm(String drawNm) {
        this.drawNm = drawNm;
    }

    DrawingEnum(Long drawtype, String drawtypename, String drawNm) {
        this.drawtype = drawtype;
        this.drawtypename = drawtypename;
        this.drawNm = drawNm;
    }

    public Long getDrawtype() {
        return drawtype;
    }

    public void setDrawtype(Long drawtype) {
        this.drawtype = drawtype;
    }

    public String getDrawtypename() {
        return drawtypename;
    }

    public void setDrawtypename(String drawtypename) {
        this.drawtypename = drawtypename;
    }



    public static DrawingEnum getDrawName(Long drawingType ){
        for (DrawingEnum value : DrawingEnum.values()) {
            if(drawingType == value.getDrawtype() ){
                return value;
            }
        }
        return 未知;
    }

}
