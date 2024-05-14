package com.flytrap.rssreader.global.model;

public interface DefaultDomain {

    public abstract DomainId getId();

    public default String getDomainCode() {
        return this.getClass().getAnnotation(Domain.class).name();
    }

    public default String getDomainCodeWithId() {
        return String.format("%s_%d", this.getDomainCode(), this.getId().value());
    }

}
