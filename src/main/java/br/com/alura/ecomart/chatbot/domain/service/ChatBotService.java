package br.com.alura.ecomart.chatbot.domain.service;

import br.com.alura.ecomart.chatbot.infra.openai.DadosRequisicaoChatCompletion;
import br.com.alura.ecomart.chatbot.infra.openai.LmStudioClient;
import br.com.alura.ecomart.chatbot.infra.openai.OpenAIClient;
import org.springframework.stereotype.Service;

@Service
public class ChatBotService {

    private LmStudioClient client;
    private OpenAIClient openAIClient;

    public ChatBotService(LmStudioClient client, OpenAIClient openAIClient) {
        this.client = client;
        this.openAIClient = openAIClient;
    }

    public String responderPergunta(String pergunta) {
        String promptSistema = "Você é um chatbot de atendimento ao cliente de um e-commerce e deve responder apenas perguntas relacionadas ao e-commerce";
        var dados = new DadosRequisicaoChatCompletion(promptSistema, pergunta);
        return openAIClient.enviarRequisicaoChatCompletion(dados);
    }
}
