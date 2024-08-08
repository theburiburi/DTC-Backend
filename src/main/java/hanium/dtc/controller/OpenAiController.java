package hanium.dtc.controller;

import hanium.dtc.dto.openai.request.UserQuestionRequest;
import hanium.dtc.global.ResponseDto;
import hanium.dtc.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class OpenAiController {
    private final OpenAiService openAiService;

    @PostMapping("/openai")
    public ResponseDto<?> getResponseOfQuestion(@RequestBody UserQuestionRequest userQuestionRequest) {
        return ResponseDto.ok(openAiService.getResponseOfQuestion(userQuestionRequest.question()));
    }
}
