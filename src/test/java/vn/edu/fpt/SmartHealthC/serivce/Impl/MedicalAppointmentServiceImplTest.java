package vn.edu.fpt.SmartHealthC.serivce.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.fpt.SmartHealthC.domain.Enum.TypeMedicalAppointment;
import vn.edu.fpt.SmartHealthC.domain.Enum.TypeMedicalAppointmentStatus;
import vn.edu.fpt.SmartHealthC.domain.dto.request.MedicalAppointmentDTO;
import vn.edu.fpt.SmartHealthC.domain.dto.response.MedicalAppointmentResponseDTO;
import vn.edu.fpt.SmartHealthC.domain.dto.response.ResponsePaging;
import vn.edu.fpt.SmartHealthC.domain.entity.AppUser;
import vn.edu.fpt.SmartHealthC.domain.entity.MedicalAppointment;
import vn.edu.fpt.SmartHealthC.domain.entity.WebUser;
import vn.edu.fpt.SmartHealthC.exception.AppException;
import vn.edu.fpt.SmartHealthC.exception.ErrorCode;
import vn.edu.fpt.SmartHealthC.repository.AppUserRepository;
import vn.edu.fpt.SmartHealthC.repository.MedicalAppointmentRepository;
import vn.edu.fpt.SmartHealthC.serivce.WebUserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MedicalAppointmentServiceImplTest {

    @Mock
    private MedicalAppointmentRepository medicalAppointmentRepository;

    @Mock
    private WebUserService webUserService;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private MedicalAppointmentServiceImpl medicalAppointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetMedicalAppointmentByUserIdMobile_WithContent() {
        // Arrange
        Integer userId = 1;

        // Mocking AppUser
        AppUser appUser = new AppUser();
        appUser.setId(1);
        appUser.setName("John Doe");

        // Mocking MedicalAppointment
        List<MedicalAppointment> medicalAppointments = new ArrayList<>();
        MedicalAppointment medicalAppointment1 = new MedicalAppointment();
        medicalAppointment1.setId(1);
        medicalAppointment1.setAppUserId(appUser); // Set the correct AppUser instance
        medicalAppointment1.setHospital("Hospital A");
        medicalAppointment1.setTypeMedicalAppointment(TypeMedicalAppointment.MEDICAL_CHECKUP);
        medicalAppointment1.setStatusMedicalAppointment(TypeMedicalAppointmentStatus.DONE);
        medicalAppointment1.setNote("Regular checkup");
        medicalAppointments.add(medicalAppointment1);

        when(medicalAppointmentRepository.findAllByUserIdAndType(TypeMedicalAppointmentStatus.DONE, userId))
                .thenReturn(medicalAppointments);

        // Act
        List<MedicalAppointmentResponseDTO> response = medicalAppointmentService.getMedicalAppointmentByUserIdMobile(userId);

        // Assert
        assertEquals(1, response.size());
        assertEquals("Hospital A", response.get(0).getHospital());
        assertEquals(TypeMedicalAppointment.MEDICAL_CHECKUP, response.get(0).getTypeMedicalAppointment());
        assertEquals(TypeMedicalAppointmentStatus.DONE, response.get(0).getStatusMedicalAppointment());
        assertEquals("Regular checkup", response.get(0).getNote());
    }
    @Test
    void testGetMedicalAppointmentByUserIdMobile_NoContent() {
        // Arrange
        Integer userId = 1;
        List<MedicalAppointment> medicalAppointments = new ArrayList<>();

        when(medicalAppointmentRepository.findAllByUserIdAndType(TypeMedicalAppointmentStatus.DONE, userId))
                .thenReturn(medicalAppointments);

        // Act
        List<MedicalAppointmentResponseDTO> response = medicalAppointmentService.getMedicalAppointmentByUserIdMobile(userId);

        // Assert
        assertEquals(0, response.size());
    }

    @Test
    void testDeleteMedicalAppointment_Success() {
        // Arrange
        Integer appointmentId = 1;

        MedicalAppointment existingMedicalAppointment = new MedicalAppointment();
        existingMedicalAppointment.setId(appointmentId);
        existingMedicalAppointment.setAppUserId(new AppUser()); // Set necessary fields
        existingMedicalAppointment.setTypeMedicalAppointment(TypeMedicalAppointment.MEDICAL_CHECKUP);
        existingMedicalAppointment.setHospital("Hospital A");
        existingMedicalAppointment.setStatusMedicalAppointment(TypeMedicalAppointmentStatus.CONFIRM.CONFIRM);
        existingMedicalAppointment.setNote("Note");

        when(medicalAppointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(existingMedicalAppointment));
        doNothing().when(medicalAppointmentRepository).deleteById(appointmentId);

        // Act
        MedicalAppointmentResponseDTO result = medicalAppointmentService.deleteMedicalAppointment(appointmentId);

        // Assert
        assertEquals(existingMedicalAppointment.getId(), result.getId());
        assertEquals(existingMedicalAppointment.getAppUserId().getName(), result.getAppUserName());
        assertEquals(existingMedicalAppointment.getHospital(), result.getHospital());
        assertEquals(existingMedicalAppointment.getTypeMedicalAppointment(), result.getTypeMedicalAppointment());
        assertEquals(existingMedicalAppointment.getStatusMedicalAppointment(), result.getStatusMedicalAppointment());
        assertEquals(existingMedicalAppointment.getNote(), result.getNote());
    }

    @Test
    void testDeleteMedicalAppointment_AppointmentNotFound() {
        // Arrange
        Integer appointmentId = 1;

        when(medicalAppointmentRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act and Assert
        AppException exception = assertThrows(AppException.class, () -> {
            medicalAppointmentService.deleteMedicalAppointment(appointmentId);
        });

        assertEquals(ErrorCode.MEDICAL_APPOINTMENT_NOT_FOUND, exception.getErrorCode());
    }
    @Test
    void testUpdateMedicalAppointment_Success() {
        // Arrange
        Integer appointmentId = 1;
        MedicalAppointmentDTO medicalAppointmentDTO = new MedicalAppointmentDTO();
        medicalAppointmentDTO.setType(TypeMedicalAppointment.MEDICAL_CHECKUP);
        medicalAppointmentDTO.setLocation("Hospital A");
        medicalAppointmentDTO.setNote("Updated note");

        MedicalAppointment existingMedicalAppointment = new MedicalAppointment();
        existingMedicalAppointment.setId(appointmentId);
        existingMedicalAppointment.setAppUserId(new AppUser()); // Set necessary fields
        existingMedicalAppointment.setTypeMedicalAppointment(TypeMedicalAppointment.MEDICAL_CHECKUP);
        existingMedicalAppointment.setHospital("Initial Hospital");
        existingMedicalAppointment.setNote("Initial Note");

        MedicalAppointment updatedMedicalAppointment = new MedicalAppointment();
        updatedMedicalAppointment.setId(appointmentId);
        updatedMedicalAppointment.setAppUserId(existingMedicalAppointment.getAppUserId());
        updatedMedicalAppointment.setTypeMedicalAppointment(medicalAppointmentDTO.getType());
        updatedMedicalAppointment.setDate(medicalAppointmentDTO.getDate());
        updatedMedicalAppointment.setHospital(medicalAppointmentDTO.getLocation());
        updatedMedicalAppointment.setNote(medicalAppointmentDTO.getNote());

        when(medicalAppointmentRepository.findById(appointmentId))
                .thenReturn(Optional.of(existingMedicalAppointment));
        when(medicalAppointmentRepository.save(updatedMedicalAppointment))
                .thenReturn(updatedMedicalAppointment);

        // Act
        MedicalAppointmentResponseDTO result = medicalAppointmentService.updateMedicalAppointment(appointmentId,
                medicalAppointmentDTO);

        // Assert
        assertEquals(updatedMedicalAppointment.getId(), result.getId());
        assertEquals(updatedMedicalAppointment.getAppUserId().getName(), result.getAppUserName());
        assertEquals(updatedMedicalAppointment.getDate(), result.getDate());
        assertEquals(updatedMedicalAppointment.getHospital(), result.getHospital());
        assertEquals(updatedMedicalAppointment.getTypeMedicalAppointment(), result.getTypeMedicalAppointment());
        assertEquals(updatedMedicalAppointment.getStatusMedicalAppointment(), result.getStatusMedicalAppointment());
        assertEquals(updatedMedicalAppointment.getNote(), result.getNote());
    }

    @Test
    void testUpdateMedicalAppointment_AppointmentNotFound() {
        // Arrange
        Integer appointmentId = 1;
        MedicalAppointmentDTO medicalAppointmentDTO = new MedicalAppointmentDTO();
        medicalAppointmentDTO.setType(TypeMedicalAppointment.MEDICAL_CHECKUP);
        medicalAppointmentDTO.setLocation("Hospital A");
        medicalAppointmentDTO.setNote("Updated note");

        when(medicalAppointmentRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act and Assert
        AppException exception = assertThrows(AppException.class, () -> {
            medicalAppointmentService.updateMedicalAppointment(appointmentId, medicalAppointmentDTO);
        });

        assertEquals(ErrorCode.MEDICAL_APPOINTMENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testCreateMedicalAppointment_ValidInput() {
        // Mock MedicalAppointmentDTO
        MedicalAppointmentDTO medicalAppointmentDTO = new MedicalAppointmentDTO();
        medicalAppointmentDTO.setAppUserId(1); // assuming appUserId for an existing user
        medicalAppointmentDTO.setType(TypeMedicalAppointment.MEDICAL_CHECKUP);
        medicalAppointmentDTO.setLocation("Hospital A");
        medicalAppointmentDTO.setNote("Test appointment");

        // Mock AppUser from repository
        AppUser appUser = new AppUser();
        appUser.setId(1);
        appUser.setName("John Doe");
        when(appUserRepository.findById(anyInt())).thenReturn(Optional.of(appUser));

        // Mock saved MedicalAppointment
        MedicalAppointment savedMedicalAppointment = new MedicalAppointment();
        savedMedicalAppointment.setId(1);
        savedMedicalAppointment.setAppUserId(appUser);
        savedMedicalAppointment.setTypeMedicalAppointment(TypeMedicalAppointment.MEDICAL_CHECKUP);
        savedMedicalAppointment.setHospital("Hospital A");
        savedMedicalAppointment.setStatusMedicalAppointment(TypeMedicalAppointmentStatus.PENDING);
        savedMedicalAppointment.setNote("Test appointment");
        when(medicalAppointmentRepository.save(any())).thenReturn(savedMedicalAppointment);

        // Call the service method
        MedicalAppointmentResponseDTO result = medicalAppointmentService.createMedicalAppointment(medicalAppointmentDTO);

        // Assertions
        assertEquals(savedMedicalAppointment.getId(), result.getId());
        assertEquals(savedMedicalAppointment.getAppUserId().getName(), result.getAppUserName());
        assertEquals(savedMedicalAppointment.getHospital(), result.getHospital());
        assertEquals(savedMedicalAppointment.getTypeMedicalAppointment(), result.getTypeMedicalAppointment());
        assertEquals(savedMedicalAppointment.getStatusMedicalAppointment(), result.getStatusMedicalAppointment());
        assertEquals(savedMedicalAppointment.getNote(), result.getNote());

        // Verify repository methods were called
        verify(appUserRepository, times(1)).findById(anyInt());
        verify(medicalAppointmentRepository, times(1)).save(any());
    }



    @Test
    void testGetMedicalAppointmentById_WhenNotFound() {
        // Arrange
        Integer id = 1;
        when(medicalAppointmentRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act and Assert
        AppException exception = assertThrows(AppException.class, () -> {
            medicalAppointmentService.getMedicalAppointmentById(id);
        });

        assertEquals(ErrorCode.MEDICAL_APPOINTMENT_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    void testCreateMedicalAppointment_WhenAppUserNotFound() {
        // Arrange
        MedicalAppointmentDTO medicalAppointmentDTO = new MedicalAppointmentDTO();
        medicalAppointmentDTO.setAppUserId(1); // assuming appUserId for a non-existent user

        when(appUserRepository.findById(any())).thenReturn(Optional.empty());

        // Act and Assert
        AppException exception = assertThrows(AppException.class, () -> {
            medicalAppointmentService.createMedicalAppointment(medicalAppointmentDTO);
        });

        assertEquals(ErrorCode.APP_USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testCreateMedicalAppointment_WhenAppUserFound() {
        // Arrange
        MedicalAppointmentDTO medicalAppointmentDTO = new MedicalAppointmentDTO();
        medicalAppointmentDTO.setAppUserId(1); // assuming appUserId for an existing user
        medicalAppointmentDTO.setType(TypeMedicalAppointment.MEDICAL_CHECKUP);
        medicalAppointmentDTO.setLocation("Hospital A");
        medicalAppointmentDTO.setNote("Test appointment");
        AppUser appUser = new AppUser();
        appUser.setId(1);
        appUser.setName("Test User");

        when(appUserRepository.findById(any())).thenReturn(Optional.of(appUser));

        MedicalAppointment savedMedicalAppointment = new MedicalAppointment();
        savedMedicalAppointment.setId(1);
        savedMedicalAppointment.setAppUserId(appUser);
        savedMedicalAppointment.setTypeMedicalAppointment(medicalAppointmentDTO.getType());
        savedMedicalAppointment.setHospital(medicalAppointmentDTO.getLocation());
        savedMedicalAppointment.setStatusMedicalAppointment(TypeMedicalAppointmentStatus.PENDING);
        savedMedicalAppointment.setNote(medicalAppointmentDTO.getNote());

        when(medicalAppointmentRepository.save(any())).thenReturn(savedMedicalAppointment);

        // Act
        MedicalAppointmentResponseDTO result = medicalAppointmentService.createMedicalAppointment(medicalAppointmentDTO);

        // Assert
        assertEquals(savedMedicalAppointment.getId(), result.getId());
        assertEquals(savedMedicalAppointment.getAppUserId().getName(), result.getAppUserName());
        assertEquals(savedMedicalAppointment.getDate(), result.getDate());
        assertEquals(savedMedicalAppointment.getHospital(), result.getHospital());
        assertEquals(savedMedicalAppointment.getTypeMedicalAppointment(), result.getTypeMedicalAppointment());
        assertEquals(savedMedicalAppointment.getStatusMedicalAppointment(), result.getStatusMedicalAppointment());
        assertEquals(savedMedicalAppointment.getNote(), result.getNote());
    }
    @Test
    void testGetAllMedicalAppointmentsPending_WhenWebUserIsNull() {
        // Mock authenticated user
        String userEmail = "test@example.com";
        when(webUserService.getWebUserByEmail(anyString())).thenReturn(null);

        // Call service method and expect exception
        assertThrows(Exception.class, () -> {
            medicalAppointmentService.getAllMedicalAppointmentsPending(0, TypeMedicalAppointment.MEDICAL_CHECKUP);
        });
    }
    @Test
    void testGetAllMedicalAppointmentsPending() {
        // Mock authenticated user
        String userEmail = "test@example.com";
        WebUser webUser = new WebUser();
        webUser.setId(1);
        when(webUserService.getWebUserByEmail(anyString())).thenReturn(webUser);

        // Mock Authentication and SecurityContextHolder
        Authentication authentication = mockAuthentication(userEmail);
        SecurityContext securityContext = mockSecurityContext(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock pageable and expected data
        int pageNo = 0;
        TypeMedicalAppointment type = TypeMedicalAppointment.MEDICAL_CHECKUP;
        PageRequest pageable = PageRequest.of(pageNo, 5);
        List<MedicalAppointment> medicalAppointments = new ArrayList<>();

        AppUser appUser = new AppUser();
        appUser.setId(1);
        appUser.setName("John Doe");

        MedicalAppointment medicalAppointment1 = new MedicalAppointment();
        medicalAppointment1.setId(1);
        medicalAppointment1.setAppUserId(appUser); // Set the AppUser
        medicalAppointment1.setTypeMedicalAppointment(type);
        medicalAppointment1.setHospital("Hospital A");
        medicalAppointment1.setNote("Note A");
        medicalAppointments.add(medicalAppointment1);

        MedicalAppointment medicalAppointment2 = new MedicalAppointment();
        medicalAppointment2.setId(2);
        medicalAppointment2.setAppUserId(appUser); // Set the AppUser
        medicalAppointment2.setTypeMedicalAppointment(type);
        medicalAppointment2.setHospital("Hospital B");
        medicalAppointment2.setNote("Note B");
        medicalAppointments.add(medicalAppointment2);

        Page<MedicalAppointment> page = new PageImpl<>(medicalAppointments);

        when(medicalAppointmentRepository.findAllPendingByUserIdAndType(any(), anyInt(), any(), any())).thenReturn(page);

        // Call service method
        ResponsePaging<List<MedicalAppointmentResponseDTO>> response = medicalAppointmentService.getAllMedicalAppointmentsPending(pageNo, type);

        // Assertions
        assertEquals(1, response.getCurrentPage()); // Assuming pageNo starts from 0 and you want currentPage to start from 1
        assertEquals(2, response.getTotalItems());
        assertEquals(1, response.getTotalPages());
        assertEquals(2, response.getDataResponse().size());
        assertEquals("Hospital A", response.getDataResponse().get(0).getHospital());
        assertEquals("Hospital B", response.getDataResponse().get(1).getHospital());
    }
    @Test
    void testGetMedicalAppointmentEntityById_WhenFound() {
        // Arrange
        Integer id = 1;
        MedicalAppointment expectedMedicalAppointment = new MedicalAppointment();
        expectedMedicalAppointment.setId(id);
        when(medicalAppointmentRepository.findById(anyInt())).thenReturn(Optional.of(expectedMedicalAppointment));

        // Act
        MedicalAppointment result = medicalAppointmentService.getMedicalAppointmentEntityById(id);

        // Assert
        assertEquals(expectedMedicalAppointment, result);
    }

    @Test
    void testGetMedicalAppointmentEntityById_WhenNotFound() {
        // Arrange
        Integer id = 1;
        when(medicalAppointmentRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act and Assert
        AppException exception = assertThrows(AppException.class, () -> {
            medicalAppointmentService.getMedicalAppointmentEntityById(id);
        });

        assertEquals(ErrorCode.MEDICAL_APPOINTMENT_NOT_FOUND, exception.getErrorCode());
    }
    @Test
    void testGetAllMedicalAppointments_NoContent() {
        // Mock authenticated user
        String userEmail = "test@example.com";
        WebUser webUser = new WebUser();
        webUser.setId(1);
        when(webUserService.getWebUserByEmail(anyString())).thenReturn(webUser);

        // Mock SecurityContextHolder
        mockSecurityContextHolder(userEmail);

        // Mock pageable and expected data
        int pageNo = 0;
        String search = "";
        List<MedicalAppointment> medicalAppointments = new ArrayList<>(); // Empty list
        Page<MedicalAppointment> pagedResult = new PageImpl<>(medicalAppointments);
        when(medicalAppointmentRepository.findAllByWebUserId(anyInt(), any(Pageable.class), anyString()))
                .thenReturn(pagedResult);

        // Call service method
        ResponsePaging<List<MedicalAppointmentResponseDTO>> response = medicalAppointmentService.getAllMedicalAppointments(pageNo, search);

        // Assertions
        assertEquals(1, response.getCurrentPage());
        assertEquals(0, response.getTotalItems());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getDataResponse().size());
    }

    @Test
    void testGetAllMedicalAppointments_WithContent() {
        // Mock authenticated user
        String userEmail = "test@example.com";
        WebUser webUser = new WebUser();
        webUser.setId(1);

        // Mock getAppUserList() to return a non-null list
        List<AppUser> appUserList = new ArrayList<>();
        AppUser appUser1 = new AppUser();
        appUser1.setId(1);
        appUser1.setName("John Doe");
        appUserList.add(appUser1);
        webUser.setAppUserList(appUserList);

        when(webUserService.getWebUserByEmail(anyString())).thenReturn(webUser);

        // Mock pageable and expected data
        int pageNo = 0;
        String search = "someSearchCriteria";
        PageRequest pageable = PageRequest.of(pageNo, 5);
        List<MedicalAppointment> medicalAppointments = new ArrayList<>();
        // Populate medicalAppointments as needed...

        Page<MedicalAppointment> page = new PageImpl<>(medicalAppointments);

        when(medicalAppointmentRepository.findAllByWebUserId(anyInt(), any(), anyString())).thenReturn(page);

        // Call service method
        ResponsePaging<List<MedicalAppointmentResponseDTO>> response = medicalAppointmentService.getAllMedicalAppointments(pageNo, search);

        // Assertions and verifications as needed
        assertNotNull(response);
        // Add more assertions based on your expected behavior
    }

    private void mockSecurityContextHolder(String userEmail) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(userEmail);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    private Authentication mockAuthentication(String userEmail) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(userEmail);
        return authentication;
    }

    private SecurityContext mockSecurityContext(Authentication authentication) {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        return securityContext;
    }
}