package com.back;

import com.back.domain.wiseSaying.controller.WiseSayingController;

import java.io.IOException;
import java.util.Scanner;

public class App {
    private final Scanner sc = new Scanner(System.in);
    private final WiseSayingController wiseSayingController;

    public App() throws IOException {
        wiseSayingController = new WiseSayingController();
    }

    public void run() throws IOException {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");

            String cmd = sc.nextLine().trim();
            Rq rq = new Rq(cmd);

            switch (rq.getActionName()) {
                case "등록" -> wiseSayingController.register();
                case "목록" -> wiseSayingController.list();
                case "삭제" -> wiseSayingController.delete(rq);
                case "수정" -> wiseSayingController.modify(rq);
                case "빌드" -> wiseSayingController.build();
                case "종료" -> {
                    return;
                }
                default -> System.out.println("알 수 없는 명령어입니다.");
            }
        }
    }
}
