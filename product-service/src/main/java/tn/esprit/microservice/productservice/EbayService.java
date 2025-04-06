package tn.esprit.microservice.productservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class EbayService {

    private static final String EBAY_API_ENDPOINT = "https://api.sandbox.ebay.com/buy/browse/v1/item_summary/search?q=%s&limit=5";

    @Autowired
    private TokenService tokenService;

    public List<ComparisonProduct> fetchProducts(String query) {
        List<ComparisonProduct> results = new ArrayList<>();
        try {
            String apiUrl = String.format(EBAY_API_ENDPOINT, query.replace(" ", "+"));
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.setRequestProperty("Authorization", "Bearer " + tokenService.getAccessToken());
            conn.setRequestProperty("Accept", "application/json");

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) response.append(scanner.nextLine());
            scanner.close();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.toString());
            JsonNode items = root.get("itemSummaries");

            if (items != null && items.isArray()) {
                for (JsonNode item : items) {
                    String title = item.path("title").asText();
                    double price = item.path("price").path("value").asDouble();
                    String currency = item.path("price").path("currency").asText();
                    String imageUrl = item.path("image").path("imageUrl").asText();
                    String itemLink = item.path("itemWebUrl").asText();

                    results.add(new ComparisonProduct(
                            title,
                            price,
                            currency,
                            imageUrl,
                            "eBay",
                            itemLink
                    ));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
}
