package com.flytrap.rssreader.api.shared_member.infrastructure.implementation;

import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;
import com.flytrap.rssreader.api.shared_member.infrastructure.output.SharedMemberOutput;
import com.flytrap.rssreader.api.shared_member.infrastructure.repository.SharedMemberDslRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SharedMemberQuery {

    private final SharedMemberDslRepository sharedMemberDslRepository;

    public List<SharedMember> readAllByFolder(FolderId folderId) {
        return sharedMemberDslRepository.findAllByFolder(folderId.value())
            .stream()
            .map(SharedMemberOutput::toDomain)
            .toList();
    }

}
