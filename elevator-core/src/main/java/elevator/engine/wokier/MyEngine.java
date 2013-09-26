package elevator.engine.wokier;

import java.util.*;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import elevator.Command;
import elevator.Direction;
import elevator.User;
import elevator.engine.ElevatorEngine;
import elevator.exception.ElevatorIsBrokenException;

/**
 * @author francois wauquier
 */
public class MyEngine implements ElevatorEngine {

	int currentFloor = LOWER_FLOOR;
	int usersIn;
	Set<Integer> floorsToPick = new LinkedHashSet<>();
	Set<Integer> floorsToGo = new HashSet<>();
	Set<Integer> possibleFloorsToGo = new HashSet<>();
	boolean open;

	@Override
	public ElevatorEngine call(Integer atFloor, Direction to) throws ElevatorIsBrokenException {
		floorsToPick.add(atFloor);
		possibleFloorsToGo.addAll(range(atFloor, to));
		return null;
	}

	private Collection<Integer> range(Integer atFloor, Direction to) {
		Collection<Integer> floors = new ArrayList<>();
		for (int floor = LOWER_FLOOR; floor <= HIGHER_FLOOR; floor++) {
			if (floor < atFloor && to.equals(Direction.DOWN)) {
				floors.add(floor);
			}
			if (floor > atFloor && to.equals(Direction.UP)) {
				floors.add(floor);
			}
		}
		return floors;
	}

	@Override
	public ElevatorEngine go(Integer floorToGo) throws ElevatorIsBrokenException {
		floorsToGo.add(floorToGo);
		return null;
	}

	@Override
	public Command nextCommand() throws ElevatorIsBrokenException {
		if (floorsToGo.contains(currentFloor) || floorsToPick.contains(currentFloor)  && !open) {
			floorsToPick.remove(currentFloor);
			floorsToGo.remove(currentFloor);
			possibleFloorsToGo.remove(currentFloor);
			open = true;
			return Command.OPEN;
		}
		if (open && usersIn > 0) {
			open = false;
			return Command.CLOSE;
		}
        int uppers = upperFloorsToGoSize();
        int downers = downerFloorsToGoSize();
        if (uppers > downers) {
			currentFloor++;
			return Command.UP;
		}
        if (uppers < downers) {
            currentFloor--;
            return Command.DOWN;
        }
		return Command.NOTHING;

	}

	private int downerFloorsToGoSize() {
        return Sets.newHashSet(Iterables.filter(floorsToGo, new Predicate<Integer>() {
            @Override
            public boolean apply( java.lang.Integer input) {
                return input < currentFloor;
            }
        })).size();
	}

	private int upperFloorsToGoSize() {
        return Sets.newHashSet(Iterables.filter(floorsToGo, new Predicate<Integer>() {
            @Override
            public boolean apply(java.lang.Integer input) {
                return input > currentFloor;
            }
        })).size();
	}

	@Override
	public ElevatorEngine reset(String cause) throws ElevatorIsBrokenException {
		currentFloor = LOWER_FLOOR;
		usersIn = 0;
		floorsToGo = new HashSet<>();
		possibleFloorsToGo = new HashSet<>();
		return null;
	}

	@Override
	public ElevatorEngine userHasEntered(User user) throws ElevatorIsBrokenException {
		usersIn++;
		return null;
	}

	@Override
	public ElevatorEngine userHasExited(User user) throws ElevatorIsBrokenException {
		usersIn--;
		return null;
	}

	Integer getCurrentFloor() {
		return currentFloor;
	}
}
