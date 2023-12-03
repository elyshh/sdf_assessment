package sdf.task01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static final int COL_APP = 0;
    public static final int COL_CAT = 1;
    public static final int COL_RATING = 2;
    
    public static void main(String[] args) throws Exception {

        if (args.length <= 0) {
            System.err.println("Missing googleplaystore.csv");
            System.exit(1);
        }
        
        System.out.printf("Processing %s\n", args[0]);
        
        int lines = 0;
        try (FileReader fr = new FileReader(args[0])) {
            BufferedReader br = new BufferedReader(fr);

            while (br.readLine() != null) {
                lines++;
            }
        }
        
        try (FileReader fr = new FileReader(args[0])) {
            BufferedReader br = new BufferedReader(fr);

            Map<String, List<App>> categorized = br.lines()
                .skip(1)
                .map(row -> row.trim().split(","))
                // String[] -> App object
                .map(fields -> new App(fields[COL_APP], fields[COL_CAT], fields[COL_RATING]))
                // groupingBy -> returns a value that classifies the input
                .collect(Collectors.groupingBy(app -> app.getCategory()));

            for (String category: categorized.keySet()) {
                List<App> apps = categorized.get(category);
                System.out.printf("\nCategory: %s\n", category);

                int discarded_count = 0;
                String max_appname = null;
                String min_appname = null;
                String initial_appname = null;
                float min_rating = 0;
                float max_rating = 0;
                float sum = 0;
                int count = 0;
                float avg_rating = 0;

                for (int i = 0; i < apps.size(); i++) {
                    if (apps.get(i).getRating().contains("NaN")) {
                        discarded_count += 1;
                    }

                    else {
                        if (i == 0) {
                            max_rating = Float.parseFloat(apps.get(0).getRating());
                            min_rating = Float.parseFloat(apps.get(0).getRating());
                            initial_appname = apps.get(0).getApp_name();
                            max_appname = initial_appname;
                            min_appname = initial_appname;
                            count += 1;
                            sum += max_rating;
                        }

                        else if (i > 0) {
                            Float rating = Float.parseFloat(apps.get(i).getRating());
                            if (rating < min_rating) {
                                min_rating = rating;
                                min_appname = apps.get(i).getApp_name();
                            }
                            if (rating > max_rating) {
                                max_rating = rating;
                                max_appname = apps.get(i).getApp_name();
                            }
                            count += 1;
                            sum += rating;
                            
                        }
                    }
                }

                avg_rating = sum / count;
                
                System.out.printf("\tHighest: %s, %.2f\n", max_appname, max_rating);
                System.out.printf("\tLowest: %s, %.2f\n", min_appname, min_rating);
                System.out.printf("\tAverage: %.2f\n", avg_rating);
                System.out.printf("\tCount: %d\n", apps.size());
                System.out.printf("\tDiscarded: %d\n", discarded_count);

            }
            br.close();

        }
        System.out.printf("\nTotal lines in file: %d\n", lines);

    }
}

