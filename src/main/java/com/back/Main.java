package com.back;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.File;

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
    public static void main(String[] args) throws IOException {
        //TIP 캐럿을 강조 표시된 텍스트에 놓고 <shortcut actionId="ShowIntentionActions"/>을(를) 누르면
        // IntelliJ IDEA이(가) 수정을 제안하는 것을 확인할 수 있습니다.
        System.out.println("== 명언 앱==");

        Scanner sc = new Scanner(System.in);
        int lastId = 1;
        String[] wisearr = new String[100];
        String[] authorarr = new String[100];
        //디렉토리생성
        File dir = new File("db/wiseSaying");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 2. [먼저] lastId.txt를 읽어서 lastId 값을 복원하기
        File lastIdFile = new File("db/wiseSaying/lastId.txt");
        if (lastIdFile.exists()) {
            String idStr = new String(Files.readAllBytes(Paths.get("db/wiseSaying/lastId.txt"))).trim();
            if (!idStr.isEmpty()) {
                lastId = Integer.parseInt(idStr) + 1;
            }
        }

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();

            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();

                    // 3. 파일 이름이 ".json"으로 끝나는 파일들만 선택
                    // (lastId.txt 같은 텍스트 파일은 자연스럽게 제외됨)
                    if (fileName.endsWith(".json")) {
                        String jsonContent = new String(Files.readAllBytes(file.toPath()));

                        int idIndex = jsonContent.indexOf("\"id\":");
                        int idEndIndex = jsonContent.indexOf(",", idIndex);
                        String idStr = jsonContent.substring(idIndex + 5, idEndIndex).trim();
                        int id = Integer.parseInt(idStr);

                        int contentIndex = jsonContent.indexOf("\"content\":");
                        int contentStartIndex = jsonContent.indexOf("\"", contentIndex + 10) + 1;
                        int contentEndIndex = jsonContent.indexOf("\"", contentStartIndex);
                        String content = jsonContent.substring(contentStartIndex, contentEndIndex);

                        int authorIndex = jsonContent.indexOf("\"author\":");
                        int authorStartIndex = jsonContent.indexOf("\"", authorIndex + 9) + 1;
                        int authorEndIndex = jsonContent.indexOf("\"", authorStartIndex);
                        String author = jsonContent.substring(authorStartIndex, authorEndIndex);

                        wisearr[id] = content;
                        authorarr[id] = author;
                    }
                }
            }
        }



        while (true) {
            System.out.print("명령) ");

            String cmd = sc.nextLine().trim();

            if(cmd.equals("종료")){
                break;
            }

            if(cmd.equals("등록")){
                System.out.print("명언 : ");
                String wise = sc.nextLine().trim();
                System.out.print("작가 : ");
                String author = sc.nextLine().trim();

                wisearr[lastId] = wise;
                authorarr[lastId] = author;

                //json
                String jsonStr = "{\n";
                jsonStr += "  \"id\": " + lastId + ",\n";
                jsonStr += "  \"content\": \"" + wise + "\",\n";
                jsonStr += "  \"author\": \"" + author + "\"\n";
                jsonStr += "}";

                FileWriter writer = new FileWriter("db/wiseSaying/" + lastId + ".json");
                writer.write(jsonStr);
                writer.close();

                FileWriter writer1 = new FileWriter("db/wiseSaying/lastId.txt");
                writer1.write(String.valueOf(lastId));
                writer1.close();

                System.out.println(lastId++ + "번 명령이 등록되었습니다.");
            }

            if(cmd.equals("목록")) {
                System.out.println("번호 / 작가 / 명언");
                System.out.println("------------------");

                for(int i = lastId -1; i>0; i--) {
                    if(wisearr[i] == null && authorarr[i] == null){
                        continue;
                    }
                    System.out.print(i+ " / ");
                    System.out.print(authorarr[i] + " / ");
                    System.out.println(wisearr[i]);
                }
            }
            if(cmd.startsWith("삭제?id=")){
                String idStr = cmd.substring(6);
                int targetId = Integer.parseInt(idStr);

                if(wisearr[targetId] == null && authorarr[targetId] == null){
                    System.out.println(targetId + "번 명언은 존재하지 않습니다.");
                }
                else {
                    wisearr[targetId] = null;
                    authorarr[targetId] = null;

                    String filePath = "db/wiseSaying/" + targetId + ".json";
                    File file = new File(filePath);

                    if (file.exists()) {
                        file.delete();
                    }

                    System.out.println(targetId + "번 명언이 삭제되었습니다.");
                }
            }
            if(cmd.startsWith("수정?id=")){
                String idStr = cmd.substring(6);
                int targetId = Integer.parseInt(idStr);

                if(wisearr[targetId] == null && authorarr[targetId] == null){
                    System.out.println(targetId + "번 명언은 존재하지 않습니다.");
                }
                else {
                    System.out.println("명언(기존) : " + wisearr[targetId]);
                    System.out.print("명언 : " + authorarr[targetId]);
                    String wise = sc.nextLine().trim();
                    System.out.println("작가(기존) : ");
                    System.out.print("명언 : ");
                    String author = sc.nextLine().trim();

                    wisearr[targetId] = wise;
                    authorarr[targetId] = author;

                    String filePath = "db/wiseSaying/" + targetId + ".json";
                    File file = new File(filePath);

                    if (file.exists()) {
                        String jsonStr = "{\n";
                        jsonStr += "  \"id\": " + lastId + ",\n";
                        jsonStr += "  \"content\": \"" + wise + "\",\n";
                        jsonStr += "  \"author\": \"" + author + "\"\n";
                        jsonStr += "}";

                        FileWriter writer = new FileWriter("db/wiseSaying/" + lastId + ".json");
                        writer.write(jsonStr);
                        writer.close();
                    }
                }
            }

            if(cmd.equals("빌드")){
                boolean isFirst = true;
                String Datajson = "[\n";
                for(int i=1; i < lastId; i++){
                    if(wisearr[i] == null && authorarr[i] == null){
                        continue;
                    }
                    if(!isFirst == true) {
                        Datajson += ",\n";
                    }
                    Datajson += "   {\n";
                    Datajson += "       \"id\": " + i + ",\n";
                    Datajson += "       \"content\": \"" + wisearr[i] + "\",\n";
                    Datajson += "       \"author\": \"" + authorarr[i] + "\"\n";
                    Datajson += "   }";
                    isFirst = false;
                }
                Datajson += "\n]";

                FileWriter writer = new FileWriter("db/wiseSaying/data.json");
                writer.write(Datajson);
                writer.close();

                System.out.println("data.json 파일의 내용이 갱신되었습니다.");
            }
        }
    }
}
