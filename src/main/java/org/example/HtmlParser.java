package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ConcurrentSkipListSet;

import java.lang.Thread;

public class HtmlParser {
    // private static ConcurrentSkipListSet<String> childUrls;

    public static ConcurrentSkipListSet<String> getLinks(String url) {
        ConcurrentSkipListSet<String> childUrls = new ConcurrentSkipListSet<>();
        try {
            int PAUSE_TIME = 150;

            Thread.sleep(PAUSE_TIME);

            Document doc = Jsoup.connect(url)
                    .ignoreHttpErrors(true)
                    .get();

            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String childUrl = link.attr("abs:href");
                if (isValidLink(childUrl) && isSameDomain(url, childUrl) /*&& isChildUrlThisDomain(url, childUrl)*/) {
                    childUrls.add(childUrl);
                }
            }
        } catch (IOException | InterruptedException e ) {
            e.getCause();
        }

        return childUrls;
    }

    private static boolean isValidLink(String link) {
        return !link.contains("#") && link.startsWith("http");
    }

    private static boolean isSameDomain(String parentUrl, String childUrl) {
        String domainName =  parentUrl.replaceAll("http(s)?://|www\\.|/.*", "");
        return childUrl.contains(domainName);
    }

    private boolean isChildUrlThisDomain(String parentUrl, String childUrl) {
        return childUrl.contains(parentUrl);
    }
}
