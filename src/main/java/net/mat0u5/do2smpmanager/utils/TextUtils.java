package net.mat0u5.do2smpmanager.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
    private static HashMap<List<String>, List<String>> emotes = new HashMap<List<String>, List<String>>();
    private static final List<String> maxOne = List.of("warden", "warden_pointing", "warden_pointing_right"
            , "warden_pointing_left", "warden_scream", "ravager");
    public static void setEmotes() {
        emotes.put(List.of("skull"),List.of("☠"));
        emotes.put(List.of("smile"),List.of("☺"));
        emotes.put(List.of("frown"),List.of("☹"));
        emotes.put(List.of("heart"),List.of("❤"));
        emotes.put(List.of("copyright"),List.of("©"));
        emotes.put(List.of("trademark","tm"),List.of("™"));

        emotes.put(List.of("mat","Mat0u5"), List.of("\uE0A1", "1293651244296044595"));
        emotes.put(List.of("gari","Garibaldi","Garibaldi_"), List.of("\uE0A2", "1293651157348257863"));
        emotes.put(List.of("onti","OntiMoose"), List.of("\uE0A3", "1293651158723858564"));
        emotes.put(List.of("simple","ItsSimpleAsThat"), List.of("\uE0A4", "1293651160376414280"));
        emotes.put(List.of("orb","ball","ontiball","onti_ball","oversized_onti_ball"), List.of("\uE0A5", "1293649858233634948"));

        emotes.put(List.of("bates","batesminecraft"), List.of("\uE0B1", "1294225705152020540"));
        emotes.put(List.of("eric","ericeldrick"), List.of("\uE0B2", "1294225672650231819"));
        emotes.put(List.of("fui","fuinobi"), List.of("\uE0B3", "1294225654203809805"));
        emotes.put(List.of("hazel","uwu","uwu_hazel"), List.of("\uE0B4", "1308181026547437578"));
        emotes.put(List.of("kyuu","shiny_kyuu"), List.of("\uE0B5", "1308181027872575548"));
        emotes.put(List.of("mopag","mopag_24"), List.of("\uE0B6", "1294225658909691915"));
        emotes.put(List.of("nicky","nicky_001"), List.of("\uE0B7", "1294225660960702545"));
        emotes.put(List.of("nifty","thedogcape","niftyname641"), List.of("\uE0B8", "1308181024101896395"));
        emotes.put(List.of("ralmen","ralmenkuro"), List.of("\uE0B9", "1294225664068816906"));
        emotes.put(List.of("smol","smol_person"), List.of("\uE0BA", "1294225666631536704"));
        emotes.put(List.of("stuff","stuffie","mrstuff456"), List.of("\uE0BB", "1294225667910664192"));
        emotes.put(List.of("ziz","zizi","zizola"), List.of("\uE0BC", "1294225669185867910"));

        emotes.put(List.of("warden"), List.of("\uE0BD","1294941254852804648"));
        emotes.put(List.of("warden_pointing","warden_pointing_right"), List.of("\uE0BE","1294941553453699137"));
        emotes.put(List.of("warden_pointing_left"), List.of("\uE0BF","1294941551725772871"));
        emotes.put(List.of("warden_scream"), List.of("\uE0C0","1294941256832651329"));
        emotes.put(List.of("ravager"), List.of("\uE0C1","1294941258313240586"));
    }
    public static String replaceEmotes(String input) {
        for (String maxOneEmote : maxOne) {
            if (!input.contains(maxOneEmote)) continue;
            input = input.replaceFirst(":"+maxOneEmote+":", "_"+maxOneEmote+"_");
            input = input.replaceAll(":"+maxOneEmote+":", "");
            input = input.replaceFirst("_"+maxOneEmote+"_", ":"+maxOneEmote+":");
        }
        for (Map.Entry<List<String>, List<String>> entry : emotes.entrySet()) {
            if (entry.getValue().size()==0) continue;
            String emoteValue = entry.getValue().get(0);
            for (String emote : entry.getKey()) {
                String emoteCode = ":" + emote + ":";
                input = replaceCaseInsensitive(input, emoteCode, emoteValue);
            }
            if (!input.contains(":")) return input;
        }
        return input;
    }
    public static String replaceEmotesDiscord(String input) {
        for (Map.Entry<List<String>, List<String>> entry : emotes.entrySet()) {
            if (entry.getValue().isEmpty()) continue;
            String emoteValue = entry.getValue().get(0);

            if (entry.getValue().size() > 1) {
                String emoteID = entry.getValue().get(1);
                for (String emote : entry.getKey()) {
                    String emoteCode = "<:" + emote + ":"+emoteID+">";
                    input = replaceCaseInsensitive(input, emoteCode, emoteValue);
                }
            }
            for (String emote : entry.getKey()) {
                String emoteCode = ":" + emote + ":";
                input = replaceCaseInsensitive(input, emoteCode, emoteValue);
            }
            if (!input.contains(":")) return input;
        }
        return input;
    }
    public static String formatEmotesForDiscord(String input) {
        for (Map.Entry<List<String>, List<String>> entry : emotes.entrySet()) {
            if (entry.getValue().size() <=1) continue;
            String emoteValue = entry.getValue().get(1);
            for (String emote : entry.getKey()) {
                String emoteCode = ":" + emote + ":";
                input = replaceCaseInsensitive(input, emoteCode, "<"+emoteCode+emoteValue+">");
            }
            if (!input.contains(":")) return input;
        }
        return input;
    }
    public static String replaceCaseInsensitive(String input, String replaceWhat, String replaceWith) {
        Pattern pattern = Pattern.compile(replaceWhat, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        String result = matcher.replaceAll(replaceWith);
        return result;
    }
    public static String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
