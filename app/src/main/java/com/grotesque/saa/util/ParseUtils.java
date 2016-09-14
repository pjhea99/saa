package com.grotesque.saa.util;


import android.os.Parcelable;

import com.grotesque.saa.content.data.ContentItem;
import com.grotesque.saa.fixture.data.FixtureData;
import com.grotesque.saa.home.data.PointRankData;
import com.grotesque.saa.home.data.PointRankList;
import com.grotesque.saa.rank.data.LeagueTable;
import com.grotesque.saa.search.data.SearchItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;

/**
 * Created by 0614_000 on 2015-06-06.
 */
public class ParseUtils {
    private static final String TAG = makeLogTag("ParseUtils");
    private static final String BLANK = "<td height=\"150\" style=\"word-break:break-all; padding:10px;\"> \n" +
            "                                                                         <!-- 내용 출력 --> <span id=\"writeContents\">";
    public static ArrayList<FixtureData> parseFixtue(Document doc){
        ArrayList<FixtureData> fixtureDatas = new ArrayList<>();
        Elements elements = doc.select("section[class^=risultati] div.box-partita");
        for(Element e : elements){
            Elements team = e.select("div[class^=col-xs-6");
            fixtureDatas.add(new FixtureData(e.select("div[class^=datipartita] span").text().replaceAll("\\D", "").substring(0, 12), team.get(0).text().split(" "), team.get(1).text().split(" "), "", team.get(0).select("img[src]").attr("src"), team.get(1).select("img[src").attr("src")));

        }
        return  fixtureDatas;
    }
    public static ArrayList<LeagueTable> parseRankDaum(Document doc){
        ArrayList<LeagueTable> arrayList = new ArrayList<>();
        Elements rankdata = doc.select("tbody tr");
        for(Element e : rankdata){
            Elements teamdata = e.select("td");
            arrayList.add(new LeagueTable(teamdata.get(0).text(),
                    teamdata.get(1).text(),
                    teamdata.get(2).text(),
                    teamdata.get(3).text(),
                    teamdata.get(4).text(),
                    teamdata.get(5).text(),
                    teamdata.get(6).text(),
                    teamdata.get(7).text(),
                    teamdata.get(8).text(),
                    teamdata.get(9).text(),
                    teamdata.get(0).select("a href").attr("src")));
        }
        return arrayList;
    }
    public static String parseImgUrl(String str){
        Document doc = Jsoup.parse(str);
        String imgUrl = doc.select("img[src]").get(0).attr("src");
        if(imgUrl.startsWith("//")){
            LOGE(TAG, "parseImgUrl" + imgUrl);
            return imgUrl.replace("//","http://");
        }else if(imgUrl.startsWith("files/attach/images/")){
            return "http://www.serieamania.com/xe/" + imgUrl;
        }else
            return imgUrl;
    }

    public static ArrayList<ContentItem> parseContent(String str){
        //LOGE(TAG, "content : " +str);
        str = str.replaceAll("<iframe", "<embed").replaceAll("\\r\\n", "<br />").replaceAll("\\n","<br />");
        ArrayList<ContentItem> arrayList = new ArrayList<>();
        Document doc = Jsoup.parse(str);
        Elements elements_image = doc.select("img[src]");
        Elements elements_embed = doc.select("embed[src]");
        String temp = doc.toString();
        String split;
        String image ="";
        String embed ="";
        String attr;
        Element element;

        int size_image = elements_image.size();
        int size_embed = elements_embed.size();
        int i = 0;
        int j = 0;

        while(size_image > 0 || size_embed > 0){

            if(i < size_image)
                image = elements_image.get(i).toString();
            if(j < size_embed)
                embed = elements_embed.get(j).toString();

            if(j == size_embed){
                //LOGE(TAG, "j == size_embed");
                attr = elements_image.get(i).attr("src").replace("../data/file", "http://highbury.co.kr/data/file");
                split = temp.substring(0, temp.indexOf(image));
                if (!split.isEmpty() || !split.equals(BLANK)) {
                    arrayList.add(new ContentItem(split, null, null, "text", false));
                }


                arrayList.add(new ContentItem(null, attr, null, "image", false));


                temp = temp.substring(temp.indexOf(image) + image.length(), temp.length());
                i++;
            }
            if(j == size_embed && i == size_image)
                break;
            if(i == size_image){
                //LOGE(TAG, "i == size_image");
                element = elements_embed.get(j);
                split = temp.substring(0, temp.indexOf(embed));
                if(!split.isEmpty() || !split.equals(BLANK)){
                    arrayList.add(new ContentItem(split, null, null, "text", false));
                    arrayList.add(new ContentItem(null, null, StringUtils.getVideoItem(element), "video", false));
                }else{
                    arrayList.add(new ContentItem(null, null, StringUtils.getVideoItem(element), "video", false));
                }
                temp = temp.substring(temp.indexOf(embed) + embed.length(), temp.length());
                j++;
            }
            if(j == size_embed && i == size_image)
                break;

            if(i < size_image && j < size_embed) {
                //LOGE(TAG, "i < size_image && j < size_embed)");
                if (temp.indexOf(image) < temp.indexOf(embed)) {
                    attr = elements_image.get(i).attr("src");
                    split = temp.substring(0, temp.indexOf(image));
                    if (!split.isEmpty() || !split.equals(BLANK)) {
                        arrayList.add(new ContentItem(split, null, null, "text", false));
                    }

                    arrayList.add(new ContentItem(null, attr, null, "image", false));

                    temp = temp.substring(temp.indexOf(image) + image.length(), temp.length());
                    i++;
                } else {
                    element = elements_embed.get(j);
                    split = temp.substring(0, temp.indexOf(embed));
                    if (!split.isEmpty()) {
                        arrayList.add(new ContentItem(split, null, null, "text", false));
                    }
                    arrayList.add(new ContentItem(null, null, StringUtils.getVideoItem(element), "video", false));
                    temp = temp.substring(temp.indexOf(embed) + embed.length(), temp.length());
                    j++;
                }
            }
        }
        arrayList.add(new ContentItem(temp, null, null, "text", false));
        return arrayList;
    }

    public static ArrayList<SearchItem> parseSearch(String html){
        LOGE(TAG, String.valueOf(html));

        ArrayList<SearchItem> arrayList = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("tr[class^=bg");

        if(elements.size() == 0)
            return null;

        for(Element content : elements){
            String document_srl = content.select("td[class^=title").html();
            document_srl = document_srl.substring(document_srl.indexOf("document_srl="));
            document_srl = document_srl.substring(13, document_srl.indexOf("\""));
            arrayList.add(new SearchItem(content.select("td[class^=title").text(),
                    content.select("td[class^=author").text(),
                    content.select("td[class^=reading").text(),
                    content.select("span[class^=replyNum").text(),
                    content.select("td[class^=date").text(),
                    document_srl));
        }
        return arrayList;
    }
    public static ArrayList<PointRankList> parsePointRank(String html){
        ArrayList<PointRankList> arrayList = new ArrayList<>();

        for(Element e : Jsoup.parse(html).select("tbody tr")){
            arrayList.add(new PointRankList(e.select("img").attr("src"),
                    e.select("td.nick").text(),
                    e.select("td.point").text()));
        }
        return arrayList;
    }
}
