package uz.nazir.task.service;

import lombok.extern.slf4j.Slf4j;
import uz.nazir.task.entities.Aquarium;
import uz.nazir.task.entities.thread.Fish;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static uz.nazir.task.config.ConsoleColorsConfig.*;
import static uz.nazir.task.config.ArgumentVariablesConfig.*;

@Slf4j
public class AquariumManager {

    private final Aquarium aquarium;

    private final AtomicInteger checks;
    private final Date applicationStartedDate;
    private final Instant start;

    public AquariumManager(Aquarium aquarium) {
        this.aquarium = aquarium;
        applicationStartedDate = new Date();
        start = Instant.now();
        checks = new AtomicInteger(0);
    }

    public synchronized void checkStep() {
        checks.set(checks.incrementAndGet());
        List<Fish> fishes = aquarium.getFishes();

        int alive = 0;
        for (int i = 0; i < fishes.size() - 1; i++) {
            Fish fish1 = fishes.get(i);

            if (fish1.getLifeTime() > 0) alive++;
            if (alive >= aquarium.getCapacity()) {
                break;
            }

            for (int j = 1; j < fishes.size(); j++) {
                Fish fish2 = fishes.get(j);
                if (isFishCanPopulate(fish1, fish2)) {
                    populateFish();
                    log.info(ANSI_GREEN + "New fish parents ->" + fish1 + fish2 + ANSI_RESET);
                    fish1.setLocationXYZ(new Random().nextInt(FISH_LOCATION_MAX_RANDOM_VALUE));
                    fish2.setLocationXYZ(new Random().nextInt(FISH_LOCATION_MAX_RANDOM_VALUE));
                }
            }
        }

        int aliveFishesCount = 0;
        int deadFishesCount = 0;
        for (Fish fish : fishes) {
            if (fish.getLifeTime() > 0) {
                aliveFishesCount++;
            }
            if (fish.getLifeTime() <= 0) {
                deadFishesCount++;
            }
        }

        if (aliveFishesCount >= aquarium.getCapacity()) {
            printWall();
            log.info(ANSI_BLUE + "Aquarium stopped! Cause: AQUARIUM IS FULL" + ANSI_RESET);
            printStats(aliveFishesCount, deadFishesCount);
            System.exit(0);
        }

        boolean isAllFishesDead = true;
        for (Fish fish : fishes) {
            if (fish.getLifeTime() > 0) {
                isAllFishesDead = false;
                break;
            }
        }

        if (isAllFishesDead) {
            printWall();
            log.info(ANSI_BLUE + "Aquarium stopped! Cause: ALL FISHES IS DEAD" + ANSI_RESET);
            printStats(aliveFishesCount, deadFishesCount);
            System.exit(0);
        }
    }

    private synchronized void populateFish() {
        Fish bornFish = Fish.builder()
                .manager(this)
                .strategy("born")
                .build();

        aquarium.getFishes().add(bornFish);
        new Thread(bornFish).start();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //log.info("New fish info ->" + bornFish);
    }

    private synchronized boolean isFishCanPopulate(Fish fish1, Fish fish2) {
        if (fish1.getLifeTime() > 0 && fish2.getLifeTime() > 0) {
            if (fish1.isAdult() && fish2.isAdult()) {
                if (fish1.getLocationXYZ() == fish2.getLocationXYZ()) {
                    if (fish1.isGender() != fish2.isGender()) {
                        return !fish1.isGender();
                    }
                }
            }
        }
        return false;
    }

    private void printStats(int aliveFishesCount, int deadFishesCount) {
        Date applicationEndedDate = new Date();
        Instant end = Instant.now();

        int malesStartCount = 0;
        int femalesStartCount = 0;
        int bornFishes = 0;
        int totalMalesCount = 0;
        int totalFemalesCount = 0;

        for (Fish fish : aquarium.getFishes()) {
            if (fish.getStrategy() == "init" && fish.isGender()) {
                malesStartCount++;
            }
            if (fish.getStrategy() == "init" && !fish.isGender()) {
                femalesStartCount++;
            }
            if (fish.getStrategy() == "born") {
                bornFishes++;
            }
            if (fish.isGender()) {
                totalMalesCount++;
            } else {
                totalFemalesCount++;
            }

        }

        printWall();
        log.info(ANSI_BLUE + "Total fishes = " + aquarium.getFishes().size() + ANSI_RESET);
        log.info(ANSI_BLUE + "Total alive fishes = " + aliveFishesCount + ANSI_RESET);
        log.info(ANSI_BLUE + "Total dead fishes = " + deadFishesCount + ANSI_RESET);
        log.info(ANSI_BLUE + "Total born fishes = " + bornFishes + ANSI_RESET);
        log.info(ANSI_BLUE + "Total male   fishes = " + totalMalesCount + ANSI_RESET);
        log.info(ANSI_BLUE + "Total female fishes = " + totalFemalesCount + ANSI_RESET);
        log.info(ANSI_BLUE + "Total checks = " + checks.get() + ANSI_RESET);
        printWall();
        log.info(ANSI_BLUE + "Aquarium height = " + aquarium.getHeight() + ANSI_RESET);
        log.info(ANSI_BLUE + "Aquarium width = " + aquarium.getWidth() + ANSI_RESET);
        log.info(ANSI_BLUE + "Aquarium depth = " + aquarium.getDepth() + ANSI_RESET);
        log.info(ANSI_BLUE + "Aquarium capacity = " + aquarium.getCapacity() + ANSI_RESET);
        log.info(ANSI_BLUE + "Male start count = " + malesStartCount + ANSI_RESET);
        log.info(ANSI_BLUE + "Female start count = " + femalesStartCount + ANSI_RESET);
        printWall();
        log.info(ANSI_YELLOW + "Argument variables" + ANSI_RESET);
        log.info(ANSI_YELLOW + "MAX_RANDOM_VALUE = " + MAX_RANDOM_VALUE + ANSI_RESET);
        log.info(ANSI_YELLOW + "FISH_LOCATION_MAX_RANDOM_VALUE = " + FISH_LOCATION_MAX_RANDOM_VALUE + ANSI_RESET);
        log.info(ANSI_YELLOW + "FISH_LIFETIME_MAX_RANDOM_VALUE = " + FISH_LIFETIME_MAX_RANDOM_VALUE + ANSI_RESET);
        log.info(ANSI_YELLOW + "TIME_TO_BECOME_ADULT_MAX_RANDOM_VALUE = " + TIME_TO_BECOME_ADULT_MAX_RANDOM_VALUE + ANSI_RESET);
        log.info(ANSI_YELLOW + "MALES_START_MAX_RANDOM_VALUE = " + MALES_START_MAX_RANDOM_VALUE + ANSI_RESET);
        log.info(ANSI_YELLOW + "FEMALES_START_MAX_RANDOM_VALUE = " + FEMALES_START_MAX_RANDOM_VALUE + ANSI_RESET);
        log.info(ANSI_YELLOW + "AQUARIUM_CAPACITY_MAX_RANDOM_VALUE = " + AQUARIUM_CAPACITY_MAX_RANDOM_VALUE + ANSI_RESET);
        printWall();
        log.info(ANSI_CYAN + "Additional info" + ANSI_RESET);
        log.info(ANSI_CYAN + "Application started date = " + applicationStartedDate + ANSI_RESET);
        log.info(ANSI_CYAN + "Application finished date = " + applicationEndedDate + ANSI_RESET);
        log.info(ANSI_CYAN + "Application elapsed time = " + Duration.between(start, end).toMinutes() + "m " + Duration.between(start, end).toSeconds() + "s " + Duration.between(start, end).toMillis() + "ms" + ANSI_RESET);
        printWall();
    }

    private void printWall() {
        log.info(ANSI_PURPLE + "********** ********** ********** **********" + ANSI_RESET);
    }
}
