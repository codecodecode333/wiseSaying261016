package com.back.domain.wiseSaying.controller;

import com.back.Rq;
import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private final Scanner sc = new Scanner(System.in);
    private final WiseSayingService service;
    private final WiseSayingRepository repository
            = new WiseSayingRepository();

    public WiseSayingController() throws IOException {
        this.service = new WiseSayingService();
    }

    public void register() throws IOException {
        System.out.print("명언 : ");
        String content = sc.nextLine().trim();

        System.out.print("작가 : ");
        String author = sc.nextLine().trim();

        service.register(content, author);

        System.out.println(repository.findLastId() + "번 명언이 등록되었습니다.");
    }
    public void list(){
        System.out.println("번호 / 작가 / 명언");
        System.out.println("------------------");

        List<WiseSaying> wiseSayings = service.findAll();

        for (int i = wiseSayings.size() - 1; i >= 0; i--) {
            WiseSaying ws = wiseSayings.get(i);

            System.out.println(ws.getId() + " / "
                    + ws.getAuthor() + " / "
                    + ws.getContent());
        }
    }
    public void delete(Rq rq) {
        int targetId = rq.getParamAsInt("id", -1);

        boolean deleted = service.delete(targetId);

        if (deleted) {
            System.out.println(targetId + "번 명언이 삭제되었습니다.");
        } else {
            System.out.println(targetId + "번 명언은 존재하지 않습니다.");
        }
    }
    public void modify(Rq rq) throws IOException {
        int targetId = rq.getParamAsInt("id", -1);

        WiseSaying ws = service.findById(targetId);

        if (ws == null) {
            System.out.println(targetId + "번 명언은 존재하지 않습니다.");
            return;
        }

        System.out.println("명언(기존) : " + ws.getContent());
        System.out.print("명언 : ");
        String content = sc.nextLine().trim();

        System.out.println("작가(기존) : " + ws.getAuthor());
        System.out.print("작가 : ");
        String author = sc.nextLine().trim();

        service.modify(targetId, content, author);
    }
    public void build(){
        service.build();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}
