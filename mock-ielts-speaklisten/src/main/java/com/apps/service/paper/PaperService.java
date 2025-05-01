package com.apps.service.paper;

import com.apps.model.paper.Paper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface PaperService {
    List<Paper> getAllPapers();

    Paper getPaperById(Long id);
    Paper createPaper(Paper paper);

    Paper updatePaper(Paper paper);
    void deletePaper(Long id);

}
