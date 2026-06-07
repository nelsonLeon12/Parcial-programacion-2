package gamezone.services.impl;

import gamezone.entities.Sale;
import gamezone.entities.Sellable;
import gamezone.entities.VideoGame;
import gamezone.repositories.SaleRepository;
import gamezone.repositories.VideoGameRepository;
import gamezone.services.SaleService;

import java.util.List;
import java.util.UUID;

public class SaleServiceImpl implements SaleService {

    private final VideoGameRepository gameRepository;
    private final SaleRepository saleRepository;

    public SaleServiceImpl(VideoGameRepository gameRepository, SaleRepository saleRepository) {
        this.gameRepository = gameRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public Sale sellVideoGame(String title, int quantity) {
        VideoGame game = gameRepository.findByTitle(title);
        if (game == null) {
            throw new IllegalArgumentException("El videojuego '" + title + "' no existe en el catálogo.");
        }
        if (game.getStock() < quantity) {
            throw new IllegalStateException("Stock insuficiente. Disponible: " + game.getStock());
        }

        // Usar interfaz Sellable para reducir stock y calcular total
        double total = ((Sellable) game).sell(quantity);
        gameRepository.update(title, game); // guarda el stock reducido

        String id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        double unitPrice = game.calculateFinalPrice();
        Sale sale = new Sale(id, game, quantity, unitPrice);
        saleRepository.add(sale);
        return sale;
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.getAll();
    }
}
