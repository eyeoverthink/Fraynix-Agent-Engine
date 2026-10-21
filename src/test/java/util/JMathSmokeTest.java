package util;

import org.joml.Vector2f;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JMathSmokeTest {

    @Test
    public void rotateQuarterTurnAroundOrigin() {
        Vector2f v = new Vector2f(1.0f, 0.0f);
        JMath.rotate(v, 90.0f, new Vector2f(0.0f, 0.0f));

        assertTrue(JMath.compare(v, new Vector2f(0.0f, 1.0f), 0.0001f));
    }
}

