package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.EjercicioDTO;
import com.gym.gym_ver2.domain.model.dto.ExercisesCreateDTO;
import com.gym.gym_ver2.domain.model.entity.Ejercicio;
import com.gym.gym_ver2.infraestructure.exceptions.RecursoNoEncontradoException;
import com.gym.gym_ver2.infraestructure.persistence.repository.EjercicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EjercicioServiceImpl implements EjercicioService {

    private final EjercicioRepository ejercicioRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public List<EjercicioDTO> obtenerEjercicios() {

        List<Ejercicio> ejercicios = ejercicioRepository.findAll();

        if( ejercicios.isEmpty() ) {
            throw new RecursoNoEncontradoException("No se encontraron ejercicios");
        }

        return ejercicios.stream().map(ejercicio -> new EjercicioDTO(
                ejercicio.getIdEjercicio(),
                ejercicio.getNombreEjercicio(),
                ejercicio.getDescripcionEjercicio(),
                ejercicio.getFotoEjercicio(),
                ejercicio.getMusculos(),
                ejercicio.getMet()
        )).toList();

    }

    @Override
    public EjercicioDTO crearEjercicio(ExercisesCreateDTO ejercicioDTO) {
        String imagenUrl = null;
        String imagenPublicId = null;
        MultipartFile image = ejercicioDTO.getFotoEjercicio();

        if (image != null && !image.isEmpty()) {
            try {
                var imageCloudinary = cloudinaryService.uploadImage(image,  "ejercicios");
                imagenUrl = imageCloudinary.get("url");
                imagenPublicId = imageCloudinary.get("public_id");
            } catch (Exception e) {
                throw new RuntimeException("Error al subir la imagen: " + e.getMessage());
            }
        }else {
            imagenUrl = "default_image_url";
            imagenPublicId = "default_public_id";
        }
        Ejercicio ejercicio = Ejercicio.builder()
                .nombreEjercicio(ejercicioDTO.getNombreEjercicio())
                .descripcionEjercicio(ejercicioDTO.getDescripcionEjercicio())
                .fotoEjercicio(imagenUrl)
                .musculos(ejercicioDTO.getMusculos())
                .met(ejercicioDTO.getMet())
                .imagePublicId(imagenPublicId)
                .build();

        Ejercicio nuevoEjercicio = ejercicioRepository.save(ejercicio);

        return new EjercicioDTO(
                nuevoEjercicio.getIdEjercicio(),
                nuevoEjercicio.getNombreEjercicio(),
                nuevoEjercicio.getDescripcionEjercicio(),
                nuevoEjercicio.getFotoEjercicio(),
                nuevoEjercicio.getMusculos(),
                nuevoEjercicio.getMet()
        );
    }
}
