package hanium.dtc.openai.controller;

import hanium.dtc.openai.dto.request.UserQuestionRequest;
import hanium.dtc.global.ResponseDto;
import hanium.dtc.openai.service.OpenAiService;
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
    public ResponseDto<?> test(@RequestBody UserQuestionRequest userQuestionRequest) {
        return ResponseDto.ok(openAiService.getListOfTravel(userQuestionRequest.question()));
    }
}
