package org.example;
import java.util.concurrent.CopyOnWriteArraySet;

public class SiteMap implements Comparable<SiteMap>{
    private final String url;
    private final CopyOnWriteArraySet<SiteMap> siteMapSubLinks;

    public SiteMap(String url) {
        siteMapSubLinks = new CopyOnWriteArraySet<>();
        this.url = url;
    }

    public void addSiteMapSubLinks(SiteMap subLink) {
        siteMapSubLinks.add(subLink);
    }

    public CopyOnWriteArraySet<SiteMap> getSiteMapSubLinks() {
        return siteMapSubLinks;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int compareTo(SiteMap o) {
        return this.url.compareTo(o.getUrl());
    }
}
