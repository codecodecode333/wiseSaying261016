
package com.back;

import com.back.domain.wiseSaying.Rq;
import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("== 명언 앱==");

        Scanner sc = new Scanner(System.in);
        WiseSayingRepository repository = new WiseSayingRepository();

        int lastId = 1;

        // 디렉토리 생성
        File dir = new File("db/wiseSaying");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // lastId 복원
        File lastIdFile = new File("db/wiseSaying/lastId.txt");
        if (lastIdFile.exists()) {
            String idStr = new String(
                    Files.readAllBytes(Paths.get("db/wiseSaying/lastId.txt"))
            ).trim();

            if (!idStr.isEmpty()) {
                lastId = Integer.parseInt(idStr) + 1;
            }
        }

        // 저장된 명언 불러오기
        List<WiseSaying> wiseSayings = repository.findAll();

        while (true) {
            System.out.print("명령) ");

            String cmd = sc.nextLine().trim();
            Rq rq = new Rq(cmd);

            switch (rq.getActionName()) {
                case "종료" -> {
                    return;
                }

                case "등록" -> {
                    System.out.print("명언 : ");
                    String wise = sc.nextLine().trim();

                    System.out.print("작가 : ");
                    String author = sc.nextLine().trim();

                    WiseSaying ws = new WiseSaying(lastId, wise, author);
                    wiseSayings.add(ws);

                    repository.saveWiseSaying(ws, lastId);
                    repository.saveLastId(lastId);

                    System.out.println(lastId++ + "번 명언이 등록되었습니다.");
                }

                case "목록" -> {
                    System.out.println("번호 / 작가 / 명언");
                    System.out.println("------------------");

                    for (int i = wiseSayings.size() - 1; i >= 0; i--) {
                        WiseSaying ws = wiseSayings.get(i);

                        System.out.println(ws.getId() + " / "
                                + ws.getAuthor() + " / "
                                + ws.getContent());
                    }
                }

                case "삭제" -> {
                    int targetId = rq.getParamAsInt("id", -1);

                    WiseSaying ws = wiseSayings.stream()
                            .filter(w -> w.getId() == targetId)
                            .findFirst()
                            .orElse(null);

                    if (ws == null) {
                        System.out.println(targetId + "번 명언은 존재하지 않습니다.");
                    } else {
                        wiseSayings.remove(ws);
                        repository.deleteWiseSaying(targetId);

                        System.out.println(targetId + "번 명언이 삭제되었습니다.");
                    }
                }

                case "수정" -> {
                    int targetId = rq.getParamAsInt("id", -1);

                    WiseSaying ws = wiseSayings.stream()
                            .filter(w -> w.getId() == targetId)
                            .findFirst()
                            .orElse(null);

                    if (ws == null) {
                        System.out.println(targetId + "번 명언은 존재하지 않습니다.");
                    } else {
                        System.out.println("명언(기존) : " + ws.getContent());
                        System.out.print("명언 : ");
                        String wise = sc.nextLine().trim();

                        System.out.println("작가(기존) : " + ws.getAuthor());
                        System.out.print("작가 : ");
                        String author = sc.nextLine().trim();

                        ws.setContent(wise);
                        ws.setAuthor(author);

                        repository.saveWiseSaying(ws, lastId);
                    }
                }

                case "빌드" -> {
                    repository.saveDataJson();

                    System.out.println("data.json 파일의 내용이 갱신되었습니다.");
                }
            }
        }
    }
}
