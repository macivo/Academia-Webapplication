package ch.bfh.ti.academia.modulerun;

import ch.bfh.ti.academia.module.Module;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModuleRunTest {

    @Test
    public void createModuleRun() {
        ModuleRun modulerun = new ModuleRun((long)1, "autumn", 2021, true, "Astrophysics", "BTI2021", 1);
        assertEquals(1, modulerun.getId());
        assertEquals("autumn", modulerun.getSemester());
        assertEquals(2021, modulerun.getYear());
        assertEquals(true, modulerun.getRunning());
        assertEquals("Astrophysics", modulerun.getModuleName());
        assertEquals("BTI2021", modulerun.getModuleNumber());
        assertEquals(1, modulerun.getModuleId());
    }

}
