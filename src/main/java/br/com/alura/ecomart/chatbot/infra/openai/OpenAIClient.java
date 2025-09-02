package br.com.alura.ecomart.chatbot.infra.openai;

import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.messages.Message;
import com.theokanning.openai.messages.MessageRequest;
import com.theokanning.openai.runs.RunCreateRequest;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.threads.ThreadRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

@Component
public class OpenAIClient {

    private final OpenAiService service;

    private final String assistentId;

    private String threadId;

    public OpenAIClient(@Value("${app.openai.api.key}") String apiKey,
            @Value("${app.openai.assistant.id}") String assistentId) {
        this.service = new OpenAiService(apiKey, Duration.ofSeconds(60));
        this.assistentId = assistentId;
    }

    public String enviarRequisicaoChatCompletion(DadosRequisicaoChatCompletion dados) {
        var messageRequest = MessageRequest.builder()
                .role(ChatMessageRole.USER.value())
                .content(dados.promptUsuario())
                .build();

        if (this.threadId == null) {
            var threadRequest = ThreadRequest.builder()
                    .messages(Arrays.asList(messageRequest))
                    .build();
            var thread = service.createThread(threadRequest);
            this.threadId = thread.getId();
        } else {
            service.createMessage(this.threadId, messageRequest);
        }

        var runRequest = RunCreateRequest
                .builder()
                .assistantId(assistentId)
                .build();
        var run = service.createRun(threadId, runRequest);

        try {
            while (!run.getStatus().equalsIgnoreCase("completed")) {
                TimeUnit.SECONDS.sleep(10);
                run = service.retrieveRun(threadId, run.getId());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        var mensagens = service.listMessages(threadId);
        var respostaAssistente = mensagens.getData().stream()
                .sorted(Comparator.comparingInt(Message::getCreatedAt).reversed())
                .findFirst().get().getContent().get(0).getText().getValue();

        return respostaAssistente;
    }
}
