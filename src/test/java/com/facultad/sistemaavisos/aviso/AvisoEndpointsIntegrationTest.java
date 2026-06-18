package com.facultad.sistemaavisos.aviso;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "security.jwt.secret=test-secret-pala-2026-test-secret-pala-2026"
})
@Sql(scripts = "/sql/cleanup-aviso-endpoints.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/seed-aviso-endpoints.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class AvisoEndpointsIntegrationTest {

    private static final String TEST_SECRET = "test-secret-pala-2026-test-secret-pala-2026";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtieneSoporteDelFormularioConEmpresasCarrerasYTiposActivos() throws Exception {
        mockMvc.perform(get("/api/reclutadores/{reclutadorId}/avisos/soporte", 1L)
                        .header("Authorization", bearerReclutador("reclu@test.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empresasActivas.length()").value(1))
                .andExpect(jsonPath("$.empresasActivas[0].id").value(1))
                .andExpect(jsonPath("$.carrerasActivas.length()").value(2))
                .andExpect(jsonPath("$.tiposAvisoActivos.length()").value(2))
                .andExpect(jsonPath("$.tiposAvisoActivos[0].subTiposAviso.length()").isNotEmpty());
    }

    @Test
    void ejecutaFlujoDeCrearEditarPausarReanudarYCancelarAviso() throws Exception {
        final String crearRequest = """
                {
                  "nombreAviso": "Backend Java",
                  "descripcionAviso": "Busqueda backend SR",
                  "fechaPublicacionAviso": null,
                  "fechaCierreAviso": "%s",
                  "imagenUrlAviso": "https://img.test/aviso.png",
                  "empresaId": 1,
                  "guardarComoBorrador": true,
                  "carreras": [
                    { "carreraId": 1, "prioridad": 1 },
                    { "carreraId": 2, "prioridad": 2 }
                  ],
                  "tiposAviso": [
                    { "tipoAvisoId": 1, "subTipoAvisoIds": [1] },
                    { "tipoAvisoId": 2, "subTipoAvisoIds": [3] }
                  ]
                }
                """.formatted(Instant.parse("2026-07-01T00:00:00Z"));

        final String crearResponse = mockMvc.perform(post("/api/reclutadores/{reclutadorId}/avisos", 1L)
                        .header("Authorization", bearerReclutador("reclu@test.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(crearRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estadoActual.codigoInterno").value("BORRADOR"))
                .andExpect(jsonPath("$.empresa.id").value(1))
                .andExpect(jsonPath("$.carreras.length()").value(2))
                .andExpect(jsonPath("$.tiposAviso.length()").value(2))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final Long avisoId = objectMapper.readTree(crearResponse).path("id").asLong();
        assertThat(avisoId).isNotNull();

        mockMvc.perform(get("/api/reclutadores/{reclutadorId}/avisos/{avisoId}", 1L, avisoId)
                        .header("Authorization", bearerReclutador("reclu@test.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(avisoId))
                .andExpect(jsonPath("$.estadoActual.codigoInterno").value("BORRADOR"));

        final String actualizarRequest = """
                {
                  "nombreAviso": "Backend Java Actualizado",
                  "descripcionAviso": "Busqueda backend SR con guardias",
                  "fechaPublicacionAviso": "2026-06-18T12:00:00Z",
                  "fechaCierreAviso": "%s",
                  "imagenUrlAviso": "https://img.test/aviso-v2.png",
                  "empresaId": 1,
                  "guardarComoBorrador": false,
                  "carreras": [
                    { "carreraId": 2, "prioridad": 1 }
                  ],
                  "tiposAviso": [
                    { "tipoAvisoId": 1, "subTipoAvisoIds": [2] },
                    { "tipoAvisoId": 2, "subTipoAvisoIds": [3, 4] }
                  ]
                }
                """.formatted(Instant.parse("2026-07-15T00:00:00Z"));

        mockMvc.perform(put("/api/reclutadores/{reclutadorId}/avisos/{avisoId}", 1L, avisoId)
                        .header("Authorization", bearerReclutador("reclu@test.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(actualizarRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreAviso").value("Backend Java Actualizado"))
                .andExpect(jsonPath("$.estadoActual.codigoInterno").value("ABIERTO"))
                .andExpect(jsonPath("$.carreras.length()").value(1))
                .andExpect(jsonPath("$.carreras[0].id").value(2))
                .andExpect(jsonPath("$.tiposAviso[0].subTiposAviso.length()").isNotEmpty());

        mockMvc.perform(patch("/api/reclutadores/{reclutadorId}/avisos/{avisoId}/pausar", 1L, avisoId)
                        .header("Authorization", bearerReclutador("reclu@test.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoActual.codigoInterno").value("PAUSADO"));

        mockMvc.perform(patch("/api/reclutadores/{reclutadorId}/avisos/{avisoId}/reanudar", 1L, avisoId)
                        .header("Authorization", bearerReclutador("reclu@test.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoActual.codigoInterno").value("ABIERTO"));

        final String cancelarResponse = mockMvc.perform(
                        patch("/api/reclutadores/{reclutadorId}/avisos/{avisoId}/cancelar", 1L, avisoId)
                                .header("Authorization", bearerReclutador("reclu@test.com"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoActual.codigoInterno").value("CANCELADO"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final JsonNode cancelado = objectMapper.readTree(cancelarResponse);
        assertThat(cancelado.path("tiposAviso").size()).isEqualTo(2);
        assertThat(cancelado.path("carreras").size()).isEqualTo(1);
    }

    private String bearerReclutador(String mail) {
        final SecretKey secretKey = Keys.hmacShaKeyFor(TEST_SECRET.getBytes(StandardCharsets.UTF_8));
        final Date ahora = new Date();
        final Date expiracion = new Date(ahora.getTime() + 60_000);

        final String token = Jwts.builder()
                .subject("1")
                .claim("mail", mail)
                .claim("roles", List.of("Reclutador"))
                .claim("permisos", List.of("AVISO_CREAR", "AVISO_EDITAR"))
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(secretKey)
                .compact();

        return "Bearer " + token;
    }
}
