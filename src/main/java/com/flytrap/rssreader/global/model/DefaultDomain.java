package com.flytrap.rssreader.global.model;

public interface DefaultDomain<T> {

    DomainId<T> getId();

    default String getDomainCode() {
        return this.getClass().getAnnotation(Domain.class).name();
    }

    default String getDomainCodeWithId() {
        return String.format("%s_%s", this.getDomainCode(), this.getId().value().toString());
    }

}
