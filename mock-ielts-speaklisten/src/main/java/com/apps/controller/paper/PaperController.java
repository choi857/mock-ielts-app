package com.apps.controller.paper;

import com.apps.common.ResponseResult;
import com.apps.model.paper.Paper;
import com.apps.service.paper.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/papers")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @GetMapping("all")
    public List<Paper> getAllPapers() {
        return paperService.getAllPapers();
    }

    @GetMapping("/{id}")
    public Paper getPaperById(@PathVariable Long id) {
        return paperService.getPaperById(id);
    }

    @PostMapping("/add")
    public Paper createPaper(@RequestBody Paper paper) {
        return paperService.createPaper(paper);
    }
    @PostMapping("/update")
    public Paper updatePaper(@RequestBody Paper paper) {
        return paperService.updatePaper(paper);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseResult deletePaper(@PathVariable Long id) {
        paperService.deletePaper(id);
        return ResponseResult.success("删除成功");
    }


}
