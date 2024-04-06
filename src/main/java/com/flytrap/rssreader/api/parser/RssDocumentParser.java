package com.flytrap.rssreader.api.parser;

import java.util.function.Predicate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface RssDocumentParser {

    Predicate<String> IS_RSS_ROOT_TAG = rootTagName
        -> RssTag.RSS.getRootTagName().equals(rootTagName);
    Predicate<String> IS_ATOM_ROOT_TAG = rootTagName
        -> RssTag.ATOM.getRootTagName().equals(rootTagName);

    String RSS_PARSING_ERROR_MESSAGE = "RSS문서를 파싱할 수 없습니다.";

    default String getTagValue(Node parentNode, String tagName) {
        Element element = (Element) parentNode;
        Node item = element.getElementsByTagName(tagName).item(0);

        return (item != null) ? item.getTextContent() : "";
    }
}
