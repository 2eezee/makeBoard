package com.study.board.service;

import org.springframework.data.domain.Pageable;

import com.study.board.entity.Board;
import com.study.board.repository.BoardReposiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardReposiory boardReposiory;

    // 글 작성
    public void write(Board board, MultipartFile file) throws Exception {
        boardReposiory.save(board);

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);

        // 디렉토리가 존재하지 않으면 만듭니다.
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }

        file.transferTo(saveFile);

        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);

        boardReposiory.save(board);

    }

    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {

        return boardReposiory.findAll(pageable);
    }

    //  검색
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable){
        return boardReposiory.findByTitleContaining(searchKeyword, pageable);
    }

    // 특정 게시글 불러오기
    public Board boardView(Integer id) {
        return boardReposiory.findById(id).get();
    }

    // 특정 게시글 삭제하기
    public void boardDelete(Integer id) {

        boardReposiory.deleteById(id);
    }
}
