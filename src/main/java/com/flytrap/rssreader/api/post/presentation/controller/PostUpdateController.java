package com.flytrap.rssreader.api.post.presentation.controller;

import com.flytrap.rssreader.api.post.business.service.PostOpenService;
import com.flytrap.rssreader.api.post.presentation.controller.swagger.PostUpdateControllerApi;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.api.reaction.presentation.dto.ReactionRequest;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountSession;
import com.flytrap.rssreader.global.presentation.resolver.Login;
import com.flytrap.rssreader.api.reaction.business.service.ReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostUpdateController implements PostUpdateControllerApi {

    private final ReactionService reactionService;
    private final PostOpenService postOpenService;

    /**
     * 리액션 반응은 공유된 폴더의 POST만 가능합니다. 공유 된 폴더인지 체크 한 후 POST와 MEMBER사이에 리액션이 일어납니다.
     *
     * @param postId
     * @param member
     * @return
     */
    @PostMapping("/{postId}/reactions")
    public ApplicationResponse<Long> addReaction(
            @PathVariable Long postId,
            @Valid @RequestBody ReactionRequest request,
            @Login AccountSession member) {

        //TODO: 1.공유된 폴더인가?, 유효한 폴더 인가?
        // 2.공유폴더에 구독된 POST를 알아야한다
        Long reaction = reactionService.addReaction(postId, member.id(), request.emoji());

        return new ApplicationResponse<>(reaction);
    }

    @DeleteMapping("/{postId}/reactions")
    public ApplicationResponse<Void> deleteReaction(
            @PathVariable Long postId,
            @Login AccountSession member) {

        //TODO: 1.공유된 폴더인가?, 유효한 폴더 인가?
        // 2.공유폴더에 구독된 POST를 알아야한다
        reactionService.deleteReaction(postId, member.id());

        return new ApplicationResponse<>(null);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{postId}/read")
    public ApplicationResponse<Void> deleteRead(
            @PathVariable Long postId,
            @Login AccountSession member) {

        postOpenService.deleteRead(member.id(), postId);

        return new ApplicationResponse<>(null);
    }
}
