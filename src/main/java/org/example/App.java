package org.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;


public class App
{
    public static void main(String[] args) {
        String url = "https://skillbox.ru/";
        String outputFileName = "sitemap.txt";
        long start = System.currentTimeMillis();

        SiteMap siteMap = new SiteMap(url);
        SiteMapMapping task = new SiteMapMapping(siteMap);

        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(task);

        try {
            FileOutputStream stream = new FileOutputStream(outputFileName);
            String siteMapFile = writeIndentedLink(siteMap, 0);
            stream.write(siteMapFile.getBytes());
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("FINISH");
            long finish = System.currentTimeMillis();
            long elapsed = finish - start;
            System.out.println("Program is completed: " + TimeUnit.MILLISECONDS.toMinutes(elapsed) + " minutes");
        }
    }

    public static String writeIndentedLink(SiteMap siteMap, int depth) {
        String link = siteMap.getUrl();
        String indent = generateIndent(depth);
        StringBuilder result = new StringBuilder(indent + link + "\n");
        siteMap.getSiteMapSubLinks().forEach(childLink -> result.append(writeIndentedLink(childLink, depth + 1)));
        return result.toString();
    }

    private static String generateIndent(int depth) {
        StringBuilder temp = new StringBuilder();
        if (depth != 0) {
            for (int i = 0; i < depth; i++) {
                temp.append("\t");
            }
        }
        return temp.toString();
    }
}
