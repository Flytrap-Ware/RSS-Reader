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
    private static final String hashAlgorithm = "SHA-1";

    public static PostId generateString(Instant pubDate, String guid)
        throws NoSuchAlgorithmException
    {
        ZonedDateTime zonedDateTime = pubDate.atZone(ZoneOffset.UTC);

        MessageDigest messageDigest = MessageDigest.getInstance(hashAlgorithm);
        byte[] digest = messageDigest.digest(guid.getBytes(StandardCharsets.UTF_8));
        String hashValue = DatatypeConverter.printHexBinary(digest).toLowerCase();

        return new PostId(zonedDateTime.format(formatter) + "-" + hashValue);
    }

}
