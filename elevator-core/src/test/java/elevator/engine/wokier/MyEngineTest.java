package elevator.engine.wokier;

import org.fest.assertions.Assertions;
import org.junit.Test;

import elevator.Command;
import elevator.Direction;

/**
 * @author francois wauquier
 */
public class MyEngineTest {

    MyEngine engine = new MyEngine();

    @Test
    public void test_gotToHighestFloor() {
        engine.call(0, Direction.UP);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.OPEN);
        engine.userHasEntered(null);
        engine.go(5);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.CLOSE);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.UP);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.UP);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.UP);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.UP);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.UP);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.OPEN);
        engine.userHasExited(null);
    }

    @Test
    public void test_pickMeUp() {
        engine.call(0, Direction.UP);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.OPEN);
        engine.userHasEntered(null);
        engine.go(5);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.CLOSE);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.UP);
        engine.call(3, Direction.UP);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.UP);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.UP);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.OPEN);
        engine.userHasEntered(null);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.CLOSE);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.UP);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.UP);
        Assertions.assertThat(engine.nextCommand()).isEqualTo(Command.OPEN);
        engine.userHasExited(null);
        engine.userHasExited(null);

    }
}
