package com.github.surick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.surick.dao.RssMapper;
import com.github.surick.model.Rss;
import com.github.surick.service.RssService;
import org.springframework.stereotype.Service;

/**
 * @author Jin
 * @since 2022/9/7
 */
@Service
public class RssServiceImpl extends ServiceImpl<RssMapper, Rss> implements RssService {

}
