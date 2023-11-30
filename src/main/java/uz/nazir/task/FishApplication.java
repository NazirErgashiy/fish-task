package uz.nazir.task;

import lombok.extern.slf4j.Slf4j;
import uz.nazir.task.config.ArgumentVariablesConfig;
import uz.nazir.task.service.AquariumRunner;

import static uz.nazir.task.config.ConsoleColorsConfig.ANSI_RESET;
import static uz.nazir.task.config.ConsoleColorsConfig.ANSI_YELLOW;

@Slf4j
public class FishApplication {

    public static void main(String[] args) {
        try {
            ArgumentVariablesConfig.MAX_RANDOM_VALUE = Integer.parseInt(args[0]);
            ArgumentVariablesConfig.FISH_LOCATION_MAX_RANDOM_VALUE = Integer.parseInt(args[1]);
            ArgumentVariablesConfig.FISH_LIFETIME_MAX_RANDOM_VALUE = Integer.parseInt(args[2]);
            ArgumentVariablesConfig.TIME_TO_BECOME_ADULT_MAX_RANDOM_VALUE = Integer.parseInt(args[3]);
            ArgumentVariablesConfig.MALES_START_MAX_RANDOM_VALUE = Integer.parseInt(args[4]);
            ArgumentVariablesConfig.FEMALES_START_MAX_RANDOM_VALUE = Integer.parseInt(args[5]);
            ArgumentVariablesConfig.AQUARIUM_CAPACITY_MAX_RANDOM_VALUE = Integer.parseInt(args[6]);
        } catch (ArrayIndexOutOfBoundsException exception) {
            log.warn(ANSI_YELLOW + "Arguments are not configured! Running with default settings" + ANSI_RESET);
        }

        AquariumRunner aquariumRunner = new AquariumRunner();//Aquarium environment initializr
        aquariumRunner.startEmulation();
        while (true) ;//Keeping main thread alive
    }
}