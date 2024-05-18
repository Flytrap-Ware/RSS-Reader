package com.flytrap.rssreader.api.admin.infrastructure.system;

import com.flytrap.rssreader.api.admin.infrastructure.implementation.AdminSystemCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCollectionEnableLoader implements CommandLineRunner {

    private boolean isEnable;

    private final AdminSystemCommand adminSystemCommand;

    @Override
    public void run(String... args) throws Exception {
        this.isEnable = adminSystemCommand.read()
            .isPostCollectionEnabled();
    }

    public boolean isEnabled() {
        return this.isEnable;
    }

    public boolean isDisabled() {
        return !this.isEnable;
    }

    public void toEnable() {
        this.isEnable = true;
    }

    public void toDisable() {
        this.isEnable = false;
    }
}
