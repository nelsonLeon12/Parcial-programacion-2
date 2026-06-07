package gamezone.entities;

public class DigitalVideoGame extends VideoGame implements Sellable, Tableable {

    private double sizeGB;
    private String downloadPlatform;

    public DigitalVideoGame(String title, double price, String platform, int stock,
                            String genre, double sizeGB, String downloadPlatform) {
        super(title, price, platform, stock, genre);
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
    public double calculateFinalPrice() {
        return sizeGB > 50 ? price + 5000 : price;
    }

    @Override
    public double sell(int qty) {
        if (qty > stock) throw new IllegalArgumentException("Stock insuficiente.");
        stock -= qty;
        return calculateFinalPrice() * qty;
    }

    @Override
    public String getDisplayInfo() {
        return "[DIGITAL] " + title + " | " + platform + " | $" + calculateFinalPrice()
               + " | Stock: " + stock + " | " + sizeGB + "GB";
    }

    @Override
    public Object[] toTableRow() {
        return new Object[]{ title, "Digital", platform, genre, calculateFinalPrice(), stock, sizeGB + " GB" };
    }

    @Override
    public String toString() {
        return "DigitalVideoGame{title='" + title + "', price=" + price +
               ", platform='" + platform + "', stock=" + stock +
               ", genre='" + genre + "', sizeGB=" + sizeGB +
               ", downloadPlatform='" + downloadPlatform + "'}";
    }
}
