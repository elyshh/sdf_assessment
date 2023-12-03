package sdf;

public class App {
    
    private String app_name;
    private String category;
    private String rating;
    public String getRating;
    
    public App(String app_name, String category, String rating) {
        this.app_name = app_name;
        this.category = category.toUpperCase();
        this.rating = rating;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getApp_name() { return app_name; }
    public void setApp_name(String app_name) { this.app_name = app_name; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

}
