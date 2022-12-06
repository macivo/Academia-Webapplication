package ch.bfh.ti.academia.enroll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnrollTest {
    @Test
    public void createEnroll() {
        Enroll enroll = new Enroll(1L, 1L, 1L, "A");
        assertEquals(1L, enroll.getId());
        assertEquals(1L, enroll.getModule_run_id());
        assertEquals(1L, enroll.getStudent_id());
        assertEquals("A", enroll.getGrade());
    }
}
