package com.flytrap.rssreader.api.parser;

import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;
import com.flytrap.rssreader.api.parser.dto.RssSubscribeData;
import com.flytrap.rssreader.api.subscribe.presentation.dto.SubscribeRequest.CreateRequest;
import java.io.IOException;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

@Slf4j
@Component
public class RssSubscribeParser implements RssDocumentParser {

    public Optional<RssSubscribeData> parseRssDocuments(CreateRequest request) {
        BlogPlatform blogPlatform = BlogPlatform.parseLink(request.blogUrl());

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(request.blogUrl());

            String rootTagName = document.getDocumentElement().getTagName();
            if (IS_RSS_ROOT_TAG.test(rootTagName)) {
                return Optional.of(createSubscribeDataFromRss(document, request.blogUrl(), blogPlatform));
            } else if (IS_ATOM_ROOT_TAG.test(rootTagName)) {
                return Optional.of(createSubscribeDataFromAtom(document, request.blogUrl(), blogPlatform));
            } else {
                throw new ParserConfigurationException();
            }

        } catch (SAXException | IOException | ParserConfigurationException e) {
            log.error(RSS_PARSING_ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    private RssSubscribeData createSubscribeDataFromRss(Document document, String url,
        BlogPlatform blogPlatform) {
        Element tag = (Element) document.getElementsByTagName(
                RssTag.RssSubscribeTag.CHANNEL.getTagName())
            .item(0);

        return new RssSubscribeData(
            getTagValue(tag, RssTag.RssSubscribeTag.TITLE.getTagName()),
            url,
            blogPlatform,
            getTagValue(tag, RssTag.RssSubscribeTag.DESCRIPTION.getTagName())
        );
    }

    private RssSubscribeData createSubscribeDataFromAtom(Document document, String url,
        BlogPlatform blogPlatform) {
        Element tag = (Element) document.getElementsByTagName(
                RssTag.AtomSubscribeTag.FEED.getTagName())
            .item(0);

        return new RssSubscribeData(
            getTagValue(tag, RssTag.AtomSubscribeTag.TITLE.getTagName()),
            url,
            blogPlatform,
            ""
        );
    }

}
