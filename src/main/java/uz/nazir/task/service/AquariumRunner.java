package uz.nazir.task.service;

import uz.nazir.task.entities.Aquarium;
import uz.nazir.task.entities.thread.Fish;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static uz.nazir.task.config.ArgumentVariablesConfig.*;

public class AquariumRunner {

    private Aquarium aquarium;
    private AquariumManager manager;
    private int malesStartCount = getRandomInt(MALES_START_MAX_RANDOM_VALUE);
    private int femalesStartCount = getRandomInt(FEMALES_START_MAX_RANDOM_VALUE);

    public AquariumRunner() {
        aquarium = new Aquarium();
        manager = new AquariumManager(aquarium);

        aquarium.setHeight(getRandomInt(MAX_RANDOM_VALUE));
        aquarium.setWidth(getRandomInt(MAX_RANDOM_VALUE));
        aquarium.setDepth(getRandomInt(MAX_RANDOM_VALUE));
        aquarium.setCapacity(getRandomInt(AQUARIUM_CAPACITY_MAX_RANDOM_VALUE));

        List<Fish> startFishesList = new CopyOnWriteArrayList<>();
        addFishes(startFishesList, true, malesStartCount);
        addFishes(startFishesList, false, femalesStartCount);

        aquarium.setFishes(startFishesList);
    }

    public void startEmulation() {
        for (Fish fish : aquarium.getFishes()) {
            new Thread(fish).start();
        }
        manager.checkStep();
    }

    private void addFishes(List<Fish> fishes, boolean gender, int count) {
        for (int i = 0; i < count; i++) {
            Fish fish = Fish.builder()
                    .manager(manager)
                    .isAdult(true)
                    .gender(gender)
                    .strategy("init")
                    .build();

            fishes.add(fish);
        }
    }

    private int getRandomInt(int max) {
        return new Random().nextInt(max);
    }
}
