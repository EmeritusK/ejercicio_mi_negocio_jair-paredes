package org.alquimiasoft.minegocio.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import org.alquimiasoft.minegocio.dto.ClienteDTO;
import org.alquimiasoft.minegocio.dto.DireccionDTO;
import org.alquimiasoft.minegocio.enums.TipoIdentificacion;
import org.alquimiasoft.minegocio.exception.ConflictException;
import org.alquimiasoft.minegocio.service.ClienteService;
import org.alquimiasoft.minegocio.service.DireccionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({ ClienteController.class, DireccionController.class })
public class ClienteControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private ClienteService clienteService;
        @MockBean
        private DireccionService direccionService;


        // Caso: Crear un nuevo cliente con direccion matriz
// Este test valida que el endpoint POST /api/clientes permite crear un cliente nuevo,
// enviando datos en formato JSON. Simula una respuesta exitosa del servicio y verifica
// que se devuelve el cliente creado, cumpliendo con el requerimiento de retorno en formato JSON.
// Incluye validacion para que no exista identificacion duplicada.
        @Test
        @DisplayName("Crear nuevo cliente con direccion matriz - exito")
        void crearCliente_Success() throws Exception {
                ClienteDTO nuevoCliente = ClienteDTO.builder()
                                .numeroIdentificacion("1234567890")
                                .nombres("Cliente Test")
                                .tipoIdentificacion(TipoIdentificacion.CEDULA)
                                .build();

                when(clienteService.crearCliente(any(ClienteDTO.class))).thenReturn(nuevoCliente);

                mockMvc.perform(post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(nuevoCliente)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.numeroIdentificacion").value("1234567890"));
        }

        // Caso: Crear cliente - error por identificacion duplicada
// Este test verifica que si se intenta crear un cliente con una identificacion ya existente,
// el servicio retorna correctamente un error 409 (CONFLICT), cumpliendo con la validacion requerida
// de duplicacion del numero de identificacion.
        @Test
        @DisplayName("Crear cliente - error por identificacion duplicada")
        void crearCliente_Conflict() throws Exception {
                ClienteDTO nuevoCliente = ClienteDTO.builder()
                                .numeroIdentificacion("1234567890")
                                .tipoIdentificacion(TipoIdentificacion.CEDULA)
                                .build();

                when(clienteService.crearCliente(any(ClienteDTO.class)))
                                .thenThrow(new ConflictException("Identificacion ya existe"));

                mockMvc.perform(post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(nuevoCliente)))
                                .andExpect(status().isConflict());
        }

        // Caso: Editar datos del cliente
        // Este test simula la actualizacion de los datos de un cliente existente a traves del endpoint
        // PUT /api/clientes/{id}. Verifica que el cliente es actualizado correctamente y que se retorna
        // el objeto actualizado en formato JSON, cumpliendo el requerimiento de editar cliente.
        @Test
        @DisplayName("Editar cliente existente - exito")
        void editarCliente_Success() throws Exception {
                ClienteDTO clienteEditado = ClienteDTO.builder()
                                .numeroIdentificacion("1234567890")
                                .nombres("Cliente Editado")
                                .tipoIdentificacion(TipoIdentificacion.CEDULA)
                                .build();

                when(clienteService.editarCliente(eq(1L), any(ClienteDTO.class))).thenReturn(clienteEditado);

                mockMvc.perform(put("/api/clientes/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clienteEditado)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nombres").value("Cliente Editado"));
        }

        // Caso: Editar cliente - error por conflicto
        // Este test asegura que si se intenta editar un cliente asignandole una identificacion ya existente
        // en otro cliente, se lanza correctamente un error 409 (CONFLICT), validando la unicidad en la actualizacion.
        @Test
        @DisplayName("Editar cliente - error por conflicto")
        void editarCliente_Conflict() throws Exception {
                ClienteDTO clienteEditado = ClienteDTO.builder()
                                .numeroIdentificacion("1234567890")
                                .tipoIdentificacion(TipoIdentificacion.CEDULA)
                                .build();

                when(clienteService.editarCliente(eq(1L), any(ClienteDTO.class)))
                                .thenThrow(new ConflictException("Identificacion ya existe"));

                mockMvc.perform(put("/api/clientes/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clienteEditado)))
                                .andExpect(status().isConflict());
        }

        // Caso: Eliminar cliente
        // Este test valida el endpoint DELETE /api/clientes/{id}, simulando la eliminacion de un cliente.
        // Confirma que la respuesta sea 204 (NO CONTENT), lo cual indica una eliminacion exitosa.
        @Test
        @DisplayName("Eliminar cliente - exito")
        void eliminarCliente_Success() throws Exception {
                doNothing().when(clienteService).eliminarCliente(1L);

                mockMvc.perform(delete("/api/clientes/1"))
                                .andExpect(status().isNoContent());
        }

        // Caso: Registrar direccion adicional por cliente
// Este test verifica que el endpoint POST /api/clientes/direcciones/{clienteId} permite
// registrar una nueva direccion asociada a un cliente existente, enviando los datos en JSON
// y recibiendo una respuesta exitosa.
        @Test
        @DisplayName("Registrar direccion adicional - exito")
        void registrarDireccion_Success() throws Exception {
                String direccionJson = """
                                {
                                    "provincia": "Guayas",
                                    "ciudad": "Guayaquil",
                                    "direccion": "Av. 9 de Octubre",
                                    "matriz": false
                                }
                                """;

                when(direccionService.registrarDireccion(eq(1L), any())).thenReturn(DireccionDTO.builder().build());

                mockMvc.perform(post("/api/clientes/direcciones/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(direccionJson))
                                .andExpect(status().isOk());
        }

        // Caso: Listar direcciones del cliente
        // Este test simula una peticion GET al endpoint /api/clientes/direcciones/{clienteId}
        // para obtener todas las direcciones asociadas al cliente, incluyendo la direccion matriz.
        // Confirma que se retorna un listado en formato JSON con los datos esperados.
        @Test
        @DisplayName("Listar direcciones del cliente - exito")
        void listarDirecciones_Success() throws Exception {
                DireccionDTO direccionMock = DireccionDTO.builder()
                                .id(1L)
                                .provincia("Pichincha")
                                .ciudad("Quito")
                                .direccion("Av. Naciones Unidas")
                                .matriz(true)
                                .build();

                when(direccionService.obtenerDireccionesDeCliente(eq(1L)))
                                .thenReturn(List.of(direccionMock));

                mockMvc.perform(get("/api/clientes/direcciones/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].provincia").value("Pichincha"))
                                .andExpect(jsonPath("$[0].ciudad").value("Quito"))
                                .andExpect(jsonPath("$[0].direccion").value("Av. Naciones Unidas"))
                                .andExpect(jsonPath("$[0].matriz").value(true));
        }
}
