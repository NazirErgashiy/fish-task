package uz.nazir.task.entities.thread;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import uz.nazir.task.service.AquariumManager;

import java.util.Random;

import static uz.nazir.task.config.ConsoleColorsConfig.ANSI_RED;
import static uz.nazir.task.config.ConsoleColorsConfig.ANSI_RESET;
import static uz.nazir.task.config.ArgumentVariablesConfig.*;

@Getter
@Setter
@Slf4j
public class Fish implements Runnable {

    private AquariumManager manager;
    private String strategy;
    private int height;
    private int width;
    private int depth;
    private boolean gender;//Values: [true -> Male] [false -> Female]
    private volatile boolean isAdult;
    private volatile int lifeTime;
    //Simplified
    private volatile int locationXYZ;

    @Builder
    public Fish(AquariumManager manager, String strategy, boolean isAdult, boolean gender) {
        this.manager = manager;
        this.strategy = strategy;
        this.isAdult = isAdult;
        this.gender = gender;
    }

    @Override
    public void run() {
        randomSize();
        //You can add your custom strategy
        switch (strategy) {
            case "init" -> {
                lifeTime = getRandomInt(FISH_LIFETIME_MAX_RANDOM_VALUE);
                locationXYZ = getRandomInt(FISH_LOCATION_MAX_RANDOM_VALUE);
            }
            case "born" -> {
                lifeTime = getRandomInt(FISH_LIFETIME_MAX_RANDOM_VALUE);
                locationXYZ = getRandomInt(FISH_LOCATION_MAX_RANDOM_VALUE);
                gender = new Random().nextBoolean();
                isAdult = false;
            }
            default -> {
                lifeTime = getRandomInt(FISH_LIFETIME_MAX_RANDOM_VALUE);
                locationXYZ = getRandomInt(FISH_LOCATION_MAX_RANDOM_VALUE);
                gender = new Random().nextBoolean();
                isAdult = new Random().nextBoolean();
            }
        }

        emulateLife();
        //log.info("THREAD ENDED");
    }

    private void emulateLife() {
        int timeToBecomeAdult = getRandomInt(TIME_TO_BECOME_ADULT_MAX_RANDOM_VALUE);
        while (lifeTime > 0) {
            locationXYZ = getRandomInt(FISH_LOCATION_MAX_RANDOM_VALUE);

            if (!isAdult) {
                timeToBecomeAdult -= 1;
            }
            if (timeToBecomeAdult == 0) {
                isAdult = true;
            }

            log.info(this.toString());

            //kill fish if life is over
            if (lifeTime == 1) {
                log.info(ANSI_RED + "Fish is dead -> " + this.toString() + ANSI_RESET);
                lifeTime = 0;
            }
            lifeTime -= 1;
            manager.checkStep();
        }
        /*
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
         */
    }

    private void randomSize() {
        height = getRandomInt();
        width = getRandomInt();
        depth = getRandomInt();
    }

    private int getRandomInt() {
        return new Random().nextInt(MAX_RANDOM_VALUE);
    }

    private int getRandomInt(int max) {
        return new Random().nextInt(max);
    }

    @Override
    public String toString() {
        return "Fish[" +
                "strategy=" + strategy +
                " height=" + height +
                " width=" + width +
                " depth=" + depth +
                " isAdult=" + isAdult +
                " isGender=" + gender +
                " locationXYZ=" + locationXYZ +
                " lifeTime=" + lifeTime +
                "]";
    }
}
