package com.apps.service.paper;

import com.apps.model.paper.Paper;
import com.apps.mapper.paper.PaperMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperServiceImpl implements PaperService {

    @Autowired
    private PaperMapper paperMapper;

    @Override
    public List<Paper> getAllPapers() {
        return paperMapper.getAllPapers();
    }

    @Override
    public Paper getPaperById(Long id) {
        return paperMapper.getPaperById(id);
    }

    @Override
    public Paper createPaper(Paper paper) {
        paperMapper.createPaper(paper);
        return paper;
    }

    @Override
    public Paper updatePaper(Paper paper) {
        paperMapper.updatePaper(paper);
        return paper;
    }

    @Override
    public void deletePaper(Long id) {
        paperMapper.deletePaper(id);
    }
}
