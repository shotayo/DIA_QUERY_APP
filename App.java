import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

public class App {

    static class DataPoint {
        private String price;
        private LocalDateTime timestamp;

        public DataPoint(String price, LocalDateTime timestamp) {
            this.price = price;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "price=" + price + ", timestamp=" + timestamp;
        }
    }

    public static void main(String[] args) {

        String apiKey = System.getenv("TWELVE_DATA_API_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("API key not found.");
            return;
        }

        Queue<DataPoint> queue = new LinkedList<>();

        try {
            while (true) {

                String endpoint =
                    "https://api.twelvedata.com/price?symbol=DIA&apikey="
                    + apiKey;

                URL url = new URL(endpoint);
                HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
                );

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                String json = response.toString();

                String price = "Unknown";

                if (json.contains("\"price\":\"")) {
                    int start = json.indexOf("\"price\":\"") + 9;
                    int end = json.indexOf("\"", start);
                    price = json.substring(start, end);
                }

                LocalDateTime timestamp = LocalDateTime.now();

                DataPoint dataPoint = new DataPoint(price, timestamp);

                queue.add(dataPoint);

                System.out.println(
                    "Added data point: " + dataPoint
                );

                System.out.println(
                    "Current queue size: " + queue.size()
                );

                Thread.sleep(15000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}