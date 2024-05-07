package br.com.alura.adopet.api.dto.abrigo;

import br.com.alura.adopet.api.model.Pet;

import java.util.List;

public record AbrigoRequestDto(String nome, String telefone, String email, List<Pet> pets) {
}
