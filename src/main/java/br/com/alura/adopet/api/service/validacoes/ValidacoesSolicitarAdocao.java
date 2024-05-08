package br.com.alura.adopet.api.service.validacoes;

import br.com.alura.adopet.api.dto.adocao.SolicitacaoAdocaoDto;

public interface ValidacoesSolicitarAdocao {

    public void validar(SolicitacaoAdocaoDto dto);
}
