/*
 * Academia (c) 2020, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.module;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModuleTest {

    @Test
    public void createModule() {
        Module module = new Module((long)1, "BTI3021", "Project and Training 3", "Description goes here...", (long)1, 4, false, "Eric", "Dubuis");
        assertEquals(1, module.getId());
        assertEquals("BTI3021", module.getNumber());
        assertEquals("Project and Training 3", module.getName());
        assertEquals("Description goes here...", module.getDescription());
        assertEquals(1, module.getCoordinator());
        assertEquals(4, module.getEcts());
        assertEquals(false, module.getRunning());
        assertEquals("Eric", module.getFirstname());
        assertEquals("Dubuis", module.getLastname());
        assertEquals(4, module.getEcts());
    }
}
