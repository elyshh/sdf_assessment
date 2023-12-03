package sdf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static sdf.Constants.*;

public class Client {

    private Socket socket;

    public Client(Socket client) {
        this.socket = client;
    }
    
    // java -jar ./src/sdf/task02-server.jar
    public void start() throws Exception {

        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter ows = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(ows);

        List<String> x = new LinkedList<>();
        List<Products> prodList = new ArrayList<>();

        String line;
        String request_id = null;
        int item_count = -1;
        float budget = 0f;
        Products p = null;

        while (true) {
            if (item_count == 0) {
                break;
            }

            line = br.readLine().trim();

            // Print information received from server
            System.out.printf("> %s\n", line); // Optional
            x.add(line);

            if (line.equals(PROD_LIST) || line.length() <= 0) {
                continue;
            }
            else if (line.startsWith(REQUEST_ID)) {
                request_id = splitLine(line);
            }
            else if (line.contains(ITEM_COUNT)) {
                item_count = Integer.parseInt(splitLine(line)); // Actual count
            }
            else if(line.contains(BUDGET)) {
                budget = Float.parseFloat(splitLine(line));
            }
            else if(line.equals(PROD_START)) {
                int prod_id = 0;
                String title = null;
                float price = 0f;
                float rating = 0f;

                while(true) {
                    line = br.readLine().trim();
                    System.out.printf("> %s\n", line); // Optional
                    if (line.length() <= 0) {
                        continue;
                    }
                    else if (line.startsWith(PROD_ID)) {
                        prod_id = Integer.parseInt(splitLine(line).trim());
                    }
                    else if (line.startsWith(TITLE)) {
                        title = splitLine(line);
                    }
                    else if (line.startsWith(PRICE)) {
                        price = Float.parseFloat(splitLine(line));
                    }
                    else if (line.startsWith(RATING)) {
                        rating = Float.parseFloat(splitLine(line));
                    }
                    else if (line.startsWith(PROD_END)) {
                        item_count--;
                        break;
                    }
                }
                prodList.add(new Products(prod_id, title, price, rating));

            }
        }

        // Sort highest rating followed by highest priced
        List<Products> sortedList = prodList.stream()
            .sorted(Comparator.comparing(Products::getRating)
                .reversed()
                .thenComparing(Comparator.comparing(Products::getPrice)
                    .reversed()))
            .collect(Collectors.toList());

        // Calculate budget and compile items
        List<String> processed_list = Products.processBudget(sortedList, budget);
        String items = String.join(",", processed_list);
        float usedBudget = Products.getUsedBudget(processed_list, sortedList);
        float remainingBudget = Products.getRemainingBudget(budget, usedBudget);

        // For debugging
        System.out.println("\nPrinting request_id: " + request_id);
        System.out.println("Printing items: " + items);
        System.out.println("Printing budget: " + budget);
        System.out.println("Printing used budget: " + usedBudget);
        System.out.println("Printing remaining budget: " + remainingBudget);

        bw.write("request_id: " + request_id + "\n");
        bw.write("name: elysia s\n");
        bw.write("email: elysialshun@gmail.com\n");
        bw.write("items: " + items + "\n");
        bw.write("spent: " + usedBudget + "\n");
        bw.write("remaining: " + remainingBudget + "\n");
        bw.write("client_end\n");

        bw.flush();

        line = br.readLine().trim();
        System.out.printf("\nResult: %s\n", line);

        is.close();
        os.close();

        socket.close();
        System.out.println("Socket closed");

    }

    private static String splitLine(String line) {
        return line.split(": ")[1];
    }
    
}
