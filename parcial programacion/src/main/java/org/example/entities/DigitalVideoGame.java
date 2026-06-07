package org.example.entities;

public class DigitalVideoGame extends VideoGame {
    private double sizeGB;
    String downloadPlatform;

    public DigitalVideoGame(String title, double price, String platform, int stock, String genre,
                            double sizeGB, String downloadPlatform) {
        super(title,price,platform,stock,genre);
        this.sizeGB = sizeGB;
        this.downloadPlatform = downloadPlatform;
    }

    public double getSizeGB() {
        return sizeGB;
    }

    public void setSizeGB(double sizeGB) {
        this.sizeGB = sizeGB;
    }

    public String getDownloadPlatform() {
        return downloadPlatform;
    }

    public void setDownloadPlatform(String downloadPlatform) {
        this.downloadPlatform = downloadPlatform;
    }
    @Override
    public double calculateFinalPrice(){
        if(sizeGB>50){
            price+=5000;
        }
        return price;
    }

    public double sell(int qty){
        return calculateFinalPrice() * qty;
    }

    public String getDisplayInfo(){
        return "Título: " + title +
                "\nPrecio: " + price +
                "\nPlataforma: " + platform +
                "\nTamaño: " + sizeGB + " GB" +
                "\nDescarga: " + downloadPlatform;
    }

    public Object[] toTableRow(){
        return new Object[]{
                title,
                price,
                platform,
                stock,
                genre,
                sizeGB,
                downloadPlatform

        };
    }

    @Override
    public String toString() {
        return "DigitalVideoGame{" +
                "genre='" + genre + '\'' +
                ", stock=" + stock +
                ", platform='" + platform + '\'' +
                ", price=" + price +
                ", title='" + title + '\'' +
                ", downloadPlatform='" + downloadPlatform + '\'' +
                ", sizeGB=" + sizeGB +
                '}';
    }
}
