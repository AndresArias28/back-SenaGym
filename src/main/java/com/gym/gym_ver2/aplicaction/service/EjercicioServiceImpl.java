package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.EjercicioDTO;
import com.gym.gym_ver2.domain.model.dto.ExcerciseDTO;
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
    @Transactional
    public EjercicioDTO crearEjercicio(ExcerciseDTO ejercicioDTO, MultipartFile fotoEjercicio) {
        String imagenUrl = null;
        String imagenPublicId = null;
        MultipartFile image = fotoEjercicio;

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

    @Transactional
    @Override
    public EjercicioDTO actualizarEjercicio(Integer id, ExercisesCreateDTO datos) {
        Ejercicio ejercicio = ejercicioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ejercicio no encontrado con ID: " + id));

        if (datos.getFotoEjercicio() != null && !datos.getFotoEjercicio().isEmpty()) {
            try {
                var imageCloudinary = cloudinaryService.uploadImage(datos.getFotoEjercicio(), "ejercicios");
                ejercicio.setFotoEjercicio(imageCloudinary.get("url"));
                ejercicio.setImagePublicId(imageCloudinary.get("public_id"));
            } catch (Exception e) {
                throw new RuntimeException("Error al subir la imagen: " + e.getMessage());
            }
        }

       if(datos.getNombreEjercicio() != null && !datos.getNombreEjercicio().isEmpty()) {
            ejercicio.setNombreEjercicio(datos.getNombreEjercicio());
        }

        if(datos.getDescripcionEjercicio() != null && !datos.getDescripcionEjercicio().isEmpty()) {
            ejercicio.setDescripcionEjercicio(datos.getDescripcionEjercicio());
        }

        if(datos.getMusculos() != null && !datos.getMusculos().isEmpty()) {
            ejercicio.setMusculos(datos.getMusculos());
        }

        if(datos.getMet() != null) {
            ejercicio.setMet(datos.getMet());
        }

        // Actualizar los campos del ejercicio
//       }
//        ejercicio.setNombreEjercicio(datos.getNombreEjercicio());
//        ejercicio.setDescripcionEjercicio(datos.getDescripcionEjercicio());
//        ejercicio.setMusculos(datos.getMusculos());
//        ejercicio.setMet(datos.getMet());

        Ejercicio ejercicioActualizado = ejercicioRepository.save(ejercicio);

        return new EjercicioDTO(
                ejercicioActualizado.getIdEjercicio(),
                ejercicioActualizado.getNombreEjercicio(),
                ejercicioActualizado.getDescripcionEjercicio(),
                ejercicioActualizado.getFotoEjercicio(),
                ejercicioActualizado.getMusculos(),
                ejercicioActualizado.getMet()
        );
    }

    @Override
    public void eliminarEjercicio(Integer id) {
        Ejercicio ejercicio = ejercicioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ejercicio no encontrado con ID: " + id));

        // Eliminar la imagen de Cloudinary si existe
        if (ejercicio.getImagePublicId() != null) {
            cloudinaryService.deleteImage(ejercicio.getImagePublicId());
        }

        ejercicioRepository.delete(ejercicio);

    }
}
