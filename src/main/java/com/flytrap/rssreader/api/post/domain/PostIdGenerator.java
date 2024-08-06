package com.flytrap.rssreader.api.post.domain;

import jakarta.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class PostIdGenerator {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final String HASH_ALGORITHM = "SHA-1";
    private static final MessageDigest MESSAGE_DIGEST;

    static {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static PostId generate(Instant pubDate, String guid) {
        ZonedDateTime zonedDateTime = pubDate.atZone(ZoneOffset.UTC);

        byte[] digest = MESSAGE_DIGEST.digest(guid.getBytes(StandardCharsets.UTF_8));
        String hashValue = DatatypeConverter.printHexBinary(digest).toLowerCase();

        return new PostId(zonedDateTime.format(formatter) + "-" + hashValue);
    }

}
