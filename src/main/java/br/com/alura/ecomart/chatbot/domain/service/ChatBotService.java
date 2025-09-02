package br.com.alura.ecomart.chatbot.domain.service;

import br.com.alura.ecomart.chatbot.infra.openai.*;
import org.springframework.stereotype.Service;

@Service
public class ChatBotService {

    private LmStudioClient client;
    private OpenAIClient openAIClient;
    private OpenAIService openAIService;

    public ChatBotService(LmStudioClient client, OpenAIClient openAIClient, OpenAIService openAIService) {
        this.client = client;
        this.openAIClient = openAIClient;
        this.openAIService = openAIService;
    }

    public String responderPergunta(String pergunta) {
        String promptSistema = "Você é um chatbot de atendimento ao cliente de um e-commerce e deve responder apenas perguntas relacionadas ao e-commerce";
        var dados = new DadosRequisicaoChatCompletion(promptSistema, pergunta);
        return openAIService.enviarRequisicaoChatCompletion(dados);
    }
}
