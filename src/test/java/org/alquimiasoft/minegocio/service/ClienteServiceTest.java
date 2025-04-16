package org.alquimiasoft.minegocio.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.alquimiasoft.minegocio.dto.ClienteDTO;
import org.alquimiasoft.minegocio.dto.DireccionDTO;
import org.alquimiasoft.minegocio.entity.Cliente;
import org.alquimiasoft.minegocio.enums.TipoIdentificacion;
import org.alquimiasoft.minegocio.exception.ClientValidationException;
import org.alquimiasoft.minegocio.exception.ConflictException;
import org.alquimiasoft.minegocio.exception.MissingFieldsException;
import org.alquimiasoft.minegocio.mapper.ClienteMapper;
import org.alquimiasoft.minegocio.repository.ClienteRepository;
import org.alquimiasoft.minegocio.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

        @Mock
        private ClienteRepository clienteRepository;

        @Mock
        private ClienteMapper clienteMapper;

        @InjectMocks
        private ClienteServiceImpl clienteService;

        @BeforeEach
        void configurarMocksGenerales() {
                lenient().when(clienteMapper.toEntity(any())).thenAnswer(invocation -> {
                        ClienteDTO dto = invocation.getArgument(0);
                        Cliente cliente = new Cliente();
                        cliente.setId(1L);
                        cliente.setNumeroIdentificacion(dto.getNumeroIdentificacion());
                        cliente.setNombres(dto.getNombres());
                        cliente.setCorreo(dto.getCorreo());
                        cliente.setCelular(dto.getCelular());

                        if (dto.getDirecciones() != null) {
                                cliente.setDirecciones(dto.getDirecciones().stream().map(direccionDTO -> {
                                        var direccion = new org.alquimiasoft.minegocio.entity.Direccion();
                                        direccion.setProvincia(direccionDTO.getProvincia());
                                        direccion.setCiudad(direccionDTO.getCiudad());
                                        direccion.setDireccion(direccionDTO.getDireccion());
                                        direccion.setMatriz(direccionDTO.isMatriz());
                                        direccion.setCliente(cliente);
                                        return direccion;
                                }).collect(Collectors.toList()));
                        } else {
                                cliente.setDirecciones(new ArrayList<>());
                        }

                        return cliente;
                });
        }

        @Test
        void crearCliente_valido_retornaClienteCreado() {
                ClienteDTO dtoEntrada = ClienteDTO.builder()
                                .numeroIdentificacion("1234567890")
                                .tipoIdentificacion(TipoIdentificacion.CEDULA)
                                .nombres("Luis Perez")
                                .correo("luis@mail.com")
                                .celular("0987654321")
                                .direcciones(List.of(DireccionDTO.builder()
                                                .provincia("Azuay")
                                                .ciudad("Cuenca")
                                                .direccion("Calle Bolivar")
                                                .matriz(true)
                                                .build()))
                                .build();

                Cliente clienteMock = new Cliente();
                clienteMock.setId(1L);
                clienteMock.setNumeroIdentificacion("1234567890");
                clienteMock.setDirecciones(new ArrayList<>());

                when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                                .thenReturn(Optional.empty());

                when(clienteRepository.save(any())).thenReturn(clienteMock);
                when(clienteMapper.toDto(any())).thenReturn(dtoEntrada);

                ClienteDTO resultado = clienteService.crearCliente(dtoEntrada);

                assertNotNull(resultado);
                assertEquals("1234567890", resultado.getNumeroIdentificacion());
                verify(clienteRepository).save(any());
        }

        @Test
        void crearCliente_duplicado_lanzaExcepcion() {
                ClienteDTO dto = ClienteDTO.builder()
                                .numeroIdentificacion("1234567890")
                                .tipoIdentificacion(TipoIdentificacion.CEDULA)
                                .nombres("Luis Perez")
                                .correo("luis@mail.com")
                                .celular("0987654321")
                                .build();

                Cliente clienteExistente = new Cliente();
                clienteExistente.setId(1L);
                clienteExistente.setNumeroIdentificacion("1234567890");

                when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                                .thenReturn(Optional.of(clienteExistente));

                Exception exception = assertThrows(ConflictException.class, () -> clienteService.crearCliente(dto));

                assertEquals("Ya existe un cliente con esa identificacion", exception.getMessage());
                verify(clienteRepository, never()).save(any());
        }

        @Test
        void crearCliente_conDosDireccionesMatriz_lanzaExcepcion() {
                ClienteDTO dto = ClienteDTO.builder()
                                .numeroIdentificacion("1234567890")
                                .tipoIdentificacion(TipoIdentificacion.CEDULA)
                                .nombres("Laura Mendez")
                                .correo("laura@mail.com")
                                .celular("0988111222")
                                .direcciones(List.of(
                                                DireccionDTO.builder().provincia("Loja").ciudad("Loja")
                                                                .direccion("Calle 1").matriz(true).build(),
                                                DireccionDTO.builder().provincia("Zamora").ciudad("Zamora")
                                                                .direccion("Av. Principal").matriz(true).build()))
                                .build();

                when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                                .thenReturn(Optional.empty());

                Exception ex = assertThrows(ClientValidationException.class, () -> clienteService.crearCliente(dto));

                assertEquals("Solo puede existir una direccion matriz", ex.getMessage());
                verify(clienteRepository, never()).save(any());
        }

        @Test
        void crearCliente_sinDirecciones_lanzaExcepcion() {
                ClienteDTO dto = ClienteDTO.builder()
                                .numeroIdentificacion("1234567890")
                                .tipoIdentificacion(TipoIdentificacion.CEDULA)
                                .nombres("Mario Estrella")
                                .correo("mario@mail.com")
                                .celular("0911223344")
                                .direcciones(List.of()) // vacio
                                .build();

                when(clienteRepository.findByNumeroIdentificacion("1234567890"))
                                .thenReturn(Optional.empty());

                Exception exception = assertThrows(MissingFieldsException.class, () -> clienteService.crearCliente(dto));

                assertEquals("El cliente debe tener al menos una direccion", exception.getMessage());
                verify(clienteRepository, never()).save(any());
        }

        @Test
        void buscarClientes_conFiltroYPagina_retornaResultados() {
                PageRequest pageRequest = PageRequest.of(0, 2);
                Page<Cliente> resultadoSimulado = new PageImpl<>(List.of(
                                Cliente.builder()
                                                .id(1L)
                                                .tipoIdentificacion(TipoIdentificacion.CEDULA)
                                                .numeroIdentificacion("1234567890")
                                                .nombres("Luis Perez")
                                                .correo("luis@mail.com")
                                                .celular("0999999999")
                                                .direcciones(new ArrayList<>())
                                                .build()));

                when(clienteRepository.buscarPorFiltro(eq("lu"), eq(pageRequest))).thenReturn(resultadoSimulado);

                when(clienteMapper.toDto(any(Cliente.class))).thenAnswer(invocation -> {
                        Cliente c = invocation.getArgument(0);
                        return ClienteDTO.builder()
                                        .numeroIdentificacion(c.getNumeroIdentificacion())
                                        .nombres(c.getNombres())
                                        .build();
                });

                Page<ClienteDTO> pagina = clienteService.buscarClientes("lu", pageRequest);

                assertEquals(1, pagina.getContent().size());
                assertEquals("Luis Perez", pagina.getContent().get(0).getNombres());
                verify(clienteRepository).buscarPorFiltro("lu", pageRequest);
        }
}
