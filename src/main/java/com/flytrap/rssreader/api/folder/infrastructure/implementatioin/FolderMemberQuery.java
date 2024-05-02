package com.flytrap.rssreader.api.folder.infrastructure.implementatioin;

import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.domain.SharedMember;
import com.flytrap.rssreader.api.folder.infrastructure.output.FolderMemberOutput;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderMemberDslRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FolderMemberQuery {

    private final FolderMemberDslRepository folderMemberDslRepository;

    public List<SharedMember> readAllByFolder(FolderId folderId) {
        return folderMemberDslRepository.findAllByFolder(folderId.value())
            .stream()
            .map(FolderMemberOutput::toDomain)
            .toList();
    }

}
