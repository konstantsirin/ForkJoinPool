package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveAction;

public class SiteMapMapping extends RecursiveAction {
    private final SiteMap siteMap;
    private final static CopyOnWriteArraySet<String> linksPool = new CopyOnWriteArraySet<>();

    public SiteMapMapping(SiteMap siteMap) {
        this.siteMap = siteMap;
    }

    @Override
    protected void compute() {
        String currentUrl = siteMap.getUrl();
        linksPool.add(currentUrl);

        ConcurrentSkipListSet<String> links = HtmlParser.getLinks(currentUrl);
        if (!links.isEmpty()) {
            for (String link : links) {
                if (!linksPool.contains(link)) {
                    // System.out.println(link);
                    linksPool.add(link);
                    siteMap.addSiteMapSubLinks(new SiteMap(link));
                }
            }

            List<SiteMapMapping> taskList = new ArrayList<>();

            for (SiteMap child : siteMap.getSiteMapSubLinks()) {
                SiteMapMapping task = new SiteMapMapping(child);
                task.fork();
                taskList.add(task);
            }

            for (SiteMapMapping task : taskList) {
                task.join();
            }
        }
    }
}
