package br.com.alura.ecomart.chatbot.domain.service;

import br.com.alura.ecomart.chatbot.infra.openai.DadosRequisicaoChatCompletion;
import br.com.alura.ecomart.chatbot.infra.openai.OpenAIClient;
import org.springframework.stereotype.Service;

@Service
public class ChatBotService {

    private OpenAIClient client;

    public ChatBotService(OpenAIClient client) {
        this.client = client;
    }

    public String responderPergunta(String pergunta) {
        String promptSistema = "Você é um chatbot de atendimento ao cliente de um e-commerce e deve responder apenas perguntas relacionadas ao e-commerce";
        var dados = new DadosRequisicaoChatCompletion(promptSistema, pergunta);
        return client.enviarRequisicaoChatCompletion(dados);
    }
}
