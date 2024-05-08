package br.com.alura.adopet.api.service.validacoes;

import br.com.alura.adopet.api.dto.adocao.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoExcepition;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TutorComLimiteDeAdocoes implements ValidacoesSolicitarAdocao {
    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private AdocaoRepository adocaoRepository;
    public void validar(SolicitacaoAdocaoDto dto){
        List<Adocao> adocoes= adocaoRepository.findAll();
        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());
        for (Adocao a : adocoes) {
            int contador = 0;
            if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.APROVADO) {
                contador = contador + 1;
            }
            if (contador == 5) {
                throw new ValidacaoExcepition("Tutor chegou ao limite máximo de 5 adoções!");
//                    return ResponseEntity.badRequest().body("Tutor chegou ao limite máximo de 5 adoções!");
            }
        }
    }
}
