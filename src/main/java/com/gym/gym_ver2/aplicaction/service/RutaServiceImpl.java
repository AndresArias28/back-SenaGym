package com.gym.gym_ver2.aplicaction.service;

import com.gym.gym_ver2.domain.model.dto.RutasRequest;
import com.gym.gym_ver2.domain.model.dto.RutasResponse;
import com.gym.gym_ver2.domain.model.entity.Ruta;
import com.gym.gym_ver2.infraestructure.repository.RutasRepository;
import org.springframework.stereotype.Service;

@Service
public class RutaServiceImpl implements  RutasService{

    private final RutasRepository rutasRepository;

    public RutaServiceImpl(RutasRepository rutasRepository) {
        this.rutasRepository = rutasRepository;
    }

    @Override
    public RutasResponse save(RutasRequest rutasRequest) {

        Ruta  rute = Ruta.builder()
                .origen(rutasRequest.getOrigen())
                .destino(rutasRequest.getDestino())
                .distanciaKm(rutasRequest.getDistancia())
                .build();


        Ruta nuevaRuta = rutasRepository.save(rute);

        return new RutasResponse(
                nuevaRuta.getIdRuta(),
                nuevaRuta.getOrigen(),
                nuevaRuta.getDestino(),
                nuevaRuta.getDistanciaKm()

        );

    }
}
