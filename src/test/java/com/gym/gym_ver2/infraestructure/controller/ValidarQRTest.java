package com.gym.gym_ver2.infraestructure.controller;

import com.gym.gym_ver2.aplicaction.service.AdminService;
import com.gym.gym_ver2.domain.model.dto.responseDTO.ValidacionRutinaResponse;
import com.gym.gym_ver2.domain.model.requestModels.ValidacionQrRutinaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidarQRTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidarQR_Success() {
        ValidacionQrRutinaRequest request = new ValidacionQrRutinaRequest("codigoQRValido", 1);
        request.setCodigoQR("codigoQRValido");
        request.setIdDesafioRealizado(1);

        ValidacionRutinaResponse expectedResponse = new ValidacionRutinaResponse("Rutina validada con éxito", 500.0);
        when(adminService.validarQr(request.getCodigoQR(), request.getIdDesafioRealizado())).thenReturn(expectedResponse);

        ResponseEntity<ValidacionRutinaResponse> response = adminController.validarQR(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(adminService, times(1)).validarQr(request.getCodigoQR(), request.getIdDesafioRealizado());
    }

    @Test
    void testValidarQR_Failure() {
        ValidacionQrRutinaRequest request = new ValidacionQrRutinaRequest( "codigoQRInvalido", 1);
        request.setCodigoQR("codigoQRInvalido");
        request.setIdDesafioRealizado(1);

        when(adminService.validarQr(request.getCodigoQR(), request.getIdDesafioRealizado()))
                .thenThrow(new RuntimeException("Error al validar QR"));

        Exception exception = assertThrows(RuntimeException.class, () ->
                adminController.validarQR(request));

        assertEquals("Error al validar QR", exception.getMessage());
        verify(adminService, times(1)).validarQr(request.getCodigoQR(), request.getIdDesafioRealizado());
    }
}
