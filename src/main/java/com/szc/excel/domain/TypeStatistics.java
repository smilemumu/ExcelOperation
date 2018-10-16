package com.szc.excel.domain;

public class TypeStatistics {
    private Integer rollCount;
    private Integer fileCount;
    private String fileType;

    public Integer getRollCount() {
        return rollCount;
    }

    public Integer getFileCount() {
        return fileCount;
    }

    public String getFileType() {
        return fileType;
    }

    public void setRollCount(Integer rollCount) {
        this.rollCount = rollCount;
    }

    public void setFileCount(Integer fileCount) {
        this.fileCount = fileCount;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "TypeStatistics{" +
                "rollCount=" + rollCount +
                ", fileCount=" + fileCount +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
