package br.com.alura.adopet.api.exception;

public class ValidacaoExcepition extends RuntimeException {
    public ValidacaoExcepition(String mensagem) {
        super(mensagem);
    }
}
