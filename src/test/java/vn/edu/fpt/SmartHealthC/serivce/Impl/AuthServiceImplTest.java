package vn.edu.fpt.SmartHealthC.serivce.Impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.edu.fpt.SmartHealthC.domain.Enum.TypeAccount;
import vn.edu.fpt.SmartHealthC.domain.dto.request.LoginDto;
import vn.edu.fpt.SmartHealthC.domain.dto.request.RegisterDto;
import vn.edu.fpt.SmartHealthC.domain.dto.response.AuthenticationResponseDto;
import vn.edu.fpt.SmartHealthC.domain.dto.response.RefreshTokenResponseDto;
import vn.edu.fpt.SmartHealthC.domain.entity.Account;
import vn.edu.fpt.SmartHealthC.domain.entity.AppUser;
import vn.edu.fpt.SmartHealthC.domain.entity.RefreshToken;
import vn.edu.fpt.SmartHealthC.exception.AppException;
import vn.edu.fpt.SmartHealthC.exception.ErrorCode;
import vn.edu.fpt.SmartHealthC.repository.AccountRepository;
import vn.edu.fpt.SmartHealthC.repository.AppUserRepository;
import vn.edu.fpt.SmartHealthC.repository.RefreshTokenRepository;
import vn.edu.fpt.SmartHealthC.security.JwtProvider;
import vn.edu.fpt.SmartHealthC.serivce.EmailService;
import vn.edu.fpt.SmartHealthC.serivce.Impl.AuthServiceImpl;


import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(1);
        testAccount.setEmail("test@example.com");
        testAccount.setPassword("hashedPassword"); // Mock hashed password
        testAccount.setType(TypeAccount.USER);
    }

    @Test
    void testLogin_Success() throws ParseException {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        when(accountRepository.findAccountByEmail(anyString())).thenReturn(Optional.of(testAccount));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtProvider.generateToken(any(), any())).thenReturn("mockedJwtToken");

        // Act
        AuthenticationResponseDto response = authService.login(loginDto);

        // Assert
        assertNotNull(response);
        assertEquals(testAccount.getType(), response.getType());
        assertEquals(testAccount.getId(), response.getIdUser());
        assertEquals("mockedJwtToken", response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("wrongPassword");

        when(accountRepository.findAccountByEmail(anyString())).thenReturn(Optional.of(testAccount));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act and Assert
        assertThrows(AppException.class, () -> authService.login(loginDto));
        verify(jwtProvider, never()).generateToken(any(), any());
    }

    @Test
    void testLogin_AccountNotFound() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("nonexistent@example.com");
        loginDto.setPassword("password");

        when(accountRepository.findAccountByEmail(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(AppException.class, () -> authService.login(loginDto));
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtProvider, never()).generateToken(any(), any());
    }

    @Test
    void testRegister_Success() {
        // Arrange
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("newuser@example.com");
        registerDto.setPassword("password");
        registerDto.setName("New User");

        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(appUserRepository.save(any())).thenAnswer(invocation -> {
            AppUser savedUser = invocation.getArgument(0);
            savedUser.setId(1); // Mock saved ID
            return savedUser;
        });

        // Act
        assertDoesNotThrow(() -> authService.register(registerDto));

        // Assert
        verify(accountRepository, times(1)).save(any());
        verify(appUserRepository, times(1)).save(any());
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        // Arrange
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password");
        registerDto.setName("New User");

        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(testAccount));

        // Act and Assert
        assertThrows(AppException.class, () -> authService.register(registerDto));
        verify(accountRepository, never()).save(any());
        verify(appUserRepository, never()).save(any());
    }
    @Test
    void testSendEmailCode_Success() {
        // Arrange
        String email = "test@example.com";
        Account existingAccount = new Account();
        existingAccount.setEmail(email);

        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(emailService.generateRandomCode(anyInt())).thenReturn("123456");
        when(emailService.sendMail(anyString(), anyString(), anyString())).thenReturn(true);

        // Act
        String code = authService.sendEmailCode(email);

        // Assert
        assertNotNull(code);
        assertEquals("123456", code);
        verify(emailService, times(1)).sendMail(email, "MÃ XÁC THỰC EMAIL", "Code xác thực email đăng ký của bạn là : 123456");
    }

    @Test
    void testSendEmailCode_EmailAlreadyExists() {
        // Arrange
        String existingEmail = "existing@example.com";
        Account existingAccount = new Account();
        existingAccount.setEmail(existingEmail);

        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(existingAccount));

        // Act and Assert
        assertThrows(AppException.class, () -> authService.sendEmailCode(existingEmail));
        verify(emailService, never()).sendMail(anyString(), anyString(), anyString());
    }

    @Test
    void testSendEmailCode_SendEmailFail() {
        // Arrange
        String email = "test@example.com";
        Account newAccount = new Account();
        newAccount.setEmail(email);

        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(emailService.generateRandomCode(anyInt())).thenReturn("123456");
        when(emailService.sendMail(anyString(), anyString(), anyString())).thenReturn(false);

        // Act and Assert
        assertThrows(AppException.class, () -> authService.sendEmailCode(email));
        verify(emailService, times(1)).sendMail(email, "MÃ XÁC THỰC EMAIL", "Code xác thực email đăng ký của bạn là : 123456");
    }


}