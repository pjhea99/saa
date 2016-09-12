package com.grotesque.saa.util;


import com.grotesque.saa.R;
import com.grotesque.saa.content.data.VideoItem;

import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;

/**
 * Created by 0614_000 on 2015-06-06.
 */
public class StringUtils {
    private static final String TAG = makeLogTag(StringUtils.class);
    public static final int NAVER_VOD = 0;
    public static final int DAUM_VOD = 1;
    public static final int YOUTUBE_VOD = 2;
    public static final int IMGUR_VOD = 3;
    public static final int GOOGLE_VOD = 4;
    public static final int MGOON_VOD = 5;
    public static final int DAILY_VOD = 6;
    public static final int ETC_VOD = 7;

    public static String getComment(String comment){
        if (comment.length() > 2) {
            comment = comment.replace("(", "").replace(")", "");
            return comment.equals("0") ? "" : comment;
        }else
            return "";
    }

    public static String getTime(String writeTime){
        String inTime   = new SimpleDateFormat("HHmm").format(new Date());

        if(writeTime.contains(":")) {
            int currentTimeInt = Integer.parseInt(inTime.substring(0, 2)) * 60 + Integer.parseInt(inTime.substring(2, 4));
            int writeTimeInt = Integer.parseInt(writeTime.substring(0, 2)) * 60 + Integer.parseInt(writeTime.substring(3, 5));
            int t = currentTimeInt - writeTimeInt;
            int h = (currentTimeInt - writeTimeInt) / 60;
            int m = (currentTimeInt - writeTimeInt) % 60;
            if (h != 0) {
                return h + "시간 전";
            }else
                return m == 0 ? "방금 전" : m + "분 전";
        }else{
            if(Integer.parseInt(writeTime.substring(0,2))>9){
                return writeTime.substring(0,2)+"월 "+writeTime.substring(3,5)+"일";
            }else
                return writeTime.substring(1,2)+"월 "+writeTime.substring(3,5)+"일";
        }
    }
    public static String getCommentTime(String commentTime){
        String inTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date());

        if(!commentTime.substring(0, 8).equals(inTime.substring(0, 8))){
            if(!commentTime.substring(0, 2).equals(inTime.substring(0, 2))){
                return Integer.parseInt(inTime.substring(0, 4)) - Integer.parseInt(commentTime.substring(0, 4))+ "년 전";
            }else if(!commentTime.substring(4, 6).equals(inTime.substring(4, 6))){
                return Integer.parseInt(inTime.substring(4, 6)) - Integer.parseInt(commentTime.substring(4, 6))+ "개월 전";
            }else{
                return Integer.parseInt(inTime.substring(6, 8)) - Integer.parseInt(commentTime.substring(6, 8))+ "일 전";
            }


        }else{
            int currentTimeInt = Integer.parseInt(inTime.substring(8, 10)) * 60 + Integer.parseInt(inTime.substring(10, 12));
            int writeTimeInt = Integer.parseInt(commentTime.substring(8, 10)) * 60 + Integer.parseInt(commentTime.substring(10, 12));
            int t = currentTimeInt - writeTimeInt;
            int h = (currentTimeInt - writeTimeInt) / 60;
            int m = (currentTimeInt - writeTimeInt) % 60;
            if (h != 0) {
                return h + "시간 전";
            }else
                return m == 0 ? "방금 전" : m + "분 전";
        }

    }
    public static String convertImgUrl(String imgUrl){
        if(imgUrl.startsWith("//")) {
            LOGE(TAG, imgUrl);
            return imgUrl.replace("//", "http://");
        }else if(imgUrl.startsWith("files/attach/images/")){
            return "http://www.serieamania.com/xe/" + imgUrl;
        }else
            return imgUrl;
    }
    public static String convertFixtureDate(String str){
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmm", Locale.ITALY);
        TimeZone tz = TimeZone.getTimeZone("Europe/Rome");
        format.setTimeZone(tz);
        try {
            Date date = format.parse(str);
            SimpleDateFormat format1 = new SimpleDateFormat("M월 d일\nE요일 HH:mm", Locale.KOREA);
            tz = TimeZone.getTimeZone("Asia/Seoul");
            format1.setTimeZone(tz);
            return format1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertDate(String str){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        SimpleDateFormat convertFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA);
        try {
            return convertFormat.format(format.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static int getVideoType(Element element){
        String src = element.attr("src");
        if(src.contains("naver.com"))
            return NAVER_VOD;
        else if(src.contains("daum.net"))
            return DAUM_VOD;
        else if(src.contains("youtube"))
            return YOUTUBE_VOD;
        else if(src.contains("imgur.com"))
            return IMGUR_VOD;
        else if(src.contains("dailymotion"))
            return DAILY_VOD;
        else if(src.contains("play.mgoon"))
            return MGOON_VOD;
        else if(src.contains("googledrive.com"))
            return GOOGLE_VOD;
        else
            return ETC_VOD;
    }

    public static String convertVideo(Element element, int type){
        String video = element.attr("src");
        switch(type){
            case NAVER_VOD:
                return video.replace("http://serviceapi.nmv.naver.com/flash/NFPlayer.swf","http://serviceapi.nmv.naver.com/flash/convertIframeTag.nhn");
            case DAUM_VOD:
                String daum_video = "";
                if(element.attr("flashvars").isEmpty()){
                    daum_video = video.replace("player/VodPlayer.swf", "video/viewer/Video.html");
                    daum_video = daum_video.replace("playLoc=undefined", "play_loc=undefined");
                }else{
                    daum_video = element.attr("flashvars");
                    daum_video = daum_video.replace("playLoc=undefined", "play_loc=undefined");
                    daum_video = "http://videofarm.daum.net/controller/video/viewer/Video.html?" + daum_video + " width='640px' height='360px' ";

                }
                return daum_video;
            case YOUTUBE_VOD:
                video = video.replace("youtube.com/v", "youtube.com/embed");
                video = video.replace("youtube-nocookie.com/v", "youtube.com/embed");
                break;
            case IMGUR_VOD:
                break;
            case GOOGLE_VOD:
                break;
            case MGOON_VOD:
                video = video.replace("http://play.mgoon.com/Video/V", "http://www.mgoon.com/play/view/");
                break;
            case DAILY_VOD:
                break;
            case ETC_VOD:
                break;
        }
        return video;
    }
    public static String convertCategoryName(String category){
        switch (category){
            case "10282945":
                return "국대";
            case "112817":
                return "k리그";
            case "112800":
                return "ita";
            case "112806":
                return "기타";
            case "34559678":
                return "Heroes Of The Storm";
            case "7904739":
                return "League Of Legends";
            case "1652283":
                return "프리스타일풋볼";
            case "1652270":
                return "프로야구매니저";
            case "1652293":
                return "스타크래프트";
            case "1680546":
                return "기타";
            case "29559230":
                return "풋볼매니저";
            case "22041632":
                return "하스스톤";
            case "22999512":
                return "풋볼데이";
            case "15939357":
                return "피파온라이3";
            case "11719256":
                return "디아블로3";
            case "37532467":
                return "세븐나이츠";
            case "46192952":
                return "오버워치";
            default:
                return "";
        }
    }
    public static String convertTeamName(String team) {
        switch (team) {
            case "Roma":
                return "AS로마";
            case "Udinese":
                return "우디네세";
            case "Juventus":
                return "유벤투스";
            case "Fiorentina":
                return "피오렌티나";
            case "Milan":
                return "AC밀란";
            case "Torino":
                return "토리노";
            case "Atalanta":
                return "아탈란타";
            case "Lazio":
                return "라치오";
            case "Bologna":
                return "볼로냐";
            case "Crotone":
                return "크로토네";
            case "Chievoverona":
                return "키에보 베로나";
            case "Inter":
                return "인터밀란";
            case "Empoli":
                return "엠폴리";
            case "Sampdoria":
                return "삼프도리아";
            case "Genoa":
                return "제노아";
            case "Cagliari":
                return "칼리아리";
            case "Pescara":
                return "페스카라";
            case "Napoli":
                return "나폴리";
            case "Palermo":
                return "팔레르모";
            case "Sassuolo":
                return "사수올로";
        }
        return "";
    }
    public static int getColorId(String category){
        switch (category){
            case "ita":
                return R.color.azure_blue;
            case "k리그":
                return R.color.colorAccent;
            default:
                return R.color.brunch_mint;
        }
    }

    public static String getYoutubeId(String videoId){
        videoId = videoId.substring(!videoId.contains("/embed/") ? (videoId.contains(".be/") ? videoId.indexOf(".be/")+4 : videoId.indexOf("/v/")+3) : videoId.indexOf("embed/")+6, videoId.length());
        videoId = videoId.contains("?") ? videoId.substring(0, videoId.indexOf("?")) : videoId;
        if(videoId.contains("\"")){
            videoId = videoId.substring(0, videoId.indexOf("\""));
        }
        LOGE(TAG, "videoid : " + videoId);
        return videoId;
    }
    public static String getYoutubeIframe(String videoId){
        return "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/" + videoId + " frameborder=\"0\" allowfullscreen></iframe>";
    }
    public static CharSequence trimTrailingWhitespace(CharSequence source) {

        if(source == null)
            return "";

        int i = source.length();

        // loop back to the first non-whitespace character
        while(--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }

        return source.subSequence(0, i + 1);
    }
    public static String removeCharExceptNumber(String str) {
        return str.replaceAll("[^0-9]", "");
    }

    public static String toHexString(byte[] paramArrayOfByte)
    {
        if (paramArrayOfByte == null) {
            return null;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        int j = paramArrayOfByte.length;
        int i = 0;
        while (i < j)
        {
            int k = paramArrayOfByte[i];
            localStringBuilder.append(a[((k & 0xF0) >> 4)]);
            localStringBuilder.append(a[(k & 0xF)]);
            i += 1;
        }
        return localStringBuilder.toString();
    }
    public static String getMid(String mid){
        switch (mid){
            case "Calcio":
                return "calcioboard";
            case "자유":
                return "freeboard3";
            case "미디어":
                return "multimedia1";
            case "스포츠":
                return "rest";
            case "게임":
                return "game";
            case "질문":
                return "qna1";
            case "칼치오토리":
                return "players1";
            case "스페셜 리포트":
                return "specialreport";
            case "중계":
                return "broadcast1";
            default:
                break;
        }
        return "freeboard";
    }
    public static String getBoardName(String mid){
        switch (mid){
            case "calcioboard":
                return "CALCIO 게시판";
            case "multimedia1":
                return "멀티 미디어";
            case "rest":
                return "기타 스포츠";
            case "game":
                return "게임 게시판";
            case "qna1":
                return "질문 게시판";
            case "gag":
                return "후로 게시판";
            case "players1":
                return "칼치오토리";
            case "specialreport":
                return "스페셜 리포트";
            case "broadcast1":
                return "중계 게시판";
            default:
                break;
        }
        return "자유 게시판";
    }
    public static HashMap<String, String> setDaumQuery(String vid){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("vid", vid);
        hashMap.put("profile", "HIGH");
        hashMap.put("play_lock", "tvpot");
        hashMap.put("contents_type", "MP4");
        hashMap.put("dte_type","");
        return hashMap;
    }
    public static HashMap<String, String> setCastQuery(String outKey){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ver", "1.5");
        hashMap.put("key", outKey);
        hashMap.put("ptc", "http");
        hashMap.put("doct", "json");
        hashMap.put("devt", "android");
        return hashMap;
    }
    public static HashMap<String, String> setBlogQuery(String outKey){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ver", "1.5");
        hashMap.put("key", outKey);
        hashMap.put("ptc", "http");
        hashMap.put("doct", "json");
        hashMap.put("devt", "android");
        return hashMap;
    }
    public static String getNaverOutKey(String url){
        url = url.substring(url.indexOf("outKey=")+7, url.length());
        return url.contains("&") ? url.substring(0, url.indexOf("&")) : url;
    }
    public static String getVideoId(Element element, int videoType){

        String url = element.toString();
        LOGE(TAG, "getVideoId " + url);
        switch (videoType){
            case NAVER_VOD:
                return url.substring(url.indexOf("vid=")+4, url.indexOf("&"));
            case DAUM_VOD:
                return url.substring(url.indexOf("vid=")+4, url.indexOf("&"));
            case YOUTUBE_VOD:
                return getYoutubeId(element.attr("src"));
            default:
                return "";
        }
    }
    public static VideoItem getVideoItem(Element element){
        int videoType = getVideoType(element);
        switch (videoType){
            case NAVER_VOD:
                return new VideoItem(getVideoId(element, videoType), getNaverOutKey(element.attr("src")), element, videoType);
            default:
                return new VideoItem(getVideoId(element, videoType), "", element, videoType);
        }
    }
    public static String getDepthColor(int depth){
        switch (depth){
            case 1:
                return "#1abc9c";
            case 2:
                return "#16a085";
            case 3:
                return "#2ecc71";
            case 4:
                return "#27ae60";
            case 5:
                return "#3498db";
            case 6:
                return "#2980b9";
            case 7:
                return "#9b59b6";
            case 8:
                return "#8e44ad";
            case 9:
                return "#34495e";
            default:
                return "#2c3e50";
        }
    }
    static final char[] a = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
}
