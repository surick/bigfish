package com.github.surick.controller;

import cn.hutool.core.bean.BeanUtil;
import com.github.surick.model.Rss;
import com.github.surick.model.dto.RssAddDTO;
import com.github.surick.service.RssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jin
 * @since 2022/9/7
 */
@RestController
@RequestMapping("/rss")
public class RssController {

    @Autowired
    RssService rssService;

    @PostMapping("/saveOrUpdate")
    public ResponseEntity<Integer> saveOrUpdate(RssAddDTO dto) {
        Rss rss = new Rss();
        BeanUtil.copyProperties(dto, rss);
        rssService.saveOrUpdate(rss);

        return ResponseEntity.ok(rss.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(rssService.getById(id));
    }

    @PostMapping("/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(rssService.list());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(rssService.removeById(id));
    }
}
