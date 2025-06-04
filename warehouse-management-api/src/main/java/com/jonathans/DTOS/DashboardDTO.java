package com.jonathans.DTOS;

public class DashboardDTO {
    private long totalItems;
    private long activeWarehouses;
    private long openAlerts;
    private long recentMovements;

    public DashboardDTO() {
    }

    public DashboardDTO(long totalItems, long activeWarehouses, long openAlerts, long recentMovements) {
        this.totalItems = totalItems;
        this.activeWarehouses = activeWarehouses;
        this.openAlerts = openAlerts;
        this.recentMovements = recentMovements;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public long getActiveWarehouses() {
        return activeWarehouses;
    }

    public void setActiveWarehouses(long activeWarehouses) {
        this.activeWarehouses = activeWarehouses;
    }

    public long getOpenAlerts() {
        return openAlerts;
    }

    public void setOpenAlerts(long openAlerts) {
        this.openAlerts = openAlerts;
    }

    public long getRecentMovements() {
        return recentMovements;
    }

    public void setRecentMovements(long recentMovements) {
        this.recentMovements = recentMovements;
    }

    @Override
    public String toString() {
        return "DashboardDTO [totalItems=" + totalItems + ", activeWarehouses=" + activeWarehouses + ", openAlerts="
                + openAlerts + ", recentMovements=" + recentMovements + ", getTotalItems()=" + getTotalItems()
                + ", getActiveWarehouses()=" + getActiveWarehouses() + ", getOpenAlerts()=" + getOpenAlerts()
                + ", getRecentMovements()=" + getRecentMovements() + ", getClass()=" + getClass() + ", hashCode()="
                + hashCode() + ", toString()=" + super.toString() + "]";
    }

}
