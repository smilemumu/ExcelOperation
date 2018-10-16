package com.szc.excel.domain;

import java.util.List;

public class Statistics {
    private Integer rollCount;
    private Integer fileCount;
    List<TypeStatistics> typeStatistics;

    public Integer getRollCount() {
        return rollCount;
    }

    public Integer getFileCount() {
        return fileCount;
    }

    public List<TypeStatistics> getTypeStatistics() {
        return typeStatistics;
    }

    public void setRollCount(Integer rollCount) {
        this.rollCount = rollCount;
    }

    public void setFileCount(Integer foleCount) {
        this.fileCount = foleCount;
    }

    public void setTypeStatistics(List<TypeStatistics> typeStatistics) {
        this.typeStatistics = typeStatistics;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "rollCount=" + rollCount +
                ", foleCount=" + fileCount +
                ", typeStatistics=" + typeStatistics +
                '}';
    }
}
