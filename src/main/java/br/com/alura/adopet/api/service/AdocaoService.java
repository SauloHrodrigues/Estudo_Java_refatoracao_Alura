package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.adocao.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.adocao.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.adocao.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.service.validacoes.ValidacoesSolicitarAdocao;
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

    @Autowired
    private List<ValidacoesSolicitarAdocao> listDeValidardores;

    public void solicitar(SolicitacaoAdocaoDto dto){

        Pet pet= petRepository.getReferenceById(dto.idPet());
        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());

        /*
        for(ValidacoesSolicitarAdocao v :listDeValidardores){
            v.validar(dto);
        } OU    */

        listDeValidardores.forEach(v -> v.validar(dto));

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
