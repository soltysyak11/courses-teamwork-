package com.softserve.itacademy.service;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.repository.RoleRepository;
import com.softserve.itacademy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role expectedRole;
    final Long ROLE_ID = 1L;
    final String NAME = "name";

    @BeforeEach
    void setUp () {
        expectedRole = new Role ( );
        expectedRole.setName ( String.valueOf ( ROLE_ID ) );
        expectedRole.setName ( NAME );
    }

    @Test
    void createRoleTest () {
        when ( roleRepository.save ( any ( Role.class ) ) ).thenReturn ( expectedRole );
        Role actualRole = roleService.create ( expectedRole );
        verify ( roleRepository ).save ( any ( Role.class ) );
        assertEquals ( expectedRole, actualRole );
    }

    @Test
    void createRoleNullEntityTest () {
        when ( roleRepository.save ( any ( Role.class ) ) ).thenThrow ( new IllegalArgumentException ( ) );
        Exception exception = assertThrows ( NullEntityReferenceException.class, () -> roleService.create ( expectedRole ) );
        assertEquals ( "Role cannot be 'null'", exception.getMessage ( ) );
        verify ( roleRepository ).save ( any ( Role.class ) );
    }

    @Test
    void readByIdTest () {
        when ( roleRepository.findById ( anyLong ( ) ) ).thenReturn ( Optional.of ( expectedRole ) );
        Role actual = roleService.readById ( ROLE_ID );
        verify ( roleRepository ).findById ( anyLong ( ) );
        assertEquals ( expectedRole, actual );
        assertEquals ( expectedRole.getId ( ), actual.getId ( ) );
    }

    @Test
    void readByIdNonExistentIdTest () {
        when ( roleRepository.findById ( anyLong ( ) ) ).thenReturn ( Optional.empty ( ) );
        assertThrows ( NoSuchElementException.class, () -> roleService.readById ( ROLE_ID ) );
    }

    @Test
    void updateRoleTest () {
        when ( roleRepository.findById ( anyLong ( ) ) ).thenReturn ( Optional.of ( expectedRole ) );
        when ( roleRepository.save ( any ( Role.class ) ) ).thenReturn ( expectedRole );
        Role actualRole = roleService.update ( expectedRole );
        verify ( roleRepository ).findById ( anyLong ( ) );
        verify ( roleRepository ).save ( any ( Role.class ) );
        Assertions.assertEquals ( expectedRole, actualRole );
        Assertions.assertEquals ( expectedRole.getId ( ), actualRole.getId ( ) );
    }



    @Test
    void updateRoleNullEntityTest () {
        Exception exception = assertThrows ( NullEntityReferenceException.class, () -> roleService.update ( null ) );
        assertEquals ( "Role cannot be 'null'", exception.getMessage ( ) );
        verifyNoInteractions ( roleRepository );
    }

    @Test
    void updateRoleNonExistentIdTest () {
        when ( roleRepository.findById ( anyLong ( ) ) ).thenReturn ( Optional.empty ( ) );
        assertThrows ( NoSuchElementException.class, () -> roleService.update ( expectedRole ) );
        verify ( roleRepository, times ( 0 ) ).save ( any ( Role.class ) );
    }

    @Test
    void deleteRoleTest () {
        when ( roleRepository.findById ( anyLong ( ) ) ).thenReturn ( Optional.of ( expectedRole ) );
        roleService.delete ( ROLE_ID );
        verify ( roleRepository ).findById ( anyLong ( ) );
        verify ( roleRepository ).delete ( any ( Role.class ) );
    }

    @Test
    void deleteRoleNonExistentIdTest () {
        when ( roleRepository.findById ( anyLong ( ) ) ).thenReturn ( Optional.empty ( ) );
        assertThrows ( NoSuchElementException.class, () -> roleService.delete ( ROLE_ID ) );
        verify ( roleRepository ).findById ( anyLong ( ) );
        verify ( roleRepository, times ( 0 ) ).delete ( any ( Role.class ) );
    }

    @Test
    void getAllEmptyListTest () {
        when ( roleRepository.findAll ( ) ).thenReturn ( Collections.emptyList ( ) );
        List<Role> actual = roleService.getAll ( );
        assertEquals ( 0, actual.size ( ) );
        verify ( roleRepository ).findAll ( );
    }

    @Test
    void getAllListNotEmptyTest () {
        List<Role> rolesExpected = Arrays.asList ( new Role ( ), new Role ( ) );
        when ( roleRepository.findAll ( ) ).thenReturn ( rolesExpected );
        List<Role> actualRoles = roleService.getAll ( );
        assertEquals ( rolesExpected.size ( ), actualRoles.size ( ) );
        verify ( roleRepository ).findAll ( );
    }
}
