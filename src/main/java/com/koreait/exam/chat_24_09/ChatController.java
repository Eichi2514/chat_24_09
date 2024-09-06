package com.koreait.exam.chat_24_09;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private List<ChatMessage> chatMessages = new ArrayList<>();

    /*
    @AllArgsConstructor
    @Getter
    public static class writeMessageRequest{
        private final String authorName;
        private final String content;
    }
    */

    public record writeMessageRequest(String authorName, String content) {
    }

    public record writeMessageResponse(long id) {
    }

    @PostMapping("/writeMessage")
    @ResponseBody
    public RsData<writeMessageResponse> writeMessage(@RequestBody writeMessageRequest req) {
        ChatMessage message = new ChatMessage(req.authorName, req.content);

        chatMessages.add(message);

        return new RsData<>("S-1",
                "메세지가 작성됨",
                new writeMessageResponse(message.getId()));
    }


    public record messagesRequest(Long fromId) {
    }

    public record messagesResponse(List<ChatMessage> chatMessages, long count) {
    }

    @GetMapping("/messages")
    @ResponseBody
    public RsData<messagesResponse> message(messagesRequest req) {

        List<ChatMessage> messages = chatMessages;

        log.debug("req : {}", req);

        if(req.fromId != null){
            int index = IntStream.range(0, messages.size())
                    .filter(i -> chatMessages.get(i).getId() == req.fromId)
                    .findFirst()
                    .orElse(-1);

            if(index != -1){
                messages = messages.subList(index+1, messages.size());
            }
        }



        return new RsData<>("S-1", "성공", new messagesResponse(messages, chatMessages.size()));
    }

}
