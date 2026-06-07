package org.example.entities;

import java.time.LocalTime;

public class Sale {
    private String id;
    private VideoGame videoGame;
    private int quantity;
    private double unitPrice;
    private double total;
    private LocalTime saleDate;

    public Sale(String id, VideoGame videoGame, int quantity, double unitPrice) {
        this.id = id;
        this.videoGame = videoGame;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VideoGame getVideoGame() {
        return videoGame;
    }

    public void setVideoGame(VideoGame videoGame) {
        this.videoGame = videoGame;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalTime saleDate) {
        this.saleDate = saleDate;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id='" + id + '\'' +
                ", videoGame=" + videoGame +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", total=" + total +
                ", saleDate=" + saleDate +
                '}';
    }
}
