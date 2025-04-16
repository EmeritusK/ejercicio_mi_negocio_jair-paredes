
package org.alquimiasoft.minegocio.service;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.alquimiasoft.minegocio.dto.ClienteDTO;
import org.alquimiasoft.minegocio.dto.DireccionDTO;
import org.alquimiasoft.minegocio.entity.Cliente;
import org.alquimiasoft.minegocio.enums.TipoIdentificacion;
import org.alquimiasoft.minegocio.mapper.ClienteMapper;
import org.alquimiasoft.minegocio.repository.ClienteRepository;
import org.alquimiasoft.minegocio.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void crearCliente_valido_retornaClienteCreado() {
        // Arrange
        ClienteDTO dtoEntrada = ClienteDTO.builder()
                .numeroIdentificacion("1234567890")
                .tipoIdentificacion(TipoIdentificacion.CEDULA)
                .nombres("Luis Pérez")
                .correo("luis@mail.com")
                .celular("0987654321")
                .direcciones(List.of(DireccionDTO.builder()
                        .provincia("Azuay")
                        .ciudad("Cuenca")
                        .direccion("Calle Bolívar")
                        .matriz(true)
                        .build()))
                .build();

        Cliente clienteMock = new Cliente();
        clienteMock.setId(1L);
        clienteMock.setNumeroIdentificacion("1234567890");

        when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.empty());

        when(clienteMapper.toEntity(any())).thenReturn(clienteMock);
        when(clienteRepository.save(any())).thenReturn(clienteMock);
        when(clienteMapper.toDto(any())).thenReturn(dtoEntrada);

        // Act
        ClienteDTO resultado = clienteService.crearCliente(dtoEntrada);

        // Assert
        assertNotNull(resultado);
        assertEquals("1234567890", resultado.getNumeroIdentificacion());
        verify(clienteRepository).save(any());
    }

    @Test
    void crearCliente_duplicado_lanzaExcepcion() {
        // Arrange
        ClienteDTO dto = ClienteDTO.builder()
                .numeroIdentificacion("1234567890")
                .tipoIdentificacion(TipoIdentificacion.CEDULA)
                .nombres("Luis Pérez")
                .correo("luis@mail.com")
                .celular("0987654321")
                .build();

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(1L);
        clienteExistente.setNumeroIdentificacion("1234567890");

        when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                .thenReturn(Optional.of(clienteExistente));

        // Act + Assert
        Exception exception = assertThrows(RuntimeException.class, () -> clienteService.crearCliente(dto));

        assertEquals("Cliente ya existe", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

}