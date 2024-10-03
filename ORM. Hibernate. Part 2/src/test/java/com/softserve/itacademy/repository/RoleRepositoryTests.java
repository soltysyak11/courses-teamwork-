package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateRole() {
        Role role = new Role();
        role.setName("Admin");
        Role savedRole = roleRepository.save(role);
        assertNotNull(savedRole);
    }

    @Test
    public void testFindByName() {
        Role role = new Role();
        role.setName("User");
        entityManager.persist(role);
        entityManager.flush();

        Role foundRole = roleRepository.findByName("User");
        assertEquals(role.getName(), foundRole.getName());
    }

    @Test
    public void testDeleteRole() {
        Role role = new Role();
        role.setName("DeleteMe");
        entityManager.persist(role);
        entityManager.flush();

        roleRepository.delete(role);
        Role deletedRole = roleRepository.findByName("DeleteMe");
        assertNull(deletedRole);
    }
}
