package br.com.alura.adopet.api.service.validacoes;

import br.com.alura.adopet.api.dto.adocao.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoExcepition;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PetDisponivel {
    @Autowired
    private PetRepository petRepository;

    public boolean validar(SolicitacaoAdocaoDto dto){
        Pet pet= petRepository.getReferenceById(dto.idPet());
        if (pet.getAdotado()) {
            throw new ValidacaoExcepition("Pet já foi adotado!");
        }
        return true;
    }
}
