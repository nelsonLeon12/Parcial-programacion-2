package gamezone.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import gamezone.entities.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String GAMES_FILE = "data/videogames.json";
    private static final String SALES_FILE = "data/sales.json";

    // ---- VideoGames ----

    public static List<VideoGame> loadGames() {
        File file = new File(GAMES_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
            List<VideoGame> games = new ArrayList<>();
            for (JsonElement el : array) {
                JsonObject obj = el.getAsJsonObject();
                String type = obj.get("type").getAsString();
                if ("digital".equals(type)) {
                    games.add(gson.fromJson(obj, DigitalVideoGame.class));
                } else {
                    games.add(gson.fromJson(obj, PhysicalVideoGame.class));
                }
            }
            return games;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void saveGames(List<VideoGame> games) {
        new File("data").mkdirs();
        try (Writer writer = new FileWriter(GAMES_FILE)) {
            JsonArray array = new JsonArray();
            for (VideoGame g : games) {
                JsonObject obj = gson.toJsonTree(g).getAsJsonObject();
                obj.addProperty("type", g instanceof DigitalVideoGame ? "digital" : "physical");
                array.add(obj);
            }
            gson.toJson(array, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---- Sales ----

    public static List<SaleData> loadSales() {
        File file = new File(SALES_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<SaleData>>(){}.getType();
            List<SaleData> list = gson.fromJson(reader, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void saveSales(List<SaleData> sales) {
        new File("data").mkdirs();
        try (Writer writer = new FileWriter(SALES_FILE)) {
            gson.toJson(sales, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // DTO simple para persistir ventas sin referencia circular
    public static class SaleData {
        public String id;
        public String gameTitle;
        public int quantity;
        public double unitPrice;
        public double total;
        public String saleDate;
    }
}
