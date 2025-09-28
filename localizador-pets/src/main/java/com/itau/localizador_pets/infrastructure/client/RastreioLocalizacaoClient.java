package com.itau.localizador_pets.infrastructure.client;

import com.itau.localizador_pets.infrastructure.dto.LocalizaoClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "positionStack", url = "${positionstack.api.url}")
public interface RastreioLocalizacaoClient{

    @GetMapping("/reverse")
    LocalizaoClientResponse buscarLocalizacao(@RequestParam("access_key") String chaveAcesso,
                                              @RequestParam("query") String coordenadas);
}
