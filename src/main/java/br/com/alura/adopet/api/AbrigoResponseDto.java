package br.com.alura.adopet.api;

import br.com.alura.adopet.api.model.Pet;

import java.util.List;

public record AbrigoResponseDto(Long id, String nome, String telefone, String email, List<Pet> pets) {

}
