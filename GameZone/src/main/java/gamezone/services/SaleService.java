package gamezone.services;

import gamezone.entities.Sale;
import java.util.List;

public interface SaleService {
    Sale sellVideoGame(String title, int quantity);
    List<Sale> getAllSales();
}
