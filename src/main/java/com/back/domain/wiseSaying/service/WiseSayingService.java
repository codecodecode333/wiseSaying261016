package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.io.IOException;
import java.util.List;

public class WiseSayingService {
    private final WiseSayingRepository repository
            = new WiseSayingRepository();
    private int lastId;
    private final List<WiseSaying> wiseSayings;

    public WiseSayingService() throws IOException {
        this.lastId = repository.findLastId() + 1;
        wiseSayings = repository.findAll();
    }

    public List<WiseSaying> findAll(){
        return List.copyOf(wiseSayings);
    }

    public WiseSaying findById(int targetId) {
        return wiseSayings.stream()
                .filter(ws -> ws.getId() == targetId)
                .findFirst()
                .orElse(null);
    }

    public WiseSaying register(String content, String author) throws IOException {
        WiseSaying ws = new WiseSaying(lastId, content, author);

        repository.saveWiseSaying(ws);
        repository.saveLastId(lastId++);

        wiseSayings.add(ws);

        return ws;
    }
    public boolean delete(int targetId) {
        boolean exists = wiseSayings.stream()
                .anyMatch(ws -> ws.getId() == targetId);

        if (!exists) {
            return false;
        }

        boolean deleted = repository.deleteWiseSaying(targetId);

        if (!deleted) {
            return false;
        }

        wiseSayings.removeIf(ws -> ws.getId() == targetId);

        return true;
    }
    public boolean modify(int targetId, String content, String author) throws IOException {
        WiseSaying ws = findById(targetId);

        if(ws == null) {
            return false;
        }

        ws.setContent(content);
        ws.setAuthor(author);

        repository.saveWiseSaying(ws);
        return true;
    }
    public void build(){
        repository.saveDataJson();
    }
}
