package com.gym.gym_ver2.aplicaction.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.gym_ver2.domain.model.dto.RutinaAprendizDTO;
import com.gym.gym_ver2.domain.model.dto.RutinaCreateDTO;
import com.gym.gym_ver2.domain.model.dto.RutinaDTO;
import com.gym.gym_ver2.domain.model.dto.SolicitudRutinaDTO;
import com.gym.gym_ver2.domain.model.entity.*;
import com.gym.gym_ver2.infraestructure.config.OpenAiProperties;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RutinaServiceImpl implements  RutinaService {

    private final OpenAiProperties openAiProperties;
    private static final Logger logger = LoggerFactory.getLogger(RutinaServiceImpl.class);
    private final RutinaRepository rutinaRepo;
    private final EjercicioRepository ejercicioRepo;
    private final RutinaEjerciciosRepository rutinaEjercicioRepo;
    private final CloudinaryService cloudinaryService;
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private final UsuarioRepository usuarioRepository;
    private final AprendizRepository aprendizRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public RutinaDTO crearRutina(RutinaCreateDTO rutinaDTO) {

        String imageUrl = null;
        String imagePublicId = null;

        MultipartFile file = rutinaDTO.getFotoRutina();

        if (file != null && !file.isEmpty()) {
            try{
                var result = cloudinaryService.uploadImage(file, "rutinas");
                imageUrl = result.get("url");
                imagePublicId = result.get("public_id");
            }catch (Exception e){
                throw new RuntimeException("Error al subir la imagen: " + e.getMessage());
            }

        }else{
            imageUrl = "default.png";
            imagePublicId = "default";
        }

        int puntaje = switch (rutinaDTO.getDificultad()) {
            case PRINCIPIANTE -> 100;
            case INTERMEDIO   -> 200;
            case AVANZADO     -> 300;
        };

        // 1. Crear y guardar la rutina
        Rutina rutina = Rutina.builder()
                .nombre(rutinaDTO.getNombre())
                .descripcion(rutinaDTO.getDescripcion())
                .fotoRutina(imageUrl)
                .imagePublicId(imagePublicId)
                .enfoque(rutinaDTO.getEnfoque())
                .dificultad(rutinaDTO.getDificultad())
                .build();

        Rutina savedRutina = rutinaRepo.save(rutina);

        // 2. Mapear y guardar los ejercicios asociados a la rutina
        try {
            List<RutinaEjercicio> rutinaEjercicios = rutinaDTO.getEjercicios().stream().map(ejDto -> {
                // Verificar si el ejercicio existe
                Ejercicio ejercicio = ejercicioRepo.findById(ejDto.getIdEjercicio())
                        .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado con ID: " + ejDto.getIdEjercicio()));

                // Crear RutinaEjercicio y asociarlo a la rutina y ejercicio
                return RutinaEjercicio.builder()
                        .rutina(savedRutina)
                        .ejercicio(ejercicio)
                        .series(ejDto.getSeries())
                        .repeticiones(ejDto.getRepeticion())
                        .carga(ejDto.getCarga())
                        .duracion(ejDto.getDuracion())
                        .asignacion(false)
                        .build();
            }).collect(Collectors.toList());

            System.out.println("Total de RutinaEjercicio generados: " + rutinaEjercicios.size());

            rutinaEjercicioRepo.saveAll(rutinaEjercicios);
            System.out.println("RutinaEjercicios guardados correctamente.");

            // 3. Mapear los ejercicios al DTO interno
            List<RutinaDTO.RutinaEjercicioDTO> ejercicioDTOs = rutinaEjercicios.stream().map(re -> {
                Ejercicio ej = re.getEjercicio();

                return RutinaDTO.RutinaEjercicioDTO.builder()
                        .idEjercicio(ej.getIdEjercicio())
                        .nombre(ej.getNombreEjercicio())
                        .descripcion(ej.getDescripcionEjercicio())
                        .musculos(ej.getMusculos())
                        .series(re.getSeries())
                        .repeticion(re.getRepeticiones())
                        .carga(re.getCarga())
                        .duracion(re.getDuracion())
                        .asignacion(re.getAsignacion())
                        .build();
            }).collect(Collectors.toList());

            // 4. Retornar la rutina con ejercicios incluidos
            return RutinaDTO.builder()
                    .idRutina(savedRutina.getIdRutina())
                    .nombre(savedRutina.getNombre())
                    .descripcion(savedRutina.getDescripcion())
                    .fotoRutina(savedRutina.getFotoRutina())
                    .enfoque(savedRutina.getEnfoque())
                    .dificultad(savedRutina.getDificultad())
                    .ejercicios(ejercicioDTOs)
                    .build();

        } catch (Exception e) {
            System.err.println("Error al guardar RutinaEjercicios:");
            e.printStackTrace();
        }
        throw new RuntimeException("Error al crear rutina: " + rutinaDTO.getNombre());
    }

    @Override
    public List<RutinaDTO> obtenerRutinas() {
        // Obtener todas las rutinas desde el repositorio
        List<Rutina> rutinas = rutinaRepo.findAll();

        return rutinas.stream().map(rutina -> {
            // Obtener los ejercicios relacionados a la rutina
            List<RutinaEjercicio> ejercicios = rutinaEjercicioRepo.findByRutina(rutina);
//            logger.info("Rutina: {} -> ejercicios: {}", rutina.getNombre(), ejercicios.size());
            // Convertir RutinaEjercicio a EjercicioDTO
            List<RutinaDTO.RutinaEjercicioDTO> ejercicioDTOs = ejercicios.stream()
                    .map( re -> {
                        Ejercicio ejercicio = re.getEjercicio();
                        return RutinaDTO.RutinaEjercicioDTO.builder()
                                .idEjercicio(ejercicio.getIdEjercicio())
                                .nombre(ejercicio.getNombreEjercicio())
                                .descripcion(ejercicio.getDescripcionEjercicio())
                                .musculos(ejercicio.getMusculos())
                                .repeticion(re.getRepeticiones())
                                .series(re.getSeries())
                                .duracion(re.getDuracion())
                                .carga(re.getCarga())
                                .build();
                    })
                    .collect(Collectors.toList());

            return RutinaDTO.builder()
                    .idRutina(rutina.getIdRutina())
                    .nombre(rutina.getNombre())
                    .descripcion(rutina.getDescripcion())
                    .fotoRutina(rutina.getFotoRutina())
                    .enfoque(rutina.getEnfoque())
                    .dificultad(rutina.getDificultad())
                    .ejercicios(ejercicioDTOs)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarRutina(Integer idRutina) {
        try {
            logger.info("Verificando existencia de rutina ID: {}", idRutina);

            if (!rutinaRepo.existsById(idRutina)) {
                logger.warn("Rutina no encontrada con ID: {}", idRutina);
                throw new RuntimeException("Rutina no encontrada con ID: " + idRutina);
            }

            logger.info("Eliminando ejercicios asociados por SQL nativo...");
            rutinaEjercicioRepo.eliminarTodoPorRutina(idRutina);
            entityManager.flush();
            entityManager.clear();

            logger.info("Ejercicios eliminados. Procediendo a eliminar rutina por ID directamente...");
            rutinaRepo.deleteById(idRutina);

            logger.info("Rutina con ID {} eliminada correctamente.", idRutina);
        } catch (Exception e) {
            logger.error("Fallo al eliminar rutina ID {}: {}", idRutina, e.getMessage(), e);
            throw new RuntimeException("Error crítico al eliminar rutina: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public RutinaDTO actualizarRutina(Integer id, RutinaCreateDTO rutinaDTO) throws IOException {//todo: AGREGAR LA FOTO DE CADA AJERCICIO EN AL LA RESPUESTA

        String imageUrl = null;
        String imagePublicId = null;
        MultipartFile file = rutinaDTO.getFotoRutina();

        // 1. Buscar la rutina
        Rutina rutina = rutinaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada con ID: " + id));

        imageUrl = rutina.getFotoRutina();
        imagePublicId = rutina.getImagePublicId();

        if (file != null && !file.isEmpty()) {
            try {
                var result = cloudinaryService.uploadImage(file, "rutinas");
                imageUrl = result.get("url");
                imagePublicId = result.get("public_id");
            } catch (Exception e) {
                throw new RuntimeException("Error al subir la imagen: " + e.getMessage());
            }
        }

        // 2. Actualizar datos generales
        rutina.setNombre(rutinaDTO.getNombre());
        rutina.setFotoRutina(imageUrl);
        rutina.setDescripcion(rutinaDTO.getDescripcion());
        rutina.setImagePublicId(imagePublicId);
        rutina.setEnfoque(rutinaDTO.getEnfoque());
        rutina.setDificultad(rutinaDTO.getDificultad());

        // 3. Guardar cambios de la rutina
        rutinaRepo.save(rutina);

        // 4. Eliminar ejercicios antiguos por ID de rutina
        rutinaEjercicioRepo.eliminarPorRutinaId(rutina.getIdRutina());

        // 5. Crear nuevos RutinaEjercicio y guardar directamente
        List<RutinaEjercicio> nuevos = new ArrayList<>();

        //6 validar que la lista de ejercicios no esté vacía
        for (RutinaCreateDTO.RutinaEjercicioDTO ejDto : rutinaDTO.getEjercicios()) {
            Ejercicio ejercicio = ejercicioRepo.findById(ejDto.getIdEjercicio())
                    .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));

            RutinaEjercicio nuevo = RutinaEjercicio.builder()
                    .rutina(rutina)
                    .ejercicio(ejercicio)
                    .series(ejDto.getSeries())
                    .repeticiones(ejDto.getRepeticion())
                    .carga(ejDto.getCarga())
                    .duracion(ejDto.getDuracion())
                    .build();

            nuevos.add(nuevo);
        }

        rutinaEjercicioRepo.saveAll(nuevos); // sin tocar la lista en la entidad RutinaEjercicio

        // 6. DTO de respuesta
        List<RutinaDTO.RutinaEjercicioDTO> ejercicioDTOs = nuevos.stream().map(re -> {
            Ejercicio ej = re.getEjercicio();
            return RutinaDTO.RutinaEjercicioDTO.builder()
                    .idEjercicio(ej.getIdEjercicio())
                    .nombre(ej.getNombreEjercicio())
                    .descripcion(ej.getDescripcionEjercicio())
                    .musculos(ej.getMusculos())
                    .repeticion(re.getRepeticiones())
                    .series(re.getSeries())
                    .duracion(re.getDuracion())
                    .carga(re.getCarga())
                    .build();
        }).collect(Collectors.toList());

        return RutinaDTO.builder()
                .idRutina(rutina.getIdRutina())
                .nombre(rutina.getNombre())
                .descripcion(rutina.getDescripcion())
                .fotoRutina(rutina.getFotoRutina())
                .enfoque(rutina.getEnfoque())
                .dificultad(rutina.getDificultad())
                .ejercicios(ejercicioDTOs)
                .build();
    }

    @Override
    public RutinaDTO obtenerRutinaPorId(Integer id) {
        Rutina rutina = rutinaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada con ID: " + id));

        List<RutinaEjercicio> ejercicios = rutinaEjercicioRepo.findByRutina(rutina);

        List<RutinaDTO.RutinaEjercicioDTO> ejercicioDTOs = ejercicios.stream().map(re -> {
            Ejercicio ej = re.getEjercicio();
//            RutinaEjercicio rutinaEjercicio = re
            return RutinaDTO.RutinaEjercicioDTO.builder()
                    .idRutinaEjercicio(re.getIdRutinaEjercicio())
                    .idEjercicio(ej.getIdEjercicio())
                    .nombre(ej.getNombreEjercicio())
                    .descripcion(ej.getDescripcionEjercicio())
                    .musculos(ej.getMusculos())
                    .repeticion(re.getRepeticiones())
                    .series(re.getSeries())
                    .duracion(re.getDuracion())
                    .met(ej.getMet())
                    .carga(re.getCarga())
                    .build();
        }).collect(Collectors.toList());

        return RutinaDTO.builder()
                .idRutina(rutina.getIdRutina())
                .nombre(rutina.getNombre())
                .descripcion(rutina.getDescripcion())
                .fotoRutina(rutina.getFotoRutina())
                .enfoque(rutina.getEnfoque())
                .dificultad(rutina.getDificultad())
                .ejercicios(ejercicioDTOs)
                .build();
    }

    @Override
    public String generarRutinaConIA(SolicitudRutinaDTO datos) {
        String prompt=construirPrompt(datos);
        String apikey = openAiProperties.getApiKey();

        if (apikey == null || apikey.isEmpty()) {
            throw new RuntimeException("API Key de OpenAI no configurada");
        }
        if (prompt.isEmpty()) {
            throw new RecursoNoEncontradoException("No se pudo construir el prompt para la IA");
        }
        try {
            OkHttpClient client = new OkHttpClient();
            String bodyJson = "{\n" +
                    "  \"model\": \"gpt-3.5-turbo\",\n" +
                    "  \"messages\": [\n" +
                    "    {\"role\": \"system\", \"content\": \"Eres un experto en entrenamiento físico y nutrición.\"},\n" +
                    "    {\"role\": \"user\", \"content\": \"" + prompt + "\"}\n" +
                    "  ],\n" +
                    "  \"max_tokens\": 1500,\n" +
                    "  \"temperature\": 0.7\n" +
                    "}";

            Request request = new Request.Builder()
                    .url(OPENAI_URL)
                    .addHeader("Authorization", "Bearer " + apikey)
                    .post(okhttp3.RequestBody.create(bodyJson, okhttp3.MediaType.parse("application/json")))
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String respuestaJson = response.body().string();
                // Extrae el texto de la respuesta
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(respuestaJson);
                String rutina = root.path("choices").get(0).path("message").path("content").asText();
                return rutina;
            }else {
                String errorBody = response.body().string();
                logger.error("Código HTTP OpenAI: {}", response.code());
                logger.error("Cuerpo de error OpenAI: {}", errorBody);
                return "error al consultar OPenAI: " + errorBody;
            }

        } catch (Exception e) {
            logger.error("Error al llamar a la IA: {}", e.getMessage());
            e.printStackTrace();
            return "error al consultar OPenAI" + e.getMessage();
        }
    }

    @Override
    public List<RutinaAprendizDTO> getRutinaByAprendiz(int idUsuario) {

        Usuario user = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        Integer idPersona = user.getPersona().getIdPersona();
        Aprendiz aprendiz = aprendizRepository.findById(idPersona)
                .orElseThrow(() -> new RecursoNoEncontradoException("Aprendiz no encontrado"));

        Integer idAprendiz = aprendiz.getIdPersona();

        return rutinaRepo.obtenerRutinasPorAprendiz(idAprendiz);
    }

    private String construirPrompt(SolicitudRutinaDTO datos) {
        return String.format(
                "Género: %s, Edad: %d, Altura: %d cm, Peso: %d kg,  Objetivo: %s, Lesiones: %s, Nivel: %s, Frecuencia entrenos a la semana: %s, Lugar de entrenamiento: %s, frecuencia cardiaca: %d " +
                        "Genera una rutina semanal detallada de ejercicios acorde a estos datos. Incluye calentamiento, ejercicios principales y estiramiento. Especifica series, repeticiones, carga, tiempos de descanso y advertencias si es necesario.",
                datos.getSexo(), datos.getEdad(), datos.getAltura(), datos.getPeso(),
                datos.getObjetivo(), datos.getLesiones(), datos.getNivel(),
                datos.getFrecuencia(), datos.getUbicacion(), datos.getFrecuenciaCardio()
        );
    }

}
