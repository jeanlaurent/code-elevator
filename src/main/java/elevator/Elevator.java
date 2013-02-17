package elevator;

import java.util.Observable;
import java.util.Observer;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.ElevatorState.CLOSE;
import static elevator.ElevatorState.OPEN;

public class Elevator implements Observer {

    private static final Integer maxStage = 5;

    private final Commands commands = new Commands(0, maxStage);

    private Integer stage = 0;
    private Direction direction;
    private ElevatorState state = CLOSE;

    public ElevatorState state() {
        return state;
    }

    public Integer stage() {
        return stage;
    }

    public Elevator call(Integer atStage, Direction to) {
        commands.add(new Command(atStage, to));
        return this;
    }

    public Elevator go(Integer stageToGo) {
        final Direction direction;
        if (stage > stageToGo) {
            direction = DOWN;
        } else {
            direction = UP;
        }
        call(stageToGo, direction);
        return this;
    }

    @Override
    public void update(Observable clock, Object arg) {
        Command nextCommand;
        if (direction == null) {
            nextCommand = commands.get(stage);
        } else {
            nextCommand = commands.get(stage, direction);
        }
        if (nextCommand == null) {
            state = CLOSE;
            direction = null;
            return;
        }

        if (state == OPEN) {
            state = CLOSE;
            direction = null;
            return;
        }

        if (nextCommand.stage.equals(stage)) {
            direction = nextCommand.direction;
        } else if (nextCommand.stage < stage) {
            direction = DOWN;
        } else {
            direction = UP;
        }

        if (nextCommand.equals(new Command(stage, direction)) || (nextCommand.stage.equals(stage) && commands.commands().isEmpty())) {
            state = OPEN;
            direction = null;
            return;
        }

        if (direction == UP) {
            stage++;
            direction = UP;
            return;
        }

        if (direction == DOWN) { // stage > nextStage.stage
            stage--;
            direction = DOWN;
        }
    }

    @Override
    public String toString() {
        return "elevator " + (direction == null ? state : direction) + " " + stage;
    }

}
