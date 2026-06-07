package gamezone;

import gamezone.entities.*;
import gamezone.repositories.SaleRepository;
import gamezone.repositories.VideoGameRepository;
import gamezone.services.impl.SaleServiceImpl;
import gamezone.services.impl.VideoGameServiceImpl;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    private VideoGameRepository gameRepo;
    private SaleRepository saleRepo;
    private VideoGameServiceImpl gameService;
    private SaleServiceImpl saleService;

    @Override
    public void start(Stage primaryStage) {
        gameRepo   = new VideoGameRepository();
        saleRepo   = new SaleRepository(gameRepo.getAll());
        gameService = new VideoGameServiceImpl(gameRepo);
        saleService = new SaleServiceImpl(gameRepo, saleRepo);

        primaryStage.setTitle("SISTEMA DE GESTIÓN - GAMEZONE");

        //  Botones del menú principal
        Button btnAgregar   = new Button("1. Agregar Videojuego");
        Button btnListar    = new Button("2. Listar todos los videojuegos");
        Button btnBuscarT   = new Button("3. Buscar por título");
        Button btnBuscarP   = new Button("4. Buscar por plataforma");
        Button btnVender    = new Button("5. Realizar venta");
        Button btnVerVentas = new Button("6. Mostrar ventas");
        Button btnSalir     = new Button("7. Salir");

        // Ancho uniforme
        double btnWidth = 280;
        for (Button b : new Button[]{btnAgregar, btnListar, btnBuscarT, btnBuscarP, btnVender, btnVerVentas, btnSalir}) {
            b.setPrefWidth(btnWidth);
        }

        VBox menu = new VBox(10, new Label("==== GAMEZONE ===="),
                btnAgregar, btnListar, btnBuscarT, btnBuscarP, btnVender, btnVerVentas, btnSalir);
        menu.setPadding(new Insets(20));

        // Acciones
        btnAgregar.setOnAction(e -> showAddDialog());
        btnListar.setOnAction(e -> showAllGames());
        btnBuscarT.setOnAction(e -> showSearchByTitle());
        btnBuscarP.setOnAction(e -> showSearchByPlatform());
        btnVender.setOnAction(e -> showSaleDialog());
        btnVerVentas.setOnAction(e -> showSales());
        btnSalir.setOnAction(e -> primaryStage.close());

        primaryStage.setScene(new Scene(menu, 340, 380));
        primaryStage.show();
    }

    // AGREGAR VIDEOJUEGO
    private void showAddDialog() {
        Stage st = new Stage();
        st.setTitle("Agregar Videojuego");

        // Tipo
        ToggleGroup tg = new ToggleGroup();
        RadioButton rbDigital  = new RadioButton("Digital");
        RadioButton rbPhysical = new RadioButton("Físico");
        rbDigital.setToggleGroup(tg);
        rbPhysical.setToggleGroup(tg);
        rbDigital.setSelected(true);

        // Campos comunes
        TextField tfTitle    = new TextField(); tfTitle.setPromptText("Título");
        TextField tfPrice    = new TextField(); tfPrice.setPromptText("Precio");
        TextField tfPlatform = new TextField(); tfPlatform.setPromptText("Plataforma");
        TextField tfStock    = new TextField(); tfStock.setPromptText("Stock");
        TextField tfGenre    = new TextField(); tfGenre.setPromptText("Género");

        // Campos específicos
        TextField tfSizeGB   = new TextField(); tfSizeGB.setPromptText("Tamaño (GB)");
        TextField tfDownload = new TextField(); tfDownload.setPromptText("Plataforma descarga");
        TextField tfCondition   = new TextField(); tfCondition.setPromptText("Condición (nuevo/usado)");
        TextField tfDistributor = new TextField(); tfDistributor.setPromptText("Distribuidor");

        // Panel dinámico
        VBox extraBox = new VBox(5, tfSizeGB, tfDownload);
        rbDigital.setOnAction(e -> {
            extraBox.getChildren().setAll(tfSizeGB, tfDownload);
        });
        rbPhysical.setOnAction(e -> {
            extraBox.getChildren().setAll(tfCondition, tfDistributor);
        });

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(e -> {
            try {
                String title   = tfTitle.getText().trim();
                double price   = Double.parseDouble(tfPrice.getText().trim());
                String platform= tfPlatform.getText().trim();
                int stock      = Integer.parseInt(tfStock.getText().trim());
                String genre   = tfGenre.getText().trim();

                VideoGame game;
                if (rbDigital.isSelected()) {
                    double sizeGB = Double.parseDouble(tfSizeGB.getText().trim());
                    String dl     = tfDownload.getText().trim();
                    game = new DigitalVideoGame(title, price, platform, stock, genre, sizeGB, dl);
                } else {
                    String cond = tfCondition.getText().trim();
                    String dist = tfDistributor.getText().trim();
                    game = new PhysicalVideoGame(title, price, platform, stock, genre, cond, dist);
                }

                gameService.addVideoGame(game);
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Videojuego agregado correctamente.");
                st.close();
            } catch (IllegalStateException ex) {
                // Título duplicado
                showAlert(Alert.AlertType.WARNING, "Duplicado", ex.getMessage());
            } catch (IllegalArgumentException ex) {
                showAlert(Alert.AlertType.ERROR, "Validación", ex.getMessage());
            }
        });

        VBox layout = new VBox(8,
                new Label("Tipo:"), new HBox(10, rbDigital, rbPhysical),
                new Label("Título:"), tfTitle,
                new Label("Precio:"), tfPrice,
                new Label("Plataforma:"), tfPlatform,
                new Label("Stock:"), tfStock,
                new Label("Género:"), tfGenre,
                new Label("Datos específicos:"), extraBox,
                btnGuardar);
        layout.setPadding(new Insets(15));

        st.setScene(new Scene(new ScrollPane(layout), 320, 520));
        st.show();
    }

    //  LISTAR TODOS
    private void showAllGames() {
        List<VideoGame> games = gameService.getAllVideoGames();
        if (games.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Catálogo", "No hay videojuegos registrados.");
            return;
        }
        showGamesTable("Todos los videojuegos", games);
    }

    //  BUSCAR POR TITULO
    private void showSearchByTitle() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Buscar por título");
        dlg.setHeaderText(null);
        dlg.setContentText("Ingrese el título:");
        dlg.showAndWait().ifPresent(title -> {
            VideoGame g = gameService.findByTitle(title);
            if (g == null) {
                showAlert(Alert.AlertType.WARNING, "No encontrado", "No se encontró: " + title);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Resultado", ((Sellable) g).getDisplayInfo());
            }
        });
    }

    // BUSCAR POR PLATAFORMA
    private void showSearchByPlatform() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Buscar por plataforma");
        dlg.setHeaderText(null);
        dlg.setContentText("Ingrese la plataforma:");
        dlg.showAndWait().ifPresent(platform -> {
            List<VideoGame> results = gameService.findByPlatform(platform);
            if (results.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Sin resultados", "No hay juegos para la plataforma: " + platform);
            } else {
                showGamesTable("Juegos en plataforma: " + platform, results);
            }
        });
    }

    // VENDER
    private void showSaleDialog() {
        Stage st = new Stage();
        st.setTitle("Realizar Venta");

        TextField tfTitle = new TextField(); tfTitle.setPromptText("Título del videojuego");
        TextField tfQty   = new TextField(); tfQty.setPromptText("Cantidad");
        Button btnVender  = new Button("Vender");

        btnVender.setOnAction(e -> {
            try {
                String title = tfTitle.getText().trim();
                int qty      = Integer.parseInt(tfQty.getText().trim());
                Sale sale    = saleService.sellVideoGame(title, qty);
                showAlert(Alert.AlertType.INFORMATION, "Venta realizada",
                        "ID: " + sale.getId() + "\nJuego: " + sale.getVideoGame().getTitle()
                        + "\nCantidad: " + sale.getQuantity()
                        + "\nTotal: $" + sale.getTotal());
                st.close();
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "La cantidad debe ser un número entero.");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                showAlert(Alert.AlertType.WARNING, "No se pudo realizar la venta", ex.getMessage());
            }
        });

        VBox layout = new VBox(10, new Label("Título:"), tfTitle, new Label("Cantidad:"), tfQty, btnVender);
        layout.setPadding(new Insets(15));
        st.setScene(new Scene(layout, 280, 200));
        st.show();
    }

    //  VER VENTAS
    private void showSales() {
        List<Sale> sales = saleService.getAllSales();
        if (sales.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Ventas", "No hay ventas registradas.");
            return;
        }

        TableView<Sale> table = new TableView<>();
        TableColumn<Sale, String> colId    = new TableColumn<>("ID");
        TableColumn<Sale, String> colGame  = new TableColumn<>("Juego");
        TableColumn<Sale, Integer> colQty  = new TableColumn<>("Cantidad");
        TableColumn<Sale, Double> colTotal = new TableColumn<>("Total");
        TableColumn<Sale, String> colDate  = new TableColumn<>("Fecha");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        colGame.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getVideoGame().getTitle()));
        colDate.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getSaleDate().toString()));

        table.getColumns().addAll(colId, colGame, colQty, colTotal, colDate);
        table.setItems(FXCollections.observableArrayList(sales));

        Stage st = new Stage();
        st.setTitle("Historial de Ventas");
        st.setScene(new Scene(new BorderPane(table), 620, 300));
        st.show();
    }

    // ---- TABLA GENÉRICA PARA JUEGOS ----
    private void showGamesTable(String title, List<VideoGame> games) {
        TableView<VideoGame> table = new TableView<>();

        TableColumn<VideoGame, String> colTitle    = new TableColumn<>("Título");
        TableColumn<VideoGame, String> colType     = new TableColumn<>("Tipo");
        TableColumn<VideoGame, String> colPlatform = new TableColumn<>("Plataforma");
        TableColumn<VideoGame, String> colGenre    = new TableColumn<>("Género");
        TableColumn<VideoGame, Double> colPrice    = new TableColumn<>("Precio Final");
        TableColumn<VideoGame, Integer> colStock   = new TableColumn<>("Stock");

        colTitle.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTitle()));
        colType.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue() instanceof DigitalVideoGame ? "Digital" : "Físico"));
        colPlatform.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getPlatform()));
        colGenre.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getGenre()));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("calculateFinalPrice"));
        colPrice.setCellValueFactory(d -> new javafx.beans.property.SimpleDoubleProperty(
                d.getValue().calculateFinalPrice()).asObject());
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        table.getColumns().addAll(colTitle, colType, colPlatform, colGenre, colPrice, colStock);
        table.setItems(FXCollections.observableArrayList(games));

        // Botones CRUD debajo de la tabla
        Button btnEliminar   = new Button("Eliminar seleccionado");
        Button btnActualizar = new Button("Actualizar seleccionado");

        btnEliminar.setOnAction(e -> {
            VideoGame selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) { showAlert(Alert.AlertType.WARNING, "Aviso", "Selecciona un juego."); return; }
            try {
                gameService.deleteVideoGame(selected.getTitle());
                table.getItems().remove(selected);
                showAlert(Alert.AlertType.INFORMATION, "Eliminado", "Videojuego eliminado.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        btnActualizar.setOnAction(e -> {
            VideoGame selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) { showAlert(Alert.AlertType.WARNING, "Aviso", "Selecciona un juego."); return; }
            showUpdateDialog(selected, table);
        });

        HBox crudButtons = new HBox(10, btnEliminar, btnActualizar);
        crudButtons.setPadding(new Insets(5));

        BorderPane bp = new BorderPane();
        bp.setCenter(table);
        bp.setBottom(crudButtons);

        Stage st = new Stage();
        st.setTitle(title);
        st.setScene(new Scene(bp, 680, 360));
        st.show();
    }

    // ---- ACTUALIZAR ----
    private void showUpdateDialog(VideoGame game, TableView<VideoGame> table) {
        Stage st = new Stage();
        st.setTitle("Actualizar: " + game.getTitle());

        TextField tfPrice    = new TextField(String.valueOf(game.getPrice()));
        TextField tfPlatform = new TextField(game.getPlatform());
        TextField tfStock    = new TextField(String.valueOf(game.getStock()));
        TextField tfGenre    = new TextField(game.getGenre());

        Button btnSave = new Button("Guardar cambios");
        btnSave.setOnAction(e -> {
            try {
                game.setPrice(Double.parseDouble(tfPrice.getText().trim()));
                game.setPlatform(tfPlatform.getText().trim());
                game.setStock(Integer.parseInt(tfStock.getText().trim()));
                game.setGenre(tfGenre.getText().trim());
                gameService.updateVideoGame(game.getTitle(), game);
                table.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Actualizado", "Videojuego actualizado.");
                st.close();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        VBox layout = new VBox(8,
                new Label("Precio:"), tfPrice,
                new Label("Plataforma:"), tfPlatform,
                new Label("Stock:"), tfStock,
                new Label("Género:"), tfGenre,
                btnSave);
        layout.setPadding(new Insets(15));
        st.setScene(new Scene(layout, 280, 280));
        st.show();
    }

    // ---- HELPER ALERTAS ----
    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
