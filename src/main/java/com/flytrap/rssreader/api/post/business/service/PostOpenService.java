package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.post.business.event.postOpen.PostOpenEventParam;
import com.flytrap.rssreader.api.post.infrastructure.output.OpenPostCountOutput;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostOpenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostOpenService {

    private final PostOpenRepository postOpenRepository;

    @Transactional
    public void open(PostOpenEventParam value) {

        if (postOpenRepository.existsByMemberIdAndPostId(value.memberId(), value.postId())) {
            return;
        }

        postOpenRepository.save(value.toEntity());
    }

    @Transactional(readOnly = true)
    public Map<Long, OpenPostCountOutput> countOpens(long id, List<Long> subscribes) {
        return postOpenRepository.countOpens(id, subscribes).stream()
                .collect(Collectors.toMap(OpenPostCountOutput::getSubscribeId, it -> it));
    }

    @Transactional
    public void deleteRead(long memberId, Long postId) {
        postOpenRepository.deleteByMemberIdAndPostId(memberId, postId);
    }

}
