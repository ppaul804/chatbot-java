package br.com.alura.ecomart.chatbot.domain.service;

import br.com.alura.ecomart.chatbot.infra.openai.DadosRequisicaoChatCompletion;
import br.com.alura.ecomart.chatbot.infra.openai.LmStudioClient;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import io.reactivex.Flowable;
import org.springframework.stereotype.Service;

@Service
public class ChatBotService {

    private LmStudioClient client;

    public ChatBotService(LmStudioClient client) {
        this.client = client;
    }

    public Flowable<ChatCompletionChunk> responderPergunta(String pergunta) {
        String promptSistema = "Você é um chatbot de atendimento ao cliente de um e-commerce e deve responder apenas perguntas relacionadas ao e-commerce";
        var dados = new DadosRequisicaoChatCompletion(promptSistema, pergunta);
        return client.enviarRequisicaoChatCompletion(dados);
    }
}
