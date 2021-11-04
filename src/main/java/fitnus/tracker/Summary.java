// @@author siyuancheng178
package fitnus.tracker;

import fitnus.database.EntryDatabase;
import fitnus.exception.FitNusException;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.time.Period;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Summary {
    private static final int UNIT_PER_SQUARE = 100;
    private static final String SQUARE = "#";
    private final ArrayList<Entry> entries;
    private int days;

    public Summary(EntryDatabase ed, int days) {
        ed.sortDatabase();
        this.entries = ed.getEntries();
        if (entries.size() == 0) {
            days = 1;
        } else {
            LocalDate firstUseDate = entries.get(0).getRawDate();
            int period = firstUseDate.until(LocalDate.now()).getDays() + 1;
            if (period >= days) {
                this.days = days;
            } else {
                this.days = period;
            }
        }
    }

    private String getMostAndLeastEatenFood() {
        HashMap<String, Integer> occurrence = new HashMap<>();

        for (Entry i : entries) {
            String foodName = i.getFood().getName();
            occurrence.compute(foodName, (key, val) -> {
                if (val == null) {
                    return 1;
                }
                return val + 1;
            });
        }

        ArrayList<String> mostFrequentFoods = new ArrayList<>();
        ArrayList<String> leastFrequentFoods = new ArrayList<>();
        int maxOccurrence = 0;
        int minOccurrence = entries.size();

        // Iterates through hashmap entries to find most frequent food
        for (Map.Entry<String, Integer> e : occurrence.entrySet()) {
            if (e.getValue() > maxOccurrence) {
                // Update max val and most freq foods
                maxOccurrence = e.getValue();
                mostFrequentFoods.clear();
                mostFrequentFoods.add(e.getKey());
            } else if (e.getValue() == maxOccurrence) {
                // Add food to most freq foods
                mostFrequentFoods.add(e.getKey());
            }
        }
        // Iterates through hashmap entries to find least frequent food
        for (Map.Entry<String, Integer> e : occurrence.entrySet()) {
            if (e.getValue() < minOccurrence) {
                // Update min val and least freq foods
                minOccurrence = e.getValue();
                leastFrequentFoods.clear();
                leastFrequentFoods.add(e.getKey());
            } else if (e.getValue() == minOccurrence) {
                // Add food to least freq foods
                leastFrequentFoods.add(e.getKey());
            }
        }
        mostFrequentFoods.sort(String::compareTo);
        leastFrequentFoods.sort(String::compareTo);
        return String.format("Food eaten most: %s [%d time(s)]\n"
                        + "Food eaten least: %s [%d time(s)]",
                mostFrequentFoods, maxOccurrence,
                leastFrequentFoods, minOccurrence);
    }

    private int getAverageCalories() {
        int totalCalories = 0;
        int totalNumEntries = entries.size();

        for (Entry e : entries) {
            totalCalories += e.getFood().getCalories();
        }

        return totalCalories / days;
    }

    private static String drawGraphSquares(int calorie, int unit) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < calorie / unit; i++) {
            builder.append(SQUARE);
        }
        return builder.toString();
    }

    private String getWeekCalorieTrendGraph() {
        StringBuilder output = new StringBuilder();
        LocalDate date;
        if (this.days < 6) {
            date = LocalDate.now().minusDays(this.days - 1);
        } else {
            date = LocalDate.now().minusDays(6);
        }


        int calories = 0;
        int j = 0;
        for (int i = 0; i < entries.size(); i++) {
            if (!entries.get(i).getRawDate().isBefore(date)) {
                j = i;
                break;
            }
        }


        do {
            if (j < entries.size() && entries.get(j).getRawDate().equals(date)) {
                calories += entries.get(j).getFood().getCalories();
                j++;
            } else {
                output.append(String.format("%s: %s %d\n", date,
                        drawGraphSquares(calories, UNIT_PER_SQUARE), calories));
                date = date.plusDays(1);
                calories = 0;
            }
        } while (!date.isAfter(LocalDate.now()));
        return output.toString();
    }


    /**
     * This function generates a report based on the calorie intake over the past seven days.
     * Report includes the calorie intake trend graph, weekly average calorie intake and the
     * most/least frequently eaten food.
     *
     * @return String a report of weekly calorie intake
     */
    public String generateWeekSummaryReport() {
        if (entries.size() < 1) {
            return "No entries found!";
        }

        int averageCalories = getAverageCalories();
        String output = String.format(getWeekCalorieTrendGraph()
                + "Average Daily Calorie Intake: %s %d\n"
                + getMostAndLeastEatenFood(), drawGraphSquares(averageCalories, UNIT_PER_SQUARE), averageCalories
        );

        return output;
    }

    /**
     * This function generates a report based on the calorie intake over current month.
     * Report includes monthly average calorie intake and the most/least frequently eaten food.
     *
     * @return String a report of monthly calorie intake
     */
    public String generateMonthSummaryReport() {
        if (entries.size() < 1) {
            return "No entries found!";
        }

        String output = String.format("Average Daily Calorie Intake: %d\n"
                        + getMostAndLeastEatenFood(),
                getAverageCalories());
        return output;
    }
}
