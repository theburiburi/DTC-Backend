package hanium.dtc.community.controller;

import hanium.dtc.community.dto.Response.PostResponse;
import hanium.dtc.community.service.CommunityService;
import hanium.dtc.global.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping("/free")
    public ResponseDto<List<PostResponse>> getFreeBoardPosts() {
        return ResponseDto.ok(communityService.getFreeBoardPosts());
    }
    @GetMapping("/travel")
    public ResponseDto<List<PostResponse>> getTravelBoardPosts() {
        return ResponseDto.ok(communityService.getTravelBoardPosts());
    }

    @GetMapping("/hot")
    public ResponseDto<List<PostResponse>> getHotBoardPosts(@RequestParam int likeThreshold) {
        return ResponseDto.ok(communityService.getHotBoardPosts(likeThreshold));
    }



}
