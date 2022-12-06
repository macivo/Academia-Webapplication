/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.module;

import java.util.Objects;

/**
 * The class Module represents a teaching module.
 */
public class Module {

    private Long id;
    private String module_number;
    private String name;
    private String description;
    private Long coordinator;
    private int ects;
    private boolean running;
    private String firstname;
    private String lastname;

    public Module() {
    }

    public Module(Long id, String module_number, String name, String description, Long coordinator,
                  int ects, boolean running, String firstname, String lastname) {
        this.id = id;
        this.module_number = module_number;
        this.name = name;
        this.description = description;
        this.coordinator = coordinator;
        this.ects = ects;
        this.running = running;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Module(String number, String name, String description, Long coordinator,
                  int ects, boolean running, String firstname, String lastname) {
        this(null, number, name, description, coordinator, ects, running, firstname, lastname);
    }

    public Module(String module_number, String name, String description,
                  Long coordinator, int ects, boolean running) {
        this.module_number = module_number;
        this.name = name;
        this.description = description;
        this.coordinator = coordinator;
        this.ects = ects;
        this.running = running;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return module_number;
    }

    public void setNumber(String number) {
        this.module_number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Long module_coordinator) {
        this.coordinator = module_coordinator;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public boolean getRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Module module = (Module) object;
        return Objects.equals(id, module.id)
                && Objects.equals(module_number, module.module_number)
                && Objects.equals(name, module.name)
                && Objects.equals(description, module.description)
                && Objects.equals(coordinator, module.coordinator)
                && Objects.equals(ects, module.ects)
                && Objects.equals(running, module.running)
                && Objects.equals(firstname, module.firstname)
                && Objects.equals(lastname, module.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, module_number, name, description,
				coordinator, ects, running, firstname, lastname);
    }

    @Override
    public String toString() {
        return "Module{id=" + id + ", module_number=" + module_number + ", name='" + name
                + "', description='" + description
                + "', coordinator='" + coordinator
                + "', ects='" + ects
                + "', running='" + running
                + "', firstname='" + firstname
                + "', lastname='" + lastname
                + "'}";

    }
}
