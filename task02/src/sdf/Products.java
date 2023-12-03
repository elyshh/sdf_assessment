package sdf;

import java.util.LinkedList;
import java.util.List;

public class Products {
    
    private int prod_id;
    private String title;
    private float price;
    private float rating;

    public Products(int prod_id, String title, float price, float rating) {
        this.prod_id = prod_id;
        this.title = title;
        this.price = price;
        this.rating = rating;
    }

    public int getProd_id() { return prod_id; }
    public String getTitle() { return title; }
    public float getPrice() { return price; }
    public float getRating() { return rating; }

    // Methods
    public static List<String> processBudget(List<Products> sortedList, float budget) {
        float usedBudget = 0f;
        List<String> items = new LinkedList<>();

        for (int i = 0; i < sortedList.size(); i++) {
            if (sortedList.get(i).getPrice() + usedBudget <= budget) {
                items.add(Integer.toString(sortedList.get(i).prod_id));
                usedBudget += sortedList.get(i).getPrice();
            }
            else {
                break;
            }
        }
        return items;
    }

    public static float getUsedBudget(List<String> list, List<Products> sortedList) {
        float usedBudget = 0f;
        for (String item : list) {
            int a = Integer.parseInt(item);
            for (int i = 0; i < sortedList.size(); i++) {
                if (sortedList.get(i).getProd_id() == a) {
                    usedBudget += sortedList.get(i).getPrice();
                }
            }
        }
        return usedBudget;
        
    }

    public static float getRemainingBudget(float budget, float usedBudget) {
        return budget - usedBudget;
    }
    
}
