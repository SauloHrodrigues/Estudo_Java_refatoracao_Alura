package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.adocao.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.adocao.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.adocao.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoExcepition;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdocaoService {
    @Autowired
    private AdocaoRepository adocaoRepository;
    @Autowired
    private PetRepository petRepository;

    private TutorRepository tutorRepository;

    @Autowired
    private EmailSevice emailSevice;

    public void solicitar(SolicitacaoAdocaoDto dto){
        Pet pet= petRepository.getReferenceById(dto.idPet());
        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());

//        if (adocao.getPet().getAdotado() == true) { 3:59
        /*
//                     PRIMEIRA REGRA  - PET DISPONIVEL?
        if (pet.getAdotado() == true) {
            throw new ValidacaoExcepition("Pet já foi adotado!");
//            return ResponseEntity.badRequest().body("Pet já foi adotado!");
        } */

//        else {
//            List<Adocao> adocoes = adocaoRepository.findAll();
//            for (Adocao a : adocoes) {

//          SEGUNDA VALIDAÇÃO
//                if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
//                    throw new ValidacaoExcepition("Tutor já possui outra adoção aguardando avaliação!");
////                    return ResponseEntity.badRequest().body("Tutor já possui outra adoção aguardando avaliação!");
//                }
//            }
//        TERCEIRA AVALIAÇÃO
//            for (Adocao a : adocoes) {
//                if (a.getPet() == pet && a.getStatus() == StatusAdocao.AGUARDANDO_AVALIACAO) {
//                    throw new ValidacaoExcepition("Pet já está aguardando avaliação para ser adotado!");
////                    return ResponseEntity.badRequest().body("Pet já está aguardando avaliação para ser adotado!");
//                }
//            }

//        QUARTA AVALIACAO - APENAS 5 PETS
//            for (Adocao a : adocoes) {
//                int contador = 0;
//                if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.APROVADO) {
//                    contador = contador + 1;
//                }
//                if (contador == 5) {
//                    throw new ValidacaoExcepition("Tutor chegou ao limite máximo de 5 adoções!");
////                    return ResponseEntity.badRequest().body("Tutor chegou ao limite máximo de 5 adoções!");
//                }
//            }
//        }
        Adocao adocao = new Adocao();
        adocao.setData(LocalDateTime.now());
        adocao.setStatus(StatusAdocao.AGUARDANDO_AVALIACAO);
        adocao.setPet(pet);
        adocao.setTutor(tutor);
        adocao.setMotivo(dto.motivo());
        adocaoRepository.save(adocao);
        emailSevice.enviarEmail(adocao.getPet().getAbrigo().getEmail(),
                "Solicitação de adoção",
                "Olá " + adocao.getPet().getAbrigo().getNome() +
                        "!\n\nUma solicitação de adoção foi registrada hoje para o pet: " +
                        adocao.getPet().getNome() + ". Favor avaliar para aprovação ou reprovação."   );
    }

    public void aprovar(AprovacaoAdocaoDto dto){
        Adocao adocao = adocaoRepository.getReferenceById(dto.idAdocao());
        adocao.setStatus(StatusAdocao.APROVADO);
        emailSevice.enviarEmail(adocao.getPet().getAbrigo().getEmail(),
                "Adoção aprovada",
                "Parabéns " + adocao.getTutor().getNome() +
                        "!\n\nSua adoção do pet " + adocao.getPet().getNome() + ", solicitada em "
                        + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
                        ", foi aprovada.\nFavor entrar em contato com o abrigo " +
                        adocao.getPet().getAbrigo().getNome() + " para agendar a busca do seu pet.");
    }

    public void reprovar(ReprovacaoAdocaoDto dto){
        Adocao adocao = adocaoRepository.getReferenceById(dto.idAdocao());
        adocao.setStatus(StatusAdocao.REPROVADO);
//        adocaoRepository.save(adocao); não precisa
        emailSevice.enviarEmail(adocao.getPet().getAbrigo().getEmail(),
                "Adoção reprovada",
                "Olá " + adocao.getTutor().getNome() + "!\n\nInfelizmente sua adoção do pet " +
                        adocao.getPet().getNome() + ", solicitada em " +
                        adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
                        ", foi reprovada pelo abrigo " + adocao.getPet().getAbrigo().getNome() +
                        " com a seguinte justificativa: " + adocao.getJustificativaStatus());

    }
}
