package com.sunnyserenade.midnightdiner.dto.request;

public class CloseOrderRequest {
    private Long tableId;

    public Long getTableId() { return tableId; }
    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }
}
