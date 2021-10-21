package fitnus.command;

import fitnus.database.EntryDatabase;
import fitnus.database.FoodDatabase;
import fitnus.utility.User;

public class HelpCommand extends Command {
    public HelpCommand() {
        super();
    }

    @Override
    public String execute(EntryDatabase ed, FoodDatabase fd, User us) {
        return "------------------ \n"
                + "Here are some commands that you can use!\n"
                + "------------------ \n"
                + "========== ADD/EDIT/REMOVE ==========\n"
                + "[X] Add entry: add /MEALTYPE FOOD_NAME (MEALTYPE - /bfast, /lunch, /dinner, /snack)\n"
                + "[X] Edit existing entry: edit INDEX_OF_FOOD FOOD_NAME\n"
                + "[X] Delete entry: remove /entry INDEX_OF_FOOD\n"
                + "[X] Delete preset food: remove /food INDEX_OF_FOOD\n\n"
                + "=============== LIST ================\n"
                + "[X] List foods in database: list /food\n"
                + "[X] List out entries for the day: list /entry\n"
                + "[X] List past records of weight: list /weight\n\n"
                + "============== SEARCH ===============\n"
                + "[X] Search food with keyword: find /food KEYWORD\n"
                + "[X] Search entry with keyword: find /entry KEYWORD\n\n"
                + "=============== USER ================\n"
                + "[X] Set Gender: gender /set M/F (Select one)\n"
                + "[X] Set Weight: weight /set WEIGHT\n"
                + "[X] Set calorie goal: calorie /set GOAL\n"
                + "[X] View remaining calories for the day: calorie /remain\n"
                + "[X] Auto generate calorie goal: calorie /generate\n\n"
                + "============== OTHERS ===============\n"
                + "[X] List out available commands: help\n"
                + "[X] Show summary: summary /TIMEFRAME (/week, /month)\n"
                + "[X] Recommend food to eat: suggest\n"
                + "[X] Exit FitNUS: exit\n"
                + "------------------ \n";
    }
}
