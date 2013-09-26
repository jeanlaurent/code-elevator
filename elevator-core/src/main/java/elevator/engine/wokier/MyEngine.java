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

	private int currentFloor = LOWER_FLOOR;
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
		if (floorsToGo.contains(currentFloor) || floorsToPick.contains(currentFloor) && !open) {
			floorsToPick.remove(currentFloor);
			floorsToGo.remove(currentFloor);
			possibleFloorsToGo.remove(currentFloor);
			open = true;
			return Command.OPEN;
		}
		if (open) {
			open = false;
			return Command.CLOSE;
		}
		if (usersIn > 0) {
			int uppersToGo = upperFloorsSize(floorsToGo);
			int downersToGo = downerFloorsSize(floorsToGo);
			if (uppersToGo > downersToGo) {
				setCurrentFloor(currentFloor + 1);
				return Command.UP;
			}
			if (uppersToGo < downersToGo) {
				setCurrentFloor(currentFloor - 1);
				return Command.DOWN;
			}
		}
		int uppersToPick = upperFloorsSize(floorsToPick);
		int downersToPick = downerFloorsSize(floorsToPick);
		if (uppersToPick > downersToPick) {
			setCurrentFloor(currentFloor + 1);
			return Command.UP;
		}
		if (uppersToPick < downersToPick) {
			setCurrentFloor(currentFloor - 1);
			return Command.DOWN;
		}
        if(nearest()<currentFloor)  {
            return Command.DOWN;
        }
		return Command.UP;

	}

    private int nearest()     {
         return Collections.min(Sets.union(floorsToGo, floorsToPick), new Comparator<Integer>() {
             @Override
             public int compare(Integer o1, Integer o2) {
                return  new Integer(Math.abs(o1 - currentFloor)).compareTo(new Integer( Math.abs(o2 - currentFloor)));
             }
         });
    }

	private int downerFloorsSize(Set floors) {
		return Sets.newHashSet(Iterables.filter(floors, new Predicate<Integer>() {
			@Override
			public boolean apply(java.lang.Integer input) {
				return input < currentFloor;
			}
		})).size();
	}

	private int upperFloorsSize(Set floors) {
		return Sets.newHashSet(Iterables.filter(floors, new Predicate<Integer>() {
			@Override
			public boolean apply(java.lang.Integer input) {
				return input > currentFloor;
			}
		})).size();
	}

	@Override
	public ElevatorEngine reset(String cause) throws ElevatorIsBrokenException {
		setCurrentFloor(LOWER_FLOOR);
		usersIn = 0;
		floorsToGo.clear();
		floorsToPick.clear();
		possibleFloorsToGo.clear();
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

    void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }
}
