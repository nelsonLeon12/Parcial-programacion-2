package org.example.entities;

public class PhysicalVideoGame extends VideoGame {
    private String condition;
    private String distributor;

    public PhysicalVideoGame(String title, double price, String platform, int stock,
                             String genre,String condition, String distributor) {
        super(title,price,platform,stock,genre);
        this.condition = condition;
        this.distributor = distributor;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }
    @Override
    public double calculateFinalPrice(){
        if(condition.equals("USED")){
            price = price - (price*0.25);
        }
        return price;
    }

    public double sell(int qty){
        return calculateFinalPrice() * qty;
    }

    public String getDisplayInfo(){
        return "Titulo: " + title +
                "\nPrecio: "+ price +
                "\nPlataforma: "+ platform +
                "\nStock: "+ stock +
                "\nGenero: "+ genre +
                "\nEstado:"+ condition +
                "\nDistribuidor: "+ distributor;
    }

    public Object[] toTableRow(){
        return new Object[]{
                title,
                price,
                platform,
                stock,
                genre,
                condition,
                distributor

        };
    }

    @Override
    public String toString() {
        return "PhysicalVideoGame{" +
                "condition='" + condition + '\'' +
                ", distributor='" + distributor + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", platform='" + platform + '\'' +
                ", stock=" + stock +
                ", genre='" + genre + '\'' +
                '}';
    }
}
