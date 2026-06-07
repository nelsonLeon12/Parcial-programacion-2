package gamezone.repositories;

import gamezone.entities.Sale;
import gamezone.entities.VideoGame;
import gamezone.utils.JsonUtil;
import gamezone.utils.JsonUtil.SaleData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SaleRepository {

    private List<Sale> sales = new ArrayList<>();

    public SaleRepository(List<VideoGame> catalog) {
        // Reconstruir objetos Sale desde SaleData persistido
        List<SaleData> dataList = JsonUtil.loadSales();
        for (SaleData sd : dataList) {
            VideoGame g = catalog.stream()
                    .filter(v -> v.getTitle().equalsIgnoreCase(sd.gameTitle))
                    .findFirst().orElse(null);
            if (g != null) {
                Sale s = new Sale(sd.id, g, sd.quantity, sd.unitPrice);
                sales.add(s);
            }
        }
    }

    public void add(Sale sale) {
        sales.add(sale);
        persist();
    }

    public List<Sale> getAll() {
        return new ArrayList<>(sales);
    }

    private void persist() {
        List<SaleData> dataList = new ArrayList<>();
        for (Sale s : sales) {
            SaleData sd = new SaleData();
            sd.id = s.getId();
            sd.gameTitle = s.getVideoGame().getTitle();
            sd.quantity = s.getQuantity();
            sd.unitPrice = s.getUnitPrice();
            sd.total = s.getTotal();
            sd.saleDate = s.getSaleDate().toString();
            dataList.add(sd);
        }
        JsonUtil.saveSales(dataList);
    }
}
