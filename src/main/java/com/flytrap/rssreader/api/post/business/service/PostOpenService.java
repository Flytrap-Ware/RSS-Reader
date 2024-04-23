package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.post.business.event.postOpen.PostOpenEventParam;
import com.flytrap.rssreader.api.post.infrastructure.output.OpenPostCountOutput;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostOpenEntityRepository;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostOpenService {

    private final PostOpenEntityRepository postOpenEntityRepository;

    @Transactional
    public void open(PostOpenEventParam value) {

        if (postOpenEntityRepository.existsByMemberIdAndPostId(value.memberId(), value.postId())) {
            return;
        }

        postOpenEntityRepository.save(value.toEntity());
    }


    // TODO: Folder쪽으로 옮기기
    @Transactional(readOnly = true)
    public Map<SubscriptionId, OpenPostCountOutput> countReadInSubscription(long id, SubscriptionId subscriptionId) {
        return postOpenEntityRepository.countOpens(id, List.of(subscriptionId.value())).stream()
                .collect(Collectors.toMap(
                    output -> new SubscriptionId(output.getSubscribeId()),
                    output -> output
                ));
    }

    // TODO: Folder쪽으로 옮기기
    @Transactional(readOnly = true)
    public Map<SubscriptionId, OpenPostCountOutput> countReadInSubscriptions(long id, List<SubscriptionId> subscriptionIds) {
        List<Long> ids = subscriptionIds.stream().map(SubscriptionId::value).toList();

        return postOpenEntityRepository.countOpens(id, ids).stream()
                .collect(Collectors.toMap(
                    output -> new SubscriptionId(output.getSubscribeId()),
                    output -> output
                ));
    }

    @Transactional
    public void deleteRead(long memberId, Long postId) {
        postOpenEntityRepository.deleteByMemberIdAndPostId(memberId, postId);
    }

}
