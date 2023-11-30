package uz.nazir.task.entities;

import lombok.Data;
import uz.nazir.task.entities.thread.Fish;

import java.util.List;

@Data
public class Aquarium {

    private int height;
    private int width;
    private int depth;
    private int capacity;
    private List<Fish> fishes;
}
