package br.com.alura.ecomart.chatbot.infra.openai;

import br.com.alura.ecomart.chatbot.domain.service.LmStudioService;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
public class LmStudioClient {
    private static final List<String> MODELOS = Arrays.asList(System.getenv("LM_MODEL_NAME").split(","));

    private final LmStudioService service;

    public LmStudioClient() {
        this.service = new LmStudioService(Duration.ofSeconds(60));
    }

    public String enviarRequisicaoChatCompletion(DadosRequisicaoChatCompletion dados) {
        var request = ChatCompletionRequest
                .builder()
                .model(MODELOS.get(0))
                .messages(Arrays.asList(
                        new ChatMessage(
                                ChatMessageRole.SYSTEM.value(),
                                dados.promptSistema()),
                        new ChatMessage(
                                ChatMessageRole.USER.value(),
                                dados.promptUsuario())))
                .build();

        var segundosParaProximaTentiva = 5;
        var tentativas = 0;
        while (tentativas++ != 5) {
            try {
                return service
                        .createChatCompletion(request)
                        .getChoices().get(0)
                        .getMessage().getContent();
            } catch (OpenAiHttpException ex) {
                var errorCode = ex.statusCode;
                switch (errorCode) {
                    case 401 -> throw new RuntimeException("Erro com a chave da API!", ex);
                    case 429, 500, 503 -> {
                        try {
                            Thread.sleep(1000 * segundosParaProximaTentiva);
                            segundosParaProximaTentiva *= 2;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        throw new RuntimeException("API Fora do ar! Tentativas finalizadas sem sucesso!");
    }

}
