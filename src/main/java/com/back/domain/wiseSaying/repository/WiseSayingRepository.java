package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class WiseSayingRepository {
    private final String DB_PATH = "db/wiseSaying";

    public void saveWiseSaying(WiseSaying wiseSaying) throws IOException {
        File dir = new File(DB_PATH);
        if (!dir.exists()){
            dir.mkdirs();
        }

        String jsonStr = "{\n";
        jsonStr += "  \"id\": " + wiseSaying.getId() + ",\n";
        jsonStr += "  \"content\": \"" + wiseSaying.getContent() + "\",\n";
        jsonStr += "  \"author\": \"" + wiseSaying.getAuthor() + "\"\n";
        jsonStr += "}";

        try (FileWriter writer = new FileWriter(DB_PATH + "/" + wiseSaying.getId() + ".json")) {
            writer.write(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLastId(int lastId) {
        File dir = new File(DB_PATH);
        if (!dir.exists()){
            dir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(DB_PATH + "/lastId.txt")) {
            writer.write(String.valueOf(lastId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int findLastId() throws IOException {
        File file = new File(DB_PATH + "/lastId.txt");

        if (!file.exists()) {
            return 0;
        }

        String content = Files.readString(file.toPath()).trim();

        if (content.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(content);
    }

    public boolean deleteWiseSaying(int targetId){
        File file = new File("db/wiseSaying/" + targetId + ".json");
        if (!file.exists()) {
            return false; // 삭제 실패
        }
        return file.delete();
    }

    // WiseSayingRepository 클래스 안에 추가
    public List<WiseSaying> findAll() {
        List<WiseSaying> wiseSayings = new ArrayList<>();
        File dir = new File(DB_PATH);

        if (!dir.exists() || !dir.isDirectory()) {
            return wiseSayings;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            return wiseSayings;
        }

        for (File file : files) {
            String fileName = file.getName();

            if (fileName.matches("\\d+\\.json")) {
                try {
                    String jsonContent = new String(Files.readAllBytes(file.toPath()));

                    int idIndex = jsonContent.indexOf("\"id\":");
                    int idEndIndex = jsonContent.indexOf(",", idIndex);
                    int id = Integer.parseInt(jsonContent.substring(idIndex + 5, idEndIndex).trim());

                    int contentIndex = jsonContent.indexOf("\"content\":");
                    int contentStartIndex = jsonContent.indexOf("\"", contentIndex + 10) + 1;
                    int contentEndIndex = jsonContent.indexOf("\"", contentStartIndex);
                    String content = jsonContent.substring(contentStartIndex, contentEndIndex);

                    int authorIndex = jsonContent.indexOf("\"author\":");
                    int authorStartIndex = jsonContent.indexOf("\"", authorIndex + 9) + 1;
                    int authorEndIndex = jsonContent.indexOf("\"", authorStartIndex);
                    String author = jsonContent.substring(authorStartIndex, authorEndIndex);

                    wiseSayings.add(new WiseSaying(id, content, author));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return wiseSayings;
    }

    public void saveDataJson(){
        List<WiseSaying> wiseSayings = findAll();

        boolean isFirst = true;
        String Datajson = "[\n";
        for(int i=0; i < wiseSayings.size(); i++){
            WiseSaying ws = wiseSayings.get(i);
            if(!isFirst == true) {
                Datajson += ",\n";
            }
            Datajson += "   {\n";
            Datajson += "       \"id\": " + ws.getId() + ",\n";
            Datajson += "       \"content\": \"" + ws.getContent() + "\",\n";
            Datajson += "       \"author\": \"" + ws.getAuthor() + "\"\n";
            Datajson += "   }";
            isFirst = false;
        }
        Datajson += "\n]";

        try (FileWriter writer = new FileWriter(DB_PATH + "/data.json")) {
            writer.write(Datajson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
