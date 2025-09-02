package br.com.alura.ecomart.chatbot.infra.openai;

import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseOutputText;
import org.springframework.stereotype.Component;

@Component
public class OpenAIService {

    private com.openai.client.OpenAIClient client;


    public OpenAIService(com.openai.client.OpenAIClient client) {
        this.client = client;
    }

    public String enviarRequisicaoChatCompletion(DadosRequisicaoChatCompletion dados) {

        ResponseCreateParams params = ResponseCreateParams.builder()
                .input(dados.promptUsuario())
                .model(ChatModel.GPT_4_1)
                .build();
        Response response = client.responses().create(params);
        return response.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(ResponseOutputText::text)
                .findFirst()
                .orElse("");
    }
}
