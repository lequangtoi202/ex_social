package com.lqt.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLConverter {
    public static String convertToHTML(String text) {
        Document document = Jsoup.parseBodyFragment(text);

        return document.body().html();
    }
}
